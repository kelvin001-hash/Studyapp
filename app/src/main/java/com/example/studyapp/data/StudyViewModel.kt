package com.example.studyapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyapp.models.StudySession
import com.example.studyapp.models.Subject
import com.example.studyapp.models.Task
import com.example.studyapp.models.TimetableEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class StudyViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = StudyDatabase.getDatabase(application).studyDao()
    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    val subjects: StateFlow<List<Subject>> = dao.getAllSubjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tasks: StateFlow<List<Task>> = dao.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sessions: StateFlow<List<StudySession>> = dao.getAllStudySessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val timetableEntries: StateFlow<List<TimetableEntry>> = dao.getAllTimetableEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalStudyTimeToday: StateFlow<Int> = dao.getTotalStudyTimeToday(getStartOfDay())
        .map { it ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        seedDatabase()
    }

    private fun seedDatabase() {
        viewModelScope.launch {
            val currentSubjects = dao.getAllSubjects().first()
            val currentNames = currentSubjects.map { it.name }
            
            val colors = listOf(
                0xFF4CAF50, 0xFF8BC34A, 0xFF2196F3, 0xFF00BCD4, 
                0xFFFF9800, 0xFFF44336, 0xFF795548, 0xFF009688, 
                0xFF9C27B0, 0xFF607D8B
            )

            LibraryData.secondarySubjects.forEachIndexed { index, libSubject ->
                if (libSubject.name !in currentNames) {
                    val color = colors.getOrElse(index) { 0xFF9E9E9E }.toInt()
                    dao.insertSubject(Subject(name = libSubject.name, color = color))
                }
            }
        }
    }

    private fun getUserId(): String? = auth.currentUser?.uid

    private fun getStartOfDay(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun addSubject(name: String, color: Int) {
        viewModelScope.launch {
            val subject = Subject(name = name, color = color)
            val id = dao.insertSubject(subject).toInt()
            saveToFirebase("subjects", subject.copy(id = id))
        }
    }

    fun updateSubject(subject: Subject) {
        viewModelScope.launch {
            dao.updateSubject(subject)
            saveToFirebase("subjects", subject)
        }
    }

    fun updateSubjectScore(subjectId: Int, score: Int) {
        viewModelScope.launch {
            dao.updateSubjectScore(subjectId, score)
            // Also need to update the firebase version if possible, 
            // but dao.updateSubjectScore only updates one field.
            // For simplicity in this demo, we'll just update local and 
            // assume a full sync might happen later or update the whole object.
            val subject = subjects.value.find { it.id == subjectId }
            subject?.let {
                saveToFirebase("subjects", it.copy(score = score))
            }
        }
    }

    fun deleteSubject(subject: Subject) {
        viewModelScope.launch {
            dao.deleteSubject(subject)
            removeFromFirebase("subjects", subject.id.toString())
        }
    }

    fun addTask(title: String, subjectId: Int, deadline: Long) {
        viewModelScope.launch {
            val task = Task(title = title, subjectId = subjectId, deadline = deadline)
            val id = dao.insertTask(task).toInt()
            saveToFirebase("tasks", task.copy(id = id))
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            dao.updateTask(updatedTask)
            saveToFirebase("tasks", updatedTask)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            dao.deleteTask(task)
            removeFromFirebase("tasks", task.id.toString())
        }
    }

    fun addStudySession(subjectId: Int, durationMinutes: Int) {
        viewModelScope.launch {
            val session = StudySession(
                subjectId = subjectId,
                durationMinutes = durationMinutes,
                date = System.currentTimeMillis()
            )
            val id = dao.insertStudySession(session).toInt()
            saveToFirebase("study_sessions", session.copy(id = id))
        }
    }

    fun addTimetableEntry(subjectId: Int, dayOfWeek: Int, startHour: Int) {
        viewModelScope.launch {
            val entry = TimetableEntry(
                subjectId = subjectId,
                dayOfWeek = dayOfWeek,
                startHour = startHour
            )
            val id = dao.insertTimetableEntry(entry).toInt()
            saveToFirebase("timetable_entries", entry.copy(id = id))
        }
    }

    fun deleteTimetableEntryAt(dayOfWeek: Int, startHour: Int) {
        viewModelScope.launch {
            // Find the entry first to get its ID for Firebase
            val entryToDelete = timetableEntries.value.find { 
                it.dayOfWeek == dayOfWeek && it.startHour == startHour 
            }
            dao.deleteTimetableEntryAt(dayOfWeek, startHour)
            entryToDelete?.let {
                removeFromFirebase("timetable_entries", it.id.toString())
            }
        }
    }

    private fun saveToFirebase(node: String, data: Any) {
        val userId = getUserId() ?: return
        val id = when (data) {
            is Subject -> data.id.toString()
            is Task -> data.id.toString()
            is StudySession -> data.id.toString()
            is TimetableEntry -> data.id.toString()
            else -> null
        }
        if (id != null) {
            database.child("users").child(userId).child(node).child(id).setValue(data)
        }
    }

    private fun removeFromFirebase(node: String, id: String) {
        val userId = getUserId() ?: return
        database.child("users").child(userId).child(node).child(id).removeValue()
    }
}

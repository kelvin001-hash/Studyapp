package com.example.studyapp.data

import androidx.room.*
import com.example.studyapp.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {
    @Query("SELECT * FROM subjects")
    fun getAllSubjects(): Flow<List<Subject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: Subject): Long

    @Update
    suspend fun updateSubject(subject: Subject)

    @Query("UPDATE subjects SET score = :score WHERE id = :subjectId")
    suspend fun updateSubjectScore(subjectId: Int, score: Int)

    @Delete
    suspend fun deleteSubject(subject: Subject)

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM study_sessions")
    fun getAllStudySessions(): Flow<List<StudySession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudySession(session: StudySession): Long

    @Query("SELECT SUM(durationMinutes) FROM study_sessions WHERE date >= :startOfDay")
    fun getTotalStudyTimeToday(startOfDay: Long): Flow<Int?>

    @Query("SELECT * FROM timetable_entries")
    fun getAllTimetableEntries(): Flow<List<TimetableEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetableEntry(entry: TimetableEntry): Long

    @Delete
    suspend fun deleteTimetableEntry(entry: TimetableEntry)

    @Query("DELETE FROM timetable_entries WHERE dayOfWeek = :day AND startHour = :hour")
    suspend fun deleteTimetableEntryAt(day: Int, hour: Int)
}

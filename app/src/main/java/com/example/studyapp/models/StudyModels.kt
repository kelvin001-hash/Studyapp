package com.example.studyapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class Subject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val color: Int,
    val score: Int = 0 // Quiz score / 12 points scale
)

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: Int,
    val title: String,
    val deadline: Long, // Timestamp
    val isCompleted: Boolean = false
)

@Entity(tableName = "study_sessions")
data class StudySession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: Int,
    val date: Long, // Timestamp
    val durationMinutes: Int
)

@Entity(tableName = "timetable_entries")
data class TimetableEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: Int,
    val dayOfWeek: Int, // 0-6 (Mon-Sun) or 1-7
    val startHour: Int, // 0-23
    val durationHours: Int = 1
)

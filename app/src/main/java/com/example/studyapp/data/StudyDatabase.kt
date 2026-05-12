package com.example.studyapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studyapp.models.StudySession
import com.example.studyapp.models.Subject
import com.example.studyapp.models.Task
import com.example.studyapp.models.TimetableEntry

@Database(entities = [Subject::class, Task::class, StudySession::class, TimetableEntry::class], version = 3, exportSchema = false)
abstract class StudyDatabase : RoomDatabase() {
    abstract fun studyDao(): StudyDao

    companion object {
        @Volatile
        private var INSTANCE: StudyDatabase? = null

        fun getDatabase(context: Context): StudyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudyDatabase::class.java,
                    "study_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

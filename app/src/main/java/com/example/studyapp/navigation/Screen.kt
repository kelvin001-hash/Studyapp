package com.example.studyapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Login : Screen("login", "Login", Icons.Default.Lock)
    object Register : Screen("register", "Register", Icons.Default.PersonAdd)
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Timetable : Screen("timetable", "Timetable", Icons.Default.DateRange)
    object Timer : Screen("timer", "Timer", Icons.Default.PlayArrow)
    object Tasks : Screen("tasks", "Tasks", Icons.AutoMirrored.Filled.List)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object Library : Screen("library", "Library", Icons.Default.Book)
    object Revision : Screen("revision", "Revision", Icons.AutoMirrored.Filled.Assignment)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    
    // Nested routes
    object Topics : Screen("topics/{subjectName}", "Topics", Icons.Default.Book)
    object NoteDetail : Screen("note/{subjectName}/{topicTitle}", "Notes", Icons.Default.Book)
}

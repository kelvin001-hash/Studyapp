package com.example.studyapp.models

data class LibrarySubject(
    val name: String,
    val icon: String, // String representation of icon or emoji
    val description: String,
    val topics: List<Topic> = emptyList()
)

data class Topic(
    val title: String,
    val content: String,
    val chapters: List<String> = emptyList()
)

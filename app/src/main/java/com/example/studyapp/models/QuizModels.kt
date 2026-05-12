package com.example.studyapp.models

data class Quiz(
    val subjectName: String,
    val questions: List<Question>
)

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

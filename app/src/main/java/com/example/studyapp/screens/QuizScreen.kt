package com.example.studyapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.studyapp.data.QuizData
import com.example.studyapp.data.StudyViewModel
import com.example.studyapp.models.Question
import com.example.studyapp.models.Quiz

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(navController: NavController, viewModel: StudyViewModel, subjectName: String?) {
    val quiz = QuizData.subjectQuizzes[subjectName]
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswered by remember { mutableStateOf(false) }
    var quizCompleted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(subjectName ?: "Quiz") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (quiz == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No quiz available for $subjectName")
            }
        } else if (quizCompleted) {
            QuizResult(
                score = score,
                total = quiz.questions.size,
                subjectName = subjectName!!,
                onFinish = {
                    // Update score in ViewModel (scale to 12 points)
                    val points = (score.toFloat() / quiz.questions.size * 12).toInt()
                    val subjectId = viewModel.subjects.value.find { it.name == subjectName }?.id
                    if (subjectId != null) {
                        viewModel.updateSubjectScore(subjectId, points)
                    }
                    navController.popBackStack()
                }
            )
        } else {
            val question = quiz.questions[currentQuestionIndex]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    LinearProgressIndicator(
                        progress = (currentQuestionIndex + 1).toFloat() / quiz.questions.size,
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Question ${currentQuestionIndex + 1} of ${quiz.questions.size}",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Gray
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = question.text,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(24.dp))

                    question.options.forEachIndexed { index, option ->
                        OptionCard(
                            text = option,
                            isSelected = selectedOptionIndex == index,
                            isCorrect = index == question.correctAnswerIndex,
                            isAnswered = isAnswered,
                            onClick = {
                                if (!isAnswered) {
                                    selectedOptionIndex = index
                                }
                            }
                        )
                    }
                }

                if (isAnswered) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = if (selectedOptionIndex == question.correctAnswerIndex) 
                                    Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = if (selectedOptionIndex == question.correctAnswerIndex) "Correct!" else "Wrong Answer",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedOptionIndex == question.correctAnswerIndex) 
                                Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                        Text(text = question.explanation, fontSize = 14.sp)
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (currentQuestionIndex < quiz.questions.size - 1) {
                                    currentQuestionIndex++
                                    selectedOptionIndex = null
                                    isAnswered = false
                                } else {
                                    quizCompleted = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (currentQuestionIndex < quiz.questions.size - 1) "Next Question" else "See Results")
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            if (selectedOptionIndex != null) {
                                isAnswered = true
                                if (selectedOptionIndex == question.correctAnswerIndex) {
                                    score++
                                }
                            }
                        },
                        enabled = selectedOptionIndex != null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Check Answer")
                    }
                }
            }
        }
    }
}

@Composable
fun OptionCard(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isAnswered: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isAnswered && isCorrect -> Color(0xFFC8E6C9)
        isAnswered && isSelected && !isCorrect -> Color(0xFFFFCDD2)
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }
    
    val borderColor = when {
        isAnswered && isCorrect -> Color(0xFF4CAF50)
        isAnswered && isSelected && !isCorrect -> Color(0xFFF44336)
        isSelected -> MaterialTheme.colorScheme.primary
        else -> Color.LightGray
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(enabled = !isAnswered, onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            if (isAnswered && isCorrect) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50))
            }
        }
    }
}

@Composable
fun QuizResult(score: Int, total: Int, subjectName: String, onFinish: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Quiz Completed!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Text("You scored", style = MaterialTheme.typography.bodyLarge)
        Text(
            text = "$score / $total",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        val percentage = (score.toFloat() / total * 100).toInt()
        Text("($percentage%)", style = MaterialTheme.typography.headlineSmall)
        
        Spacer(Modifier.height(32.dp))
        
        val meanPoints = (score.toFloat() / total * 12).toInt()
        val grade = when (meanPoints) {
            12 -> "A"
            11 -> "A-"
            10 -> "B+"
            9 -> "B"
            8 -> "B-"
            7 -> "C+"
            6 -> "C"
            5 -> "C-"
            4 -> "D+"
            3 -> "D"
            2 -> "D-"
            else -> "E"
        }
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Performance for $subjectName")
                Text("Mean Points: $meanPoints", fontWeight = FontWeight.Bold)
                Text("Grade: $grade", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
        }
        
        Spacer(Modifier.height(48.dp))
        Button(
            onClick = onFinish,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Update Profile & Exit")
        }
    }
}

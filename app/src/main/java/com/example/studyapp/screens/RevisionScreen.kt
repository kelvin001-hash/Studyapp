package com.example.studyapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyapp.data.StudyViewModel
import com.example.studyapp.models.Subject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevisionScreen(
    viewModel: StudyViewModel,
    onNavigateToQuiz: (String) -> Unit
) {
    val subjects by viewModel.subjects.collectAsState()
    
    val meanPoints = if (subjects.isNotEmpty()) {
        val total = subjects.sumOf { it.score }.toFloat()
        if (total == 0f) null else total / subjects.size
    } else null
    
    val meanGrade = meanPoints?.let { getGrade(it.toInt()) } ?: "N/A"

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Revision Center", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Overall Performance", style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = "Grade: $meanGrade",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = meanPoints?.let { "Mean Points: %.1f".format(it) } ?: "No revision data",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Icon(
                            Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Subject Progress",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(subjects) { subject ->
                RevisionSubjectCard(
                    subject = subject,
                    onTakeQuiz = { onNavigateToQuiz(subject.name) }
                )
            }
        }
    }
}

@Composable
fun RevisionSubjectCard(subject: Subject, onTakeQuiz: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = subject.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (subject.score > 0) 
                            "Score: ${subject.score}/12 (Grade: ${getGrade(subject.score)})"
                            else "No score yet",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Button(
                    onClick = onTakeQuiz,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Quiz", fontSize = 12.sp)
                }
            }
            
            Spacer(Modifier.height(12.dp))
            
            LinearProgressIndicator(
                progress = { subject.score / 12f },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = getScoreColor(subject.score),
                trackColor = Color.LightGray.copy(alpha = 0.3f),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        }
    }
}

fun getGrade(points: Int): String {
    return when (points) {
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
        1 -> "E"
        else -> "N/A"
    }
}

fun getScoreColor(points: Int): Color {
    return when {
        points >= 10 -> Color(0xFF4CAF50) // Green
        points >= 7 -> Color(0xFF8BC34A)  // Light Green
        points >= 5 -> Color(0xFFFFC107)  // Amber
        points >= 3 -> Color(0xFFFF9800)  // Orange
        else -> Color(0xFFF44336)         // Red
    }
}

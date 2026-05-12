package com.example.studyapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyapp.models.Topic
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    topic: Topic,
    onBackClick: () -> Unit,
    onTakeQuiz: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var showSummary by remember { mutableStateOf(false) }
    var isGenerating by remember { mutableStateOf(false) }
    var aiSummary by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Notes", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            isGenerating = true
                            showSummary = true
                            // Simulate AI processing
                            delay(1500)
                            aiSummary = generateSummary(topic.content)
                            isGenerating = false
                        }
                    }) {
                        Icon(
                            Icons.Default.AutoAwesome, 
                            contentDescription = "AI Summary",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { /* Share functionality */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = topic.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            // AI Summary Section
            if (showSummary) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AI Study Assistant",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (isGenerating) {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth().height(2.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "Generating key takeaways...",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        } else {
                            Text(
                                text = aiSummary,
                                style = MaterialTheme.typography.bodyMedium,
                                lineHeight = 20.sp
                            )
                            TextButton(
                                onClick = { showSummary = false },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Dismiss")
                            }
                        }
                    }
                }
            }
            
            // Content with specialized formatting for diagrams and workouts
            topic.content.split("\n\n").forEach { block ->
                if (block.startsWith("--- Diagram:") || block.startsWith("--- Workout:")) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = block,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp
                            ),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                } else if (block.contains("Example:") || block.contains("Solve:")) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = block,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                } else {
                    Text(
                        text = block,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 28.sp,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Fixed the Quick Summary section at the bottom
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Quick Tips",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Focus on the diagrams and worked examples for better understanding of science and math concepts.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onTakeQuiz,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Test Your Knowledge (Quiz)")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun generateSummary(content: String): String {
    // A simple deterministic "AI" summarizer for demo purposes
    val lines = content.lines()
    val keyPoints = lines.filter { it.contains(":") || it.contains("=") }
        .take(4)
        .joinToString("\n") { "• " + it.substringBefore("\n").trim() }
    
    return if (keyPoints.isNotBlank()) {
        "Based on these notes, here are the key takeaways:\n$keyPoints"
    } else {
        "This topic covers the fundamental principles of the subject. Focus on understanding the core definitions and practical applications discussed in the text."
    }
}

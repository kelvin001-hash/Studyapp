package com.example.studyapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyapp.data.AuthViewModel
import com.example.studyapp.data.StudyViewModel

@Composable
fun HomeScreen(
    viewModel: StudyViewModel,
    authViewModel: AuthViewModel = viewModel(),
    onLogout: () -> Unit
) {
    val totalTime by viewModel.totalStudyTimeToday.collectAsState()
    val sessions by viewModel.sessions.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val user by authViewModel.user.collectAsState()
    var showAddSubjectDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddSubjectDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("New Subject") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    val userName = user?.email?.substringBefore("@") ?: "Student"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Hello, $userName!",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Ready for some focused studying?",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Timer, 
                                        contentDescription = null, 
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.padding(4.dp).size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Today's Progress",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "$totalTime / 120 minutes",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            LinearProgressIndicator(
                                progress = { (totalTime / 120f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(12.dp),
                                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Quick Stats",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Subjects",
                            value = subjects.size.toString(),
                            icon = Icons.Default.Book,
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Sessions",
                            value = sessions.size.toString(),
                            icon = Icons.Default.CheckCircle,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    }
                }

                item {
                    Text(
                        text = "Recent Activity",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                if (sessions.isEmpty()) {
                    item {
                        EmptyState(
                            message = "No study sessions yet. Start the timer to begin!",
                            icon = Icons.Default.Timer
                        )
                    }
                } else {
                    items(sessions.reversed().take(5)) { session ->
                        val subjectName = subjects.find { it.id == session.subjectId }?.name ?: "Unknown"
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            ListItem(
                                headlineContent = { Text(subjectName, fontWeight = FontWeight.Bold) },
                                supportingContent = { Text("${session.durationMinutes} minutes focused") },
                                leadingContent = {
                                    Surface(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = CircleShape,
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(20.dp))
                                        }
                                    }
                                },
                                colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
                            )
                        }
                    }
                }
            }

            if (showAddSubjectDialog) {
                AddSubjectDialog(
                    onDismiss = { showAddSubjectDialog = false },
                    onAdd = { name ->
                        viewModel.addSubject(name, 0)
                        showAddSubjectDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    containerColor: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.onSecondaryContainer)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(text = title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun EmptyState(message: String, icon: ImageVector) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            shape = CircleShape,
            modifier = Modifier.size(100.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun AddSubjectDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Subject") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Subject Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onAdd(name) },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

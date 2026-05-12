package com.example.studyapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyapp.data.StudyViewModel
import com.example.studyapp.models.Subject
import com.example.studyapp.models.Task
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TasksScreen(viewModel: StudyViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("New Task") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Assignments",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Don't miss any deadlines",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            if (tasks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    EmptyState(
                        message = "Your task list is empty. Focus on what's important by adding tasks.",
                        icon = Icons.AutoMirrored.Filled.Assignment
                    )
                }
            } else {
                val groupedTasks = tasks.sortedBy { it.isCompleted }
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(groupedTasks) { task ->
                        val subject = subjects.find { it.id == task.subjectId }
                        TaskItem(
                            task = task,
                            subject = subject,
                            onCheckedChange = { viewModel.toggleTaskCompletion(task) },
                            onDelete = { viewModel.deleteTask(task) }
                        )
                    }
                }
            }
        }
        
        if (showAddDialog) {
            AddTaskDialog(
                subjects = subjects,
                onDismiss = { showAddDialog = false },
                onAdd = { title, subjectId, deadline ->
                    viewModel.addTask(title, subjectId, deadline)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task, 
    subject: Subject?,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val isOverdue = !task.isCompleted && task.deadline < System.currentTimeMillis()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) 
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) 
            else 
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (task.isCompleted) 0.dp else 2.dp),
        border = if (isOverdue) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)) else null
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted, 
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                if (subject != null) {
                    Surface(
                        color = Color(subject.color).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = subject.name,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            color = Color(subject.color),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
                
                Text(
                    text = task.title,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
                
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    Icon(
                        Icons.Default.CalendarToday, 
                        contentDescription = null, 
                        modifier = Modifier.size(12.dp),
                        tint = if (isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isOverdue) "Overdue • ${sdf.format(Date(task.deadline))}" else "Due ${sdf.format(Date(task.deadline))}",
                        color = if (isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete, 
                    contentDescription = "Delete", 
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun AddTaskDialog(
    subjects: List<Subject>,
    onDismiss: () -> Unit, 
    onAdd: (String, Int, Long) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedSubjectId by remember { mutableStateOf(if (subjects.isNotEmpty()) subjects[0].id else 0) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Task", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = title, 
                    onValueChange = { title = it }, 
                    label = { Text("What needs to be done?") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                
                if (subjects.isNotEmpty()) {
                    Column {
                        Text("Category", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(subjects) { subject ->
                                FilterChip(
                                    selected = selectedSubjectId == subject.id,
                                    onClick = { selectedSubjectId = subject.id },
                                    label = { Text(subject.name) },
                                    leadingIcon = if (selectedSubjectId == subject.id) {
                                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                                    } else null,
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (title.isNotBlank()) onAdd(title, selectedSubjectId, System.currentTimeMillis() + 86400000) },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Add Task")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

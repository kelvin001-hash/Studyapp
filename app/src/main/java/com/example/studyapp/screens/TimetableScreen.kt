package com.example.studyapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyapp.data.StudyViewModel
import com.example.studyapp.models.Subject
import com.example.studyapp.models.TimetableEntry

@Composable
fun TimetableScreen(viewModel: StudyViewModel) {
    val subjects by viewModel.subjects.collectAsState()
    val entries by viewModel.timetableEntries.collectAsState()
    
    var showAddSubjectDialog by remember { mutableStateOf(false) }
    var subjectToEdit by remember { mutableStateOf<Subject?>(null) }
    var subjectToSchedule by remember { mutableStateOf<Subject?>(null) }
    
    var showScheduleDialog by remember { mutableStateOf<Pair<Int, Int>?>(null) } // Day, Hour
    
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val timeSlots = (8..18).toList()

    val scheduleMap = remember(entries, subjects) {
        entries.associate { entry ->
            (entry.dayOfWeek to entry.startHour) to subjects.find { it.id == entry.subjectId }
        }
    }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Class Schedule",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Tap a slot to add or remove a subject",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Subjects List
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Subjects",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (subjects.isNotEmpty()) {
                    Text(
                        text = "${subjects.size} total",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            if (subjects.isEmpty()) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "No subjects added yet. Add one to start scheduling.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(subjects) { subject ->
                        SubjectBadge(
                            subject = subject,
                            onEdit = { subjectToEdit = subject },
                            onDelete = { viewModel.deleteSubject(subject) },
                            onSchedule = { subjectToSchedule = subject }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    // Days Header
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Spacer(modifier = Modifier.width(44.dp))
                        days.forEach { day ->
                            Text(
                                text = day,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    // Grid content
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        timeSlots.forEach { hour ->
                            // Time slot label
                            item {
                                Box(
                                    modifier = Modifier
                                        .height(64.dp)
                                        .padding(end = 4.dp),
                                    contentAlignment = Alignment.TopEnd
                                ) {
                                    Text(
                                        text = "$hour:00",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }

                            // Slots for each day
                            items(6) { dayIndex ->
                                val scheduledSubject = scheduleMap[dayIndex to hour]
                                
                                Box(
                                    modifier = Modifier
                                        .padding(1.dp)
                                        .height(64.dp)
                                        .background(
                                            color = scheduledSubject?.let { Color(it.color).copy(alpha = 0.25f) } 
                                                ?: MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .clickable { 
                                            if (scheduledSubject == null) {
                                                showScheduleDialog = dayIndex to hour 
                                            } else {
                                                viewModel.deleteTimetableEntryAt(dayIndex, hour)
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (scheduledSubject != null) {
                                        Text(
                                            text = scheduledSubject.name,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.padding(2.dp),
                                            color = Color(scheduledSubject.color)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialogs ... (Keeping the logic same but enhancing UI if needed)
    if (showAddSubjectDialog) {
        EnhancedSubjectDialog(
            onDismiss = { showAddSubjectDialog = false },
            onConfirm = { name, color ->
                viewModel.addSubject(name, color)
                showAddSubjectDialog = false
            }
        )
    }

    subjectToEdit?.let { subject ->
        EnhancedSubjectDialog(
            subject = subject,
            onDismiss = { subjectToEdit = null },
            onConfirm = { name, color ->
                viewModel.updateSubject(subject.copy(name = name, color = color))
                subjectToEdit = null
            }
        )
    }

    subjectToSchedule?.let { subject ->
        EnhancedScheduleTimeDialog(
            subject = subject,
            days = days,
            timeSlots = timeSlots,
            onDismiss = { subjectToSchedule = null },
            onConfirm = { dayIndex, hour ->
                viewModel.addTimetableEntry(subject.id, dayIndex, hour)
                subjectToSchedule = null
            }
        )
    }

    showScheduleDialog?.let { (day, hour) ->
        EnhancedScheduleSubjectDialog(
            subjects = subjects,
            onDismiss = { showScheduleDialog = null },
            onSelect = { subjectId ->
                viewModel.addTimetableEntry(subjectId, day, hour)
                showScheduleDialog = null
            }
        )
    }
}

@Composable
fun SubjectBadge(
    subject: Subject,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSchedule: () -> Unit
) {
    Surface(
        color = Color(subject.color).copy(alpha = 0.15f),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(subject.color).copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(modifier = Modifier.size(8.dp).background(Color(subject.color), CircleShape))
            Text(
                text = subject.name,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Color(subject.color)
            )
            IconButton(onClick = onSchedule, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp))
            }
            IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun EnhancedSubjectDialog(
    subject: Subject? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit
) {
    var name by remember { mutableStateOf(subject?.name ?: "") }
    var selectedColor by remember { mutableStateOf(if (subject != null) Color(subject.color) else Color(0xFF2196F3)) }

    val colors = listOf(
        Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF673AB7), 
        Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF00BCD4),
        Color(0xFF009688), Color(0xFF4CAF50), Color(0xFF8BC34A),
        Color(0xFFFFC107), Color(0xFFFF9800), Color(0xFFFF5722)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (subject == null) "New Subject" else "Edit Subject", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Subject Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Text("Theme Color", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    colors.forEach { color ->
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            color = color,
                            onClick = { selectedColor = color },
                            border = if (selectedColor == color) 
                                androidx.compose.foundation.BorderStroke(3.dp, MaterialTheme.colorScheme.outline) 
                            else null
                        ) {
                            if (selectedColor == color) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.padding(6.dp))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onConfirm(name, selectedColor.toArgb()) },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EnhancedScheduleTimeDialog(
    subject: Subject,
    days: List<String>,
    timeSlots: List<Int>,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var selectedDay by remember { mutableStateOf(0) }
    var selectedHour by remember { mutableStateOf(timeSlots.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Schedule ${subject.name}", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Day", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    days.forEachIndexed { index, day ->
                        FilterChip(
                            selected = selectedDay == index,
                            onClick = { selectedDay = index },
                            label = { Text(day) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
                
                Text("Time", style = MaterialTheme.typography.labelLarge)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(timeSlots) { hour ->
                        FilterChip(
                            selected = selectedHour == hour,
                            onClick = { selectedHour = hour },
                            label = { Text("$hour:00") },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(selectedDay, selectedHour) }, shape = RoundedCornerShape(12.dp)) {
                Text("Schedule")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun EnhancedScheduleSubjectDialog(
    subjects: List<Subject>,
    onDismiss: () -> Unit,
    onSelect: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pick a Subject", fontWeight = FontWeight.Bold) },
        text = {
            if (subjects.isEmpty()) {
                Text("Please add subjects first.")
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    subjects.forEach { subject ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(subject.id) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(subject.color).copy(alpha = 0.1f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(12.dp).background(Color(subject.color), CircleShape))
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(subject.name, fontWeight = FontWeight.Bold, color = Color(subject.color))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Dismiss") }
        }
    )
}

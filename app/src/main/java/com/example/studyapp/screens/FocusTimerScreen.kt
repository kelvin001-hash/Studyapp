package com.example.studyapp.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.studyapp.data.StudyViewModel

@Composable
fun FocusTimerScreen(viewModel: StudyViewModel) {
    var customMinutes by rememberSaveable { mutableIntStateOf(25) }
    var timeLeft by rememberSaveable { mutableIntStateOf(customMinutes * 60) }
    var isRunning by rememberSaveable { mutableStateOf(false) }
    val subjects by viewModel.subjects.collectAsState()
    var selectedSubjectId by rememberSaveable { mutableIntStateOf(if (subjects.isNotEmpty()) subjects[0].id else 0) }
    var showEditDialog by remember { mutableStateOf(false) }

    val totalTimeSeconds = customMinutes * 60

    val progress by animateFloatAsState(
        targetValue = if (totalTimeSeconds > 0) timeLeft.toFloat() / totalTimeSeconds else 0f,
        label = "Timer Progress"
    )

    LaunchedEffect(customMinutes) {
        if (!isRunning) {
            timeLeft = customMinutes * 60
        }
    }

    LaunchedEffect(isRunning) {
        while (isRunning && timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
        if (timeLeft == 0 && isRunning) {
            isRunning = false
            viewModel.addStudySession(selectedSubjectId, customMinutes)
            timeLeft = customMinutes * 60
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Focus Session",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Put away distractions and dive deep",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(300.dp)
        ) {
            val primaryColor = MaterialTheme.colorScheme.primary
            val trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            
            Canvas(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                drawCircle(
                    color = trackColor,
                    style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = primaryColor,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(enabled = !isRunning) { showEditDialog = true }
                ) {
                    Text(
                        text = formatTime(timeLeft),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 72.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-2).sp
                        )
                    )
                    if (!isRunning) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Time",
                            modifier = Modifier.padding(start = 8.dp).size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Surface(
                    color = if (isRunning) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isRunning) "FOCUSING" else "READY",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = if (isRunning) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (subjects.isNotEmpty()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Select a subject to focus on",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(subjects) { subject ->
                        FilterChip(
                            selected = selectedSubjectId == subject.id,
                            onClick = { if (!isRunning) selectedSubjectId = subject.id },
                            label = { Text(subject.name) },
                            shape = RoundedCornerShape(16.dp),
                            leadingIcon = if (selectedSubjectId == subject.id) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            enabled = !isRunning
                        )
                    }
                }
            }
        } else {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Add subjects in the Home or Timetable screen to track your progress.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                onClick = {
                    isRunning = false
                    timeLeft = customMinutes * 60
                },
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(28.dp))
                }
            }
            
            Button(
                onClick = { isRunning = !isRunning },
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isRunning) "Pause Session" else "Start Session",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    if (showEditDialog) {
        EditTimerDialog(
            initialMinutes = customMinutes,
            onDismiss = { showEditDialog = false },
            onConfirm = { 
                customMinutes = it
                showEditDialog = false
            }
        )
    }
}

@Composable
fun EditTimerDialog(
    initialMinutes: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var minutesText by remember { mutableStateOf(initialMinutes.toString()) }
    val minutes = minutesText.toIntOrNull() ?: 0
    val isValid = minutes in 1..1440

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Focus Duration") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    "Enter focus time in minutes (max 24 hrs / 1440 mins)",
                    style = MaterialTheme.typography.bodySmall
                )
                
                OutlinedTextField(
                    value = minutesText,
                    onValueChange = { 
                        if (it.isEmpty() || (it.all { char -> char.isDigit() } && it.length <= 4)) {
                            minutesText = it
                        }
                    },
                    label = { Text("Minutes") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isValid && minutesText.isNotEmpty(),
                    suffix = { Text("min") }
                )

                if (minutes > 0) {
                    val hours = minutes / 60
                    val mins = minutes % 60
                    Text(
                        text = if (hours > 0) "Total Duration: ${hours}h ${mins}m" else "Total Duration: ${mins}m",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }

                // Quick Select Chips
                Text("Quick Select", style = MaterialTheme.typography.labelMedium)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf(15, 25, 45, 60, 90, 120).forEach { preset ->
                        AssistChip(
                            onClick = { minutesText = preset.toString() },
                            label = { Text("${preset}m") }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(minutes) },
                enabled = isValid
            ) {
                Text("Set Timer")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun formatTime(seconds: Int): String {
    val totalSeconds = seconds
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val secs = totalSeconds % 60
    return if (hours > 0) {
        "%02d:%02d:%02d".format(hours, minutes, secs)
    } else {
        "%02d:%02d".format(minutes, secs)
    }
}

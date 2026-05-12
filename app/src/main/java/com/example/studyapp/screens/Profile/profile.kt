package com.example.studyapp.screens.Profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyapp.data.AuthViewModel
import com.example.studyapp.data.StudyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentProfileScreen(
    authViewModel: AuthViewModel = viewModel(),
    studyViewModel: StudyViewModel = viewModel(),
    onLogout: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val user by authViewModel.user.collectAsState()
    val subjects by studyViewModel.subjects.collectAsState(initial = emptyList())
    
    val meanPoints = if (subjects.isNotEmpty()) {
        val total = subjects.sumOf { it.score }.toFloat()
        if (total == 0f) null else total / subjects.size
    } else null
    
    val meanGrade = when (meanPoints?.toInt()) {
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

    var goals by remember { mutableStateOf("Improve Mathematics Mean Points") }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "My Profile",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                    IconButton(onClick = {
                        authViewModel.logout()
                        onLogout()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Header
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = user?.email?.take(1)?.uppercase() ?: "S",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val userName = user?.email?.substringBefore("@") ?: "Student"
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = user?.email ?: "student@example.com",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Mean Points",
                    value = meanPoints?.let { "%.1f".format(it) } ?: "N/A",
                    icon = Icons.Default.Star,
                    color = Color(0xFFFFD700)
                )
                ProfileStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Grade",
                    value = meanGrade,
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    color = Color(0xFF4CAF50)
                )
            }

            // Information Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "School Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    ProfileDetailItem(icon = Icons.Default.School, label = "Institution", value = "Nairobi School")
                    ProfileDetailItem(icon = Icons.Default.Badge, label = "Student ID", value = "NS-2024-089")
                    ProfileDetailItem(icon = Icons.Default.Email, label = "Contact Email", value = user?.email ?: "N/A")
                }
            }

            // Goals Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Learning Goals",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                    }

                    OutlinedTextField(
                        value = goals,
                        onValueChange = { goals = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ProfileStatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = label, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun ProfileDetailItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.padding(8.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
    }
}

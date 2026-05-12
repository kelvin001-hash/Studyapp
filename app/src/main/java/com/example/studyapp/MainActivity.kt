package com.example.studyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.studyapp.data.AuthViewModel
import com.example.studyapp.data.LibraryData
import com.example.studyapp.data.StudyViewModel
import com.example.studyapp.navigation.Screen
import com.example.studyapp.screens.*
import com.example.studyapp.screens.Profile.StudentProfileScreen
import com.example.studyapp.screens.Settings.SettingsScreen
import com.example.studyapp.ui.theme.StudyappTheme

class MainActivity : ComponentActivity() {
    private var keepSplashScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { keepSplashScreen }
        
        // Simulating a small delay for branding
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            keepSplashScreen = false
        }, 1000)

        enableEdgeToEdge()
        setContent {
            StudyappTheme {
                val navController = rememberNavController()
                val viewModel: StudyViewModel = viewModel()
                val authViewModel: AuthViewModel = viewModel()
                val user by authViewModel.user.collectAsState()
                
                val items = listOf(
                    Screen.Home,
                    Screen.Library,
                    Screen.Revision,
                    Screen.Timetable,
                    Screen.Timer,
                    Screen.Tasks,
                    Screen.Profile
                )

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val showBottomBar = currentDestination?.route in items.map { it.route }

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar {
                                items.forEach { screen ->
                                    NavigationBarItem(
                                        icon = { Icon(screen.icon, contentDescription = null) },
                                        label = { Text(screen.title) },
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (user == null) Screen.Login.route else Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Login.route) {
                            LoginScreen(
                                viewModel = authViewModel,
                                onLoginSuccess = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = {
                                    navController.navigate(Screen.Register.route)
                                }
                            )
                        }
                        composable(Screen.Register.route) {
                            RegisterScreen(
                                viewModel = authViewModel,
                                onRegisterSuccess = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                },
                                onNavigateToLogin = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(Screen.Home.route) { 
                            HomeScreen(
                                viewModel = viewModel,
                                onLogout = {
                                    authViewModel.logout()
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(0)
                                    }
                                }
                            ) 
                        }
                        composable(Screen.Library.route) {
                            LibraryScreen(
                                onSubjectClick = { subject ->
                                    navController.navigate("topics/${subject.name}")
                                }
                            )
                        }
                        composable(Screen.Revision.route) {
                            RevisionScreen(
                                viewModel = viewModel,
                                onNavigateToQuiz = { subjectName ->
                                    navController.navigate("quiz/$subjectName")
                                }
                            )
                        }
                        composable("topics/{subjectName}") { backStackEntry ->
                            val subjectName = backStackEntry.arguments?.getString("subjectName")
                            val subject = LibraryData.secondarySubjects.find { it.name == subjectName }
                            if (subject != null) {
                                TopicsScreen(
                                    subject = subject,
                                    onBackClick = { navController.popBackStack() },
                                    onTopicClick = { topic ->
                                        navController.navigate("note/${subject.name}/${topic.title}")
                                    }
                                )
                            }
                        }
                        composable("note/{subjectName}/{topicTitle}") { backStackEntry ->
                            val subjectName = backStackEntry.arguments?.getString("subjectName")
                            val topicTitle = backStackEntry.arguments?.getString("topicTitle")
                            val subject = LibraryData.secondarySubjects.find { it.name == subjectName }
                            val topic = subject?.topics?.find { it.title == topicTitle }
                            if (topic != null) {
                                NoteDetailScreen(
                                    topic = topic,
                                    onBackClick = { navController.popBackStack() },
                                    onTakeQuiz = {
                                        navController.navigate("quiz/$subjectName")
                                    }
                                )
                            }
                        }
                        composable("quiz/{subjectName}") { backStackEntry ->
                            val subjectName = backStackEntry.arguments?.getString("subjectName")
                            QuizScreen(navController, viewModel, subjectName)
                        }
                        composable(Screen.Timetable.route) { TimetableScreen(viewModel) }
                        composable(Screen.Timer.route) { FocusTimerScreen(viewModel) }
                        composable(Screen.Tasks.route) { TasksScreen(viewModel) }
                        composable(Screen.Profile.route) { 
                            StudentProfileScreen(
                                onLogout = {
                                    authViewModel.logout()
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(0)
                                    }
                                },
                                onSettingsClick = {
                                    navController.navigate(Screen.Settings.route)
                                }
                            ) 
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen(onBackClick = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}

package org.livewall.todoapp

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.livewall.todoapp.data.FirestoreAppUserRepository
import org.livewall.todoapp.data.FirestoreAuthenticationService
import org.livewall.todoapp.data.FirestoreToDoTaskRepository
import org.livewall.todoapp.domain.AppUser
import org.livewall.todoapp.domainimport.ToDoTask


@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val scope = rememberCoroutineScope()

        val authService = remember { FirestoreAuthenticationService() }

        var userEmail by remember { mutableStateOf("") }
        var userPassword by remember { mutableStateOf("") }

        var appUser by remember { mutableStateOf<AppUser?>(null) }

        val coroutineScope = rememberCoroutineScope()

        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        val repository = FirestoreAppUserRepository()

        LaunchedEffect(authService) {
            authService.authState.collect { user ->
                if (user != null) {
                    appUser = repository.getAppUser(user.uid)
                }
            }
        }

        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                if (authService.currentUser == null) {
                    LoginView(
                        userEmail = userEmail,
                        onEmailChange = { userEmail = it },
                        userPassword = userPassword,
                        onPasswordChange = { userPassword = it },
                        isLoading = isLoading,
                        errorMessage = errorMessage,
                        onLoginClick = {
                            scope.launch {
                                isLoading = true
                                errorMessage = null
                                try {
                                    appUser =
                                        authService.signIn(
                                            email = userEmail,
                                            password = userPassword
                                        )
                                } catch (e: Exception) {
                                    try {
                                        appUser = authService.signUp(
                                            email = userEmail,
                                            password = userPassword
                                        )
                                    } catch (e: Exception) {
                                        errorMessage = e.message
                                    }
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    )
                } else {
                    HomeView(
                        userId = appUser?.id ?: "Unknown Id",
                        tasks = appUser?.tasks,
                        onAddTaskClick = {
                            navController.navigate("taskInput")
                        },
                        onSignOutClick = {
                            coroutineScope.launch {
                                authService.signOut()
                                appUser = null
                            }
                        },
                        onTaskCheckedChange = { task, isChecked ->
                            val updatedTask = appUser?.tasks?.find { it.id == task.id }
                            if (updatedTask != null) {
                                updatedTask.isCompleted = isChecked
                                scope.launch {
                                    appUser?.id?.let {
                                        FirestoreToDoTaskRepository(it).updateToDoTask(
                                            updatedTask
                                        )
                                    }
                                }
                            }

//                            appUser?.tasks?.find { it.id == task.id }?.isCompleted = isChecked
//                            taskRepository.updateToDoTask()
                        },
                        onSaveTask = { task ->
                            val updatedTask = appUser?.tasks?.find { it.id == task.id }
                            scope.launch {
                                appUser?.id?.let {

                                        FirestoreToDoTaskRepository(it).updateToDoTask(
                                            task
                                        )

                                }
                            }

//                            appUser?.tasks?.find { it.id == task.id }?.isCompleted = isChecked
//                            taskRepository.updateToDoTask()
                        }


                    )

                }
            }
            composable("taskInput") {
                TaskInputView(
                    onSaveTask = { title, description ->
                        // Save the task and go back to the home screen
                        scope.launch {
                            appUser?.id?.let {
                                FirestoreToDoTaskRepository(it).addToDoTask(
                                    ToDoTask(title, description, false)
                                )
                            }
                            // Navigate back to home after saving the task
                            appUser = appUser?.id?.let { it1 -> repository.getAppUser(it1) }
                            navController.popBackStack()
                        }
                    },
                    onCancel = {
                        // Navigate back to home if the task is not saved
                        navController.popBackStack()
                    },
                    initialTitle = "",
                    initialDescription = ""
                )
            }
        }

    }
}

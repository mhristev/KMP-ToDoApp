package org.livewall.todoapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.livewall.todoapp.Factories.HomeViewModelFactory
import org.livewall.todoapp.Factories.LoginViewModelFactory
import org.livewall.todoapp.ViewModels.HomeViewModel
import org.livewall.todoapp.ViewModels.LoginViewModel
import org.livewall.todoapp.Views.HomeView
import org.livewall.todoapp.Views.LoginView
import org.livewall.todoapp.Views.ToDoTaskCreateNUpdateView
import org.livewall.todoapp.data.FirestoreAppUserRepository
import org.livewall.todoapp.data.FirestoreAuthenticationService
import org.livewall.todoapp.data.FirestoreToDoTaskRepository

@Composable
fun App() {
    val navController = rememberNavController()

    val authService = remember { FirestoreAuthenticationService() }
    val userRepository = remember { FirestoreAppUserRepository() }

    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(authService, userRepository))

    var isLoginSuccessful by remember { mutableStateOf(authService.currentUser != null) }

    var homeViewModel: HomeViewModel? = null

    // To init after the user is successfully logged in and in the beginning of the app if the current user is already present
    if (isLoginSuccessful && authService.currentUser != null) {
        homeViewModel = viewModel(
            factory = HomeViewModelFactory(FirestoreToDoTaskRepository(authService.currentUser?.uid ?: ""))
        )
    }


    NavHost(navController = navController, startDestination = if (authService.currentUser  != null) "home" else "login") {

        composable("login") {
            LoginView(
                loginViewModel = loginViewModel,
                onLoginSuccess = {
                    isLoginSuccessful = true
                    navController.navigate("home")
                }
            )
        }

        composable("home") {
            homeViewModel?.let {
                HomeView(
                    homeViewModel = it,
                    onAddTaskClick = { navController.navigate("taskInput") },
                    onEditTaskClick = { task ->
                        navController.navigate("taskInput/${task.id}")
                    },
                    onSignOutClick = {
                        authService.signOut()
                    },
                    navController = navController
                )
            }
        }
        composable("taskInput/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            val task = homeViewModel?.getTaskById(taskId)

            ToDoTaskCreateNUpdateView(
                initialTitle = task?.title ?: "",
                initialDescription = task?.details ?: "",
                onSaveTask = { title, description ->
                    homeViewModel?.updateTask(taskId, title, description)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }

        composable("taskInput") {
            ToDoTaskCreateNUpdateView(
                initialTitle = "",
                initialDescription = "",
                onSaveTask = { title, description ->
                    homeViewModel?.addTask(title, description)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
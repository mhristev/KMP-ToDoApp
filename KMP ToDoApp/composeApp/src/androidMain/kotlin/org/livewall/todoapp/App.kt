package org.livewall.todoapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    val homeViewModel: HomeViewModel? = authService.currentUser?.uid?.let {
        viewModel(factory = HomeViewModelFactory(FirestoreToDoTaskRepository(it)))
    }

    NavHost(navController = navController, startDestination = if (authService.currentUser  != null) "home" else "login") {

        composable("login") {
            LoginView(
                loginViewModel = loginViewModel,
                onLoginSuccess = {
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
                    if (homeViewModel != null) {
                        homeViewModel.updateTask(taskId, title, description)
                    }
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
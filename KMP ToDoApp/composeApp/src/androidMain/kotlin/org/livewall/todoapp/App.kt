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


//1. ViewModel and Lifecycle Awareness
//
//•	ViewModel is lifecycle-aware, which means it survives configuration changes (like screen rotations) and helps manage UI-related data in a lifecycle-conscious way.
//•	Jetpack Compose, like any UI framework, requires that the data provided to the UI is consistent and persists across configuration changes.
//
//By injecting a ViewModel into a composable function, you ensure that the UI is receiving a reference to a single instance of the ViewModel, which survives configuration changes and can handle data updates.
//
//2. Factory Pattern and Parameterized ViewModels
//
//The factory pattern comes into play when your ViewModel requires parameters to be passed to it at creation time (e.g., repository dependencies or user-specific data like an id).
//
//In Android, ViewModelProvider (part of the architecture components) creates and manages ViewModels. However, by default, it doesn’t handle ViewModels that require parameters. This is where the factory pattern is useful:
//
//•	The factory pattern allows you to inject custom dependencies (such as repositories or services) into the ViewModel, making it flexible and testable.
//•	It also separates the ViewModel’s creation logic from its use, adhering to Separation of Concerns.


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
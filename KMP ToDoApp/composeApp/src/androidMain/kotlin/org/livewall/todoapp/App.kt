package org.livewall.todoapp
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.material.Button
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import org.jetbrains.compose.ui.tooling.preview.Preview
//
//import kotlinx.coroutines.launch
//import org.livewall.todoapp.data.FirestoreAppUserRepository
//import org.livewall.todoapp.data.FirestoreToDoTaskRepository
//import org.livewall.todoapp.domain.AppUser
//import org.livewall.todoapp.domainimport.ToDoTask
//
//@Composable
//@Preview
//fun App() {
//    MaterialTheme {
//        var showContent by remember { mutableStateOf(false) }
//        var registrationStatus by remember { mutableStateOf<String?>(null) }
//        val coroutineScope = rememberCoroutineScope()
//        val repository = FirestoreAppUserRepository() // Assuming you have initialized Firestore
//
//
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            if (showContent) {
//                Button(onClick = {
//                    coroutineScope.launch {
////                        repository.registerUser(AppUser("123", "ivan@gmail.com")).collect { user ->
////                            registrationStatus = "User registered with ID: ${user.id}"
////                        }
//                        FirestoreToDoTaskRepository().addToDoTask(ToDoTask(
//                            "2", "title",
//                            "description",
//                            false))
//                    }
//                }) {
//                    Text("Register User")
//                }
//
//                registrationStatus?.let {
//                    Text(it)
//                }
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { FirestoreAppUserRepository().registerUser(AppUser("123", "ivan@gmail.com")) }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
////                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
//    }
//}


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dev.gitlive.firebase.auth.auth
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
        val scope = rememberCoroutineScope()
        val authService = remember { FirestoreAuthenticationService() }

        var userEmail by remember { mutableStateOf("") }
        var userPassword by remember { mutableStateOf("") }
        var currentUser by remember { mutableStateOf(authService.currentUser) }

        val coroutineScope = rememberCoroutineScope()
        val repository = FirestoreAppUserRepository() // Assuming you have initialized Firestore

        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        var appUser by remember { mutableStateOf<AppUser?>(null) }


        LaunchedEffect(authService) {
            authService.authState.collect { user ->
                currentUser = user
            }
        }

        LaunchedEffect(authService) {
            authService.authState.collect { user ->
                if (user != null) {
                    appUser = user.email?.let { AppUser(user.uid, it) }
                }
            }
        }

        if (currentUser == null) {
            // **Login UI**
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = userEmail,
                    onValueChange = { userEmail = it },
                    placeholder = { Text(text = "Email Address") }
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    value = userPassword,
                    onValueChange = { userPassword = it },
                    placeholder = { Text(text = "Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        isLoading = true
                        errorMessage = null
                        scope.launch {
                            try {
                                appUser = authService.signUp(email = userEmail, password = userPassword)
                            } catch (e: Exception) {
                                try {
                                    appUser = authService.signIn(email = userEmail, password = userPassword)
                                } catch (e: Exception) {
                                    errorMessage = e.message
                                }
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text(text = "Sign in / Sign up")
                    }
                }
                errorMessage?.let { Text(text = it, color = MaterialTheme.colors.error) }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = currentUser?.uid ?: "Unknown Id")
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Tasks in the application " + appUser?.tasks?.count().toString())
                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = {
                    coroutineScope.launch {
//                        repository.registerUser(AppUser("123", "ivan@gmail.com")).collect { user ->
//                            registrationStatus = "User registered with ID: ${user.id}"
//                        }
                        FirestoreToDoTaskRepository().addToDoTask(
                            ToDoTask(
                                "3", "title",
                                "description",
                                false
                            )
                        )
                    }
                }) {
                    Text("Add To Do Task")
                }


                Button(onClick = {
                    coroutineScope.launch {
                        authService.signOut()
                        currentUser = null
                        appUser = null
                    }
                }) {
                    Text(text = "Sign out")
                }
            }
        }

    }
}

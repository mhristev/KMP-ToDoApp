package org.livewall.todoapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun LoginView(
    userEmail: String,
    onEmailChange: (String) -> Unit,
    userPassword: String,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onLoginClick: suspend () -> Unit
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = userEmail,
            onValueChange = onEmailChange,
            placeholder = { Text(text = "Email Address") }
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = userPassword,
            onValueChange = onPasswordChange,
            placeholder = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                scope.launch {
                    if (!isLoading) {
                        onLoginClick()
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
}
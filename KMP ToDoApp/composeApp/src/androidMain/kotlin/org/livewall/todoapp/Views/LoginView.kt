package org.livewall.todoapp.Views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.livewall.todoapp.ViewModels.LoginViewModel

@Composable
fun LoginView(
    loginViewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = "",
            onValueChange = loginViewModel::onEmailChange,
            placeholder = { Text(text = "Test") }
        )
        TextField(
            value = loginViewModel.userEmail,
            onValueChange = loginViewModel::onEmailChange,
            placeholder = { Text(text = "Email Address") }
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = loginViewModel.userPassword,
            onValueChange = loginViewModel::onPasswordChange,
            placeholder = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                scope.launch { loginViewModel.onLoginClick(onLoginSuccess) }
            },
            enabled = !loginViewModel.isLoading
        ) {
            if (loginViewModel.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = "Sign in / Sign up")
            }
        }
        loginViewModel.errorMessage?.let { Text(text = it, color = MaterialTheme.colors.error) }
    }
}
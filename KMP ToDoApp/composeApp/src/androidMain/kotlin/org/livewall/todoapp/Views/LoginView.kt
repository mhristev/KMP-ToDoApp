package org.livewall.todoapp.Views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.livewall.todoapp.ViewModels.LoginViewModel

@Composable
fun LoginView(
    loginViewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var isLogin by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isLogin) "Log in" else "Create account",
            style = MaterialTheme.typography.h4
        )

        Spacer(modifier = Modifier.height(24.dp))

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
                scope.launch {
                    if (isLogin) {
                        loginViewModel.onLoginClick(onLoginSuccess)
                    } else {
                        loginViewModel.onSignUpClick(onLoginSuccess)
                    }
                }
            },
            enabled = !loginViewModel.isLoading
        ) {
            if (loginViewModel.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = if (isLogin) "Log in" else "Create account")
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            loginViewModel.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colors.error
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                    text = if (isLogin) "No account? " else "Have an account? ",
            style = MaterialTheme.typography.body1
            )
            Text(
                text = if (isLogin) "Sign up" else "Log in",
                color = Color.Blue,
                modifier = Modifier
                    .clickable { isLogin = !isLogin }
                    .padding(horizontal = 4.dp),
                style = MaterialTheme.typography.body1
            )
        }
    }
}
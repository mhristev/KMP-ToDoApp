package org.livewall.todoapp.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.livewall.todoapp.data.repositoryimpl.FirestoreAuthenticationService
import org.livewall.todoapp.data.repositoryimpl.FirestoreAppUserRepository
import org.livewall.todoapp.domain.models.AppUser

class LoginViewModel(
    private val authService: FirestoreAuthenticationService,
    private val repository: FirestoreAppUserRepository
) : ViewModel() {
    var userEmail by mutableStateOf("")
    var userPassword by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage: String? by mutableStateOf(null)
    var appUser: AppUser? = null
        private set

    fun onEmailChange(newEmail: String) {
        userEmail = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        userPassword = newPassword
    }

    fun onLoginClick(onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                appUser = authService.signIn(email = userEmail, password = userPassword)
                if (appUser != null) {
                    appUser = repository.getAppUser(authService.currentUser!!.uid)
                    onLoginSuccess()
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun onSignUpClick(onSignUpSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                appUser = authService.signUp(email = userEmail, password = userPassword)
                if (appUser != null) {
                    appUser = repository.getAppUser(authService.currentUser!!.uid)
                    onSignUpSuccess()
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}
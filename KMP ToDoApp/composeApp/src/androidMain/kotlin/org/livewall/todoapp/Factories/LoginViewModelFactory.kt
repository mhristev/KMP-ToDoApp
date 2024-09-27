package org.livewall.todoapp.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.livewall.todoapp.ViewModels.LoginViewModel
import org.livewall.todoapp.data.repositoryimpl.FirestoreAppUserRepository
import org.livewall.todoapp.data.repositoryimpl.FirestoreAuthenticationService

class LoginViewModelFactory(
    private val authService: FirestoreAuthenticationService,
    private val repository: FirestoreAppUserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(authService, repository) as T
    }
}


package org.livewall.todoapp.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.livewall.todoapp.ViewModels.HomeViewModel
import org.livewall.todoapp.data.FirestoreToDoTaskRepository

class HomeViewModelFactory(
    private val taskRepository: FirestoreToDoTaskRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(taskRepository) as T
    }
}
package org.livewall.todoapp.ViewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.livewall.todoapp.data.repositoryimpl.FirestoreToDoTaskRepository
import org.livewall.todoapp.domain.models.ToDoTask


class HomeViewModel(
    private val taskRepository: FirestoreToDoTaskRepository
) : ViewModel() {

    // Private mutable state list
    private val _toDoTasks = mutableStateListOf<ToDoTask>()

    // Public read-only state list
    val toDoTasks: List<ToDoTask> = _toDoTasks

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            taskRepository.getToDoTasks().collect { taskList ->
                _toDoTasks.clear()
                _toDoTasks.addAll(taskList)
            }
        }
    }

    fun getTaskById(taskId: String?): ToDoTask? {
        return toDoTasks.find { it.id == taskId }
    }

    fun updateTask(taskId: String?, title: String, description: String) {
        viewModelScope.launch {
            taskId?.let {
                val task = toDoTasks.find { it.id == taskId }
                if (task != null) {
                    val updatedTask = task.copy(
                        title = title,
                        details = description
                    )
                    taskRepository.updateToDoTask(updatedTask)
                    loadTasks()
                }
            }
        }
    }

    fun toggleTaskCompletion(task: ToDoTask) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            taskRepository.updateToDoTask(updatedTask)
            loadTasks()
        }
    }

    fun addTask(title: String, description: String) {
        viewModelScope.launch {
            taskRepository.addToDoTask(ToDoTask(title, description, false))
            loadTasks()
        }
    }

    fun deleteTask(task: ToDoTask) {
        viewModelScope.launch {
            taskRepository.deleteToDoTask(task)
            loadTasks()
        }
    }
}
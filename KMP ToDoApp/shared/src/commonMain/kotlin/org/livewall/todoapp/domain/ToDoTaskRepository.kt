package org.livewall.todoapp.domain

import kotlinx.coroutines.flow.Flow
import org.livewall.todoapp.domainimport.ToDoTask
//
//	•	Flow is used for streaming data and handling multiple values over time, and is suited for observables or real-time updates.
//	•	suspend Functions are used for one-time asynchronous operations that return a single result or complete a task.
interface ToDoTaskRepository {
    fun getToDoTasks(): Flow<List<ToDoTask>>
    fun getToDoTaskById(id: String): Flow<ToDoTask?>
    suspend fun addToDoTask(toDoTask: ToDoTask)
    suspend fun updateToDoTask(toDoTask: ToDoTask)
    suspend fun deleteToDoTask(toDoTask: ToDoTask)
}
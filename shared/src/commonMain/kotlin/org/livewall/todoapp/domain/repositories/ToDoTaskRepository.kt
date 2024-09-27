package org.livewall.todoapp.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.livewall.todoapp.domain.models.ToDoTask

interface ToDoTaskRepository {
    fun getToDoTasks(): Flow<List<ToDoTask>>
    fun getToDoTaskById(id: String): Flow<ToDoTask?>
//    @Throws(FirebaseFirestoreException::class, CancellationException::class)
    @Throws(Exception::class)
    suspend fun addToDoTask(toDoTask: ToDoTask)
    suspend fun updateToDoTask(toDoTask: ToDoTask)
    @Throws(Exception::class)
    suspend fun deleteToDoTask(toDoTask: ToDoTask)
    suspend fun markToDoTaskCompleted(toDoTask: ToDoTask)
}
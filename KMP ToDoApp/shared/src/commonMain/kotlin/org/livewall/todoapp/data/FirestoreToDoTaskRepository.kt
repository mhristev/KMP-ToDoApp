package org.livewall.todoapp.data

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FieldValue

import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import org.livewall.todoapp.domain.ToDoTaskRepository
import org.livewall.todoapp.domainimport.ToDoTask

const val TODOTASK_ARRAY = "tasks"

class FirestoreToDoTaskRepository(private val currentUserId: String) : ToDoTaskRepository {

    private val db = Firebase.firestore
    val documentReference = db.collection("Users").document(currentUserId)

    override fun getToDoTasks(): Flow<List<ToDoTask>> = flow {

        try {
            val documentSnapshot = documentReference.get()
            emit(documentSnapshot.get<List<ToDoTask>>(TODOTASK_ARRAY))
        } catch (e: Exception) {
            println("Error retrieving tasks: ${e.message}")
            emit(emptyList())
        }
    }

    override fun getToDoTaskById(id: String): Flow<ToDoTask?> = flow {
        val taskref = documentReference.collection(TODOTASK_ARRAY).document(id)
        try {
            val task = taskref.get()
            if (task.exists) {
                emit(task.data<ToDoTask>())
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            println("Error retrieving task with id ${id}: ${e.message}")
        }
  
    }
    
    override suspend fun addToDoTask(toDoTask: ToDoTask) {
        try {
            documentReference.update(mapOf(
                TODOTASK_ARRAY to FieldValue.arrayUnion(toDoTask)
            ))
            println("Task added successfully.")
        } catch (e: Exception) {
            println("Error adding task: ${e.message}")
        }
    }

    override suspend fun updateToDoTask(toDoTask: ToDoTask) {
        try {
            val tasks = documentReference.get()
                .get<List<ToDoTask>>(TODOTASK_ARRAY)

            val updatedTasks = tasks.map { task ->
                if (task.id == toDoTask.id) {
                    task.copy(isCompleted = toDoTask.isCompleted,
                        title = toDoTask.title,
                        description = toDoTask.description,
                        )
                } else {
                    task
                }
            }

            documentReference.update(mapOf(
                TODOTASK_ARRAY to updatedTasks
            ))


        } catch (e: Exception) {
            println("Error updating isCompleted task with id ${toDoTask.id}: ${e.message}")
        }
    }

    override suspend fun deleteToDoTask(toDoTask: ToDoTask) {

        try {
           documentReference.update(mapOf(
               TODOTASK_ARRAY to FieldValue.arrayRemove(toDoTask)
           ))
        } catch (e: Exception) {
            println("Error deleting task: ${e.message}")
        }	
    }

    override suspend fun markToDoTaskCompleted(toDoTask: ToDoTask) {
        try {
            val tasks = documentReference.get()
                .get<List<ToDoTask>>(TODOTASK_ARRAY)

            val updatedTasks = tasks.map { task ->
                if (task.id == toDoTask.id) {
                    task.copy(isCompleted = toDoTask.isCompleted)
                } else {
                    task
                }
            }

            documentReference.update(mapOf(
                TODOTASK_ARRAY to updatedTasks
            ))


        } catch (e: Exception) {
            println("Error updating isCompleted task with id ${toDoTask.id}: ${e.message}")
        }
    }

}
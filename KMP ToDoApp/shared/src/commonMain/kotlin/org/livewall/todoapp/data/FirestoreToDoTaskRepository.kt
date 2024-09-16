package org.livewall.todoapp.data

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FieldValue

import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime

import org.livewall.todoapp.domain.ToDoTaskRepository
import org.livewall.todoapp.domainimport.ToDoTask

const val TODOTASK_ARRAY = "tasks"

class FirestoreToDoTaskRepository : ToDoTaskRepository {
    
    private val currentUserId = "2uTw76whdwW37bOkqQgFCNnFEpi1"
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
        val taskRef = documentReference.collection(TODOTASK_ARRAY).document(toDoTask.id)
        try {
            val task = taskRef.get()
            if (task.exists) {
                val oldTask = task.data<ToDoTask>()
                oldTask.title = toDoTask.title
                oldTask.description = toDoTask.description
                oldTask.isCompleted = toDoTask.isCompleted
                
                taskRef.set(oldTask)
                println("Task updated successfully.")
            } else {
                println("Task with id ${toDoTask.id} does not exist.")
            }
        } catch (e: Exception) {
            println("Error updating task with id ${toDoTask.id}: ${e.message}")
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

}
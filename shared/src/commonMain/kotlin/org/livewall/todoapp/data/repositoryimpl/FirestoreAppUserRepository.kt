package org.livewall.todoapp.data.repositoryimpl

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.livewall.todoapp.domain.models.AppUser
import org.livewall.todoapp.domain.repositories.AppUserRepository
import org.livewall.todoapp.domain.models.ToDoTask

class FirestoreAppUserRepository: AppUserRepository {
    private val db = Firebase.firestore

    override suspend fun getAppUser(id: String): AppUser {
        val documentSnapshot = db.collection("Users").document(id).get()
        val appUser = documentSnapshot.data<AppUser>()
        return appUser
    }

    suspend fun getUserTasks(userId: String): Flow<List<ToDoTask>> = flow {
        val tasksSnapshot = db.collection("Users").document(userId).collection("Tasks").get()
        emit(tasksSnapshot.documents.mapNotNull { it.data<ToDoTask>() })
    }

    override suspend fun registerUser(user: AppUser): AppUser {
        val documentReference = db.collection("Users")
        val p = documentReference.document(user.id)
        p.set(user)
        return user
    }
}
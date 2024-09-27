package org.livewall.todoapp.domain.repositories

import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import org.livewall.todoapp.domain.models.AppUser

interface AuthenticationService {
    val currentUser: FirebaseUser?

    val authState: Flow<FirebaseUser?>

    @Throws(Exception::class)
    suspend fun signIn(email: String, password: String):  AppUser?

    suspend fun signUp(email: String, password: String):  AppUser?

    suspend fun signOut()
}
package org.livewall.todoapp.domain

import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthenticationService {
    /** Current authenticated user, if any. */
    val currentUser: FirebaseUser?

    /** Authentication state as a Flow. */
    val authState: Flow<FirebaseUser?>

    /** Sign in with email and password. */
    @Throws(Exception::class)
    suspend fun signIn(email: String, password: String):  AppUser?

    /** Sign up (register) with email and password. */
    suspend fun signUp(email: String, password: String):  AppUser?

    /** Sign out the current user. */
    suspend fun signOut()
}
package org.livewall.todoapp.data

import dev.gitlive.firebase.auth.auth
import org.livewall.todoapp.domain.AuthenticationService
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import org.livewall.todoapp.domain.AppUser

class FirestoreAuthenticationService: AuthenticationService {

    private val auth: FirebaseAuth = Firebase.auth
    private val userRepository = FirestoreAppUserRepository()

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override val authState: Flow<FirebaseUser?>
        get() = auth.authStateChanged

    override suspend fun signIn(email: String, password: String): AppUser? {
        val result = auth.signInWithEmailAndPassword(email, password)
        val firebaseUser = result.user

        return firebaseUser?.let {
            userRepository.getAppUser(it.uid)
        }

    }

    override suspend fun signUp(email: String, password: String): AppUser? {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password)
            val firebaseUser = result.user
            return if (firebaseUser != null) {
                val appUser = AppUser(
                    id = firebaseUser.uid,
                    email = firebaseUser.email ?: email
                )
                userRepository.registerUser(appUser)
                appUser
            } else {
                null
            }
        } catch (e: Exception) {
            return this.signIn(email, password)
        }



    }

    override suspend fun signOut() {
        auth.signOut()
    }

}
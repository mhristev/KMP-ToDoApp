package org.livewall.todoapp.domain

import kotlinx.coroutines.flow.Flow

interface AppUserRepository {
    suspend fun getAppUser(id: String):  AppUser?
    suspend fun registerUser(user: AppUser): AppUser
}
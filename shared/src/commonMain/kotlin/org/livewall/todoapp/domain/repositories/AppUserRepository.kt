package org.livewall.todoapp.domain.repositories

import org.livewall.todoapp.domain.models.AppUser

interface AppUserRepository {
    suspend fun getAppUser(id: String):  AppUser?
    suspend fun registerUser(user: AppUser): AppUser
}
package org.livewall.todoapp.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class AppUser(val id: String, val email: String, val tasks: List<ToDoTask> = emptyList())
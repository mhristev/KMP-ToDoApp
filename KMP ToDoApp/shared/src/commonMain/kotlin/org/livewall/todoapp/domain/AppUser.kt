package org.livewall.todoapp.domain

import kotlinx.serialization.Serializable
import org.livewall.todoapp.domainimport.ToDoTask

@Serializable
data class AppUser(val id: String, val email: String, val tasks: List<ToDoTask> = emptyList()) {
    
}
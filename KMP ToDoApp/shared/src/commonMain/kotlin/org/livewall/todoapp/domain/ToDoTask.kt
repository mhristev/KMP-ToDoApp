package org.livewall.todoapp.domainimport

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ToDoTask(
    val id: String,                
    var title: String,          
    var description: String?,   
    var isCompleted: Boolean
) {
    
}
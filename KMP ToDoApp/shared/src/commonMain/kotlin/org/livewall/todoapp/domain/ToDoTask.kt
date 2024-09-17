package org.livewall.todoapp.domainimport

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ToDoTask(
    val id: String,                
    var title: String,          
    var description: String?,
    @SerialName("completed") var isCompleted: Boolean
) {
    
}
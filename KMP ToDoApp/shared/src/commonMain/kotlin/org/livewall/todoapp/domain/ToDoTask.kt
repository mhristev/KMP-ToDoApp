package org.livewall.todoapp.domainimport

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ToDoTask(
    var title: String,          
    var description: String?,
    @SerialName("completed") var isCompleted: Boolean,
    val id: String =  uuid4().toString()
) {

    
}
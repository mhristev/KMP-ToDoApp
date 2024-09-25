package org.livewall.todoapp.domainimport

import com.benasher44.uuid.uuid4
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ToDoTask(
    var title: String,
    var details: String?,
    @SerialName("completed") var isCompleted: Boolean,
    val id: String
) {
    constructor(title: String, details: String?, isCompleted: Boolean) : this(
        title,
        details,
        isCompleted,
        uuid4().toString() // Auto-generate id
    )

    fun toMap(): Map<String, Any?> {
//        val jsonString = Json.encodeToString(this)
//        return Json.parseToJsonElement(jsonString).jsonObject.mapValues { it.value.toString() }
        return mapOf(
            "title" to title,
            "details" to details,
            "completed" to isCompleted,
            "id" to id
        )
    }
    
}
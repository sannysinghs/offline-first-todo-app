package com.tixeon.todos.android.data.remote.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    @SerializedName("created_at")
    val creationDate: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("_id")
    val id: String?,
    @SerializedName("local_id")
    val localId: String,
    @SerializedName("completed")
    val isCompleted: Boolean,
)

@Serializable
data class CompleteTaskRequest(
    @SerializedName("completed")
    val isCompleted: Boolean,
)

@Serializable
data class AddTaskRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("completed")
    val completed: Boolean,
    @SerializedName("local_id")
    val localId: String,
    @SerializedName("created_at")
    val createdAt: Long,
)
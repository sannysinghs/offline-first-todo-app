package com.tixeon.todos.android.data.remote.api

import com.tixeon.todos.android.data.remote.response.AddTaskRequest
import com.tixeon.todos.android.data.remote.response.CompleteTaskRequest
import com.tixeon.todos.android.data.remote.response.NetworkChangeList
import com.tixeon.todos.android.data.remote.response.TaskDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskApi {

    @POST("api/todos")
    suspend fun addTask(@Body vararg task: AddTaskRequest): List<TaskDto>

    @GET("api/todos")
    suspend fun getTasks(@Query("id") ids: List<String>? = null): List<TaskDto>

    @GET("api/todos/changelist")
    suspend fun getChangeList(@Query("lastSyncedVersion") lastSyncedVersion: Int): List<NetworkChangeList>

    @PATCH("api/todos/{id}")
    suspend fun updateTask(
        @Path("id") id: String,
        @Body body: CompleteTaskRequest
    ): TaskDto
}
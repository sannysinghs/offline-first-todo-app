package com.tixeon.todos.android.data.remote.api

import com.tixeon.todos.android.data.remote.response.TaskDto
import retrofit2.http.GET

interface TaskApi {
    @GET("api/todos")
    suspend fun getTasks(): List<TaskDto>
}
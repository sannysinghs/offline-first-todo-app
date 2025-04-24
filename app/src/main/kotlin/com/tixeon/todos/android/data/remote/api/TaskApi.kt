package com.tixeon.todos.android.data.remote.api

import com.tixeon.todos.android.data.remote.response.NetworkChangeList
import com.tixeon.todos.android.data.remote.response.TaskDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TaskApi {
    @GET("api/todos")
    suspend fun getTasks(@Query("id") ids: List<String>? = null): List<TaskDto>

    @GET("api/todos/changelist")
    suspend fun getChangeList(@Query("lastSyncedVersion") lastSyncedVersion: Int): List<NetworkChangeList>
}
package com.tixeon.todos.android.data.respository

import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.util.Resource
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getTaskList(): Flow<List<TaskEntity>>
    fun getCompletedTasks(): List<String>
    suspend fun syncTasks(): Boolean
    fun updateTaskToComplete(id: String, completed: Boolean): Flow<Resource<List<TaskEntity>>>
    suspend fun updateTask(
        id: String,
        title: String,
        description: String,
        isCompleted: Boolean
    ): Flow<Resource<TaskEntity>>
}

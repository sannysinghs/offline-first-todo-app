package com.tixeon.todos.android.data.respository

import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.util.Resource
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getTaskList(): Flow<List<TaskEntity>>
    fun updateTask(
        id: String,
        title: String,
        description: String,
        isCompleted: Boolean
    ): Flow<Resource<TaskEntity>>

    fun addTask(
        title: String,
        description: String,
    ): Flow<Resource<TaskEntity>>
}

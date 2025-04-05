package com.tixeon.todos.android.domain.repository

import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.util.Resource
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    /**
     * Get list of tasks
     *
     * @return Flow<Resource<List<TaskEntity>>>
     */
    suspend fun getTaskList(): Flow<Resource<List<TaskEntity>>>

    /**
     * Get completed task ids
     *
     * @return List<Long>
     */
    fun getCompletedTasks(): List<String>

    /**
     * Update task to complete
     *
     * @param id task id
     * @param completed true if task is completed
     * @return Flow<Resource<TaskEntity>>
     */
    suspend fun updateTaskToComplete(id: String, completed: Boolean): Flow<Resource<List<TaskEntity>>>
}
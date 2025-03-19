package com.tixeon.todos.android.domain.usecases

import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.domain.repository.TaskRepository
import com.tixeon.todos.android.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

open class GetAllTasksUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(): Flow<Resource<List<TaskEntity>>> {
        return repository.getTaskList()
    }
}
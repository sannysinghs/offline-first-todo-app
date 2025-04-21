package com.tixeon.todos.android.domain.usecases

import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.data.respository.TaskRepository
import com.tixeon.todos.android.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class GetAllTasksUseCase @Inject constructor(private val repository: TaskRepository) {
    operator fun invoke(): Flow<Resource<List<TaskEntity>>> {
        return repository.getTaskList().map {
            Resource.Success(it)
        }.catch {
            Resource.Error(it.message.orEmpty())
        }
    }
}
package com.tixeon.todos.android.domain.usecases

import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.data.respository.TaskRepository
import com.tixeon.todos.android.domain.model.Task
import com.tixeon.todos.android.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

open class AddTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    operator fun invoke(
        title: String,
        description: String,
    ): Flow<Resource<TaskEntity>> =
        repository.addTask(
            title = title,
            description = description,
        )
}

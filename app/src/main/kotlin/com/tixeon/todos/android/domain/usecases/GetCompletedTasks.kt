package com.tixeon.todos.android.domain.usecases

import com.tixeon.todos.android.data.respository.TaskRepository
import javax.inject.Inject

open class GetCompletedTasks @Inject constructor(private val repository: TaskRepository) {
    operator fun invoke(): List<String> {
        return repository.getCompletedTasks()
    }
}

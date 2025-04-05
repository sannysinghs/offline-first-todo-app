package com.tixeon.todos.android.presentation.tasks

import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.domain.repository.TaskRepository
import com.tixeon.todos.android.presentation.tasks.TaskViewModelTest.Companion.FAKE_TASK_ENTITY
import com.tixeon.todos.android.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeTaskRepositoryImpl: TaskRepository {
    private var tasks: List<TaskEntity> = emptyList()

    private var exception: Throwable? = null
    fun fakeSuccess(task: List<TaskEntity> = emptyList()) {
        tasks = task
        exception = null
    }

    fun fakeRetrievalError(
        err: Exception = Exception("error")
    ) {
        exception = err
        tasks = emptyList()
    }

    override suspend fun getTaskList(): Flow<Resource<List<TaskEntity>>> {
        return flowOf(
            if (exception != null) {
                Resource.Error(exception?.message.orEmpty())
            } else {
                Resource.Success(tasks)
            }

        )
    }

    override fun getCompletedTasks(): List<Long> {
        if(exception != null) throw exception!!
        return listOf(FAKE_TASK_ENTITY.id)
    }

    override suspend fun updateTaskToComplete(
        id: Long,
        completed: Boolean
    ): Flow<Resource<TaskEntity>> {
        return flowOf(
            if (exception != null) {
                Resource.Error(exception?.message.orEmpty())
            } else {
                Resource.Success(tasks.first())
            }
        )
    }
}
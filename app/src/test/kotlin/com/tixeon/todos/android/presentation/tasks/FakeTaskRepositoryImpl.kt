package com.tixeon.todos.android.presentation.tasks

import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.data.respository.TaskRepository
import com.tixeon.todos.android.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeTaskRepositoryImpl: TaskRepository {
    private var tasks: List<TaskEntity> = emptyList()

    private var exception: Throwable? = null
    fun fakeSuccess(task: List<TaskEntity> = emptyList()) {
        tasks = task
        exception = null
    }

    fun fakeError(err: Exception = Exception("error")) {
        exception = err
        tasks = emptyList()
    }

    override fun updateTask(
        id: String,
        title: String,
        description: String,
        isCompleted: Boolean
    ): Flow<Resource<TaskEntity>> = flowOf(
        if (exception != null) {
            Resource.Error(exception?.message.orEmpty())
        } else {
            Resource.Success(tasks.first())
        }
    )

    override fun addTask(
        title: String,
        description: String
    ): Flow<Resource<TaskEntity>> = flowOf(
        if (exception != null) {
            Resource.Error(exception?.message.orEmpty())
        } else {
            Resource.Success(
                TaskEntity(
                    localId = "local_123",
                    remoteId = "1000",
                    title = title,
                    description = description,
                    isCompleted = false,
                    creationDate = 1000L,
                    dependencies = "",
                )
            )
        }
    )

    override fun getTaskList(): Flow<List<TaskEntity>> = flow {
        if (exception != null) {
            throw exception!!
        }
        emit(tasks)
    }
}
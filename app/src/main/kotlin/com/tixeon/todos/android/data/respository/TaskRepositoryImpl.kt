package com.tixeon.todos.android.data.respository

import android.util.Log
import com.tixeon.todos.android.data.local.dao.TaskDao
import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.data.remote.api.TaskApi
import com.tixeon.todos.android.data.remote.response.TaskDto
import com.tixeon.todos.android.domain.repository.TaskRepository
import com.tixeon.todos.android.util.AppResourceProvider
import com.tixeon.todos.android.util.Resource
import com.tixeon.todos.android.util.toTimeInMills
import com.tixeon.todos.android.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val resProvider: AppResourceProvider,
    private val taskApi: TaskApi,
    private val dao: TaskDao,
) : TaskRepository {

    private val taskToCompletedMap: MutableMap<String, Boolean> = hashMapOf()
    private val allTasks: MutableList<TaskEntity> = mutableListOf()

    override suspend fun getTaskList(): Flow<Resource<List<TaskEntity>>> = flow {
        try {
            if (allTasks.isEmpty()) {
                // load local data
                allTasks.addAll(dao.getAllTask())
            }

            if (allTasks.isEmpty()) {
                // load remote data
                val taskEntities = taskApi.getTasks().map {
                    Log.e(TAG, "getTaskList: map to entities")
                    it.toTaskEntity()
                }
                dao.insertAllTasks(*taskEntities.toTypedArray())
                allTasks.addAll(taskEntities)
            }

            allTasks.forEach { taskToCompletedMap[it.id] = it.isCompleted }
            emit(Resource.Success(allTasks))
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    when (e) {
                        is IOException -> resProvider.getString(R.string.error_connection)
                        is HttpException -> {
                            if (e.code() == 504) resProvider.getString(R.string.error_connection)
                            else resProvider.getString(R.string.error_service)
                        }

                        else -> resProvider.getString(R.string.error_unknown)
                    }
                )
            )
        }
    }

    override fun getCompletedTasks(): List<String> = taskToCompletedMap.filter { it.value }.map { it.key }

    override suspend fun updateTaskToComplete(
        id: String,
        completed: Boolean
    ): Flow<Resource<List<TaskEntity>>> = flow {
        try {
            val oldTask = allTasks.first { it.id == id }.also { allTasks.remove(it) }
            val newTask = oldTask.copy(isCompleted = completed)

            taskToCompletedMap[id] = true
            allTasks.add(newTask)

            emit(Resource.Success(allTasks))
            // update local db
            dao.insertAllTasks(newTask)
        } catch (e: Exception) {
            emit(Resource.Error(e.message.orEmpty()))
        }
    }

    companion object {
        const val TAG = "TaskRepository"
    }
}
private fun TaskDto.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        creationDate = creationDate.toTimeInMills(),
        dependencies = "",
        isCompleted = false,
    )
}

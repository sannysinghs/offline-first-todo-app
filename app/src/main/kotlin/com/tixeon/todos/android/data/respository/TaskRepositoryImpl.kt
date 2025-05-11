package com.tixeon.todos.android.data.respository

import android.util.Log
import com.tixeon.todos.android.data.datastore.AppPreferenceDataStore
import com.tixeon.todos.android.data.local.dao.TaskDao
import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.data.remote.api.TaskApi
import com.tixeon.todos.android.data.remote.response.AddTaskRequest
import com.tixeon.todos.android.data.sync.Syncable
import com.tixeon.todos.android.util.DispatcherProvider
import com.tixeon.todos.android.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskApi: TaskApi,
    private val dao: TaskDao,
    private val appPreferences: AppPreferenceDataStore,
    private val dispatchers: DispatcherProvider
) : TaskRepository, Syncable {

    override fun getTaskList(): Flow<List<TaskEntity>> = dao.getAllTask()

    override fun addTask(title: String, description: String): Flow<Resource<TaskEntity>> =
        flow {
            try {
                TaskEntity(
                    remoteId = "",
                    localId = generateUniqueLocalId(),
                    title = title,
                    description = description,
                    creationDate = System.currentTimeMillis(),
                    dependencies = "",
                ).also {
                    dao.updateTasks(it)
                    emit(Resource.Success(it))
                }
            } catch (e: Exception) {
                Log.e(TAG, "addTask: ${e.stackTraceToString()}")
                emit(Resource.Error(e.message.orEmpty()))
            }
        }.flowOn(dispatchers.io())

    override fun updateTask(
        localId: String,
        title: String,
        description: String,
        isCompleted: Boolean
    ): Flow<Resource<TaskEntity>> = flow {
        try {
            val old = dao.getTask(localId = localId) ?: throw TaskNotFoundException("Task (localId $localId) not found")
            val new = old.copy(isCompleted = isCompleted, isSynced = false)

            dao.updateTasks(new)

            emit(Resource.Success(new))

        } catch (e: Exception) {
            Log.e(TAG, "updateTask: Error update task: $e")
            emit(Resource.Error(e.message.orEmpty()))
        }
    }.flowOn(dispatchers.io())

    private suspend fun generateUniqueLocalId(): String {
        fun generate(): String {
            // Combine timestamp with random UUID to ensure uniqueness
            val timestamp = System.currentTimeMillis()
            val uuid = UUID.randomUUID().toString().take(8) // Take first 8 chars for brevity
            return "local_${timestamp}_$uuid"
        }

        var localId = ""
        var isUnique = false

        while (!isUnique) {
            localId = generate()
            // Check if ID exists in local database
            isUnique = dao.getTask(localId) == null
        }

        return localId
    }

    override suspend fun syncs(): Boolean = syncTasks()

    private suspend fun syncTasks(): Boolean = runCatching {
        // push the changes.
        try {
            dao.getUnSyncedTask().map {
                AddTaskRequest(
                    title = it.title,
                    description = it.description,
                    completed = it.isCompleted,
                    localId = it.localId,
                    createdAt = it.creationDate,
                )
            }.also {
                if (it.isNotEmpty()) {
                    taskApi.addTask(*it.toTypedArray())
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "syncTasks: ${e.stackTraceToString()}")
        }

        // pull the changes and update the local database.
        val currentVersion = appPreferences.getChangeListVersion()
        val changes = taskApi.getChangeList(lastSyncedVersion = currentVersion)

        val (deleted, updated) = changes.partition { it.isDelete }

        if (deleted.isNotEmpty()) {
            dao.deleteTasks(deleted.map { it.resourceId })
        }

        if (updated.isNotEmpty()) {
            val updatedTasks = taskApi.getTasks(ids = updated.map { it.resourceId })
            dao.updateTasks(
                *updatedTasks.map { it.toTaskEntity() }.toTypedArray()
            )
        }

        val lastSyncVersion = changes.last().version
        appPreferences.setChangeListVersion(lastSyncVersion)

        Result.success(true)
    }.getOrElse {
        Log.e(TAG, "syncTasks: ${it.message}", )
        Result.success(false)
    }.isSuccess

    data class TaskNotFoundException(val taskId: String) : Exception()

    companion object {
        const val TAG = "TaskRepository"
    }
}
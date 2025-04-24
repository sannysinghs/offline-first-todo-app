package com.tixeon.todos.android.data.respository

import com.tixeon.todos.android.data.datastore.AppPreferenceDataStore
import com.tixeon.todos.android.data.local.dao.TaskDao
import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.data.remote.api.TaskApi
import com.tixeon.todos.android.data.remote.response.TaskDto
import com.tixeon.todos.android.util.Resource
import com.tixeon.todos.android.util.toTimeInMills
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskApi: TaskApi,
    private val dao: TaskDao,
    private val appPreferences: AppPreferenceDataStore
) : TaskRepository {

    private val taskToCompletedMap: MutableMap<String, Boolean> = hashMapOf()
    private val allTasks: MutableList<TaskEntity> = mutableListOf()

    override fun getTaskList(): Flow<List<TaskEntity>> = dao.getAllTask()

    override fun getCompletedTasks(): List<String> = taskToCompletedMap.filter { it.value }.map { it.key }

    override suspend fun syncTasks(): Boolean = runCatching {
        val currentVersion = appPreferences.getChangeListVersion()
        val changes = taskApi.getChangeList(lastSyncedVersion = currentVersion)

        val (deleted, updated) = changes.partition { it.isDelete }

        if (deleted.isNotEmpty()) {
            dao.deleteTasks(deleted.map { it.resourceId })
        }

        if (updated.isNotEmpty()) {
            val updatedTasks = taskApi.getTasks(ids = updated.map { it.resourceId })
            dao.updateTasks(updatedTasks.map { it.toTaskEntity() })
        }

        val lastSyncVersion = changes.last().version
        appPreferences.setChangeListVersion(lastSyncVersion)

        Result.success(true)
    }.getOrElse {
        Result.success(false)
    }.isSuccess

    override fun updateTaskToComplete(
        id: String,
        completed: Boolean
    ): Flow<Resource<List<TaskEntity>>> = flow {
        try {
            val oldTask = allTasks.first { it.id == id }.also { allTasks.remove(it) }
            val newTask = oldTask.copy(isCompleted = completed, isSynced = false)

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
private fun TaskDto.toTaskEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    description = description,
    creationDate = creationDate.toTimeInMills(),
    dependencies = "",
    isCompleted = false,
)

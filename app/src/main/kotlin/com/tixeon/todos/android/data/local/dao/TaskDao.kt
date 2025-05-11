package com.tixeon.todos.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.tixeon.todos.android.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task ORDER BY creation_date DESC")
    fun getAllTask(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE is_synced = 0")
    fun getUnSyncedTask(): List<TaskEntity>

    @Query("SELECT * FROM task WHERE localId = :localId")
    suspend fun getTask(localId: String): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTasks(vararg tasks: TaskEntity)

    @Upsert
    suspend fun updateTasks(vararg tasks: TaskEntity)

    @Query("DELETE FROM task WHERE localId IN (:localIds)")
    suspend fun deleteTasks(localIds: List<String>)
}
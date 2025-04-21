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

    @Query("SELECT * FROM task WHERE id = :id")
    fun getTask(id: Long): Flow<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTasks(vararg tasks: TaskEntity)

    @Upsert
    suspend fun updateTasks(tasks: List<TaskEntity>)

    @Query("DELETE FROM task WHERE id IN (:ids)")
    suspend fun deleteTasks(ids: List<String>)
}
package com.tixeon.todos.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tixeon.todos.android.data.local.entity.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM task ORDER BY creation_date DESC")
    suspend fun getAllTask(): List<TaskEntity>

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getTask(id: Long): TaskEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTasks(vararg tasks: TaskEntity)
}
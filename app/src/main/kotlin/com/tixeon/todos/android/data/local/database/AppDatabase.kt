package com.tixeon.todos.android.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tixeon.todos.android.data.local.dao.TaskDao
import com.tixeon.todos.android.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
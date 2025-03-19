package com.tixeon.todos.android.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "creation_date")
    val creationDate: Long,
    @ColumnInfo(name = "due_date")
    val dueDate: Long? = null,
    @ColumnInfo("image")
    val image: String? = null,
    @ColumnInfo("dependencies")
    val dependencies: String,
    @ColumnInfo("is_completed")
    val isCompleted: Boolean,
    @ColumnInfo("is_synced")
    val isSynced: Boolean = false
)
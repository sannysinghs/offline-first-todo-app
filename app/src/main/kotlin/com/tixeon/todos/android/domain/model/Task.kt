package com.tixeon.todos.android.domain.model

data class Task(
    val id: String,
    val title: String,
    val image: String?,
    val description: String,
    val dueDate: String?,
    val createdDate: String,
    val isCompletable: Boolean,
    val isCompleted: Boolean = false,
    val dependencies: List<Long> = emptyList(),
)
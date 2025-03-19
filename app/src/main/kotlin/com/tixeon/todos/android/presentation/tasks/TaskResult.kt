package com.tixeon.todos.android.presentation.tasks

import com.tixeon.todos.android.domain.model.Task

/**
 * Data class representing the result.
 *
 * @property allTasks List of all tasks.
 * @property upcomingTasks List of upcoming tasks.
 */
data class TaskResult(
    val allTasks: List<Task>,
    val upcomingTasks: List<Task>
)

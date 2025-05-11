package com.tixeon.todos.android.presentation.tasks

import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.domain.model.Task
import com.tixeon.todos.android.util.toDateString
import kotlinx.coroutines.coroutineScope

internal suspend fun mapToViewState(res: List<TaskEntity>) = coroutineScope {
        val map = res.map { task ->
            val hasDependenciesResolved = true
            task.toTask(hasDependenciesResolved)
        }

        return@coroutineScope with(map) {
            TaskResult(
                allTasks = sortedBy { it.createdDate },
                upcomingTasks = filterNot { it.isCompleted }.sortedBy { it.dependencies.size }
            )
        }
    }

fun TaskEntity.toTask(hasDependenciesResolved: Boolean): Task {
    val dependencies = if (dependencies.isNotEmpty()) {
        dependencies.split(",").map { it.toLong() }
    } else {
        emptyList()
    }

    return Task(
        title = title,
        description = description,
        dueDate = dueDate?.toDateString(),
        createdDate = creationDate.toDateString(),
        id = localId,
        image = image,
        isCompletable = hasDependenciesResolved,
        isCompleted = isCompleted,
        dependencies = dependencies
    )
}

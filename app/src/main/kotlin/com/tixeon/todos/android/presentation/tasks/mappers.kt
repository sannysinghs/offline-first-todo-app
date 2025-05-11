package com.tixeon.todos.android.presentation.tasks

import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.domain.model.Task
import com.tixeon.todos.android.util.toDateString
import kotlinx.coroutines.coroutineScope

internal suspend fun mapToViewState(res: List<TaskEntity>) = coroutineScope {
        val map = res.map { task ->
            val dependencies = if (task.dependencies.isNotEmpty()) {
                task.dependencies.split(",").map { it.toLong() }
            } else {
                emptyList()
            }

            val hasDependenciesResolved = true

            Task(
                title = task.title,
                description = task.description,
                dueDate = task.dueDate?.toDateString(),
                createdDate = task.creationDate.toDateString(),
                id = task.localId,
                image = task.image,
                isCompletable = hasDependenciesResolved,
                isCompleted = task.isCompleted,
                dependencies = dependencies
            )
        }

        return@coroutineScope with(map) {
            TaskResult(
                allTasks = sortedBy { it.createdDate },
                upcomingTasks = filterNot { it.isCompleted }.sortedBy { it.dependencies.size }
            )
        }
    }

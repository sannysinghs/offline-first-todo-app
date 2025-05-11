package com.tixeon.todos.android.data.respository

import com.tixeon.todos.android.data.local.entity.TaskEntity
import com.tixeon.todos.android.data.remote.response.TaskDto
import com.tixeon.todos.android.util.toTimeInMills

internal fun TaskDto.toTaskEntity(): TaskEntity = TaskEntity(
    remoteId = id.orEmpty(),
    title = title,
    description = description,
    creationDate = creationDate.toTimeInMills(),
    isCompleted = isCompleted,
    isSynced = true,
    localId = localId,
    dependencies = "" // not needed
)

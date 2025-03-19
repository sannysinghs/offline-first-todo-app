package com.tixeon.todos.android.presentation.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Notifications
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tixeon.todos.android.AppTheme
import com.tixeon.todos.android.domain.model.Task

@Composable
fun TaskList(
    tasks: List<Task>,
    onToggleTaskCompleted: (task: Task) -> Unit,
    disableTaskToggle: Boolean,
) {
    LazyColumn {
        itemsIndexed(
            items = tasks,
            key = { _, item -> item.id }
        ) { _, task ->
            key(task.id) {
                val (isCompleted, setIsCompleted) = remember { mutableStateOf(task.isCompleted) }

                TaskListItem(
                    image = task.image,
                    title = task.title,
                    description = task.description,
                    createdDate = task.createdDate,
                    dueDate = task.dueDate.orEmpty(),
                    done = isCompleted,
                    isCheckBoxEnabled = task.isCompletable && !disableTaskToggle,
                    onCheckedChange = { _ ->
                        if (!disableTaskToggle) {
                            setIsCompleted(!isCompleted)
                            onToggleTaskCompleted(task)
                        }

                    }
                )
            }
        }
    }
}

@Composable
fun TaskListItem(
    image: String?,
    title: String,
    description: String,
    createdDate: String,
    dueDate: String,
    done: Boolean,
    isCheckBoxEnabled: Boolean,
    onItemClick: () -> Unit = {},
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Row(modifier = Modifier.clickable { onItemClick() }) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(16.dp)
        ) {
            Text(text = title)
            if (description.isNotEmpty()) {
                Text(text = description)
            }

            Row {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Sharp.Notifications),
                            contentDescription = null
                        )
                        Text(text = "Due:", modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        }

        Column {
            Checkbox(
                enabled = isCheckBoxEnabled,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                checked = done,
                onCheckedChange = onCheckedChange
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TaskListItemPreview() {
    AppTheme {
        TaskListItem(
            image = null,
            title = "Title",
            description = "Description",
            createdDate = "2024-12-17 • 12:00",
            dueDate = "2024-12-18 • 12:00",
            done = false,
            isCheckBoxEnabled = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskListPreview() {
    AppTheme {
        TaskList(
            listOf(
                Task(
                    createdDate = "2024-12-17 • 12:00",
                    dueDate = "2024-12-17 • 13:00",
                    description = "Encrypted description",
                    title = "Encrypted Title",
                    id = "1",
                    image = "",
                    isCompletable = false,
                    isCompleted = true,
                ),
                Task(
                    createdDate = "2024-12-17 • 14:00",
                    dueDate = "2024-12-17 • 15:00",
                    description = "Encrypted description",
                    title = "Encrypted Title",
                    id = "2",
                    image = "",
                    isCompletable = true,
                    isCompleted = false,
                )
            ),
            onToggleTaskCompleted = {},
            false
        )
    }
}
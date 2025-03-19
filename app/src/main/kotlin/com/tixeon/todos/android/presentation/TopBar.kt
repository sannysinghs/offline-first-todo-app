package com.tixeon.todos.android.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(title: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )


            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.Notifications, contentDescription = "Wifi")
            }
        }
    }
}

@Composable
fun TaskTabs(
    allTasksCount: Int,
    upcomingTaskCount: Int,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        contentColor = MaterialTheme.colorScheme.primary, // Or your preferred content color
    ) {
        Tab(
            text = { Text("All tasks ($allTasksCount)") }, // Replace with your actual task counts
            selected = selectedTabIndex == 0,
            onClick = { onTabSelected(0) }
        )
        Tab(
            text = { Text("Upcoming tasks ($upcomingTaskCount)") }, // Replace with your actual task counts
            selected = selectedTabIndex == 1,
            onClick = { onTabSelected(1) }
        )
    }
}

@Preview
@Composable
fun TopBarPreview() {
    MaterialTheme {
        Column {
            TopBar(title = "Master screen")
            TaskTabs(
                allTasksCount = 10,
                upcomingTaskCount = 5,
                selectedTabIndex = 0, onTabSelected = {  })

            // Display task list based on selected tab
            // Display all tasks
            Text("All Tasks Content") // Replace with your actual task list
        }
    }
}
package com.tixeon.todos.android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tixeon.todos.android.domain.model.Task
import com.tixeon.todos.android.presentation.HomeTopAppBar
import com.tixeon.todos.android.presentation.tasks.TaskList
import com.tixeon.todos.android.presentation.tasks.TaskViewState
import com.tixeon.todos.android.util.composables.GenericErrorScreen
import com.tixeon.todos.android.util.composables.GenericLoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    taskViewState: TaskViewState,
    onClickAddTask: (String, String) -> Unit,
    onToggleCompleted: (Task) -> Unit
) {

    val topAppBarState = rememberTopAppBarState()

    Scaffold(
        topBar = {
            HomeTopAppBar(
                topAppBarState = topAppBarState,
            )
        }
    ) { contentPadding ->
        Box (modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(top = contentPadding.calculateTopPadding())
            ) {
                when (taskViewState) {
                    is TaskViewState.Loading -> {
                        GenericLoadingScreen()
                    }

                    is TaskViewState.Success -> {
                        Box{
                            TaskList(
                                tasks = taskViewState.result.allTasks,
                                disableTaskToggle = false,
                                onToggleTaskCompleted = onToggleCompleted
                            )
                        }
                    }

                    is TaskViewState.Error -> {
                        GenericErrorScreen(taskViewState.errorMessage)
                    }
                }
            }

            // State for the text input field
            var textInput by remember { mutableStateOf("") }
            // Sticky bottom section
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Align the row to the bottom center of the Box
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Add spacing between text field and button
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    label = { Text("Enter text") },
                    modifier = Modifier.weight(1f) // Make the text field take up available space
                )

                Button(
                    onClick = {
                        onClickAddTask(textInput, "")
                        textInput = ""
                    }
                ) {
                    Text("Send")
                }
            }
        }
    }

}

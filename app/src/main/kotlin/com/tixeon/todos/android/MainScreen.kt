package com.tixeon.todos.android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tixeon.todos.android.presentation.tasks.TaskList
import com.tixeon.todos.android.presentation.TaskTabs
import com.tixeon.todos.android.presentation.tasks.TaskViewModel
import com.tixeon.todos.android.presentation.tasks.TaskViewState
import com.tixeon.todos.android.presentation.TopBar
import com.tixeon.todos.android.presentation.tasks.ToggleTaskCompleteViewState
import com.tixeon.todos.android.util.composables.ActionLoadingScreen
import com.tixeon.todos.android.util.composables.GenericErrorScreen
import com.tixeon.todos.android.util.composables.GenericLoadingScreen

@Composable
fun MainScreen(
    viewModel: TaskViewModel
) {
    val state = viewModel.viewStateFlow.collectAsStateWithLifecycle()
    val action = viewModel.toggleTaskCompleteViewStateStateFlow.collectAsStateWithLifecycle()

    val (selectedTabIndex, setSelectedTabIndex) = remember { mutableIntStateOf(0) } // State for selected tab

    Column {
        TopBar(title = "Super todo")
        when (val viewState = state.value) {
            is TaskViewState.Loading -> {
                GenericLoadingScreen()
            }

            is TaskViewState.Success -> {
                TaskTabs(
                    allTasksCount = viewState.result.allTasks.size,
                    upcomingTaskCount = viewState.result.upcomingTasks.size,
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { setSelectedTabIndex(it) }
                )
                Box {
                    if (action.value is ToggleTaskCompleteViewState.Loading) {
                        ActionLoadingScreen()
                    }

                    // Display all tasks
                    TaskList(
                        tasks = if (selectedTabIndex == 0) {
                            viewState.result.allTasks
                        } else {
                            viewState.result.upcomingTasks
                        },
                        disableTaskToggle = action.value is ToggleTaskCompleteViewState.Loading,
                        onToggleTaskCompleted = {
                            viewModel.toggleCompleteTask(task = it)
                        }
                    )
                }

            }

            is TaskViewState.Error -> {
                GenericErrorScreen(viewState.errorMessage)
            }
        }
    }
}

package com.tixeon.todos.android.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tixeon.todos.android.MainScreen
import com.tixeon.todos.android.presentation.tasks.TaskViewModel

@Composable
fun TodoNavGraph(
    viewModel: TaskViewModel = viewModel(),
    navController: NavHostController,
    startDestination: String = TodoDestinations.HOME_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(TodoDestinations.HOME_ROUTE) {
            val taskViewState by viewModel.viewStateFlow.collectAsStateWithLifecycle()

            MainScreen(
                taskViewState,
                onClickAddTask = { title, desc ->
                    viewModel.addTask(title, desc)
                },
                onToggleCompleted = { task ->
                    viewModel.toggleCompleteTask(task)
                }
            )
        }
    }
}

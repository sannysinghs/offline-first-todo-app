package com.tixeon.todos.android.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.tixeon.todos.android.presentation.theme.TodoAppTheme

@Composable
fun TodoApp() {
    TodoAppTheme {
        val navController = rememberNavController()
        TodoNavGraph(
            navController = navController,
        )
    }
}
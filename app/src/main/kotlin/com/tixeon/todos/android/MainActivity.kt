package com.tixeon.todos.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tixeon.todos.android.presentation.tasks.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val taskViewModel by viewModels<TaskViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                val taskViewState = taskViewModel.viewStateFlow.collectAsStateWithLifecycle()

                MainScreen(
                    viewModel = taskViewModel,
                    taskViewState = taskViewState.value,
                    onClickAddTask = { title: String, description: String ->
                        taskViewModel.addTask(title, description)
                    }
                )
            }
        }
    }
}
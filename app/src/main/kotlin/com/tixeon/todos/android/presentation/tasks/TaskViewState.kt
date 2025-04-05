package com.tixeon.todos.android.presentation.tasks

/**
 * Represents the different states of the main task screen.
 */
sealed interface TaskViewState {
    /**
     * Indicates that the task data is being loaded.
     */
    data object Loading : TaskViewState
    /**
     * Indicates that the task data has been loaded successfully.
     * @param result The result of the task operation.
     */
    data class Success(val result: TaskResult) : TaskViewState
    /** Indicates that there was an error while loading the task data.
     * @param errorMessage The error message to display.
     */
    data class Error(val errorMessage: String) : TaskViewState
}
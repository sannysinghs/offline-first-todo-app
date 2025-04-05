package com.tixeon.todos.android.presentation.tasks

/**
 * Represents the different states of the ToggleTaskComplete operation.
 */
sealed class ToggleTaskCompleteViewState {
    data object None: ToggleTaskCompleteViewState()
    data object Loading : ToggleTaskCompleteViewState()
    data object Success : ToggleTaskCompleteViewState()
    data class Error(val errorMessage: String) : ToggleTaskCompleteViewState()
}
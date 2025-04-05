package com.tixeon.todos.android.util.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GenericErrorScreen(errorMessage: String) {
    Text(text = errorMessage)
}

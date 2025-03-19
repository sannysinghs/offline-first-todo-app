package com.tixeon.todos.android.util.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GenericLoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Show the CircularProgressIndicator while loading
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp)
        )
    }
}

@Composable
fun ActionLoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
            .clickable {
                println("absorb click event")
            },

        contentAlignment = Alignment.Center
    ) {
        // Show the CircularProgressIndicator while loading
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp)
        )
    }
}

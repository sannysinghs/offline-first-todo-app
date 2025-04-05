package com.tixeon.todos.android.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface AppResourceProvider {
    fun getString(resId: Int): String
}

class AppResourceProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
): AppResourceProvider {
    override fun getString(resId: Int): String = context.getString(resId)
}
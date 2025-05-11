package com.tixeon.todos.android.data.sync

interface Syncable {
    suspend fun syncs(): Boolean
}
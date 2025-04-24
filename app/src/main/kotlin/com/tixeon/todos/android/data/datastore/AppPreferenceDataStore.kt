package com.tixeon.todos.android.data.datastore

interface AppPreferenceDataStore {
    suspend fun getChangeListVersion(): Int
    suspend fun setChangeListVersion(version: Int)
}


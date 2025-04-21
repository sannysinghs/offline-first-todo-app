package com.tixeon.todos.android.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.tixeon.todos.android.data.sync.ChangeListVersion
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class AppPreference(
    val changeListVersion: Int
)

interface AppPreferenceDataStore {
    suspend fun getChangeListVersion(): Int
    suspend fun setChangeListVersion(version: Int)
}

class DefaultAppPreferenceDataStore @Inject constructor(
    private val datastore: DataStore<Preferences>
): AppPreferenceDataStore {
    override suspend fun getChangeListVersion() = datastore.data
        .map {
            ChangeListVersion(
                it[KEY_CHANGE_LIST_VERSION] ?: -1
            )
        }.firstOrNull()?.taskVersion ?: -1


    override suspend fun setChangeListVersion(version: Int) {
        datastore.edit {
            it[KEY_CHANGE_LIST_VERSION] = version
        }
    }

    companion object {
        val KEY_CHANGE_LIST_VERSION = intPreferencesKey("CHANGE_LIST_VERSION")
    }
}
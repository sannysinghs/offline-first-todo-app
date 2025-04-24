package com.tixeon.todos.android.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.tixeon.todos.android.data.datastore.AppPreferenceDataStore
import com.tixeon.todos.android.data.datastore.DefaultAppPreferenceDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = {
            context.preferencesDataStoreFile("app_preferences")
        },
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        migrations = listOf()
    )

    @Provides
    fun providesAppPreferenceDataStore(datastore: DefaultAppPreferenceDataStore): AppPreferenceDataStore =
        datastore
}
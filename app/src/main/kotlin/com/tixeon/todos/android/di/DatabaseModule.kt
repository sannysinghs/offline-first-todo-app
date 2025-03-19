package com.tixeon.todos.android.di

import android.content.Context
import androidx.room.Room
import com.tixeon.todos.android.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "task-db"
    ).build()

    @Singleton
    @Provides
    fun provideToastDao(database: AppDatabase) = database.taskDao()
}
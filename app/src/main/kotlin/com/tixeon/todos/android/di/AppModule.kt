package com.tixeon.todos.android.di

import android.content.Context
import com.tixeon.todos.android.util.AppResourceProvider
import com.tixeon.todos.android.util.AppResourceProviderImpl
import com.tixeon.todos.android.util.CryptoHelper
import com.tixeon.todos.android.util.DefaultCryptoHelper
import com.tixeon.todos.android.util.DefaultDispatcherProvider
import com.tixeon.todos.android.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppResourceProvider(
        @ApplicationContext context: Context
    ): AppResourceProvider = AppResourceProviderImpl(context)

    @Singleton
    @Provides
    fun bindDispatcherProvider(): DispatcherProvider =
        DefaultDispatcherProvider()

    @Singleton
    @Provides
    fun provideCryptoHelper(
        cryptoHelper: DefaultCryptoHelper
    ): CryptoHelper = cryptoHelper
}
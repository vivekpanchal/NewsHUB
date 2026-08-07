package com.vivekpanchal.newshub.di

import android.content.Context
import androidx.room.Room
import com.vivekpanchal.newshub.data.local.AppDatabase
import com.vivekpanchal.newshub.data.local.NewsHeadlineDao
import com.vivekpanchal.newshub.data.local.UserPreferencesDataStore
import com.vivekpanchal.newshub.data.connectivity.ConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME).build()

    @Provides
    fun provideNewsHeadlineDao(database: AppDatabase): NewsHeadlineDao = database.newsHeadlineDao()

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(@ApplicationContext context: Context): UserPreferencesDataStore =
        UserPreferencesDataStore(context)

    @Provides
    @Singleton
    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver =
        ConnectivityObserver(context)
}

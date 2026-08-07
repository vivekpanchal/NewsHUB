package com.vivekpanchal.newshub.di

import com.vivekpanchal.newshub.data.repository.FavoritesRepository
import com.vivekpanchal.newshub.data.repository.FavoritesRepositoryImpl
import com.vivekpanchal.newshub.data.repository.NewsRepository
import com.vivekpanchal.newshub.data.repository.NewsRepositoryImpl
import com.vivekpanchal.newshub.data.repository.UserPreferencesRepository
import com.vivekpanchal.newshub.data.repository.UserPreferencesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(impl: NewsRepositoryImpl): NewsRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository
}

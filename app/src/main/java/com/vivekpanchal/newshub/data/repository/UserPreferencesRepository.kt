package com.vivekpanchal.newshub.data.repository

import com.vivekpanchal.newshub.data.local.UserPreferencesDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface UserPreferencesRepository {
    val isFirstLaunch: Flow<Boolean>
    suspend fun setFirstLaunchDone()
    val userInterests: Flow<List<String>>
    suspend fun setUserInterests(choices: List<String>)
}

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: UserPreferencesDataStore,
) : UserPreferencesRepository {
    override val isFirstLaunch: Flow<Boolean> get() = dataStore.isFirstLaunch
    override suspend fun setFirstLaunchDone() = dataStore.setFirstLaunchDone()
    override val userInterests: Flow<List<String>> get() = dataStore.userInterests
    override suspend fun setUserInterests(choices: List<String>) = dataStore.setUserInterests(choices)
}

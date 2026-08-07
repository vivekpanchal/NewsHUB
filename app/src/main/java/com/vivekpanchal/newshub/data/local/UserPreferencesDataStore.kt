package com.vivekpanchal.newshub.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vivekpanchal.newshub.domain.model.Categories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "newshub_preferences")

/** Replaces the legacy `Utility` SharedPreferences helper backed by "NewsDashSharedPreference". */
class UserPreferencesDataStore(private val context: Context) {

    private object Keys {
        val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
        val USER_INTERESTS = stringPreferencesKey("user_interests")
    }

    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.IS_FIRST_LAUNCH] ?: true
    }

    suspend fun setFirstLaunchDone() {
        context.dataStore.edit { it[Keys.IS_FIRST_LAUNCH] = false }
    }

    val userInterests: Flow<List<String>> = context.dataStore.data.map { prefs ->
        prefs[Keys.USER_INTERESTS]?.split(",")?.filter { it.isNotBlank() }
            ?: Categories.DEFAULT_CHOICES
    }

    suspend fun setUserInterests(choices: List<String>) {
        context.dataStore.edit { it[Keys.USER_INTERESTS] = choices.joinToString(",") }
    }
}

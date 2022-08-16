package com.example.recipeapp.data.datastore

import android.content.Context

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val SORT_PREFERENCE_NAME = "sort_preference"

private val Context.dataStore:DataStore<Preferences> by preferencesDataStore(
    name = SORT_PREFERENCE_NAME
)

class SortSettingsDataStore(context: Context) {

    private val SORT_ASD = booleanPreferencesKey("sort")

    suspend fun saveSortToPreferencesStore(sortedASD:Boolean, context: Context){
        context.dataStore.edit { preferences ->
            preferences[SORT_ASD] = sortedASD
        }
    }

    val preferenceFlow: Flow<Boolean> = context.dataStore.data
        .catch {
            if(it is IOException){
                it.printStackTrace()
                emit(emptyPreferences())
            }else{
                throw it
            }
        }
        .map { preferences->
            preferences[SORT_ASD] ?: true
        }
}
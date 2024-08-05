package edu.rit.nn7716.steakapp.data.api.model

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = "favorites")

class DataStoreHelper(context: Context) {

    private val dataStore = context.dataStore

    suspend fun saveFavorite(steakId: Int, isFavorite: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(steakId.toString())] = isFavorite
        }
    }

    suspend fun getFavorites(): Map<Int, Boolean> {
        return dataStore.data.map { preferences ->
            preferences.asMap().mapKeys { it.key.name.toInt() }.mapValues { it.value as Boolean }
        }.first()
    }


}
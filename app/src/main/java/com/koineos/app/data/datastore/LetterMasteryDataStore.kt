package com.koineos.app.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.letterMasteryDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "letter_mastery_preferences"
)

/**
 * Manages letter mastery progress using DataStore
 */
@Singleton
class LetterMasteryDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Gets the mastery level for a specific letter
     *
     * @param letterId The letter ID
     * @return Flow of mastery level from 0.0 to 1.0
     */
    fun getLetterMasteryLevel(letterId: String): Flow<Float> {
        val key = floatPreferencesKey("letter_mastery_$letterId")
        return context.letterMasteryDataStore.data.map { preferences ->
            preferences[key] ?: 0f
        }
    }

    /**
     * Updates the mastery level for a specific letter
     *
     * @param letterId The letter ID
     * @param masteryLevel New mastery level from 0.0 to 1.0
     */
    suspend fun updateLetterMasteryLevel(letterId: String, masteryLevel: Float) {
        val key = floatPreferencesKey("letter_mastery_$letterId")
        context.letterMasteryDataStore.edit { preferences ->
            preferences[key] = masteryLevel
        }
    }

    /**
     * Gets all letter mastery levels as a map
     *
     * @return Flow of map from letter ID to mastery level
     */
    fun getAllLetterMasteryLevels(): Flow<Map<String, Float>> {
        return context.letterMasteryDataStore.data.map { preferences ->
            preferences.asMap().mapNotNull { (key, value) ->
                val keyName = key.name
                if (keyName.startsWith("letter_mastery_")) {
                    val letterId = keyName.removePrefix("letter_mastery_")
                    letterId to (value as? Float ?: 0f)
                } else {
                    null
                }
            }.toMap()
        }
    }
}
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

/**
 * Preferences DataStore for alphabet mastery
 */
private val Context.alphabetMasteryDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "alphabet_mastery_preferences"
)

/**
 * Manages alphabet mastery progress using DataStore
 *
 * @property context The application context
 */
@Singleton
class AlphabetMasteryDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Gets the mastery level for a specific alphabet entity
     *
     * @param alphabetEntityId The alphabet entity ID
     * @return Flow of mastery level from 0.0 to 1.0
     */
    fun getAlphabetEntityMasteryLevel(alphabetEntityId: String): Flow<Float> {
        val key = floatPreferencesKey("alphabet_entity_mastery_$alphabetEntityId")
        return context.alphabetMasteryDataStore.data.map { preferences ->
            preferences[key] ?: 0f
        }
    }

    /**
     * Updates the mastery level for a specific alphabet entity
     *
     * @param alphabetEntityId The alphabet entity ID
     * @param masteryLevel New mastery level from 0.0 to 1.0
     */
    suspend fun updateAlphabetEntityMasteryLevel(alphabetEntityId: String, masteryLevel: Float) {
        val key = floatPreferencesKey("alphabet_entity_mastery_$alphabetEntityId")
        context.alphabetMasteryDataStore.edit { preferences ->
            preferences[key] = masteryLevel
        }
    }

    /**
     * Gets all alphabet entity mastery levels as a map
     *
     * @return [Flow] of map from alphabet entity ID to mastery level
     */
    fun getAllAlphabetEntityMasteryLevels(): Flow<Map<String, Float>> {
        return context.alphabetMasteryDataStore.data.map { preferences ->
            preferences.asMap().mapNotNull { (key, value) ->
                val keyName = key.name
                if (keyName.startsWith("alphabet_entity_mastery_")) {
                    val alphabetEntityId = keyName.removePrefix("alphabet_entity_mastery_")
                    alphabetEntityId to (value as? Float ?: 0f)
                } else {
                    null
                }
            }.toMap()
        }
    }
}
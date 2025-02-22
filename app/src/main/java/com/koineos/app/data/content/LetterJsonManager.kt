package com.koineos.app.data.content

import android.content.Context
import com.koineos.app.data.content.dto.LetterDto
import com.koineos.app.data.content.dto.LettersResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager responsible for retrieving letter data from JSON files
 *
 * CURRENTLY NOT USED
 */
@Singleton
class LetterJsonManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val TAG = "LetterJsonManager"

    /**
     * Retrieves all letters from the letters.json file
     */
    fun getAllLetters(): Flow<LettersResponse> = flow {
        withContext(Dispatchers.IO) {
            val jsonString = context.assets.open("letters.json").bufferedReader().use { it.readText() }
            val adapter = moshi.adapter(LettersResponse::class.java)
            val lettersResponse = adapter.fromJson(jsonString)
            lettersResponse
        }?.let { emit(it) }
    }

    /**
     * Retrieves a single letter by its ID
     */
    fun getLetterById(id: String): Flow<LetterDto?> = flow {
        getAllLetters().collect { response ->
            emit(response.letters.find { it.id == id })
        }
    }

    /**
     * Retrieves a group of letters by range (useful for learning subsets)
     * @param fromOrder Starting position (inclusive)
     * @param toOrder Ending position (inclusive)
     */
    fun getLettersByRange(fromOrder: Int, toOrder: Int): Flow<List<LetterDto>> = flow {
        getAllLetters().collect { response ->
            emit(
                response.letters.filter {
                    it.order in fromOrder..toOrder
                }.sortedBy { it.order }
            )
        }
    }
}
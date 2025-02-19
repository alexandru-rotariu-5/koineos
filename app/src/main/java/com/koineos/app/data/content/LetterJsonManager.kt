package com.koineos.app.data.content

import android.content.Context
import com.koineos.app.data.content.dto.LetterDto
import com.koineos.app.data.content.dto.LettersResponse
import com.koineos.app.data.utils.StorageUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager responsible for retrieving letter data from JSON files
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
    suspend fun getAllLetters(): Result<LettersResponse> = withContext(Dispatchers.IO) {
        StorageUtils.tryExecuteLocalRequest(TAG) {
            val jsonString = context.assets.open("letters.json").bufferedReader().use { it.readText() }
            val adapter = moshi.adapter(LettersResponse::class.java)
            val lettersResponse = adapter.fromJson(jsonString)

            if (lettersResponse != null) {
                Result.success(lettersResponse)
            } else {
                Result.failure(IOException("Failed to parse letters.json"))
            }
        }
    }

    /**
     * Retrieves a single letter by its ID
     */
    suspend fun getLetterById(id: String): Result<LetterDto?> {
        return StorageUtils.tryExecuteLocalRequest("$TAG-getLetterById") {
            getAllLetters().map { response ->
                response.letters.find { it.id == id }
            }
        }
    }

    /**
     * Retrieves a group of letters by range (useful for learning subsets)
     * @param fromOrder Starting position (inclusive)
     * @param toOrder Ending position (inclusive)
     */
    suspend fun getLettersByRange(fromOrder: Int, toOrder: Int): Result<List<LetterDto>> {
        return StorageUtils.tryExecuteLocalRequest("$TAG-getLettersByRange") {
            getAllLetters().map { response ->
                response.letters.filter {
                    it.order in fromOrder..toOrder
                }.sortedBy { it.order }
            }
        }
    }
}
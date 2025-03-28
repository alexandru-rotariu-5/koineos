package com.koineos.app.domain.utils.practice.alphabet

import androidx.annotation.StringRes
import com.koineos.app.R
import com.koineos.app.domain.model.practice.alphabet.AlphabetBatch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provider for theory screen titles and descriptions based on batch types.
 */
@Singleton
class AlphabetTheoryTextProvider @Inject constructor() {

    /**
     * Gets the appropriate title resource ID for a given batch.
     *
     * @param batch The alphabet batch
     * @return String resource ID for the batch title
     */
    @StringRes
    fun getTitleResourceId(batch: AlphabetBatch): Int {
        val batchType = getBatchType(batch)
        val batchNumber = extractBatchNumber(batch.id)

        return when (batchType) {
            BatchType.LETTERS -> {
                when (batchNumber) {
                    1 -> R.string.theory_title_letters_batch_1
                    2 -> R.string.theory_title_letters_batch_2
                    3 -> R.string.theory_title_letters_batch_3
                    4 -> R.string.theory_title_letters_batch_4
                    5 -> R.string.theory_title_letters_batch_5
                    6 -> R.string.theory_title_letters_batch_6
                    else -> R.string.theory_title_letters_batch_1 // Default
                }
            }
            BatchType.DIPHTHONGS -> {
                when (batchNumber) {
                    1 -> R.string.theory_title_diphthongs_batch_1
                    2 -> R.string.theory_title_diphthongs_batch_2
                    else -> R.string.theory_title_diphthongs_batch_1 // Default
                }
            }
            BatchType.IMPROPER_DIPHTHONGS -> R.string.theory_title_improper_diphthongs
            BatchType.BREATHING_MARKS -> R.string.theory_title_breathing_marks
            BatchType.ACCENT_MARKS -> R.string.theory_title_accent_marks
            else -> R.string.theory_title_letters_batch_1 // Default fallback
        }
    }

    /**
     * Gets the appropriate intro text resource ID for a given batch.
     *
     * @param batch The alphabet batch
     * @return String resource ID for the batch intro text
     */
    @StringRes
    fun getIntroResourceId(batch: AlphabetBatch): Int {
        val batchType = getBatchType(batch)
        val batchNumber = extractBatchNumber(batch.id)

        return when (batchType) {
            BatchType.LETTERS -> {
                when (batchNumber) {
                    1 -> R.string.theory_intro_letters_batch_1
                    2 -> R.string.theory_intro_letters_batch_2
                    3 -> R.string.theory_intro_letters_batch_3
                    4 -> R.string.theory_intro_letters_batch_4
                    5 -> R.string.theory_intro_letters_batch_5
                    6 -> R.string.theory_intro_letters_batch_6
                    else -> R.string.theory_intro_letters_batch_1 // Default
                }
            }
            BatchType.DIPHTHONGS -> {
                when (batchNumber) {
                    1 -> R.string.theory_intro_diphthongs_batch_1
                    2 -> R.string.theory_intro_diphthongs_batch_2
                    else -> R.string.theory_intro_diphthongs_batch_1 // Default
                }
            }
            BatchType.IMPROPER_DIPHTHONGS -> R.string.theory_intro_improper_diphthongs
            BatchType.BREATHING_MARKS -> R.string.theory_intro_breathing_marks
            BatchType.ACCENT_MARKS -> R.string.theory_intro_accent_marks
            else -> R.string.theory_intro_letters_batch_1 // Default fallback
        }
    }

    /**
     * Determines the batch type from the first entity in the batch.
     */
    private fun getBatchType(batch: AlphabetBatch): BatchType {
        val firstEntityId = batch.entities.firstOrNull()?.id ?: return BatchType.UNKNOWN

        return when {
            firstEntityId.startsWith("letter_") -> BatchType.LETTERS
            firstEntityId.startsWith("diphthong_") -> BatchType.DIPHTHONGS
            firstEntityId.startsWith("improper_diphthong_") -> BatchType.IMPROPER_DIPHTHONGS
            firstEntityId.startsWith("breathing_") -> BatchType.BREATHING_MARKS
            firstEntityId.startsWith("accent_") -> BatchType.ACCENT_MARKS
            else -> BatchType.UNKNOWN
        }
    }

    /**
     * Extracts batch number from batch ID.
     */
    private fun extractBatchNumber(batchId: String): Int {
        val numberString = batchId.split("_").lastOrNull() ?: return 1
        return numberString.toIntOrNull() ?: 1
    }

    /**
     * Enum for the different types of batches.
     */
    private enum class BatchType {
        LETTERS,
        DIPHTHONGS,
        IMPROPER_DIPHTHONGS,
        BREATHING_MARKS,
        ACCENT_MARKS,
        UNKNOWN
    }
}
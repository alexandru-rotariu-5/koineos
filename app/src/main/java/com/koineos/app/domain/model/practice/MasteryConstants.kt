package com.koineos.app.domain.model.practice

/**
 * Constants used for mastery level calculations.
 */
object MasteryConstants {

    /**
     * Base rate at which mastery increases for correct answers.
     */
    const val BASE_LEARNING_RATE = 0.15f

    /**
     * Base rate at which mastery decreases for incorrect answers.
     */
    const val BASE_FORGET_RATE = 0.10f

    /**
     * Exercise weight for SelectTransliteration exercises.
     */
    const val SELECT_TRANSLITERATION_WEIGHT = 1.0f

    /**
     * Exercise weight for SelectLemma exercises.
     */
    const val SELECT_LEMMA_WEIGHT = 1.2f

    /**
     * Exercise weight for MatchPairs exercises.
     */
    const val MATCH_PAIRS_WEIGHT = 0.8f
}
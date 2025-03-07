package com.koineos.app.domain.model.practice

/**
 * Represents the possible states of a practice flow.
 * Used to control UI rendering and business logic during a practice session.
 */
enum class PracticeFlowState {
    /**
     * Initial state before the practice session has started.
     */
    INITIAL,

    /**
     * User is actively working on an exercise.
     */
    IN_PROGRESS,

    /**
     * Showing feedback for the current exercise.
     */
    FEEDBACK,

    /**
     * Practice session has been completed.
     */
    COMPLETED
}
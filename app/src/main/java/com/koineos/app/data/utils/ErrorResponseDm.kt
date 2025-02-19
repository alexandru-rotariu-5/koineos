package com.koineos.app.data.utils

/**
 * Represents an error response data model that encapsulates detailed error information.
 *
 * @property errorMessages A list of error messages providing details about the error.
 * @property errorDescription A textual description of the error.
 * @property errorCause The underlying cause of the error, if available.
 * @property errorType The type of error, if available.
 * @property operationErrorCode An optional error code related to the operation that failed.
 */
data class ErrorResponseDm(
    val errorMessages: List<ErrorMessage>? = null,
    val errorDescription: String? = null,
    val errorCause: Throwable? = null,
    val errorType: ErrorType? = null,
    val operationErrorCode: String? = null,
) : Exception(errorCause)

/**
 * Represents an individual error message with an associated error code.
 *
 * @property errorCode A unique identifier for the specific error.
 * @property message A human-readable message explaining the error.
 */
data class ErrorMessage(
    val errorCode: String? = null,
    val message: String? = null,
)

/**
 * Defines the different types of errors that can occur.
 */
enum class ErrorType {
    /** Represents a system-related issue such as database failure or network errors. */
    TECHNICAL_ERROR,

    /** Represents an error related to business logic, such as invalid input data. */
    BUSINESS_ERROR,

    /** Represents an authorization failure where the user lacks necessary permissions. */
    UNAUTHORIZED,

    /** Represents a resource that could not be found. */
    NOT_FOUND
}

package com.koineos.app.data.utils

import android.util.Log

object StorageUtils {
    suspend fun <R> tryExecuteLocalRequest(
        tag: String? = null,
        block: suspend () -> Result<R>
    ): Result<R> {
        return try {
            val response = block()
            Log.d(tag ?: "StorageUtils", "Response: $response")
            return response
        } catch (e: Exception) {
            Log.e(tag ?: "StorageUtils", "Exception during local request", e)
            Result.failure(
                ErrorResponseDm(
                    errorMessages = listOf(ErrorMessage(message = e.message)),
                    errorDescription = "Exception during local request",
                    errorCause = e,
                )
            )
        }
    }
}

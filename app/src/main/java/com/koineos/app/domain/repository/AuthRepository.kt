package com.koineos.app.domain.repository

import com.koineos.app.domain.model.auth.AuthResult
import com.koineos.app.domain.model.auth.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication-related operations.
 */
interface AuthRepository {
    // Current user info
    val currentUser: Flow<User?>
    val isUserAuthenticated: Flow<Boolean>

    // Authentication methods
    suspend fun signInWithEmail(email: String, password: String): AuthResult
    suspend fun signUpWithEmail(name: String, email: String, password: String): AuthResult

    // Account management
    suspend fun resetPassword(email: String): AuthResult
    suspend fun sendEmailVerification(): AuthResult
    suspend fun updateUserProfile(name: String?, photoUrl: String?): AuthResult
    suspend fun signOut()
}
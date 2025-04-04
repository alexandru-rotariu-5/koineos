package com.koineos.app.presentation.manager

import com.koineos.app.domain.model.auth.AuthResult
import com.koineos.app.domain.usecase.auth.IsUserAuthenticatedUseCase
import com.koineos.app.domain.usecase.auth.SignInWithEmailUseCase
import com.koineos.app.domain.usecase.auth.SignOutUseCase
import com.koineos.app.domain.usecase.auth.SignUpWithEmailUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages authentication state of the user
 *
 * @param isUserAuthenticatedUseCase Use case to check if the user is authenticated
 * @param signInWithEmailUseCase Use case to sign in the user with email and password
 * @param signUpWithEmailUseCase Use case to sign up the user with email and password
 * @param signOutUseCase Use case to sign out the user
 */
@Singleton
class AuthManager @Inject constructor(
    isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase,
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signUpWithEmailUseCase: SignUpWithEmailUseCase,
    private val signOutUseCase: SignOutUseCase
) {
    // Flow to check if the user is authenticated
    val isAuthenticated: Flow<Boolean> = isUserAuthenticatedUseCase()

    suspend fun signInWithEmail(email: String, password: String): AuthResult {
        return signInWithEmailUseCase(email, password)
    }

    suspend fun signUpWithEmail(name: String, email: String, password: String): AuthResult {
        return signUpWithEmailUseCase(name, email, password)
    }

    suspend fun signOut() {
        signOutUseCase()
    }
}
package com.koineos.app.domain.usecase.auth

import com.koineos.app.domain.model.auth.AuthResult
import com.koineos.app.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpWithEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): AuthResult {
        return authRepository.signUpWithEmail(name, email, password)
    }
}
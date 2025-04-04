package com.koineos.app.domain.usecase.auth

import com.koineos.app.domain.model.auth.User
import com.koineos.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<User?> {
        return authRepository.currentUser
    }
}
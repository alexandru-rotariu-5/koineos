package com.koineos.app.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koineos.app.domain.model.auth.AuthResult
import com.koineos.app.domain.usecase.auth.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

    private val _successEvent = MutableSharedFlow<Unit>()
    val successEvent: SharedFlow<Unit> = _successEvent.asSharedFlow()

    fun updateEmail(value: String) {
        _email.value = value
        validateEmail(value)
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            _emailError.value = "Email cannot be empty"
            false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailError.value = "Invalid email format"
            false
        } else {
            _emailError.value = null
            true
        }
    }

    fun resetPassword() {
        val isEmailValid = validateEmail(_email.value)

        if (!isEmailValid) {
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                when (val result = resetPasswordUseCase(_email.value)) {
                    is AuthResult.Success -> {
                        _successEvent.emit(Unit)
                    }
                    is AuthResult.Error -> {
                        _errorEvent.emit(result.message)
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}
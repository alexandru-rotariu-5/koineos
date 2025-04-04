package com.koineos.app.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koineos.app.domain.model.auth.AuthResult
import com.koineos.app.presentation.manager.AuthManager
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
class LoginViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    fun updateEmail(value: String) {
        _email.value = value
        validateEmail(value)
    }

    fun updatePassword(value: String) {
        _password.value = value
        validatePassword(value)
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

    private fun validatePassword(password: String): Boolean {
        return if (password.isEmpty()) {
            _passwordError.value = "Password cannot be empty"
            false
        } else {
            _passwordError.value = null
            true
        }
    }

    fun login() {
        val isEmailValid = validateEmail(_email.value)
        val isPasswordValid = validatePassword(_password.value)

        if (!isEmailValid || !isPasswordValid) {
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                when (val result = authManager.signInWithEmail(_email.value, _password.value)) {
                    is AuthResult.Success -> {
                        // Authentication success will be handled by listener in AuthManager
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
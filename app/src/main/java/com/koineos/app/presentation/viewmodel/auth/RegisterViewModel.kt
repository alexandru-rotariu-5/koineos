package com.koineos.app.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koineos.app.domain.model.auth.AuthResult
import com.koineos.app.presentation.manager.AuthManager
import com.koineos.app.presentation.manager.PasswordValidationManager
import com.koineos.app.ui.components.auth.PasswordRequirement
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
class RegisterViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    private val _passwordRequirements = MutableStateFlow<List<PasswordRequirement>>(emptyList())
    val passwordRequirements: StateFlow<List<PasswordRequirement>> = _passwordRequirements.asStateFlow()

    fun updateName(value: String) {
        _name.value = value
        validateName(value)
    }

    fun updateEmail(value: String) {
        _email.value = value
        validateEmail(value)
    }

    fun updatePassword(value: String) {
        _password.value = value
        _passwordRequirements.value = PasswordValidationManager.validatePassword(value)
        validatePassword(value)
    }

    private fun validateName(name: String): Boolean {
        return if (name.isEmpty()) {
            _nameError.value = "Name cannot be empty"
            false
        } else {
            _nameError.value = null
            true
        }
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
        } else if (!PasswordValidationManager.isPasswordValid(password)) {
            _passwordError.value = "Password does not meet requirements"
            false
        } else {
            _passwordError.value = null
            true
        }
    }

    fun register() {
        val isNameValid = validateName(_name.value)
        val isEmailValid = validateEmail(_email.value)
        val isPasswordValid = validatePassword(_password.value)

        if (!isNameValid || !isEmailValid || !isPasswordValid) {
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                when (val result = authManager.signUpWithEmail(_name.value, _email.value, _password.value)) {
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
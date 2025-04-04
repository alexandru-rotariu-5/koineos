package com.koineos.app.presentation.manager

import com.koineos.app.ui.components.auth.PasswordRequirement

object PasswordValidationManager {

    fun validatePassword(password: String): List<PasswordRequirement> {
        return listOf(
            PasswordRequirement(
                "Lowercase character",
                password.any { it.isLowerCase() }
            ),
            PasswordRequirement(
                "Uppercase character",
                password.any { it.isUpperCase() }
            ),
            PasswordRequirement(
                "Numeric character",
                password.any { it.isDigit() }
            ),
            PasswordRequirement(
                "Special character",
                password.any { !it.isLetterOrDigit() }
            ),
            PasswordRequirement(
                "At least 10 characters",
                password.length >= 10
            )
        )
    }

    fun isPasswordValid(password: String): Boolean {
        val requirements = validatePassword(password)
        return requirements.all { it.isValid }
    }
}
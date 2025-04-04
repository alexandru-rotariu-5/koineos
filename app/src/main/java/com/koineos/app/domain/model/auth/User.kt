package com.koineos.app.domain.model.auth

/**
 * Data class representing a user in the application.
 *
 * @property id The unique identifier of the user.
 * @property name The name of the user.
 * @property email The email address of the user.
 * @property isEmailVerified A flag indicating whether the user's email is verified.
 * @property photoUrl The URL of the user's profile photo.
 */
data class User(
    val id: String,
    val name: String?,
    val email: String?,
    val isEmailVerified: Boolean,
    val photoUrl: String?
)
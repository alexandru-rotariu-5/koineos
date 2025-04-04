package com.koineos.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.koineos.app.domain.model.auth.AuthResult
import com.koineos.app.domain.model.auth.User
import com.koineos.app.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toUser())
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override val isUserAuthenticated: Flow<Boolean> = currentUser.map { it != null }

    override suspend fun signInWithEmail(email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success(result.user?.toUser() ?: throw Exception("Authentication failed"))
        } catch (e: FirebaseAuthInvalidUserException) {
            AuthResult.Error("User doesn't exist or has been disabled")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthResult.Error("Invalid email or password")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Authentication failed")
        }
    }

    override suspend fun signUpWithEmail(
        name: String,
        email: String,
        password: String
    ): AuthResult {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("User creation failed")

            // Update user profile with name
            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }
            user.updateProfile(profileUpdates).await()

            // Send email verification
            user.sendEmailVerification().await()

            AuthResult.Success(user.toUser())
        } catch (e: FirebaseAuthWeakPasswordException) {
            AuthResult.Error("The password is too weak")
        } catch (e: FirebaseAuthUserCollisionException) {
            AuthResult.Error("An account already exists with this email")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthResult.Error("Invalid email format")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Registration failed")
        }
    }

    override suspend fun resetPassword(email: String): AuthResult {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            AuthResult.Success(
                User(
                    id = "",
                    name = null,
                    email = email,
                    isEmailVerified = false,
                    photoUrl = null
                )
            )
        } catch (e: FirebaseAuthInvalidUserException) {
            AuthResult.Error("No user found with this email address")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Password reset failed")
        }
    }

    override suspend fun sendEmailVerification(): AuthResult {
        val user = firebaseAuth.currentUser

        return if (user != null) {
            try {
                user.sendEmailVerification().await()
                AuthResult.Success(user.toUser())
            } catch (e: Exception) {
                AuthResult.Error(e.message ?: "Failed to send verification email")
            }
        } else {
            AuthResult.Error("No user is currently signed in")
        }
    }

    override suspend fun updateUserProfile(name: String?, photoUrl: String?): AuthResult {
        val user = firebaseAuth.currentUser

        return if (user != null) {
            try {
                val profileUpdates = userProfileChangeRequest {
                    name?.let { displayName = it }
                    photoUrl?.let { photoUri = android.net.Uri.parse(it) }
                }

                user.updateProfile(profileUpdates).await()
                AuthResult.Success(user.toUser())
            } catch (e: Exception) {
                AuthResult.Error(e.message ?: "Failed to update profile")
            }
        } else {
            AuthResult.Error("No user is currently signed in")
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    private fun FirebaseUser.toUser(): User {
        return User(
            id = uid,
            name = displayName,
            email = email,
            isEmailVerified = isEmailVerified,
            photoUrl = photoUrl?.toString()
        )
    }
}
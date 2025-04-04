package com.koineos.app.ui.navigation.auth

/**
 * Defines all destinations accessible within the Auth feature
 */
sealed class AuthDestination(val route: String) {

    /**
     * Start screen
     */
    data object Start : AuthDestination("start")

    /**
     * Login screen
     */
    data object Login : AuthDestination("login")

    /**
     * Register screen
     */
    data object Register : AuthDestination("register")

    /**
     * Forgot password screen
     */
    data object ForgotPassword : AuthDestination("forgot_password")
}
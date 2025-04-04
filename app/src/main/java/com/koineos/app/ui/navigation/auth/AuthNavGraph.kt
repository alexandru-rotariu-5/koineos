package com.koineos.app.ui.navigation.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.koineos.app.ui.screens.auth.ForgotPasswordScreen
import com.koineos.app.ui.screens.auth.LoginScreen
import com.koineos.app.ui.screens.auth.RegisterScreen
import com.koineos.app.ui.screens.auth.StartScreen

@Composable
fun AuthNavGraph(
    navController: NavHostController = rememberNavController(),
    onNavigateToMain: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AuthDestination.Start.route
    ) {
        composable(AuthDestination.Start.route) {
            StartScreen(
                onStartLearning = {
                    navController.navigate(AuthDestination.Register.route) {
                        popUpTo(AuthDestination.Start.route) { inclusive = true }
                    }
                },
                onLogin = {
                    navController.navigate(AuthDestination.Login.route) {
                        popUpTo(AuthDestination.Start.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AuthDestination.Login.route) {
            LoginScreen(
                onForgotPassword = {
                    navController.navigate(AuthDestination.ForgotPassword.route) {
                        popUpTo(AuthDestination.Login.route) { inclusive = true }
                    }
                },
                onRegister = {
                    navController.navigate(AuthDestination.Register.route) {
                        popUpTo(AuthDestination.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AuthDestination.Register.route) {
            RegisterScreen(
                onLogin = {
                    navController.navigate(AuthDestination.Login.route) {
                        popUpTo(AuthDestination.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AuthDestination.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackToLogin = {
                    navController.navigate(AuthDestination.Login.route) {
                        popUpTo(AuthDestination.ForgotPassword.route) { inclusive = true }
                    }
                },
                onResetSuccess = {
                    navController.navigate(AuthDestination.Login.route) {
                        popUpTo(AuthDestination.ForgotPassword.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
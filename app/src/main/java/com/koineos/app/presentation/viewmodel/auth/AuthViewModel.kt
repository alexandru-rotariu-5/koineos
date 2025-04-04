package com.koineos.app.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koineos.app.presentation.manager.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {

    private val _navigateToMain = MutableSharedFlow<Unit>()
    val navigateToMain: SharedFlow<Unit> = _navigateToMain

    init {
        viewModelScope.launch {
            authManager.isAuthenticated.collect { isAuth ->
                if (isAuth) {
                    _navigateToMain.emit(Unit)
                }
            }
        }
    }
}
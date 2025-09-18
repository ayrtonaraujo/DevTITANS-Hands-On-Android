package com.example.plaintext.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado que representa os dados da tela de preferências
data class PreferencesState(
    val login: String = "",
    val password: String = "",
    val preencherLogin: Boolean = false
)

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(PreferencesState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        val savedLogin = sharedPreferences.getString("login", "") ?: ""
        val savedPassword = sharedPreferences.getString("password", "") ?: ""
        val shouldFillLogin = sharedPreferences.getBoolean("showLogin", false)

        _uiState.update {
            it.copy(
                login = savedLogin,
                password = savedPassword,
                preencherLogin = shouldFillLogin
            )
        }
    }

    fun updateLogin(login: String) {
        viewModelScope.launch {
            sharedPreferences.edit().putString("login", login).apply()
            _uiState.update { it.copy(login = login) }
        }
    }

    fun updatePassword(password: String) {
        viewModelScope.launch {
            sharedPreferences.edit().putString("password", password).apply()
            _uiState.update { it.copy(password = password) }
        }
    }

    fun updatePreencher(preencher: Boolean) {
        viewModelScope.launch {
            sharedPreferences.edit().putBoolean("showLogin", preencher).apply()
            _uiState.update { it.copy(preencherLogin = preencher) }
        }
    }

    fun checkCredentials(login: String, password: String): Boolean {
        return login == _uiState.value.login && password == _uiState.value.password
    }
}
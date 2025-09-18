package com.example.plaintext.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.data.repository.PasswordDBStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListViewState(
    val passwordList: List<PasswordInfo>,
    val isCollected: Boolean = false
)

@HiltViewModel
class ListViewModel @Inject constructor(
    private val passwordDBStore: PasswordDBStore
) : ViewModel() {

    var listViewState by mutableStateOf(ListViewState(passwordList = emptyList()))
        private set

    init {
        viewModelScope.launch {
            if (passwordDBStore.isEmpty().first()) {
                val examplePassword = PasswordInfo(
                    id = 0,
                    name = "Exemplo",
                    login = "usuario@exemplo.com",
                    password = "senha123",
                    notes = "Esta é uma senha de exemplo para teste."
                )
                passwordDBStore.add(examplePassword)
            }
            passwordDBStore.getList().collect { passwordList ->
                listViewState = listViewState.copy(
                    passwordList = passwordList,
                    isCollected = true
                )
            }
        }
    }

    fun savePassword(password: PasswordInfo) {
        viewModelScope.launch {
            passwordDBStore.save(password)
        }
    }
}
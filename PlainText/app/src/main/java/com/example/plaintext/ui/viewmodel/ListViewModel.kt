package com.example.plaintext.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.data.repository.PasswordDBStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class ListViewState(
    val passwordList: List<PasswordInfo> = emptyList(),
    val isLoading: Boolean = false,
    val isCollected: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ListViewModel @Inject constructor(
    private val passwordDBStore: PasswordDBStore
) : ViewModel() {

    private val _listViewState = mutableStateOf(ListViewState())
    val listViewState: State<ListViewState> = _listViewState

    init {
        loadPasswords()
    }

    fun loadPasswords() {
        viewModelScope.launch {
            try {
                _listViewState.value = _listViewState.value.copy(
                    isLoading = true,
                    error = null
                )

                // Add example password if database is empty
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

                // Collect password list
                passwordDBStore.getList().collect { passwords ->
                    _listViewState.value = _listViewState.value.copy(
                        passwordList = passwords,
                        isCollected = true,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _listViewState.value = _listViewState.value.copy(
                    error = e.message ?: "Erro ao carregar senhas",
                    isLoading = false
                )
            }
        }
    }

    suspend fun savePassword(password: PasswordInfo): Boolean {
        return try {
            passwordDBStore.save(password)
            loadPasswords()
            true
        } catch (e: Exception) {
            _listViewState.value = _listViewState.value.copy(
                error = e.message ?: "Erro desconhecido ao salvar a senha"
            )
            false
        }
    }
    
    fun getPassword(id: Int) = passwordDBStore.get(id)
}
package com.example.plaintext.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.data.repository.PasswordDBStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListViewState(
    val passwordList: List<PasswordInfo> = emptyList(),
    val isCollected: Boolean = false
)

@HiltViewModel
class ListViewModel @Inject constructor(
    private val passwordDBStore: PasswordDBStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListViewState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPasswords()
    }

    private fun loadPasswords() {
        viewModelScope.launch {
            passwordDBStore.getPasswords().collect { passwords ->
                _uiState.update {
                    it.copy(passwordList = passwords, isCollected = true)
                }
            }
        }
    }
}
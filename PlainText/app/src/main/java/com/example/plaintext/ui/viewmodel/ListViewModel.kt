package com.example.plaintext.ui.viewmodel



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.data.repository.PasswordDBStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListViewState(
    var passwordList: List<PasswordInfo>,
    var isCollected: Boolean = false
)

@HiltViewModel
open class ListViewModel @Inject constructor(
    private val passwordDBStore: PasswordDBStore
) : ViewModel() {
    var listViewState by mutableStateOf(ListViewState(passwordList = emptyList()))
        private set

    init {
        viewModelScope.launch {
            // Execute o método getList() do passwordDBStore e colete o resultado
            passwordDBStore.getList().collect { passwordList ->
                listViewState = listViewState.copy(
                    passwordList = passwordList,
                    isCollected = true
                )
            }
        }
    }

    fun savePassword(password: PasswordInfo){
        viewModelScope.launch {
            passwordDBStore.save(password)
        }
    }
}
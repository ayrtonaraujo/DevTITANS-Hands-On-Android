package com.example.plaintext.ui.screens.hello

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.data.repository.PasswordDBStore
import com.example.plaintext.ui.screens.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

// dbSimulator has been replaced with PasswordDBStore

data class PasswordListViewState(
    val passwords: List<PasswordInfo> = emptyList()
)

@HiltViewModel
class ListViewModel @Inject constructor(
    private val passwordDBStore: PasswordDBStore
) : ViewModel() {
    var listState by mutableStateOf(PasswordListViewState())
        private set

    init {
        loadPasswords()
    }

    private fun loadPasswords() {
        viewModelScope.launch {
            try {
                passwordDBStore.getList().collect { passwords ->
                    listState = PasswordListViewState(passwords)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

@Composable
fun HelloScreen(
    modifier: Modifier = Modifier,
    viewModel: ListViewModel = hiltViewModel()
) {
    val passwordState = viewModel.listState

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            passwordState.passwords.isEmpty() -> {
                Text("Nenhuma senha cadastrada", fontSize = 20.sp)
            }
            else -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Total de itens: ${passwordState.passwords.size}",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(16.dp)
                    )
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(passwordState.passwords) { password ->
                            Text(
                                text = "${password.name} - ${password.login}",
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelloScreen(args: Screen.Hello) {
    Scaffold { padding ->
        HelloScreen(Modifier.padding(padding))
    }
}

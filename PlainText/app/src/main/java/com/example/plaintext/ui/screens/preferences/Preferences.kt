package com.example.plaintext.ui.screens.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.plaintext.ui.screens.util.MyInputAlertDialog // Certifique-se que você tem este Composable
import com.example.plaintext.ui.theme.PlainTextTheme
import com.example.plaintext.ui.viewmodel.PreferencesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController?,
    viewModel: PreferencesViewModel = hiltViewModel()
) {
    // 1. Coleta o estado mais recente do ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showLoginDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações") },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {

            // Item para editar o login
            PreferenceItem(
                title = "Setar Login",
                summary = uiState.login.ifEmpty { "Login para entrar no sistema" },
                onClick = { showLoginDialog = true }
            )
            // Item para editar a senha
            PreferenceItem(
                title = "Setar Senha",
                summary = "Senha para entrar no sistema",
                onClick = { showPasswordDialog = true }
            )
            // Item com Switch
            SwitchPreferenceItem(
                title = "Preencher login",
                summary = "Preencher login na tela inicial",
                checked = uiState.preencherLogin, // Usa o valor do ViewModel
                onCheckedChange = { viewModel.updatePreencher(it) } // CHAMA A FUNÇÃO DE SALVAR
            )
        }
    }

    // Dialog para editar o Login
    if (showLoginDialog) {
        MyInputAlertDialog(
            title = "Setar Login",
            label = "Login",
            initialValue = uiState.login,
            onDismiss = { showLoginDialog = false },
            onConfirm = { newLogin ->
                viewModel.updateLogin(newLogin) // CHAMA A FUNÇÃO DE SALVAR
                showLoginDialog = false
            }
        )
    }

    // Dialog para editar a Senha
    if (showPasswordDialog) {
        MyInputAlertDialog(
            title = "Setar Senha",
            label = "Senha",
            isPassword = true,
            onDismiss = { showPasswordDialog = false },
            onConfirm = { newPassword ->
                viewModel.updatePassword(newPassword) // CHAMA A FUNÇÃO DE SALVAR
                showPasswordDialog = false
            }
        )
    }
}

// Composables auxiliares para a UI
@Composable
fun PreferenceItem(title: String, summary: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Text(text = summary, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Divider()
}

@Composable
fun SwitchPreferenceItem(title: String, summary: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = summary, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
    Divider()
}


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    PlainTextTheme {
        Text("Preview da tela de Configurações")
    }
}
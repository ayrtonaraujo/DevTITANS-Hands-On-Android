package com.example.plaintext.ui.screens.editList


import com.example.plaintext.ui.screens.login.TopBarComponent

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.Screen
import com.example.plaintext.ui.screens.login.TopBarComponent
import com.example.plaintext.ui.theme.PlainTextTheme

fun isPasswordEmpty(password: PasswordInfo): Boolean {
    return password.name.isEmpty() && password.login.isEmpty() && password.password.isEmpty() && password.notes.isEmpty()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditList(
    args: Screen.EditList,
    navigateBack: () -> Unit,
    savePassword: suspend (password: PasswordInfo) -> Unit
) {
    val isNewPassword = args.id == 0
    val title = if (isNewPassword) "💾 Adicionar nova senha" else "✏️ Editar Senha"
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var showError by rememberSaveable { mutableStateOf<String?>(null) }

    var nome by rememberSaveable { mutableStateOf("") }
    var usuario by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var notas by rememberSaveable { mutableStateOf("") }
    
    val coroutineScope = rememberCoroutineScope()
    
    // Load password data if editing
    LaunchedEffect(args) {
        // For new passwords, we don't need to load anything
        if (!isNewPassword) {
            // In a real app, you would load the password data here
            // For now, we'll just use empty strings
            // The actual loading is handled by the parent composable
        }
    }

    Scaffold(
        topBar = {
            TopBarComponent(
                title = title,
                onBackClick = if (!isLoading) navigateBack else null
            )
        } 
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditInput(
                textInputLabel = "Nome",
                textState = nome,
                onValueChange = { nome = it }
            )
            EditInput(
                textInputLabel = "Usuário",
                textState = usuario,
                onValueChange = { usuario = it }
            )
            EditInput(
                textInputLabel = "Senha",
                textState = senha,
                onValueChange = { senha = it }
            )
            EditInput(
                textInputLabel = "Notas",
                textState = notas,
                onValueChange = { notas = it },
                textInputHeight = 120
            )

//            Spacer(modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val password = PasswordInfo(
                        id = args.id,
                        name = nome,
                        login = usuario,
                        password = senha,
                        notes = notas
                    )
                    
                    if (isPasswordEmpty(password)) {
                        showError = "Por favor, preencha pelo menos um campo"
                        return@Button
                    }
                    
                    coroutineScope.launch {
                        try {
                            isLoading = true
                            savePassword(password)
                            // Navigation is now handled by the parent after successful save
                        } catch (e: Exception) {
                            showError = e.message ?: "Erro ao salvar a senha"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Salvando..." else "Salvar")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun EditInput(
    textInputLabel: String,
    textState: String,
    onValueChange: (String) -> Unit,
    textInputHeight: Int = 60
) {
    val padding: Int = 30

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(textInputHeight.dp)
            .padding(horizontal = padding.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = textState,
            onValueChange = onValueChange,
            label = { Text(textInputLabel) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Preview(showBackground = true, name = "Modo Adicionar")
@Composable
fun EditListAddPreview() {
    PlainTextTheme {
        EditList(
            args = Screen.EditList(PasswordInfo(0, "", "", "", "")),
            navigateBack = {},
            savePassword = {}
        )
    }
}


@Preview(showBackground = true, name = "Modo Editar")
@Composable
fun EditListEditPreview() {
    PlainTextTheme {
        EditList(
            args = Screen.EditList(PasswordInfo(1, "Facebook", "devtitans", "123456", "Conta principal")),
            navigateBack = {},
            savePassword = {}
        )
    }
}
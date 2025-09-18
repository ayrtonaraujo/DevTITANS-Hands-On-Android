package com.example.plaintext.ui.screens.editList

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.Screen
import com.example.plaintext.ui.theme.PlainTextTheme

fun isPasswordEmpty(password: PasswordInfo): Boolean {
    return password.name.isEmpty() && password.login.isEmpty() && password.password.isEmpty() && password.notes.isEmpty()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditList(
    args: Screen.EditList,
    navigateBack: () -> Unit,
    savePassword: (password: PasswordInfo) -> Unit
) {
    val title = if (isPasswordEmpty(args.password)) "Adicionar nova senha" else "Editar Senha"

    var nome by rememberSaveable { mutableStateOf(args.password.name) }
    var usuario by rememberSaveable { mutableStateOf(args.password.login) }
    var senha by rememberSaveable { mutableStateOf(args.password.password) }
    var notas by rememberSaveable { mutableStateOf(args.password.notes) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
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

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val updatedPassword = PasswordInfo(
                        id = args.password.id,
                        name = nome,
                        login = usuario,
                        password = senha,
                        notes = notas
                    )
                    savePassword(updatedPassword)
                    navigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
            ) {
                Text("Salvar")
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
            modifier = Modifier.fillMaxWidth()
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}

//Modo adicionar
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


//modo editar
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
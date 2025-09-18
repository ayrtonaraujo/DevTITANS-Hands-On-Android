package com.example.plaintext.ui.screens.login



import androidx.compose.ui.graphics.Color

import androidx.compose.foundation.background
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.plaintext.R
import com.example.plaintext.ui.theme.PlainTextTheme
import com.example.plaintext.ui.viewmodel.PreferencesViewModel

@Composable
fun Login_screen(
    navigateToSettings: () -> Unit,
    navigateToList: () -> Unit,
    viewModel: PreferencesViewModel = hiltViewModel() // ViewModel está ativado
) {
    // Lendo o estado (dados salvos) do ViewModel
    val preferencesState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var login by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var salvarLogin by rememberSaveable { mutableStateOf(false) }

    // Efeito para preencher o campo de login com o valor salvo
    LaunchedEffect(preferencesState) {
        if (preferencesState.preencherLogin) {
            login = preferencesState.login
        }
        salvarLogin = preferencesState.preencherLogin
    }

    Scaffold(
        topBar = {
            TopBarComponent(navigateToSettings = navigateToSettings)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderComponent()
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Digite suas credenciais para continuar",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Login") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val novoEstado = !salvarLogin
                        salvarLogin = novoEstado
                        viewModel.updatePreencher(novoEstado)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = salvarLogin,
                    onCheckedChange = {
                        salvarLogin = it
                        viewModel.updatePreencher(it)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Salvar as informações de login")
            }
            Spacer(modifier = Modifier.height(24.dp))

            // LÓGICA DO BOTÃO CORRIGIDA
            Button(
                onClick = {
                    // AGORA CHAMA A FUNÇÃO DO VIEWMODEL PARA VERIFICAR
                    if (viewModel.checkCredentials(login, senha)) {
                        Toast.makeText(context, "Olá $login", Toast.LENGTH_SHORT).show()
                        navigateToList() // Navega para a próxima tela
                    } else {
                        Toast.makeText(context, "Login/senha inválidos!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ENTRAR")
            }
        }
    }
}


// ... O resto do arquivo continua igual ...
@Composable
fun HeaderComponent() {
    Row(
        modifier = Modifier
             .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
            .padding(top = 20.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.cadeado_logo),
            contentDescription = "Logo do App",
            modifier = Modifier.height(80.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "\"O mais seguro ",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = " gerenciador",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = "de senhas.\"",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = "Equipe 2 - DevTitans_08",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
        }
    }
}


@Composable
fun MyAlertDialog(shouldShowDialog: MutableState<Boolean>) {
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = { shouldShowDialog.value = false },
            title = { Text(text = "Sobre") },
            text = { Text(text = "Foi difícil, hein!\nPlainText Password Manager v1.0") },
            confirmButton = {
                Button(onClick = { shouldShowDialog.value = false }) { Text(text = "Ok") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComponent(
    title: String? = null, // Adiciona um parâmetro opcional para o título
    navigateToSettings: (() -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    val shouldShowDialog = remember { mutableStateOf(false) }

    if (shouldShowDialog.value) {
        MyAlertDialog(shouldShowDialog = shouldShowDialog)
    }

    TopAppBar(
        title = { Text(title ?: "ConfiaEmNois Security") },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Configurações") },
                    onClick = {
                        navigateToSettings?.invoke()
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Sobre") },
                    onClick = {
                        shouldShowDialog.value = true
                        expanded = false
                    }
                )
            }
        }
    )
}


@Preview(showBackground = true, name = "Login Screen - Static Layout")
@Composable
fun LoginScreenStaticPreview() {
    PlainTextTheme {
        // Criamos uma versão "fake" da tela que não precisa de ViewModel
        // para checar o layout visualmente.
        Scaffold(
            topBar = { TopBarComponent() }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderComponent()
                // ... (etc, você pode recriar a UI aqui sem a lógica)
                Text("Preview Estático da Tela de Login")
            }
        }
    }
}
package com.example.plaintext.ui.screens.list

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.plaintext.R
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.login.TopBarComponent
import com.example.plaintext.ui.theme.PlainTextTheme
import com.example.plaintext.ui.viewmodel.ListViewModel
import com.example.plaintext.ui.viewmodel.ListViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListView(
    onItemClick: (password: PasswordInfo) -> Unit,
    onAddClick: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ListViewModel = hiltViewModel(),
    onNavigateToEdit: (PasswordInfo) -> Unit = {},
    onNavigateToDetails: (PasswordInfo) -> Unit = {}
) {
    val listState = viewModel.listViewState.value
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadPasswords()
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Sair") },
            text = { Text("Deseja realmente sair?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogout()
                }) {
                    Text("Sim")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Não")
                }
            }
        )
    }
    
    Scaffold(
        topBar = { 
            TopBarComponent(
                title = "🔐 Senhas salvas",
                showLogout = true,
                onLogoutClick = { showLogoutDialog = true }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar")
            }
        }
    ) { padding ->
        when {
            listState.isLoading -> {
                LoadingScreen()
            }
            listState.passwordList.isEmpty() -> {
                EmptyListScreen()
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.padding(padding),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(
                        items = listState.passwordList,
                        key = { it.id }
                    ) { password ->
                        ListItem(
                            password = password,
                            onEditClick = { onNavigateToEdit(password) },
                            onItemClick = { onNavigateToDetails(password) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Carregando")
    }
}

@Composable
fun EmptyListScreen() {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Nenhuma senha salva")
    }
}

@Composable
fun ListItem(
    password: PasswordInfo,
    onEditClick: (PasswordInfo) -> Unit,
    onItemClick: (PasswordInfo) -> Unit
) {
    val title = password.name
    val subTitle = password.login

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onItemClick(password) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left side: Icon and text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cadeado_log_azul),
                    contentDescription = "Logo",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Right side: Action buttons
            Row {
                // Edit button
                IconButton(
                    onClick = { 
                        onEditClick(password)
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                // View details button
                IconButton(
                    onClick = { onItemClick(password) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.Visibility,
                        contentDescription = "Ver detalhes",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
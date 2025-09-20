package com.example.plaintext.ui.screens.list

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
    val listState by viewModel.listViewState
    
    LaunchedEffect(Unit) {
        // Trigger initial load if needed
        if (!listState.isCollected) {
            viewModel.loadPasswords()
        }
    }

    var showLogoutDialog by remember { mutableStateOf(false) }
    
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Sair") },
            text = { Text("Deseja realmente sair do aplicativo?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Sair")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    Scaffold(
        topBar = { 
            TopBarComponent(
                title = "\uD83D\uDD10 Senhas salvas",
                showLogout = true,
                onLogoutClick = { showLogoutDialog = true }
            ) 
        },
        floatingActionButton = { AddButton(onClick = onAddClick) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when {
                !listState.isCollected -> {
                    LoadingScreen()
                }
                listState.passwordList.isEmpty() -> {
                    EmptyListScreen()
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
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
}

@Composable
fun AddButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary
    ) {
        Icon(Icons.Filled.Add, "Small floating action button.")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItemContent(
    modifier: Modifier = Modifier,
    listState: ListViewState,
    navigateToEdit: (password: PasswordInfo) -> Unit,
    onItemClick: (password: PasswordInfo) -> Unit = {}
) {
    when {
        !listState.isCollected -> {
            LoadingScreen()
        }
        listState.passwordList.isEmpty() -> {
            EmptyListScreen()
        }
        else -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
            ) {
                items(
                    items = listState.passwordList,
                    key = { it.id }
                ) {
                    ListItem(
                        password = it,
                        onEditClick = { password -> navigateToEdit(password) },
                        onItemClick = { password -> onItemClick(password) }
                    )
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { onItemClick(password) }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.cadeado_log_azul),
            contentDescription = "Logo",
            modifier = Modifier.fillMaxHeight()
        )
        Column(
            modifier = Modifier
                .weight(.7f)
                .padding(horizontal = 5.dp),
        ) {
            Text(title, fontSize = 20.sp)
            Text(subTitle, fontSize = 14.sp)
        }
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Menu",
            tint = Color.White
        )
    }
}
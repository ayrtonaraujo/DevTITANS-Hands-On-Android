package com.example.plaintext.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.Screen
import com.example.plaintext.ui.screens.login.TopBarComponent
import com.example.plaintext.ui.theme.PlainTextTheme
import com.example.plaintext.ui.viewmodel.ListViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailItem(
    label: String,
    value: String,
    isPassword: Boolean = false,
    onCopy: () -> Unit
) {
    var showCopied by remember { mutableStateOf(false) }
    
    LaunchedEffect(showCopied) {
        if (showCopied) {
            kotlinx.coroutines.delay(2000)
            showCopied = false
        }
    }
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isPassword) "•".repeat(10) else value,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            
            if (showCopied) {
                Text(
                    text = "Copiado!",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            
            IconButton(
                onClick = {
                    onCopy()
                    showCopied = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copiar $label"
                )
            }
        }
        
        Divider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordDetails(
    args: Screen.PasswordDetails,
    navigateBack: () -> Unit,
    viewModel: ListViewModel = hiltViewModel()
) {
    val password by viewModel.getPassword(args.id).collectAsState(initial = null)
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            TopBarComponent(
                title = "Detalhes da Senha",
                onBackClick = navigateBack
            )
        }
    ) { padding ->
        password?.let { pwd ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Name field
                DetailItem(
                    label = "Nome",
                    value = pwd.name,
                    onCopy = { clipboardManager.setText(AnnotatedString(pwd.name)) }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Login field
                DetailItem(
                    label = "Usuário/E-mail",
                    value = pwd.login,
                    onCopy = { clipboardManager.setText(AnnotatedString(pwd.login)) }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Password field
                DetailItem(
                    label = "Senha",
                    value = pwd.password,
                    isPassword = true,
                    onCopy = { clipboardManager.setText(AnnotatedString(pwd.password)) }
                )
                
                // Notes field (only show if not empty)
                if (pwd.notes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Notas",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = pwd.notes,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
        } ?: run {
            // Show loading or error state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Carregando...")
            }
        }
    }
}}



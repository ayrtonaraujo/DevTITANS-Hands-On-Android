package com.example.plaintext.ui.screens.util

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.plaintext.ui.screens.preferences.PreferenceItem

@Composable
fun PreferenceInput(
    title: String,
    label: String,
    fieldValue: String,
    summary: String,
    isPassword: Boolean = false,
    // CORRECT THIS LINE:
    onValueChange: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    // This is the clickable item the user sees
    PreferenceItem(
        title = title,
        summary = fieldValue.ifEmpty { summary },
        onClick = { showDialog = true }
    )

    // This is the dialog that opens when the item is clicked
    if (showDialog) {
        MyInputAlertDialog(
            title = title,
            label = label,
            initialValue = fieldValue,
            isPassword = isPassword,
            onDismiss = { showDialog = false },
            onConfirm = { newValue ->
                onValueChange(newValue)
                showDialog = false
            }
        )
    }
}

// Keep this preview, it's correct
@Preview(showBackground = true)
@Composable
fun PreferenceInputPreview() {
    PreferenceInput(
        title = "Setar Login",
        label = "Login",
        fieldValue = "devtitans",
        summary = "Login para entrar no sistema",
        onValueChange = {}
    )
}
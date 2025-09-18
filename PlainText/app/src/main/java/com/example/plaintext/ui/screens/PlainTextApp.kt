package com.example.plaintext.ui.screens
import androidx.navigation.compose.composable
import com.example.plaintext.ui.screens.preferences.SettingsScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.editList.EditList
import com.example.plaintext.ui.screens.hello.Hello_screen
import com.example.plaintext.ui.screens.list.ListView
import com.example.plaintext.ui.screens.login.Login_screen
import com.example.plaintext.utils.parcelableType
import kotlin.reflect.typeOf

@Composable
fun PlainTextApp(
    appState: JetcasterAppState = rememberJetcasterAppState()
) {
    NavHost(
        navController = appState.navController,
        startDestination = Screen.Login, //tela de login
    ) {
        composable<Screen.Hello> {
            val args = it.toRoute<Screen.Hello>()
            Hello_screen(args)
        }
        composable<Screen.Login> {
            Login_screen(
                navigateToSettings = { appState.navigateToPreferences() },
                navigateToList = { appState.navigateToList() }
            )
        }

        // tela lista aqui
        composable<Screen.List> {
            ListView(
                onItemClick = { password ->
                    // Navega para a tela de edição em modo "Editar"
                    appState.navigateToEditList(password)
                },
                onAddClick = {
                    // Navega para a tela de edição em modo "Adicionar"
                    val newPassword = PasswordInfo(0, "", "", "", "")
                    appState.navigateToEditList(newPassword)
                }
            )
        }

        composable<Screen.EditList>(
            typeMap = mapOf(typeOf<PasswordInfo>() to parcelableType<PasswordInfo>())
        ) {
            val args = it.toRoute<Screen.EditList>()
            EditList(
                args = args,
                navigateBack = { appState.navController.popBackStack() },
                savePassword = { password ->
                    // Lógica para salvar virá aqui
                }
            )
        }

        composable<Screen.Preferences> {
            SettingsScreen(
                navController = appState.navController
            )
        }
    }
}
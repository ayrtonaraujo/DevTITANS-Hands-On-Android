package com.example.plaintext.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.editList.EditList
import com.example.plaintext.ui.screens.hello.HelloScreen
import com.example.plaintext.ui.screens.details.PasswordDetails
import com.example.plaintext.ui.screens.list.ListView
import com.example.plaintext.ui.screens.login.Login_screen
import com.example.plaintext.ui.screens.preferences.SettingsScreen
import com.example.plaintext.ui.viewmodel.ListViewModel

@Composable
fun PlainTextApp(
    appState: JetcasterAppState = rememberJetcasterAppState()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    
    NavHost(
        navController = appState.navController,
        startDestination = "Login"
    ) {
        composable("hello/{name}", arguments = listOf(
            navArgument("name") { type = NavType.StringType; nullable = true }
        )) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            HelloScreen(Screen.Hello(name ?: ""))
        }
        
        composable("Login") {
            Login_screen(
                navigateToSettings = { appState.navigateToPreferences() },
                navigateToList = { appState.navigateToList() }
            )
        }

        composable("List") {
            val viewModel: ListViewModel = hiltViewModel()
            // Get the current state from ViewModel
            val listState = viewModel.listViewState
            
            ListView(
                onItemClick = { password ->
                    // This is now handled by onNavigateToDetails
                },
                onAddClick = {
                    appState.navigateToEditList(0)
                },
                onLogout = {
                    appState.logout()
                },
                viewModel = viewModel,
                onNavigateToEdit = { password ->
                    appState.navigateToEditList(password.id)
                },
                onNavigateToDetails = { password ->
                    appState.navigateToPasswordDetails(password.id)
                }
            )
        }

        composable(
            route = "details/{passwordId}",
            arguments = listOf(
                navArgument("passwordId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val passwordId = backStackEntry.arguments?.getInt("passwordId") ?: 0
            PasswordDetails(
                args = Screen.PasswordDetails(passwordId),
                navigateBack = { appState.navigateBack() }
            )
        }

        composable(
            route = "edit_list/{id}",
            arguments = listOf(
                navArgument("id") { 
                    type = NavType.IntType 
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val passwordId = backStackEntry.arguments?.getInt("id") ?: 0
            val viewModel: ListViewModel = hiltViewModel()
            
            // Create a new password with the given ID
            val password = remember(passwordId) {
                PasswordInfo(
                    id = passwordId,
                    name = "",
                    login = "",
                    password = "",
                    notes = ""
                )
            }
            
            val coroutineScope = rememberCoroutineScope()
            
            EditList(
                args = Screen.EditList(password.id),
                navigateBack = { appState.navigateBack() },
                savePassword = { updatedPassword ->
                    coroutineScope.launch {
                        val success = viewModel.savePassword(updatedPassword)
                        if (success) {
                            appState.navigateBack()
                        }
                    }
                }
            )
        }

        composable("Preferences") {
            SettingsScreen(
                navController = appState.navController
            )
        }
    }

}
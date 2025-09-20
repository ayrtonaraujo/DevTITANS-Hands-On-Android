package com.example.plaintext.ui.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.NavArgument
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.plaintext.data.model.PasswordInfo
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen() {

    @Serializable
    object Login;

    @Serializable
    data class Hello(
        val name: String?
    )

    @Serializable
    object Preferences;

    @Serializable
    object List;

    @Serializable
    data class EditList(
        val id: Int = 0
    ) {
        constructor(password: PasswordInfo) : this(password.id)
    }

    @Serializable
    object sensors;
    
    @Serializable
    data class PasswordDetails(
        val id: Int = 0
    ) {
        constructor(password: PasswordInfo) : this(password.id)
    }
}

@Composable
fun rememberJetcasterAppState(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current
) = remember(navController, context) {
    JetcasterAppState(navController, context)
}


// Substitua a sua classe JetcasterAppState por esta

class JetcasterAppState(
    val navController: NavHostController,
    private val context: Context
) {

    val currentRoute: String?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination?.route

    fun navigateBack() {
        navController.popBackStack()
    }
    
    fun logout() {
        navController.navigate("Login") {
            popUpTo(0) // This will clear the back stack
        }
    }

    fun checkRoute(route: String): Boolean {
        val currentRoute = navController.currentBackStackEntry?.destination?.route.toString()
        return currentRoute != route
    }

    fun navigateToHello(name: String?) {
        navController.navigate("hello/${name ?: ""}")
    }

    fun navigateToLogin(){
        navController.navigate("Login") {
            popUpTo("Login") { inclusive = true }
        }
    }

    // --- FUNÇÕES QUE ESTAVAM FALTANDO ---
    fun navigateToPreferences() {
        navController.navigate("Preferences")
    }

    fun navigateToList() {
        navController.navigate("List") {
            popUpTo("Login") { inclusive = true }
        }
    }

    fun navigateToEditList(passwordId: Int) {
        navController.navigate("edit_list/$passwordId") {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    
    fun navigateToPasswordDetails(passwordId: Int) {
        navController.navigate("details/$passwordId") {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    // --- FIM DAS FUNÇÕES ADICIONADAS ---
}

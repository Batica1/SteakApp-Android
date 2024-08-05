package edu.rit.nn7716.steakapp.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import edu.rit.nn7716.steakapp.data.api.APIService
import edu.rit.nn7716.steakapp.data.api.model.DataStoreHelper
import edu.rit.nn7716.steakapp.data.api.model.Steak
import edu.rit.nn7716.steakapp.screens.HomeScreen
import edu.rit.nn7716.steakapp.screens.SteakDetailView


@Composable
fun NavGraph(navController: NavHostController) {
    val apiService = remember { APIService.getInstance() }
    val scope = rememberCoroutineScope()
    var steakList by remember { mutableStateOf(emptyList<Steak>()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val dataStoreHelper = rememberDataStoreHelper()

    LaunchedEffect(key1 = Unit) {
        isLoading = true
        try {
            val response = apiService.getSteaks()
            steakList = response.steaks
        } catch (e: Exception) {
            errorMessage = e.localizedMessage ?: "An error occurred"
        }
        isLoading = false
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController, dataStoreHelper = dataStoreHelper)
        }
        composable(
            "steak_detail/{steakId}",
            arguments = listOf(navArgument("steakId") { type = NavType.IntType })
        ) { backStackEntry ->
            val steakId = backStackEntry.arguments?.getInt("steakId") ?: -1
            val steak = steakList.find { it.id == steakId }

            steak?.let {
                SteakDetailView(
                    steakId = it.id,
                    steakList = steakList,
                    onBackClick = { navController.popBackStack() })
            }
        }

    }
}

@Composable
fun rememberDataStoreHelper(): DataStoreHelper {
    val context = LocalContext.current
    return remember(context) {
        DataStoreHelper(context)
    }
}
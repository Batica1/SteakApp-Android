package edu.rit.nn7716.steakapp

import SteakTimerScreen
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import createNotificationChannel
import edu.rit.nn7716.steakapp.components.NavDrawerItem
import edu.rit.nn7716.steakapp.data.api.APIService
import edu.rit.nn7716.steakapp.data.api.model.DataStoreHelper
import edu.rit.nn7716.steakapp.data.api.model.Steak
import edu.rit.nn7716.steakapp.data.api.model.SteaksViewModel
import edu.rit.nn7716.steakapp.data.api.model.SteaksViewModelFactory
import edu.rit.nn7716.steakapp.screens.FavoritesScreen
import edu.rit.nn7716.steakapp.screens.HomeScreen
import edu.rit.nn7716.steakapp.screens.MethodsScreen
import edu.rit.nn7716.steakapp.screens.PriceCalculatorScreen
import edu.rit.nn7716.steakapp.screens.SteakDetailView
import edu.rit.nn7716.steakapp.ui.theme.NavigationDemo2Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)
        setContent {
            MainScreen()
        }

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val apiService = remember { APIService.getInstance() }
    val scope = rememberCoroutineScope()
    var steakList by remember { mutableStateOf(emptyList<Steak>()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }



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
    Scaffold(bottomBar = { BottomBar(navController = navController) }) {
        Navigation(navController = navController, steakList = steakList)
    }
}

@Composable
fun rememberDataStoreHelper(): DataStoreHelper {
    val context = LocalContext.current
    return remember(context) {
        DataStoreHelper(context)
    }
}

@Composable
fun Navigation(navController: NavHostController, steakList: List<Steak>) {
    var elapsedTime by remember { mutableStateOf("00:00:00") }
    val dataStoreHelper = rememberDataStoreHelper()

    fun onTimeChanged(newTime: String) {
        elapsedTime = newTime
    }

    NavHost(navController = navController, startDestination = "home") {

        composable(NavDrawerItem.Timer.route) {
            SteakTimerScreen(onTimeChanged = ::onTimeChanged)
        }

        //favorite
        composable(NavDrawerItem.Favorites.route) {
            val dataStoreHelper = rememberDataStoreHelper()
            val viewModel: SteaksViewModel =
                viewModel(factory = SteaksViewModelFactory(dataStoreHelper))

            FavoritesScreen(navController, viewModel = viewModel)
        }

        composable(NavDrawerItem.Home.route) {

            HomeScreen(navController = navController, dataStoreHelper = dataStoreHelper)
        }

        composable(NavDrawerItem.Calculator.route) {
            PriceCalculatorScreen(navController = navController)
        }

        composable(NavDrawerItem.Methods.route) {
            MethodsScreen(navController = navController)
        }


        composable(
            "steak_detail/{steakId}",
            arguments = listOf(navArgument("steakId") { type = NavType.IntType })
        ) { backStackEntry ->
            val steakId = backStackEntry.arguments?.getInt("steakId") ?: -1
            val steak = steakList.find { it.id == steakId }

            steak?.let {
                SteakDetailView(steakId = it.id,
                    steakList = steakList,
                    onBackClick = { navController.popBackStack() })
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(scope: CoroutineScope, drawerState: DrawerState) {
    TopAppBar(title = {
        Text(
            text = stringResource(id = R.string.app_name), fontSize = 18.sp
        )
    }, navigationIcon = {
        IconButton(onClick = {
            scope.launch {
                drawerState.open()
            }
        }) {
            Icon(imageVector = Icons.Filled.Menu, contentDescription = "")
        }
    })
} // TopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(
    modifier: Modifier = Modifier, navController: NavController
) {
    val screens = listOf(

        NavDrawerItem.Timer,
        NavDrawerItem.Favorites,
        NavDrawerItem.Home,
        NavDrawerItem.Calculator,
        NavDrawerItem.Methods,
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->
            NavigationBarItem(icon = {
                BadgedBox(badge = {
                    if (screen.badgeCount != null) {
                        Badge {
                            Text(screen.badgeCount.toString())
                        }
                    } else if (screen.hasNews) {
                        Badge()
                    }
                } // badge
                ) {
                    Icon(
                        imageVector = if (currentRoute == screen.route) screen.selectedIcon
                        else screen.icon, contentDescription = screen.title
                    )
                }// BadgedBox
            }, // icon
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                    }
                })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NavigationDemo2Theme {
        MainScreen()
    }
}
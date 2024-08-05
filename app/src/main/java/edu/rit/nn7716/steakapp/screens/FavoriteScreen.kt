package edu.rit.nn7716.steakapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import edu.rit.nn7716.steakapp.data.api.model.SteaksViewModel
import edu.rit.nn7716.steakapp.ui.theme.DarkColorScheme
import edu.rit.nn7716.steakapp.ui.theme.LightColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController, viewModel: SteaksViewModel) {
    val favoriteSteaks by remember { mutableStateOf(viewModel.favoriteSteaks) }

    var isDarkTheme by remember { mutableStateOf(false) } // Theme switch state


    LaunchedEffect(key1 = Unit) {
        viewModel.getSteaks()
        viewModel.loadFavorites()
    }


    MaterialTheme(colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme) {

        TopAppBar(
            title = {
                Row {
                    Text("Favorite Steaks")
                }
            }

        )

        /*TopEndAlignedSwitch(
            isDarkTheme = isDarkTheme,
            onToggle = { isDarkTheme = it } // Updates the state when switched
        )*/

        if (favoriteSteaks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No favorite steaks added yet.",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp, bottom = 80.dp)

            ) {
                items(favoriteSteaks) { steak ->
                    SteakCard(steak, navController, viewModel)
                }
            }
        }
    }

}







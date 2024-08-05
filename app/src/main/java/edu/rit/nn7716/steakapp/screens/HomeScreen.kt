package edu.rit.nn7716.steakapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import edu.rit.nn7716.steakapp.data.api.model.DataStoreHelper
import edu.rit.nn7716.steakapp.data.api.model.Steak
import edu.rit.nn7716.steakapp.data.api.model.SteaksViewModel
import edu.rit.nn7716.steakapp.ui.theme.DarkColorScheme
import edu.rit.nn7716.steakapp.ui.theme.LightColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, dataStoreHelper: DataStoreHelper) {
    //val vm: SteaksViewModel = viewModel()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearchBarVisible by rememberSaveable { mutableStateOf(false) }

    var isDarkTheme by remember { mutableStateOf(false) } // Theme switch state


    val vm: SteaksViewModel = remember {
        SteaksViewModel(dataStoreHelper)
    }

    LaunchedEffect(key1 = Unit, block = {
        vm.getSteaks()
    })


    MaterialTheme(colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Steaks")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            isSearchBarVisible = !isSearchBarVisible
                            if (!isSearchBarVisible) {
                                searchQuery = ""
                            }
                        }) {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                    },
                    actions = {
                        if (isSearchBarVisible) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { newValue ->
                                    searchQuery = newValue
                                },
                                placeholder = { Text("Search") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = {
                                            searchQuery = ""
                                        }) {
                                            Icon(Icons.Filled.Close, contentDescription = "Clear")
                                        }
                                    }
                                }
                            )
                        }
                    }
                )

                // Theme switch
                /* TopEndAlignedSwitch(
                     isDarkTheme = isDarkTheme,
                     onToggle = { isDarkTheme = it }
                 )*/

            }

        )

        { innerPadding ->
            if (vm.errorMessage.isEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(bottom = 80.dp)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (vm.steakList.isEmpty()) {
                        item {
                            Text(
                                text = "No steaks to show - please connect to the RIT vpn",
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 20.dp,
                                        vertical = 12.dp
                                    )
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            ) // Text

                            Spacer(modifier = Modifier.height(10.dp))

                            AnimatedVisibility(visible = vm.loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.width(64.dp),
                                    color = MaterialTheme.colorScheme.secondary,
                                    strokeWidth = 10.dp
                                )
                            }
                        } // item
                    } // if empty
                    items(items = vm.steakList.filter { steak ->
                        steak.name.contains(searchQuery, ignoreCase = true)
                    }) { steak ->
                        SteakCard(steak, navController, vm)
                    }
                } // LazyColumn
            } // if
            else {
                Text(text = vm.errorMessage)
            }
        } // Scaffold

    }

} // HomeScreen


@Composable
fun SteakCard(steak: Steak, navController: NavController, viewModel: SteaksViewModel) {
    var isFavorite by remember { mutableStateOf(steak.isFavourite) }

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(bottom = 25.dp, start = 25.dp, end = 25.dp)
            .height(390.dp)
            .clickable {
                navController.navigate("steak_detail/${steak.id}")
            }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3EFEE))
                .padding(16.dp)
        ) {
            GlideImage(
                imageModel = { steak.imageUrl },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillBounds,
                    alignment = Alignment.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(bottom = 55.dp, top = 20.dp),

                loading = {
                    Box(modifier = Modifier.matchParentSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                failure = {
                    Text("Image request failed.")
                },


                ) // GlideImage
            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                    viewModel.toggleFavorite(steak)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(15.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite"
                )
            }

            Surface(
                color = Color(0xFF590315),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(15.dp),
                contentColor = Color.Black
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(15.dp)
                ) {
                    Text(
                        text = steak.name,
                        color = Color(0xFFF8F5F4),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = steak.origin,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
            } // Surface
        } // Box
    } // Card
} // end






package edu.rit.nn7716.steakapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.SoupKitchen
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavDrawerItem(
    var route: String,
    var icon: ImageVector,
    var selectedIcon: ImageVector,
    var title: String,
    var hasNews: Boolean,
    var badgeCount: Int? = null
) {

    object Timer : NavDrawerItem(
        "timer",
        Icons.Default.Timer,
        Icons.Rounded.Timer,
        "Timer",
        false)

    object Favorites : NavDrawerItem(
        "favorites",
        Icons.Default.Favorite,
        Icons.Default.Favorite,
        "Favorites",
        false
    )

    object Home : NavDrawerItem(
        "home",
        Icons.Outlined.Home,
        Icons.Filled.Home,
        "Home",
        false)

    object Calculator : NavDrawerItem(
        "calculator",
        Icons.Rounded.Calculate,
        Icons.Rounded.Calculate,
        "Calculator",
        false
    )

    object Methods : NavDrawerItem(
        "methods",
        Icons.Rounded.SoupKitchen,
        Icons.Rounded.SoupKitchen,
        "Guides",
        false
    )


}

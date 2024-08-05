package edu.rit.nn7716.steakapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


//This was supposed to be used for toggle between light and dark theme

@Composable
fun TopEndAlignedSwitch(isDarkTheme: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                end = 12.dp
            ), // Top padding for height, end padding to position switch at end
        horizontalArrangement = Arrangement.End, // Ensures content is arranged at the end
        verticalAlignment = Alignment.Top // Aligns content to the top
    ) {
        Switch(
            checked = isDarkTheme,
            onCheckedChange = onToggle,
            modifier = Modifier.padding(end = 8.dp) // Padding to maintain some space from the edge
        )
    }
}

package edu.rit.nn7716.steakapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.skydoves.landscapist.glide.GlideImage
import edu.rit.nn7716.steakapp.data.api.APIService
import edu.rit.nn7716.steakapp.data.api.model.Steak
import edu.rit.nn7716.steakapp.ui.theme.DarkColorScheme
import edu.rit.nn7716.steakapp.ui.theme.LightColorScheme

data class SelectedSteak(val steak: Steak, val quantity: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceCalculatorScreen(navController: NavHostController) {
    val apiService = remember { APIService.getInstance() }
    var steaks by remember { mutableStateOf(emptyList<Steak>()) }
    val selectedSteaks = remember { mutableStateListOf<SelectedSteak>() }
    val totalPrice = remember { mutableStateOf(0.0) }

    var selectedCurrency by remember { mutableStateOf("EUR") }

    var isDarkTheme by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        try {
            val response = apiService.getSteaks()
            steaks = response.steaks
        } catch (e: Exception) {

        }
    }

    LaunchedEffect(selectedCurrency) {
        selectedSteaks.forEach { selectedSteak ->
            updateSelectedSteak(
                selectedSteak.steak,
                selectedSteak.quantity,
                selectedSteaks,
                totalPrice,
                selectedCurrency
            )
        }
    }


    MaterialTheme(colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(bottom = 85.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 15.dp)
                    .height(35.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Calculated Price: ", fontSize = 20.sp
                )



                Text(
                    text = String.format("%.2f", totalPrice.value),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        selectedCurrency = if (selectedCurrency == "EUR") "USD" else "EUR"
                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8F2E0A),
                        contentColor = Color.White
                    )

                ) {
                    Text(
                        text = selectedCurrency,
                        fontSize = 18.sp, fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(5.dp))


                /*
                // Theme switch
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { isDarkTheme = it },
                    modifier = Modifier.padding(start = 10.dp)
                )

                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(8.dp))

                } */


            }

            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight()
                    .weight(1f),

                horizontalAlignment = Alignment.CenterHorizontally


            ) {
                items(items = steaks) { steak ->
                    SteakSelectionCard(steak, selectedSteaks, totalPrice, selectedCurrency)
                }
            }
        } //column

    }

}

@Composable
fun SteakSelectionCard(
    steak: Steak,
    selectedSteaks: MutableList<SelectedSteak>,
    totalPrice: MutableState<Double>,
    selectedCurrency: String
) {
    var quantity by remember { mutableStateOf(0) }

    val selectedSteak = selectedSteaks.find { it.steak.id == steak.id }

    fun updateSelectedSteak(newQuantity: Int) {
        if (newQuantity == 0) {
            selectedSteaks.remove(selectedSteak)
        } else {
            val index = selectedSteaks.indexOf(selectedSteak)
            if (index != -1) {
                selectedSteaks[index] = SelectedSteak(steak, newQuantity)
            } else {
                selectedSteaks.add(SelectedSteak(steak, newQuantity))
            }
        }
        val conversionRate = if (selectedCurrency == "USD") 1.2 else 1.0
        totalPrice.value =
            selectedSteaks.sumByDouble { it.steak.price * it.quantity * conversionRate }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(
                color = Color(0xFF590315),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))


    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)

        ) {


            GlideImage(imageModel = { steak.imageUrl },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                loading = {
                    Box(modifier = Modifier.matchParentSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                },
                failure = {
                    Text("Image request failed.")
                })

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = steak.name,
                fontSize = 30.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Price: ${
                    String.format(
                        "%.2f", steak.price * if (selectedCurrency == "USD") 1.2 else 1.0
                    )
                } $selectedCurrency",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally),
                color = Color.White
            )


            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (quantity > 0) {
                            quantity--
                            updateSelectedSteak(quantity)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Quantity: $quantity",
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )

                IconButton(
                    onClick = {
                        quantity++
                        updateSelectedSteak(quantity)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = Color.White
                    )
                }
            }
        }


    }


}
//}

fun updateSelectedSteak(
    steak: Steak,
    newQuantity: Int,
    selectedSteaks: MutableList<SelectedSteak>,
    totalPrice: MutableState<Double>,
    selectedCurrency: String
) {
    val selectedSteak = selectedSteaks.find { it.steak.id == steak.id }

    if (newQuantity == 0) {
        selectedSteaks.remove(selectedSteak)
    } else {
        val index = selectedSteaks.indexOf(selectedSteak)
        if (index != -1) {
            selectedSteaks[index] = SelectedSteak(steak, newQuantity)
        } else {
            selectedSteaks.add(SelectedSteak(steak, newQuantity))
        }
    }
    val conversionRate = if (selectedCurrency == "USD") 1.2 else 1.0
    totalPrice.value = selectedSteaks.sumByDouble { it.steak.price * it.quantity * conversionRate }
}

package edu.rit.nn7716.steakapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import edu.rit.nn7716.steakapp.data.api.model.Steak


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SteakDetailView(steakId: Int, steakList: List<Steak>, onBackClick: () -> Unit) {
    val steak = steakList.find { it.id == steakId }
    var showMoreInfo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (steak != null) {
                        Text(steak.name)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 82.dp)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(295.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .background(Color(0xFFF3EFEE))
                    ) {
                        if (steak != null) {
                            Image(
                                painter = rememberAsyncImagePainter(model = steak.imageUrl),
                                contentDescription = "Steak Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (steak != null) {
                    Text(text = steak.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (steak != null) {
                    Text(
                        text = steak.origin,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Description:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                if (steak != null) {
                    Text(text = "${steak.description}", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Flavor Profiles:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                if (steak != null) {
                    Text(text = "${steak.flavorProfiles}", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Recommended Cooking Methods:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                if (steak != null) {
                    Text(steak.recommendedCookingMethods, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(20.dp))



                // If time allows part: "More" button if additional content is hidden
                if (!showMoreInfo) {
                    Button(
                        onClick = { showMoreInfo = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF590315),
                            contentColor = Color.White
                        )
                    ) {
                        Text("More")
                    }
                }
            }

            if (showMoreInfo) {
                item {
                    // Additional information
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Complementary Side Dishes:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (steak != null) {
                        Text(steak.complementarySideDishes, fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Complementary Sauce:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (steak != null) {
                        Text(steak.sauces, fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))


                    Text(
                        text = "Wine Pairings:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (steak != null) {
                        Text(steak.winePairings, fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // "Less" button at the bottom
                    Button(
                        onClick = { showMoreInfo = false }, // Hide additional info
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF590315),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Less")
                    }
                }
            }
        }
    }
}
package edu.rit.nn7716.steakapp.screens

import Method
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.rit.nn7716.steakapp.data.api.model.MethodsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MethodsScreen(navController: NavController) {
    val vm: MethodsViewModel = viewModel()

    LaunchedEffect(key1 = Unit, block = {
        vm.getMethods()
    })

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        Text("Steak Cooking Methods")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (vm.errorMessage.isEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(bottom = 100.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (vm.methodsList.isEmpty()) {
                    item {
                        Text(
                            text = "No Methods",
                            fontSize = 30.sp,
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
                items(items = vm.methodsList) { method ->
                    val context = LocalContext.current
                    MethodCard(method, context)
                }
            } // LazyColumn
        } // if
        else {
            Text(text = vm.errorMessage)
        }
    } // Scaffold
} // MethodsScreen

@Composable
fun MethodCard(method: Method, context: Context) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(16.dp)

    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3EFEE))
                .padding(5.dp)
        )

        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)

            ) {
                Text(
                    text = method.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color(0xFF590315)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Step 1", fontWeight = FontWeight.Bold, color = Color(0xFF590315))
                Text(text = "${method.step1}")

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Step 2", fontWeight = FontWeight.Bold, color = Color(0xFF590315))
                Text(text = "${method.step2}")

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Step 3", fontWeight = FontWeight.Bold, color = Color(0xFF590315))
                Text(text = "${method.step3}")

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Step 4", fontWeight = FontWeight.Bold, color = Color(0xFF590315))
                Text(text = "${method.step4}")

                Spacer(modifier = Modifier.height(8.dp))
                // Button to watch video
                Button(
                    onClick = {
                        val url = method.video
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8F2E0A),
                        contentColor = Color.White
                    )

                ) {
                    Text(text = "Watch Video")
                }

            }

        } //box

    } // Card
} // end









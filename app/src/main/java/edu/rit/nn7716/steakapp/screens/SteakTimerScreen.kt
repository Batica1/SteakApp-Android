import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val NOTIFICATION_CHANNEL_ID = "steak_timer_channel"
private const val NOTIFICATION_ID = 123

@Composable
fun CircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    thickness: Dp = 32.dp
) {
    val startAngle = -90f
    val sweepAngle = 360f * progress

    Canvas(
        modifier = modifier
            .size(300.dp)
    ) {  // Set size to fit above the timer
        val radius = (size.minDimension - thickness.toPx()) / 2
        drawArc(
            color = Color(0xFF8F2E0A),
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(thickness.toPx())
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SteakTimerScreen(onTimeChanged: (String) -> Unit) {
    val initialTime = 10 * 60 * 1000L // 10 minutes in milliseconds
    val elapsedTime = remember { mutableStateOf(initialTime) }
    val isRunning = remember { mutableStateOf(false) }
    val twoMinuteElapsedTime =
        remember { mutableStateOf(2 * 60 * 1000L) }
    val fourMinuteElapsedTime = remember { mutableStateOf(4 * 60 * 1000L) }
    val fiveMinuteElapsedTime = remember { mutableStateOf(5 * 60 * 1000L) }
    val sixMinuteElapsedTime = remember { mutableStateOf(6 * 60 * 1000L) }
    val sevenMinuteElapsedTime = remember { mutableStateOf(7 * 60 * 1000L) }

    val scope = rememberCoroutineScope()
    var selectedTimer by remember { mutableStateOf(elapsedTime) }

    val context = LocalContext.current

    RequestNotificationPermission(context)


    fun startCountdown() {
        isRunning.value = true
        scope.launch {
            while (isRunning.value && selectedTimer.value > 0) {
                delay(10L)
                selectedTimer.value -= 10L
                onTimeChanged(formatTime(selectedTimer.value))
            }
            if (selectedTimer.value <= 0) {
                isRunning.value = false
                onTimeChanged("00:00:00")
                // Notify on the app screen when the timer ends
                Toast.makeText(context, "Time is up. Your steak is ready!", Toast.LENGTH_SHORT)
                    .show()
                // Notify on the phone push notification when the timer ends
                showNotification(context)

            }
        }
    }

    fun stopCountdown() {
        isRunning.value = false
    }

    val initialTimers = mapOf(
        "WELL DONE" to 10 * 60 * 1000L,
        "MEDIUM WELL" to 7 * 60 * 1000L,
        "MEDIUM" to 6 * 60 * 1000L,
        "MEDIUM RARE" to 5 * 60 * 1000L,
        "RARE" to 4 * 60 * 1000L,
        "BLUE RARE" to 2 * 60 * 1000L
    )

    fun resetCountdown() {
        val selectedKey = when (selectedTimer) {
            elapsedTime -> "WELL DONE"
            sevenMinuteElapsedTime -> "MEDIUM WELL"
            sixMinuteElapsedTime -> "MEDIUM"
            fiveMinuteElapsedTime -> "MEDIUM RARE"
            fourMinuteElapsedTime -> "RARE"
            twoMinuteElapsedTime -> "BLUE RARE"
            else -> "WELL DONE"
        }
        selectedTimer.value = initialTimers[selectedKey] ?: (10 * 60 * 1000L)
        onTimeChanged(formatTime(selectedTimer.value))
    }


    TopAppBar(
        title = {
            Row {
                Text("Steak Timer")
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally


    ) {
        Row {
            LazyRow(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 80.dp)
            ) {
                items(
                    listOf(
                        "WELL DONE" to elapsedTime,
                        "MEDIUM WELL" to sevenMinuteElapsedTime,
                        "MEDIUM" to sixMinuteElapsedTime,
                        "MEDIUM RARE" to fiveMinuteElapsedTime,
                        "RARE" to fourMinuteElapsedTime,
                        "BLUE RARE" to twoMinuteElapsedTime
                    )
                ) { (text, timer) ->
                    Button(
                        onClick = { selectedTimer = timer },
                        modifier = Modifier.padding(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF590315),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = text)
                    }
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(320.dp)
        ) {
            CircularProgress(
                progress = selectedTimer.value.toFloat() / initialTime,

                modifier = Modifier
                    .fillMaxSize()
            )

            Text(
                text = formatTime(selectedTimer.value),
                fontSize = 60.sp,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp, bottom = 75.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (!isRunning.value) {
                        startCountdown()
                    } else {
                        stopCountdown()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8F2E0A),
                    contentColor = Color.White
                )
            ) {
                Text(text = if (isRunning.value) "STOP" else "START")
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                onClick = { resetCountdown() }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF590315),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Reset")

            }
        }
    }
}

fun formatTime(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    val hours = (millis / (1000 * 60 * 60)) % 24
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}


fun createNotificationChannel(context: android.content.Context) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val channelId = "steak_timer_channel"
        val channelName = "Steak Timer Notifications"
        val description = "Notifications for Steak Timer app"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(channelId, channelName, importance).apply {
            setDescription(description)
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}


@Composable
fun RequestNotificationPermission(context: android.content.Context) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                NotificationManagerCompat.from(context)
            }
        }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

fun showNotification(context: android.content.Context) {
    val hasPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

    if (hasPermission) {
        createNotificationChannel(context)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Steak Timer")
            .setContentText("Your steak is ready!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    } else {
         if (context is androidx.activity.ComponentActivity) {
             ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_ID
            )
        } else {
            // show Toast if permission is denied
            android.widget.Toast.makeText(
                context,
                "Cannot send notifications without permission.",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }
}





package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.RunningViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RunningWorkoutScreen(
    navController: NavController,
    runningViewModel: RunningViewModel
) {

    /*
    * temp variables for distance, time and pace
    * */
    val distance = "5.2"
    val time = "30:00"
    val pace = "5:45"

    val pedometerPermission =
        rememberPermissionState(permission = android.Manifest.permission.ACTIVITY_RECOGNITION)


    // Only request permission if it's not already granted
    LaunchedEffect(pedometerPermission.status) {
        if (!pedometerPermission.status.isGranted) {
            pedometerPermission.launchPermissionRequest()
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        MainContent(
            runningViewModel = runningViewModel,
            pedometerPermission = pedometerPermission,
            distance = distance,
            time = time,
            pace = pace
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainContent(
    runningViewModel: RunningViewModel,
    pedometerPermission: PermissionState,
    distance: String,
    time: String,
    pace: String
) {
    val stepCount = runningViewModel.stepCounter

    // Map Section Placeholder
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Map View (Your Route)",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    when {
        pedometerPermission.status.isGranted -> {
            // Run Details Section
            RunDetailsSection(distance, time, pace, stepCount.value)
        }

        pedometerPermission.status.shouldShowRationale -> {
            pedometerPermission.launchPermissionRequest()
        }

        else -> {
            Text("Permission denied. Cannot track steps.", color = MaterialTheme.colorScheme.error)
        }
    }


    Spacer(modifier = Modifier.height(32.dp))

    // Controls: Start, Pause, Stop
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ControlButton(
            text = if (runningViewModel.isRunning) "Pause" else "Start",
            color = if (runningViewModel.isRunning) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
            onClick = {
                runningViewModel.isRunning = !runningViewModel.isRunning
            }
        )
        ControlButton(
            text = "Stop",
            color = MaterialTheme.colorScheme.error,
            onClick = {
                runningViewModel.isRunning = false
                runningViewModel.stepCounter.value = 0
            }
        )
    }
}

@Composable
private fun RunDetailsSection(distance: String, time: String, pace: String, steps: Int) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            RunDetailItem(label = "Distance", value = "$distance km")

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            RunDetailItem(label = "Time", value = time)

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            RunDetailItem(label = "Pace", value = "$pace min/km")

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            RunDetailItem(label = "Steps", value = steps.toString())
        }
    }
}

@Composable
private fun RunDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ControlButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .width(120.dp)
            .padding(8.dp)
    ) {
        Text(text = text, color = Color.White)
    }
}
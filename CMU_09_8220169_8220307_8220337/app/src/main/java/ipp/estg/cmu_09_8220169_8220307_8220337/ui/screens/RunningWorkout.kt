package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens


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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.RunningViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RunningWorkoutScreen(
    navController: NavController,
    runningViewModel: RunningViewModel
) {
    val distance = "5.2"
    val time = "30:00"
    val pace = "5:45"
    val stepCount = runningViewModel.stepCounter.value

    val pedometerPermission =
        rememberPermissionState(android.Manifest.permission.ACTIVITY_RECOGNITION)

    LaunchedEffect(pedometerPermission.status) {
        if (!pedometerPermission.status.isGranted) {
            pedometerPermission.launchPermissionRequest()
        }
    }

    Scaffold { innerPading ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFD0F0FF))
                .padding(innerPading)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                MapSection()

                RunDetailsSection(distance, time, pace, stepCount)

                ControlsSection(
                    runningViewModel = runningViewModel,
                    pedometerPermission = pedometerPermission
                )
            }
        }

    }
}

@Composable
private fun MapSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        Color(0xFF00BFFF)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Map View (Your Route)",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RunDetailsSection(distance: String, time: String, pace: String, steps: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(12.dp)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            RunDetailItem(
                label = "Distance",
                value = "$distance km",
                icon = Icons.Filled.DirectionsRun
            )
            RunDetailItem(
                label = "Time",
                value = time,
                icon = Icons.Filled.Timer
            )
            RunDetailItem(
                label = "Pace",
                value = "$pace min/km",
                icon = Icons.Filled.Speed
            )
            RunDetailItem(
                label = "Steps",
                value = steps.toString(),
                icon = Icons.Filled.Directions
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ControlsSection(
    runningViewModel: RunningViewModel,
    pedometerPermission: PermissionState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ControlButton(
            text = if (runningViewModel.isRunning) "Pause" else "Start",
            color = if (runningViewModel.isRunning) Color(0xFFFF9800) else Color(0xFF4CAF50),
            onClick = {
                runningViewModel.isRunning = !runningViewModel.isRunning
            }
        )
        ControlButton(
            text = "Stop",
            color = Color(0xFFE53935),
            onClick = {
                runningViewModel.isRunning = false
                runningViewModel.stepCounter.value = 0
            }
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
        Text(text = text, color = Color.White, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun RunDetailItem(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
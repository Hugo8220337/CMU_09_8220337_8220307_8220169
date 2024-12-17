package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.cards.LowBatteryCard
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.cards.StatPreviewCard
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation.Screen
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.BatteryManager
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.formatDuration
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.RunningViewModel
import ipp.estg.mobile.ui.components.utils.Loading

@Composable
fun RunningWorkoutStartScreen(
    navController: NavController,
    runningViewModel: RunningViewModel = viewModel()
) {
    val lastRun by runningViewModel.lastRun.collectAsState()
    val isLoading by runningViewModel.isLoading.collectAsState()

    val batteryManager = BatteryManager(LocalContext.current)
    val batteryLevel = batteryManager.getBatteryLevel()


    LaunchedEffect(Unit) {
        runningViewModel.getLastRun()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section with Welcome Text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.ready_for_your_run),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))


                Text(
                    text = stringResource(id = R.string.track_your_progress_and_achieve_your_goals),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            if (isLoading) {
                Loading()
            } else {
                val distance: String = String.format("%.2f", lastRun?.distance ?: 0.0)
                val duration: String = lastRun?.duration ?: "00:00"
                StatsSection(
                    distance =  distance,
                    duration = formatDuration(duration)
                )
            }

            // Bottom Section with Start Button or Battery Warning
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                if (batteryLevel > 40) {
                    Button(
                        onClick = { navController.navigate(Screen.RunningWorkout.route) },
                        modifier = Modifier
                            .height(56.dp)
                            .fillMaxWidth(0.8f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.DirectionsRun,
                            contentDescription = stringResource(id = R.string.start_running),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.start_running),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    LowBatteryCard(batteryLevel = batteryLevel)
                }
            }
        }
    }
}

@Composable
private fun StatsSection(
    distance: String,
    duration: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatPreviewCard(
                icon = Icons.AutoMirrored.Filled.DirectionsRun,
                title = stringResource(id = R.string.distance),
                value = "$distance km"
            )
            StatPreviewCard(
                icon = Icons.Default.Timer,
                title = stringResource(id = R.string.duration),
                value = duration
            )
        }
    }
}
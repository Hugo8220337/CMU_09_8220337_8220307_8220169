package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.RunDetails
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.buttons.ControlButton
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation.Screen
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.formatTime
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.RunningViewModel


@SuppressLint("InlinedApi")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RunningWorkoutScreen(
    navController: NavController,
    runningViewModel: RunningViewModel = viewModel()
) {
    val distance by runningViewModel.distance.collectAsState()
    val time by runningViewModel.time.collectAsState()
    val stepCount by runningViewModel.stepCounter.collectAsState()


    val pedometerPermission =
        rememberPermissionState(android.Manifest.permission.ACTIVITY_RECOGNITION)
    val fineLocationPermission =
        rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    val coarseLocationPermission =
        rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    val foregroundLocationPermission =
        rememberPermissionState(android.Manifest.permission.FOREGROUND_SERVICE_LOCATION)
    val foregroundServicePermission =
        rememberPermissionState(android.Manifest.permission.FOREGROUND_SERVICE)


    // Reuqest permissions on first launch
    LaunchedEffect(
        pedometerPermission.status,
        fineLocationPermission.status,
        coarseLocationPermission.status,
        foregroundLocationPermission.status,
        foregroundServicePermission.status
    ) {
        if (!pedometerPermission.status.isGranted) {
            pedometerPermission.launchPermissionRequest()
        }
        if (!fineLocationPermission.status.isGranted) {
            fineLocationPermission.launchPermissionRequest()
        }
        if (!coarseLocationPermission.status.isGranted) {
            coarseLocationPermission.launchPermissionRequest()
        }
        if (!foregroundLocationPermission.status.isGranted) {
            foregroundLocationPermission.launchPermissionRequest()
        }
        if (!foregroundServicePermission.status.isGranted) {
            foregroundServicePermission.launchPermissionRequest()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            runningViewModel.stopRun() // Stop updates when the Composable is removed
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
                MapSection(runningViewModel, fineLocationPermission)

                RunDetails(
                    distance = String.format("%.2f", distance), // Format to 2 decimal places
                    time = formatTime(time), // Format seconds to "mm:ss"
                    steps = stepCount
                )

                if (!fineLocationPermission.status.isGranted || !coarseLocationPermission.status.isGranted) {
                    Text(
                        text = stringResource(id = R.string.location_permission_required),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                } else if (!pedometerPermission.status.isGranted) {
                    Text(
                        text = stringResource(id = R.string.pedometer_permission_required),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else if(!foregroundServicePermission.status.isGranted) {
                    Text(
                        text = stringResource(id = R.string.foreground_service_permission_required),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    ControlsSection(
                        runningViewModel = runningViewModel,
                        goBack = {
                            navController.popBackStack()
                            navController.navigate(Screen.Home.route)
                        }
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MapSection(
    runningViewModel: RunningViewModel,
    fineLocationPermission: PermissionState
) {

    val currentLocation by runningViewModel.currentLocation.collectAsState()
    val path by runningViewModel.path.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position =
            CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 14f) // Initial zoom level (default)
    }

    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            // Keep the zoom level as is, but update the position to the new location
            cameraPositionState.position = CameraPosition(
                LatLng(it.latitude, it.longitude), // target
                cameraPositionState.position.zoom, // keep zoom
                cameraPositionState.position.tilt, // keep tilt
                cameraPositionState.position.bearing // keep bearing
            )
        }
    }

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

        if (fineLocationPermission.status.isGranted) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                // Draw the user's path as a polyline
                Polyline(
                    points = path,
                    color = Color.Blue,
                    width = 5f
                )

                // Update Marker position dynamically
                currentLocation?.let {
                    Marker(
                        state = MarkerState(
                            position = LatLng(it.latitude, it.longitude)
                        ),
                        title = stringResource(id = R.string.you_are_here)
                    )
                }
            }
        } else {
            Text(
                text = stringResource(id = R.string.location_permission_required),
            )
        }

    }
}


@Composable
private fun ControlsSection(
    runningViewModel: RunningViewModel,
    goBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ControlButton(
            text = if (runningViewModel.isRunning) stringResource(id = R.string.pause) else stringResource(
                id = R.string.start
            ),
            color = if (runningViewModel.isRunning) Color(0xFFFF9800) else Color(0xFF4CAF50),
            onClick = {
                runningViewModel.isRunning = !runningViewModel.isRunning
                if (runningViewModel.isRunning) {
                    runningViewModel.startRun() // Start the run
                } else {
                    runningViewModel.pauseRun() // Pause the run
                }
            }
        )
        ControlButton(
            text = stringResource(id = R.string.stop),
            color = Color(0xFFE53935),
            onClick = {
                runningViewModel.stopRun()
                goBack()
            }
        )
    }
}




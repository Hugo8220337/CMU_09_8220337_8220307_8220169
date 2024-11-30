package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens


import android.annotation.SuppressLint
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
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.plugin.Plugin
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.RunningViewModel
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.sources.generated.rememberGeoJsonSourceState
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.ControlButton
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.RunDetailItem
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.RunDetails
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation.Screen
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.formatTime


@SuppressLint("InlinedApi")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RunningWorkoutScreen(
    navController: NavController,
    runningViewModel: RunningViewModel = viewModel()
) {
    val distance by runningViewModel.distance.collectAsState()
    val time by runningViewModel.time.collectAsState()
    val pace by runningViewModel.pace.collectAsState()
    val stepCount by runningViewModel.stepCounter.collectAsState()


    val pedometerPermission =
        rememberPermissionState(android.Manifest.permission.ACTIVITY_RECOGNITION)
    val locationPermission =
        rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(pedometerPermission.status) {
        if (!pedometerPermission.status.isGranted) {
            pedometerPermission.launchPermissionRequest()
        }
    }

    LaunchedEffect(locationPermission.status.isGranted) {
        if (!locationPermission.status.isGranted) {
            locationPermission.launchPermissionRequest()
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
                MapSection(runningViewModel, locationPermission)

                RunDetails(
                    distance = String.format("%.2f", distance), // Format to 2 decimal places
                    time = formatTime(time), // Format seconds to "mm:ss"
                    pace = String.format("%.2f", pace), // Format to 2 decimal places
                    steps = stepCount
                )

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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MapSection(runningViewModel: RunningViewModel, locationPermission: PermissionState) {

    val currentLocation by runningViewModel.currentLocation.collectAsState()
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(currentLocation) {
        // Move the camera to the new location when it changes
        currentLocation?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(it.latitude, it.longitude),
                19f
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

        if(locationPermission.status.isGranted) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                // Update Marker position dynamically
                currentLocation?.let {
                    Marker(
                        state = MarkerState(
                            position = LatLng(it.latitude, it.longitude)
                        ),
                        title = "You are here"
                    )
                }
            }
        } else {
            Text("Location permission not granted")
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
                    runningViewModel.stopRun() // Pause the run
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




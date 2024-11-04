package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs

import android.Manifest
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.checkCameraPermission
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.launchCamera
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.requestCameraPermission
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.HomeViewModel

@Composable
fun MainContent(homeViewModel: HomeViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Progress Overview Section
        ProgressSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Daily Task Checklist
        TaskChecklist(homeViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Daily Photo Section
        DailyPhotoSection(homeViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Motivational Quote
        MotivationalQuote()

        homeViewModel.getError()?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

    }
}

@Composable
private fun ProgressSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Day 20 of 75", style = MaterialTheme.typography.bodyMedium)

        LinearProgressIndicator(
            progress = {
                20f / 75f // Dynamic based on progress
            },
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )
    }
}

@Composable
private fun TaskChecklist(homeViewModel: HomeViewModel) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Today's Tasks",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )


            // Lista de tarefas com acesso ao estado persistente
            TaskItemCard(
                "Drink 4L of Water",
                homeViewModel::getTaskGallonOfWater,
                homeViewModel::setTaskGallonOfWater
            )
            TaskItemCard(
                "Complete 2 Workouts",
                homeViewModel::getTaskTwoWorkouts,
                homeViewModel::setTaskTwoWorkouts
            )
            TaskItemCard(
                "Follow Diet",
                homeViewModel::getTaskFollowDiet,
                homeViewModel::setTaskFollowDiet
            )
            TaskItemCard(
                "Read 10 Pages",
                homeViewModel::getTaskReadTenPages,
                homeViewModel::setTaskReadTenPages
            )
        }
    }
}


@Composable
private fun TaskItemCard(
    task: String,
    isTaskCompleted: () -> Boolean,
    setTaskCompleted: (Boolean) -> Unit
) {
    var isChecked by remember { mutableStateOf(isTaskCompleted()) }

    // Atualizar as preferências ao clicar
    fun toggleChecked() {
        isChecked = !isChecked
        setTaskCompleted(isChecked)
    }

    val backgroundColor =
        if (isChecked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    val textColor =
        if (isChecked) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { toggleChecked() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    toggleChecked()
                },
                colors = CheckboxDefaults.colors(MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = when (task) {
                    "Drink 4L of Water" -> Icons.Default.LocalDrink
                    "Complete 2 Workouts" -> Icons.Default.FitnessCenter
                    "Follow Diet" -> Icons.Default.Restaurant
                    "Read 10 Pages" -> Icons.Default.Book
                    "Take Progress Photo" -> Icons.Default.CameraAlt
                    else -> Icons.Default.Check
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = task,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
        }
    }
}

@Composable
fun DailyPhotoSection(homeViewModel: HomeViewModel) {
    val context = LocalContext.current
    var hasCameraPermission by remember { mutableStateOf(false) }

    // Reusing permission and camera launch functions
    val cameraLauncher = launchCamera { bitmap ->
        if (bitmap != null) {
            homeViewModel.updateProgressPicture(bitmap)
        }
    }

    val requestPermissionLauncher = requestCameraPermission { granted ->
        hasCameraPermission = granted
        if (granted) {
            cameraLauncher.launch()
        } else {
            // TODO Handle permission denied, show message if needed
        }
    }

    // Check if camera permission is granted on load
    hasCameraPermission = checkCameraPermission(context)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Daily Progress Photo", style = MaterialTheme.typography.bodyMedium)

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray)
                .clickable {
                    if (hasCameraPermission) {
                        cameraLauncher.launch()
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
        ) {
            if (homeViewModel.getProgressPicture() != null) {
                Image(bitmap = homeViewModel.getProgressPicture()!!.asImageBitmap(), contentDescription = "Captured Photo")
            } else {
                Text(text = "Upload Photo", color = Color.White)
            }
        }
    }
}


@Composable
private fun MotivationalQuote() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Text(
            text = "\"The only bad workout is the one you didn't do.\"",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}
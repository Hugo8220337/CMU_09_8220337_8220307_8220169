package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs

import android.Manifest
import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.DailyTasks
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.StreakLinearProgressIndicator
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.cards.MotivationalQuoteCard
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.cards.TaskItemCard
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.theme.GoldColor
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.checkCameraPermission
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.launchCamera
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.requestCameraPermission
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.HomeViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainContent(homeViewModel: HomeViewModel) {
    val notificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    
    val streak = homeViewModel.state.streak
    val tasks by homeViewModel.dailyTasks.observeAsState()
    val dailyQuote by homeViewModel.dailyQuote.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.loadTodayTasks()
        homeViewModel.updateDailyStreak()
        homeViewModel.loadTodaysProgressPicture()
        homeViewModel.loadDailyQuote()
    }

    LaunchedEffect(Unit, notificationPermission.status) {
        if (!notificationPermission.status.isGranted) {
            notificationPermission.launchPermissionRequest()
        } else {
            homeViewModel.buildNotificationChannel()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Progress Overview Section
        StreakLinearProgressIndicator(streak)

        Spacer(modifier = Modifier.height(16.dp))

        // Daily Task Checklist
        TaskChecklist(
            tasks = tasks ?: DailyTasks(),
            homeViewModel = homeViewModel
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Daily Photo Section
        DailyPhotoSection(homeViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Motivational Quote
        MotivationalQuoteCard(dailyQuote)

        homeViewModel.state.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

    }
}

@Composable
private fun TaskChecklist(tasks: DailyTasks, homeViewModel: HomeViewModel) {
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
                stringResource(id = R.string.daily_tasks),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )


            // Lista de tarefas com acesso ao estado persistente
            TaskItemCard(
                task = stringResource(id = R.string.drink_4l_water),
                isTaskCompleted = tasks.gallonOfWater,
                onTaskToggle = { isCompleted ->
                    homeViewModel.setTasksValue(
                        tasks.copy(gallonOfWater = isCompleted)
                    )
                }
            )
            TaskItemCard(
                task = stringResource(id = R.string.complete_2_workouts),
                isTaskCompleted = tasks.twoWorkouts,
                onTaskToggle = { isCompleted ->
                    homeViewModel.setTasksValue(
                        tasks.copy(twoWorkouts = isCompleted)
                    )
                }
            )
            TaskItemCard(
                task = stringResource(id = R.string.follow_diet),
                isTaskCompleted = tasks.followDiet,
                onTaskToggle = { isCompleted ->
                    homeViewModel.setTasksValue(
                        tasks.copy(followDiet = isCompleted)
                    )
                }
            )
            TaskItemCard(
                task = stringResource(id = R.string.read_10_pages),
                isTaskCompleted = tasks.readTenPages,
                onTaskToggle = { isCompleted ->
                    homeViewModel.setTasksValue(
                        tasks.copy(readTenPages = isCompleted)
                    )
                }
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

    val lackOfPermissionsText = stringResource(id = R.string.lack_of_permissions)
    val requestPermissionLauncher = requestCameraPermission { granted ->
        hasCameraPermission = granted
        if (granted) {
            cameraLauncher.launch()
        } else {
            // Mostra mensagem no toast caso a permissão não seja concedida
            Toast.makeText(context, lackOfPermissionsText, Toast.LENGTH_SHORT).show()
        }
    }

    // Checa se a permissão para a camara foi concedida
    hasCameraPermission = checkCameraPermission(context)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(id = R.string.daily_progress_picture),
            style = MaterialTheme.typography.bodyMedium
        )

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
            if (homeViewModel.state.imageBitmap != null) {
                Image(
                    bitmap = homeViewModel.state.imageBitmap!!.asImageBitmap(),
                    contentDescription = stringResource(id = R.string.captured_photo)
                )
            } else {
                Text(text = stringResource(id = R.string.upload_photo), color = Color.White)
            }
        }

        SaveButton(homeViewModel = homeViewModel, context = context)
    }
}

@Composable
private fun SaveButton(homeViewModel: HomeViewModel, context: Context) {
    val toastText = stringResource(id = R.string.photo_saved_in_the_gallery)
    IconButton(
        onClick = {
            homeViewModel.saveProgressPitureToGallery()?.let {
                Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            }
        },
        modifier = Modifier
            .padding(8.dp)
            .size(36.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.Save,
            contentDescription = stringResource(id = R.string.save),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}
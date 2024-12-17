package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs

import android.Manifest
import android.widget.Toast
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.DailyTasks
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.StreakLinearProgressIndicator
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.buttons.SaveButton
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.cards.MotivationalQuoteCard
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.cards.TaskItemCard
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.checkCameraPermission
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.launchCamera
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.requestCameraPermission
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.DailyTasksViewModel
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.HomeViewModel
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.QuoteViewModel
import ipp.estg.mobile.ui.components.utils.Loading

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainContent(
    homeViewModel: HomeViewModel,
    dailyTasksViewModel: DailyTasksViewModel
) {
    val quoteViewModel: QuoteViewModel = viewModel()

    val notificationPermission =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    val isLoaddingTasks by dailyTasksViewModel.isLoading.collectAsState()
    val streak by dailyTasksViewModel.streak.collectAsState()
    val tasks by dailyTasksViewModel.todaysTasks.observeAsState()
    val dailyQuote by quoteViewModel.dailyQuote.collectAsState()
    val isLoadingQuote by quoteViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        dailyTasksViewModel.loadTodayTasks()
        dailyTasksViewModel.updateDailyStreak()
        quoteViewModel.loadDailyQuote()
    }

    LaunchedEffect(Unit) {
        if (!notificationPermission.status.isGranted) {
            notificationPermission.launchPermissionRequest()
        } else {
            homeViewModel.startNotificationService()
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

        // Loding for daily tasks and daily progress picture
        if (isLoaddingTasks) {
            Loading()
        } else {
            TaskChecklist(
                tasks = tasks ?: DailyTasks(),
                dailyTasksViewModel = dailyTasksViewModel
            )

            Spacer(modifier = Modifier.height(16.dp))

            DailyPhotoSection(dailyTasksViewModel)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Motivational Quote
        if (isLoadingQuote) {
            Loading()
        } else {
            MotivationalQuoteCard(dailyQuote)
        }

    }
}

@Composable
private fun TaskChecklist(tasks: DailyTasks, dailyTasksViewModel: DailyTasksViewModel) {
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
                    dailyTasksViewModel.setTasksValue(
                        tasks.copy(gallonOfWater = isCompleted)
                    )
                }
            )
            TaskItemCard(
                task = stringResource(id = R.string.complete_2_workouts),
                isTaskCompleted = tasks.twoWorkouts,
                onTaskToggle = { isCompleted ->
                    dailyTasksViewModel.setTasksValue(
                        tasks.copy(twoWorkouts = isCompleted)
                    )
                }
            )
            TaskItemCard(
                task = stringResource(id = R.string.follow_diet),
                isTaskCompleted = tasks.followDiet,
                onTaskToggle = { isCompleted ->
                    dailyTasksViewModel.setTasksValue(
                        tasks.copy(followDiet = isCompleted)
                    )
                }
            )
            TaskItemCard(
                task = stringResource(id = R.string.read_10_pages),
                isTaskCompleted = tasks.readTenPages,
                onTaskToggle = { isCompleted ->
                    dailyTasksViewModel.setTasksValue(
                        tasks.copy(readTenPages = isCompleted)
                    )
                }
            )
        }
    }
}

@Composable
fun DailyPhotoSection(dailyTasksViewModel: DailyTasksViewModel) {
    val context = LocalContext.current
    var hasCameraPermission by remember { mutableStateOf(false) }
    val imageBitmap by dailyTasksViewModel.imageBitmap.collectAsState()

    // Reusing permission and camera launch functions
    val cameraLauncher = launchCamera { bitmap ->
        if (bitmap != null) {
            dailyTasksViewModel.updateProgressPicture(bitmap)
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
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap!!.asImageBitmap(),
                    contentDescription = stringResource(id = R.string.captured_photo),
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(text = stringResource(id = R.string.upload_photo), color = Color.White)
            }
        }

        val toastText = stringResource(id = R.string.photo_saved_in_the_gallery)
        SaveButton {
            dailyTasksViewModel.saveProgressPitureToGallery()?.let {
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        }}

    }
}
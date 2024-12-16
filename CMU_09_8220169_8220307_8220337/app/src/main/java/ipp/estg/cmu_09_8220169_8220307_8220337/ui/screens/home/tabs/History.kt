package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.Bitmap
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.DailyTasks
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Running
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Workout
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.EmptyPicturesState
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.buttons.DropDownButton
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.cards.DailyPictureCard
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.cards.RunCardItem
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.cards.WorkoutCardItem
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.getImageFromFile
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.HomeViewModel
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.RunningViewModel
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.WorkoutViewModel
import java.time.LocalDate

// Enum RunSortOrder separado
enum class EnumEntries(val value: Int) {
    RUUNING(R.string.running),
    WORKOUTS(R.string.workouts),
    PHOTOS(R.string.photos);
}

data class DailyPicture(
    val date: LocalDate,
    val imageBitMap: Bitmap? = null
)

@Composable
fun WorkoutHistoryPage(
    homeViewModel: HomeViewModel = viewModel(),
    workoutViewModel: WorkoutViewModel = viewModel(),
    runningViewModel: RunningViewModel = viewModel()
) {
    var selectedTab by rememberSaveable { mutableStateOf(EnumEntries.RUUNING) }

    val workouts = workoutViewModel.state.storedWorkouts
    val allTasks by homeViewModel.allDailyTasks.collectAsState(emptyList())
    val runnings by runningViewModel.runnings.collectAsState(emptyList())


    LaunchedEffect(Unit) {
        workoutViewModel.getWorkoutsByUserID()
        runningViewModel.getRunningWorkoutsByUserID()
        homeViewModel.loadAllUserTasks()
    }


    Column {
        // Título da página e botão para ordenação
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = selectedTab.value),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
            DropDownButton(onSortOrderSelected = { selectedTab = it })
        }

        // Conteúdo da página
        when (selectedTab) {
            EnumEntries.RUUNING -> RunningHistoryScreenContentStatic(runnings = runnings)
            EnumEntries.WORKOUTS -> WorkoutHistory(workouts = workouts)
            EnumEntries.PHOTOS -> DailyPicturesHistory(tasks = allTasks)
        }
    }
}


@Composable
private fun RunningHistoryScreenContentStatic(
    runnings: List<Running?>,
    onItemClick: (Running) -> Unit = {}
) {
    if (runnings.isEmpty()) {
        Text(
            text = stringResource(id = R.string.no_runnings_yet),
            modifier = Modifier.fillMaxSize(),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(runnings) { running ->
                if (running != null) {
                    RunCardItem(running = running, onItemClick = onItemClick)
                }
            }
        }
    }
}

@Composable
private fun WorkoutHistory(
    workouts: List<Workout>,
    onItemClick: (Workout) -> Unit = {}
) {
    if (workouts.isEmpty()) {
        Text(
            text = stringResource(id =  R.string.no_workouts_yet),
            modifier = Modifier.fillMaxSize(),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(workouts) { workout ->
                WorkoutCardItem(workout = workout, onItemClick = onItemClick)
            }
        }
    }
}

@Composable
fun DailyPicturesHistory(
    tasks: List<DailyTasks>
) {
    val pictures = mutableListOf<DailyPicture>()
    tasks.forEach { task ->
        pictures.add(
            DailyPicture(
                date = LocalDate.parse(task.date),
                imageBitMap = getImageFromFile(task.takeProgressPicture)
            )
        )

    }
    if (pictures.isEmpty()) {
        EmptyPicturesState()
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pictures) { picture ->
                if (picture.imageBitMap != null) {
                    DailyPictureCard(picture)
                }
            }
        }
    }
}





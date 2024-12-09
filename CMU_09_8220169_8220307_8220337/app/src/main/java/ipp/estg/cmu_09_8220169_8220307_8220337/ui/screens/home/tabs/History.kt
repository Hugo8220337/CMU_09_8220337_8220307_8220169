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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Running
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Workout
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.DropDownList
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.EmptyPicturesState
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.cards.DailyPictureCard
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.cards.RunCardItem
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.cards.WorkoutCardItem
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.utils.DropDownButton
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.RunningViewModel
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.WorkoutViewModel
import java.time.LocalDate

// Enum RunSortOrder separado
enum class EnumEntries(val value: String) {
    RUUNING("Running"),
    WORKOUTS("Workouts"),
    PHOTOS("Photos");
}

//// Dados de uma corrida
//data class Run(
//    val date: String,
//    val distance: String,
//    val calories: String,
//    val speed: String,
//    val img: Bitmap? = null
//)

data class DailyPicture(
    val date: LocalDate,
    val imageUri: String,
    val description: String? = null
)

@Composable
fun WorkoutHistoryPage(
    workoutViewModel: WorkoutViewModel = viewModel(),
    runningViewModel: RunningViewModel = viewModel()
) {
    // Dados estáticos para visualização de corridas
//    val staticRunList = listOf(
//        Run(
//            date = "August 04, 2024",
//            distance = "0.849 km",
//            calories = "35 kcal",
//            speed = "24.45 km/hr"
//        ),
//        Run(
//            date = "August 04, 2024",
//            distance = "1.185 km",
//            calories = "49 kcal",
//            speed = "15.13 km/hr"
//        ),
//        Run(
//            date = "August 04, 2024",
//            distance = "1.56 km",
//            calories = "64 kcal",
//            speed = "73.89 km/hr"
//        )
//    )

    val workouts = workoutViewModel.state.storedWorkouts
    val runnings by runningViewModel.running.collectAsState(emptyList())
    var selectedTab by rememberSaveable { mutableStateOf(EnumEntries.RUUNING) }

    // Obter os dados dos treinos
    // Carregando dados ao iniciar a página
    LaunchedEffect(Unit) {
        workoutViewModel.getWorkoutsByUserID()
        runningViewModel.getRunningWorkoutsByUserID()
    }


    Column {
        // Título da página e botão de ordenação
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedTab.value,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
            DropDownButton(onSortOrderSelected = { selectedTab = it })
        }

        // Conteúdo da página
        when (selectedTab) {
            EnumEntries.RUUNING -> RunningHistoryScreenContentStatic(runnings = runnings)
            EnumEntries.WORKOUTS -> WorkoutHistory(workouts = workouts)
            EnumEntries.PHOTOS -> DailyPicturesHistory()
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
            text = "No running workouts yet",
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
            text = "No workouts yet",
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
    pictures: List<DailyPicture> = listOf(
        DailyPicture(
            date = LocalDate.of(2024, 8, 4),
            imageUri = "https://img.freepik.com/fotos-gratis/um-leao-com-uma-juba-de-arco-iris-e-olhos-azuis_1340-39421.jpg",
            description = "Morning run"
        ),
        DailyPicture(
            date = LocalDate.of(2024, 8, 5),
            imageUri = "https://example.com/sample2.jpg",
            description = "Sunset view"
        )
    )
) {
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
                DailyPictureCard(picture)
            }
        }
    }
}





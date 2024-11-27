package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Workout
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.DropDownList
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.RunCardItem
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.WorkoutCardItem
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.WorkoutViewModel

// Enum RunSortOrder separado
enum class EnumEntries {
    RUUNING,
    WORKOUTS
}

// Dados de uma corrida
data class Run(
    val date: String,
    val distance: String,
    val calories: String,
    val speed: String,
    val img: Bitmap? = null
)

@Composable
fun WorkoutHistoryPage(workoutViewModel: WorkoutViewModel) {
    // Dados estáticos para visualização de corridas
    val staticRunList = listOf(
        Run(
            date = "August 04, 2024",
            distance = "0.849 km",
            calories = "35 kcal",
            speed = "24.45 km/hr"
        ),
        Run(
            date = "August 04, 2024",
            distance = "1.185 km",
            calories = "49 kcal",
            speed = "15.13 km/hr"
        ),
        Run(
            date = "August 04, 2024",
            distance = "1.56 km",
            calories = "64 kcal",
            speed = "73.89 km/hr"
        )
    )

    // Obter os dados dos treinos
    LaunchedEffect(Unit) {
        workoutViewModel.getWorkouts()
    }

    val workouts = workoutViewModel.state.storedWorkouts
    var selectedTab by rememberSaveable { mutableStateOf(EnumEntries.RUUNING) }

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
                text = if(selectedTab == EnumEntries.WORKOUTS) "Workouts" else "Running",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
            DropDownButton(onSortOrderSelected = { selectedTab = it })
        }

        // Conteúdo da página
        when (selectedTab) {
            EnumEntries.RUUNING -> RunningHistoryScreenContentStatic(runItems = staticRunList, onSortOrderSelected = {}, onItemClick = {})
            EnumEntries.WORKOUTS -> WorkoutHistory(workouts = workouts, onItemClick = {})
        }
    }
}


@Composable
private fun RunningHistoryScreenContentStatic(
    runItems: List<Run>,
    onSortOrderSelected: (EnumEntries) -> Unit,
    onItemClick: (Run) -> Unit,
) {

    Box {
        RunningListStatic(
            runItems = runItems,
            onItemClick = onItemClick
        )
    }
}

@Composable
private fun WorkoutHistory(
    workouts: List<Workout>,
    onItemClick: (Workout) -> Unit
) {
    Box {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(workouts) { workout ->
                WorkoutCardItem(workout = workout, onItemClick = onItemClick)
            }
        }
    }
}

@Composable
private fun RunningListStatic(
    runItems: List<Run>,
    onItemClick: (Run) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 8.dp)
    ) {
        items(runItems) { run ->
            RunCardItem(run = run, onItemClick = onItemClick)
        }
    }
}

@Composable
fun DropDownButton(onSortOrderSelected: (EnumEntries) -> Unit) {
    var isDropDownExpanded by rememberSaveable { mutableStateOf(false) }
    val sortOrderList = remember { EnumEntries.entries }

    Column {
        Button(
            onClick = { isDropDownExpanded = !isDropDownExpanded },
        ) {
            Text(text = stringResource(id = R.string.sort_by), style = MaterialTheme.typography.labelLarge)
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = stringResource(id = R.string.sort_by))
        }
        DropDownList(
            list = sortOrderList,
            onItemSelected = {
                onSortOrderSelected(it)
                isDropDownExpanded = false
            },
            request = { isDropDownExpanded = it },
            isOpened = isDropDownExpanded,
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}



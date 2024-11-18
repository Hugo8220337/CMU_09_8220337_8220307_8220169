package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.DropDownList
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.WorkoutViewModel

// Enum RunSortOrder separado
enum class EnumEntries {
    GENERATED_WORKOUTS,
    RUUNING
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
    // Dados estáticos para visualização
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

    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "title", style = MaterialTheme.typography.titleLarge)
        DropDownButton()
    }

    RunningHistoryScreenContentStatic(
        runItems = staticRunList,
        onSortOrderSelected = {}, // Nenhuma ação para visualização estática
        onItemClick = {}, // Nenhuma ação para visualização estática
    )
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
private fun RunCardItem(
    run: Run,
    onItemClick: (Run) -> Unit = {}
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .clickable { onItemClick(run) }
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        RunItem(
            run = run,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Composable
private fun RunItem(
    run: Run,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = run.date, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = run.distance,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "${run.calories}  ${run.speed}", style = MaterialTheme.typography.bodySmall)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTopAppBar(
    onSortOrderSelected: (EnumEntries) -> Unit,
    onNavIconClick: () -> Unit,
    title: String = "Running History"
) {
    TopAppBar(
        title = {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        },
        actions = {
            var isDropDownExpanded by rememberSaveable { mutableStateOf(false) }
            val sortOrderList = remember { EnumEntries.entries }

            Column {
                Button(
                    onClick = { isDropDownExpanded = !isDropDownExpanded },
                ) {
                    Text(text = "Sort by", style = MaterialTheme.typography.labelLarge)
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "sort by")
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
    )
}

@Composable
fun DropDownButton() {
    var isDropDownExpanded by rememberSaveable { mutableStateOf(false) }
    val sortOrderList = remember { EnumEntries.entries }

    Column {
        Button(
            onClick = { isDropDownExpanded = !isDropDownExpanded },
        ) {
            Text(text = "Sort by", style = MaterialTheme.typography.labelLarge)
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "sort by")
        }
        DropDownList(
            list = sortOrderList,
            onItemSelected = {
//                onSortOrderSelected(it)
                isDropDownExpanded = false
            },
            request = { isDropDownExpanded = it },
            isOpened = isDropDownExpanded,
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}



package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.exerciceDbApi.ExerciseItem
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation.Screen
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.WorkoutViewModel

@Composable
fun WorkoutScreen(navController: NavController, bodyParts: List<String>) {
    val workoutViewModel: WorkoutViewModel = viewModel()
    val state = workoutViewModel.state

    // Navigate back to home if no body parts are provided
    if (bodyParts.isEmpty()) {
        navController.navigate(Screen.Home.route)
    }

    // Generate workout on the first composition
    LaunchedEffect(true) { // o true faz com que seja só executado uma única vez
        workoutViewModel.generateWorkout(bodyParts)
    }

    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 30.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (state.isGeneratingWorkout && state.workout.isEmpty()) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else {
                if (state.workout.isEmpty()) {
                    Text(
                        text = "No exercises available.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    ExerciseScreen(navController, exercises = state.workout)
                }
            }
        }
    }
}

@Composable
private fun ExerciseScreen(navController: NavController, exercises: List<ExerciseItem>) {
    var currentExercise by remember { mutableIntStateOf(0) }

    ExerciseCard(exercise = exercises[currentExercise])

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (currentExercise > 0) {
            Button(
                onClick = { currentExercise-- },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(text = "Retroceder")
            }
        }

        if (currentExercise < exercises.size - 1) {
            Button(
                onClick = { currentExercise++ },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(text = "Avançar")
            }
        } else {
            Button(
                onClick = { navController.navigate(Screen.Home.route) },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Finalizar")
            }
        }
    }
}

@Composable
private fun ExerciseCard(exercise: ExerciseItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        AsyncImage(
            model = exercise.gifUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .size(300.dp)
                .align(Alignment.CenterHorizontally)
                .clip(MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = exercise.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(text = "Instructions:", style = MaterialTheme.typography.bodyMedium)

        exercise.instructions.forEachIndexed { index, instruction ->
            Text(text = "${index + 1}) $instruction", style = MaterialTheme.typography.bodySmall)
        }
    }
}
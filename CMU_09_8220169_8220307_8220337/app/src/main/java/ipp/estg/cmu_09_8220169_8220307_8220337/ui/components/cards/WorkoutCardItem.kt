package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Workout
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Converter

@Composable
fun WorkoutCardItem(workout: Workout, onItemClick: (Workout) -> Unit = {}) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .clickable { onItemClick(workout) }
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        WorkoutItem(
            workout = workout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Composable
private fun WorkoutItem(
    workout: Workout,
    modifier: Modifier = Modifier
) {
    val converter = Converter()
    val trainedBodyParts: List<String> = converter.toStringList(workout.trainedBodyParts)

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = workout.dateOfWorkout,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.trained_parts),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(end = 8.dp)
            )

            Column {
                trainedBodyParts.forEach { bodyPart ->
                    Text(
                        text = bodyPart,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
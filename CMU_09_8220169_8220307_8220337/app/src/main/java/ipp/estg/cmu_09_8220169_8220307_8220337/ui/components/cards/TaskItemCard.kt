package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ipp.estg.cmu_09_8220169_8220307_8220337.R

@Composable
fun TaskItemCard(
    task: String,
    isTaskCompleted: Boolean,
    onTaskToggle: (Boolean) -> Unit
) {
    val backgroundColor =
        if (isTaskCompleted) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    val textColor =
        if (isTaskCompleted) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                onTaskToggle(!isTaskCompleted)
            },
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
                checked = isTaskCompleted,
                onCheckedChange = {
                    onTaskToggle(!isTaskCompleted)
                },
                colors = CheckboxDefaults.colors(MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.width(8.dp))


            Icon(
                imageVector = when (task) {
                    stringResource(id = R.string.drink_4l_water) -> Icons.Default.LocalDrink
                    stringResource(id = R.string.complete_2_workouts) -> Icons.Default.FitnessCenter
                    stringResource(id = R.string.follow_diet) -> Icons.Default.Restaurant
                    stringResource(id = R.string.read_10_pages) -> Icons.Default.Book
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
package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Running

@Composable
fun RunCardItem(
    running: Running,
    onItemClick: (Running) -> Unit = {}
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .clickable { onItemClick(running) }
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        RunItem(
            running = running,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Composable
private fun RunItem(
    running: Running,
    modifier: Modifier = Modifier
) {
    val distance: String = String.format("%.2f", running.distance)
    val calories: Int = running.calories
    Column(modifier = modifier) {
        Text(text = running.date, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "$distance km",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "$calories cal,  ${running.duration} " + stringResource(id = R.string.seconds),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${running.steps}" + stringResource(id = R.string.steps),
            style = MaterialTheme.typography.bodySmall
        )
    }
}


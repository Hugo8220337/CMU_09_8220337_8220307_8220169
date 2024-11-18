package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs.Run

@Composable
fun RunCardItem(
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
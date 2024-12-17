package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.theme.GoldColor

@Composable
fun StreakLinearProgressIndicator(streak: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Text("Day $sstreak of 75", style = MaterialTheme.typography.bodyMedium)
        Text(stringResource(id = R.string.streak_text, streak), style = MaterialTheme.typography.bodyMedium)

        LinearProgressIndicator(
            progress = {
                streak / 75f // Dynamic based on progress
            },
            color = if (streak <= 75) MaterialTheme.colorScheme.primary else GoldColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )
    }
}
package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ipp.estg.cmu_09_8220169_8220307_8220337.R

@Composable
fun RunDetails(distance: String, time: String, steps: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(12.dp)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            RunDetailItem(
                label = stringResource(id = R.string.distance),
                value = "$distance Km",
                icon = Icons.AutoMirrored.Filled.DirectionsRun
            )
            RunDetailItem(
                label = stringResource(id = R.string.duration),
                value = time,
                icon = Icons.Filled.Timer
            )
            RunDetailItem(
                label = stringResource(id = R.string.steps),
                value = steps.toString(),
                icon = Icons.Filled.Directions
            )
        }
    }
}
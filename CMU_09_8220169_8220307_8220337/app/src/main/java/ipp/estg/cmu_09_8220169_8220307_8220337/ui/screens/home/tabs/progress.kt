package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProgressScreen(completedDays: Int) {
    var totalDays by rememberSaveable { mutableIntStateOf(75) }

    LaunchedEffect(Unit) {
        if (completedDays > totalDays) {
            totalDays = completedDays
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 50.dp),  // Adapta as colunas com um tamanho mínimo
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(totalDays) { day ->
            DayCircle(
                day = day + 1,
                completedDays = completedDays
            )
        }

    }
}

@Composable
private fun DayCircle(day: Int, completedDays: Int) {
    val backgroundColor = when {
        day <= completedDays -> MaterialTheme.colorScheme.primary
        day == completedDays + 1 -> Color.Transparent
        else -> Color.Transparent
    }

    val borderModifier = if (day == completedDays + 1) {
        Modifier.border(2.dp, Color.LightGray, CircleShape)
    } else Modifier

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .then(borderModifier)
            .background(color = backgroundColor, shape = CircleShape)
    ) {
        Text(
            text = "$day",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
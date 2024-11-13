package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineSeatLegroomExtra
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.LegendToggle
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.SettingsInputComponent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ipp.estg.cmu_09_8220169_8220307_8220337.BuildConfig
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation.Screen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.theme.CMU_09_8220169_8220307_8220337Theme
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Converter
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.WorkoutViewModel
import kotlinx.coroutines.launch

@Composable
fun WorkoutGeneratorScreen(navController: NavController) {
    val selectedParts = remember { mutableStateListOf<String>() }
    val bodyParts = listOf(
        "Back" to Icons.Default.Backpack,
        "Cardio" to Icons.Default.DirectionsRun,
        "Chest" to Icons.Default.FitnessCenter,
        "Lower Arms" to Icons.Default.SettingsInputComponent,
        "Lower Legs" to Icons.Default.AirlineSeatLegroomExtra,
        "Neck" to Icons.Default.Headset,
        "Shoulders" to Icons.Default.LegendToggle,
        "Upper Arms" to Icons.Default.NearMe,
        "Upper Legs" to Icons.Default.FolderOpen,
        "Waist" to Icons.Default.Backpack
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            Text(
                text = stringResource(id = R.string.select_body_parts),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Body Parts Grid
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(bodyParts) { (part, icon) ->
                    BodyPartCard(
                        partName = part,
                        partIcon = icon,
                        isSelected = selectedParts.contains(part),
                        onPartClicked = {
                            if (selectedParts.contains(part)) selectedParts.remove(part)
                            else selectedParts.add(part)
                        }
                    )
                }
            }

            // Generate Workout Button
            Button(
                onClick = {
                    val converter = Converter()
                    val selectedPartsAsString = converter.fromStringList(selectedParts)
                    navController.navigate("${Screen.Workout.route}/$selectedPartsAsString")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(28.dp),
                enabled = selectedParts.isNotEmpty()
            ) {
                Text(
                    text = stringResource(id = R.string.generate_workout),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Generate Workout"
                )
            }
        }
    }
}

@Composable
private fun BodyPartCard(
    partName: String,
    partIcon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onPartClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPartClicked() }
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = partIcon,
                contentDescription = partName,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = partName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Checkbox(
                checked = isSelected,
                onCheckedChange = { onPartClicked() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}
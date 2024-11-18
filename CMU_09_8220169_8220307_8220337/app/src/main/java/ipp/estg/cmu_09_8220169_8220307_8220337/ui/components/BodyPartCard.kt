package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BodyPartCard(navController: NavController) {

    ElevatedCard(
        onClick = {  },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .padding(10.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .background(
//                    Brush.linearGradient(
//                        listOf(
//                            dominantColor,
//                            defaultDominantColor
//                        )
//                    )
//                )
                .padding(16.dp)
        ) {
            Column {
//                AsyncImage(
//                    model = pokemonImageUrl,
//                    contentDescription = pokemon.name,
//                    modifier = Modifier
//                        .height(90.dp)
//                        .width(90.dp),
//                    onSuccess = { painterState ->
//                        val drawable = (painterState.result as SuccessResult).drawable
//                        homeMvvm.calcDominantColor(drawable) { color ->
//                            dominantColor = color // Store the dominant color
//                        }
//                    }
//                )
            }

            Column {
                Row {
                    Text(
                        text = "Punhos",
                        modifier = Modifier
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                    )

                }

//                Row {
//                    Text(
//                        text = "Tipos",
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.padding(start = 16.dp)
//                    )
//                }
            }
        }


    }
}
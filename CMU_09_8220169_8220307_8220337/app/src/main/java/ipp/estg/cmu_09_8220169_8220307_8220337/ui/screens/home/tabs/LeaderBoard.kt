package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.leaderBoardEntries.LeaderboardEntryCalories
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.leaderBoardEntries.LeaderboardEntryExerciseTime
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.leaderBoardEntries.LeaderboardEntrySteps
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.buttons.LeaderboardDropDownButton
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.LeaderboardViewModel
import ipp.estg.mobile.ui.components.utils.Loading


enum class EnumLeaderboardEntries(val value: Int) {
    CALORIES(R.string.burned_calories),
    EXERCISE_TIME(R.string.exercice_time),
    STEPS(R.string.number_of_steps);
}

@Composable
fun LeaderboardPage(
    leaderboardViewModel: LeaderboardViewModel = viewModel()
) {

    // Obter o leaderboard por calorias
    val leaderboardState by leaderboardViewModel.leaderboardState.collectAsState()
    // Obter o leaderboard por tempo de exercício
    val leaderboardStateExerciseTime by leaderboardViewModel.leaderboardStateExerciseTime.collectAsState()
    // Obter o leaderboard por nível de atividade
    val leaderboardStateSteps by leaderboardViewModel.leaderboardStateSteps.collectAsState()

    val isLoading by leaderboardViewModel.isLoading.collectAsState()
    val errorMessage by leaderboardViewModel.errorMessage.collectAsState()

    // Controlar qual tab está selecionada
    var selectedTab by rememberSaveable { mutableStateOf(EnumLeaderboardEntries.CALORIES) }

    // Carregar dados ao abrir a tela
    LaunchedEffect(Unit) {
            leaderboardViewModel.getLeaderboardByCalories()
            leaderboardViewModel.getLeaderboardByExerciseTime()
            leaderboardViewModel.getLeaderboardBySteps()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            LeaderboardDropDownButton(onSortOrderSelected = { selectedTab = it })
        }

        Text(
            text = stringResource(id = selectedTab.value),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        when (selectedTab) {
            EnumLeaderboardEntries.CALORIES -> {
                if (isLoading) {
                    Loading()
                } else if (errorMessage?.isNotEmpty() == true) {
                    Text(text = errorMessage.toString())
                } else {
                    LeaderboardCaloriesList(entries = leaderboardState)
                }
            }
            EnumLeaderboardEntries.EXERCISE_TIME -> {
                if (isLoading) {
                    Loading()
                } else if (errorMessage?.isNotEmpty() == true) {
                    Text(text = errorMessage.toString())
                } else {
                    LeaderboardListExerciseTime(entries = leaderboardStateExerciseTime)
                }
            }
            EnumLeaderboardEntries.STEPS -> {
                if (isLoading) {
                    Loading()
                } else if (errorMessage?.isNotEmpty() == true) {
                    Text(text = errorMessage.toString())
                } else {
                    LeaderboardListSteps(entries = leaderboardStateSteps)
                }
            }
        }
    }
}

@Composable
private fun LeaderboardCaloriesList(entries: List<LeaderboardEntryCalories>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(entries.sortedByDescending { it.calories }) { index, entry ->
            LeaderboardItem(
                rank = index + 1,
                userName = entry.name,
                leaderboardData = entry.calories.toString() + " cal"
            )
        }
    }
}

@Composable
fun LeaderboardListExerciseTime(entries: List<LeaderboardEntryExerciseTime>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(entries.sortedByDescending { it.exerciseTime }) { index, entry ->
            LeaderboardItem(
                rank = index + 1,
                userName = entry.name,
                leaderboardData = formatDuration(entry.exerciseTime)
            )
        }
    }
}

// Função para formatar a duração (em segundos) para horas, minutos e segundos
private fun formatDuration(exerciseTime: Double): String {
    val hours = (exerciseTime / 3600).toInt()  // Calcula as horas (3600 segundos em uma hora)
    val minutes = ((exerciseTime % 3600) / 60).toInt()  // Calcula os minutos restantes
    val seconds = (exerciseTime % 60).toInt()  // Calcula os segundos restantes

    // Exibe as horas, minutos e segundos no formato "Xh Ym Zs"
    return if (hours > 0) {
        "${hours}h ${minutes}m ${seconds}s"
    } else if (minutes > 0) {
        "${minutes}m ${seconds}s"
    } else {
        "${seconds}s"
    }
}

@Composable
private fun LeaderboardListSteps(entries: List<LeaderboardEntrySteps>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(entries.sortedByDescending { it.steps }) { index, entry ->
            LeaderboardItem(
                rank = index + 1,
                userName = entry.userName,
                leaderboardData = entry.steps.toString()
            )
        }
    }
}

@Composable
private fun LeaderboardItem(
    rank: Int,
    userName: String,
    leaderboardData: String
) {
    // Gradientes base para os efeitos metálicos
    val baseBrush = when (rank) {
        1 -> listOf(Color(0xFFFFD700), Color(0xFFFFE234)) // Dourado
        2 -> listOf(Color(0xFFC0C0C0), Color(0xFFE5E5E5)) // Prateado
        3 -> listOf(Color(0xFFCD7F32), Color(0xFFD89659)) // Bronze
        else -> listOf(Color(0xFFF0F0F0), Color(0xFFFAFAFA)) // Neutro
    }

    // Criando uma animação simples para brilho alternado
    val transition = rememberInfiniteTransition(label = "LeaderboardShineAnimation")

    // Animação para o brilho visível nos três primeiros ranks
    val animatedOffset by transition.animateFloat(
        initialValue = -400f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShineOffset"
    )

    // Criar a animação de brilho apenas para os ranks 1, 2 e 3
    val isActive = rank in 1..3 // Vai ser visível para os três primeiros ranks

    // Se o rank for 1, 2 ou 3, aplicamos o brilho, caso contrário, um gradiente normal
    val backgroundBrush = if (isActive) {
        Brush.linearGradient(
            colors = baseBrush + Color.White.copy(alpha = 0.4f) + baseBrush,
            start = Offset(animatedOffset, animatedOffset),
            end = Offset(animatedOffset + 400f, animatedOffset + 400f)
        )
    } else {
        Brush.linearGradient(colors = baseBrush) // Gradiente fixo para outros ranks
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(backgroundBrush, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank
            Text(
                text = "$rank°",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.width(40.dp)
            )

            Text(
                text = userName,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = leaderboardData,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}
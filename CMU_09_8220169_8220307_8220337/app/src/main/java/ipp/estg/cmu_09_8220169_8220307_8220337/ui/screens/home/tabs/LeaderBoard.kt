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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Updated data model for calories burned
data class LeaderboardEntry(
    val userName: String,
    val caloriesBurned: Int,  // Changed to represent calories burned
    val userId: String? = null
)

@Composable
fun LeaderboardPage() {
    // TODO: Replace with real data (e.g., from ViewModel or Firebase)
    val caloriesEntries = listOf(
        LeaderboardEntry("João Silva", 1500),
        LeaderboardEntry("Maria Souza", 1350),
        LeaderboardEntry("Pedro Santos", 1200),
        LeaderboardEntry("Ana Oliveira", 1100),
        LeaderboardEntry("Carlos Pereira", 950)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Top Calorias Queimadas",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LeaderboardList(caloriesEntries)
    }
}

@Composable
fun LeaderboardList(entries: List<LeaderboardEntry>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(entries.sortedByDescending { it.caloriesBurned }) { index, entry ->
            LeaderboardItem(
                rank = index + 1,
                userName = entry.userName,
                caloriesBurned = entry.caloriesBurned
            )
        }
    }
}

@Composable
fun LeaderboardItem(
    rank: Int,
    userName: String,
    caloriesBurned: Int
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

            // Nome do usuário
            Text(
                text = userName,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )

            // Calorias queimadas
            Text(
                text = "$caloriesBurned cal",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}







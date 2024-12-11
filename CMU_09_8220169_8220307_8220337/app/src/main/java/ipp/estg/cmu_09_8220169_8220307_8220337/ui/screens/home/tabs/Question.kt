import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoubtsScreen() {
    val context = LocalContext.current
    val companyEmail = "empresa@email.com"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Em caso de dúvidas",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )

        // Card para email da empresa - Centralizado
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFCE4EC))
                    .clickable {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:$companyEmail")
                        }
                        context.startActivity(intent)
                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email icon",
                        tint = Color(0xFFE91E63),
                        modifier = Modifier.size(36.dp)
                    )

                    Text(
                        text = "Envie-nos um email",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Text(
                        text = companyEmail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFE91E63)
                    )
                }
            }
        }

        // Seção de desenvolvedores
        Text(
            text = "Equipe de Desenvolvimento",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        // Cards dos desenvolvedores
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DeveloperCard("Hugo", "8220337@estg.ipp.pt")
            DeveloperCard("Pedro", "8220307@estg.ipp.pt")
            DeveloperCard("Cesar", "8220169@estg.ipp.pt")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperCard(name: String, email: String) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$email")
                }
                context.startActivity(intent)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Developer icon",
                tint = Color(0xFFE91E63),
                modifier = Modifier.size(24.dp)
            )

            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFE91E63)
                )
            }
        }
    }
}
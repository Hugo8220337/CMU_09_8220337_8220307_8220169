package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation.Screen
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.UserViewModel
import kotlinx.coroutines.launch


@Composable
fun OnboardingScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f) // Let the pager take up available space
            ) { page ->
                when (page) {
                    0 -> OnboardingScreen1()
                    1 -> OnboardingScreen2()
                    2 -> OnboardingScreen3()
                    // Adicione mais telas de onboarding aqui se necessário
                }
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage < 2) { // Mude para 2 se houver 3 páginas no total
                            pagerState.scrollToPage(pagerState.currentPage + 1)
                        } else {
                            // Navega para a tela inicial ou principal do app após o onboarding
                            navController.navigate(Screen.Home.route) {
                                // Remove a tela de onboarding da pilha para que o usuário não possa voltar
                                popUpTo(Screen.Onboarding.route) { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                //OnboardingScreen3()
                Text(text = if (pagerState.currentPage < 2) "Next" else "Get Started")
            }
        }

    }
}

@Composable
private fun OnboardingScreen1() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = R.drawable.wave,
            contentDescription = "Exemplo de GIF",
            modifier = Modifier.size(250.dp)
        )
        Text(
            text = "Welcome to MyApp!",
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Hi there, seems like you're new around here.",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun OnboardingScreen2() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = R.drawable.muscle_man,
            contentDescription = "Exemplo de GIF",
            modifier = Modifier
                .size(400.dp)
                .padding(horizontal = 0.dp, vertical = 0.dp)
        )
        Text(
            text = "Welcome to MyApp!",
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome to 75Heart, here we will guide you in different ways to put you in shape.",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun OnboardingScreen3() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = R.drawable.phone_checkmark,
            contentDescription = "Exemplo de GIF",
            modifier = Modifier
                .size(400.dp)
                .padding(horizontal = 0.dp, vertical = 0.dp)
        )
        Text(
            text = "Welcome to MyApp!",
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "But looks like it is your first time here so first we need you to create a account for yourself.",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}
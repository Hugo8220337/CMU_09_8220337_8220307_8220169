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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation.Screen
import kotlinx.coroutines.launch


@Composable
fun OnboardingScreen(
    navController: NavController
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
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> OnboardingScreen1()
                    1 -> OnboardingScreen2()
                    2 -> OnboardingScreen3()
                }
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage < 2) { // Mudar para 2 se houver 3 páginas no total
                            pagerState.scrollToPage(pagerState.currentPage + 1)
                        } else {
                            // Navega para a tela inicial ou principal do app após o onboarding
                            navController.navigate(Screen.Home.route) {
                                // Remove a tela de onboarding da stack para que o usuário não possa voltar
                                popUpTo(Screen.Onboarding.route) { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                Text(text = if (pagerState.currentPage < 2) stringResource(id = R.string.next) else stringResource(
                    id = R.string.get_started
                ))
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
            text = stringResource(id = R.string.wellcome_to_75hard),
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.seems_like_youre_new),
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
            text = stringResource(id = R.string.wellcome_to_75hard),
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.wellcome_to_75hard_description),
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
            text = stringResource(id = R.string.wellcome_to_75hard),
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.lets_get_started),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}
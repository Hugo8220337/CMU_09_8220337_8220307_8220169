package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.utils.SuperUsefulDropDownMenuBox
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.HomeViewModel

@Composable
fun SettingsScreen(navController: NavController, homeViewModel: HomeViewModel) {

    val settingsPreferencesRepo = homeViewModel.settingsPreferencesRepository

    var notificationsEnabled by remember { mutableStateOf( settingsPreferencesRepo.getNotificationsPreference()) }
    var darkModeEnabled by remember { mutableStateOf(settingsPreferencesRepo.getDarkModePreference()) }
    var selectedLanguage by remember { mutableStateOf(settingsPreferencesRepo.getLanguagePreference()) }


    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        Text(
            text = "Preferências de Notificação",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Switch for enabling/disabling notifications
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Ativar Notificações")
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = {
                    notificationsEnabled = it
                    settingsPreferencesRepo.setNotificationsPreference(it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Preferências de Tema",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Switch for enabling/disabling dark mode
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Ativar Modo Escuro")
            Switch(
                checked = darkModeEnabled,
                onCheckedChange = {
                    darkModeEnabled = it
//                    Hard75Application.appModule.setDarkMode(it)
                    settingsPreferencesRepo.setDarkModePreference(it)

                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Idioma",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        SuperUsefulDropDownMenuBox(
            label = "Idioma",
            currentValue = selectedLanguage,
            options = listOf("pt-pt", "en"),
            onOptionSelected = {
                selectedLanguage = it
                settingsPreferencesRepo.setLanguagePreference(it)

            }
        )


    }
}

//@Preview(showBackground = true)
//@Composable
//fun SettingsPreview() {
//    CMU_09_8220169_8220307_8220337Theme {
//        val navController = rememberNavController()
//
//        SettingsScreen(navController)
//    }
//}
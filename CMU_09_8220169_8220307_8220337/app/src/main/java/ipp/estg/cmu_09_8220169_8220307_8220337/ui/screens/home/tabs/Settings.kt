package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.utils.SuperUsefulDropDownMenuBox
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation.Screen
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.AuthenticationViewModel
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.HomeViewModel

@Composable
fun SettingsScreen(navController: NavController, homeViewModel: HomeViewModel, authViewModel: AuthenticationViewModel) {

    val settingsPreferencesRepo = homeViewModel.settingsPreferencesRepository

    var notificationsEnabled by remember { mutableStateOf(settingsPreferencesRepo.getNotificationsPreference()) }
    var darkModeEnabled by remember { mutableStateOf(settingsPreferencesRepo.getDarkModePreference()) }
    var selectedLanguage by remember { mutableStateOf(settingsPreferencesRepo.getLanguagePreference()) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.notification_preferences),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Switch for enabling/disabling notifications
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(id = R.string.enable_notifications))
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
            text = stringResource(id = R.string.theme_preferences),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Switch for enabling/disabling dark mode
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(id = R.string.enable_dark_mode))
            Switch(
                checked = darkModeEnabled,
                onCheckedChange = {
                    darkModeEnabled = it
                    settingsPreferencesRepo.setDarkModePreference(it)

                    // Force recreation of the activity to apply theme changes immediately
                    if (context is androidx.activity.ComponentActivity) {
                        context.recreate()  // Recreates the activity to apply the new theme
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.language),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Language dropdown menu
        SuperUsefulDropDownMenuBox(
            label = stringResource(id = R.string.language),
            currentValue = selectedLanguage,
            options = listOf("pt-rPT", "en", "de"),
            onOptionSelected = {
                selectedLanguage = it
                settingsPreferencesRepo.setLanguagePreference(it)
            }
        )
    }

    // Log out button
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = {
                authViewModel.logout()
                navController.navigate(Screen.Start.route)
            },
            modifier = Modifier
                .fillMaxWidth(0.45f)
                .padding(vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Text("Log out")
        }
    }
}
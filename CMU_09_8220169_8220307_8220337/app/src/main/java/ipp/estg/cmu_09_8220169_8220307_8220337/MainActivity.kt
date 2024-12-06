package ipp.estg.cmu_09_8220169_8220307_8220337

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ipp.estg.cmu_09_8220169_8220307_8220337.preferences.SettingsPreferencesRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation.Screen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.HomeScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.OnboardingScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.RunningWorkoutScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.WorkoutScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.auth.StartScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.auth.LoginScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.auth.RegisterScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.theme.CMU_09_8220169_8220307_8220337Theme
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Converter
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.AuthenticationViewModel
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.RunningViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Recuperar a preferência do tema (modo escuro ou claro)
        val settingsPreferences = SettingsPreferencesRepository(this)
        val darkModeEnabled = settingsPreferences.getDarkModePreference()

        setContent {
            // Aplica o tema com base na preferência de modo escuro
            CMU_09_8220169_8220307_8220337Theme(
                darkTheme = darkModeEnabled
            ) {
                val navController = rememberNavController()

                // Configurar a navegação e o conteúdo da aplicação
                MyApp(navController)
            }
        }
    }
}

@Composable
fun MyApp(navController: NavHostController) {

    // Set up a NavHost to hold different composable destinations (screens)
    NavHost(
        navController = navController,
        startDestination = Screen.Start.route
    ) {
        composable(Screen.Start.route) {
            StartScreen(navController)
        }
        composable(Screen.Login.route) {
            val authViewModel: AuthenticationViewModel = viewModel()
            LoginScreen(navController, authViewModel)
        }
        composable(Screen.Register.route) {
            val authViewModel: AuthenticationViewModel = viewModel()
            RegisterScreen(navController, authViewModel)
        }
        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Workout.route + "/{bodyparts}") { backStackEntry ->
            val converter = Converter()

            // Converte os parámetros numa lista
            val bodyParts = backStackEntry.arguments?.getString("bodyparts")
            val bodyPartsList = converter.toStringList(bodyParts!!)

            WorkoutScreen(navController, bodyPartsList)
        }
        composable(Screen.RunningWorkout.route) {
            RunningWorkoutScreen(navController, viewModel(modelClass = RunningViewModel::class.java))
        }
    }
}
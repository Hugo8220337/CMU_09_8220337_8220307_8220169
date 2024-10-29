package ipp.estg.cmu_09_8220169_8220307_8220337

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation.Screen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.HomeScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.OnboardingScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.StartScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.auth.LoginScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.auth.RegisterScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.theme.CMU_09_8220169_8220307_8220337Theme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CMU_09_8220169_8220307_8220337Theme {
                val navController =
                    rememberNavController() // Create NavController to track current route

                // Set up the main app
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
        startDestination = Screen.Start.route,
    ) {
        composable(Screen.Start.route) {
            StartScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
    }
}
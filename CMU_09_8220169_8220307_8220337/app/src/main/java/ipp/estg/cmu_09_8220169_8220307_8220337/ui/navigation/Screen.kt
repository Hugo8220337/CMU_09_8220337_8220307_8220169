package ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation

sealed class Screen(val route: String) {
    data object Start: Screen("start")
    data object Login: Screen("login")
    data object Register: Screen("register")
    data object Onboarding: Screen("onBoarding")
    data object Home: Screen("home")
    data object Workout: Screen("workout")
    data object RunningWorkout: Screen("runningWorkout")
    data object EditProfile: Screen("editProfile")
}
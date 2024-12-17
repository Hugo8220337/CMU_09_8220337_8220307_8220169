package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home

import DoubtsScreen
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Microwave
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AllInclusive
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Microwave
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.navigation.menuWithLeftNavigation.MenuWithLeftNavigation
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.navigation.menuWithLeftNavigation.NavigationItem
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs.LeaderboardPage
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs.MainContent
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs.ProfileScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs.ProgressScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs.RunningWorkoutStartScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs.SettingsScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs.WorkoutGeneratorScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs.WorkoutHistoryPage
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.DailyTasksViewModel
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController
) {
    val homeViewModel: HomeViewModel = viewModel()
    val dailyTasksViewModel: DailyTasksViewModel = viewModel()

    val streak by dailyTasksViewModel.streak.collectAsState()

    val startingNavItem by remember { mutableIntStateOf(0) }

    val navItems = listOf(
        NavigationItem(
            title = stringResource(id = R.string.home),
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            content = {
                MainContent(homeViewModel, dailyTasksViewModel)
            }
        ),
        NavigationItem(
            title = stringResource(id = R.string.progress),
            selectedIcon = Icons.Filled.DateRange,
            unselectedIcon = Icons.Outlined.DateRange,
            content = {
                ProgressScreen(streak)
            }
        ),
        NavigationItem(
            title = stringResource(id = R.string.workout_generator),
            selectedIcon = Icons.Filled.FitnessCenter,
            unselectedIcon = Icons.Outlined.FitnessCenter,
            content = {
                WorkoutGeneratorScreen(navController)
            }
        ),
        NavigationItem(
            title = stringResource(id = R.string.running),
            selectedIcon = Icons.AutoMirrored.Filled.DirectionsRun,
            unselectedIcon = Icons.AutoMirrored.Outlined.DirectionsRun,
            content = {
                RunningWorkoutStartScreen(navController)
            }
        ),
        NavigationItem(
            title = stringResource(id = R.string.history),
            selectedIcon = Icons.Filled.DateRange,
            unselectedIcon = Icons.Outlined.DateRange,
            content = {
                WorkoutHistoryPage(homeViewModel = homeViewModel)
            }
        ),
        NavigationItem(
            title = stringResource(id = R.string.leaderboard),
            selectedIcon = Icons.Filled.AllInclusive,
            unselectedIcon = Icons.Outlined.AllInclusive,
            content = {
                LeaderboardPage()
            }
        ),
        NavigationItem(
            title = stringResource(id = R.string.profile),
            selectedIcon = Icons.Filled.Face,
            unselectedIcon = Icons.Outlined.Face,
            content = {
                ProfileScreen(navController)
            }
        ),
        NavigationItem(
            title = stringResource(id = R.string.settings),
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            content = {
                SettingsScreen()
            }
        ),
        NavigationItem(
            title = stringResource(id = R.string.questions),
            selectedIcon = Icons.Filled.Microwave,
            unselectedIcon = Icons.Outlined.Microwave,
            content = {
                DoubtsScreen()
            }
        )
    )


    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior() // existem 3 destes para brincar
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Track the selected index to switch between contents
    var selectedIndex by remember { mutableIntStateOf(startingNavItem) }

    MenuWithLeftNavigation(
        items = navItems,
        drawerState = drawerState,
        onItemSelected = { index ->
            selectedIndex = index  // Update selected index when an item is clicked
        }
    ) {

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {

                TopAppBar(
                    title = { Text(navItems[selectedIndex].title) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = stringResource(id = R.string.open_menu))
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )

            }

        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                // Display the content based on the selected item
                navItems[selectedIndex].content()
            }
        }
    }
}
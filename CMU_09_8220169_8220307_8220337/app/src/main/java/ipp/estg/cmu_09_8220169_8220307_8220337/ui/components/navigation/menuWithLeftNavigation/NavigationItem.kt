package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.navigation.menuWithLeftNavigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean = false,
    val badgeCount: Int? = null,
    val content: @Composable () -> Unit
)

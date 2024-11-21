package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.utils

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InvisibleButton(
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier.size(0.dp)
    ) {

    }
}
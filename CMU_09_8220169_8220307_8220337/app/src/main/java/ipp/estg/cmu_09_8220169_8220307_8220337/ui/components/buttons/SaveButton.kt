package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ipp.estg.cmu_09_8220169_8220307_8220337.R

@Composable
fun SaveButton(onSave: () -> Unit) {
    IconButton(
        onClick = {
            onSave()
        },
        modifier = Modifier
            .padding(8.dp)
            .size(36.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.Save,
            contentDescription = stringResource(id = R.string.save),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}
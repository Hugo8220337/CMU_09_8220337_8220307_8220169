package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.DropDownList
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs.EnumEntries
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs.EnumLeaderboardEntries

@Composable
fun DropDownButton(onSortOrderSelected: (EnumEntries) -> Unit) {
    var isDropDownExpanded by rememberSaveable { mutableStateOf(false) }
    val sortOrderList = remember { EnumEntries.entries }

    Column {
        Button(
            onClick = { isDropDownExpanded = !isDropDownExpanded },
        ) {
            Text(text = stringResource(id = R.string.sort_by), style = MaterialTheme.typography.labelLarge)
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = stringResource(id = R.string.sort_by))
        }
        DropDownList(
            list = sortOrderList,
            onItemSelected = {
                onSortOrderSelected(it)
                isDropDownExpanded = false
            },
            request = { isDropDownExpanded = it },
            isOpened = isDropDownExpanded,
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}


@Composable
fun LeaderboardDropDownButton(onSortOrderSelected: (EnumLeaderboardEntries) -> Unit) {
    var isDropDownExpanded by rememberSaveable { mutableStateOf(false) }
    val sortOrderList = remember { EnumLeaderboardEntries.entries }

    Column {
        Button(
            onClick = { isDropDownExpanded = !isDropDownExpanded },
        ) {
            Text(text = stringResource(id = R.string.sort_by), style = MaterialTheme.typography.labelLarge)
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = stringResource(id = R.string.sort_by))
        }
        DropDownList(
            list = sortOrderList,
            onItemSelected = {
                onSortOrderSelected(it)
                isDropDownExpanded = false
            },
            request = { isDropDownExpanded = it },
            isOpened = isDropDownExpanded,
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}


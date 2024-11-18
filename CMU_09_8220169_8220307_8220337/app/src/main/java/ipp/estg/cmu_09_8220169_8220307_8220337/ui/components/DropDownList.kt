package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T : Any> DropDownList(
    modifier: Modifier = Modifier,
    list: List<T>,
    stringTransform: (T) -> String = { it.toString() },
    onItemSelected: (T) -> Unit,
    isOpened: Boolean = false,
    request: (Boolean) -> Unit,
) {
    DropdownMenu(
        expanded = isOpened,
        onDismissRequest = { request(false) },
        modifier = modifier.wrapContentSize()
    ) {
        list.forEach { item ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringTransform(item),
                        modifier = Modifier
                            .wrapContentWidth()
                            .align(Alignment.Start)
                    )
                },
                onClick = {
                    onItemSelected(item)
                    request(false) // Fechar o menu após a seleção
                },
            )
        }
    }
}
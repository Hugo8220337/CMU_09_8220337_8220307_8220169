package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomSheet(
    isSheetOpen: Boolean,
    onDismissRequest: () -> Unit,
    sheetContent: @Composable () -> Unit
) {

    // values that should be use
    val sheetState = rememberModalBottomSheetState()


    if(isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest =  onDismissRequest,
        ) {
            sheetContent()
        }
    }

}
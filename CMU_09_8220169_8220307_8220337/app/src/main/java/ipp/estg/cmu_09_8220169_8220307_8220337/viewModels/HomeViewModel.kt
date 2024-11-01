package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    var state by mutableStateOf(ScreenState())
        private set



    data class ScreenState(
        val isLoading: Boolean = false,
        val error: String? = null,
    )
}


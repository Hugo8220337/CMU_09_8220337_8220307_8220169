package ipp.estg.cmu_09_8220169_8220307_8220337.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ipp.estg.cmu_09_8220169_8220307_8220337.data.retrofit.RemoteApis
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.LocalDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.QuotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuoteViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var quotesRepository: QuotesRepository =
        QuotesRepository(
            RemoteApis.getQuotesApi(),
            LocalDatabase.getDatabase(application).quotesDao
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _dailyQuote = MutableStateFlow("")
    val dailyQuote = _dailyQuote.asStateFlow()


    fun loadDailyQuote() {
        viewModelScope.launch {
            _isLoading.value = true
            val dailyQuote = quotesRepository.getTodaysQuote().quote
            _dailyQuote.value = dailyQuote
            _isLoading.value = false
        }
    }
}
package ipp.estg.cmu_09_8220169_8220307_8220337.room.repositories

import androidx.lifecycle.LiveData
import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.Quote
import ipp.estg.cmu_09_8220169_8220307_8220337.room.dao.QuoteDao
import java.time.LocalDate

class QuoteRepository(
    private val quoteDao: QuoteDao,
) {
    // Função para inserir uma nova citação
    suspend fun insertQuote(
        quote: String,
    ){
        try {
            val newQuote = Quote(
                quote = quote
            )
            // Inserir citação
            quoteDao.insertQuote(newQuote)
        } catch (e: Exception) {
            throw e
        }
    }

    // Função para obter uma citação
    suspend fun getTodaysQuote(): Quote {
        val currentDate = LocalDate.now().toString()
        return quoteDao.getQuoteByDate(currentDate)
    }

}
package ipp.estg.cmu_09_8220169_8220307_8220337.repositories

import android.util.Log
import ipp.estg.cmu_09_8220169_8220307_8220337.data.retrofit.apis.QuotesApi
import ipp.estg.cmu_09_8220169_8220307_8220337.data.retrofit.models.quotesApi.QuoteRetrofitResponse
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao.QuoteDao
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.LocalDate

class QuotesRepository(
    private val quotesApi: QuotesApi,
    private val quoteDao: QuoteDao
) {

    suspend fun getTodaysQuote(): Quote {
        val currentDate = LocalDate.now().toString()

        // Try to get the quote from cache
        var dailyQuote = getTodaysQuoteFromCache(currentDate)

        // If not found in cache, fetch from API
        if (dailyQuote == null) {
            val response = try {
                getQuoteFromApi()
            } catch (e: Exception) {
                null
            }

            if (response == null || !response.isSuccessful) {
                // Default Quote in case of error
                return Quote(quote = "The only bad workout is the one you didn't do")
            }

            val quoteRetrofitResponse = response.body() ?: return Quote(quote = "Default quote due to error")

            // Convert and cache the quote
            dailyQuote = Quote(quote = quoteRetrofitResponse.text)
            insertQuoteInCache(dailyQuote)
        }

        return dailyQuote
    }

    private suspend fun getQuoteFromApi(): Response<QuoteRetrofitResponse> {
        return withContext(Dispatchers.IO) {
            val call = quotesApi.getQuote()
            call.execute()
        }
    }

    private suspend fun insertQuoteInCache(quote: Quote) {
        try {
            val newQuote = Quote(
                quote = quote.quote
            )
            quoteDao.insertQuote(newQuote)
        } catch (e: Exception) {
            Log.d("QuotesRepository", "Error inserting quote in cache")
        }
    }

    private suspend fun getTodaysQuoteFromCache(currentDate: String): Quote {
        return quoteDao.getQuoteByDate(currentDate)
    }
}
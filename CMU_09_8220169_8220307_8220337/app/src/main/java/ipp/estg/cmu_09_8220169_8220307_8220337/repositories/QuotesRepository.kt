package ipp.estg.cmu_09_8220169_8220307_8220337.repositories

import ipp.estg.cmu_09_8220169_8220307_8220337.data.local.Quote
import ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.quotesApi.QuoteRetrofitResponse
import ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.apis.QuotesApi
import ipp.estg.cmu_09_8220169_8220307_8220337.room.dao.QuoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.LocalDate

interface IQuotesRepository {
    suspend fun getTodaysQuote(): Quote
}

class QuotesRepository(
    private val quotesApi: QuotesApi,
    private val quoteDao: QuoteDao
) : IQuotesRepository {

    override suspend fun getTodaysQuote(): Quote {
        val currentDate = LocalDate.now().toString()

        var dailyQuote = getTodaysQuoteFromCache(currentDate)

        // se não está vazio é porque já fez um pedido à API hoje, logo pode ir simplesmente à cache
        if(dailyQuote == null) {
            val response = getQuoteFromApi()

            if (!response.isSuccessful) {
                throw Exception("Error code: ${response.code()}")
            }

            val quoteRetrofitResponse = response.body() ?: throw Exception("Error: Quote is null")

            // Convert Quote from api to quote from Room DB
            dailyQuote = Quote(
                quote = quoteRetrofitResponse.text
            )

            // Inseret quote in Room DB on a different thread
            withContext(Dispatchers.IO) {
                insertQuoteInCache(dailyQuote)
            }

            return dailyQuote
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
            throw e
        }
    }

    private suspend fun getTodaysQuoteFromCache(currentDate: String): Quote {
        return quoteDao.getQuoteByDate(currentDate)
    }
}
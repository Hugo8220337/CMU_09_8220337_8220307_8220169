package ipp.estg.cmu_09_8220169_8220307_8220337.retrofit

import ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.quotesApi.Quote
import retrofit2.http.GET

interface QuotesApi {

    @GET("quote")
    suspend fun getQuote(): Quote
}
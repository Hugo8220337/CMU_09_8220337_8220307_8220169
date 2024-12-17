package ipp.estg.cmu_09_8220169_8220307_8220337.data.retrofit.apis

import ipp.estg.cmu_09_8220169_8220307_8220337.BuildConfig
import ipp.estg.cmu_09_8220169_8220307_8220337.data.retrofit.models.quotesApi.QuoteRetrofitResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface QuotesApi {

    @GET("quote")
    fun getQuote(
        @Header("x-rapidapi-key") apiKey: String = BuildConfig.QUOTES_API_KEY,
    ): Call<QuoteRetrofitResponse>
}
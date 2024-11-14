package ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.apis

import ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.quotesApi.Quote
import retrofit2.Call
import retrofit2.http.GET

interface QuotesApi {

    @GET("quote")
    fun getQuote(): Call <Quote>
}
package ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.repositories


import ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.quotesApi.Quote
import ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.apis.QuotesApi
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuotesApiRepository(
    private val quotesApi: QuotesApi
) {

        fun getQuote(
            callback: (Resource<Quote>) -> Unit
        ) {
            val call = quotesApi.getQuote()

            call.enqueue(object : Callback<Quote> {
                override fun onResponse(call: Call<Quote>, response: Response<Quote>) {
                    if (response.isSuccessful) {
                        callback(Resource.Success(response.body() ?: Quote()))
                    } else {
                        callback(Resource.Error("Error code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<Quote>, t: Throwable) {
                    callback(Resource.Error("An unknown error occurred: ${t.localizedMessage}"))
                }
            })
        }

}
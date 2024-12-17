package ipp.estg.cmu_09_8220169_8220307_8220337.data.retrofit

import ipp.estg.cmu_09_8220169_8220307_8220337.data.retrofit.apis.ExerciseDbApi
import ipp.estg.cmu_09_8220169_8220307_8220337.data.retrofit.apis.QuotesApi
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.EXERCICE_DB_API_BASE_URL
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.QUOTES_API_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteApis {
    companion object {
        // útil para fazer chamadas HTTP
        private val client = OkHttpClient.Builder()
            .protocols(listOf(Protocol.HTTP_1_1))
            .build()

        private var exerciseDbApiInstance: ExerciseDbApi? = null
        private var quotesApiInstance: QuotesApi? = null

        /**
         * Get ExerciseDbApi Instance
         */
        fun getExerciseDbApi(): ExerciseDbApi {
            return exerciseDbApiInstance ?: synchronized(this) {
                val instance = Retrofit.Builder()
                    .baseUrl(EXERCICE_DB_API_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ExerciseDbApi::class.java)

                exerciseDbApiInstance = instance
                instance
            }
        }


        /**
         * Get QuotesApi Instace
         */
        fun getQuotesApi(): QuotesApi {
            return quotesApiInstance ?: synchronized(this) {
                val instance = Retrofit.Builder()
                    .baseUrl(QUOTES_API_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(QuotesApi::class.java)

                quotesApiInstance = instance
                instance

            }
        }
    }
}
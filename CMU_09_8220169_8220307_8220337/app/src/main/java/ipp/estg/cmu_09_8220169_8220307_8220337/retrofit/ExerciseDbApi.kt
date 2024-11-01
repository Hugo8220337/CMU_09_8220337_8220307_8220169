package ipp.estg.cmu_09_8220169_8220307_8220337.retrofit

import ipp.estg.cmu_09_8220169_8220307_8220337.BuildConfig
import ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.exerciceDbApi.BodyParts
import ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.exerciceDbApi.Exercises
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ExerciseDbApi {

    @GET("exercises/bodyPartList")
    suspend fun getBodyParts(): BodyParts

    @GET("exercises/bodyPart/{bodyPart}")
    fun getExercisesByBodyPart(
        @Header("x-rapidapi-key") apiKey: String = BuildConfig.EXERCICEDB_API_KEY,
        @Path("bodyPart") bodyPart: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<Exercises>

    // se calhar não vai ser preciso este
    @GET("exercises/name/{exerciseName}")
    suspend fun getExercisesByName(@Path("exerciseName") exerciseName: String): Exercises
}
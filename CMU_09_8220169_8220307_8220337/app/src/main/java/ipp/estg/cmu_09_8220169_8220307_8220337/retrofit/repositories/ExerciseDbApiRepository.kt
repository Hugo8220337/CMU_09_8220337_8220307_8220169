package ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.repositories

import androidx.lifecycle.LiveData
import ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.exerciceDbApi.Exercises
import ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.ExerciseDbApi
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExerciseDbApiRepository(
    private val exerciseDbApi: ExerciseDbApi
) {

//    suspend fun getExercisesByBodyPart(bodyPart: String, limit: Int, offset: Int) : Resource<Exercises>  {
//        val response = try {
//            exerciseDbApi.getExercisesByBodyPart(
//                bodyPart = bodyPart,
//                limit = limit,
//                offset =  offset
//            )
//        } catch(e: Exception) {
//            return Resource.Error("An unknown error occured: ${e.localizedMessage}")
//        }
//        return Resource.Success(response)
//    }

    fun getExercisesByBodyPart(
        bodyPart: String,
        limit: Int,
        offset: Int,
        callback: (Resource<Exercises>) -> Unit
    ) {
        val call = exerciseDbApi.getExercisesByBodyPart(
            bodyPart = bodyPart,
            limit = limit,
            offset = offset
        )

        call.enqueue(object : Callback<Exercises> {
            override fun onResponse(call: Call<Exercises>, response: Response<Exercises>) {
                if (response.isSuccessful) {
                    callback(Resource.Success(response.body() ?: Exercises()))
                } else {
                    callback(Resource.Error("Error code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Exercises>, t: Throwable) {
                callback(Resource.Error("An unknown error occurred: ${t.localizedMessage}"))
            }
        })
    }
}
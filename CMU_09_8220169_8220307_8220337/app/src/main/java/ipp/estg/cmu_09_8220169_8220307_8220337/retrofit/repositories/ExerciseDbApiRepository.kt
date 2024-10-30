package ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.repositories

import ipp.estg.cmu_09_8220169_8220307_8220337.data.remote.exerciceDbApi.Exercises
import ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.ExerciseDbApi
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Resource

class ExerciseDbApiRepository(
    private val exerciseDbApi: ExerciseDbApi
) {

    suspend fun getExercisesByBodyPart(bodyPart: String, limit: Int, offset: Int) : Resource<Exercises>  {
        val response = try {
            exerciseDbApi.getExercisesByBodyPart(
                bodyPart = bodyPart,
                limit = limit,
                offset =  offset
            )
        } catch(e: Exception) {
            return Resource.Error("An unknown error occured: ${e.localizedMessage}")
        }
        return Resource.Success(response)
    }
}
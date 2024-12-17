package ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.CollectionsNames
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.firestore.models.DailyTasksCollection
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.DailyTasks
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class DailyTasksFirestoreRepository(
    private val firestore: FirebaseFirestore = Firebase.firestore
) {

    private val authFirebaseRepository: AuthFirebaseRepository = AuthFirebaseRepository()

    // Insert daily task in Firebase
    suspend fun insertDailyTaskInFirebase(
        tasks: DailyTasks
    ) {
        try {
            val userId = authFirebaseRepository.getCurrentUser()?.uid

            if (userId != null) {
                val documentId = "$userId${LocalDate.now()}" // Compound key: userId + date

                val taskData = mapOf(
                    DailyTasksCollection.FIELD_ID to documentId,
                    DailyTasksCollection.FIELD_DATE to tasks.date,
                    DailyTasksCollection.FIELD_USER_ID to userId,
                    DailyTasksCollection.FIELD_GALLON_OF_WATER to tasks.gallonOfWater,
                    DailyTasksCollection.FIELD_TWO_WORKOUTS to tasks.twoWorkouts,
                    DailyTasksCollection.FIELD_FOLLOW_DIET to tasks.followDiet,
                    DailyTasksCollection.FIELD_READ_TEN_PAGES to tasks.readTenPages,
                    DailyTasksCollection.FIELD_TAKE_PROGRESS_PICTURE to tasks.takeProgressPicture
                )

            firestore.collection(CollectionsNames.dailyTasksCollection)
                .document(documentId) // Compound key: userId + date
                .set(taskData)
                .await()
            }else{
                Log.e("DailyTasksFirestoreRepository", "User ID is null")
            }
        } catch (e: Exception) {
            Log.d("DailyTasksFirestoreRepository", "Erro ao inserir daily task no Firebase", e)
        }
    }


    suspend fun getAllDailyTasksFromFirebase(): List<DailyTasks>? {
        return try {
            val result = firestore.collection(CollectionsNames.dailyTasksCollection)
                .get()
                .await()

            // Convert the result into a list of Workout objects
            result.documents.mapNotNull { document ->
                val date = document.get(DailyTasksCollection.FIELD_DATE) as String
                val userId = document.get(DailyTasksCollection.FIELD_USER_ID) as String
                val gallonOfWater = document.get(DailyTasksCollection.FIELD_GALLON_OF_WATER) as Boolean
                val twoWorkouts = document.get(DailyTasksCollection.FIELD_TWO_WORKOUTS) as Boolean
                val followDiet = document.get(DailyTasksCollection.FIELD_FOLLOW_DIET) as Boolean
                val readTenPages = document.get(DailyTasksCollection.FIELD_READ_TEN_PAGES) as Boolean
                val takeProgressPicture = document.get(DailyTasksCollection.FIELD_TAKE_PROGRESS_PICTURE) as String

                DailyTasks(
                    date = date,
                    userId = userId,
                    gallonOfWater = gallonOfWater,
                    twoWorkouts = twoWorkouts,
                    followDiet = followDiet,
                    readTenPages = readTenPages,
                    takeProgressPicture = takeProgressPicture
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getAllUserDailyTasksFromFirebase(): List<DailyTasks>? {
        val userId = authFirebaseRepository.getCurrentUser()?.uid
        return try {
            val result = firestore.collection(CollectionsNames.dailyTasksCollection)
                .whereEqualTo(DailyTasksCollection.FIELD_USER_ID, userId)
                .get()
                .await()

            // Convert the result into a list of Workout objects
            result.documents.mapNotNull { document ->
                val date = document.get(DailyTasksCollection.FIELD_DATE) as String
                val gallonOfWater = document.get(DailyTasksCollection.FIELD_GALLON_OF_WATER) as Boolean
                val twoWorkouts = document.get(DailyTasksCollection.FIELD_TWO_WORKOUTS) as Boolean
                val followDiet = document.get(DailyTasksCollection.FIELD_FOLLOW_DIET) as Boolean
                val readTenPages = document.get(DailyTasksCollection.FIELD_READ_TEN_PAGES) as Boolean
                val takeProgressPicture = document.get(DailyTasksCollection.FIELD_TAKE_PROGRESS_PICTURE) as String

                DailyTasks(
                    date = date,
                    userId = userId!!,
                    gallonOfWater = gallonOfWater,
                    twoWorkouts = twoWorkouts,
                    followDiet = followDiet,
                    readTenPages = readTenPages,
                    takeProgressPicture = takeProgressPicture
                )
            }
        } catch (e: Exception) {
            null
        }
    }


    // get daily tasks by user Id and date from Firebase
    suspend fun getDailyTasksByUserAndDateFromFirebase(date: String): List<DailyTasks>? {
        return try {
            val userId = authFirebaseRepository.getCurrentUser()?.uid

            val result = firestore.collection(CollectionsNames.dailyTasksCollection)
                .document("$userId$date")
                .get()
                .await()

            if (result != null && result.exists()) {
                val document = result.data
                val gallonOfWater = document?.get(DailyTasksCollection.FIELD_GALLON_OF_WATER) as Boolean
                val twoWorkouts = document[DailyTasksCollection.FIELD_TWO_WORKOUTS] as Boolean
                val followDiet = document[DailyTasksCollection.FIELD_FOLLOW_DIET] as Boolean
                val readTenPages = document[DailyTasksCollection.FIELD_READ_TEN_PAGES] as Boolean
                val takeProgressPicture = document[DailyTasksCollection.FIELD_TAKE_PROGRESS_PICTURE] as String

                // Return a list of DailyTasks
                listOf(
                    DailyTasks(
                        date = date,
                        userId = userId!!,
                        gallonOfWater = gallonOfWater,
                        twoWorkouts = twoWorkouts,
                        followDiet = followDiet,
                        readTenPages = readTenPages,
                        takeProgressPicture = takeProgressPicture
                    )
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

}
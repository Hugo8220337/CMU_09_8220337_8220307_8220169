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

            val taskData = mapOf(
                DailyTasksCollection.FIELD_DATE to LocalDate.now().toString(),
                DailyTasksCollection.FIELD_USER_ID to userId,
                DailyTasksCollection.FIELD_GALLON_OF_WATER to tasks.gallonOfWater,
                DailyTasksCollection.FIELD_TWO_WORKOUTS to tasks.twoWorkouts,
                DailyTasksCollection.FIELD_FOLLOW_DIET to tasks.followDiet,
                DailyTasksCollection.FIELD_READ_TEN_PAGES to tasks.readTenPages,
                DailyTasksCollection.FIELD_TAKE_PROGRESS_PICTURE to tasks.takeProgressPicture
            )

            firestore.collection(CollectionsNames.dailyTasksCollection)
                .document(LocalDate.now().toString()) // Convert LocalDate to String to use as document ID
                .set(taskData)
                .await()

        } catch (e: Exception) {
            Log.d("DailyTasksFirestoreRepository", "Erro ao inserir daily task no Firebase", e)
        }
    }

    //gel All daily tasks from Firebase
    suspend fun getAllDailyTasksFromFirebase(): List<DailyTasks>? {
        return try {
            val result = firestore.collection(CollectionsNames.dailyTasksCollection)
                .get()
                .await()

            // Convert the result into a list of Workout objects
            result.documents.mapNotNull { document ->
                val gallonOfWater = document.get(DailyTasksCollection.FIELD_GALLON_OF_WATER) as Boolean
                val twoWorkouts = document.get(DailyTasksCollection.FIELD_TWO_WORKOUTS) as Boolean
                val followDiet = document.get(DailyTasksCollection.FIELD_FOLLOW_DIET) as Boolean
                val readTenPages = document.get(DailyTasksCollection.FIELD_READ_TEN_PAGES) as Boolean
                val takeProgressPicture = document.get(DailyTasksCollection.FIELD_TAKE_PROGRESS_PICTURE) as String

                DailyTasks(
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


    // Get daily tasks from Firebase
    suspend fun getDailyTasksFromFirebase(): List<Boolean>? {
        return try {
            val result = firestore.collection(CollectionsNames.dailyTasksCollection)
                .document(LocalDate.now().toString())
                .get()
                .await()

            if (result != null && result.exists()) {
                val document = result.data
                val gallonOfWater = document?.get(DailyTasksCollection.FIELD_GALLON_OF_WATER) as Boolean
                val twoWorkouts = document?.get(DailyTasksCollection.FIELD_TWO_WORKOUTS) as Boolean
                val followDiet = document?.get(DailyTasksCollection.FIELD_FOLLOW_DIET) as Boolean
                val readTenPages = document?.get(DailyTasksCollection.FIELD_READ_TEN_PAGES) as Boolean
                val takeProgressPicture = document?.get(DailyTasksCollection.FIELD_TAKE_PROGRESS_PICTURE) as String
                listOf(gallonOfWater, twoWorkouts, followDiet, readTenPages)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // Get all daily tasks by user Id from Firebase
    suspend fun getAllDailyTasksFromFirebaseByUser(): List<Boolean>? {
        return try {
            val userId = authFirebaseRepository.getCurrentUser()?.uid

            val result = firestore.collection(CollectionsNames.dailyTasksCollection)
                //.whereEqualTo(WorkoutCollection.FIELD_USER_ID, userId)
                .document(userId.toString())
                .get()
                .await()

            if (result != null && result.exists()) {
                val document = result.data
                val gallonOfWater = document?.get(DailyTasksCollection.FIELD_GALLON_OF_WATER) as Boolean
                val twoWorkouts = document?.get(DailyTasksCollection.FIELD_TWO_WORKOUTS) as Boolean
                val followDiet = document?.get(DailyTasksCollection.FIELD_FOLLOW_DIET) as Boolean
                val readTenPages = document?.get(DailyTasksCollection.FIELD_READ_TEN_PAGES) as Boolean
                val takeProgressPicture = document?.get(DailyTasksCollection.FIELD_TAKE_PROGRESS_PICTURE) as String
                listOf(gallonOfWater, twoWorkouts, followDiet, readTenPages)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // Get daily tasks by date from Firebase
    suspend fun getDailyTasksByDateFromFirebase(date: String): List<Boolean>? {
        return try {
            val result = firestore.collection(CollectionsNames.dailyTasksCollection)
                .document(date)
                .get()
                .await()

            if (result != null && result.exists()) {
                val document = result.data
                val gallonOfWater = document?.get(DailyTasksCollection.FIELD_GALLON_OF_WATER) as Boolean
                val twoWorkouts = document?.get(DailyTasksCollection.FIELD_TWO_WORKOUTS) as Boolean
                val followDiet = document?.get(DailyTasksCollection.FIELD_FOLLOW_DIET) as Boolean
                val readTenPages = document?.get(DailyTasksCollection.FIELD_READ_TEN_PAGES) as Boolean
                val takeProgressPicture = document?.get(DailyTasksCollection.FIELD_TAKE_PROGRESS_PICTURE) as String
                listOf(gallonOfWater, twoWorkouts, followDiet, readTenPages)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

}
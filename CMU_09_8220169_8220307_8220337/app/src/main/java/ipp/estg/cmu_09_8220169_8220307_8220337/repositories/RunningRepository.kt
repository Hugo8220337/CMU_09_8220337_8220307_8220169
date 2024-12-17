package ipp.estg.cmu_09_8220169_8220307_8220337.repositories

import android.util.Log
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.AuthFirebaseRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.repositories.RunningFirestoreRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.dao.RunningDao
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.Running

class RunningRepository(
    private val runningDao: RunningDao,
) {
    private val authFirebaseRepository: AuthFirebaseRepository = AuthFirebaseRepository()
    private val runningFirestoreRepository: RunningFirestoreRepository =
        RunningFirestoreRepository()

    suspend fun insertRunningWorkout(
        distance: Double,
        duration: Long,
        steps: Int
    ) {
        try {
            val calories =
                (distance * 0.05 + 0.02 * steps).toInt() // 0.05 calorias por metro + 1 caloria por passo

            // Inserir treino de corrida no Room
            val running = Running(
                distance = distance,
                duration = duration.toString(),
                steps = steps,
                calories = calories // 0.05 calorias por metro + 1 caloria por passo
            )
            // Inserir na base de dados local
            runningDao.insertRunning(running)

            // Inserir na base de dados remota (Firebase)
            runningFirestoreRepository.insertRunningInFirebase(running)
        } catch (e: Exception) {
            Log.e("RunningRepository", "Error inserting running workout", e)
        }
    }

    suspend fun getAllRunningWorkouts(): List<Running> {
        try {
            // Sincronizar treinos de corrida da base de dados remota
            syncRunningWorkoutsFromFirebase()

            // Obter treinos de corrida da base de dados local
            return runningDao.getRunnings()

        } catch (e: Exception) {
            Log.e("RunningRepository", "Error getting all running workouts", e)
        }
        return emptyList()
    }

    suspend fun getLastRun(userId: String): Running? {
        try {
            // Sincronizar treinos de corrida da base de dados remota
            syncRunningWorkoutsFromFirebase()

            //Obter último treino de corrida da base de dados local
            return runningDao.getLastRun(userId)
        } catch (e: Exception) {
            Log.e("RunningRepository", "Error getting last run", e)
        }

        return null
    }

    suspend fun getRunningWorkoutsByUserId(userId: String): Running? {
        try {
            // Sincronizar treinos de corrida da base de dados remota
            syncRunningWorkoutsFromFirebase()

            return runningDao.getRunningById(userId)
        } catch (e: Exception) {
            Log.e("RunningRepository", "Error getting running workouts by user id", e)
        }
        return null
    }

    private suspend fun syncRunningWorkoutsFromFirebase() {
        try {
            // Obter treinos de corrida da base de dados remota
            val firebaseRunningWorkouts = runningFirestoreRepository.getAllUserRunningFromFirebase()

            if (firebaseRunningWorkouts.isEmpty()) {
                return
            }

            // Guardar cada treino de corrida da base de dados remota na base de dados local
            runningDao.insertRunnings(firebaseRunningWorkouts)
        } catch (e: Exception) {
            Log.e("RunningRepository", "Error syncing running workouts from Firebase", e)
        }
    }

    suspend fun getRunningByUserID(userId: String): List<Running> {
        try {
            syncRunningWorkoutsFromFirebase()
            val runnings = runningDao.getRunningsByUserId(userId)
            return runnings
        } catch (e: Exception) {
            Log.e("RunningRepository", "Error getting running workouts by user id", e)
            return emptyList()
        }
    }
}
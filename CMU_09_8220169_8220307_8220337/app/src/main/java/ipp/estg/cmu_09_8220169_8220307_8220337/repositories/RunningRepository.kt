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
    private val runningFirestoreRepository: RunningFirestoreRepository = RunningFirestoreRepository()

    suspend fun insertRunningWorkout(
        distance: Double,
        duration: Long,
        steps: Int
    ) {
        try {
            //Inserir treino de corrida no Room
            val running = Running(
                distance = distance,
                duration = duration.toString(),
                steps = steps,
                calories = distance * 0.05 + 0.02 * steps // 0.05 calorias por metro + 1 caloria por passo
            )
            //Inserir na base de dados local
            runningDao.insertRunning(running)

            //inserir na base de dados remota
            runningFirestoreRepository.insertRunningInFirebase(running)
        } catch (e: Exception) {
            Log.e("RunningRepository", "Error inserting running workout", e)
        }
    }

    suspend fun getAllRunningWorkouts() {
        try {
            //Obter treinos de corrida da base de dados local
            runningDao.getRunnings()

            //Sincronizar treinos de corrida da base de dados remota
            syncRunningWorkoutsFromFirebase()
        } catch (e: Exception) {
            Log.e("RunningRepository", "Error getting all running workouts", e)
        }

    }

    suspend fun getRunningWorkoutsByUserId() {
        try {
            val userId = authFirebaseRepository.getCurrentUser()?.uid

            //Obter treinos de corrida da base de dados local
            if (userId != null) {
                runningDao.getRunningById(userId)
            }
        } catch (e: Exception) {
            Log.e("RunningRepository", "Error getting running workouts by user id", e)
        }
    }

    suspend fun deleteRunningWorkout(running: Running) {
        try {
            //Eliminar treino de corrida da base de dados local
            runningDao.deleteRunningById(running.id.toString())
        } catch (e: Exception) {
            Log.e("RunningRepository", "Error deleting running workout", e)
        }
    }

    private suspend fun syncRunningWorkoutsFromFirebase() {
        try {
            //Obter treinos de corrida da base de dados remota
            val firebaseRunningWorkouts = runningFirestoreRepository.getAllRunningFromFirebase()

            //Guardar cada treino de corrida da base de dados remota na base de dados local
            for (firebaseRunningWorkout in firebaseRunningWorkouts) {
                val localRunningWorkout = runningDao.getRunningById(firebaseRunningWorkout.id.toString())
                if (localRunningWorkout == null) {
                    runningDao.insertRunning(firebaseRunningWorkout)
                }
            }
        } catch (e: Exception) {
            Log.e("RunningRepository", "Error syncing running workouts from Firebase", e)
        }
    }
}
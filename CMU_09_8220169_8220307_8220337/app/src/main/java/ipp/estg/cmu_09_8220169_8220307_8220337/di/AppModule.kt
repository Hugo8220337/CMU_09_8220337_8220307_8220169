package ipp.estg.cmu_09_8220169_8220307_8220337.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.LOCAL_DB_NAME
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.SETTINGS_PREFERENCES_FILE
import ipp.estg.cmu_09_8220169_8220307_8220337.room.LocalDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.ExerciseDbApi
import ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.QuotesApi
import ipp.estg.cmu_09_8220169_8220307_8220337.room.dao.DailyTaskCompletionDao
import ipp.estg.cmu_09_8220169_8220307_8220337.room.dao.WorkoutDao
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.DAILY_TASKS_PREFERENCES_FILE
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.EXERCICE_DB_API_BASE_URL
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.QUOTES_API_BASE_URL
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.USER_PREFERENCES_FILE
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppModule {
    val settingsPreferences: SharedPreferences
    val userPreferences: SharedPreferences
    val dailyTasksPreferences: SharedPreferences

    // Retrofit API
    val exerciseDbApi: ExerciseDbApi
    val quotesApi: QuotesApi

    // Room DAOs
    val workoutDao: WorkoutDao
    val dailyTasksCompletionDao: DailyTaskCompletionDao
}

/**
 * AppModule contém as dependências que queremos poder injetar nas classes que se vai criar
 *
 * por ter o lazy, os objetos vão ser criados na primeira vez que forem acessados
 */
class AppModuleImpl(
    private val appContext: Context
) : AppModule {

    // útil para fazer chamadas HTTP
    private val client = OkHttpClient.Builder()
        .protocols(listOf(Protocol.HTTP_1_1))
        .build()

    // Instanciar a base de dados local no Room
    private val localDatabase by lazy {
        Room.databaseBuilder(
            appContext,
            LocalDatabase::class.java, LOCAL_DB_NAME
        ).fallbackToDestructiveMigration() // Add this to handle version changes during development
            .build()
    }

    override val settingsPreferences: SharedPreferences by lazy {
        appContext.getSharedPreferences(SETTINGS_PREFERENCES_FILE, Context.MODE_PRIVATE)
    }

    override val userPreferences: SharedPreferences by lazy {
        appContext.getSharedPreferences(USER_PREFERENCES_FILE, Context.MODE_PRIVATE)
    }

    override val dailyTasksPreferences: SharedPreferences by lazy {
        appContext.getSharedPreferences(DAILY_TASKS_PREFERENCES_FILE, Context.MODE_PRIVATE)
    }

    override val exerciseDbApi: ExerciseDbApi by lazy {
        Retrofit.Builder()
            .baseUrl(EXERCICE_DB_API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExerciseDbApi::class.java)
    }

    override val quotesApi: QuotesApi by lazy {
        Retrofit.Builder()
            .baseUrl(QUOTES_API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuotesApi::class.java)
    }



    override val workoutDao = localDatabase.workoutDao

    override val dailyTasksCompletionDao = localDatabase.dailyTaskCompletionDao

}
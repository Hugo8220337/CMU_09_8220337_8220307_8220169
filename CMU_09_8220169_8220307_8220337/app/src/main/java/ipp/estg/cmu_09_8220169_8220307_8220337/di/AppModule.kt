package ipp.estg.cmu_09_8220169_8220307_8220337.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.LOCAL_DB_NAME
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.SETTINGS_PREFERENCES_FILE
import ipp.estg.cmu_09_8220169_8220307_8220337.room.LocalDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.preferences.SettingsPreferencesRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.preferences.UserPreferencesRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.ExerciseDbApi
import ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.QuotesApi
import ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.repositories.ExerciseDbApiRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.retrofit.repositories.QuotesApiRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.EXERCICE_DB_API_BASE_URL
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.QUOTES_API_BASE_URL
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.USER_PREFERENCES_FILE
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppModule {
    val settingsPreferencesRepository: SettingsPreferencesRepository
    val exerciseDbApiRepository: ExerciseDbApiRepository
    val quotesApiRepository: QuotesApiRepository
    val userPreferencesRepository: UserPreferencesRepository

//    val darkModeEnabled: StateFlow<Boolean>
//    fun setDarkMode(enabled: Boolean)
}

/**
 * AppModule contém as dependências que queremos poder injetar nas classes que se vai criar
 *
 * por ter o lazy, os objetos vão ser criados na primeira vez que forem acessados
 */
class AppModuleImpl(
    private val appContext: Context
) : AppModule {

    private val settingsPreferences: SharedPreferences by lazy {
        appContext.getSharedPreferences(SETTINGS_PREFERENCES_FILE, Context.MODE_PRIVATE)
    }

    private val userPreferences: SharedPreferences by lazy {
        appContext.getSharedPreferences(USER_PREFERENCES_FILE, Context.MODE_PRIVATE)
    }

    private val exerciseDbApi: ExerciseDbApi by lazy {
        Retrofit.Builder()
            .baseUrl(EXERCICE_DB_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExerciseDbApi::class.java)
    }

    private val quotesApi: QuotesApi by lazy {
        Retrofit.Builder()
            .baseUrl(QUOTES_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuotesApi::class.java)
    }

    private val localDatabase by lazy {
        Room.databaseBuilder(
            appContext,
            LocalDatabase::class.java, LOCAL_DB_NAME
        ).fallbackToDestructiveMigration() // Add this to handle version changes during development
            .build()
    }



    override val settingsPreferencesRepository by lazy {
        SettingsPreferencesRepository(settingsPreferences)
    }

    override val userPreferencesRepository by lazy {
        UserPreferencesRepository(userPreferences)
    }


    override val exerciseDbApiRepository by lazy {
        ExerciseDbApiRepository(exerciseDbApi)
    }

    override val quotesApiRepository by lazy {
        QuotesApiRepository(quotesApi)
    }



//    private val _darkModeEnabled =
//        MutableStateFlow(settingsPreferences.getBoolean("darkMode", false))

//    override val darkModeEnabled: StateFlow<Boolean> get() = _darkModeEnabled
//
//    override fun setDarkMode(enabled: Boolean) {
//        settingsPreferences.edit().putBoolean("darkMode", enabled).apply()
//        _darkModeEnabled.value = enabled // Update the StateFlow
//    }

}
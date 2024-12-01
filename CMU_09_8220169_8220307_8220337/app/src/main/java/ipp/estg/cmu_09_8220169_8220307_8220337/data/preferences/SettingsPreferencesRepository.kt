package ipp.estg.cmu_09_8220169_8220307_8220337.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.LocaleList
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.SETTINGS_PREFERENCES_FILE
import java.util.Locale

class SettingsPreferencesRepository(
    context: Context
) {
    private val NOTIFICATIONS_PREFERENCE = "notifications"
    private val DARK_MODE_PREFERENCE = "darkMode"
    private val LANGUAGE_PREFERENCE = "language"

    private val settingsPreferences: SharedPreferences =
        context.getSharedPreferences(SETTINGS_PREFERENCES_FILE, Context.MODE_PRIVATE)


    fun getDarkModePreference(): Boolean {
        return settingsPreferences.getBoolean(DARK_MODE_PREFERENCE, false)
    }

    fun setDarkModePreference(enabled: Boolean) {
        settingsPreferences.edit().putBoolean(DARK_MODE_PREFERENCE, enabled).apply()

    }

    fun getNotificationsPreference(): Boolean {
        return settingsPreferences.getBoolean(NOTIFICATIONS_PREFERENCE, true)
    }

    fun setNotificationsPreference(enabled: Boolean) {
        settingsPreferences.edit().putBoolean(NOTIFICATIONS_PREFERENCE, enabled).apply()
    }

    fun getLanguagePreference(): String {
        return settingsPreferences.getString(LANGUAGE_PREFERENCE, "pt-pt")!!
    }

    fun setLanguagePreference(language: String) {
        settingsPreferences.edit().putString(LANGUAGE_PREFERENCE, language).apply()
    }

    fun updateLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLocales(LocaleList(locale))

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}
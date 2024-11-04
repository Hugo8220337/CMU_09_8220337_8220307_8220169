package ipp.estg.cmu_09_8220169_8220307_8220337.preferences

import android.content.SharedPreferences
import ipp.estg.cmu_09_8220169_8220307_8220337.Hard75Application

class SettingsPreferencesRepository(
    private val settingsPreferences: SharedPreferences
) {
    private val NOTIFICATIONS_PREFERENCE = "notifications"
    private val DARK_MODE_PREFERENCE = "darkMode"
    private val LANGUAGE_PREFERENCE = "language"


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
}
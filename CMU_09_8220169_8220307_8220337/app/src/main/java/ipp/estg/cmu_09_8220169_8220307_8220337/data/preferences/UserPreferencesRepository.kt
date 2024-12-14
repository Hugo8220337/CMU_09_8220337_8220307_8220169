package ipp.estg.cmu_09_8220169_8220307_8220337.data.preferences

import android.content.Context
import android.content.SharedPreferences
import ipp.estg.cmu_09_8220169_8220307_8220337.utils.Constants.USER_PREFERENCES_FILE

class UserPreferencesRepository(
    context: Context
) {
    private val USERNAME_PREFERENCE = "username"
    private val FIRST_NAME_PREFERENCE = "firstName"
    private val LAST_NAME_PREFERENCE = "lastName"
    private val EMAIL_PREFERENCE = "email"
    private val AGE_PREFERENCE = "age"
    private val WEIGHT_PREFERENCE = "weight"
    private val currentUserId = "userId"


    private val userPreferences: SharedPreferences = context.getSharedPreferences(
        USER_PREFERENCES_FILE, Context.MODE_PRIVATE)

    // Username
    fun getUsername(): String {
        return userPreferences.getString(USERNAME_PREFERENCE, "")
            ?: "" // If null, return empty string
    }

    fun setUsername(username: String) {
        userPreferences.edit().putString(USERNAME_PREFERENCE, username).apply()
    }

    // First Name
    fun getFirstName(): String {
        return userPreferences.getString(FIRST_NAME_PREFERENCE, "")
            ?: "" // If null, return empty string
    }

    fun setFirstName(firstName: String) {
        userPreferences.edit().putString(FIRST_NAME_PREFERENCE, firstName).apply()
    }

    // Last Name
    fun getLastName(): String {
        return userPreferences.getString(LAST_NAME_PREFERENCE, "") ?: ""
    }

    fun setLastName(lastName: String) {
        userPreferences.edit().putString(LAST_NAME_PREFERENCE, lastName).apply()
    }

    // Email
    fun getEmail(): String {
        return userPreferences.getString(EMAIL_PREFERENCE, "") ?: ""
    }

    fun setEmail(email: String) {
        userPreferences.edit().putString(EMAIL_PREFERENCE, email).apply()
    }

    // Age
    fun getAge(): Int {
        return userPreferences.getInt(AGE_PREFERENCE, 0)
    }

    fun setAge(age: Int) {
        userPreferences.edit().putInt(AGE_PREFERENCE, age).apply()
    }

    // Weight
    fun getWeight(): Float {
        return userPreferences.getFloat(WEIGHT_PREFERENCE, 0f)
    }

    fun setWeight(weight: Float) {
        userPreferences.edit().putFloat(WEIGHT_PREFERENCE, weight).apply()
    }

    fun getCurrentUserId(): String {
        return userPreferences.getString(currentUserId, "") ?: ""
    }

    fun setCurrentUserId(userId: String) {
        userPreferences.edit().putString(currentUserId, userId).apply()
    }

    // Clear all user data
    fun clearUserData() {
        userPreferences.edit().apply {
            remove(USERNAME_PREFERENCE)
            remove(FIRST_NAME_PREFERENCE)
            remove(LAST_NAME_PREFERENCE)
            remove(EMAIL_PREFERENCE)
            remove(AGE_PREFERENCE)
            remove(WEIGHT_PREFERENCE)
            apply()
        }
    }

    // Check if user profile is completed
    fun isProfileCompleted(): Boolean {
        return getUsername().isNotEmpty() &&
                getFirstName().isNotEmpty() &&
                getLastName().isNotEmpty() &&
                getEmail().isNotEmpty() &&
                getAge() > 0 &&
                getWeight() > 0
    }
}
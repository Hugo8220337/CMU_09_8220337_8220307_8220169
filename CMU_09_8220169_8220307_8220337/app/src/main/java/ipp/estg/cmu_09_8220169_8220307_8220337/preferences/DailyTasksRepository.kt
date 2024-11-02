package ipp.estg.cmu_09_8220169_8220307_8220337.preferences

import android.content.SharedPreferences

class DailyTasksRepository(
    private val dailyTasksPreferences: SharedPreferences
) {
    private val GALLON_OF_WATER = "gallonOfWater"
    private val TWO_WORKOUTS = "twoWorkouts"
    private val FOLLOW_DIET = "followDiet"
    private val READ_TEN_Pages = "readTenPages"
    private val TAKE_PROGRESS_PICTURE = "takeProgressPicture"

    fun getGallonOfWater(): Boolean {
        return dailyTasksPreferences.getBoolean(GALLON_OF_WATER, false)
    }

    fun setGallonOfWater(enabled: Boolean) {
        dailyTasksPreferences.edit().putBoolean(GALLON_OF_WATER, enabled).apply()
    }

    fun getTwoWorkouts(): Boolean {
        return dailyTasksPreferences.getBoolean(TWO_WORKOUTS, false)
    }

    fun setTwoWorkouts(enabled: Boolean) {
        dailyTasksPreferences.edit().putBoolean(TWO_WORKOUTS, enabled).apply()
    }

    fun getFollowDiet(): Boolean {
        return dailyTasksPreferences.getBoolean(FOLLOW_DIET, false)
    }

    fun setFollowDiet(enabled: Boolean) {
        dailyTasksPreferences.edit().putBoolean(FOLLOW_DIET, enabled).apply()
    }

    fun getReadTenPages(): Boolean {
        return dailyTasksPreferences.getBoolean(READ_TEN_Pages, false)
    }

    fun setReadTenPages(enabled: Boolean) {
        dailyTasksPreferences.edit().putBoolean(READ_TEN_Pages, enabled).apply()
    }

    fun getTakeProgressPicture(): Boolean {
        return dailyTasksPreferences.getBoolean(TAKE_PROGRESS_PICTURE, false)
    }

    fun setTakeProgressPicture(enabled: Boolean) {
        dailyTasksPreferences.edit().putBoolean(TAKE_PROGRESS_PICTURE, enabled).apply()
    }
}
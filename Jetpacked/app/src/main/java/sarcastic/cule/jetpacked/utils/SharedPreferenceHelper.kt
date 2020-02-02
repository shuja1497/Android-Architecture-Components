package sarcastic.cule.jetpacked.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class SharedPreferenceHelper {

    companion object {

        private val PREF_TIME = "Prefs time"
        private var prefs: SharedPreferences? = null

        @Volatile private var instance: SharedPreferenceHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildSharedPreference(context).also {
                instance = it
            }
        }

        private fun buildSharedPreference(context: Context) : SharedPreferenceHelper{
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferenceHelper()
        }
    }

    fun saveUpdateTime(time: Long) {
        prefs?.edit(commit = true) {
            putLong(PREF_TIME, time)
        }
    }

    fun getUpdateTime() = prefs?.getLong(PREF_TIME, 0)

}
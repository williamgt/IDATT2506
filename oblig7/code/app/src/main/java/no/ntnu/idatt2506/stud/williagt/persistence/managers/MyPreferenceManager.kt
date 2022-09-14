package no.ntnu.idatt2506.stud.williagt.persistence.managers

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import no.ntnu.idatt2506.stud.williagt.persistence.R

class MyPreferenceManager(private val activity: AppCompatActivity) {

    private val resources = activity.resources
    private val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
    private val editor: SharedPreferences.Editor = preferences.edit()

    fun putString(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return preferences.getString(key, defaultValue) ?: defaultValue
    }

    fun updateBgColor() {
        val key = resources.getString(R.string.bg)
        val bgValues = resources.getStringArray(R.array.bg_color_values)
        val value = getString(key, resources.getString(R.string.bg_default_value))


        when(value) {
            bgValues[0] -> {
                putString(key, "WHITE")
            }
            bgValues[1] -> {
                putString(key, "RED")
            }
            bgValues[2] -> {
                putString(key, "GREEN")
            }
            bgValues[3] -> {
                putString(key, "BLUE")
            }
        }
    }

    fun registerListener(activity: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(activity)
    }

    fun unregisterListener(activity: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.unregisterOnSharedPreferenceChangeListener(activity)
    }
}

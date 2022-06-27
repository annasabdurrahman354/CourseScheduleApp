package com.dicoding.courseschedule.ui.setting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.notification.DailyReminder
import com.dicoding.courseschedule.util.NightMode
import com.dicoding.courseschedule.util.executeThread
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var dailyReminder: DailyReminder

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        dailyReminder = DailyReminder()

        val nightModePref: Preference? = findPreference(getString(R.string.pref_key_dark))
        nightModePref?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                val mode = NightMode.valueOf(newValue.toString().toUpperCase(Locale.US))
                AppCompatDelegate.setDefaultNightMode(mode.value)
                true
            }

        val notifyPref: Preference? = findPreference(getString(R.string.pref_key_notify))
        notifyPref?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                if(newValue == true){
                    executeThread {
                        val repository = DataRepository.getInstance(requireActivity())
                        val courses = repository?.getTodaySchedule()

                        courses?.let {
                            Log.e("List", courses.toString())
                        }
                    }
                    dailyReminder.setDailyReminder(requireActivity())
                }
                else if(newValue == false){
                    dailyReminder.cancelAlarm(requireActivity())
                }
                true
            }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}
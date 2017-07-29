package com.gmail.jiangyang5157.sudoku.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import com.gmail.jiangyang5157.sudoku.R

/**
 * Created by Yang Jiang on July 16, 2017
 */
class SettingsFragment : BasePreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.preference_settings)

        val pVersion = findPreference(getString(R.string.label_version))
        pVersion.title = activity.packageManager.getPackageInfo(activity.packageName, 0).versionName
    }

    override fun getSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(activity)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.d(TAG, "onSharedPreferenceChanged key=$key")
    }

    companion object {
        val TAG = "SettingsFragment"
    }
}
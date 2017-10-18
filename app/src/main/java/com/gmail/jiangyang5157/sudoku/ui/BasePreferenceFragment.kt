package com.gmail.jiangyang5157.sudoku.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceFragment

/**
 * Created by Yang Jiang on July 16, 2017
 */
abstract class BasePreferenceFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    abstract fun getSharedPreferences(): SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        getSharedPreferences().registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this)
    }
}
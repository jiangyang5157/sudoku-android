package com.gmail.jiangyang5157.sudoku.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.gmail.jiangyang5157.kotlin_android_core.utils.instance
import com.gmail.jiangyang5157.sudoku.R
import android.content.Intent
import android.util.Log

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.container_activity)

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.container, instance<MainFragment>(), MainFragment.TAG)
                    .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.menu_storage -> {
                Log.d(TAG, "onOptionsItemSelected R.id.menu_storage")
                return true
            }
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        val TAG = "MainActivity"
    }
}

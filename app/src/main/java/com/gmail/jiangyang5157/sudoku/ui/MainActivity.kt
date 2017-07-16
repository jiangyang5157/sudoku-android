package com.gmail.jiangyang5157.sudoku.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.gmail.jiangyang5157.kotlin_android_core.utils.instance
import com.gmail.jiangyang5157.sudoku.R

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_shared)

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.container, instance<MainFragment>())
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
            R.id.menu_settings -> {
                Log.d("####", "R.id.menu_settings")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}

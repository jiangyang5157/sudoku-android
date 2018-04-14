package com.gmail.jiangyang5157.sudoku.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gmail.jiangyang5157.kotlin_android_core.utils.instance
import com.gmail.jiangyang5157.sudoku.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_activity)

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.activity_container, instance<MainFragment>(), MainFragment.TAG)
                    .commit()
        }
    }

}

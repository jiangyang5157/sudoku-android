package com.gmail.jiangyang5157.sudoku.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gmail.jiangyang5157.kotlin_android_kit.ext.instance
import com.gmail.jiangyang5157.kotlin_android_kit.ext.replaceFragmentInActivity
import com.gmail.jiangyang5157.sudoku.R

/**
 * Created by Yang Jiang on April 21, 2018
 */
class SudokuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_activity)

        if (savedInstanceState == null) {
            replaceFragmentInActivity(R.id.activity_container, instance<SudokuFragment>(intent.extras))
        }
    }

}

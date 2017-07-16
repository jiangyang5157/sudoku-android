package com.gmail.jiangyang5157.sudoku.ui

import android.os.Bundle
import com.gmail.jiangyang5157.kotlin_android_core.utils.instance
import com.gmail.jiangyang5157.sudoku.R

/**
 * Created by Yang Jiang on July 16, 2017
 */
class PuzzleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.container_activity)

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.container, instance<PuzzleFragment>(), PuzzleFragment.TAG)
                    .commit()
        }
    }
}
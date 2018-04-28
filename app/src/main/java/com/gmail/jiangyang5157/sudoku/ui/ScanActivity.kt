package com.gmail.jiangyang5157.sudoku.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.gmail.jiangyang5157.kotlin_android_kit.ext.instance
import com.gmail.jiangyang5157.kotlin_android_kit.ext.replaceFragmentInActivity
import com.gmail.jiangyang5157.sudoku.R
import org.opencv.android.OpenCVLoader

/**
 * Created by Yang Jiang on April 21, 2018
 */
class ScanActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ScanActivity"

        init {
            if (OpenCVLoader.initDebug()) {
                Log.d(TAG, "Initialization of OpenCV was successful")
            } else {
                Log.d(TAG, "Initialization of OpenCV was failed")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_activity)

        if (savedInstanceState == null) {
            replaceFragmentInActivity(R.id.activity_container, instance<ScanFragment>())
        }
    }

}

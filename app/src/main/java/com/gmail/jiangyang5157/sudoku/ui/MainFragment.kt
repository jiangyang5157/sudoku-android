package com.gmail.jiangyang5157.sudoku.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.jiangyang5157.sudoku.R
import com.gmail.jiangyang5157.sudoku_scan.ui.FrameCameraActivity

/**
 * Created by Yang Jiang on April 21, 2018
 */
class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view?.apply {
            findViewById(R.id.btn_sudoku)?.setOnClickListener {
                val edge = (findViewById(R.id.et_edge) as TextInputEditText).text.trim().toString()
                        .toInt()
                val subGiven = (findViewById(R.id.et_min_sub_given) as TextInputEditText).text.trim().toString()
                        .toInt()
                val totalGiven = (findViewById(R.id.et_min_total_given) as TextInputEditText).text.trim().toString()
                        .toInt()
                activity.startActivity(Intent(context, SudokuActivity::class.java).apply {
                    putExtra(SudokuFragment.KEY_EDGE, edge)
                    putExtra(SudokuFragment.KEY_MIN_SUB_GIVEN, subGiven)
                    putExtra(SudokuFragment.KEY_MIN_TOTAL_GIVEN, totalGiven)
                })
            }
            findViewById(R.id.btn_scan)?.setOnClickListener {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), 1)
                } else {
                    val desiredPreviewWidth = (findViewById(R.id.et_desired_preview_width) as TextInputEditText).text.trim().toString()
                            .toInt()
                    val desiredPreviewHeight = (findViewById(R.id.et_desired_preview_height) as TextInputEditText).text.trim().toString()
                            .toInt()
                    activity.startActivity(Intent(context, FrameCameraActivity::class.java).apply {
                        putExtra(FrameCameraActivity.KEY_CAMERA_DESIRED_WIDTH, desiredPreviewWidth)
                        putExtra(FrameCameraActivity.KEY_CAMERA_DESIRED_HEIGHT, desiredPreviewHeight)
                    })
                }
            }
        }
    }

}
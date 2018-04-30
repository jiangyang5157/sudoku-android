package com.gmail.jiangyang5157.sudoku.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.jiangyang5157.sudoku.R
import com.gmail.jiangyang5157.sudoku.REQUEST_CAMERA_PERMISSION

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
                val subGiven = (findViewById(R.id.et_min_sub_given) as TextInputEditText).text.trim().toString()
                val totalGiven = (findViewById(R.id.et_min_total_given) as TextInputEditText).text.trim().toString()
                activity.startActivity(Intent(context, SudokuActivity::class.java).apply {
                    putExtra(SudokuFragment.KEY_EDGE, edge)
                    putExtra(SudokuFragment.KEY_MIN_SUB_GIVEN, subGiven)
                    putExtra(SudokuFragment.KEY_MIN_TOTAL_GIVEN, totalGiven)
                })
            }
            findViewById(R.id.btn_scan)?.setOnClickListener {
                val permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA),
                            REQUEST_CAMERA_PERMISSION)
                } else {
                    activity.startActivity(Intent(context, ScanActivity::class.java))
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    activity.startActivity(Intent(context, ScanActivity::class.java))
                }
            }
        }
    }

}
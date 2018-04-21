package com.gmail.jiangyang5157.sudoku.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.jiangyang5157.sudoku.R

/**
 * Created by Yang Jiang on April 21, 2018
 */
class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view?.findViewById(R.id.btn_sudoku)?.setOnClickListener {
            activity.startActivity(Intent(context, SudokuActivity::class.java))
        }
        view?.findViewById(R.id.btn_scan)?.setOnClickListener {
            activity.startActivity(Intent(context, ScanActivity::class.java))
        }
    }

}
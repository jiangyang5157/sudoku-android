package com.gmail.jiangyang5157.sudoku.ui

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.gmail.jiangyang5157.sudoku.R

/**
 * Created by Yang Jiang on July 16, 2017
 */
class MainFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val btn = view?.findViewById(R.id.btn) as Button
        btn.setOnClickListener {
            Log.d("####", "MainFragment.btn.setOnClickListener")
        }
    }

}
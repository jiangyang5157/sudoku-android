package com.gmail.jiangyang5157.sudoku.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.gmail.jiangyang5157.sudoku.R

/**
 * Created by Yang Jiang on April 21, 2018
 */
class ScanFragment : Fragment() {

    private lateinit var debugLinearLayout: LinearLayout
    private lateinit var debugScrollView: ScrollView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view?.apply {
            debugScrollView = findViewById(R.id.debug_scrollview) as ScrollView
            debugLinearLayout = findViewById(R.id.debug_linearlayout) as LinearLayout
            findViewById(R.id.debug_btn_next).setOnClickListener(debugBtnNextOnClicklistener)
        }
    }

    private val debugBtnNextOnClicklistener = View.OnClickListener {
        debugLinearLayout.addView(TextView(context).apply {
            text = "testing"
        })

        debugScrollView.fullScroll(View.FOCUS_DOWN)
    }

}
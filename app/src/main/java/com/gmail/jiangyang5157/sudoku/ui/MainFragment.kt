package com.gmail.jiangyang5157.sudoku.ui

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.jiangyang5157.sudoku.R
import com.gmail.jiangyang5157.sudoku_presenter.SudokuContract
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal

/**
 * Created by Yang Jiang on July 16, 2017
 */
class MainFragment : Fragment(), SudokuContract.View {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPrePuzzleGeneration() {
        // todo
    }

    override fun onPostPuzzleGeneration(result: Terminal?) {
        // todo
    }

    override fun onPrePuzzleResolution() {
        // todo
    }

    override fun onPostPuzzleResolution(result: Terminal?) {
        // todo
    }

    companion object {
        const val TAG = "MainFragment"
    }

}
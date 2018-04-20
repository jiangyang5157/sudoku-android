package com.gmail.jiangyang5157.sudoku_presenter

import com.gmail.jiangyang5157.kotlin_kit.model.BasePresenter
import com.gmail.jiangyang5157.kotlin_kit.model.BaseView
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal

/**
 * Created by Yang Jiang on April 14, 2018
 */
interface SudokuContract {

    interface Presenter : BasePresenter {

        fun getPuzzle(edge: Int, minSubGiven: Int, minTotalGiven: Int)

        fun getTerminal()

        fun updateProgress(index: Int, d: Int)

        fun resolveProgress()

        fun updatePossibility(index: Int, d: Int)

        fun selectCell(index: Int)

    }

    interface View : BaseView<Presenter> {

        fun showPuzzle(t: Terminal)

        fun showTerminal(t: Terminal)

        fun showUpdatedProgress(index: Int, d: Int)

        fun showResolvedProgress(t: Terminal)

        fun showUpdatedPossibility(index: Int, possibility: IntArray)

        fun showSelectedCell(index: Int, relevant: IntArray)

    }

}

package com.gmail.jiangyang5157.sudoku_presenter

import com.gmail.jiangyang5157.kotlin_kit.model.BasePresenter
import com.gmail.jiangyang5157.kotlin_kit.model.BaseView
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal

/**
 * Created by Yang Jiang on April 14, 2018
 */
interface SudokuContract {

    interface Presenter : BasePresenter {

        val mView: View

        fun generatePuzzle(edge: Int, minSubGiven: Int, minTotalGiven: Int)

        fun revealTerminal()

        fun updateProgress(index: Int, d: Int)

        fun resolveProgress()

        fun updatePossibility(index: Int, d: Int)

        fun clearPossibility(index: Int)

        fun selectCell(index: Int)

        fun enablePossibilityEnter()

        fun disablePossibilityEnter()

        fun invertPossibilityEnterStatus()

        fun enterClear()

        fun enterDigit(digit: Int)
    }

    interface View : BaseView<Presenter> {

        fun puzzleGenerated(t: Terminal?)

        fun terminalReveald(t: Terminal?)

        fun progressUpdated(index: Int, d: Int)

        fun progressResolved(t: Terminal?)

        fun possibilityUpdated(index: Int, possibility: IntArray)

        fun cellSelected(index: Int, relevant: IntArray)

        fun possibilityEnterEnabled()

        fun possibilityEnterDisabled()

        fun digitCleard()

        fun possibilityCleard()

        fun digitEnterd(digit: Int)

        fun possibilityEnterd(digit: Int)
    }

}

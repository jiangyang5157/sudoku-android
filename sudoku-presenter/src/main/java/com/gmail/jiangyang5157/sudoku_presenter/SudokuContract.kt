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

        fun updateProgress(index: Int, digit: Int)

        fun clearProgress()

        fun updatePossibility(index: Int, digit: Int)

        fun clearPossibility(index: Int)

        fun invertPossibilityEnterStatus()

        fun enterClear()

        fun enterDigit(digit: Int)
    }

    interface View : BaseView<Presenter> {

        fun puzzleGenerated(puzzle: Terminal?)

        fun terminalReveald(terminal: Terminal?)

        fun progressUpdated(index: Int, digit: Int)

        fun progressCleard(progress: Terminal)

        fun possibilityUpdated(index: Int, possibility: Array<Int?>)

        fun possibilityEnterEnabled()

        fun possibilityEnterDisabled()

        fun digitCleard()

        fun possibilityCleard()

        fun digitEnterd(digit: Int)

        fun possibilityEnterd(digit: Int)
    }

}

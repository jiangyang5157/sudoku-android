package com.gmail.jiangyang5157.sudoku_presenter

import com.gmail.jiangyang5157.kotlin_kit.model.BasePresenter
import com.gmail.jiangyang5157.kotlin_kit.model.BaseView
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal

/**
 * Created by Yang Jiang on April 14, 2018
 */
interface SudokuContract {

    interface Presenter : BasePresenter {

        fun generateSudoku(edge: Int, minSubGiven: Int, minTotalGiven: Int)

    }

    interface View : BaseView<Presenter> {

        fun showPuzzle(result: Terminal)

    }

}

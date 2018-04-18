package com.gmail.jiangyang5157.sudoku_presenter

import com.gmail.jiangyang5157.kotlin_core.model.BasePresenter
import com.gmail.jiangyang5157.kotlin_core.model.BaseView

/**
 * Created by Yang Jiang on April 14, 2018
 */
interface SudokuContract {

    interface Presenter : BasePresenter

    interface View : BaseView<Presenter>

}

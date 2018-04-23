package com.gmail.jiangyang5157.sudoku_presenter

import com.gmail.jiangyang5157.kotlin_kit.model.BasePresenter
import com.gmail.jiangyang5157.kotlin_kit.model.BaseView

/**
 * Created by Yang Jiang on April 23, 2018
 */
interface KeypadContract {

    interface Presenter : BasePresenter {

        val mView: View

        fun enterDigit(digit: Int)

        fun enterClear()

        fun switchPossibilityMode(enabled: Boolean)
    }

    interface View : BaseView<Presenter> {

        var mDelegate: Delegate?

        interface Delegate {

            fun digitEnterd(digit: Int)

            fun digitCleard()

            fun possibilityModeEnabled()

            fun possibilityModeDisabled()

            fun possibilityEnterd(digit: Int)

            fun possibilityCleard()
        }
    }

}
package com.gmail.jiangyang5157.sudoku.widget.terminal

import com.gmail.jiangyang5157.kotlin_kit.model.Mapper
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.TPossibilityTerminal
import com.gmail.jiangyang5157.sudoku_presenter.model.PossibilityTerminal

/**
 * Created by Yang Jiang on April 25, 2018
 */
data class TMapper(val width: Int, val height: Int) : Mapper<PossibilityTerminal, TPossibilityTerminal> {

    override fun map(from: PossibilityTerminal): TPossibilityTerminal {
        if (width <= 0 || height <= 0) {
            throw IllegalStateException()
        }

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
package com.gmail.jiangyang5157.sudoku_presenter.model

/**
 * Created by Yang Jiang on April 22, 2018
 */
data class Sudoku(

        /**
         * A non-editable [Terminal] has unique solution
         */
        val T: Terminal,

        /**
         * A editable [PossibilityTerminal]
         */
        val PT: PossibilityTerminal = PossibilityTerminal(T.copy())
)
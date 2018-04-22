package com.gmail.jiangyang5157.sudoku_presenter.model

/**
 * Created by Yang Jiang on April 22, 2018
 */
data class Sudoku(

        /**
         * A non-editable [Terminal] with unique solution
         */
        val puzzle: Terminal,

        /**
         * A editable [PossibilityTerminal]
         */
        val progress: PossibilityTerminal = PossibilityTerminal(puzzle.copy())
)
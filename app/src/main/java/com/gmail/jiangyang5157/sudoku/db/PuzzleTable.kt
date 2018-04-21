package com.gmail.jiangyang5157.sudoku.db

/**
 * Created by Yang Jiang on July 02, 2017
 */
class PuzzleTable {

    companion object {

        const val TABLE_NAME: String = "Puzzle"

        const val SQL_CREATE_TABLE: String = "create table " + TABLE_NAME +
                "(${Column.KEY_ID} integer primary key autoincrement, " +
                "${Column.KEY_TERMINAL_DATA} text, " +
                "${Column.KEY_LAST_MODIFIED_DATE} text);"
    }

    object Column {

        const val KEY_ID: String = "_id"

        const val KEY_TERMINAL_DATA: String = "terminal_data"

        const val KEY_LAST_MODIFIED_DATE: String = "last_modified_date"
    }

}

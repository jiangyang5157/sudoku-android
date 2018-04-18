package com.gmail.jiangyang5157.sudoku.db

/**
 * Created by Yang Jiang on July 02, 2017
 */
class PuzzleTable {

    companion object {
        const val TABLE_NAME: String = "Puzzle"

        const val SQL_TABLE_CREATION: String
                = "create table " +
                "$TABLE_NAME(${Column.KEY_ID} integer primary key autoincrement, " +
                "${Column.KEY_CACHE} text, " +
                "${Column.KEY_DRAWABLE} text, " +
                "${Column.KEY_DATE} text, " +
                "${Column.KEY_TIMER} text, " +
                "${Column.KEY_BEST_TIME} text);"
    }

    object Column {
        const val KEY_ID: String = "_id"

        const val KEY_CACHE: String = "cache"
        const val KEY_DRAWABLE: String = "drawable"
        const val KEY_DATE: String = "date"
        const val KEY_TIMER: String = "timer"
        const val KEY_BEST_TIME: String = "best_time"
    }

}

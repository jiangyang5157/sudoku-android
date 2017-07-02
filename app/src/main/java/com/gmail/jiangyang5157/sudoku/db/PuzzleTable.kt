package com.gmail.jiangyang5157.sudoku.db

/**
 * Created by Yang Jiang on July 02, 2017
 */
class PuzzleTable {

    companion object {
        val TABLE_NAME: String = "Puzzle"

        val SQL_TABLE_CREATION: String
                = "create table " +
                "$TABLE_NAME(${Column.KEY_ROWID} integer primary key autoincrement, " +
                "${Column.KEY_CACHE} text, " +
                "${Column.KEY_DRAWABLE} text, " +
                "${Column.KEY_DATE} text, " +
                "${Column.KEY_TIMER} text, " +
                "${Column.KEY_BEST_TIME} text);"
    }

    object Column {
        val KEY_ROWID: String = "_id"

        val KEY_CACHE: String = "cache"
        val KEY_DRAWABLE: String = "drawable"
        val KEY_DATE: String = "date"
        val KEY_TIMER: String = "timer"
        val KEY_BEST_TIME: String = "best_time"
    }
}

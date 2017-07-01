package com.gmail.jiangyang5157.sudoku.db

import com.gmail.jiangyang5157.kotlin_android_sql.BaseTable

/**
 * Created by Yang Jiang on July 02, 2017
 */
class PuzzleTable : BaseTable() {

    companion object {
        val TABLE_NAME: String = "Puzzle"

        val SQL_CREATE_TABLE: String
                = "create table $TABLE_NAME(${BaseTable.Column.KEY_ROWID} integer primary key autoincrement, " +
                "${Column.KEY_CACHE} text, " +
                "${Column.KEY_DRAWABLE} text, " +
                "${Column.KEY_DATE} text, " +
                "${Column.KEY_TIMER} text, " +
                "${Column.KEY_BEST_TIME} text);"
    }

    object Column {
        val KEY_CACHE: String = "cache"
        val KEY_DRAWABLE: String = "drawable"

        val KEY_DATE: String = "date"

        val KEY_TIMER: String = "timer"
        val KEY_BEST_TIME: String = "best_time"
    }
}

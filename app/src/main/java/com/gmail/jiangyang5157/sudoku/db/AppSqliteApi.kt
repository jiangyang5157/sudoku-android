package com.gmail.jiangyang5157.sudoku.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.gmail.jiangyang5157.kotlin_android_sql.BaseSqliteApi
import com.gmail.jiangyang5157.kotlin_android_sql.BaseSqliteOpenHelper
import com.gmail.jiangyang5157.kotlin_android_sql.BaseTable

/**
 * Created by Yang Jiang on July 02, 2017
 */
class AppSqliteApi private constructor(sqliteOpenHelper: BaseSqliteOpenHelper) : BaseSqliteApi(sqliteOpenHelper) {

    companion object {
        private val TAG: String = "AppSqliteApi"
        private var instance: AppSqliteApi? = null

        fun getInstance(context: Context): AppSqliteApi {
            if (instance == null) {
                instance = AppSqliteApi(AppSqliteOpenHelper(context))
                println("$TAG instance created")
            }
            return instance!!
        }
    }

    fun insertPuzzle(cache: String, drawable: String, date: String, timer: String, best_time: String): Long {
        open()
        try {
            val cv = ContentValues()
            cv.put(PuzzleTable.Column.KEY_CACHE, cache)
            cv.put(PuzzleTable.Column.KEY_DRAWABLE, drawable)
            cv.put(PuzzleTable.Column.KEY_DATE, date)
            cv.put(PuzzleTable.Column.KEY_TIMER, timer)
            cv.put(PuzzleTable.Column.KEY_BEST_TIME, best_time)
            return insert(PuzzleTable.TABLE_NAME, cv)
        } finally {
            close()
        }
    }

    fun updatePuzzle(rowId: String, cache: String, drawable: String, date: String, timer: String, best_time: String): Int {
        open()
        try {
            val cv = ContentValues()
            cv.put(PuzzleTable.Column.KEY_CACHE, cache)
            cv.put(PuzzleTable.Column.KEY_DRAWABLE, drawable)
            cv.put(PuzzleTable.Column.KEY_DATE, date)
            cv.put(PuzzleTable.Column.KEY_TIMER, timer)
            cv.put(PuzzleTable.Column.KEY_BEST_TIME, best_time)
            return update(PuzzleTable.TABLE_NAME, rowId, cv)
        } finally {
            close()
        }
    }

    fun queryPuzzle(key: String, value: String, orderBy: String): Cursor {
        open()
        try {
            val col = arrayOf(BaseTable.Column.KEY_ROWID,
                    PuzzleTable.Column.KEY_CACHE,
                    PuzzleTable.Column.KEY_DRAWABLE,
                    PuzzleTable.Column.KEY_DATE,
                    PuzzleTable.Column.KEY_TIMER,
                    PuzzleTable.Column.KEY_BEST_TIME)
            return queryValue(PuzzleTable.TABLE_NAME, col, key, value, orderBy)
        } finally {
            close()
        }
    }

    fun queryPuzzles(orderBy: String): Cursor {
        open()
        try {
            val col = arrayOf(BaseTable.Column.KEY_ROWID,
                    PuzzleTable.Column.KEY_CACHE,
                    PuzzleTable.Column.KEY_DRAWABLE,
                    PuzzleTable.Column.KEY_DATE,
                    PuzzleTable.Column.KEY_TIMER,
                    PuzzleTable.Column.KEY_BEST_TIME)
            return query(PuzzleTable.TABLE_NAME, col, orderBy)
        } finally {
            close()
        }
    }

    fun queryPuzzles(key: String, like: String, orderBy: String): Cursor {
        open()
        try {
            val col = arrayOf(BaseTable.Column.KEY_ROWID,
                    PuzzleTable.Column.KEY_CACHE,
                    PuzzleTable.Column.KEY_DRAWABLE,
                    PuzzleTable.Column.KEY_DATE,
                    PuzzleTable.Column.KEY_TIMER,
                    PuzzleTable.Column.KEY_BEST_TIME)
            return queryLike(PuzzleTable.TABLE_NAME, col, key, like, orderBy)
        } finally {
            close()
        }
    }

    fun deletePuzzle(key: String, value: String): Int {
        open()
        try {
            return delete(PuzzleTable.TABLE_NAME, key, value)
        } finally {
            close()
        }
    }

    fun deletePuzzles(): Int {
        open()
        try {
            return delete(PuzzleTable.TABLE_NAME)
        } finally {
            close()
        }
    }
}
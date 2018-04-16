package com.gmail.jiangyang5157.sudoku.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.gmail.jiangyang5157.kotlin_android_sql.BaseSqliteApi
import com.gmail.jiangyang5157.kotlin_android_sql.BaseSqliteOpenHelper

/**
 * Created by Yang Jiang on July 02, 2017
 */
class AppSqliteApi private constructor(sqliteOpenHelper: BaseSqliteOpenHelper) : BaseSqliteApi(sqliteOpenHelper) {

    companion object {
        private var instance: AppSqliteApi? = null

        fun getInstance(context: Context): AppSqliteApi {
            if (instance == null) {
                instance = AppSqliteApi(AppSqliteOpenHelper(context))
            }
            return instance!!
        }
    }

    fun insertPuzzle(cache: String, drawable: String, date: String, timer: String, best_time: String): Long {
        open()
        try {
            return insert(PuzzleTable.TABLE_NAME,
                    ContentValues().apply {
                        put(PuzzleTable.Column.KEY_CACHE, cache)
                        put(PuzzleTable.Column.KEY_CACHE, cache)
                        put(PuzzleTable.Column.KEY_DRAWABLE, drawable)
                        put(PuzzleTable.Column.KEY_DATE, date)
                        put(PuzzleTable.Column.KEY_TIMER, timer)
                        put(PuzzleTable.Column.KEY_BEST_TIME, best_time)
                    })
        } finally {
            close()
        }
    }

    fun updatePuzzle(rowId: String, cache: String, drawable: String, date: String, timer: String, best_time: String): Int {
        open()
        try {
            return update(PuzzleTable.TABLE_NAME,
                    PuzzleTable.Column.KEY_ID, rowId,
                    ContentValues().apply {
                        put(PuzzleTable.Column.KEY_CACHE, cache)
                        put(PuzzleTable.Column.KEY_DRAWABLE, drawable)
                        put(PuzzleTable.Column.KEY_DATE, date)
                        put(PuzzleTable.Column.KEY_TIMER, timer)
                        put(PuzzleTable.Column.KEY_BEST_TIME, best_time)
                    })
        } finally {
            close()
        }
    }

    fun queryPuzzle(key: String, value: String, orderBy: String): Cursor {
        open()
        try {
            return queryValue(PuzzleTable.TABLE_NAME,
                    arrayOf(PuzzleTable.Column.KEY_ID,
                            PuzzleTable.Column.KEY_CACHE,
                            PuzzleTable.Column.KEY_DRAWABLE,
                            PuzzleTable.Column.KEY_DATE,
                            PuzzleTable.Column.KEY_TIMER,
                            PuzzleTable.Column.KEY_BEST_TIME),
                    key, value, orderBy)
        } finally {
            close()
        }
    }

    fun queryPuzzles(orderBy: String): Cursor {
        open()
        try {
            return query(PuzzleTable.TABLE_NAME,
                    arrayOf(PuzzleTable.Column.KEY_ID,
                            PuzzleTable.Column.KEY_CACHE,
                            PuzzleTable.Column.KEY_DRAWABLE,
                            PuzzleTable.Column.KEY_DATE,
                            PuzzleTable.Column.KEY_TIMER,
                            PuzzleTable.Column.KEY_BEST_TIME),
                    orderBy)
        } finally {
            close()
        }
    }

    fun queryLikePuzzles(key: String, like: String, orderBy: String): Cursor {
        open()
        try {
            return queryLike(PuzzleTable.TABLE_NAME,
                    arrayOf(PuzzleTable.Column.KEY_ID,
                            PuzzleTable.Column.KEY_CACHE,
                            PuzzleTable.Column.KEY_DRAWABLE,
                            PuzzleTable.Column.KEY_DATE,
                            PuzzleTable.Column.KEY_TIMER,
                            PuzzleTable.Column.KEY_BEST_TIME),
                    key, like, orderBy)
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
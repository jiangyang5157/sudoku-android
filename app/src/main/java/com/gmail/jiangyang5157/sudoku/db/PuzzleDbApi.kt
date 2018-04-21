package com.gmail.jiangyang5157.sudoku.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.gmail.jiangyang5157.kotlin_android_kit.db.BaseDb

/**
 * Created by Yang Jiang on July 02, 2017
 */
class PuzzleDbApi private constructor(puzzleDbHelper: PuzzleDbHelper) : BaseDb(puzzleDbHelper) {

    companion object {

        private var INSTANCE: PuzzleDbApi? = null

        fun getInstance(context: Context): PuzzleDbApi {
            if (INSTANCE == null) {
                INSTANCE = PuzzleDbApi(PuzzleDbHelper(context))
            }
            return INSTANCE!!
        }
    }

    fun insertPuzzle(cache: String, date: String): Long {
        open()
        try {
            return insert(PuzzleTable.TABLE_NAME,
                    ContentValues().apply {
                        put(PuzzleTable.Column.KEY_TERMINAL_DATA, cache)
                        put(PuzzleTable.Column.KEY_LAST_MODIFIED_DATE, date)
                    })
        } finally {
            close()
        }
    }

    fun updatePuzzle(rowId: String, cache: String, date: String): Int {
        open()
        try {
            return update(PuzzleTable.TABLE_NAME,
                    PuzzleTable.Column.KEY_ID, rowId,
                    ContentValues().apply {
                        put(PuzzleTable.Column.KEY_TERMINAL_DATA, cache)
                        put(PuzzleTable.Column.KEY_LAST_MODIFIED_DATE, date)
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
                            PuzzleTable.Column.KEY_TERMINAL_DATA,
                            PuzzleTable.Column.KEY_LAST_MODIFIED_DATE),
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
                            PuzzleTable.Column.KEY_TERMINAL_DATA,
                            PuzzleTable.Column.KEY_LAST_MODIFIED_DATE),
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
                            PuzzleTable.Column.KEY_TERMINAL_DATA,
                            PuzzleTable.Column.KEY_LAST_MODIFIED_DATE),
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
package com.gmail.jiangyang5157.sudoku.db

import android.content.Context
import com.gmail.jiangyang5157.kotlin_android_sql.BaseSqliteOpenHelper

/**
 * Created by Yang Jiang on July 02, 2017
 */
class AppSqliteOpenHelper(context: Context) : BaseSqliteOpenHelper(context, DB_FILE_NAME, null, DB_VERSION) {

    companion object {
        private val DB_FILE_NAME: String = "sudoku_endless.db"
        private val DB_VERSION: Int = 5
    }

    override val sqlsTableOnCreate: Array<String>
        get() = arrayOf(PuzzleTable.SQL_TABLE_CREATION)

    override val tableNamesOnUpgrade: Array<String>
        get() = arrayOf(PuzzleTable.TABLE_NAME)
}

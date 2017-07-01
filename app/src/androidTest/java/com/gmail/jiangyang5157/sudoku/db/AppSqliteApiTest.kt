package com.gmail.jiangyang5157.sudoku.db

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.gmail.jiangyang5157.kotlin_android_sql.BaseSqliteApi
import com.gmail.jiangyang5157.kotlin_android_sql.BaseTable
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Yang Jiang on July 02, 2017
 */
@RunWith(AndroidJUnit4::class)
class AppSqliteApiTest {

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getTargetContext()
        assertNotEquals(BaseTable.INVALID_ROWID, AppSqliteApi.getInstance(appContext).insertPuzzle("", "", "", "11", "11"))
        assertNotEquals(BaseTable.INVALID_ROWID, AppSqliteApi.getInstance(appContext).insertPuzzle("", "", "", "22", "22"))
        assertNotEquals(BaseTable.INVALID_ROWID, AppSqliteApi.getInstance(appContext).insertPuzzle("", "", "", "33", "2222"))
        assertNotEquals(BaseTable.INVALID_ROWID, AppSqliteApi.getInstance(appContext).insertPuzzle("", "", "", "44", "11"))
        assertNotEquals(BaseTable.INVALID_ROWID, AppSqliteApi.getInstance(appContext).insertPuzzle("", "", "", "55", "2222"))
        val cursor = AppSqliteApi.getInstance(appContext).queryPuzzles(BaseSqliteApi.OrderBy.asc(PuzzleTable.Column.KEY_DATE))
        assertEquals(5, cursor.count)
    }

    @After
    fun tearDown() {
        val appContext = InstrumentationRegistry.getTargetContext()
        AppSqliteApi.getInstance(appContext).deletePuzzles()
        val cursor = AppSqliteApi.getInstance(appContext).queryPuzzles(BaseSqliteApi.OrderBy.asc(PuzzleTable.Column.KEY_DATE))
        assertEquals(0, cursor.count)
    }

    @Test
    fun test_updatePuzzle() {
        val appContext = InstrumentationRegistry.getTargetContext()

        val before = AppSqliteApi.getInstance(appContext).queryPuzzles(BaseSqliteApi.OrderBy.asc(PuzzleTable.Column.KEY_DATE))
        (1..before.count).map {
            val rowIdBefore = before.getLong(before.getColumnIndexOrThrow(BaseTable.Column.KEY_ROWID))
            val timerBefore = before.getString(before.getColumnIndexOrThrow(PuzzleTable.Column.KEY_TIMER))
            val bestTimeBefore = before.getString(before.getColumnIndexOrThrow(PuzzleTable.Column.KEY_BEST_TIME))
            println("test_updatePuzzle before [$rowIdBefore : $timerBefore $bestTimeBefore]")
            before.moveToNext()
        }

        val cursor = AppSqliteApi.getInstance(appContext).queryPuzzle(PuzzleTable.Column.KEY_BEST_TIME, "22", BaseSqliteApi.OrderBy.asc(PuzzleTable.Column.KEY_DATE))
        val rowId = cursor.getLong(cursor.getColumnIndexOrThrow(BaseTable.Column.KEY_ROWID))
        val updateResult = AppSqliteApi.getInstance(appContext).updatePuzzle(rowId.toString(), "", "", "", "modified", "modified")
        assertTrue(updateResult > 0)

        val after = AppSqliteApi.getInstance(appContext).queryPuzzles(BaseSqliteApi.OrderBy.asc(PuzzleTable.Column.KEY_DATE))
        (1..after.count).map {
            val rowIdAfter = after.getLong(after.getColumnIndexOrThrow(BaseTable.Column.KEY_ROWID))
            val timerAfter = after.getString(after.getColumnIndexOrThrow(PuzzleTable.Column.KEY_TIMER))
            val bestTimeAfter = after.getString(after.getColumnIndexOrThrow(PuzzleTable.Column.KEY_BEST_TIME))
            println("test_updatePuzzle After [$rowIdAfter : $timerAfter $bestTimeAfter]")
            after.moveToNext()
        }
    }

    @Test
    fun test_queryPuzzle() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val cursor = AppSqliteApi.getInstance(appContext).queryPuzzle(PuzzleTable.Column.KEY_BEST_TIME, "22", BaseSqliteApi.OrderBy.asc(PuzzleTable.Column.KEY_DATE))
        assertEquals(1, cursor.count)
    }

    @Test
    fun test_queryPuzzlesLike() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val cursor = AppSqliteApi.getInstance(appContext).queryPuzzles(PuzzleTable.Column.KEY_BEST_TIME, "22", BaseSqliteApi.OrderBy.asc(PuzzleTable.Column.KEY_DATE))
        assertEquals(3, cursor.count)
    }

    @Test
    fun test_deleteTestTableByKey() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val deletePuzzleResult = AppSqliteApi.getInstance(appContext).deletePuzzle(PuzzleTable.Column.KEY_BEST_TIME, "2222")
        assertTrue(deletePuzzleResult > 0)
    }
}
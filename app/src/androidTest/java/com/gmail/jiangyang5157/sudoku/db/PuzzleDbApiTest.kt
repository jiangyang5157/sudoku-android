package com.gmail.jiangyang5157.sudoku.db

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.gmail.jiangyang5157.kotlin_android_kit.db.BaseDb
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Yang Jiang on July 02, 2017
 */
@RunWith(AndroidJUnit4::class)
class PuzzleDbApiTest {

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getTargetContext()
        assertNotEquals(-1, PuzzleDbApi.getInstance(appContext).insertPuzzle("", "", "", "11", "11"))
        assertNotEquals(-1, PuzzleDbApi.getInstance(appContext).insertPuzzle("", "", "", "22", "22"))
        assertNotEquals(-1, PuzzleDbApi.getInstance(appContext).insertPuzzle("", "", "", "33", "2222"))
        assertNotEquals(-1, PuzzleDbApi.getInstance(appContext).insertPuzzle("", "", "", "44", "11"))
        assertNotEquals(-1, PuzzleDbApi.getInstance(appContext).insertPuzzle("", "", "", "55", "2222"))
        val cursor = PuzzleDbApi.getInstance(appContext).queryPuzzles(BaseDb.OrderBy.asc(PuzzleTable.Column.KEY_DATE))
        assertEquals(5, cursor.count)
    }

    @After
    fun tearDown() {
        val appContext = InstrumentationRegistry.getTargetContext()
        PuzzleDbApi.getInstance(appContext).deletePuzzles()
        val cursor = PuzzleDbApi.getInstance(appContext).queryPuzzles(BaseDb.OrderBy.asc(PuzzleTable.Column.KEY_DATE))
        assertEquals(0, cursor.count)
    }

    @Test
    fun test_updatePuzzle() {
        val appContext = InstrumentationRegistry.getTargetContext()

        val before = PuzzleDbApi.getInstance(appContext).queryPuzzles(BaseDb.OrderBy.asc(PuzzleTable.Column.KEY_DATE))
        (1..before.count).map {
            val rowIdBefore = before.getLong(before.getColumnIndexOrThrow(PuzzleTable.Column.KEY_ID))
            val timerBefore = before.getString(before.getColumnIndexOrThrow(PuzzleTable.Column.KEY_TIMER))
            val bestTimeBefore = before.getString(before.getColumnIndexOrThrow(PuzzleTable.Column.KEY_BEST_TIME))
            println("test_updatePuzzle before [$rowIdBefore : $timerBefore $bestTimeBefore]")
            before.moveToNext()
        }

        val cursor = PuzzleDbApi.getInstance(appContext).queryPuzzle(PuzzleTable.Column.KEY_BEST_TIME, "22", BaseDb.OrderBy.asc(PuzzleTable.Column.KEY_DATE))
        val rowId = cursor.getLong(cursor.getColumnIndexOrThrow(PuzzleTable.Column.KEY_ID))
        val updateResult = PuzzleDbApi.getInstance(appContext).updatePuzzle(rowId.toString(), "", "", "", "modified", "modified")
        assertTrue(updateResult > 0)

        val after = PuzzleDbApi.getInstance(appContext).queryPuzzles(BaseDb.OrderBy.asc(PuzzleTable.Column.KEY_DATE))
        (1..after.count).map {
            val rowIdAfter = after.getLong(after.getColumnIndexOrThrow(PuzzleTable.Column.KEY_ID))
            val timerAfter = after.getString(after.getColumnIndexOrThrow(PuzzleTable.Column.KEY_TIMER))
            val bestTimeAfter = after.getString(after.getColumnIndexOrThrow(PuzzleTable.Column.KEY_BEST_TIME))
            println("test_updatePuzzle After [$rowIdAfter : $timerAfter $bestTimeAfter]")
            after.moveToNext()
        }
    }

    @Test
    fun test_queryPuzzle() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val cursor = PuzzleDbApi.getInstance(appContext).queryPuzzle(PuzzleTable.Column.KEY_BEST_TIME, "22", BaseDb.OrderBy.asc(PuzzleTable.Column.KEY_DATE))
        assertEquals(1, cursor.count)
    }

    @Test
    fun test_queryLikePuzzles() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val cursor = PuzzleDbApi.getInstance(appContext).queryLikePuzzles(PuzzleTable.Column.KEY_BEST_TIME, "22", BaseDb.OrderBy.asc(PuzzleTable.Column.KEY_DATE))
        assertEquals(3, cursor.count)
    }

    @Test
    fun test_deletePuzzle() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val deletePuzzleResult = PuzzleDbApi.getInstance(appContext).deletePuzzle(PuzzleTable.Column.KEY_BEST_TIME, "2222")
        assertTrue(deletePuzzleResult > 0)
    }
}
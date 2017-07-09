package com.gmail.jiangyang5157.sudoku.data

import android.support.test.runner.AndroidJUnit4
import com.gmail.jiangyang5157.sudoku.puzzle.SolverTest
import org.json.JSONObject
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import sudoku.Sudoku

/**
 * Created by Yang Jiang on July 09, 2017'
 */
@RunWith(AndroidJUnit4::class)
class TerminalTest {

    @Test
    fun test_Terminal() {
        val resultJson = Sudoku.solveString(SolverTest.data.terminalJson_9x9_2)
        val jsonObj = JSONObject(resultJson)
        val e = jsonObj.getInt("E")
        Assert.assertEquals(9, e)
        val c = jsonObj.getJSONArray("C")
        Assert.assertEquals(81, c.length())
        for (i in 0..c.length() - 1) {
            val b = c.getJSONObject(i).getInt("B")
            val d = c.getJSONObject(i).getInt("D")
            Assert.assertTrue(b >= 0)
            Assert.assertTrue(d >= 0)
        }
    }

}
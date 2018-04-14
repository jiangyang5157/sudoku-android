package com.gmail.jiangyang5157.sudoku_presenter.puzzle

import android.support.test.runner.AndroidJUnit4
import org.json.JSONObject
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import sudoku.Sudoku

/**
 * Created by Yang Jiang on July 02, 2017
 */
@RunWith(AndroidJUnit4::class)
class SolverTest {

    @Test
    fun test_solver_9x9_0() {
        val resultString = Sudoku.solveString(TestData.terminalJson_9x9_0)
        Assert.assertNotNull(resultString)
        Assert.assertTrue(resultString.isNotEmpty())
        Assert.assertEquals(resultString, "null")
    }

    @Test
    fun test_solver_9x9_1() {
        val resultString = Sudoku.solveString(TestData.terminalJson_9x9_1)
        Assert.assertNotNull(resultString)
        Assert.assertTrue(resultString.isNotEmpty())
        Assert.assertNotEquals(resultString, "null")
    }

    @Test
    fun test_solver_9x9_2() {
        val resultString = Sudoku.solveString(TestData.terminalJson_9x9_2)
        Assert.assertNotNull(resultString)
        Assert.assertTrue(resultString.isNotEmpty())
        Assert.assertNotEquals(resultString, "null")

        val jsonObj = JSONObject(resultString)
        val e = jsonObj.getInt("E")
        Assert.assertEquals(9, e)
        val c = jsonObj.getJSONArray("C")
        Assert.assertEquals(81, c.length())
        for (i in 0 until c.length()) {
            val b = c.getJSONObject(i).getInt("B")
            val d = c.getJSONObject(i).getInt("D")
            Assert.assertTrue(b >= 0)
            Assert.assertTrue(d >= 0)
        }
    }

    object TestData {
        const val terminalJson_9x9_0 = """
    {
        "E":9,
        "C":[
        {"B":0,"D":0},
        {"B":0,"D":0},
        {"B":0,"D":0},
        {"B":1,"D":0},
        {"B":1,"D":0},
        {"B":1,"D":0},
        {"B":2,"D":1},
        {"B":2,"D":2},
        {"B":2,"D":3},

        {"B":0,"D":0},
        {"B":0,"D":0},
        {"B":0,"D":9},
        {"B":1,"D":0},
        {"B":1,"D":0},
        {"B":1,"D":0},
        {"B":2,"D":0},
        {"B":2,"D":0},
        {"B":2,"D":0},

        {"B":0,"D":0},
        {"B":0,"D":0},
        {"B":0,"D":0},
        {"B":1,"D":0},
        {"B":1,"D":0},
        {"B":1,"D":9},
        {"B":2,"D":0},
        {"B":2,"D":0},
        {"B":2,"D":0},

        {"B":3,"D":0},
        {"B":3,"D":0},
        {"B":3,"D":0},
        {"B":4,"D":0},
        {"B":4,"D":0},
        {"B":4,"D":0},
        {"B":5,"D":0},
        {"B":5,"D":0},
        {"B":5,"D":0},

        {"B":3,"D":0},
        {"B":3,"D":0},
        {"B":3,"D":0},
        {"B":4,"D":0},
        {"B":4,"D":0},
        {"B":4,"D":0},
        {"B":5,"D":0},
        {"B":5,"D":0},
        {"B":5,"D":0},

        {"B":3,"D":0},
        {"B":3,"D":0},
        {"B":3,"D":0},
        {"B":4,"D":0},
        {"B":4,"D":0},
        {"B":4,"D":0},
        {"B":5,"D":0},
        {"B":5,"D":0},
        {"B":5,"D":0},

        {"B":6,"D":0},
        {"B":6,"D":0},
        {"B":6,"D":0},
        {"B":7,"D":0},
        {"B":7,"D":0},
        {"B":7,"D":0},
        {"B":8,"D":0},
        {"B":8,"D":0},
        {"B":8,"D":0},

        {"B":6,"D":0},
        {"B":6,"D":0},
        {"B":6,"D":0},
        {"B":7,"D":0},
        {"B":7,"D":0},
        {"B":7,"D":0},
        {"B":8,"D":0},
        {"B":8,"D":0},
        {"B":8,"D":0},

        {"B":6,"D":0},
        {"B":6,"D":0},
        {"B":6,"D":0},
        {"B":7,"D":0},
        {"B":7,"D":0},
        {"B":7,"D":0},
        {"B":8,"D":0},
        {"B":8,"D":0},
        {"B":8,"D":0}
        ]
    }
    """

        // "........." +
// 	"..41.26.." +
// 	".3..5..2." +
// 	".2..1..3." +
// 	"..65.41.." +
// 	".8..7..4." +
// 	".7..2..6." +
// 	"..14.35.." +
// 	"........." // 1 solutions puzzle
        const val terminalJson_9x9_1 = """
    {
        "E":9,
        "C":[
        {"B":0,"D":0},
        {"B":0,"D":0},
        {"B":0,"D":0},
        {"B":1,"D":0},
        {"B":1,"D":0},
        {"B":1,"D":0},
        {"B":2,"D":0},
        {"B":2,"D":0},
        {"B":2,"D":0},

        {"B":0,"D":0},
        {"B":0,"D":0},
        {"B":0,"D":4},
        {"B":1,"D":1},
        {"B":1,"D":0},
        {"B":1,"D":2},
        {"B":2,"D":6},
        {"B":2,"D":0},
        {"B":2,"D":0},

        {"B":0,"D":0},
        {"B":0,"D":3},
        {"B":0,"D":0},
        {"B":1,"D":0},
        {"B":1,"D":5},
        {"B":1,"D":0},
        {"B":2,"D":0},
        {"B":2,"D":2},
        {"B":2,"D":0},

        {"B":3,"D":0},
        {"B":3,"D":2},
        {"B":3,"D":0},
        {"B":4,"D":0},
        {"B":4,"D":1},
        {"B":4,"D":0},
        {"B":5,"D":0},
        {"B":5,"D":3},
        {"B":5,"D":0},

        {"B":3,"D":0},
        {"B":3,"D":0},
        {"B":3,"D":6},
        {"B":4,"D":5},
        {"B":4,"D":0},
        {"B":4,"D":4},
        {"B":5,"D":1},
        {"B":5,"D":0},
        {"B":5,"D":0},

        {"B":3,"D":0},
        {"B":3,"D":8},
        {"B":3,"D":0},
        {"B":4,"D":0},
        {"B":4,"D":7},
        {"B":4,"D":0},
        {"B":5,"D":0},
        {"B":5,"D":4},
        {"B":5,"D":0},

        {"B":6,"D":0},
        {"B":6,"D":7},
        {"B":6,"D":0},
        {"B":7,"D":0},
        {"B":7,"D":2},
        {"B":7,"D":0},
        {"B":8,"D":0},
        {"B":8,"D":6},
        {"B":8,"D":0},

        {"B":6,"D":0},
        {"B":6,"D":0},
        {"B":6,"D":1},
        {"B":7,"D":4},
        {"B":7,"D":0},
        {"B":7,"D":3},
        {"B":8,"D":5},
        {"B":8,"D":0},
        {"B":8,"D":0},

        {"B":6,"D":0},
        {"B":6,"D":0},
        {"B":6,"D":0},
        {"B":7,"D":0},
        {"B":7,"D":0},
        {"B":7,"D":0},
        {"B":8,"D":0},
        {"B":8,"D":0},
        {"B":8,"D":0}
        ]
    }
    """

        // "..3456789" +
// 	"456789123" +
// 	"789123456" +
// 	"..4365897" +
// 	"365897214" +
// 	"897214365" +
// 	"531642978" +
// 	"642978531" +
// 	"978531642" // 2 solutions puzzle
        const val terminalJson_9x9_2 = """
    {
        "E":9,
        "C":[
        {"B":0,"D":0},
        {"B":0,"D":0},
        {"B":0,"D":3},
        {"B":1,"D":4},
        {"B":1,"D":5},
        {"B":1,"D":6},
        {"B":2,"D":7},
        {"B":2,"D":8},
        {"B":2,"D":9},

        {"B":0,"D":4},
        {"B":0,"D":5},
        {"B":0,"D":6},
        {"B":1,"D":7},
        {"B":1,"D":8},
        {"B":1,"D":9},
        {"B":2,"D":1},
        {"B":2,"D":2},
        {"B":2,"D":3},

        {"B":0,"D":7},
        {"B":0,"D":8},
        {"B":0,"D":9},
        {"B":1,"D":1},
        {"B":1,"D":2},
        {"B":1,"D":3},
        {"B":2,"D":4},
        {"B":2,"D":5},
        {"B":2,"D":6},

        {"B":3,"D":0},
        {"B":3,"D":0},
        {"B":3,"D":4},
        {"B":4,"D":3},
        {"B":4,"D":6},
        {"B":4,"D":5},
        {"B":5,"D":8},
        {"B":5,"D":9},
        {"B":5,"D":7},

        {"B":3,"D":3},
        {"B":3,"D":6},
        {"B":3,"D":5},
        {"B":4,"D":8},
        {"B":4,"D":9},
        {"B":4,"D":7},
        {"B":5,"D":2},
        {"B":5,"D":1},
        {"B":5,"D":4},

        {"B":3,"D":8},
        {"B":3,"D":9},
        {"B":3,"D":7},
        {"B":4,"D":2},
        {"B":4,"D":1},
        {"B":4,"D":4},
        {"B":5,"D":3},
        {"B":5,"D":6},
        {"B":5,"D":5},

        {"B":6,"D":5},
        {"B":6,"D":3},
        {"B":6,"D":1},
        {"B":7,"D":6},
        {"B":7,"D":4},
        {"B":7,"D":2},
        {"B":8,"D":9},
        {"B":8,"D":7},
        {"B":8,"D":8},

        {"B":6,"D":6},
        {"B":6,"D":4},
        {"B":6,"D":2},
        {"B":7,"D":9},
        {"B":7,"D":7},
        {"B":7,"D":8},
        {"B":8,"D":5},
        {"B":8,"D":3},
        {"B":8,"D":1},

        {"B":6,"D":9},
        {"B":6,"D":7},
        {"B":6,"D":8},
        {"B":7,"D":5},
        {"B":7,"D":3},
        {"B":7,"D":1},
        {"B":8,"D":6},
        {"B":8,"D":4},
        {"B":8,"D":2}
        ]
    }
    """
    }
}
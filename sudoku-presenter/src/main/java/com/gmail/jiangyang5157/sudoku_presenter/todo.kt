package com.gmail.jiangyang5157.sudoku_presenter

/*
class GeneratePuzzleTask(puzzleGeneration: PuzzleGeneration) : AsyncTask<Int, Void, Terminal>() {

    interface PuzzleGeneration {
        fun onPrePuzzleGeneration()
        fun onPostPuzzleGeneration(result: Terminal?)
    }

    private val mPuzzleGeneration: PuzzleGeneration = puzzleGeneration

    override fun onPreExecute() {
        mPuzzleGeneration.onPrePuzzleGeneration()
    }

    override fun doInBackground(vararg params: Int?): Terminal {
        val edge = params[0]!!
        val minSubGiven = params[1]!!
        val minTotalGiven = params[2]!!

        val s = Sudoku.genString(0, edge.toLong(), minSubGiven.toLong(), minTotalGiven.toLong())
        return Gson().fromJson(s, Terminal::class.java)
    }

    override fun onPostExecute(result: Terminal?) {
        mPuzzleGeneration.onPostPuzzleGeneration(result)
    }

    override fun onCancelled(result: Terminal?) {
        mPuzzleGeneration.onPostPuzzleGeneration(result)
    }

}

class ResolvePuzzleTask(puzzleResolution: PuzzleResolution) : AsyncTask<Terminal, Void, Terminal>() {

    interface PuzzleResolution {
        fun onPrePuzzleResolution()
        fun onPostPuzzleResolution(result: Terminal?)
    }

    private val mPuzzleResolution: PuzzleResolution = puzzleResolution

    override fun onPreExecute() {
        mPuzzleResolution.onPrePuzzleResolution()
    }

    override fun doInBackground(vararg params: Terminal?): Terminal {
        val t = params[0]!!

        val s = Sudoku.solveString(t.toString())
        return Gson().fromJson(s, Terminal::class.java)
    }

    override fun onPostExecute(result: Terminal?) {
        mPuzzleResolution.onPostPuzzleResolution(result)
    }

    override fun onCancelled(result: Terminal?) {
        mPuzzleResolution.onPostPuzzleResolution(result)
    }

}

@RunWith(AndroidJUnit4::class)
class GeneratePuzzleTaskTest {

    @Test
    fun test_execute() {
        val signal = CountDownLatch(2)
        var t : Terminal? = null

        GeneratePuzzleTask(object : SudokuContract.PuzzleGeneration {

            override fun onPrePuzzleGeneration() {
                signal.countDown()
            }

            override fun onPostPuzzleGeneration(result: Terminal?) {
                t = result
                signal.countDown()
            }
        }).execute(9, 4, 55)

        signal.await(10, TimeUnit.SECONDS)
        Assert.assertNotNull(t)
    }

}

@RunWith(AndroidJUnit4::class)
class ResolvePuzzleTaskTest {

    @Test
    fun test_execute() {
        val signal = CountDownLatch(2)
        val t1 : Terminal = Gson().fromJson(SolverTest.TestData.terminalJson_9x9_2, Terminal::class.java)
        var t2: Terminal? = null

        ResolvePuzzleTask(object : SudokuContract.PuzzleResolution {

            override fun onPrePuzzleResolution() {
                signal.countDown()
            }

            override fun onPostPuzzleResolution(result: Terminal?) {
                t2 = result
                signal.countDown()
            }
        }).execute(t1)

        signal.await(10, TimeUnit.SECONDS)

        val t3 = Gson().fromJson(Sudoku.solveString(t1.toString()), Terminal::class.java)

        Assert.assertNotNull(t2)
        Assert.assertNotNull(t3)

        Assert.assertTrue(t2 == t3)
        Assert.assertTrue(t2?.E == 9)
    }

}
*/
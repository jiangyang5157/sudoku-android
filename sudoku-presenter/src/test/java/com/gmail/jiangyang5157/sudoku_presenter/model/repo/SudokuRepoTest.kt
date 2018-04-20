package com.gmail.jiangyang5157.sudoku_presenter.model.repo

import com.gmail.jiangyang5157.sudoku_presenter.model.Cell
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import org.junit.Assert
import org.junit.Test

/**
 * Created by Yang Jiang on April 20, 2018
 */
class SudokuRepoTest {

    @Test
    fun test_update_find_size() {
        val t1 = Terminal(1)
        t1.C[0] = Cell(1, 1)

        val repo = SudokuRepo()
        var all = repo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL, SudokuRepoSpec.INDEX_PUZZLE, SudokuRepoSpec.INDEX_PROGRESS)))
        Assert.assertEquals(0, all.size)

        repo.update(t1, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
        all = repo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL, SudokuRepoSpec.INDEX_PUZZLE, SudokuRepoSpec.INDEX_PROGRESS)))
        Assert.assertEquals(1, all.size)

        repo.update(t1, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PUZZLE)))
        all = repo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL, SudokuRepoSpec.INDEX_PUZZLE, SudokuRepoSpec.INDEX_PROGRESS)))
        Assert.assertEquals(2, all.size)

        repo.update(t1, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PUZZLE)))
        all = repo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL, SudokuRepoSpec.INDEX_PUZZLE, SudokuRepoSpec.INDEX_PROGRESS)))
        Assert.assertEquals(2, all.size)

        repo.update(t1, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PROGRESS)))
        all = repo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL, SudokuRepoSpec.INDEX_PUZZLE, SudokuRepoSpec.INDEX_PROGRESS)))
        Assert.assertEquals(3, all.size)

        all = repo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL, SudokuRepoSpec.INDEX_PUZZLE)))
        Assert.assertEquals(2, all.size)

        all = repo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
        Assert.assertEquals(1, all.size)

        all = repo.find(SudokuRepoSpec(intArrayOf()))
        Assert.assertEquals(0, all.size)
    }

    @Test
    fun test_update_find_copy() {
        val t1 = Terminal(1)
        t1.C[0] = Cell(1, 1)

        val repo = SudokuRepo()
        var all = repo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL, SudokuRepoSpec.INDEX_PUZZLE, SudokuRepoSpec.INDEX_PROGRESS)))
        Assert.assertEquals(0, all.size)

        repo.update(t1, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))

        all = repo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
        Assert.assertEquals("{\"E\":1,\"C\":[{\"B\":1,\"D\":1}]}", all[0].toString())

        all[0].C[0]?.D = 2
        all = repo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
        Assert.assertEquals("{\"E\":1,\"C\":[{\"B\":1,\"D\":1}]}", all[0].toString())

        all[0].C[0]?.D = 2
        repo.update(all[0], SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
        all = repo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
        Assert.assertEquals("{\"E\":1,\"C\":[{\"B\":1,\"D\":2}]}", all[0].toString())
    }

    @Test
    fun test_remove() {
        val t1 = Terminal(1)
        val t2 = Terminal(2)
        val t3 = Terminal(3)
        t1.C[0] = Cell(1, 1)
        t1.C[0] = Cell(2, 2)
        t1.C[0] = Cell(3, 3)

        val repo = SudokuRepo()
        repo.update(t1, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
        repo.update(t2, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PUZZLE)))
        repo.update(t3, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PROGRESS)))
        var all = repo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL, SudokuRepoSpec.INDEX_PUZZLE, SudokuRepoSpec.INDEX_PROGRESS)))
        Assert.assertEquals(3, all.size)

        repo.remove(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
        all = repo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL, SudokuRepoSpec.INDEX_PUZZLE, SudokuRepoSpec.INDEX_PROGRESS)))
        Assert.assertEquals(2, all.size)

        all = repo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PUZZLE, SudokuRepoSpec.INDEX_PROGRESS)))
        Assert.assertEquals(2, all.size)

        all = repo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL, SudokuRepoSpec.INDEX_PROGRESS)))
        Assert.assertEquals(1, all.size)

        all = repo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL, SudokuRepoSpec.INDEX_PUZZLE)))
        Assert.assertEquals(1, all.size)
    }

}



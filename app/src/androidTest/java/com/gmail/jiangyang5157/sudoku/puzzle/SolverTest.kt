package com.gmail.jiangyang5157.sudoku.puzzle

import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

import sudoku.Sudoku;

/**
 * Created by Yang Jiang on July 02, 2017
 */
@RunWith(AndroidJUnit4::class)
class SolverTest {

    // 0 solution 9x9 puzzle
    val hasNone: String =
            "......123" +
                    "..9......" +
                    ".....9..." +
                    "........." +
                    "........." +
                    "........." +
                    "........." +
                    "........." +
                    "........."

    // 2 solutions 9x9 puzzle
    val hasTwo: String =
            "..3456789" +
                    "456789123" +
                    "789123456" +
                    "..4365897" +
                    "365897214" +
                    "897214365" +
                    "531642978" +
                    "642978531" +
                    "978531642"

    /*
    TODO
    java.lang.UnsatisfiedLinkError:
    dalvik.system.PathClassLoader[
        DexPathList[
            [
                zip file "/system/framework/android.test.runner.jar",
                zip file "/data/app/com.gmail.jiangyang5157.sudoku.test-1/base.apk",
                zip file "/data/app/com.gmail.jiangyang5157.sudoku-2/base.apk"
            ],
            nativeLibraryDirectories=
                [
                    /data/app/com.gmail.jiangyang5157.sudoku.test-1/lib/x86_64,
                    /data/app/com.gmail.jiangyang5157.sudoku-2/lib/x86_64,
                    /system/lib64, /vendor/lib64
                ]
        ]
    ] couldn't find "libgojni.so"
     */
    @Test
    fun test_solver_none() {
        var result: String
        result = Sudoku.solveRaw(3, hasNone, 0)
        println("test_solver result=" + result)
        result = Sudoku.solveRaw(3, hasNone, 1)
        println("test_solver result=" + result)
    }

    @Test
    fun test_solver_two() {
        var result: String
        result = Sudoku.solveRaw(3, hasTwo, 0)
        println("test_solver result=" + result)
        result = Sudoku.solveRaw(3, hasTwo, 1)
        println("test_solver result=" + result)
        result = Sudoku.solveRaw(3, hasTwo, 2)
        println("test_solver result=" + result)
        result = Sudoku.solveRaw(3, hasTwo, 3)
        println("test_solver result=" + result)
    }

}
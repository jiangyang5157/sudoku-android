package com.gmail.jiangyang5157.sudoku.widget.scan.imgproc

import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.imgproc.Imgproc

/**
 * Created by Yang Jiang on May 02, 2018
 */
data class FindContours(var mode: Int, var method: Int = Imgproc.CHAIN_APPROX_SIMPLE) {

    fun find(src: Mat): List<MatOfPoint> {
        val hierarchy = Mat()
        val ret = find(src, hierarchy)
        hierarchy.release()
        return ret
    }

    fun find(src: Mat, hierarchy: Mat): List<MatOfPoint> {
        val ret = arrayListOf<MatOfPoint>()
        Imgproc.findContours(src, ret, hierarchy, mode, method)
        return ret
    }

}
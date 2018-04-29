package com.gmail.jiangyang5157.sudoku.ui

import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.jiangyang5157.kotlin_kit.render.FPSValidation
import com.gmail.jiangyang5157.sudoku.R
import com.gmail.jiangyang5157.sudoku.widget.scan.ScanCamera2View
import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_MEAN_C
import org.opencv.imgproc.Imgproc.THRESH_BINARY_INV

/**
 * Created by Yang Jiang on April 21, 2018
 */
class ScanFragment : Fragment(), CameraBridgeViewBase.CvCameraViewListener2 {

    companion object {
        const val TAG = "ScanFragment"
    }

    private var mScanCamera2View: ScanCamera2View? = null
    private var isScanCamera2ViewEnabled = false
    private lateinit var scalarAccent: Scalar
    private val mOcrFrameRate = FPSValidation(2)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view?.apply {
            mScanCamera2View = findViewById(R.id.scancamera2view) as ScanCamera2View
            mScanCamera2View?.setCvCameraViewListener(this@ScanFragment)
            findViewById(R.id.btn_toggle).setOnClickListener { toggleScanCamera2View() }
        }
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        Log.d(TAG, "onCameraViewStarted: ${width}x$height")
        scalarAccent = color2scalar(R.color.colorAccent)
    }

    override fun onCameraViewStopped() {
        Log.d(TAG, "onCameraViewStopped")
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
        var rgba = inputFrame.rgba().clone()
        Imgproc.cvtColor(rgba, rgba, Imgproc.COLOR_RGBA2BGR)
        if (mOcrFrameRate.accept()) {
            rgba = handleRgba(rgba, inputFrame.gray())
        }
        return rgba
    }

    private fun handleRgba(rgba: Mat, gray: Mat): Mat {

        /**
         * This smooths out the noise a bit and makes extracting the grid lines easier
         */
        val blurMat = Mat()
        Imgproc.GaussianBlur(gray, blurMat,
                Size(5.0, 5.0), 0.0, 0.0)

        /**
         * It calculates a mean over a 5x5 window and subtracts 2 from the mean.
         * This is the threshold level for every pixel.
         */
        val adaptiveThresholdMat = Mat()
        Imgproc.adaptiveThreshold(blurMat, adaptiveThresholdMat,
                255.0, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY_INV, 5, 2.0)

        /**
         * Thresholding operation can disconnect certain connected parts (like lines).
         * So dilating the image once will fill up any small "cracks" that might have crept in.
         */
        val dilateMat = Mat()
        val dilationSize = 1.0
        val kernelMat = Imgproc.getStructuringElement(Imgproc.MORPH_CROSS,
                Size(2 * dilationSize + 1, 2 * dilationSize + 1), Point(dilationSize, dilationSize))
        Imgproc.dilate(adaptiveThresholdMat, dilateMat, kernelMat)

        /**
         * Find contour with largest area
         */
        val contoursMatOfPoint = arrayListOf<MatOfPoint>()
        Imgproc.findContours(dilateMat, contoursMatOfPoint, Mat(),
                Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)

        var maxContourIndex = -1
        var maxContourArea = 0.0
        contoursMatOfPoint.forEachIndexed { i, matOfPoint ->
            Imgproc.contourArea(matOfPoint).apply {
                if (this > maxContourArea) {
                    maxContourArea = this
                    maxContourIndex = i
                }
            }
        }

        /**
         * Draw contour with largest area
         */
        Imgproc.drawContours(rgba, contoursMatOfPoint, maxContourIndex,
                color2scalar(R.color.colorAccent), 2)

        return rgba
    }

    private fun color2scalar(@ColorRes resId: Int): Scalar {
        val color = context.resources.getColor(resId, context.theme)
        return Scalar(Color.red(color).toDouble(), Color.green(color).toDouble(), Color.blue(color).toDouble())
    }

    private fun toggleScanCamera2View() {
        if (isScanCamera2ViewEnabled) {
            disableScanCamera2View()
        } else {
            enableScanCamera2View()
        }
    }

    private fun enableScanCamera2View() {
        if (!isScanCamera2ViewEnabled) {
            mScanCamera2View?.enableView()
            isScanCamera2ViewEnabled = true
        }
    }

    private fun disableScanCamera2View() {
        if (isScanCamera2ViewEnabled) {
            mScanCamera2View?.disableView()
            isScanCamera2ViewEnabled = false
        }
    }

    override fun onResume() {
        super.onResume()
        enableScanCamera2View()
    }

    override fun onPause() {
        super.onPause()
        disableScanCamera2View()
    }

}
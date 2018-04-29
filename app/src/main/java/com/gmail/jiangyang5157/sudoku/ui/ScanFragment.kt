package com.gmail.jiangyang5157.sudoku.ui

import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.jiangyang5157.sudoku.R
import com.gmail.jiangyang5157.sudoku.widget.scan.ScanCamera2View
import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Scalar
import org.opencv.core.Size
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
        val rgba = inputFrame.rgba().clone()
        Imgproc.cvtColor(rgba, rgba, Imgproc.COLOR_RGBA2BGR)
        return handleCameraFrame(rgba, inputFrame.gray())
    }

    private fun handleCameraFrame(rgba: Mat, gray: Mat): Mat {

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
         * Find contour with largest area
         */
        val contoursMatOfPoint = arrayListOf<MatOfPoint>()
        Imgproc.findContours(adaptiveThresholdMat, contoursMatOfPoint, Mat(),
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
            mScanCamera2View?.enableFpsMeter()
            mScanCamera2View?.enableView()
            isScanCamera2ViewEnabled = true
        }
    }

    private fun disableScanCamera2View() {
        if (isScanCamera2ViewEnabled) {
            mScanCamera2View?.disableFpsMeter()
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
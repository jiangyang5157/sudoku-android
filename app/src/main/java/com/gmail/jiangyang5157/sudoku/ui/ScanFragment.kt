package com.gmail.jiangyang5157.sudoku.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.jiangyang5157.sudoku.R
import com.gmail.jiangyang5157.sudoku.widget.ScanCamera2View
import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * Created by Yang Jiang on April 21, 2018
 */
class ScanFragment : Fragment(), CameraBridgeViewBase.CvCameraViewListener2 {

    companion object {
        const val TAG = "ScanFragment"
    }

    private var mScanCamera2View: ScanCamera2View? = null
    private var isScanCamera2ViewEnabled = false

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
    }

    override fun onCameraViewStopped() {
        Log.d(TAG, "onCameraViewStopped")
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
        val rgba = inputFrame.rgba().clone()
        Imgproc.cvtColor(rgba, rgba, Imgproc.COLOR_RGBA2BGR)
        return rgba
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

//    private fun debug(filename: String) {
//
//        /**
//         *
//         */
//        val srcBm = BitmapFactory.decodeStream(context.assets.open(filename))
//        val srcMat = Mat()
//        Utils.bitmapToMat(srcBm, srcMat, false)
//        debugShowInfo("Original image", srcMat)
//
//        /**
//         * We don't want to bother with the colour information, so just skip it
//         */
//        val grayScaledMat = Mat()
//        Imgproc.cvtColor(srcMat, grayScaledMat, Imgproc.COLOR_BGR2GRAY)
//        debugShowInfo("Gray scaled, BGR2GRAY", null)
//
//        /**
//         * This smooths out the noise a bit and makes extracting the grid lines easier
//         */
//        val blurMat = Mat()
//        Imgproc.GaussianBlur(grayScaledMat, blurMat,
//                Size(5.0, 5.0), 0.0, 0.0)
//        debugShowInfo("GaussianBlur: (5, 5), (0, 0)", null)
//
//        /**
//         * It calculates a mean over a 5x5 window and subtracts 2 from the mean.
//         * This is the threshold level for every pixel.
//         */
//        val adaptiveThresholdMat = Mat()
//        Imgproc.adaptiveThreshold(blurMat, adaptiveThresholdMat,
//                255.0, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY_INV, 5, 2.0)
//        debugShowInfo("AdaptiveThreshold: 255, MEAN_C, BINARY_INV, 5, 2", adaptiveThresholdMat)
//
//        /**
//         * Thresholding operation can disconnect certain connected parts (like lines).
//         * So dilating the image once will fill up any small "cracks" that might have crept in.
//         *
//         * 0.0, 1.0, 0.0,
//         * 1.0, 1.0, 1.0,
//         * 0.0, 1.0, 0.0
//         */
//        val dilateMat = Mat()
//        val dilationSize = 0.5
//        val kernelMat = Imgproc.getStructuringElement(
//                Imgproc.MORPH_CROSS,
//                Size(2 * dilationSize + 1, 2 * dilationSize + 1),
//                Point(dilationSize, dilationSize))
//        Imgproc.dilate(adaptiveThresholdMat, dilateMat, kernelMat)
//        debugShowInfo("Dilate: MORPH_CROSS, 0.5", dilateMat)
//
//        /**
//         *
//         */
//        val contoursMatOfPoint = arrayListOf<MatOfPoint>()
//        Imgproc.findContours(dilateMat, contoursMatOfPoint, Mat(),
//                Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)
//        val contourMat = dilateMat.clone()
//        Imgproc.cvtColor(contourMat, contourMat, Imgproc.COLOR_GRAY2BGR)
//        var maxContourIndex = -1
//        var maxContourArea = 0.0
//        contoursMatOfPoint.forEachIndexed { i, matOfPoint ->
//            Imgproc.contourArea(matOfPoint).apply {
//                if (this > maxContourArea) {
//                    maxContourArea = this
//                    maxContourIndex = i
//                }
//            }
//        }
//        Imgproc.drawContours(contourMat, contoursMatOfPoint, maxContourIndex,
//                Scalar(255.0, 0.0, 0.0), 2)
//        debugShowInfo("DrawContours: RETR_EXTERNAL, APPROX_SIMPLE", contourMat)
//    }
//
//    private fun debugShowInfo(title: String, mat: Mat?) {
//        debugLinearLayout.addView(TextView(context).apply {
//            text = title
//        })
//        mat?.apply {
//            debugLinearLayout.addView(ImageView(context).apply {
//                setImageBitmap(Bitmap.createBitmap(
//                        mat.cols(), mat.rows(), Bitmap.Config.RGB_565).apply {
//                    Utils.matToBitmap(mat, this, false)
//                })
//            }, LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    600
//            ))
//        }
//    }

}
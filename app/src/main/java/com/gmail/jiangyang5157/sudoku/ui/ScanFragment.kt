package com.gmail.jiangyang5157.sudoku.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.gmail.jiangyang5157.sudoku.R
import org.opencv.android.Utils
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
class ScanFragment : Fragment() {

    private lateinit var debugScrollView: ScrollView
    private lateinit var debugLinearLayout: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view?.apply {
            debugScrollView = findViewById(R.id.debug_scrollview) as ScrollView
            debugLinearLayout = findViewById(R.id.debug_linearlayout) as LinearLayout

            debug("puzzle_paper.jpg")
            debug("puzzle_rotation.jpg")
            debug("puzzle_rotated_curve.jpg")
        }
    }

    private fun debug(filename: String) {

        /**
         *
         */
        val srcBm = BitmapFactory.decodeStream(context.assets.open(filename))
        val srcMat = Mat()
        Utils.bitmapToMat(srcBm, srcMat, false)
        debugShowInfo("Original image", srcMat)

        /**
         * We don't want to bother with the colour information, so just skip it
         */
        val grayScaledMat = Mat()
        Imgproc.cvtColor(srcMat, grayScaledMat, Imgproc.COLOR_BGR2GRAY)
        debugShowInfo("Gray scaled, BGR2GRAY", null)

        /**
         * This smooths out the noise a bit and makes extracting the grid lines easier
         */
        val blurMat = Mat()
        Imgproc.GaussianBlur(grayScaledMat, blurMat,
                Size(5.0, 5.0), 0.0, 0.0)
        debugShowInfo("GaussianBlur: (5, 5), (0, 0)", null)

        /**
         * It calculates a mean over a 5x5 window and subtracts 2 from the mean.
         * This is the threshold level for every pixel.
         */
        val adaptiveThresholdMat = Mat()
        Imgproc.adaptiveThreshold(blurMat, adaptiveThresholdMat,
                255.0, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY_INV, 5, 2.0)
        debugShowInfo("AdaptiveThreshold: 255, MEAN_C, BINARY_INV, 5, 2", adaptiveThresholdMat)

        /**
         * Thresholding operation can disconnect certain connected parts (like lines).
         * So dilating the image once will fill up any small "cracks" that might have crept in.
         *
         * 0.0, 1.0, 0.0,
         * 1.0, 1.0, 1.0,
         * 0.0, 1.0, 0.0
         */
//        val dilateMat = Mat()
//        val dilationSize = 0.5
//        val kernelMat = Imgproc.getStructuringElement(
//                Imgproc.MORPH_CROSS,
//                Size(2 * dilationSize + 1, 2 * dilationSize + 1),
//                Point(dilationSize, dilationSize))
//        Imgproc.dilate(adaptiveThresholdMat, dilateMat, kernelMat)
//        debugShowInfo("Dilate: MORPH_CROSS, 0.5", dilateMat)

        /**
         *
         */
        val contoursMatOfPoint = arrayListOf<MatOfPoint>()
        Imgproc.findContours(adaptiveThresholdMat, contoursMatOfPoint, Mat(),
                Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)
        val contourMat = adaptiveThresholdMat.clone()
        Imgproc.cvtColor(contourMat, contourMat, Imgproc.COLOR_GRAY2BGR)
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
        Imgproc.drawContours(contourMat, contoursMatOfPoint, maxContourIndex,
                Scalar(255.0, 0.0, 0.0), 2)
        debugShowInfo("DrawContours: RETR_EXTERNAL, APPROX_SIMPLE", contourMat)


    }

    private fun debugShowInfo(title: String, mat: Mat?) {
        debugLinearLayout.addView(TextView(context).apply {
            text = title
        })
        mat?.apply {
            debugLinearLayout.addView(ImageView(context).apply {
                setImageBitmap(Bitmap.createBitmap(
                        mat.cols(), mat.rows(), Bitmap.Config.RGB_565).apply {
                    Utils.matToBitmap(mat, this, false)
                })
            }, LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    600
            ))
        }
    }

}
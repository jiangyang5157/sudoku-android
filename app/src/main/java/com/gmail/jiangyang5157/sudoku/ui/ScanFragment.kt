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
import org.opencv.imgproc.Imgproc

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

            debug("puzzle_2.png")
        }
    }

    private fun debug(filename: String) {

        /**
         *
         */
        debugLinearLayout.addView(TextView(context).apply {
            text = "original image..."
        })
        val srcBm = BitmapFactory.decodeStream(context.assets.open(filename))
        val srcMat = Mat()
        Utils.bitmapToMat(srcBm, srcMat)
        debugDisplayNewMat(srcMat)

        /**
         *
         */
        debugLinearLayout.addView(TextView(context).apply {
            text = "Gray scaled..."
        })
        val grayScaledMat = Mat()
        Imgproc.cvtColor(srcMat, grayScaledMat,
                Imgproc.COLOR_BGR2GRAY)
        debugDisplayNewMat(grayScaledMat)

        /**
         *
         */
//        debugLinearLayout.addView(TextView(context).apply {
//            text = "AdaptiveThreshold..."
//        })
//        val adaptiveThresholdMat = Mat()
//        Imgproc.adaptiveThreshold(grayScaledMat, adaptiveThresholdMat,
//                255.0,
//                ADAPTIVE_THRESH_GAUSSIAN_C , THRESH_BINARY_INV, 3, 5.0)
//        debugDisplayNewMat(adaptiveThresholdMat)

        /**
         *
         */
        debugLinearLayout.addView(TextView(context).apply {
            text = "Canny..."
        })
        val cannyMat = Mat()
        Imgproc.Canny(grayScaledMat, cannyMat,
                127.0, 255.0, 3, true)
        debugDisplayNewMat(cannyMat)


    }

    private fun debugDisplayNewMat(mat : Mat) {
        val adaptiveThresholdBm = Bitmap.createBitmap(
                mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(mat, adaptiveThresholdBm)
        debugLinearLayout.addView(ImageView(context).apply {
            setImageBitmap(adaptiveThresholdBm)
        })
        debugScrollView.fullScroll(View.FOCUS_DOWN)
    }

}
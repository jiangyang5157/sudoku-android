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

            debug("debug_puzzle.png")
        }
    }

    private fun debug(filename: String) {
        val srcBm: Bitmap
        val srcMat: Mat

        /** 1. **/
        debugLinearLayout.addView(TextView(context).apply {
            text = "original image..."
        })

        srcBm = BitmapFactory.decodeStream(context.assets.open(filename))
        srcMat = Mat()
        Utils.bitmapToMat(srcBm, srcMat)
        debugLinearLayout.addView(ImageView(context).apply {
            setImageBitmap(srcBm)
        })
        debugScrollView.fullScroll(View.FOCUS_DOWN)

        /** 2. **/
        debugLinearLayout.addView(TextView(context).apply {
            text = "Gray scaled..."
        })

        val grayScaledMat = Mat()
        Imgproc.cvtColor(srcMat, grayScaledMat, Imgproc.COLOR_BGR2GRAY)
        val grayScaledBm = Bitmap.createBitmap(grayScaledMat.cols(), grayScaledMat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(grayScaledMat, grayScaledBm)

        debugLinearLayout.addView(ImageView(context).apply {
            setImageBitmap(grayScaledBm)
        })
        debugScrollView.fullScroll(View.FOCUS_DOWN)


        /** 3. **/
        debugLinearLayout.addView(TextView(context).apply {
            text = "Canny..."
        })

        val cannyMat = Mat()
        Imgproc.Canny(grayScaledMat, cannyMat, 10.toDouble(), 100.toDouble(), 3, true)
        val cannyBm = Bitmap.createBitmap(cannyMat.cols(), cannyMat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(cannyMat, cannyBm)

        debugLinearLayout.addView(ImageView(context).apply {
            setImageBitmap(cannyBm)
        })
        debugScrollView.fullScroll(View.FOCUS_DOWN)

        /** 4. **/

    }

}
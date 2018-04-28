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
        val dstBm: Bitmap
        val srcMat: Mat
        val dstMat: Mat

        /** 1. **/
        debugLinearLayout.addView(TextView(context).apply {
            text = "original image..."
        })
        srcBm = BitmapFactory.decodeStream(context.assets.open(filename))
        debugLinearLayout.addView(ImageView(context).apply {
            setImageBitmap(srcBm)
        })
        debugScrollView.fullScroll(View.FOCUS_DOWN)

        /** 2. **/
        debugLinearLayout.addView(TextView(context).apply {
            text = "Gray scaled..."
        })

        srcMat = Mat()
        dstMat = Mat()
        Utils.bitmapToMat(srcBm, srcMat)
        Imgproc.cvtColor(srcMat, dstMat, Imgproc.COLOR_BGR2GRAY)
        dstBm = Bitmap.createBitmap(dstMat.cols(), dstMat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dstMat, dstBm)

        debugLinearLayout.addView(ImageView(context).apply {
            setImageBitmap(dstBm)
        })
        debugScrollView.fullScroll(View.FOCUS_DOWN)


    }

}
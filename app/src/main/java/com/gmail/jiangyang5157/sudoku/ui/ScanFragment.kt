package com.gmail.jiangyang5157.sudoku.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.gmail.jiangyang5157.kotlin_kit.render.FPSValidation
import com.gmail.jiangyang5157.sudoku.R
import com.gmail.jiangyang5157.sudoku.widget.scan.Camera2CvView
import com.gmail.jiangyang5157.sudoku.widget.scan.Camera2CvViewBase
import com.gmail.jiangyang5157.sudoku.widget.scan.imgproc.*
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc

/**
 * Created by Yang Jiang on April 30, 2018
 */
class ScanFragment : Fragment(), Camera2CvViewBase.Camera2CvViewListener {

    companion object {
        const val TAG = "ScanFragment"
    }

    private var mIvSnapshot: ImageView? = null
    private var mCamera2CvView: Camera2CvView? = null
    private var isSnapshotEnabled = false
    private var isCamera2ViewEnabled = false

    private val mProcessorFps = FPSValidation(-1)
    private lateinit var scalarAccent: Scalar
    private var mRgba: Mat? = null
    private var mGray: Mat? = null
    private var width = 0
    private var height = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_scan, container, false)
        scalarAccent = color2scalar(R.color.colorAccent)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view?.apply {
            mIvSnapshot = findViewById(R.id.iv_snapshot) as ImageView?
            mCamera2CvView = findViewById(R.id.view_camera2cv) as Camera2CvView
            mCamera2CvView?.setCvCameraViewListener(this@ScanFragment)
            findViewById(R.id.btn_toggle).setOnClickListener { toggleScan() }
        }
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        Log.d(TAG, "onCameraViewStarted: ${width}x$height")
        this.width = width
        this.height = height
        mRgba = Mat(height, width, CvType.CV_8UC4)
        mGray = Mat(height, width, CvType.CV_8UC1)
    }

    override fun onCameraViewStopped() {
        Log.d(TAG, "onCameraViewStopped")
        mRgba?.release()
        mGray?.release()
    }

    override fun onCameraFrame(inputFrame: Camera2CvViewBase.CvCameraViewFrame): Mat {
        mRgba = inputFrame.rgba()
        mGray = inputFrame.gray()
        Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_RGB2BGR)

        if (mProcessorFps.accept()) {
            handleFrame(mGray!!)
        }

        return mGray!!
    }

    private val mGaussianBlur = GaussianBlur(5.0, 5.0, 0.0, 0.0)
    private val mAdaptiveThreshold = AdaptiveThreshold(255.0, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 2.0)
    private val mCrossDilate = CrossDilate(0.5)
    private val mCanny = Canny(127.0, 255.0, 3)
    private val mDrawContour = DrawContour(Color.RED, 2)
    private val mFindContours = FindContours(Imgproc.RETR_EXTERNAL)
    private val mMaxAreaContourFilter = MaxAreaContourFilter

    private fun handleFrame(gray: Mat): Mat {
        val blurMat = mGaussianBlur.convert(gray)

        val adaptiveThresholdMat = mAdaptiveThreshold.convert(blurMat)
        blurMat.release()

        val dilateMat = mCrossDilate.convert(adaptiveThresholdMat)
        adaptiveThresholdMat.release()

//        val canny = mCanny.convert(dilateMat)
//        dilateMat.release()

        val contours = mFindContours.find(dilateMat)
        dilateMat.release()

        val maxAreaContour = mMaxAreaContourFilter.filter(contours)
        maxAreaContour?.apply {
            mDrawContour.draw(gray, maxAreaContour)
        }
        contours.forEach { it.release() }

        return gray
    }

    private fun color2scalar(@ColorRes resId: Int): Scalar {
        val color = context.resources.getColor(resId, context.theme)
        return Scalar(Color.red(color).toDouble(), Color.green(color).toDouble(), Color.blue(color).toDouble())
    }

    private fun toggleScan() {
        if (isSnapshotEnabled) {
            disableSnapshot()
        } else {
            enableSnapshot()
        }
        if (isCamera2ViewEnabled) {
            disableScanCamera2View()
        } else {
            enableScanCamera2View()
        }
    }

    private fun enableScanCamera2View() {
        if (!isCamera2ViewEnabled) {
            mCamera2CvView?.enableView()
            isCamera2ViewEnabled = true
        }
    }

    private fun disableScanCamera2View() {
        if (isCamera2ViewEnabled) {
            mCamera2CvView?.disableView()
            isCamera2ViewEnabled = false
        }
    }

    private fun enableSnapshot() {
        if (!isSnapshotEnabled) {
            mCamera2CvView?.apply {
                mIvSnapshot?.imageMatrix = this.cacheMatrix
                mIvSnapshot?.setImageBitmap(Bitmap.createBitmap(this.cacheBitmap))
                mIvSnapshot?.visibility = View.VISIBLE
                isSnapshotEnabled = true
            }
        }
    }

    private fun disableSnapshot() {
        if (isSnapshotEnabled) {
            mIvSnapshot?.visibility = View.GONE
            isSnapshotEnabled = false
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isSnapshotEnabled) {
            enableScanCamera2View()
        }
    }

    override fun onPause() {
        super.onPause()
        disableScanCamera2View()
    }

}

package com.gmail.jiangyang5157.sudoku.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.app.Fragment
import android.support.v7.widget.SwitchCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.gmail.jiangyang5157.kotlin_android_kit.ext.layoutInflater
import com.gmail.jiangyang5157.sudoku.R
import com.gmail.jiangyang5157.sudoku.widget.scan.Camera2CvView
import com.gmail.jiangyang5157.sudoku.widget.scan.Camera2CvViewBase
import com.gmail.jiangyang5157.sudoku.widget.scan.imgproc.*
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

    private lateinit var scalarAccent: Scalar
    private var mRgba: Mat? = null
    private var mGray: Mat? = null
    private var width = 0
    private var height = 0

    private val mGaussianBlur = GaussianBlur(5.0, 5.0, 0.0, 0.0)
    private val mAdaptiveThreshold = AdaptiveThreshold(255.0, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 2.0)
    private val mCrossDilate = CrossDilate(0.5)
    private val mCanny = Canny(127.0, 255.0, 3)
    private val mFindContours = FindContours(Imgproc.RETR_EXTERNAL)
    private val mMaxAreaContourFilter = MaxAreaContourFilter
    private val mDrawContour = DrawContour(Color.RED, 2)

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
            findViewById(R.id.btn_scan).setOnClickListener { scanToggle() }

            setup_debug_panel(view)
        }
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        Log.d(TAG, "onCameraViewStarted: ${width}x$height")
        this.width = width
        this.height = height
    }

    override fun onCameraViewStopped() {
        Log.d(TAG, "onCameraViewStopped")
    }

    override fun onCameraFrame(inputFrame: Camera2CvViewBase.CvCameraViewFrame): Mat {
        mRgba?.release()
        mGray?.release()

//        mRgba = inputFrame.rgba()
//        Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_RGB2BGR)
        mRgba = Mat()
        mGray = inputFrame.gray()

        mGray = frameproc(mGray!!)
        Imgproc.cvtColor(mGray, mRgba, Imgproc.COLOR_GRAY2RGB)

        val contours = mFindContours.find(mGray!!)
        val maxAreaContour = mMaxAreaContourFilter.filter(contours)
        maxAreaContour?.apply {
            mDrawContour.draw(mRgba!!, maxAreaContour)
        }
        contours.forEach { it.release() }

        return mRgba!!
    }

    private fun frameproc(gray: Mat): Mat {
        var last = gray

        if (debug_enable_GaussianBlur) {
            val gaussianBlurMat = mGaussianBlur.convert(last)
            last.release()
            last = gaussianBlurMat
        }

        if (debug_enable_AdaptiveThreshold) {
            val adaptiveThresholdMat = mAdaptiveThreshold.convert(last)
            last.release()
            last = adaptiveThresholdMat
        }

        if (debug_enable_CrossDilate) {
            val crossDilateMat = mCrossDilate.convert(last)
            last.release()
            last = crossDilateMat
        }

        if (debug_enable_Canny) {
            val cannyMat = mCanny.convert(last)
            last.release()
            last = cannyMat
        }

        return last
    }

    private fun color2scalar(@ColorRes resId: Int): Scalar {
        val color = context.resources.getColor(resId, context.theme)
        return Scalar(Color.red(color).toDouble(), Color.green(color).toDouble(), Color.blue(color).toDouble())
    }

    private fun scanToggle() {
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

    /* ################ DEBUG ################ */
    private var debug_enable_GaussianBlur = false
    private var debug_enable_AdaptiveThreshold = false
    private var debug_enable_CrossDilate = false
    private var debug_enable_Canny = false

    private fun setup_debug_panel(view: View) {
        val debug_panel_container = view.findViewById(R.id.debug_panel_container) as ViewGroup
        val debug_panel_gaussian_blur = context.layoutInflater.inflate(R.layout.debug_panel_gaussian_blur, debug_panel_container, false)
        val debug_panel_adaptive_threshold = context.layoutInflater.inflate(R.layout.debug_panel_adaptive_threshold, debug_panel_container, false)
        val debug_panel_cross_dilate = context.layoutInflater.inflate(R.layout.debug_panel_cross_dilate, debug_panel_container, false)
        val debug_panel_canny = context.layoutInflater.inflate(R.layout.debug_panel_canny, debug_panel_container, false)

        view.findViewById(R.id.debug_btn_gaussian_blur).setOnClickListener {
            if (debug_panel_gaussian_blur.parent == null) {
                debug_panel_container.removeAllViews()
                debug_panel_container.addView(debug_panel_gaussian_blur)
            } else {
                debug_panel_container.removeAllViews()
            }
        }
        view.findViewById(R.id.debug_btn_adaptive_threshold).setOnClickListener {
            if (debug_panel_adaptive_threshold.parent == null) {
                debug_panel_container.removeAllViews()
                debug_panel_container.addView(debug_panel_adaptive_threshold)
            } else {
                debug_panel_container.removeAllViews()
            }
        }
        view.findViewById(R.id.debug_btn_cross_dilate).setOnClickListener {
            if (debug_panel_cross_dilate.parent == null) {
                debug_panel_container.removeAllViews()
                debug_panel_container.addView(debug_panel_cross_dilate)
            } else {
                debug_panel_container.removeAllViews()
            }
        }
        view.findViewById(R.id.debug_btn_canny).setOnClickListener {
            if (debug_panel_canny.parent == null) {
                debug_panel_container.removeAllViews()
                debug_panel_container.addView(debug_panel_canny)
            } else {
                debug_panel_container.removeAllViews()
            }
        }

        /*
        Gaussian Blur
        */
        val debug_sb_guassion_blur_ksize = debug_panel_gaussian_blur.findViewById(R.id.debug_sb_guassion_blur_ksize) as SeekBar
        val debug_sb_guassion_blur_sigma = debug_panel_gaussian_blur.findViewById(R.id.debug_sb_guassion_blur_sigma) as SeekBar
        (debug_panel_gaussian_blur.findViewById(R.id.debug_cb_guassian_blur) as CheckBox)
                .setOnCheckedChangeListener { _, isChecked ->
                    debug_enable_GaussianBlur = isChecked
                }
        val guassion_blur_ksize_step = 2.0
        val guassion_blur_ksize_min = 1.0
        val guassion_blur_ksize_max = 21.0
        debug_sb_guassion_blur_ksize.max = ((guassion_blur_ksize_max - guassion_blur_ksize_min) / guassion_blur_ksize_step).toInt()
        debug_sb_guassion_blur_ksize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = guassion_blur_ksize_min + progress.toDouble() * guassion_blur_ksize_step
                (debug_panel_gaussian_blur.findViewById(R.id.debug_tv_guassion_blur_ksize) as TextView).text = value.toString()
                mGaussianBlur.kWidth = value
                mGaussianBlur.kHeight = value
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        debug_sb_guassion_blur_ksize.progress = 2
        val guassion_blur_sigma_step = 0.1
        val guassion_blur_sigma_min = 0.0
        val guassion_blur_sigma_max = 20.0
        debug_sb_guassion_blur_sigma.max = ((guassion_blur_sigma_max - guassion_blur_sigma_min) / guassion_blur_sigma_step).toInt()
        debug_sb_guassion_blur_sigma.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = guassion_blur_sigma_min + progress.toDouble() * guassion_blur_sigma_step
                (debug_panel_gaussian_blur.findViewById(R.id.debug_tv_guassion_blur_sigma) as TextView).text = value.toString()
                mGaussianBlur.sigmaX = value
                mGaussianBlur.sigmaY = value
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        debug_sb_guassion_blur_sigma.progress = 0

        /*
        Adaptive Threshold
        */
        val debug_sb_adaptive_threshold_block_size = debug_panel_adaptive_threshold.findViewById(R.id.debug_sb_adaptive_threshold_block_size) as SeekBar
        val debug_sb_adaptive_threshold_c = debug_panel_adaptive_threshold.findViewById(R.id.debug_sb_adaptive_threshold_c) as SeekBar
        (debug_panel_adaptive_threshold.findViewById(R.id.debug_cb_adaptive_threshold) as CheckBox)
                .setOnCheckedChangeListener { _, isChecked ->
                    debug_enable_AdaptiveThreshold = isChecked
                }
        val adaptive_threshold_block_size_step = 2
        val adaptive_threshold_block_size_min = 3
        val adaptive_threshold_block_size_max = 41
        debug_sb_adaptive_threshold_block_size.max = ((adaptive_threshold_block_size_max - adaptive_threshold_block_size_min) / adaptive_threshold_block_size_step)
        debug_sb_adaptive_threshold_block_size.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = adaptive_threshold_block_size_min + progress * adaptive_threshold_block_size_step
                (debug_panel_adaptive_threshold.findViewById(R.id.debug_tv_adaptive_threshold_block_size) as TextView).text = value.toString()
                mAdaptiveThreshold.blockSize = value
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        debug_sb_adaptive_threshold_block_size.progress = 1
        val adaptive_threshold_c_step = 0.1
        val adaptive_threshold_c_min = 0.0
        val adaptive_threshold_c_max = 10.0
        debug_sb_adaptive_threshold_c.max = ((adaptive_threshold_c_max - adaptive_threshold_c_min) / adaptive_threshold_c_step).toInt()
        debug_sb_adaptive_threshold_c.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = adaptive_threshold_c_min + progress.toDouble() * adaptive_threshold_c_step
                (debug_panel_adaptive_threshold.findViewById(R.id.debug_tv_adaptive_threshold_c) as TextView).text = value.toString()
                mAdaptiveThreshold.c = value
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        debug_sb_adaptive_threshold_c.progress = 20

        /*
        Cross Dilate
        */
        val debug_sb_cross_dilate_dilation_size = debug_panel_cross_dilate.findViewById(R.id.debug_sb_cross_dilate_dilation_size) as SeekBar
        (debug_panel_cross_dilate.findViewById(R.id.debug_cb_cross_dilate) as CheckBox)
                .setOnCheckedChangeListener { _, isChecked ->
                    debug_enable_CrossDilate = isChecked
                }
        val cross_dilate_dilation_size_step = 0.1
        val cross_dilate_dilation_size_min = 0.0
        val cross_dilate_dilation_size_max = 10.0
        debug_sb_cross_dilate_dilation_size.max = (((cross_dilate_dilation_size_max - cross_dilate_dilation_size_min) / cross_dilate_dilation_size_step).toInt())
        debug_sb_cross_dilate_dilation_size.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = cross_dilate_dilation_size_min + progress.toDouble() * cross_dilate_dilation_size_step
                (debug_panel_cross_dilate.findViewById(R.id.debug_tv_cross_dilate_dilation_size) as TextView).text = value.toString()
                mCrossDilate.dilationSize = value
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        debug_sb_cross_dilate_dilation_size.progress = 5

        /*
        Canny
        */
        val debug_sb_canny_threshold1 = debug_panel_canny.findViewById(R.id.debug_sb_canny_threshold1) as SeekBar
        val debug_sb_canny_threshold2 = debug_panel_canny.findViewById(R.id.debug_sb_canny_threshold2) as SeekBar
        val debug_sb_canny_aperture_size = debug_panel_canny.findViewById(R.id.debug_sb_canny_aperture_size) as SeekBar
        val debug_sc_canny_l2gradient = debug_panel_canny.findViewById(R.id.debug_sc_canny_l2gradient) as SwitchCompat
        (debug_panel_canny.findViewById(R.id.debug_cb_canny) as CheckBox)
                .setOnCheckedChangeListener { _, isChecked ->
                    debug_enable_Canny = isChecked
                }
        val canny_threshold1_step = 1.0
        val canny_threshold1_min = 0.0
        val canny_threshold1_max = 255.0
        debug_sb_canny_threshold1.max = (((canny_threshold1_max - canny_threshold1_min) / canny_threshold1_step).toInt())
        debug_sb_canny_threshold1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = canny_threshold1_min + progress.toDouble() * canny_threshold1_step
                (debug_panel_canny.findViewById(R.id.debug_tv_canny_threshold1) as TextView).text = value.toString()
                mCanny.threshold1 = value
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        debug_sb_canny_threshold1.progress = 127
        val canny_threshold2_step = 1.0
        val canny_threshold2_min = 0.0
        val canny_threshold2_max = 255.0
        debug_sb_canny_threshold2.max = (((canny_threshold2_max - canny_threshold2_min) / canny_threshold2_step).toInt())
        debug_sb_canny_threshold2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = canny_threshold2_min + progress.toDouble() * canny_threshold2_step
                (debug_panel_canny.findViewById(R.id.debug_tv_canny_threshold2) as TextView).text = value.toString()
                mCanny.threshold2 = value
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        debug_sb_canny_threshold2.progress = 255
        val canny_apertureSize_step = 2
        val canny_apertureSize_min = 3
        val canny_apertureSize_max = 7
        debug_sb_canny_aperture_size.max = (((canny_apertureSize_max - canny_apertureSize_min) / canny_apertureSize_step))
        debug_sb_canny_aperture_size.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = canny_apertureSize_min + progress * canny_apertureSize_step
                (debug_panel_canny.findViewById(R.id.debug_tv_canny_aperture_size) as TextView).text = value.toString()
                mCanny.apertureSize = value
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        debug_sb_canny_aperture_size.progress = 0
        debug_sc_canny_l2gradient.setOnCheckedChangeListener { _, isChecked ->
            mCanny.l2gradient = isChecked
        }
    }

}

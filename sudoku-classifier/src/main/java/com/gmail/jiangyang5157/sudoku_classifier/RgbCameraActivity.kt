package com.gmail.jiangyang5157.sudoku_classifier

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Trace
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Size
import android.view.WindowManager
import android.widget.Toast
import com.gmail.jiangyang5157.kotlin_android_kit.ext.instance
import com.gmail.jiangyang5157.kotlin_android_kit.ext.replaceFragmentInActivity
import com.gmail.jiangyang5157.kotlin_kit.render.Renderable

/**
 * Created by Yang Jiang on May 06, 2018
 */
class RgbCameraActivity : AppCompatActivity(), Camera2Fragment.Callback, ImageReader.OnImageAvailableListener {

    companion object {
        const val TAG = "RgbCameraActivity"
        const val TAG_HANDLER_THREAD = "RgbCameraActivity_Handler_Thread"

        const val PERMISSION_CAMERA = Manifest.permission.CAMERA
        const val REQUEST_PERMISSION = 1
    }

    private var mBackgroundHandler: Handler? = null
    private var mBackgroundThread: HandlerThread? = null

    private var mImageConverter: Runnable? = null
    private var mImageCloser: Runnable? = null

    private val DESIRED_SIZE = Size(640, 480)
    private var mIsProcessingFrame = false
    private val mYuvBytes = arrayOfNulls<ByteArray?>(3)
    private var mRgbBytes: IntArray? = null
    private var mYRowStride: Int = 0

    private var previewWidth = 0
    private var previewHeight = 0
    private var rgbFrameBitmap: Bitmap? = null
    private var croppedBitmap: Bitmap? = null
    private var cropCopyBitmap: Bitmap? = null
    private var frameToCropTransform: Matrix? = null
    private val INPUT_SIZE = 224
    private val MAINTAIN_ASPECT = true

    private var mOverlayView: OverlayView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_rgb_camera)

        if (hasPermission()) {
            setContent()
        } else {
            requestPermission()
        }
    }

    private fun hasPermission(): Boolean {
        return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA)) {
            AlertDialog.Builder(this)
                    .setMessage("Request Permission")
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        requestPermissions(arrayOf(PERMISSION_CAMERA), REQUEST_PERMISSION)
                    }
                    .setNegativeButton(android.R.string.cancel) { _, _ ->
                        finish()
                    }
                    .create().show()
        } else {
            requestPermissions(arrayOf(PERMISSION_CAMERA), REQUEST_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.size != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                requestPermission()
            } else {
                setContent()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun isHardwareLevelSupported(characteristics: CameraCharacteristics, requiredLevel: Int): Boolean {
        val deviceLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
                ?: return false
        return if (deviceLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
            requiredLevel == deviceLevel
        } else {
            requiredLevel <= deviceLevel
        }
    }

    private fun isCamera2Supported(characteristics: CameraCharacteristics): Boolean {
        val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
        return facing == CameraCharacteristics.LENS_FACING_EXTERNAL
                || isHardwareLevelSupported(characteristics, CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL)
    }

    private fun isFrontCamera(characteristics: CameraCharacteristics): Boolean {
        val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
        return facing == CameraCharacteristics.LENS_FACING_FRONT
    }

    private fun setContent() {
        val cameraId = chooseCamera()
        if (cameraId == null) {
            Toast.makeText(this, "No Camera Detected", Toast.LENGTH_SHORT).show()
            finish()
        }

        val camera2Fragment = instance<Camera2Fragment>(Bundle().apply {
            putString(Camera2Fragment.KEY_CAMERA_ID, cameraId)
            putSize(Camera2Fragment.KEY_DESIRED_SIZE, DESIRED_SIZE)
        }) as Camera2Fragment
        camera2Fragment.setCallback(this)
        camera2Fragment.setOnImageAvailableListener(this)
        replaceFragmentInActivity(R.id.camera_container, camera2Fragment)

        mOverlayView = findViewById(R.id.view_overlay) as OverlayView?
    }

    private fun chooseCamera(): String? {
        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            for (cameraId in manager.cameraIdList) {
                val characteristics = manager.getCameraCharacteristics(cameraId)
                characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        ?: continue
                return cameraId
            }
        } catch (e: CameraAccessException) {
            Log.d(TAG, "Choose camera error: Not allowed to access camera $e")
        }

        return null
    }

    private fun startBackgroundThread() {
        mBackgroundThread = HandlerThread(TAG_HANDLER_THREAD).apply {
            start()
        }
        mBackgroundHandler = Handler(mBackgroundThread?.looper)
    }

    private fun stopBackgroundThread() {
        try {
            mBackgroundThread?.apply {
                quitSafely()
                join()
            }
            mBackgroundThread = null
            mBackgroundHandler = null
        } catch (e: InterruptedException) {
            Log.w(TAG, "Stop background thread: InterruptedException $e")
        }
    }

    private fun runInBackground(r: Runnable) {
        mBackgroundHandler?.apply {
            post(r)
        }
    }

    public override fun onResume() {
        super.onResume()
        startBackgroundThread()
    }

    public override fun onPause() {
        if (!isFinishing) {
            Log.d(TAG, "onPause: Requesting finish")
            finish()
        }
        stopBackgroundThread()
        super.onPause()
    }


    override fun onPreviewSizeChosen(size: Size, cameraRotation: Int, screenRotation: Int) {
        previewWidth = size.width
        previewHeight = size.height
        Log.d(TAG, "Initializing at size $previewWidth, $previewHeight")

        val rotation = cameraRotation - screenRotation
        Log.d(TAG, "Camera orientation relative to screen canvas: $rotation")

        mRgbBytes = IntArray(previewWidth * previewHeight)
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888)
        croppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Bitmap.Config.ARGB_8888)

        frameToCropTransform = ImageUtils.getTransformationMatrix(
                previewWidth, previewHeight,
                INPUT_SIZE, INPUT_SIZE,
                rotation,
                MAINTAIN_ASPECT)
        frameToCropTransform?.invert(Matrix())

        mOverlayView?.addRenderable(object : Renderable<Canvas> {
            override fun onRender(t: Canvas) {
                renderDebug(t)
            }
        })
    }

    override fun onImageAvailable(reader: ImageReader?) {
        if (previewWidth == 0 || previewHeight == 0) {
            return
        }

        try {
            val image = reader?.acquireLatestImage() ?: return

            if (mIsProcessingFrame) {
                image.close()
                return
            }
            mIsProcessingFrame = true

            Trace.beginSection("imageAvailable")
            val planes = image.planes

            for (i in planes.indices) {
                val buffer = planes[i].buffer
                if (mYuvBytes[i] == null) {
                    Log.d(TAG, "Initializing buffer " + i + " at size " + buffer.capacity())
                    mYuvBytes[i] = ByteArray(buffer.capacity())
                }
                buffer.get(mYuvBytes[i])
            }

            mYRowStride = planes[0].rowStride
            val uvRowStride = planes[1].rowStride
            val uvPixelStride = planes[1].pixelStride

            mImageConverter = Runnable {
                ImageUtils.convertYUV420ToARGB8888(
                        mYuvBytes[0],
                        mYuvBytes[1],
                        mYuvBytes[2],
                        previewWidth,
                        previewHeight,
                        mYRowStride,
                        uvRowStride,
                        uvPixelStride,
                        mRgbBytes)
            }

            mImageCloser = Runnable {
                image.close()
                mIsProcessingFrame = false
            }

            processImage()
        } catch (e: Exception) {
            Log.d(TAG, "onImageAvailable: Exception $e")
        }

        Trace.endSection()
    }

    private fun processImage() {
        mImageConverter?.run()
        rgbFrameBitmap?.setPixels(mRgbBytes, 0, previewWidth, 0, 0, previewWidth, previewHeight)
        val canvas = Canvas(croppedBitmap)
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null)

        runInBackground(Runnable {
            cropCopyBitmap = Bitmap.createBitmap(croppedBitmap)
            mOverlayView?.postInvalidate()
            mImageCloser?.run()
        })
    }

    private fun renderDebug(canvas: Canvas) {
        val copy = cropCopyBitmap
        if (copy != null) {
            val matrix = Matrix()
            val scaleFactor = 2f
            matrix.postScale(scaleFactor, scaleFactor)
            matrix.postTranslate(
                    canvas.width - copy.width * scaleFactor,
                    canvas.height - copy.height * scaleFactor)
            canvas.drawBitmap(copy, matrix, Paint())
        }
    }

}
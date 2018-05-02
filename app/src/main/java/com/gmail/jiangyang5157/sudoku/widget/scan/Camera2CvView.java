package com.gmail.jiangyang5157.sudoku.widget.scan;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.WindowManager;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.nio.ByteBuffer;
import java.util.Collections;

/**
 * Created by Yang Jiang on April 29, 2018
 * <p>
 * This class is copied from openCVLibrary341 {@link org.opencv.android.JavaCamera2View} to optimize for a better frame rate.
 */

/**
 * This class is an implementation of the Bridge View between OpenCV and Java Camera.
 * This class relays on the functionality available in base class and only implements
 * required functions:
 * connectCamera - opens Java camera and sets the PreviewCallback to be delivered.
 * disconnectCamera - closes the camera and stops preview.
 * When frame is delivered via callback from Camera - it processed via OpenCV to be
 * converted to RGBA32 and then passed to the external callback for modifications if required.
 */
public class Camera2CvView extends Camera2CvViewBase {

    private static final String TAG = "ScanCamera2View";

    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCaptureSession;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private String mCameraID;

    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;

    private ImageReader mImageReader;
    private int mPreviewFormat = ImageFormat.YUV_420_888;

    protected int mSensorOrientation = 0;
    protected int mDisplayRotation = 0;
    private Size mPreviewSize = new Size(-1, -1);
    protected float mScale = 1;

    public Camera2CvView(Context context) {
        super(context);
    }

    public Camera2CvView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Camera2CvView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Camera2CvView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void startBackgroundThread() {
        stopBackgroundThread();
        mBackgroundThread = new HandlerThread("Camera2CvViewBackgroundThread");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        if (mBackgroundThread == null)
            return;
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            Log.e(TAG, "stopBackgroundThread", e);
        }
    }

    protected boolean initializeCamera() {
        Log.i(TAG, "Initialize camera");
        CameraManager manager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
        try {
            assert manager != null;
            String camList[] = manager.getCameraIdList();
            if (camList.length == 0) {
                Log.e(TAG, "Error: camera isn't detected.");
                return false;
            }

            CameraCharacteristics characteristics = null;
            for (String cameraID : camList) {
                characteristics = manager.getCameraCharacteristics(cameraID);
                if (mCameraIndex == Camera2CvViewBase.CAMERA_ID_ANY) {
                    mCameraID = cameraID;
                    break;
                } else if (mCameraIndex == Camera2CvViewBase.CAMERA_ID_BACK) {
                    if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                        mCameraID = cameraID;
                        break;
                    }
                } else if (mCameraIndex == Camera2CvViewBase.CAMERA_ID_FRONT) {
                    if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                        mCameraID = cameraID;
                        break;
                    }
                }
            }

            if (mCameraID != null) {
                mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                mDisplayRotation = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
                Log.i(TAG, "Opening camera: " + mCameraID);
                manager.openCamera(mCameraID, mStateCallback, mBackgroundHandler);
            }
            return true;
        } catch (CameraAccessException e) {
            Log.e(TAG, "OpenCamera - Camera Access Exception", e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "OpenCamera - Illegal Argument Exception", e);
        } catch (SecurityException e) {
            Log.e(TAG, "OpenCamera - Security Exception", e);
        }
        return false;
    }

    private int rotationDegree() {
        switch (mDisplayRotation) {
            case Surface.ROTATION_0:
                return (90 + mSensorOrientation + 270) % 360;
            case Surface.ROTATION_90:
                return (mSensorOrientation + 270) % 360;
            case Surface.ROTATION_180:
                return (270 + mSensorOrientation + 270) % 360;
            case Surface.ROTATION_270:
                return (180 + mSensorOrientation + 270) % 360;
            default:
                throw new IllegalArgumentException("Unknown display rotation: " + mDisplayRotation);
        }
    }

    /**
     * Determines if the dimensions are swapped given the phone's current rotation.
     *
     * @return true if the dimensions are swapped, false otherwise.
     */
    private boolean areDimensionsSwapped() {
        boolean swappedDimensions = false;
        switch (mDisplayRotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                    swappedDimensions = true;
                }
                break;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                    swappedDimensions = true;
                }
                break;
            default:
                Log.e(TAG, "Display rotation is invalid: " + mDisplayRotation);
                break;
        }
        return swappedDimensions;
    }

    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error) {
            cameraDevice.close();
            mCameraDevice = null;
        }

    };

    private void createCameraPreviewSession() {
        final int w = mPreviewSize.getWidth();
        final int h = mPreviewSize.getHeight();
        Log.i(TAG, "createCameraPreviewSession(" + w + "x" + h + ")");

        if (w < 0 || h < 0)
            return;
        try {
            if (null == mCameraDevice) {
                Log.e(TAG, "createCameraPreviewSession: camera isn't opened");
                return;
            }
            if (null != mCaptureSession) {
                Log.e(TAG, "createCameraPreviewSession: mCaptureSession is already started");
                return;
            }

            mImageReader = ImageReader.newInstance(w, h, mPreviewFormat, 2);
            mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = reader.acquireLatestImage();
                    if (image == null) {
                        return;
                    }

                    Image.Plane[] planes = image.getPlanes();
                    // see also https://developer.android.com/reference/android/graphics/ImageFormat.html#YUV_420_888
                    // Y plane (0) non-interleaved => stride == 1; U/V plane interleaved => stride == 2
//                    assert (image.getFormat() == mPreviewFormat);
//                    assert (planes.length == 3);
//                    assert (planes[0].getPixelStride() == 1);
//                    assert (planes[1].getPixelStride() == 2);
//                    assert (planes[2].getPixelStride() == 2);

                    ByteBuffer y_plane = planes[0].getBuffer();
                    ByteBuffer uv_plane = planes[1].getBuffer();
                    Mat y_mat = new Mat(h, w, CvType.CV_8UC1, y_plane);
                    Mat uv_mat = new Mat(h / 2, w / 2, CvType.CV_8UC2, uv_plane);
                    Camera2CvFrame tempFrame = new Camera2CvFrame(mPreviewFormat, y_mat, uv_mat, w, h);
                    deliverAndDrawFrame(tempFrame);
                    tempFrame.release();
                    image.close();
                }
            }, mBackgroundHandler);
            Surface surface = mImageReader.getSurface();

            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            mCameraDevice.createCaptureSession(Collections.singletonList(surface),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            if (null == mCameraDevice) {
                                return; // camera is already closed
                            }
                            mCaptureSession = cameraCaptureSession;
                            try {
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

                                mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), null, mBackgroundHandler);
                                Log.i(TAG, "CameraPreviewSession has been started");
                            } catch (Exception e) {
                                Log.e(TAG, "createCaptureSession failed", e);
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                            Log.e(TAG, "createCameraPreviewSession failed");
                        }
                    },
                    null
            );
        } catch (CameraAccessException e) {
            Log.e(TAG, "createCameraPreviewSession", e);
        }
    }

    @Override
    protected void disconnectCamera() {
        Log.i(TAG, "Disconnect camera");
        try {
            CameraDevice c = mCameraDevice;
            mCameraDevice = null;
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != c) {
                c.close();
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
            }
        } finally {
            stopBackgroundThread();
        }
    }

    private Size calcBestPreviewSize(final Size[] sizes, int minWidth, float aspect, float aspectError) {
        int currWidth = sizes[0].getWidth();
        int currHeight = sizes[0].getHeight();

        for (Size sz : sizes) {
            int w = sz.getWidth(), h = sz.getHeight();
            Log.d(TAG, "Trying size: " + w + "x" + h);
            if (minWidth <= w && w <= currWidth && Math.abs(aspect - (float) w / h) < aspectError) {
                currWidth = w;
                currHeight = h;
                Log.i(TAG, "Apply size: " + currWidth + "x" + currHeight);
            }
        }
        if (currWidth < 0 || currHeight < 0) {
            return sizes[0];
        } else {
            return new Size(currWidth, currHeight);
        }
    }

    /* TODO WHY?
   Nexus 6p：
   With    statusbar, calcPreviewSize: 2392x1356 : 1.764
   Without statusbar, calcPreviewSize: 2392x1440 : 1.661

       trying size: 4032x3024 : 1.333 Half+  <<<<<<<<<<<<<<<<
       trying size: 4000x3000 : 1.333 Failed
       trying size: 3840x2160 : 1.777 Full   <<<<<<<<<<<<<<<<
       trying size: 3264x2448 : 1.333 Half+  <<<<<<<<<<<<<<<<
       trying size: 3200x2400 : 1.333 Half+  <<<<<<<<<<<<<<<<
       trying size: 2976x2976 : 1.000 Failed
       trying size: 2592x1944 : 1.333 Failed
       trying size: 2688x1512 : 1.777 Full   <<<<<<<<<<<<<<<<
       trying size: 2048x1536 : 1.333 Failed
       trying size: 1920x1080 : 1.777 Full   <<<<<<<<<<<<<<<<
       trying size: 1600x1200 : 1.333 Half+  <<<<<<<<<<<<<<<<
       trying size: 1440x1080 : 1.333 Failed
       trying size: 1280x960  : 1.333 Failed
       trying size: 1280x768  : 1.666 Failed
       trying size: 1280x720  : 1.777 Full   <<<<<<<<<<<<<<<<
       trying size: 1024x768  : 1.333 Failed
       trying size: 800x600   : 1.333 Failed
       trying size: 864x480   : 1.800 Failed
       trying size: 800x480   : 1.666 Failed
       trying size: 720x480   : 1.500 Failed
       trying size: 640x480   : 1.333 Half+  <<<<<<<<<<<<<<<<
       trying size: 640x360   : 1.777 Full   <<<<<<<<<<<<<<<<
       trying size: 352x288   : 1.222 Failed
   */
    boolean calcPreviewSize(final int width, final int height) {
        if (mCameraID == null) {
            Log.e(TAG, "Camera isn't initialized!");
            return false;
        }

        CameraManager manager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
        try {
            int minWidth = 600;
            float aspectError = 0.02f;
            float displayAspect;
            Point displaySize = new Point();
            ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(displaySize);
            Log.i(TAG, "displaySize: " + displaySize);
            boolean swappedDimensions = areDimensionsSwapped();
            Log.i(TAG, "swappedDimensions: " + swappedDimensions);
            if (swappedDimensions) {
                displayAspect = (float) displaySize.y / displaySize.x;
            } else {
                displayAspect = (float) displaySize.x / displaySize.y;
            }
            float aspect = Math.abs(1.333 - displayAspect) < Math.abs(1.777 - displayAspect) ? 1.333f : 1.777f;

            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraID);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size[] sizes = map.getOutputSizes(ImageReader.class);
            Size bestSize = calcBestPreviewSize(sizes, minWidth, aspect, aspectError);
            Log.i(TAG, "Best size: " + bestSize.toString());

            if (mPreviewSize.equals(bestSize)) {
                return false;
            } else {
                mPreviewSize = bestSize;
                if (swappedDimensions) {
                    mScale = Math.min(((float) height) / bestSize.getWidth(), ((float) width) / bestSize.getHeight());
                } else {
                    mScale = Math.min(((float) height) / bestSize.getHeight(), ((float) width) / bestSize.getWidth());
                }
                return true;
            }
        } catch (CameraAccessException e) {
            Log.e(TAG, "calcPreviewSize - Camera Access Exception", e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "calcPreviewSize - Illegal Argument Exception", e);
        } catch (SecurityException e) {
            Log.e(TAG, "calcPreviewSize - Security Exception", e);
        }
        return false;
    }

    @Override
    protected boolean connectCamera(int width, int height) {
        Log.i(TAG, "connectCamera(" + width + "x" + height + ")");
        startBackgroundThread();
        initializeCamera();

        try {
            boolean needReconfig = calcPreviewSize(width, height);
            AllocateCache(width, height,
                    mPreviewSize.getWidth(), mPreviewSize.getHeight(),
                    mPreviewSize.getWidth(), mPreviewSize.getHeight(),
                    mScale, rotationDegree());
            if (needReconfig) {
                if (null != mCaptureSession) {
                    Log.d(TAG, "closing existing previewSession");
                    mCaptureSession.close();
                    mCaptureSession = null;
                }
                createCameraPreviewSession();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Interrupted while setCameraPreviewSize.", e);
        }
        return true;
    }

}

package com.gmail.jiangyang5157.sudoku.widget.scan;

import android.content.Context;
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

    private int mPreviewWidth = -1;
    private int mPreviewHeight = -1;
    protected int mSensorOrientation = 0;
    protected int mDisplayRotation = 0;
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
        if (mBackgroundThread == null) {
            return;
        }
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            Log.e(TAG, "stopBackgroundThread", e);
        }
    }

    protected boolean openCamera() {
        Log.i(TAG, "Opening camera");
        CameraManager manager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
        assert manager != null;
        try {
            String camList[] = manager.getCameraIdList();
            if (camList.length == 0) {
                Log.e(TAG, "Open camera - camera isn't detected.");
                return false;
            }
            mCameraID = camList[0];
            Log.d(TAG, "Camera ID: " + mCameraID);
            if (mCameraID != null) {
                manager.openCamera(mCameraID, mStateCallback, mBackgroundHandler);
            }
            return true;
        } catch (CameraAccessException e) {
            Log.e(TAG, "Open camera - Camera Access Exception", e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Open camera - Illegal Argument Exception", e);
        } catch (SecurityException e) {
            Log.e(TAG, "Open camera - Security Exception", e);
        }
        return false;
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
        final int w = mPreviewWidth;
        final int h = mPreviewHeight;

        Log.i(TAG, "createCameraPreviewSession: " + w + "_" + h + ")");
        if (w < 0 || h < 0) {
            Log.d(TAG, "createCameraPreviewSession: preview size is not ready");
            return;
        }

        try {
            if (null == mCameraDevice) {
                Log.e(TAG, "createCameraPreviewSession: camera isn't opened");
                return;
            }
            if (null != mCaptureSession) {
                Log.e(TAG, "createCameraPreviewSession: mCaptureSession is already started");
                return;
            }

            mImageReader = ImageReader.newInstance(w, h, Camera2CvFrame.IMAGE_FORMAT, 2);
            mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = reader.acquireLatestImage();
                    if (image == null) {
                        return;
                    }

                    Image.Plane[] planes = image.getPlanes();
                    ByteBuffer y_plane = planes[0].getBuffer();
                    ByteBuffer uv_plane = planes[1].getBuffer();
                    Mat y_mat = new Mat(h, w, CvType.CV_8UC1, y_plane);
                    Mat uv_mat = new Mat(h / 2, w / 2, CvType.CV_8UC2, uv_plane);

                    Camera2CvFrame tempFrame = new Camera2CvFrame(y_mat, uv_mat, w, h);
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
                                Log.d(TAG, "camera is already closed");
                                return;
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
                    null);
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
        Log.d(TAG, "calcPreviewSize: " + width + "_" + height);
        CameraManager manager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
        assert manager != null;

        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraID);
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            mDisplayRotation = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();

            int minWidth = 600;
            float aspectError = 0.02f;
            float displayAspect;

            Point displaySize = new Point();
            ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(displaySize);
            Log.i(TAG, "displaySize: " + displaySize);

            boolean swappedDimensions = areDimensionsSwapped(mSensorOrientation, mDisplayRotation);
            Log.i(TAG, "swappedDimensions: " + swappedDimensions);

            if (swappedDimensions) {
                displayAspect = (float) displaySize.y / displaySize.x;
            } else {
                displayAspect = (float) displaySize.x / displaySize.y;
            }
            float aspect = Math.abs(1.333 - displayAspect) < Math.abs(1.777 - displayAspect) ? 1.333f : 1.777f;
            Log.i(TAG, "aspect: " + aspect);

            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size[] sizes = map.getOutputSizes(ImageReader.class);

            Size bestSize = calcBestPreviewSize(sizes, minWidth, aspect, aspectError);
            Log.i(TAG, "Best size: " + bestSize.toString());

            if (swappedDimensions) {
                mScale = Math.min(((float) height) / bestSize.getWidth(), ((float) width) / bestSize.getHeight());
            } else {
                mScale = Math.min(((float) height) / bestSize.getHeight(), ((float) width) / bestSize.getWidth());
            }
            Log.i(TAG, "Scale: " + mScale);

            if (mPreviewWidth != bestSize.getWidth() || mPreviewHeight != bestSize.getHeight()) {
                mPreviewWidth = bestSize.getWidth();
                mPreviewHeight = bestSize.getHeight();
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
    protected void connectCamera(int width, int height) {
        Log.i(TAG, "Connect camera: " + width + "_" + height);
        startBackgroundThread();
        openCamera();

        boolean needReconfig = calcPreviewSize(width, height);
        if (needReconfig) {
            if (null != mCaptureSession) {
                Log.d(TAG, "closing existing previewSession");
                mCaptureSession.close();
                mCaptureSession = null;
            }
            createCameraPreviewSession();
        }

        AllocateCache(
                width, height,
                mPreviewWidth, mPreviewHeight,
                mScale,
                rotationDegree(mSensorOrientation, mDisplayRotation));
    }
}

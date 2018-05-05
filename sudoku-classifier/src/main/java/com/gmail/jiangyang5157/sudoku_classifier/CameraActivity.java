package com.gmail.jiangyang5157.sudoku_classifier;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import java.nio.ByteBuffer;

public abstract class CameraActivity extends AppCompatActivity implements CameraFragment.ConnectionCallback {
    public static final String TAG = "CameraActivity";
    public static final String TAG_HANDLER_THREAD = "CameraActivity_HANDLER_THREAD";

    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final int REQUEST_PERMISSION = 1;

    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private Runnable mImageCloser;
    private Runnable mImageConverter;

    private boolean mIsProcessingFrame = false;
    private byte[][] mYuvBytes = new byte[3][];
    private int[] mRgbBytes = null;
    private int mYRowStride;

    protected int previewWidth = 0;
    protected int previewHeight = 0;

    protected CameraFragment mCameraFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.d("####", "onCreate " + this);
        super.onCreate(null);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.container_activity);

        if (hasPermission()) {
            setContent();
        } else {
            requestPermission();
        }
    }

    private boolean hasPermission() {
        return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA)) {
            new AlertDialog.Builder(this)
                    .setMessage("Request Permission")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{PERMISSION_CAMERA}, REQUEST_PERMISSION);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create().show();
        } else {
            requestPermissions(new String[]{PERMISSION_CAMERA}, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            } else {
                setContent();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean isHardwareLevelSupported(CameraCharacteristics characteristics, int requiredLevel) {
        Integer deviceLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        if (deviceLevel == null) {
            return false;
        }
        if (deviceLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
            return requiredLevel == deviceLevel;
        }
        return requiredLevel <= deviceLevel;
    }

    private String chooseCamera() {
        final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        assert manager != null;

        try {
            for (final String cameraId : manager.getCameraIdList()) {
                final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                // We don't use a front facing camera in this sample.
                final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                final StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                // Fallback to camera1 API for internal cameras that don't have full support.
                // This should help with legacy situations where using the camera2 API causes
                // distorted or otherwise broken previews.
                boolean canUseCamera2 = (facing == CameraCharacteristics.LENS_FACING_EXTERNAL)
                        || isHardwareLevelSupported(characteristics, CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL);
                Log.d(TAG, "canUseCamera2: " + canUseCamera2);
                return cameraId;
            }
        } catch (CameraAccessException e) {
            Log.d(TAG, "Not allowed to access camera");
        }
        return null;
    }

    protected void setContent() {
        final String cameraId = chooseCamera();
        if (cameraId == null) {
            Toast.makeText(this, "No Camera Detected", Toast.LENGTH_SHORT).show();
            finish();
        }

        mCameraFragment =
                CameraFragment.newInstance(
                        cameraId,
                        new CameraFragment.ConnectionCallback() {
                            @Override
                            public void onPreviewSizeChosen(final Size size, final int rotation) {
                                previewHeight = size.getHeight();
                                previewWidth = size.getWidth();
                                CameraActivity.this.onPreviewSizeChosen(size, rotation);
                            }
                        },
                        mOnImageAvailableListener,
                        getDesiredPreviewSize());

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_container, mCameraFragment)
                .commit();
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread(TAG_HANDLER_THREAD);
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (final InterruptedException e) {
            Log.w(TAG, "stopBackgroundThread: InterruptedException!");
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        startBackgroundThread();
    }

    @Override
    public synchronized void onPause() {
        if (!isFinishing()) {
            Log.d(TAG, "onPause: Requesting finish");
            finish();
        }
        stopBackgroundThread();
        super.onPause();
    }

    protected synchronized void runInBackground(final Runnable r) {
        if (mBackgroundHandler != null) {
            mBackgroundHandler.post(r);
        }
    }

    protected int getScreenOrientation() {
        switch (getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_270:
                return 270;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_0:
                return 0;
            default:
                return 0;
        }
    }

    //############################################################################################################
    //############################################################################################################
    //############################################################################################################
    //############################################################################################################


    private OnImageAvailableListener mOnImageAvailableListener = new OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            if (previewWidth == 0 || previewHeight == 0) {
                return;
            }

            if (mRgbBytes == null) {
                mRgbBytes = new int[previewWidth * previewHeight];
            }

            try {
                final Image image = reader.acquireLatestImage();
                if (image == null) {
                    return;
                }

                if (mIsProcessingFrame) {
                    image.close();
                    return;
                }
                mIsProcessingFrame = true;

                Trace.beginSection("imageAvailable");
                final Plane[] planes = image.getPlanes();
                fillBytes(planes, mYuvBytes);
                mYRowStride = planes[0].getRowStride();
                final int uvRowStride = planes[1].getRowStride();
                final int uvPixelStride = planes[1].getPixelStride();

                mImageConverter = new Runnable() {
                    @Override
                    public void run() {
                        ImageUtils.convertYUV420ToARGB8888(
                                mYuvBytes[0],
                                mYuvBytes[1],
                                mYuvBytes[2],
                                previewWidth,
                                previewHeight,
                                mYRowStride,
                                uvRowStride,
                                uvPixelStride,
                                mRgbBytes);
                    }
                };

                mImageCloser = new Runnable() {
                    @Override
                    public void run() {
                        image.close();
                        mIsProcessingFrame = false;
                    }
                };

                processImage();
            } catch (final Exception e) {
                Log.d(TAG, "onImageAvailable: Exception");
                Trace.endSection();
                return;
            }
            Trace.endSection();
        }
    };

    protected void fillBytes(final Plane[] planes, final byte[][] yuvBytes) {
        // Because of the variable row stride it's not possible to know in advance the actual necessary dimensions of the yuv planes.
        for (int i = 0; i < planes.length; ++i) {
            final ByteBuffer buffer = planes[i].getBuffer();
            if (yuvBytes[i] == null) {
                Log.d(TAG, "Initializing buffer " + i + " at size " + buffer.capacity());
                yuvBytes[i] = new byte[buffer.capacity()];
            }
            buffer.get(yuvBytes[i]);
        }
    }

    protected int[] getRgbBytes() {
        mImageConverter.run();
        return mRgbBytes;
    }

    protected void closeImage() {
        if (mImageCloser != null) {
            mImageCloser.run();
        }
    }

    protected abstract Size getDesiredPreviewSize();

    protected abstract void processImage();
}

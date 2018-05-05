package com.gmail.jiangyang5157.sudoku_classifier;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.jiangyang5157.kotlin_android_kit.utils.CompareSizesByArea;
import com.gmail.jiangyang5157.kotlin_android_kit.widget.AutoFitTextureView;
import com.gmail.jiangyang5157.kotlin_kit.render.Renderable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CameraFragment extends Fragment {

    public static final String TAG = "CameraFragment";
    public static final String TAG_HANDLER_THREAD = "CameraFragment_HANDLER_THREAD";

    public interface ConnectionCallback {
        void onPreviewSizeChosen(Size size, int cameraRotation);
    }

    private final ConnectionCallback mCameraConnectionCallback;
    private final OnImageAvailableListener mOnImageAvailableListener;
    private ImageReader mPreviewReader;

    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private final String mCameraId;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCaptureSession;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CaptureRequest previewRequest;

    private final Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private static final long TIMEOUT_CAMERA_OPEN_CLOSE_LOCK = 2500;

    /**
     * The camera preview size will be chosen to be the smallest frame by pixel size capable of
     * containing a DESIRED_SIZE x DESIRED_SIZE square.
     */
    private static final int MINIMUM_PREVIEW_SIZE = 320;
    private Size mPreviewSize;
    private final Size mDesiredSize;
    private AutoFitTextureView mAutoFitTextureView;
    private OverlayView mOverlayView;

    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private CameraFragment(
            final String cameraId,
            final ConnectionCallback connectionCallback,
            final OnImageAvailableListener imageAvailableListener,
            final Size mDesiredSize) {
        this.mCameraId = cameraId;
        this.mCameraConnectionCallback = connectionCallback;
        this.mOnImageAvailableListener = imageAvailableListener;
        this.mDesiredSize = mDesiredSize;
    }

    public static CameraFragment newInstance(
            final String cameraId,
            final ConnectionCallback callback,
            final OnImageAvailableListener imageListener,
            final Size desiredSize) {
        return new CameraFragment(cameraId, callback, imageListener, desiredSize);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        mAutoFitTextureView = (AutoFitTextureView) view.findViewById(R.id.textureview_autofit);
        mOverlayView = (OverlayView) view.findViewById(R.id.view_overlay);
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();

        // When the screen is turned off and turned back on,
        // the SurfaceTexture is already available, and "onSurfaceTextureAvailable" will not be called.
        // In that case, we can open a camera and start preview from here.
        // (otherwise, we wait until the surface is ready in the SurfaceTextureListener)
        if (mAutoFitTextureView.isAvailable()) {
            openCamera(mAutoFitTextureView.getWidth(), mAutoFitTextureView.getHeight());
        } else {
            mAutoFitTextureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    private final TextureView.SurfaceTextureListener surfaceTextureListener =
            new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(final SurfaceTexture texture, final int width, final int height) {
                    openCamera(width, height);
                }

                @Override
                public void onSurfaceTextureSizeChanged(final SurfaceTexture texture, final int width, final int height) {
                    configureTransform(width, height);
                }

                @Override
                public boolean onSurfaceTextureDestroyed(final SurfaceTexture texture) {
                    return true;
                }

                @Override
                public void onSurfaceTextureUpdated(final SurfaceTexture texture) {
                }
            };

    private final CameraCaptureSession.CaptureCallback captureCallback =
            new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureProgressed(
                        final CameraCaptureSession session,
                        final CaptureRequest request,
                        final CaptureResult partialResult) {
                }

                @Override
                public void onCaptureCompleted(
                        final CameraCaptureSession session,
                        final CaptureRequest request,
                        final TotalCaptureResult result) {
                }
            };

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

    /**
     * {@link android.hardware.camera2.CameraDevice.StateCallback}
     * is called when {@link CameraDevice} changes its state.
     */
    private final CameraDevice.StateCallback stateCallback =
            new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull final CameraDevice cd) {
                    mCameraOpenCloseLock.release();
                    mCameraDevice = cd;
                    createCameraPreviewSession();
                }

                @Override
                public void onDisconnected(final CameraDevice cd) {
                    mCameraOpenCloseLock.release();
                    cd.close();
                    mCameraDevice = null;
                }

                @Override
                public void onError(final CameraDevice cd, final int error) {
                    Log.d(TAG, "stateCallback#onError: " + error);
                    mCameraOpenCloseLock.release();
                    cd.close();
                    mCameraDevice = null;
                    final Activity activity = getActivity();
                    if (null != activity) {
                        activity.finish();
                    }
                }
            };

    /**
     * Creates a new {@link CameraCaptureSession} for camera preview.
     */
    private void createCameraPreviewSession() {
        try {
            final SurfaceTexture texture = mAutoFitTextureView.getSurfaceTexture();
            assert texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            // This is the output Surface we need to start preview.
            final Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            // Create the reader for the preview frames.
            mPreviewReader = ImageReader.newInstance(
                    mPreviewSize.getWidth(), mPreviewSize.getHeight(), ImageFormat.YUV_420_888, 2);

            mPreviewReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);
            mPreviewRequestBuilder.addTarget(mPreviewReader.getSurface());

            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mPreviewReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(final CameraCaptureSession ccs) {
                            if (null == mCameraDevice) {
                                Log.d(TAG, "onConfigured: The camera is already closed");
                                return;
                            }

                            // When the session is ready, we start displaying the preview.
                            mCaptureSession = ccs;
                            try {
                                // Auto focus should be continuous for camera preview.
                                mPreviewRequestBuilder.set(
                                        CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                // Flash is automatically enabled when necessary.
                                mPreviewRequestBuilder.set(
                                        CaptureRequest.CONTROL_AE_MODE,
                                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

                                // Finally, we start displaying the camera preview.
                                previewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(
                                        previewRequest, captureCallback, mBackgroundHandler);
                            } catch (final CameraAccessException e) {
                                Log.e(TAG, "onConfigured: CameraAccessException");
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull final CameraCaptureSession ccs) {
                            Log.w(TAG, "onConfigured: CameraAccessException");
                        }
                    },
                    null);
        } catch (final CameraAccessException e) {
            Log.e(TAG, "createCameraPreviewSession: CameraAccessException");
        }
    }

    /**
     * Opens the camera specified by {@link CameraFragment#mCameraId}.
     */
    private void openCamera(final int width, final int height) {
        final Activity activity = getActivity();
        setUpCameraOutputs();
        configureTransform(width, height);
        final CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        assert manager != null;
        try {
            if (!mCameraOpenCloseLock.tryAcquire(TIMEOUT_CAMERA_OPEN_CLOSE_LOCK, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            manager.openCamera(mCameraId, stateCallback, mBackgroundHandler);
        } catch (final CameraAccessException e) {
            Log.e(TAG, "openCamera: CameraAccessException");
        } catch (final InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    /**
     * Closes the current {@link CameraDevice}.
     */
    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mPreviewReader) {
                mPreviewReader.close();
                mPreviewReader = null;
            }
        } catch (final InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    /**
     * @param choices The list of sizes that the camera supports for the intended output class
     * @param width   The minimum desired width
     * @param height  The minimum desired height
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    protected static Size chooseOptimalSize(final Size[] choices, final int width, final int height) {
        final Size desiredSize = new Size(width, height);
        Log.d(TAG, "Desired size: " + desiredSize);
        final int minSize = Math.max(Math.min(width, height), MINIMUM_PREVIEW_SIZE);
        Log.d(TAG, "Min size: " + minSize + "x" + minSize);

        // Collect the supported resolutions that are at least as big as the preview Surface
        boolean exactSizeFound = false;
        final List<Size> bigEnough = new ArrayList<>();
        final List<Size> tooSmall = new ArrayList<>();
        for (final Size option : choices) {
            if (option.equals(desiredSize)) {
                exactSizeFound = true;
            }

            if (option.getHeight() >= minSize && option.getWidth() >= minSize) {
                bigEnough.add(option);
            } else {
                tooSmall.add(option);
            }
        }
        Log.d(TAG, "Valid preview sizes: [" + TextUtils.join(", ", bigEnough) + "]");
        Log.d(TAG, "Rejected preview sizes: [" + TextUtils.join(", ", tooSmall) + "]");

        if (exactSizeFound) {
            Log.d(TAG, "Exact size match found.");
            return desiredSize;
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            final Size chosenSize = Collections.min(bigEnough, new CompareSizesByArea());
            Log.d(TAG, "Chosen size: " + chosenSize.getWidth() + "x" + chosenSize.getHeight());
            return chosenSize;
        } else {
            Log.d(TAG, "Couldn't find any suitable preview size, choose first one from the list of sizes that the camera supports for the intended output class");
            return choices[0];
        }
    }

    /**
     * Sets up member variables related to camera.
     */
    private void setUpCameraOutputs() {
        final Activity activity = getActivity();
        final CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        assert manager != null;

        try {
            final CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);

            final StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;

            mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                    mDesiredSize.getWidth(), mDesiredSize.getHeight());

            final int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mAutoFitTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            } else {
                mAutoFitTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
            }

            Integer sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            assert sensorOrientation != null;
            mCameraConnectionCallback.onPreviewSizeChosen(mPreviewSize, sensorOrientation);
        } catch (final CameraAccessException e) {
            Log.e(TAG, "CameraAccessException");
            activity.finish();
        } catch (final NullPointerException e) {
            Log.e(TAG, "NullPointerException: This device doesn't support Camera2 API.");
            activity.finish();
        }
    }

    /**
     * Configures the necessary {@link android.graphics.Matrix} transformation to {@link TextureView}.
     * This method should be called after the camera preview size is determined in setUpCameraOutputs,
     * and also the size of {@link TextureView} is fixed.
     *
     * @param viewWidth  The width of {@link TextureView}
     * @param viewHeight The height of {@link TextureView}
     */
    private void configureTransform(final int viewWidth, final int viewHeight) {
        final Activity activity = getActivity();
        if (null == mAutoFitTextureView || null == mPreviewSize || null == activity) {
            Log.w(TAG, "configureTransform: null == mAutoFitTextureView || null == mPreviewSize || null == activity");
            return;
        }
        final int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        final Matrix matrix = new Matrix();
        final RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        final RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        final float centerX = viewRect.centerX();
        final float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            final float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mAutoFitTextureView.setTransform(matrix);
    }

    public void addOverlayRenderable(final Renderable<Canvas> callback) {
        mOverlayView.addRenderable(callback);
    }

    public void requestOverlayRender() {
        mOverlayView.postInvalidate();
    }
}

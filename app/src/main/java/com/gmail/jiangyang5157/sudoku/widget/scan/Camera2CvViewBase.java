package com.gmail.jiangyang5157.sudoku.widget.scan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

/**
 * Created by Yang Jiang on April 30, 2018
 * <p>
 * This class is copied from openCVLibrary341 {@link org.opencv.android.CameraBridgeViewBase} and modified.
 */
public abstract class Camera2CvViewBase extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "Camera2CvViewBase";

    private Camera2CvViewListener mListener;

    private final Object mSyncObject = new Object();

    private boolean mSurfaceExist;
    private boolean mEnabled;

    private static final int STOPPED = 0;
    private static final int STARTED = 1;
    private int mState = STOPPED;

    private int mCacheFrameWidth;
    private int mCacheFrameHeight;
    private Bitmap mCacheBitmap;
    private Matrix mCacheMatrix;

    public Camera2CvViewBase(Context context) {
        super(context);
        init();
    }

    public Camera2CvViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Camera2CvViewBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Camera2CvViewBase(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
    }

    public interface Camera2CvViewListener {
        /**
         * This method is invoked when camera preview has started. After this method is invoked
         * the frames will start to be delivered to client via the onCameraFrame() callback.
         *
         * @param width  The width of the frames that will be delivered
         * @param height The height of the frames that will be delivered
         */
        void onCameraViewStarted(int width, int height);

        /**
         * This method is invoked when camera preview has been stopped for some reason.
         * No frames will be delivered via onCameraFrame() callback after this method is called.
         */
        void onCameraViewStopped();

        /**
         * This method is invoked when delivery of the frame needs to be done.
         * The returned values - is a modified frame which needs to be displayed on the screen.
         */
        Mat onCameraFrame(CvCameraViewFrame inputFrame);
    }

    /**
     * This class interface is abstract representation of single frame from camera for onCameraFrame callback
     * Attention: Do not use objects, that represents this interface out of onCameraFrame callback!
     */
    public interface CvCameraViewFrame {

        /**
         * This method returns RGBA Mat with frame
         */
        Mat rgba();

        /**
         * This method returns single channel gray scale Mat with frame
         */
        Mat gray();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        synchronized (mSyncObject) {
            if (!mSurfaceExist) {
                mSurfaceExist = true;
                checkCurrentState();
            } else {
                /* Surface changed. We need to stop camera and restart with new parameters */
                // Pretend that old surface has been destroyed
                mSurfaceExist = false;
                checkCurrentState();
                // Now use new surface. Say we have it now
                mSurfaceExist = true;
                checkCurrentState();
            }
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        /* Do nothing. Wait until surfaceChanged delivered */
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (mSyncObject) {
            mSurfaceExist = false;
            checkCurrentState();
        }
    }

    /**
     * This method is provided for clients, so they can enable the camera connection.
     * The actual onCameraViewStarted callback will be delivered only after both this method is called and surface is available
     */
    public void enableView() {
        synchronized (mSyncObject) {
            mEnabled = true;
            checkCurrentState();
        }
    }

    /**
     * This method is provided for clients, so they can disable camera connection and stop
     * the delivery of frames even though the surface view itself is not destroyed and still stays on the scren
     */
    public void disableView() {
        synchronized (mSyncObject) {
            mEnabled = false;
            checkCurrentState();
        }
    }

    public void setCvCameraViewListener(Camera2CvViewListener listener) {
        mListener = listener;
    }

    /**
     * Called when mSyncObject lock is held
     */
    private void checkCurrentState() {
        int targetState;

        if (mEnabled && mSurfaceExist && getVisibility() == VISIBLE) {
            targetState = STARTED;
        } else {
            targetState = STOPPED;
        }

        if (targetState != mState) {
            /* The state change detected. Need to exit the current state and enter target state */
            processExitState(mState);
            mState = targetState;
            processEnterState(mState);
        }
    }

    private void processEnterState(int state) {
        switch (state) {
            case STARTED:
                onEnterStartedState();
                if (mListener != null) {
                    mListener.onCameraViewStarted(mCacheFrameWidth, mCacheFrameHeight);
                }
                break;
            case STOPPED:
                onEnterStoppedState();
                if (mListener != null) {
                    mListener.onCameraViewStopped();
                }
                break;
        }
    }

    private void processExitState(int state) {
        switch (state) {
            case STARTED:
                onExitStartedState();
                break;
            case STOPPED:
                onExitStoppedState();
                break;
        }
    }

    private void onEnterStartedState() {
        connectCamera(getWidth(), getHeight());
    }

    private void onEnterStoppedState() {
        /* nothing to do */
    }

    private void onExitStartedState() {
        disconnectCamera();
        if (mCacheBitmap != null) {
            mCacheBitmap.recycle();
        }
    }

    private void onExitStoppedState() {
        /* nothing to do */
    }

    /**
     * This method shall be called by the subclasses when they have valid
     * object and want it to be delivered to external client (via callback) and
     * then displayed on the screen.
     *
     * @param frame - the current frame to be delivered
     */
    protected void drawFrame(CvCameraViewFrame frame) {
        Mat modified;
        if (mListener != null) {
            modified = mListener.onCameraFrame(frame);
        } else {
            modified = frame.rgba();
        }

        if (modified != null) {
            try {
                Utils.matToBitmap(modified, mCacheBitmap);
            } catch (Exception e) {
                Log.e(TAG, "Mat type: " + modified);
                Log.e(TAG, "Bitmap type: " + mCacheFrameWidth + "*" + mCacheFrameHeight);
                Log.e(TAG, "Utils.matToBitmap() throws an exception: " + e.getMessage());
                return;
            }
        }

        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            if (canvas != null) {
                canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
                canvas.drawBitmap(mCacheBitmap, mCacheMatrix, null);
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    /**
     * This method is invoked shall perform concrete operation to initialize the camera.
     *
     * @param width  - the width of this SurfaceView
     * @param height - the height of this SurfaceView
     */
    protected abstract void connectCamera(int width, int height);

    /**
     * Disconnects and release the particular camera object being connected to this surface view.
     * Called when syncObject lock is held
     */
    protected abstract void disconnectCamera();

    protected void allocateCache(int frameWidth, int frameHeight, float scale, int rotation) {
        Log.d(TAG, "AllocateCache: " + "frameWidth_frameHeight=" + frameWidth + "_" + frameHeight + ", " + "scale=" + scale + ", " + "rotation=" + rotation);
        mCacheFrameWidth = frameWidth;
        mCacheFrameHeight = frameHeight;
        mCacheBitmap = Bitmap.createBitmap(frameWidth, frameHeight, Bitmap.Config.ARGB_8888);
        float left = (getWidth() - scale * frameWidth) / 2;
        float top = (getHeight() - scale * frameHeight) / 2;
        float right = left + scale * frameWidth;
        float bottom = top + scale * frameHeight;

        mCacheMatrix = new Matrix();
        mCacheMatrix.setScale(scale, scale);
        mCacheMatrix.setRectToRect(
                new RectF(0, 0, frameWidth, frameHeight),
                new RectF(left, top, right, bottom),
                Matrix.ScaleToFit.FILL);
        mCacheMatrix.postRotate(rotation, getWidth() / 2, getHeight() / 2);
    }

    public int getCacheFrameWidth() {
        return mCacheFrameWidth;
    }

    public int getCacheFrameHeight() {
        return mCacheFrameHeight;
    }

    public Bitmap getCacheBitmap() {
        return mCacheBitmap;
    }

    public Matrix getCacheMatrix() {
        return mCacheMatrix;
    }

}

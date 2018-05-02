package com.gmail.jiangyang5157.sudoku.widget.scan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.opencv.android.FpsMeter;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

/**
 * Created by Yang Jiang on April 30, 2018
 * <p>
 * This class is copied from openCVLibrary341 {@link org.opencv.android.CameraBridgeViewBase} to optimize for a better frame rate.
 */
public abstract class Camera2CvViewBase extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "Camera2CvViewBase";

    private static final int STOPPED = 0;
    private static final int STARTED = 1;
    private int mState = STOPPED;

    public static final int CAMERA_ID_ANY = -1;
    public static final int CAMERA_ID_BACK = 99;
    public static final int CAMERA_ID_FRONT = 98;
    protected int mCameraIndex = CAMERA_ID_ANY;

    protected int mCacheWidth;
    protected int mCacheHeight;
    protected float mCacheCenterX;
    protected float mCacheCenterY;
    protected int mCacheFrameWidth;
    protected int mCacheFrameHeight;
    private Bitmap mCacheBitmap;
    private Rect mCacheSrcRect;
    private Rect mCacheDstRect;
    protected int mCacheRotation = 0;

    private final Object mSyncObject = new Object();

    private Camera2CvViewListener mListener;

    private boolean mSurfaceExist;

    protected boolean mEnabled;

    protected FpsMeter mFpsMeter = null;

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
         * @param width  -  the width of the frames that will be delivered
         * @param height - the height of the frames that will be delivered
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
         * TODO: pass the parameters specifying the format of the frame (BPP, YUV or RGB and etc)
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

    public void enableFpsMeter() {
        if (mFpsMeter == null) {
            mFpsMeter = new FpsMeter();
            mFpsMeter.setResolution(mCacheFrameWidth, mCacheFrameHeight);
        }
    }

    public void disableFpsMeter() {
        mFpsMeter = null;
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

    private void onEnterStoppedState() {
        /* nothing to do */
    }

    private void onExitStoppedState() {
        /* nothing to do */
    }

    private void onEnterStartedState() {
        if (!connectCamera(getWidth(), getHeight())) {
            AlertDialog ad = new AlertDialog.Builder(getContext()).create();
            ad.setCancelable(false);
            ad.setMessage("It seems that you device does not support camera (or it is locked). Application will be closed.");
            ad.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ((Activity) getContext()).finish();
                }
            });
            ad.show();
        }
    }

    private void onExitStartedState() {
        disconnectCamera();
        if (mCacheBitmap != null) {
            mCacheBitmap.recycle();
        }
    }

    /**
     * This method shall be called by the subclasses when they have valid
     * object and want it to be delivered to external client (via callback) and
     * then displayed on the screen.
     *
     * @param frame - the current frame to be delivered
     */
    protected void deliverAndDrawFrame(CvCameraViewFrame frame) {
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
                canvas.save();
                canvas.rotate(mCacheRotation, mCacheCenterX, mCacheCenterY);
                canvas.drawBitmap(mCacheBitmap, mCacheSrcRect, mCacheDstRect, null);
                canvas.restore();
                if (mFpsMeter != null) {
                    mFpsMeter.measure();
                    mFpsMeter.draw(canvas, 20, 30);
                }
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    /**
     * This method is invoked shall perform concrete operation to initialize the camera.
     * CONTRACT: as a result of this method variables mCacheFrameWidth and mCacheFrameHeight MUST be
     * initialized with the size of the Camera frames that will be delivered to external processor.
     *
     * @param width  - the width of this SurfaceView
     * @param height - the height of this SurfaceView
     */
    protected abstract boolean connectCamera(int width, int height);

    /**
     * Disconnects and release the particular camera object being connected to this surface view.
     * Called when syncObject lock is held
     */
    protected abstract void disconnectCamera();

    protected void AllocateCache(int width, int height, int frameWidth, int frameHeight, int previewWidth, int previewHeight, float scale, int rotation) {
        mCacheWidth = width;
        mCacheHeight = height;
        mCacheCenterX = (float) width / 2;
        mCacheCenterY = (float) height / 2;

        mCacheFrameWidth = frameWidth;
        mCacheFrameHeight = frameHeight;

        mCacheBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
        mCacheSrcRect = new Rect(0, 0, previewWidth, previewHeight);
        int left = (int) ((width - scale * previewWidth) / 2);
        int top = (int) ((height - scale * previewHeight) / 2);
        int right = (int) (left + scale * previewWidth);
        int bottom = (int) (top + scale * previewHeight);
        mCacheDstRect = new Rect(left, top, right, bottom);

        mCacheRotation = rotation;

        Log.d(TAG, "AllocateCache: width_height=" + width + "_" + height + ", "
                + "frameWidth_frameHeight=" + frameWidth + "_" + frameHeight + ", "
                + "previewWidth_previewHeight=" + previewWidth + "_" + previewHeight + ", "
                + "scale=" + scale + ", "
                + "rotation=" + rotation);
    }

}

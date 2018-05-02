package com.gmail.jiangyang5157.sudoku.widget.scan;

import android.graphics.ImageFormat;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Yang Jiang on May 01, 2018
 */
public class Camera2CvFrame implements Camera2CvViewBase.CvCameraViewFrame {

    public static final int IMAGE_FORMAT = ImageFormat.YUV_420_888;

    private Mat mYuvFrameData;
    private Mat mUVFrameData;
    private int mWidth;
    private int mHeight;
    private Mat mRgba;

    public Camera2CvFrame(Mat Y, Mat UV, int width, int height) {
        super();
        mYuvFrameData = Y;
        mUVFrameData = UV;
        mWidth = width;
        mHeight = height;
        mRgba = new Mat();
    }

    @Override
    public Mat gray() {
        return mYuvFrameData.submat(0, mHeight, 0, mWidth);
    }

    @Override
    public Mat rgba() {
        Imgproc.cvtColorTwoPlane(mYuvFrameData, mUVFrameData, mRgba, Imgproc.COLOR_YUV2RGBA_NV21);
        return mRgba;
    }

    public void release() {
        mYuvFrameData.release();
        mUVFrameData.release();
        mRgba.release();
    }
}
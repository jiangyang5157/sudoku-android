package com.gmail.jiangyang5157.sudoku.widget.scan;

import android.graphics.ImageFormat;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Yang Jiang on May 01, 2018
 * <p>
 * This class is copied from openCVLibrary341 {@link org.opencv.android.JavaCamera2View.JavaCamera2Frame} and modified.
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
        Mat rgbaNv21 = new Mat();
        Imgproc.cvtColorTwoPlane(mYuvFrameData, mUVFrameData, rgbaNv21, Imgproc.COLOR_YUV2RGBA_NV21);
        Imgproc.cvtColor(rgbaNv21, mRgba, Imgproc.COLOR_RGB2BGR);
        rgbaNv21.release();
        return mRgba;
    }

    public void release() {
        mYuvFrameData.release();
        mUVFrameData.release();
        mRgba.release();
    }
}
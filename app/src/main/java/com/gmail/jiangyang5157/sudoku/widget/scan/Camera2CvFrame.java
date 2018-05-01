package com.gmail.jiangyang5157.sudoku.widget.scan;

import android.graphics.ImageFormat;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Yang Jiang on May 01, 2018
 */
public class Camera2CvFrame implements Camera2CvViewBase.CvCameraViewFrame {

    private int mFormat;
    private Mat mYuvFrameData;
    private Mat mUVFrameData;
    private Mat mRgba;
    private int mWidth;
    private int mHeight;

    public Camera2CvFrame(int format, Mat Y, Mat UV, int width, int height) {
        super();
        mFormat = format;
        mWidth = width;
        mHeight = height;
        mYuvFrameData = Y;
        mUVFrameData = UV;
        mRgba = new Mat();
    }

    @Override
    public Mat gray() {
        return mYuvFrameData.submat(0, mHeight, 0, mWidth);
    }

    @Override
    public Mat rgba() {
        switch (mFormat) {
            case ImageFormat.NV21:
                Imgproc.cvtColor(mYuvFrameData, mRgba, Imgproc.COLOR_YUV2RGBA_NV21, 4);
                break;
            case ImageFormat.YV12:
                Imgproc.cvtColor(mYuvFrameData, mRgba, Imgproc.COLOR_YUV2RGB_I420, 4); // COLOR_YUV2RGBA_YV12 produces inverted colors
                break;
            case ImageFormat.YUV_420_888:
                Imgproc.cvtColorTwoPlane(mYuvFrameData, mUVFrameData, mRgba, Imgproc.COLOR_YUV2RGBA_NV21);
                break;
            default:
                throw new IllegalArgumentException("Preview Format can be NV21 or YV12");
        }
        return mRgba;
    }

    public void release() {
        mYuvFrameData.release();
        mUVFrameData.release();
        mRgba.release();
    }
}
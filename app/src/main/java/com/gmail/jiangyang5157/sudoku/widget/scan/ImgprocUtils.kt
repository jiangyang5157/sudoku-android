package com.gmail.jiangyang5157.sudoku.widget.scan

/**
 * Created by Yang Jiang on May 01, 2018
 */
// TODO
//https://github.com/ErnestWong/SudokuSolver
//https://github.com/prajwalkr/SnapSudoku
//https://stackoverflow.com/questions/9413216/simple-digit-recognition-ocr-in-opencv-python/9620295#9620295
//https://docs.nvidia.com/gameworks/content/technologies/mobile/opencv_tutorial_mixed_processing.htm
//https://docs.opencv.org/3.4.0/d9/df8/tutorial_root.html
//https://github.com/googlesamples
//https://github.com/googlesamples/android-architecture
//https://github.com/googlesamples/android-DisplayingBitmaps
//https://github.com/googlesamples/android-DisplayingBitmaps/blob/master/Application/src/main/java/com/example/android/displayingbitmaps/util/RecyclingBitmapDrawable.java
//https://github.com/googlesamples/android-DisplayingBitmaps/blob/master/Application/src/main/java/com/example/android/displayingbitmaps/ui/RecyclingImageView.java
//https://github.com/googlesamples/android-Camera2Basic/tree/master/Application/src/main/java/com/example/android/camera2basic
//https://github.com/googlesamples/android-Camera2Basic/tree/master/kotlinApp/Application/src/main/java/com/example/android/camera2basic
//https://docs.opencv.org/2.4.3/doc/tutorials/introduction/android_binary_package/dev_with_OCV_on_Android.html
//http://answers.opencv.org/question/1742/how-to-set-camera-resolution-in-opencv-on-android/
//https://searchcode.com/codesearch/view/26664468/

//I am using OpenCV 3.1, I fix it by apply transform when draw bitmap on deliverAndDrawFrame method of CameraBridgeViewBase class, Hope it helpful:
//https://stackoverflow.com/questions/16669779/opencv-camera-orientation-issue

/*
/**
     * Retrieves the JPEG orientation from the specified screen rotation.
     *
     * @param rotation The screen rotation.
     * @return The JPEG orientation (one of 0, 90, 270, and 360)
     */
    private int getOrientation(int rotation) {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    // Find out if we need to swap dimension to get the preview size relative to sensor
                // coordinate.
                int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
                //noinspection ConstantConditions
                mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                boolean swappedDimensions = false;
                switch (displayRotation) {
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
                        Log.e(TAG, "Display rotation is invalid: " + displayRotation);
                }

                /**
     * Configures the necessary {@link android.graphics.Matrix} transformation to `mTextureView`.
     * This method should be called after the camera preview size is determined in
     * setUpCameraOutputs and also the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = getActivity();
        if (null == mTextureView || null == mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }
    */

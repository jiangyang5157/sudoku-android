package com.gmail.jiangyang5157.sudoku_classifier;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.util.Size;

import com.gmail.jiangyang5157.kotlin_kit.render.Renderable;

public class ClassifierActivity extends CameraActivity {
    public static final String TAG = "ClassifierActivity";

    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap = null;
    private Matrix frameToCropTransform;

    private static final boolean MAINTAIN_ASPECT = true;
    private static final int INPUT_SIZE = 224;
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);

    @Override
    protected Size getDesiredPreviewSize() {
        return DESIRED_PREVIEW_SIZE;
    }

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        previewWidth = size.getWidth();
        previewHeight = size.getHeight();
        Log.d(TAG, "Initializing at size " + previewWidth + ", " + previewHeight);

        Integer sensorOrientation = rotation - getScreenOrientation();
        Log.d(TAG, "Camera orientation relative to screen canvas: " + sensorOrientation);

        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
        croppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Config.ARGB_8888);

        frameToCropTransform = ImageUtils.getTransformationMatrix(
                previewWidth, previewHeight,
                INPUT_SIZE, INPUT_SIZE,
                sensorOrientation,
                MAINTAIN_ASPECT);
        frameToCropTransform.invert(new Matrix());

        mCameraFragment.addOverlayRenderable(new Renderable<Canvas>() {
            @Override
            public void onRender(Canvas canvas) {
                renderDebug(canvas);
            }
        });
    }

    @Override
    protected void processImage() {
        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);
        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);

        runInBackground(new Runnable() {
            @Override
            public void run() {
                cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
                mCameraFragment.requestOverlayRender();
                closeImage();
            }
        });
    }

    private void renderDebug(final Canvas canvas) {
        final Bitmap copy = cropCopyBitmap;
        if (copy != null) {
            final Matrix matrix = new Matrix();
            final float scaleFactor = 2;
            matrix.postScale(scaleFactor, scaleFactor);
            matrix.postTranslate(
                    canvas.getWidth() - copy.getWidth() * scaleFactor,
                    canvas.getHeight() - copy.getHeight() * scaleFactor);
            canvas.drawBitmap(copy, matrix, new Paint());
        }
    }
}

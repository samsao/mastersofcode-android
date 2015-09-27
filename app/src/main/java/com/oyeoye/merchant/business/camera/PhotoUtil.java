package com.oyeoye.merchant.business.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.FileOutputStream;
import java.io.IOException;

import timber.log.Timber;

public class PhotoUtil {

    private Context mContext;

    public PhotoUtil(Context context) {
        mContext = context;
    }

    /**
     * Save an image to disk
     *
     * @param bitmap
     * @param callback
     */
    public void saveImage(final Bitmap bitmap, final SaveImageCallback callback) {
        final String destFilePath = getImagePath();
        new Thread(new Runnable() {
            public void run() {
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(destFilePath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream); // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    Timber.e("Save bitmap failed:" + e.getMessage());
                    callback.onFailure();
                } finally {
                    try {
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        callback.onSuccess(destFilePath);
                    } catch (IOException e) {
                        Timber.e("Save bitmap failed:" + e.getMessage());
                        callback.onFailure();
                    }
                }
            }
        }).start();
    }

    /**
     * Rotate bitmap
     *
     * @param bitmap original bitmap
     * @param angle  angle of rotation
     * @return rotated bitmap
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, float angle) {
        if (angle != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            return bitmap;
        }
    }

    /**
     * Get center cropped a bitmap with a new aspect ratio.
     *
     * @param sourceBitmap      original bitmap
     * @param targetAspectRatio aspect ratio of the new image
     * @return rotated bitmap
     */
    public static Bitmap getCenterCropBitmapWithTargetAspectRatio(Bitmap sourceBitmap, float targetAspectRatio) {
        float originalAspectRatio = (float) sourceBitmap.getWidth() / (float) sourceBitmap.getHeight();
        Bitmap outputBitmap;

        if (originalAspectRatio < targetAspectRatio) {
            outputBitmap = Bitmap.createBitmap(
                    sourceBitmap,
                    0,
                    (int) (((float) sourceBitmap.getHeight() - (float) sourceBitmap.getWidth() / targetAspectRatio) / 2.0f),
                    sourceBitmap.getWidth(),
                    (int)((float) sourceBitmap.getWidth() / targetAspectRatio)
            );
        } else {
            outputBitmap = Bitmap.createBitmap(
                    sourceBitmap,
                    (int) (((float) sourceBitmap.getWidth() - (float) sourceBitmap.getHeight() * targetAspectRatio) / 2.0f),
                    0,
                    sourceBitmap.getWidth() - (int) ((float) sourceBitmap.getWidth() - (float) sourceBitmap.getHeight() * targetAspectRatio),
                    sourceBitmap.getHeight()
            );
        }

        return outputBitmap;
    }

    public String getImagePath() {
        return mContext.getFilesDir().getPath() + "/image.png";
    }

    public interface SaveImageCallback {
        void onSuccess(String destFilePath);

        void onFailure();
    }
}

package com.oyeoye.merchant.business.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.List;


/**
 * @author vlegault
 * @since 15-03-17
 */
public class SimpleCamera extends TextureView implements TextureView.SurfaceTextureListener {

    /**
     * Constants
     */
    private final String LOG_TAG = getClass().getSimpleName();
    private final int PREVIEW_ORIENTATION_THRESHOLD_DEG = 20;

    private FrameLayout mCameraPreviewContainer;
    private SimpleCameraCallback mSimpleCameraCallback;
    private LayoutMode mLayoutMode;
    private int mCameraId;

    private OrientationEventListener mOrientationListener;

    private Camera mCamera;
    private int mCameraBuiltInOrientationOffset = 0;
    private int mLastCameraPreviewOrientation = 0;
    private int mOrientationWhenPictureTaken = 0;
    private float mAspectRatioWhenPictureTaken = 1.0f;

    ScaleGestureDetector mScaleGestureDetector;
    private boolean mIsPinchGestureAvailable = false; // TODO not good
    private int mInitialCameraZoomLevel = 1;
    private float mInitialSpan = 0.0f;
    private int mCameraMaximumZoomLevel = 1;


    public enum LayoutMode {
        FIT_PARENT,
        CENTER_CROP
    }

    public enum CameraId {
        FRONT_FACING,
        BACK_FACING,
        INVALID
    }

    public enum FlashMode {
        AUTO,
        ON,
        OFF
    }

    public interface SimpleCameraCallback {
        void onCameraPreviewReady();

        void onCameraPreviewFailed();

        void onPictureReady(Bitmap pictureBitmap);
    }

    /**
     * Callback that plays a camera sound as near as possible to the moment when a photo is captured
     * from the sensor.
     */
    private final Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            mOrientationWhenPictureTaken = getCameraOrientation();
            mAspectRatioWhenPictureTaken = getPreviewAspectRatio();

            // Play camera sound
            AudioManager mgr = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
        }
    };

    /**
     * Called when image data is available after a picture was taken. We transform the raw data to a
     * bitmap object then start the modification activity
     */
    private final Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length); // Get resulting image
            image = PhotoUtil.rotateBitmap(image, mOrientationWhenPictureTaken); // Add rotation correction to bitmap
            image = PhotoUtil.getCenterCropBitmapWithTargetAspectRatio(image, mAspectRatioWhenPictureTaken);

            if (null != mSimpleCameraCallback) {
                mSimpleCameraCallback.onPictureReady(image);
            }

            //TODO
            //mIsPinchGestureAvalaible = true;
        }
    };

    /**
     * SimpleCamera constructor
     *
     * @param activity
     */
    private SimpleCamera(Activity activity, FrameLayout cameraPreviewContainer,
                         SimpleCameraCallback simpleCameraCallback, LayoutMode layoutMode,
                         CameraId cameraId, boolean cameraFallback) {
        super(activity);

        mCameraPreviewContainer = cameraPreviewContainer;
        mSimpleCameraCallback = simpleCameraCallback;
        mLayoutMode = layoutMode;
        setCameraId(cameraId, cameraFallback);
        into(cameraPreviewContainer);

        //TODO
        //setupTapToFocus();
        //setupPinchZoom();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        resizeToFitContainerView();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        releaseCamera();
        setSurfaceTextureListener(null);

        if (mOrientationListener != null) {
            mOrientationListener.disable();
            mOrientationListener = null;
        }
        return true;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
        resizeToFitContainerView();
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScaleGestureDetector.onTouchEvent(event);
    }

    /**
     * Set in which container to display camera preview.
     *
     * @param cameraPreviewContainer
     */
    private SimpleCamera into(FrameLayout cameraPreviewContainer) {

        // SimpleCameraCallback is mandatory
        if (mSimpleCameraCallback == null) {
            Log.e(LOG_TAG, "Unable to start SimpleCamera: no SimpleCameraCallback callback was provided");
            return null;
        }

        // Validate camera ID
        if (getCameraId() == CameraId.INVALID) {
            Log.e(LOG_TAG, "Unable to start SimpleCamera: the selected camera ID doesn't exist on this device");
            mSimpleCameraCallback.onCameraPreviewFailed();
            return null;
        }

        // Get camera info
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        try {
            android.hardware.Camera.getCameraInfo(mCameraId, info);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Unable to start SimpleCamera: unable to fetch info from Camera #" + mCameraId + ". It is probably being used by another process");
            mSimpleCameraCallback.onCameraPreviewFailed();
            return null;
        }
        mCameraBuiltInOrientationOffset = info.orientation;

        if (cameraPreviewContainer != null) {
            mCameraPreviewContainer = cameraPreviewContainer;

            // Add view to container
            mCameraPreviewContainer.removeAllViews(); // Clear container to be sure that only our camera preview is present
            setSurfaceTextureListener(this);
            mCameraPreviewContainer.addView(this);

            // Setup orientation listener to force 180 camera preview flipping (since view are not destroyed on 180 degrees layout flip)
            mOrientationListener = new OrientationEventListener(getContext(), SensorManager.SENSOR_DELAY_NORMAL) {
                public void onOrientationChanged(int orientation) {
                    if (orientation >= 0) { // if valid orientation
                        // TODO dirty fix
                        int newPreviewOrientation;
                        if(getCameraId() == CameraId.FRONT_FACING){
                            newPreviewOrientation = (orientation - mCameraBuiltInOrientationOffset + 360) % 360;
                        }else{
                            newPreviewOrientation = (mCameraBuiltInOrientationOffset + orientation) % 360;
                        }
                        int previewOrientationDelta = newPreviewOrientation - mLastCameraPreviewOrientation;

                        if ((previewOrientationDelta > (180 - PREVIEW_ORIENTATION_THRESHOLD_DEG) &&
                                previewOrientationDelta < (180 + PREVIEW_ORIENTATION_THRESHOLD_DEG)) ||
                                (previewOrientationDelta > (-180 - PREVIEW_ORIENTATION_THRESHOLD_DEG) &&
                                        previewOrientationDelta < (-180 + PREVIEW_ORIENTATION_THRESHOLD_DEG))) {
                            mLastCameraPreviewOrientation = getCameraPreviewOrientation();
                            mCamera.setDisplayOrientation(getCameraPreviewOrientation());
                        }
                    }
                }
            };

            return this;
        } else {
            Log.e(LOG_TAG, "Unable to start SimpleCamera: Camera preview container is null");
            return null;
        }
    }

    private void setupTapToFocus() {
        //TODO
    }

    //TODO
    private void setupPinchZoom() {
        /*mScaleGestureDetector = new ScaleGestureDetector(this.getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (mCamera != null && mIsPinchGestureAvalaible == true) {
                    int newZoomLevel;
                    int spanDelta = (int) detector.getCurrentSpan() - (int) mInitialSpan;

                    newZoomLevel = mInitialCameraZoomLevel + spanDelta / 10;

                    if (newZoomLevel < 1) {
                        newZoomLevel = 1;
                        mInitialCameraZoomLevel = 1;
                        mInitialSpan = detector.getCurrentSpan();
                    } else if (newZoomLevel > mCameraMaximumZoomLevel) {
                        newZoomLevel = mCameraMaximumZoomLevel;
                        mInitialCameraZoomLevel = mCameraMaximumZoomLevel;
                        mInitialSpan = detector.getCurrentSpan();
                    }
                    Camera.Parameters cameraParams = mCamera.getParameters();
                    cameraParams.setZoom(newZoomLevel);
                    mCamera.setParameters(cameraParams);
                }

                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                Camera.Parameters cameraParams = mCamera.getParameters();
                mInitialCameraZoomLevel = cameraParams.getZoom();
                mInitialSpan = detector.getCurrentSpan();
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                // nothing
            }
        });*/
    }

    /**
     * Resize SimpleCamera view to fit it's parent container
     */
    private void resizeToFitContainerView() {
        int previewContainerWidth = mCameraPreviewContainer.getWidth();
        int previewContainerHeight = mCameraPreviewContainer.getHeight();

        // Reset camera and media recorder instances
        releaseCamera();

        // Get camera instance
        mCamera = CameraHelper.getCameraInstance(mCameraId);
        if (mCamera == null) {
            Log.e(LOG_TAG, "Unable to get camera instance");
            releaseCamera();
            return;
        }

        // Get camera parameters
        Camera.Parameters cameraParams = mCamera.getParameters();

        // Set camera preview size
        List<Size> supportedPreviewSizes = cameraParams.getSupportedPreviewSizes();
        Size optimalPreviewSize = CameraHelper.getOptimalPictureSize(supportedPreviewSizes, previewContainerWidth, previewContainerHeight);

        if (!adjustSurfaceLayoutSize(optimalPreviewSize, previewContainerWidth, previewContainerHeight)) {
            if (prepareCamera()) {
                if (null != mSimpleCameraCallback) {
                    mOrientationListener.enable();
                    //TODO
                    //mIsPinchGestureAvalaible = true;
                    mSimpleCameraCallback.onCameraPreviewReady();
                }
            } else {
                if (null != mSimpleCameraCallback) {
                    //TODO
                    //mIsPinchGestureAvalaible = false;
                    mSimpleCameraCallback.onCameraPreviewFailed();
                }
                Log.e(LOG_TAG, "Unable to prepare camera");
            }
        }
    }

    /**
     * Adjusts SurfaceView dimension to our layout available space.
     *
     * @param availableWidth  available width of the parent container
     * @param availableHeight available height of the parent container
     */
    private boolean adjustSurfaceLayoutSize(Camera.Size previewSize, int availableWidth, int availableHeight) {
        float previewSizeWidth, previewSizeHeight;
        float heightScale, widthScale, previewSizeScale;

        if (CameraHelper.isPortrait(getContext())) {
            previewSizeWidth = previewSize.height;
            previewSizeHeight = previewSize.width;
        } else {
            previewSizeWidth = previewSize.width;
            previewSizeHeight = previewSize.height;
        }

        heightScale = availableHeight / previewSizeHeight;
        widthScale = availableWidth / previewSizeWidth;

        if (mLayoutMode == LayoutMode.FIT_PARENT) {
            // Select smaller factor, because the surface cannot be set to the size larger than display metrics.
            if (heightScale < widthScale) {
                previewSizeScale = heightScale;
            } else {
                previewSizeScale = widthScale;
            }
        } else {
            if (heightScale < widthScale) {
                previewSizeScale = widthScale;
            } else {
                previewSizeScale = heightScale;
            }
        }

        int layoutHeight = Math.round(previewSizeHeight * previewSizeScale);
        int layoutWidth = Math.round(previewSizeWidth * previewSizeScale);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.getLayoutParams();
        if ((layoutWidth != this.getWidth()) || (layoutHeight != this.getHeight())) {
            layoutParams.height = layoutHeight;
            layoutParams.width = layoutWidth;
            layoutParams.gravity = Gravity.CENTER;
            this.setLayoutParams(layoutParams);

            // A call to setLayoutParams will trigger another surfaceChanged invocation.
            // Set return value to true since the layout as been modified.
            return true;
        } else {
            // Set return value to false since no changes were made to the layout.
            return false;
        }
    }

    /**
     * Prepare camera to take a picture or a video.
     *
     * @return true on success
     */
    private boolean prepareCamera() {
        // Reset camera and media recorder instances
        releaseCamera();

        // Get camera instance
        mCamera = CameraHelper.getCameraInstance(mCameraId);
        if (mCamera == null) {
            Log.e(LOG_TAG, "Unable to get camera instance");
            releaseCamera();
            return false;
        }

        // Get camera parameters
        Camera.Parameters cameraParams = mCamera.getParameters();
        mCameraMaximumZoomLevel = cameraParams.getMaxZoom();

        // Set camera preview orientation
        mLastCameraPreviewOrientation = getCameraPreviewOrientation();
        mCamera.setDisplayOrientation(mLastCameraPreviewOrientation);

        // Set camera preview size
        List<Size> supportedPreviewSizes = cameraParams.getSupportedPreviewSizes();
        Size optimalPreviewSize = CameraHelper.getOptimalPictureSize(supportedPreviewSizes, mCameraPreviewContainer.getWidth(), mCameraPreviewContainer.getHeight());
        cameraParams.setPreviewSize(optimalPreviewSize.width, optimalPreviewSize.height);
        Log.d(LOG_TAG, "Camera preview size - width: " + optimalPreviewSize.width + " - height: " + optimalPreviewSize.height);

        // Setting up optimal picture size & resolution based on camera preview aspect ratio
        List<Size> supportedPictureSizes = cameraParams.getSupportedPictureSizes();
        Size optimalPictureSize = CameraHelper.getOptimalPictureSize(supportedPictureSizes, optimalPreviewSize.width, optimalPreviewSize.height);
        cameraParams.setPictureSize(optimalPictureSize.width, optimalPictureSize.height);
        Log.d(LOG_TAG, "Camera picture size - width: " + optimalPictureSize.width + " - height: " + optimalPictureSize.height);

        mCamera.setParameters(cameraParams);

        try {
            mCamera.setPreviewTexture(getSurfaceTexture());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Surface texture for camera is unavailable or unsuitable" + e.getMessage());
            releaseCamera();
            return false;
        }

        try {
            mCamera.startPreview();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to start camera preview: " + e.getMessage());
            releaseCamera();
            return false;
        }

        return true;
    }

    public void takePicture() {
        //TODO
        //mIsPinchGestureAvalaible = false;
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean b, Camera camera) {
                camera.takePicture(mShutterCallback, null, mJpegCallback);
            }
        });
    }

    /**
     * Release camera for other applications.
     */
    private void releaseCamera() {
        //TODO
        //mIsPinchGestureAvalaible = false;
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * Release the camera for other applications.
     */
    public void release() {
        releaseCamera();
        setSurfaceTextureListener(null);

        if (mOrientationListener != null) {
            mOrientationListener.disable();
            mOrientationListener = null;
        }

        if (mCameraPreviewContainer != null) {
            mCameraPreviewContainer.removeAllViews();
        }
    }

    /**
     * Get which camera is currently used.
     */
    public CameraId getCameraId() {
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return CameraId.FRONT_FACING;
        } else if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            return CameraId.BACK_FACING;
        } else {
            return CameraId.INVALID;
        }
    }

    /**
     * Set which camera to use.
     *
     * @param cameraId : FRONT_FACING, BACK_FACING
     */
    public void setCameraId(CameraId cameraId, boolean cameraIdFallback) {
        mCameraId = -1; // set init value to error

        // Setting camera id
        if (cameraId == CameraId.FRONT_FACING) {
            if (CameraHelper.hasFrontCamera()) {
                mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
            } else if (cameraIdFallback && CameraHelper.hasBackCamera()) {
                Log.d(LOG_TAG, "Unable to set front camera ID since current device has no front facing camera, falling back to back facing camera");
                mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
            } else {
                Log.e(LOG_TAG, "Unable to set front camera ID since current device has no front facing camera");
            }
        } else if (cameraId == CameraId.BACK_FACING) {
            if (CameraHelper.hasBackCamera()) {
                mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
            } else if (cameraIdFallback && CameraHelper.hasFrontCamera()) {
                Log.d(LOG_TAG, "Unable to set back camera ID since current device has no back facing camera, falling back to front facing camera");
                mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
            } else {
                Log.e(LOG_TAG, "Unable to set back camera ID since current device has no back facing camera");
            }
        } else {
            Log.e(LOG_TAG, "Invalid camera ID selected");
        }
    }

    /**
     * If more the one camera is available on the current device, this function switches the camera
     * source (FRONT, BACK).
     */
    public CameraId switchCamera() {
        CameraId currentCameraId = getCameraId();

        switch (currentCameraId) {
            case FRONT_FACING:
                if (CameraHelper.hasBackCamera()) {
                    releaseCamera();
                    setCameraId(CameraId.BACK_FACING, false);
                    resizeToFitContainerView();
                } else {
                    Log.e(LOG_TAG, "Unable to switch front facing camera to back facing camera since current device has no back facing camera");
                }
                break;
            case BACK_FACING:
                if (CameraHelper.hasFrontCamera()) {
                    releaseCamera();
                    setCameraId(CameraId.FRONT_FACING, false);
                    resizeToFitContainerView();
                } else {
                    Log.e(LOG_TAG, "Unable to switch back facing camera to front facing camera since current device has no front facing camera");
                }
                break;
            default:
                Log.d(LOG_TAG, "No current camera is selected, trying to fall back to a valid camera");
                releaseCamera();
                setCameraId(CameraId.FRONT_FACING, true);
                resizeToFitContainerView();
                break;
        }

        return getCameraId();
    }

    /**
     * Tells if the current device have a front camera.
     *
     * @return true if it has a front camera
     */
    public boolean hasFrontCamera() {
        return CameraHelper.cameraExistsById(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    /**
     * Tells if the current device have a back camera.
     *
     * @return true if it has a back camera
     */
    public boolean hasBackCamera() {
        return CameraHelper.cameraExistsById(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    /**
     * Tells if the current device have multiple cameras: back & front facing.
     *
     * @return true if it has back & front facing cameras
     */
    public boolean hasMultiCameras() {
        return hasFrontCamera() && hasBackCamera();
    }

    /**
     * Tells if the current camera have a flash functionality.
     *
     * @return true if it has a front camera
     */
    public boolean isFlashAvailable() {
        if (mCamera != null) {
            List<String> supportedFlashModes = mCamera.getParameters().getSupportedFlashModes();
            if (supportedFlashModes != null && supportedFlashModes.size() > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            Log.e(LOG_TAG, "Flash is not available since camera is not initialized");
            return false;
        }
    }

    /**
     * Get current camera's flash mode.
     */
    public FlashMode getFlashMode() {
        FlashMode flashMode = FlashMode.OFF;

        if (isFlashAvailable()) {
            List<String> supportedFlashModes = mCamera.getParameters().getSupportedFlashModes();

            if (supportedFlashModes != null) {
                String currentFlashMode = mCamera.getParameters().getFlashMode();
                switch (currentFlashMode) {
                    case Camera.Parameters.FLASH_MODE_AUTO:
                        flashMode = FlashMode.AUTO;
                        break;
                    case Camera.Parameters.FLASH_MODE_ON:
                        flashMode = FlashMode.ON;
                        break;
                }
            } else {
                Log.e(LOG_TAG, "This camera has no supported flash modes");
            }
        } else {
            Log.e(LOG_TAG, "Flash is not available for this camera");
        }

        return flashMode;
    }

    /**
     * Set current camera's flash mode.
     *
     * @param flashMode : FLASH_MODE_AUTO, FLASH_MODE_OFF, FLASH_MODE_ON
     * @return true on success
     */
    public boolean setFlashMode(FlashMode flashMode) {
        boolean success = false;

        if (isFlashAvailable()) {
            String flashModeText;
            List<String> supportedFlashModes = mCamera.getParameters().getSupportedFlashModes();

            switch (flashMode) {
                case AUTO:
                    flashModeText = Camera.Parameters.FLASH_MODE_AUTO;
                    break;
                case ON:
                    flashModeText = Camera.Parameters.FLASH_MODE_ON;
                    break;
                case OFF:
                    flashModeText = Camera.Parameters.FLASH_MODE_OFF;
                    break;
                default:
                    flashModeText = Camera.Parameters.FLASH_MODE_OFF; // Use OFF as default value
                    break;
            }

            if (supportedFlashModes != null && supportedFlashModes.contains(flashModeText)) {
                Camera.Parameters cameraParams = mCamera.getParameters();
                cameraParams.setFlashMode(flashModeText);
                mCamera.setParameters(cameraParams);
                success = true;
            } else {
                Log.e(LOG_TAG, "Flash mode \'" + flashModeText + "\' not supported for this camera");
            }
        } else {
            Log.e(LOG_TAG, "Flash is not available on the current camera");
        }

        return success;
    }

    /**
     * Get next available flash mode on the current camera. Supported mode : FLASH_MODE_AUTO, FLASH_MODE_OFF, FLASH_MODE_ON
     *
     * @return next available flash mode
     */
    public FlashMode getNextAvailableFlashMode() {
        FlashMode newFlashMode = FlashMode.OFF;

        if (isFlashAvailable()) {
            List<String> supportedFlashModes = mCamera.getParameters().getSupportedFlashModes();

            if (supportedFlashModes != null && supportedFlashModes.size() > 0) {
                FlashMode currentFlashMode = getFlashMode();
                switch (currentFlashMode) {
                    case AUTO:
                        if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                            newFlashMode = FlashMode.OFF;
                        } else if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_ON)) {
                            newFlashMode = FlashMode.ON;
                        }
                        break;
                    case OFF:
                        if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_ON)) {
                            newFlashMode = FlashMode.ON;
                        } else if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                            newFlashMode = FlashMode.AUTO;
                        }
                        break;
                    case ON:
                        if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                            newFlashMode = FlashMode.AUTO;
                        } else if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                            newFlashMode = FlashMode.OFF;
                        }
                        break;
                }
            } else {
                Log.e(LOG_TAG, "This camera has no supported flash modes");
            }
        } else {
            Log.e(LOG_TAG, "Flash is not available for this camera");
        }

        return newFlashMode;
    }

    /**
     * Get camera preview display aspect ratio.
     *
     * @return aspect ratio
     */
    public float getPreviewAspectRatio() {
        float width, height;

        if (mLayoutMode == LayoutMode.CENTER_CROP) {
            width = (float) mCameraPreviewContainer.getWidth();
            height = (float) mCameraPreviewContainer.getHeight();
        } else {
            width = (float) getWidth();
            height = (float) getHeight();
        }

        return width / height;
    }

    /**
     * Gets camera preview angle
     */
    private int getCameraPreviewOrientation() {
        int angle = mCameraBuiltInOrientationOffset;
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    angle += 180;
                }
                break;
            case Surface.ROTATION_90:
                if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    angle += 90;
                } else {
                    angle += 270;
                }
                break;
            case Surface.ROTATION_180:
                if (mCameraId != Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    angle += 180;
                }
                break;
            case Surface.ROTATION_270:
                if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    angle += 270;
                } else {
                    angle += 90;
                }
                break;
            default:
                break;
        }

        return (angle % 360); // always return a value between 0 and 360 degrees
    }

    /**
     * Gets camera's current orientation angle
     *
     * @return camera's current orientation angle in degree
     */
    public int getCameraOrientation() {
        int angle = mCameraBuiltInOrientationOffset;
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        switch (display.getRotation()) {
            case Surface.ROTATION_90:
                if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    angle += 90;
                } else {
                    angle += 270;
                }
                break;
            case Surface.ROTATION_180:
                angle += 180;
                break;
            case Surface.ROTATION_270:
                if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    angle += 270;
                } else {
                    angle += 90;
                }
                break;
            default:
                break;
        }

        return (angle % 360); // always return a value between 0 and 360 degrees
    }


    public static class SimpleCameraBuilder {
        /**
         * Constants
         */
        private final String LOG_TAG = getClass().getSimpleName();
        private final LayoutMode DEFAULT_LAYOUT_MODE = LayoutMode.FIT_PARENT;
        private final CameraId DEFAULT_CAMERA_ID = CameraId.FRONT_FACING;

        private static SimpleCamera mSimpleCameraInstance;
        private static Activity mActivity;
        private static FrameLayout mCameraPreviewContainer;
        private static SimpleCameraCallback mSimpleCameraCallback;

        private LayoutMode mLayoutMode;
        private CameraId mCameraId;
        private boolean mCameraFallback;

        public SimpleCameraBuilder(Activity activity, FrameLayout cameraPreviewContainer, SimpleCameraCallback simpleCameraCallback) {
            mActivity = activity;
            mCameraPreviewContainer = cameraPreviewContainer;
            mSimpleCameraCallback = simpleCameraCallback;
            setLayoutMode(DEFAULT_LAYOUT_MODE);
            setCamera(DEFAULT_CAMERA_ID, true);
        }

        /**
         * Set camera preview layout mode.
         *
         * @param layoutMode : FIT_PARENT or CENTER_CROP
         */
        public SimpleCameraBuilder setLayoutMode(LayoutMode layoutMode) {
            mLayoutMode = layoutMode;
            return this;
        }

        /**
         * Set which camera to use.
         *
         * @param cameraId : FRONT_FACING, BACK_FACING
         */
        public SimpleCameraBuilder setCamera(CameraId cameraId, boolean cameraFallback) {
            mCameraId = cameraId;
            mCameraFallback = cameraFallback;
            return this;
        }

        public SimpleCamera createSimpleCamera() {
            if (mSimpleCameraInstance != null) {
                mSimpleCameraInstance.release();
                mSimpleCameraInstance = null;
            }

            return mSimpleCameraInstance = new SimpleCamera(mActivity, mCameraPreviewContainer, mSimpleCameraCallback, mLayoutMode, mCameraId, mCameraFallback);
        }
    }
}

package com.oyeoye.merchant.presentation.deals.add_deal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.business.camera.SimpleCamera;
import com.oyeoye.merchant.presentation.base.PresentedFrameLayout;
import com.oyeoye.merchant.presentation.deals.add_deal.stackable.AddDealStackableComponent;

import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;

@AutoInjector(AddDealPresenter.class)
public class AddDealView extends PresentedFrameLayout<AddDealPresenter> {

    /**
     * Constants
     */
    private final String LOG_TAG = getClass().getSimpleName();
    final private float CAMERA_PREVIEW_ASPECT_RATIO = 0.4f;

    @Bind(R.id.screen_add_deal_layout)
    public LinearLayout mLayout;
    @Bind(R.id.screen_add_deal_toolbar)
    public Toolbar mToolbar;
    @Bind(R.id.screen_add_deal_camera_preview_container)
    public FrameLayout mCameraPreviewContainer;
    @Bind(R.id.screen_add_deal_take_picture_button)
    public Button mTakePictureButton;

    private Activity mActivity;
    private SimpleCamera mSimpleCamera;

    public AddDealView(Context context) {
        super(context);
        DaggerService.<AddDealStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.screen_add_deal, this);
        ButterKnife.bind(view);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.resetMenu(mToolbar);
    }

    public void startCameraPreview(final Activity activity) {
        mActivity = activity;
        if (getWidth() > 0) {
            setupCameraPreview();
        } else {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    mCameraPreviewContainer.setLayoutParams(new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            (int) (CAMERA_PREVIEW_ASPECT_RATIO * getWidth())));

                    setupCameraPreview();
                }
            });
        }

    }

    private void setupCameraPreview() {
        mTakePictureButton.setEnabled(false);
        setupTakePictureButton();

        SimpleCamera.SimpleCameraCallback simpleCameraCallback = new SimpleCamera.SimpleCameraCallback() {
            @Override
            public void onCameraPreviewReady() {
                Log.d(LOG_TAG, "Camera is ready to use");
                mTakePictureButton.setEnabled(true);
            }

            @Override
            public void onCameraPreviewFailed() {
                Log.e(LOG_TAG, "Camera failed to start");
                mTakePictureButton.setEnabled(false);
            }

            @Override
            public void onPictureReady(Bitmap bitmap) {
                mSimpleCamera.release();
                setDealPicture(bitmap);
                setupRetakePictureButton();
                mTakePictureButton.setEnabled(true);
            }
        };

        mSimpleCamera = new SimpleCamera.SimpleCameraBuilder(mActivity, mCameraPreviewContainer, simpleCameraCallback)
                .setLayoutMode(SimpleCamera.LayoutMode.CENTER_CROP)
                .setCamera(SimpleCamera.CameraId.BACK_FACING, false)
                .createSimpleCamera();
    }

    private void setupTakePictureButton() {
        mTakePictureButton.setText(R.string.add_deal_take_picture);
        mTakePictureButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTakePictureButton.setEnabled(false);
                mSimpleCamera.takePicture();
            }
        });
    }

    private void setupRetakePictureButton() {
        mTakePictureButton.setText(R.string.add_deal_retake_picture);
        mTakePictureButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraPreview(mActivity);
            }
        });
    }

    private void setDealPicture(Bitmap bitmap) {
        ImageView imageView = new ImageView(mActivity);
        imageView.setImageBitmap(bitmap);
        mCameraPreviewContainer.addView(imageView);
    }
}

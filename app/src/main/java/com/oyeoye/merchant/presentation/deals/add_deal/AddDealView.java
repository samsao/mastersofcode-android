package com.oyeoye.merchant.presentation.deals.add_deal;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.business.api.entity.Deal;
import com.oyeoye.merchant.business.camera.SimpleCamera;
import com.oyeoye.merchant.presentation.base.PresentedFrameLayout;
import com.oyeoye.merchant.presentation.deals.add_deal.stackable.AddDealStackableComponent;
import com.oyeoye.merchant.presentation.util.LoadingView;

import architect.robot.DaggerService;
import architect.view.HandlesViewTransition;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;

@AutoInjector(AddDealPresenter.class)
public class AddDealView extends PresentedFrameLayout<AddDealPresenter> implements HandlesViewTransition {

    /**
     * Constants
     */
    private final String LOG_TAG = getClass().getSimpleName();
    final private float CAMERA_PREVIEW_ASPECT_RATIO = 0.5f;

    @Bind(R.id.screen_add_deal_layout)
    public LinearLayout mLayout;
    @Bind(R.id.screen_add_deal_toolbar)
    public Toolbar mToolbar;
    @Bind(R.id.screen_add_deal_camera_preview_container)
    public FrameLayout mCameraPreviewContainer;
    @Bind(R.id.screen_add_deal_take_picture_button)
    public Button mTakePictureButton;
    @Bind(R.id.screen_add_deal_title)
    public EditText mDealTitle;
    @Bind(R.id.screen_add_deal_quantity)
    public EditText mQuantity;
    @Bind(R.id.screen_add_deal_original_price)
    public EditText mOriginalPrice;
    @Bind(R.id.screen_add_deal_discounted_price)
    public EditText mDiscountedPrice;
    @Bind(R.id.screen_add_deal_description)
    public EditText mDealDescription;
    @Bind(R.id.screen_add_deal_broadcast_button)
    public Button mBroadcastDealButton;
    @Bind(R.id.screen_add_deal_overlay)
    public LoadingView mLoadingView;

    private SimpleCamera mSimpleCamera;
    private ImageView mDealImageView;

    public AddDealView(Context context) {
        super(context);
        DaggerService.<AddDealStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.screen_add_deal, this);
        ButterKnife.bind(view);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setupBroadcastDealButton();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mCameraPreviewContainer.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        (int) (CAMERA_PREVIEW_ASPECT_RATIO * getWidth())));
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.resetMenu(mToolbar);
    }

    private void setupCameraPreview() {
        mTakePictureButton.setEnabled(false);
        setupTakePictureButton();

        SimpleCamera.SimpleCameraCallback simpleCameraCallback = new SimpleCamera.SimpleCameraCallback() {
            @Override
            public void onCameraPreviewReady() {
                Log.d(LOG_TAG, "Camera is ready to use");
                mSimpleCamera.setFlashMode(SimpleCamera.FlashMode.AUTO);
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
                presenter.saveImage(bitmap);
                setDealPicture(bitmap);
                setupRetakePictureButton();
                mTakePictureButton.setEnabled(true);
            }
        };

        mSimpleCamera = new SimpleCamera.SimpleCameraBuilder(presenter.getActivity(), mCameraPreviewContainer, simpleCameraCallback)
                .setLayoutMode(SimpleCamera.LayoutMode.CENTER_CROP)
                .setCamera(SimpleCamera.CameraId.BACK_FACING, false)
                .createSimpleCamera();
    }

    private void setupTakePictureButton() {
        mTakePictureButton.setText(R.string.add_deal_take_picture);
        mDealImageView = null;
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
                setupCameraPreview();
            }
        });
    }

    private void setDealPicture(Bitmap bitmap) {
        mDealImageView = new ImageView(getContext());
        mDealImageView.setImageBitmap(bitmap);
        mCameraPreviewContainer.addView(mDealImageView);
    }

    private void setupBroadcastDealButton() {
        // TODO replace with validator in the presenter
        mBroadcastDealButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDealImageView == null) {
                    Toast.makeText(getContext(), R.string.add_deal_must_take_picture, Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(mDealTitle.getText()) ||
                        TextUtils.isEmpty(mQuantity.getText()) ||
                        TextUtils.isEmpty(mOriginalPrice.getText()) ||
                        TextUtils.isEmpty(mDiscountedPrice.getText()) ||
                        TextUtils.isEmpty(mDealDescription.getText())) {
                    Toast.makeText(getContext(), R.string.add_deal_fill_all_fields, Toast.LENGTH_LONG).show();
                } else {
                    mTakePictureButton.setEnabled(false);
                    mBroadcastDealButton.setEnabled(false);
                    Deal deal = new Deal();
                    deal.setTitle(mDealTitle.getText().toString());
                    deal.setDescription(mDealDescription.getText().toString());
                    deal.setOriginalPrice(Float.parseFloat(mOriginalPrice.getText().toString()));
                    deal.setPrice(Float.parseFloat(mDiscountedPrice.getText().toString()));
                    deal.setQuantity(Integer.parseInt(mQuantity.getText().toString()));
                    presenter.addDeal(deal);
                }
            }
        });
    }

    public void showLoadingView(boolean visible) {
        if (visible) {
            mLoadingView.setVisibility(VISIBLE);
        } else {
            mLoadingView.setVisibility(GONE);
        }
    }

    @Override
    public void onViewTransition(AnimatorSet animatorSet) {
        if (animatorSet != null) {
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setupCameraPreview();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            setupCameraPreview();
        }
    }
}

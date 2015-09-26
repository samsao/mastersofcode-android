package com.oyeoye.merchant.presentation.deals.add_deal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.RootActivity;
import com.oyeoye.merchant.business.camera.SimpleCamera;
import com.oyeoye.merchant.presentation.RootActivityPresenter;
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
    final private float CAMERA_PREVIEW_ASPECT_RATIO = 0.3f;

    @Bind(R.id.screen_add_deal_layout)
    public LinearLayout mLayout;
    @Bind(R.id.screen_add_deal_toolbar)
    public Toolbar mToolbar;
    @Bind(R.id.screen_add_deal_camera_preview_container)
    public FrameLayout mCameraPreviewContainer;

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
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);

                mCameraPreviewContainer.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        (int) (CAMERA_PREVIEW_ASPECT_RATIO * getWidth())));

                SimpleCamera.SimpleCameraCallback simpleCameraCallback = new SimpleCamera.SimpleCameraCallback() {
                    @Override
                    public void onCameraPreviewReady() {
                        Log.d(LOG_TAG, "Camera is ready to use");
                        //setupButtons();
                    }

                    @Override
                    public void onCameraPreviewFailed() {
                        Log.e(LOG_TAG, "Camera failed to start");
                    }

                    @Override
                    public void onPictureReady(Bitmap image) {
                        //mSelectMediaProvider.saveImageAndStartEditActivity(image, CameraHelper.getDefaultImageFilePath());
                    }
                };

                mSimpleCamera = new SimpleCamera.SimpleCameraBuilder(activity, mCameraPreviewContainer, simpleCameraCallback)
                        .setLayoutMode(SimpleCamera.LayoutMode.CENTER_CROP)
                        .setCamera(SimpleCamera.CameraId.BACK_FACING, false)
                        .createSimpleCamera();
            }
        });
    }
}

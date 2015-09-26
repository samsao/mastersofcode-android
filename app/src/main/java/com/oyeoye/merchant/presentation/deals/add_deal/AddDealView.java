package com.oyeoye.merchant.presentation.deals.add_deal;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.presentation.base.PresentedFrameLayout;
import com.oyeoye.merchant.presentation.deals.add_deal.stackable.AddDealStackableComponent;

import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;

@AutoInjector(AddDealPresenter.class)
public class AddDealView extends PresentedFrameLayout<AddDealPresenter> {

    final private float DEAL_PICTURE_ASPECT_RATIO = 0.3f;

    @Bind(R.id.screen_add_deal_layout)
    public LinearLayout mLayout;
    @Bind(R.id.screen_add_deal_toolbar)
    public Toolbar mToolbar;
    @Bind(R.id.screen_add_deal_picture_preview_container)
    public FrameLayout mPicturePreviewContainer;

    public AddDealView(Context context) {
        super(context);
        DaggerService.<AddDealStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.screen_add_deal, this);
        ButterKnife.bind(view);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mPicturePreviewContainer.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        (int) (DEAL_PICTURE_ASPECT_RATIO * getWidth())));
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.resetMenu(mToolbar);
    }
}

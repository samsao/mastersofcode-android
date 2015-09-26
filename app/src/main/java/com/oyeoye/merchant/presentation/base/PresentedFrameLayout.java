package com.oyeoye.merchant.presentation.base;

import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;

import com.oyeoye.merchant.presentation.AbstractPresenter;

import architect.view.HandlesBack;
import architect.view.HandlesViewTransition;

public abstract class PresentedFrameLayout<T extends AbstractPresenter> extends architect.commons.view.PresentedFrameLayout<T> implements HandlesBack, HandlesViewTransition {
    public PresentedFrameLayout(Context context) {
        super(context);
    }

    public PresentedFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PresentedFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
        presenter.onViewAttachedToWindow();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onViewTransition(AnimatorSet animatorSet) {

    }
}

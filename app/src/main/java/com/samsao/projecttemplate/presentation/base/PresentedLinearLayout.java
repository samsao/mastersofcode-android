package com.samsao.projecttemplate.presentation.base;

import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;

import com.samsao.projecttemplate.presentation.AbstractPresenter;

import architect.view.HandlesBack;
import architect.view.HandlesViewTransition;

/**
 * @author jfcartier
 * @since 15-08-06
 */
public abstract class PresentedLinearLayout<T extends AbstractPresenter> extends architect.commons.view.PresentedLinearLayout<T> implements HandlesBack, HandlesViewTransition {
    public PresentedLinearLayout(Context context) {
        super(context);
    }

    public PresentedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PresentedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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

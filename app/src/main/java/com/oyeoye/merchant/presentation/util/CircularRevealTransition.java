package com.oyeoye.merchant.presentation.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.oyeoye.merchant.presentation.deals.add_deal.AddDealView;
import com.oyeoye.merchant.presentation.main.MainView;

import architect.ViewTransition;
import architect.ViewTransitionDirection;
import architect.commons.transition.Config;

/**
 * @author jfcartier
 * @since 15-09-26
 */
public class CircularRevealTransition implements ViewTransition {

    protected Config config;

    public CircularRevealTransition() {
        this(new Config().duration(300).interpolator(new AccelerateDecelerateInterpolator()));
    }

    public CircularRevealTransition(Config config) {
        this.config = config;
    }

    @Override
    public void transition(View view, View view2, ViewTransitionDirection viewTransitionDirection, AnimatorSet animatorSet) {
            if (viewTransitionDirection == ViewTransitionDirection.BACKWARD) {
//                exitTransition((AddDealView)view2, (MainView)view, animatorSet);
            } else {
//                enterTransition((AddDealView)view2, (MainView)view, animatorSet);
            }
    }

    public void enterTransition(AddDealView enterView, MainView exitView, AnimatorSet set) {

        set.play(ObjectAnimator.ofFloat(enterView.mLayout, View.ALPHA, 0f, 1f));

//        Animator reveal = createRevealAnimator(exitView.mFloatingActionButton, enterView.mLayout, exitView.mFloatingActionButton, enterView.mLayout);
//        set.play(reveal);
    }

    public void exitTransition(AddDealView exitView, MainView enterView, AnimatorSet set) {
        set.play(ObjectAnimator.ofFloat(exitView.mLayout, View.ALPHA, 1f, 0f));

//        Animator reveal = createRevealAnimator(enterView.mFloatingActionButton, exitView.mLayout, exitView.mLayout, enterView.mFloatingActionButton);
//        set.play(reveal);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Animator createRevealAnimator(View center, View animated, View start, View end) {
        int[] location = new int[2];
        center.getLocationOnScreen(location);

        int cx = location[0] + center.getWidth() / 2;
        int cy = location[1] + center.getHeight() / 2;

        // bug first launch ?
        // animate background separately
        // animate content alpha + translation y separately

        Animator reveal = ViewAnimationUtils.createCircularReveal(animated, cx, cy, Math.max(start.getWidth(), start.getHeight()), Math.max(end.getWidth(), end.getHeight()));
        return reveal;
    }
}

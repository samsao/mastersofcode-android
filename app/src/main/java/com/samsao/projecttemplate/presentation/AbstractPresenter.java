package com.samsao.projecttemplate.presentation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

import architect.Navigator;
import mortar.ViewPresenter;

/**
 * @author jfcartier
 * @since 15-06-24
 */
public abstract class AbstractPresenter<V extends View> extends ViewPresenter<V> {


//    public abstract class ApiCallback<T> implements Callback<T> {
//        @Override
//        public void success(T t, Response response) {
//            if (!hasView()) {
//                return;
//            }
//            onSuccess(t);
//        }
//
//        @Override
//        public void failure(RetrofitError error) {
//            Timber.e("API error: " + error.getCause().getClass().getName() + ": " + error.getMessage());
//            if (!hasView()) {
//                return;
//            }
//            onFailure(error.getCause());
//        }
//
//        public abstract void onSuccess(T t);
//
//        public abstract void onFailure(Throwable error);
//    }

    /**
     * Override this method if needed
     */
    public void onViewAttachedToWindow() {

    }

    /**
     * Go back in the backstack
     */
    public void goBack() {
        Navigator.get(getView()).back();
    }

    /**
     * Helper method to get the Context
     *
     * @return
     */
    public Context getContext() {
        if (!hasView()) {
            return null;
        } else {
            return getView().getContext();
        }
    }

    /**
     * Helper method to get Resources
     *
     * @return
     */
    public Resources getResources() {
        Context context = getContext();
        return context == null ? null : context.getResources();
    }

    /**
     * Helper method to get a String
     *
     * @return
     */
    public String getString(@StringRes int resId) {
        Context context = getContext();
        return context == null ? null : context.getString(resId);
    }

    /**
     * Helper method to get a color
     *
     * @return
     */
    public int getColor(@ColorRes int resId) {
        Resources resources = getResources();
        return resources == null ? null : resources.getColor(resId);
    }

    /**
     * Helper method to get a drawable
     *
     * @return
     */
    public Drawable getDrawable(@DrawableRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Context context = getContext();
            return context == null ? null : context.getDrawable(resId);
        } else {
            Resources resources = getResources();
            //noinspection deprecation
            return resources == null ? null : resources.getDrawable(resId);
        }
    }

    /**
     * Helper method to get a dimension
     *
     * @return
     */
    public float getDimension(@DimenRes int resId) {
        Resources resources = getResources();
        return resources == null ? null : resources.getDimension(resId);
    }

    /**
     * Helper method to show a Toast
     *
     * @param resId
     */
    public void showToast(@StringRes int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    /**
     * Helper method to show a Toast
     *
     * @param resId
     * @param duration
     */
    public void showToast(@StringRes int resId, int duration) {
        Toast.makeText(getContext(), resId, duration).show();
    }
}

package com.oyeoye.merchant.presentation.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.oyeoye.merchant.R;

public class AspectRatioImageView extends ImageView {

    private float mRatio;

    public AspectRatioImageView(Context context) {
        super(context);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            setAttributes(context, attrs);
        }
    }

    public AspectRatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (attrs != null) {
            setAttributes(context, attrs);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, (int)(width/mRatio));
    }

    private void setAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AspectRatioImageView,
                0, 0);
        try {
            mRatio = typedArray.getFloat(R.styleable.AspectRatioImageView_ratio, 1.0f);
        } finally {
            typedArray.recycle();
        }
    }
}

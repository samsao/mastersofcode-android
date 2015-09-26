package com.oyeoye.merchant.presentation.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.oyeoye.merchant.R;

import java.util.ArrayList;
import java.util.List;

import architect.StackablePath;
import architect.commons.adapter.StackablePagerAdapter;

/**
 * @author jliang
 * @since 2015-08-04
 */
public class MainPagerAdapter extends StackablePagerAdapter {
    private Context mContext;
    private List<View> views = new ArrayList<>();

    public MainPagerAdapter(Context context, StackablePath... paths) {
        super(context, paths);
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = (View) super.instantiateItem(container, position);
        views.add(view);
        return view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getStringArray(R.array.screen_main_tab_titles)[position];
    }
}

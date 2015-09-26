package com.oyeoye.merchant.presentation.main;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.presentation.base.PresentedFrameLayout;
import com.oyeoye.merchant.presentation.deals.bought_deals.stackable.BoughtDealsStackable;
import com.oyeoye.merchant.presentation.deals.my_deals.stackable.MyDealsStackable;
import com.oyeoye.merchant.presentation.main.stackable.MainStackableComponent;

import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;

@AutoInjector(MainPresenter.class)
public class MainView extends PresentedFrameLayout<MainPresenter> {

    @Bind(R.id.screen_main_toolbar)
    protected Toolbar mToolbar;
    @Bind(R.id.screen_main_tab_layout)
    protected TabLayout mTabLayout;
    @Bind(R.id.screen_main_viewpager)
    protected ViewPager mViewPager;

    protected MainPagerAdapter mMainPagerAdapter;
    public MainView(Context context) {
        super(context);
        DaggerService.<MainStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.screen_main, this);
        ButterKnife.bind(view);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mMainPagerAdapter = new MainPagerAdapter(getContext(), new MyDealsStackable(),
                new BoughtDealsStackable());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mViewPager.setAdapter(mMainPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
        presenter.resetMenu(mToolbar);
    }
}

package com.samsao.projecttemplate.presentation.main;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.samsao.projecttemplate.R;
import com.samsao.projecttemplate.presentation.base.PresentedFrameLayout;
import com.samsao.projecttemplate.presentation.main.stackable.MainStackableComponent;

import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author jliang
 * @since 2015-08-04
 */
@AutoInjector(MainPresenter.class)
public class MainView extends PresentedFrameLayout<MainPresenter> {

    @Bind(R.id.screen_main_toolbar)
    protected Toolbar mToolbar;

    public MainView(Context context) {
        super(context);
        DaggerService.<MainStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.screen_main, this);
        ButterKnife.bind(view);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.resetMenu(mToolbar);
    }
}

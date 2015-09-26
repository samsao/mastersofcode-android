package com.oyeoye.merchant.presentation.profile;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.presentation.base.PresentedFrameLayout;
import com.oyeoye.merchant.presentation.profile.stackable.ProfileStackableComponent;

import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;

@AutoInjector(ProfilePresenter.class)
public class ProfileView extends PresentedFrameLayout<ProfilePresenter> {

    @Bind(R.id.screen_profile_toolbar)
    protected Toolbar mToolbar;

    public ProfileView(Context context) {
        super(context);
        DaggerService.<ProfileStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.screen_profile, this);
        ButterKnife.bind(view);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.resetMenu(mToolbar);
    }
}

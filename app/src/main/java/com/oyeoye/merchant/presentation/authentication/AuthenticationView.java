package com.oyeoye.merchant.presentation.authentication;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.presentation.authentication.stackable.AuthenticationStackableComponent;
import com.oyeoye.merchant.presentation.base.PresentedFrameLayout;

import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;

@AutoInjector(AuthenticationPresenter.class)
public class AuthenticationView extends PresentedFrameLayout<AuthenticationPresenter> {

    @Bind(R.id.screen_authentication_toolbar)
    protected Toolbar mToolbar;

    public AuthenticationView(Context context) {
        super(context);
        DaggerService.<AuthenticationStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.screen_authentication, this);
        ButterKnife.bind(view);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.resetMenu(mToolbar);
    }
}

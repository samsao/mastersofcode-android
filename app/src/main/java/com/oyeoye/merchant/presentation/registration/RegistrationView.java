package com.oyeoye.merchant.presentation.registration;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.presentation.base.PresentedFrameLayout;
import com.oyeoye.merchant.presentation.registration.stackable.RegistrationStackableComponent;

import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.OnClick;

@AutoInjector(RegistrationPresenter.class)
public class RegistrationView extends PresentedFrameLayout<RegistrationPresenter> {

    public RegistrationView(Context context) {
        super(context);
        DaggerService.<RegistrationStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.screen_registration, this);
        ButterKnife.bind(view);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @OnClick(R.id.screen_registration_pick_a_place_btn)
    public void pickPlace() {
        presenter.pickPlace();
    }
}

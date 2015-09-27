package com.oyeoye.merchant.presentation.receiver;

import android.os.Bundle;

import com.oyeoye.merchant.AppDependencies;
import com.oyeoye.merchant.DaggerScope;
import com.oyeoye.merchant.RootActivity;
import com.oyeoye.merchant.presentation.AbstractPresenter;

import architect.robot.AutoStackable;
import autodagger.AutoComponent;
import autodagger.AutoExpose;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoStackable(
        component = @AutoComponent(dependencies = RootActivity.class, superinterfaces = AppDependencies.class),
        pathWithView = ReceiverView.class
)
@DaggerScope(ReceiverPresenter.class)
@AutoExpose(ReceiverPresenter.class)
public class ReceiverPresenter extends AbstractPresenter<ReceiverView> {

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);
    }
}

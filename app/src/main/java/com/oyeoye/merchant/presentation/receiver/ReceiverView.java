package com.oyeoye.merchant.presentation.receiver;

import android.content.Context;
import android.view.View;

import com.oyeoye.merchant.R;
import com.oyeoye.merchant.presentation.base.PresentedLinearLayout;
import com.oyeoye.merchant.presentation.receiver.stackable.ReceiverStackableComponent;

import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.ButterKnife;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(ReceiverPresenter.class)
public class ReceiverView extends PresentedLinearLayout<ReceiverPresenter> {

    public ReceiverView(Context context) {
        super(context);
        DaggerService.<ReceiverStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.screen_receiver, this);
        ButterKnife.bind(view);
    }
}

package com.samsao.projecttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.samsao.projecttemplate.presentation.RootActivityPresenter;
import com.samsao.projecttemplate.presentation.main.stackable.MainStackable;

import javax.inject.Inject;

import architect.Navigator;
import architect.NavigatorView;
import architect.TransitionsMapping;
import architect.commons.ActivityArchitector;
import architect.commons.Architected;
import architect.commons.transition.Config;
import architect.commons.transition.LateralViewTransition;
import architect.robot.DaggerService;
import autodagger.AutoComponent;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

@AutoComponent(
        dependencies = MainApplication.class,
        superinterfaces = AppDependencies.class
)
@AutoInjector
@DaggerScope(RootActivity.class)
public class RootActivity extends AppCompatActivity implements RootActivityPresenter.Activity {

    private MortarScope mScope;
    private Navigator mNavigator;

    @Inject
    protected RootActivityPresenter mMainActivityPresenter;

    @Bind(R.id.navigator_container)
    protected NavigatorView mNavigatorView;

    @Override
    public Object getSystemService(String name) {
        if (mScope != null && mScope.hasService(name)) {
            return mScope.getService(name);
        }
        return super.getSystemService(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        ButterKnife.bind(this);

        mScope = ActivityArchitector.onCreateScope(this, savedInstanceState, new Architected() {
            @Override
            public Navigator createNavigator(MortarScope scope) {
                Navigator navigator = Navigator.create(scope, null, new Navigator.Config().dontRestoreStackAfterKill(true));
                navigator.transitions().register(new TransitionsMapping()
                                .byDefault(new LateralViewTransition(new Config().duration(300)))
//                                .show(SomeView.class).withTransition(new FadeModalTransition(new Config().duration(250)))
                );
                return navigator;
            }

            @Override
            public void configureScope(MortarScope.Builder builder, MortarScope parentScope) {
                RootActivityComponent component = DaggerRootActivityComponent.builder()
                        .mainApplicationComponent(parentScope.<MainApplicationComponent>getService(DaggerService.SERVICE_NAME))
                        .build();
                builder.withService(DaggerService.SERVICE_NAME, component);
            }
        });

        DaggerService.<RootActivityComponent>get(this).inject(this);
        mMainActivityPresenter.takeView(this);

        // it is usually the best to create the mNavigator after everything else
        mNavigator = ActivityArchitector.onCreateNavigator(this, savedInstanceState, mNavigatorView, new MainStackable());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mNavigator.delegate().onNewIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(mScope).onSaveInstanceState(outState);
        mNavigator.delegate().onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mNavigator.delegate().onStart();
    }

    @Override
    protected void onStop() {
        mNavigator.delegate().onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mMainActivityPresenter.dropView(this);

        mNavigator.delegate().onDestroy();
        mNavigator = null;

        if (isFinishing() && mScope != null) {
            mScope.destroy();
            mScope = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mNavigator.delegate().onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMainActivityPresenter.onCreateOptionsMenu(getMenuInflater(), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mMainActivityPresenter.onOptionsItemSelected(item);
    }

    /**
     * Set the toolbar
     *
     * @param toolbar
     */
    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }

    /**
     * Set the toolbar title
     *
     * @param resId
     */
    public void setToolbarTitle(@StringRes int resId) {
        setToolbarTitle(getString(resId));
    }

    /**
     * Set the toolbar title
     *
     * @param title
     */
    public void setToolbarTitle(String title) {
        if (title.isEmpty()) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

}

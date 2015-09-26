package com.samsao.projecttemplate.presentation.main;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.samsao.projecttemplate.AppDependencies;
import com.samsao.projecttemplate.DaggerScope;
import com.samsao.projecttemplate.RootActivity;
import com.samsao.projecttemplate.presentation.AbstractPresenter;
import com.samsao.projecttemplate.presentation.RootActivityPresenter;
import com.samsao.projecttemplate.presentation.SetupToolbarHandler;

import architect.robot.AutoStackable;
import autodagger.AutoComponent;
import autodagger.AutoExpose;

/**
 * @author jliang
 * @since 2015-08-04
 */
@AutoStackable(
        component = @AutoComponent(dependencies = RootActivity.class, superinterfaces = AppDependencies.class),
        pathWithView = MainView.class
)
@DaggerScope(MainPresenter.class)
@AutoExpose(MainPresenter.class)
public class MainPresenter extends AbstractPresenter<MainView> implements SetupToolbarHandler {

    private final RootActivityPresenter mRootActivityPresenter;

    public MainPresenter(RootActivityPresenter mainActivityPresenter) {
        mRootActivityPresenter = mainActivityPresenter;
    }

    public void resetMenu(Toolbar toolbar) {
        mRootActivityPresenter.setupToolbar(getView().mToolbar);
        mRootActivityPresenter.resetMenu(this);
    }

    @Override
    public void setupToolbarMenu(ActionBar actionBar, MenuInflater menuInflater, Menu menu) {
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("TEMPLATE PROJECT");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

}

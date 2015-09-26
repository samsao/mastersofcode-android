package com.samsao.projecttemplate.presentation;

import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * @author jliang
 * @since 2015-08-07
 */
public interface SetupToolbarHandler {
    void setupToolbarMenu(ActionBar actionBar, MenuInflater menuInflater, Menu menu);
    boolean onOptionsItemSelected(MenuItem item);
}

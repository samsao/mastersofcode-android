package com.samsao.projecttemplate;

import com.samsao.projecttemplate.business.managers.PreferenceManager;

import autodagger.AutoComponent;

/**
 * Expose application wide dependencies
 *
 * @author jfcartier
 * @since 15-06-09
 */
@AutoComponent(
        modules = MainApplication.Module.class
)
public interface AppDependencies {
    PreferenceManager preferenceManager();
}

package com.oyeoye.merchant;

import com.oyeoye.merchant.business.PreferenceManager;

import autodagger.AutoComponent;

@AutoComponent(
        modules = MainApplication.Module.class
)
public interface AppDependencies {
    PreferenceManager preferenceManager();
}

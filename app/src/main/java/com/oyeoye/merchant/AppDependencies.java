package com.oyeoye.merchant;

import com.oyeoye.merchant.business.PreferenceManager;
import com.oyeoye.merchant.business.UserManager;

import autodagger.AutoComponent;

@AutoComponent(
        modules = MainApplication.Module.class
)
public interface AppDependencies {
    UserManager userManager();
    PreferenceManager preferenceManager();
}

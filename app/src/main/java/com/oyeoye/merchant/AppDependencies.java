package com.oyeoye.merchant;

import com.oyeoye.merchant.business.DealManager;
import com.oyeoye.merchant.business.TransactionNfcManager;
import com.oyeoye.merchant.business.PreferenceManager;
import com.oyeoye.merchant.business.UserManager;
import com.oyeoye.merchant.business.camera.PhotoUtil;

import autodagger.AutoComponent;

@AutoComponent(
        modules = MainApplication.Module.class
)
public interface AppDependencies {
    PhotoUtil photoUtil();
    UserManager userManager();
    PreferenceManager preferenceManager();
    DealManager dealManager();
    TransactionNfcManager nfcManager();
}

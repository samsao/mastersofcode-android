package com.oyeoye.merchant;

import javax.inject.Scope;

@Scope
public @interface DaggerScope {
    Class<?> value();
}

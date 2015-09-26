package com.samsao.projecttemplate;

import javax.inject.Scope;

/**
 * @author jfcartier
 * @since 15-08-06
 */
@Scope
public @interface DaggerScope {
    Class<?> value();
}

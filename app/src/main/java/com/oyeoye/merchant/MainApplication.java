package com.oyeoye.merchant;

import android.app.Application;

import architect.robot.DaggerService;
import autodagger.AutoComponent;
import mortar.MortarScope;
import timber.log.Timber;

@AutoComponent(
        modules = MainApplication.Module.class,
        superinterfaces = AppDependencies.class
)
@DaggerScope(MainApplication.class)
public class MainApplication extends Application {
    private MortarScope mScope;
    public static String SCOPE_NAME = "root";

    @Override
    public Object getSystemService(String name) {
        return (mScope != null && mScope.hasService(name)) ? mScope.getService(name) : super.getSystemService(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        MainApplicationComponent component = DaggerMainApplicationComponent.builder().module(new Module()).build();

        mScope = MortarScope.buildRootScope()
                .withService(DaggerService.SERVICE_NAME, component)
                .build(SCOPE_NAME);
    }

    @dagger.Module
    public class Module {

//        /**
//         * provide the preference manager to the whole app
//         *
//         * @return PreferenceManager
//         */
//        @Provides
//        @DaggerScope(MainApplication.class)
//        public PreferenceManager providesPreferenceManager() {
//            return new PreferenceManager(getApplicationContext());
//        }
    }
}

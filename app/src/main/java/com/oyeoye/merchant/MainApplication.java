package com.oyeoye.merchant;

import android.app.Application;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.Digits;
import com.oyeoye.merchant.business.DealManager;
import com.oyeoye.merchant.business.PreferenceManager;
import com.oyeoye.merchant.business.UserManager;
import com.oyeoye.merchant.business.api.Constants;
import com.oyeoye.merchant.business.api.CustomOkClient;
import com.oyeoye.merchant.business.api.exception.ApiException;
import com.oyeoye.merchant.business.api.exception.BadRequestException;
import com.oyeoye.merchant.business.api.exception.HostUnreachableException;
import com.oyeoye.merchant.business.api.exception.InternalServerErrorException;
import com.oyeoye.merchant.business.api.exception.MalformedUrlException;
import com.oyeoye.merchant.business.api.exception.NetworkTimeoutException;
import com.oyeoye.merchant.business.api.exception.NotFoundException;
import com.oyeoye.merchant.business.api.exception.RetrofitException;
import com.oyeoye.merchant.business.api.exception.UnauthorizedException;
import com.oyeoye.merchant.business.camera.PhotoUtil;
import com.squareup.okhttp.OkHttpClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import architect.robot.DaggerService;
import autodagger.AutoComponent;
import dagger.Provides;
import io.fabric.sdk.android.Fabric;
import mortar.MortarScope;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.JacksonConverter;
import timber.log.Timber;

@AutoComponent(
        modules = MainApplication.Module.class,
        superinterfaces = AppDependencies.class
)
@DaggerScope(MainApplication.class)
public class MainApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "FgHDjfsV6nAWKrY4a16kE3Ddx";
    private static final String TWITTER_SECRET = "GHYydknprEIAb5hygRzgPcptOChY7mqC8EzDOhzwikxjDFv3Ab";

    private MortarScope mScope;
    public static String SCOPE_NAME = "root";
    private MainApplicationComponent mComponent;

    @Override
    public Object getSystemService(String name) {
        return (mScope != null && mScope.hasService(name)) ? mScope.getService(name) : super.getSystemService(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Fabric.with(this, new TwitterCore(authConfig), new Digits());
        } else {
            Fabric.with(this, new Crashlytics(), new TwitterCore(authConfig), new Digits());
        }

        mComponent = DaggerMainApplicationComponent.builder().module(new Module()).build();

        mScope = MortarScope.buildRootScope()
                .withService(DaggerService.SERVICE_NAME, mComponent)
                .build(SCOPE_NAME);
    }

    public MainApplicationComponent getComponent() {
        return mComponent;
    }

    @dagger.Module
    public class Module {

        /**
         * provide the preference manager to the whole app
         *
         * @return PreferenceManager
         */
        @Provides
        @DaggerScope(MainApplication.class)
        public PreferenceManager providesPreferenceManager() {
            return new PreferenceManager(getApplicationContext());
        }

        /**
         * provide the restAdapter to the whole app
         *
         * @return RestAdapter
         */
        @Provides
        @DaggerScope(MainApplication.class)
        public RestAdapter providesRestAdapter(CustomOkClient retrofitClient, final PreferenceManager preferenceManager) {
            return new RestAdapter.Builder()
                    .setClient(retrofitClient)
                    .setLogLevel(RestAdapter.LogLevel.BASIC)
                    .setEndpoint(Constants.API_HOSTNAME)
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            request.addHeader("Accept-Language", getResources().getConfiguration().locale.getLanguage());
                            if (!TextUtils.isEmpty(preferenceManager.getApiToken())) {
                                request.addHeader("Authorization", "Bearer " + preferenceManager.getApiToken());
                            }
                        }
                    })
                    .setConverter(new JacksonConverter())
                    .setErrorHandler(new ErrorHandler() {
                        @Override
                        public Throwable handleError(RetrofitError retrofitError) {
                            if (retrofitError.getCause() != null) {
                                final Throwable cause = retrofitError.getCause();
                                if (cause instanceof UnknownHostException) {
                                    return new HostUnreachableException();
                                } else if (cause instanceof SocketTimeoutException) {
                                    return new NetworkTimeoutException();
                                } else if (cause instanceof MalformedURLException) {
                                    return new MalformedUrlException();
                                }
                            } else if (retrofitError.getResponse() != null) {
                                final retrofit.client.Response response = retrofitError.getResponse();
                                RetrofitException exception;
                                switch (response.getStatus()) {
                                    case 500:
                                        exception = new InternalServerErrorException();
                                        break;
                                    case 400:
                                        exception = new BadRequestException();
                                        break;
                                    case 401:
                                        exception = new UnauthorizedException();
                                        break;
                                    case 404:
                                        exception = new NotFoundException();
                                        break;
                                    default:
                                        exception = new ApiException();
                                        break;
                                }
                                exception.setMessage(retrofitError);
                                return exception;
                            }
                            return retrofitError;
                        }
                    })
                    .build();

        }

        /**
         * provide the retrofit client to the restAdapter
         *
         * @return Client
         */
        @Provides
        @DaggerScope(MainApplication.class)
        public CustomOkClient providesRetrofitClient() {
            OkHttpClient okHttpClient = new OkHttpClient();
            return new CustomOkClient(okHttpClient, getApplicationContext());
        }

        @Provides
        @DaggerScope(MainApplication.class)
        public UserManager providesUserManager(PreferenceManager preferenceManager, RestAdapter restAdapter) {
            return new UserManager(preferenceManager, restAdapter);
        }

        @Provides
        @DaggerScope(MainApplication.class)
        public PhotoUtil providesPhotoUtil() {
            return new PhotoUtil(MainApplication.this);
        }

        @Provides
        @DaggerScope(MainApplication.class)
        public DealManager providesDealManager(RestAdapter restAdapter) {
            return new DealManager(restAdapter);
        }
    }
}

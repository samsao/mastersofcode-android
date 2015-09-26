package com.oyeoye.merchant;

import android.app.Application;

import com.oyeoye.merchant.business.PreferenceManager;
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
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import architect.robot.DaggerService;
import autodagger.AutoComponent;
import dagger.Provides;
import mortar.MortarScope;
import retrofit.ErrorHandler;
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
        public RestAdapter providesRestAdapter(CustomOkClient retrofitClient) {
            return new RestAdapter.Builder()
                    .setClient(retrofitClient)
                    .setEndpoint(Constants.API_HOSTNAME)
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
    }
}

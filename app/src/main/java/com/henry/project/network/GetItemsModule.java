package com.henry.project.network;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by henry_hsu on 2017/10/13.
 */
//use dagger to build api manager
@Module
public class GetItemsModule {
    private final Context mContext;
    public GetItemsModule(Context context) {
        this.mContext = context;
    }

    @Provides
    @Singleton
    public ApiInterceptor provideApiInterceptor() {
        return new ApiInterceptor(mContext);
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(ApiInterceptor interceptor) {
        OkHttpClient okhttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        return okhttpClient;
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okhttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient)
                .baseUrl("https://api.github.com")
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    public ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    public ApiManager provideApiManager(ApiService service) {
        return new ApiManager(service);
    }

    @Provides
    public Context provideContext() {
        return mContext;
    }
}

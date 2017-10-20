package com.henry.project;

import android.app.Application;

import com.henry.project.network.ApiComponent;
import com.henry.project.network.ComponentHolder;
import com.henry.project.network.DaggerApiComponent;
import com.henry.project.network.GetItemsModule;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // create one network component using for this app
        // the DaggerApiComponent is created by dagger
        ApiComponent apiComponent = DaggerApiComponent.builder()
                .getItemsModule(new GetItemsModule(this))
                .build();
        ComponentHolder.setAppComponent(apiComponent);
    }
}
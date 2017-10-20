package com.henry.project.network;

/**
 * Created by henry_hsu on 2017/10/13.
 */

public class ComponentHolder {
    //It is hold our DaggerComponent in application
    private static ApiComponent sApiComponent;

    public static void setAppComponent(ApiComponent appComponent) {
        sApiComponent = appComponent;
    }

    public static ApiComponent getAppComponent() {
        return sApiComponent;
    }
}

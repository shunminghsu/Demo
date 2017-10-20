package com.henry.project.network;

import com.henry.project.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {GetItemsModule.class})
@Singleton
public interface ApiComponent {
    void inject(MainActivity mainActivity);

}

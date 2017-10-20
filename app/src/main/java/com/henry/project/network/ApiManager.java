package com.henry.project.network;

import android.util.Log;

import com.henry.project.RecyclerViewAdapter;
import com.henry.project.model.Item;
import com.henry.project.model.ItemCollection;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by henry_hsu on 2017/10/13.
 */

public class ApiManager {
    private final ApiService mApiService;

    @Inject
    public ApiManager(ApiService service) {
        this.mApiService = service;
    }

    public void updateItems(final int tab_position, final RecyclerViewAdapter viewAdapter, int load_more_page) {

        Call<ItemCollection> call = mApiService.getItems(getTabType(tab_position), load_more_page);
        call.enqueue(new Callback<ItemCollection>() {
            @Override
            public void onResponse(Call<ItemCollection>call, Response<ItemCollection> response) {
                List<Item> itemList = response.body().getResults();
                viewAdapter.update(itemList);
            }

            @Override
            public void onFailure(Call<ItemCollection>call, Throwable t) {
                Log.e("ApiManager", t.toString());
            }
        });
    }

    private String getTabType(int tabPosition) {
        switch (tabPosition) {
            case 0:
                return "city_guide";
            case 1:
                return "shop";
            case 2:
                return "eat";
            default:
                return "unknown";
        }
    }
}

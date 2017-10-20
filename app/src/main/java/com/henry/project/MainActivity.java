package com.henry.project;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v7.app.AppCompatActivity;

import com.henry.project.network.ApiManager;
import com.henry.project.network.ComponentHolder;

import java.util.ArrayList;


import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    //This project using dagger to build network api functions
    @Inject
    ApiManager mApiManager;
    ArrayList<MyTab> mTabs = new ArrayList<MyTab>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        //load tabs from the array resource in strings.xml
        String[] tabs = getResources().getStringArray(R.array.tabs_array);
        for (String tab : tabs) {
            mTabs.add(new MyTab(this, mOnLoadMoreListener));
        }
        MyPagerAdapter adapter = new MyPagerAdapter(mTabs, inflater, mOnTabBuildListener);
        viewPager.setAdapter(adapter);
        //since this demo project only have 3 pages, so we just set 2
        viewPager.setOffscreenPageLimit(2);
        //set tabs title
        for (int i = 0;i < tabs.length;i++) {
            tabLayout.getTabAt(i).setText(tabs[i]);
        }
        //construct mApiManager
       ComponentHolder.getAppComponent().inject(this);
    }

    //When UI is ready to show, we can start to get items from server
    MyPagerAdapter.OnTabBuildListener mOnTabBuildListener = new MyPagerAdapter.OnTabBuildListener() {

        @Override
        public void onTabBuild(int tab_position) {
            mApiManager.updateItems(tab_position, mTabs.get(tab_position).getAdapter(), 0);
        }
    };

    //It is for infinite scroll
    MyTab.OnLoadMoreListener mOnLoadMoreListener = new MyTab.OnLoadMoreListener() {

        @Override
        public void onLoadMore(int page_position, int load_more_page) {
            mApiManager.updateItems(page_position, mTabs.get(page_position).getAdapter(), load_more_page);
        }
    };
}

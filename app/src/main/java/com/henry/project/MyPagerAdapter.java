package com.henry.project;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class MyPagerAdapter extends PagerAdapter {
    private ArrayList<MyTab> mTabs;
    private LayoutInflater mInflater;
    private OnTabBuildListener mListener;
    public interface OnTabBuildListener {
        void onTabBuild(int tab_position);
    }


    public MyPagerAdapter(ArrayList<MyTab> tabs, LayoutInflater inflater, OnTabBuildListener listener) {
        mTabs = tabs;
        mInflater = inflater;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        MyTab tab = mTabs.get(position);
        View root = tab.build(position, mInflater);
        mListener.onTabBuild(position);
        container.addView(root);

        return root;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
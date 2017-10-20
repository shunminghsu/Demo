package com.henry.project;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


public class MyTab {
    private final int LOAD_MORE_THRESHOLD = 5;
    private int mLoadMorePage = 1;
    private View mRootView;
    private View mEmpty;
    private View mLoadingContainer;
    private View mListContainer;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerAdapter;
    private Context mContext;
    private OnLoadMoreListener mListener;
    public interface OnLoadMoreListener {
        void onLoadMore(int page_position, int load_more_page);
    }

    public MyTab(Context context, OnLoadMoreListener listener) {
        mContext = context;
        mListener = listener;
    }

    public View build(final int tab_position, LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.pager_layout, null);
        mLoadingContainer = mRootView.findViewById(R.id.loading_container);
        mListContainer = mRootView.findViewById(R.id.list_container);
        mEmpty = mRootView.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerAdapter = new RecyclerViewAdapter(this, mContext);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (totalItemCount <= (lastVisibleItem + LOAD_MORE_THRESHOLD)) {
                    //assume server will return 10 items in each page
                    //check the list total count to avoid duplicate page
                    if (mLoadMorePage == (totalItemCount/10) ) {
                        mListener.onLoadMore(tab_position, mLoadMorePage);
                        mLoadMorePage++;
                    }
                }
            }
        });

        return mRootView;
    }

    public RecyclerViewAdapter getAdapter() {
        return mRecyclerAdapter;
    }

    public void showLoadingResult(boolean empty) {
        mLoadingContainer.setVisibility(View.GONE);
        if (empty) {
            mListContainer.setVisibility(View.GONE);
            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mListContainer.setVisibility(View.VISIBLE);
            mEmpty.setVisibility(View.GONE);
        }
    }

}
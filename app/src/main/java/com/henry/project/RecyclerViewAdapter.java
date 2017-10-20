package com.henry.project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.henry.project.model.Item;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final int ITEM_TYPE_DEFAULT = 0;
    private final int ITEM_TYPE_JUST_IMAGE = 1;

    private List<Item> mItemList;
    private MyTab mTab;
    private Context mContext;

    public RecyclerViewAdapter(MyTab tab, Context context) {
        mTab = tab;
        mContext = context;
    }

    boolean isEmpty() {
        return mItemList == null ? true : mItemList.isEmpty();
    }

    public void update(List<Item> itemList) {
        if (mItemList == null) {
            mItemList = itemList;
            mTab.showLoadingResult(isEmpty());
            notifyDataSetChanged();
        } else {
            //Load more data
            int positionStart = mItemList.size();
            for (Item item : itemList) {
                mItemList.add(item);
            }
            notifyItemRangeChanged(positionStart, itemList.size());
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recyclerview_item_type1 will show image, title, and description
        //recyclerview_item_type2 will just show a image
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_TYPE_JUST_IMAGE) {
            return new ViewHolder(layoutInflater.inflate(R.layout.recyclerview_item_type2, parent, false));
        } else {
            return new ViewHolder(layoutInflater.inflate(R.layout.recyclerview_item_type1, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {

        if (getItemViewType(position) == ITEM_TYPE_JUST_IMAGE) {
            Picasso.with(mContext).load(mItemList.get(position).getImagePath())
                    .into(holder.image);
        } else {
            holder.title.setText(mItemList.get(position).getTitle());
            holder.description.setText(mItemList.get(position).getDescription());
            Picasso.with(mContext).load(mItemList.get(position).getImagePath())
                    .into(holder.image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mItemList.get(position).isJustImage()) {
            return ITEM_TYPE_JUST_IMAGE;
        } else {
            return ITEM_TYPE_DEFAULT;
        }
    }

    @Override
    public int getItemCount() {
        if (mItemList != null)
            return mItemList.size();
        else
            return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        ImageView image;
        TextView title;
        TextView description;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.image = (ImageView) itemView.findViewById(R.id.image);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}

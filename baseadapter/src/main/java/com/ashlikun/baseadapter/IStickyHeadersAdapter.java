package com.ashlikun.baseadapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface IStickyHeadersAdapter<HeaderViewHolder extends RecyclerView.ViewHolder> {


    HeaderViewHolder onCreateViewHolder(ViewGroup parent);


    void onBindViewHolder(HeaderViewHolder headerViewHolder, int position);


    long getHeaderId(int position);
}

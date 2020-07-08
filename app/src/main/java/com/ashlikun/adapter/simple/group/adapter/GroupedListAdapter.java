package com.ashlikun.adapter.simple.group.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.group.GroupedCommonAdapter;
import com.ashlikun.adapter.simple.R;
import com.ashlikun.adapter.simple.group.javaben.ChildEntity;
import com.ashlikun.adapter.simple.group.javaben.GroupEntity;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;

/**
 * 这是普通的分组Adapter 每一个组都有头部、尾部和子项。
 */
public class GroupedListAdapter extends GroupedCommonAdapter<GroupEntity> {


    public GroupedListAdapter(Context context, ArrayList<GroupEntity> groups) {
        super(context, groups);
        addItemTypeHeader(TYPE_HEADER, R.layout.adapter_header);
        addItemTypeFooter(TYPE_FOOTER, R.layout.adapter_footer);
        addItemTypeChild(TYPE_CHILD, R.layout.adapter_child);
    }


    @Override
    public boolean hasHeader(int groupPosition, GroupEntity itemData) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition, GroupEntity itemData) {
        return false;
    }

    @Override
    public int getChildrenCount(int groupPosition, GroupEntity itemData) {
        ArrayList<ChildEntity> children = itemData.getChildren();
        return children == null ? 0 : children.size();
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, GroupEntity itemData, int groupPosition) {
        Log.e("onBindHeaderViewHolder", "groupPosition = " + groupPosition);
        holder.setText(R.id.tv_header, itemData.getHeader());
    }

    @Override
    public void onBindFooterViewHolder(ViewHolder holder, GroupEntity itemData, int groupPosition) {
        Log.e("onBindFooterViewHolder", "groupPosition = " + groupPosition);
        holder.setText(R.id.tv_footer, itemData.getFooter());
    }

    @Override
    public void onBindChildViewHolder(ViewHolder holder, GroupEntity itemData, int groupPosition, int childPosition) {
        Log.e("onBindChildViewHolder", "groupPosition = " + groupPosition + "     childPosition = " + childPosition);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof FlexboxLayoutManager.LayoutParams){
            ((FlexboxLayoutManager.LayoutParams) layoutParams).setFlexGrow(0.5f);
            //加上这句话，保证主轴上的子view均分主轴剩余空间
            //完全可以给网络数据添加type，在这里手动设置同一主轴上子View占据的空间
        }
        ChildEntity entity = itemData.getChildren().get(childPosition);
        holder.setText(R.id.tv_child, entity.getChild());
    }
}

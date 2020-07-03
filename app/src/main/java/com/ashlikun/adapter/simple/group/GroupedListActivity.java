package com.ashlikun.adapter.simple.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashlikun.adapter.recyclerview.group.GroupedCommonAdapter;
import com.ashlikun.adapter.recyclerview.group.StickyHelper;
import com.ashlikun.adapter.simple.R;
import com.ashlikun.adapter.simple.group.adapter.GroupedListAdapter;
import com.ashlikun.adapter.simple.group.javaben.ChildEntity;
import com.ashlikun.adapter.simple.group.javaben.GroupEntity;
import com.ashlikun.adapter.simple.group.javaben.GroupModel;


/**
 * 分组的列表
 */
public class GroupedListActivity extends AppCompatActivity {

    private TextView tvTitle;
    private RecyclerView rvList;
    private GroupedListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        rvList = (RecyclerView) findViewById(R.id.rv_list);

        tvTitle.setText("分组的列表");
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 8; i < 10; i++) {
                    GroupEntity entity = adapter.getGroupData(i);
                    entity.setHeader(entity.getHeader() + " NEW");
                    entity.setFooter(entity.getFooter() + " NEW");
                    for (int j = 0; j < entity.getChildren().size(); j++) {
                        ChildEntity childEntity = entity.getChildren().get(j);
                        childEntity.setChild(childEntity.getChild() + "  new ");
                    }
                }
                adapter.notifyChildRangeChanged(9, 0, 5);
            }
        });
        rvList.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new GroupedListAdapter(this, GroupModel.getGroups(10, 5));
        adapter.setOnHeaderClickListener(new GroupedListAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedCommonAdapter adapter, int groupPosition) {
                Toast.makeText(GroupedListActivity.this, "组头：groupPosition = " + groupPosition,
                        Toast.LENGTH_LONG).show();
            }
        });
        adapter.setOnFooterClickListener(new GroupedListAdapter.OnFooterClickListener() {

            @Override
            public void onFooterClick(GroupedCommonAdapter adapter, int groupPosition) {
                Toast.makeText(GroupedListActivity.this, "组尾：groupPosition = " + groupPosition,
                        Toast.LENGTH_LONG).show();
            }
        });
        adapter.setOnChildClickListener(new GroupedListAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedCommonAdapter adapter, int groupPosition, int childPosition) {

            }
        });
        rvList.setAdapter(adapter);

        new StickyHelper(rvList, (FrameLayout) findViewById(R.id.stickyLayout));

    }

    public static void openActivity(Context context) {
        Intent intent = new Intent(context, GroupedListActivity.class);
        context.startActivity(intent);
    }
}

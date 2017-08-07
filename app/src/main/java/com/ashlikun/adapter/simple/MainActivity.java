package com.ashlikun.adapter.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.support.SectionAdapter;
import com.ashlikun.adapter.simple.databinding.ItemView1Binding;
import com.ashlikun.adapter.simple.databinding.ItemViewBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> listDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 40; i++) {
            listDatas.add("aaa" + i);
        }
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

//        recyclerView.setAdapter(new CommonAdapter<String, ItemViewBinding>(this, R.layout.item_view, listDatas) {
//
//            @Override
//            public void convert(ViewHolder<ItemViewBinding> holder, String o) {
//
//            }
//        });
//        recyclerView.setAdapter(new MultiItemCommonAdapter<String>(this, listDatas) {
//
//            @Override
//            public int getLayoutId(int itemType) {
//                if (itemType == 1) {
//                    return R.layout.item_view1;
//                } else {
//                    return R.layout.item_view2;
//                }
//            }
//
//            @Override
//            public int getItemViewType(int position, String o) {
//                if (position == 2 || position == 8 || position == 15 || position == 25 || position == 31) {
//                    return 1;
//                }
//                return 2;
//            }
//
//            @Override
//            public void convert(ViewHolder holder, String s) {
//
//            }
//        });
        recyclerView.setAdapter(new SectionAdapter<String>(this, R.layout.item_view1, listDatas) {


            @Override
            public int sectionHeaderLayoutId() {
                return R.layout.item_view;
            }

            @Override
            public String getTitle(int position,String o) {
                if(position == 5){
                    return "aaaaa";
                }else{
                    return "77777777777";
                }
            }

            @Override
            public void setTitle(ViewHolder holder, String o) {
                ((ItemViewBinding) holder.dataBind).setItemData("ssssssssssss");
            }

            @Override
            public void convert(ViewHolder holder, String s) {
                ((ItemView1Binding) holder.dataBind).setItemData(s);
            }
        });
    }
}

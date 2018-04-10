package com.ashlikun.adapter.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.ashlikun.adapter.recyclerview.multiltem.MultipleAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Data> listDatas = new ArrayList<>();
    MultipleAdapter adapter;
    ArrayList<NeibuData> neibuData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        for (int i = 0; i < 40; i++) {
//            listDatas.add("列表数据" + i);
//        }
        Data data1 = new Data(1);
        data1.data = new NeibuData("第一個");
        listDatas.add(data1);
        Data data2 = new Data(2);
        data2.data2 = new Neibu2Data("第2個");
        listDatas.add(data2);
        Data data3 = new Data(3);
        data3.data = new NeibuData("第3個");
        listDatas.add(data3);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        //DataBinding   adater
//        recyclerView.setAdapter(adapter = new SectionAdapter<String, ItemViewBinding>(this, R.layout.item_view, listDatas) {
//            @Override
//            public void convert(DataBindHolder<ItemViewBinding> holder, String s) {
//                holder.dataBind.setItemData(s);
//            }
//
//            @Override
//            public int sectionHeaderLayoutId() {
//                return R.layout.item_view2;
//            }
//
//            @Override
//            public String getTitle(int position, String s) {
//                if (position % 4 == 0) {
//                    return "标题" + position / 4;
//                }
//                return null;
//            }
//
//            @Override
//            public void setTitle(DataBindHolder holder, String title, String s) {
//                holder.setText(R.id.textView, title);
//            }
//        });
        adapter = new MultipleAdapter(layoutManager, false);

        for (int i = 0; i < 10; i++) {
            neibuData.add(new NeibuData("我是第一种" + i));
        }
        adapter.addAdapter(new MyAdapter.AdapterItem1(this, neibuData));


        ArrayList<Neibu2Data> neibu2Data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            neibu2Data.add(new Neibu2Data("我是第二种" + i));
        }
        adapter.addAdapter(new MyAdapter.AdapterItem2(this, neibu2Data));


        ArrayList<Neibu3Data> neibu3Data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            neibu3Data.add(new Neibu3Data("我是第三种" + i));
        }
        adapter.addAdapter(new MyAdapter.AdapterItem3(this, neibu3Data));
        adapter.addAdapter(new MyAdapter.AdapterItemSing(this));

        recyclerView.setAdapter(adapter);
        //adapter.setCustomAnimation(new SlideInBottomAnimation());
//        //DataBindAdapter
//        recyclerView.setAdapter(new DataBindAdapter<String, ItemViewBinding>(this, R.layout.item_view, listDatas) {
//            @Override
//            public void convert(DataBindHolder<ItemViewBinding> holder, String o) {
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
//        recyclerView.setAdapter(new SectionAdapter(this, R.layout.item_view1, listDatas) {
//
//
//            @Override
//            public void convert(DataBindHolder<ViewDataBinding> holder, Object o) {
//
//            }
//
//            @Override
//            public int sectionHeaderLayoutId() {
//                return 0;
//            }
//
//            @Override
//            public String getTitle(int position, Object o) {
//                return null;
//            }
//
//            @Override
//            public void setTitle(DataBindHolder holder, String title, Object o) {
//
//            }
//
//            @Override
//            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//            }
//        });
        recyclerView.setItemAnimator(null);
    }

    public void onClick(View view) {
        neibuData.add(new NeibuData("新加的数据" + neibuData.size()));
        adapter.notifyChanged();
    }
//    @Override
//    public String getTitle(int position,String o) {
//        if(position == 5){
//            return "7888888888888888888";
//        }else{
//            return "77777777777";
//        }
//    }
//
//    @Override
//    public void setTitle(ViewHolder holder, String title, String o) {
//        ((ItemViewBinding) holder.dataBind).setItemData(title);
//    }
//
//    @Override
//    public void convert(ViewHolder holder, String s) {
//        ((ItemView1Binding) holder.dataBind).setItemData(s);
//    }
}

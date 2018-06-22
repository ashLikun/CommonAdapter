package com.ashlikun.adapter.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.ashlikun.adapter.recyclerview.multiltem.MultipleAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Data> listDatas = new ArrayList<>();
    MultipleAdapter adapter;
    ArrayList<NeibuData> neibuData = new ArrayList<>();
    private RelativeLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(R.string.app_name);
        MenuItem menuItem = toolbar.getMenu().add("一般列表");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent = new Intent(MainActivity.this, CommentActivity.class);
//                startActivity(intent);
                onClick(null);
                return false;
            }
        });
//
        Data data1 = new Data(1);
        data1.data = new NeibuData("第一個");
        listDatas.add(data1);
        Data data2 = new Data(2);
        data2.data2 = new Neibu2Data("第2個");
        listDatas.add(data2);
        Data data3 = new Data(3);
        data3.data = new NeibuData("第3個");
        listDatas.add(data3);

        rootView = findViewById(R.id.rootView);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.bottomMargin = 60;
        params.rightMargin = 60;
        TextView textView = new TextView(this);
        textView.setText("aaaaaa\naaaaa");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("aaa", "ssss");
            }
        });
        rootView.addView(textView, params);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //DataBinding   adater
        adapter = new MultipleAdapter(layoutManager, false);
        recyclerView.setAdapter(adapter);
        for (int i = 0; i < 10; i++) {
            neibuData.add(new NeibuData("我是第一种" + i));
        }
//        adapter.addAdapter(new MyAdapter.AdapterItem1(this, neibuData).setViewType(Integer.MAX_VALUE));
//
//        ArrayList<Neibu2Data> neibu2Data = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            neibu2Data.add(new Neibu2Data("我是第二种" + i));
//        }
//        adapter.addAdapter(new MyAdapter.AdapterItem2(this, neibu2Data).setViewType(2));

        ArrayList<Neibu3Data> neibu3Data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            neibu3Data.add(new Neibu3Data("我是第三种" + i));
        }
        MyAdapter.AdapterItem3 adapterItem3 = new MyAdapter.AdapterItem3(this, neibu3Data);
        adapterItem3.setViewType(3);
        adapterItem3.getAdapterAnimHelp().setLastPositionOn();
        adapterItem3.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                Log.e("aa", "aaaaaaaaaaaaaaaaaaa");
            }
        });
        adapter.addAdapter(adapterItem3);
//        adapter.addAdapter(new MyAdapter.AdapterItemSing(this).setViewType(4));
        // recyclerView.setItemAnimator(null);
    }

    public void onClick(View view) {
        neibuData.add(new NeibuData("新加的数据" + neibuData.size()));
        adapter.notifyChanged();
    }
}

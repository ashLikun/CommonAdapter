package com.ashlikun.adapter.simple;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.alibaba.android.vlayout.Cantor;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.ashlikun.adapter.recyclerview.vlayout.MultipleAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Data> listDatas = new ArrayList<>();
    MultipleAdapter adapter;
    ArrayList<NeibuData> neibuData = new ArrayList<>();
    private RelativeLayout rootView;
    ArrayList<Neibu3Data> neibu3Data = new ArrayList<>();
    MyAdapter.AdapterItem3 adapterItem3;
    MyAdapter.AdapterItem2 adapterItem2;
    ArrayList<Neibu2Data> neibu2Data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        long aa = Cantor.getCantor(Integer.MAX_VALUE, Integer.MAX_VALUE - 1);
        long[] result = new long[2];
        Cantor.reverseCantor(aa, result);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(R.string.app_name);
        MenuItem menuItem = toolbar.getMenu().add("一般列表");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, CommentActivity.class);
                startActivity(intent);
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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MultipleAdapter(layoutManager, false);
        VirtualLayoutManager layoutManager2 = new VirtualLayoutManager(this);
        for (int i = 0; i < 20; i++) {
            neibu2Data.add(new Neibu2Data("我是第二种" + i));
        }
        adapterItem2 = new MyAdapter.AdapterItem2(this, neibu2Data);
        adapter.addAdapter(adapterItem2);

        for (int i = 0; i < 20; i++) {
            neibu3Data.add(new Neibu3Data("我是第三种" + i));
        }
        adapterItem3 = new MyAdapter.AdapterItem3(this, neibu3Data);
        adapter.addAdapter(adapterItem3);
        ArrayList<Neibu3Data> neibu4Data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            neibu4Data.add(new Neibu3Data("我是第四种" + (i + 1)));
        }
        MyAdapter.AdapterItem4 adapterItem4 = new MyAdapter.AdapterItem4(this, neibu4Data);
        adapter.addAdapter(adapterItem4);
    }

    public void onClick(View view) {
        neibu2Data.clear();
        for (int i = 0; i < 20; i++) {
            neibu2Data.add(new Neibu2Data("我是第二种" + i));
        }
        neibu3Data.clear();
        for (int i = 0; i < 20; i++) {
            neibu3Data.add(new Neibu3Data("新加的数据" + neibuData.size()));
        }
        adapter.notifyChanged();
    }
}

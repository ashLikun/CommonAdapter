package com.ashlikun.adapter.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.CommonAdapter;
import com.ashlikun.xrecycleview.RecyclerViewWithHeaderAndFooter;

import java.util.ArrayList;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/5/4 0004　下午 2:47
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
public class CommentActivity extends AppCompatActivity {
    RecyclerViewWithHeaderAndFooter recycleView;
    CommonAdapter<NeibuData> adapter;
    ArrayList<NeibuData> neibuData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        recycleView = findViewById(R.id.recyclerView);
        for (int i = 0; i < 100; i++) {
            neibuData.add(new NeibuData("我是第一种" + i));
        }

        initView();
    }

    private void initView() {
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(adapter = new CommonAdapter<NeibuData>(this, R.layout.item_view, neibuData) {
            @Override
            public void convert(ViewHolder holder, NeibuData neibuData) {
                holder.setText(R.id.textView, neibuData.name);
            }

            @Override
            public void onViewAttachedToWindow(ViewHolder holder) {
                super.onViewAttachedToWindow(holder);
            }

            @Override
            public void onViewDetachedFromWindow(ViewHolder holder) {
                super.onViewDetachedFromWindow(holder);
            }

            @Override
            public void onItemClick(int viewType,ViewGroup parent, View view, NeibuData data, int position) {
                super.onItemClick(viewType,parent, view, data, position);
                Log.e("aaaa", position + "");
//                Intent intent = new Intent();
//                intent.setClassName("com.tencent.mm",
//                        "com.tencent.mm.ui.tools.ShareToTimeLineUI");
//                intent.setAction("android.intent.action.SEND");
//                intent.setType("image/*");
//                intent.putExtra("Kdescription", "你猜你猜");
//                File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/Camera/IMG_20180504_160534.jpg");
//                Uri contentUri = FileProvider.getUriForFile(CommentActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", file);
//                intent.putExtra(Intent.EXTRA_STREAM, contentUri);
//                startActivity(intent);
            }
        });
       // addHeard();
    }

    private void addHeard() {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher_round);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600);
        imageView.setLayoutParams(params);
        recycleView.addHeaderView(imageView);
    }
}

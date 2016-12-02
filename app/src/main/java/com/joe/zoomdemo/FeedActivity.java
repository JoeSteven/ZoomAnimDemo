package com.joe.zoomdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.joe.zoomdemo.adapter.FeedAdapter;
import com.joe.zoomdemo.model.FeedItem;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity implements FeedAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private FeedAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_feed);
        mAdapter = new FeedAdapter(this);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        mAdapter.setData(createFakeList());
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position, FeedItem item) {
        DetailActivity.startActivity(this, view, item);
    }

    private List<FeedItem> createFakeList() {
        List<FeedItem> list = new ArrayList<>(10);
        list.add(new FeedItem("http://img.hb.aicdn.com/877f3d03af5e5b9b74c73e5238444e020b56f15c43d2a-KhZwNi", 690, 1035));
        list.add(new FeedItem("http://img.hb.aicdn.com/566bdcbc6dee9480bfa919ba223c84df318224e4144de-xttC7f", 600, 900));
        list.add(new FeedItem("http://img.hb.aicdn.com/5f9eee3cb654ea979e55a56fa127b116202406a21e62d-d8fKut", 540, 960));
        list.add(new FeedItem("http://img.hb.aicdn.com/e59cb694c15e73ba6f157dcb3fc61b0d9c3a44991351c-WeeJIN", 500, 660));
        list.add(new FeedItem("http://img.hb.aicdn.com/9f1c39dd9c7f0efd4e5429fbf98aa134e1e8a3ff32124-xrPn3y", 736, 1107));
        list.add(new FeedItem("http://img.hb.aicdn.com/33a7438683c38c9a9e8e99c1d46b813e38c2123b12e19-30FXez", 500, 750));
        return list;
    }
}

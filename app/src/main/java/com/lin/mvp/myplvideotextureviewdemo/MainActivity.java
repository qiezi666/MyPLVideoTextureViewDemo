package com.lin.mvp.myplvideotextureviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.lin.mvp.myplvideotextureviewdemo.base.BaseSpacesItemDecoration;
import com.lin.mvp.myplvideotextureviewdemo.bean.NeteastVideoSummary;
import com.lin.mvp.myplvideotextureviewdemo.bean.NeteastVideoSummaryV9LG4B3A0;
import com.lin.mvp.myplvideotextureviewdemo.callback.OnItemClickAdapter;
import com.lin.mvp.myplvideotextureviewdemo.contract.VideoContract;
import com.lin.mvp.myplvideotextureviewdemo.presenter.VideoPresenter;
import com.lin.mvp.myplvideotextureviewdemo.util.ClickUtils;
import com.lin.mvp.myplvideotextureviewdemo.util.MeasureUtil;
import com.lin.mvp.myplvideotextureviewdemo.view.activity.VideoActivity;
import com.lin.mvp.myplvideotextureviewdemo.view.adapter.BaseRecyclerAdapter;
import com.lin.mvp.myplvideotextureviewdemo.view.adapter.VideoAdapter;
import com.lin.mvp.myplvideotextureviewdemo.view.widget.AutoLoadMoreRecyclerView;
import com.socks.library.KLog;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements VideoContract.IVideoFragment {

    protected static final String VEDIO_ID = "V9LG4B3A0";

    private static final String TAG = "MainActivity";

    private int position ;

    protected String mVideoId;
    @InjectView(R.id.prograss)
    AVLoadingIndicatorView mLoadingView;
    @InjectView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @InjectView(R.id.recycler_view)
    AutoLoadMoreRecyclerView mRecyclerView;

    private BaseRecyclerAdapter<NeteastVideoSummaryV9LG4B3A0> mAdapter;

    VideoPresenter mPresenter;

    private boolean isrefreshing;
    private boolean isLoadMore;
    protected List<NeteastVideoSummaryV9LG4B3A0> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化黄油刀控件绑定框架
        ButterKnife.inject(this);
        //初始化ToolBar
        initToolBar();

        initDatas();
        //初始化控件
        initViews(savedInstanceState);
    }

    private void initDatas() {
        mPresenter = new VideoPresenter(getApplicationContext(), this);
    }

    private void initViews(Bundle state) {
        //4、设置SwipeRefreshLayout 的颜色变化
        mRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);

        //5、设置SwipeRefreshLayout 的刷新事件监听
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (isrefreshing) {
                    KLog.d("ignore manually update!");
                } else {
                    System.out.println("上啦刷新数据");
                    onRefreshData();
                    isrefreshing = true;
                }
            }
        });


        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new VideoAdapter(this, mData, true, layoutManager);
        mAdapter.setOnItemClickListener(new OnItemClickAdapter() {
            @Override
            public void onItemClick(View view, int position) {

                if (ClickUtils.isFastDoubleClick()) {
                    return;
                }

                final String mp4Url = mAdapter.getData().get(position).getMp4_url();
                if (TextUtils.isEmpty(mp4Url)) {
//                    toast("此视频无播放网址哎╮(╯Д╰)╭");
                    return;
                }
                Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                intent.putExtra("videoUrl", mp4Url);
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0,
                                0);
                ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
            }
        });


        mRecyclerView.setAutoLayoutManager(layoutManager).addAutoItemDecoration(
                new BaseSpacesItemDecoration(MeasureUtil.dip2px(MainActivity.this, 4)))
                .setAutoItemAnimator(new DefaultItemAnimator()).setAutoItemAnimatorDuration(250)
                .setAutoAdapter(mAdapter);

        mRecyclerView.setOnLoadMoreListener(new AutoLoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                // 状态停止，并且滑动到最后一位
                isLoadMore = true;
                position+=10;
                System.out.println("position:" + position);
                loadMoreData();
                // 显示尾部加载
                // KLog.e("显示尾部加载前："+mAdapter.getItemCount());
                mAdapter.showFooter();
                // KLog.e("显示尾部加载后："+mAdapter.getItemCount());
//                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });

        loadData();
    }

    private void loadData() {
        mPresenter.getVideoData(VEDIO_ID,0);
    }

    private void loadMoreData() {
        mPresenter.getVideoData(VEDIO_ID,position);
    }

    private void onRefreshData() {
        mPresenter.getVideoData(VEDIO_ID,0);
    }

    private void initToolBar() {

    }

    @Override
    public void updateVideoData(NeteastVideoSummary data) {
        NeteastVideoSummary videoData = data;
        if (videoData != null) {
            mData = videoData.getV9LG4B3A0();
//            mAdapter.setData(mData);

            if (isrefreshing) {
                isrefreshing = false;
                mRefreshLayout.setRefreshing(false);
            } else if (isLoadMore) {
                isLoadMore = false;
                mAdapter.hideFooter();
                if (mData == null || mData.size() == 0) {
                    mRecyclerView.notifyAllLoaded();
//                    toast("全部加载完毕噜(☆＿☆)");
                    Toast.makeText(MainActivity.this,"全部加载完毕噜(☆＿☆)",Toast.LENGTH_SHORT).show();
                } else {
                    mAdapter.addMoreData(mData);
                    mRecyclerView.notifyMoreLoaded();
                }
            }else {
                mAdapter.setData(mData);
            }
        }

    }

    @Override
    public void showProgressDialog() {
        mLoadingView.show();
    }

    @Override
    public void hidProgressDialog() {
        mLoadingView.hide();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(MainActivity.this,error,Toast.LENGTH_SHORT).show();
        System.out.println("error:"+error);
        mAdapter.hideFooter();
        mRecyclerView.notifyMoreLoadedFail();
        System.out.println("加载失败");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
        mPresenter.unsubcrible();
    }
}

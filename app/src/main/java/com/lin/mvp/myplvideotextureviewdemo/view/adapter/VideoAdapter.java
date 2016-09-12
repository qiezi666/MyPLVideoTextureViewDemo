package com.lin.mvp.myplvideotextureviewdemo.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lin.mvp.myplvideotextureviewdemo.R;
import com.lin.mvp.myplvideotextureviewdemo.bean.NeteastVideoSummaryV9LG4B3A0;
import com.lin.mvp.myplvideotextureviewdemo.util.MeasureUtil;

import java.util.List;
import java.util.Random;

/**
 * Created by mvp on 2016/9/12.
 */

public class VideoAdapter extends BaseRecyclerAdapter <NeteastVideoSummaryV9LG4B3A0>{

    Context mContext;
    List<NeteastVideoSummaryV9LG4B3A0> mData;
    boolean useAnimation;
    RecyclerView.LayoutManager layoutManager;
    Random mRandom = new Random();

    public VideoAdapter(Context context, List<NeteastVideoSummaryV9LG4B3A0> data, boolean useAnimation, RecyclerView.LayoutManager layoutManager) {
        super(context, data, useAnimation, layoutManager);
        this.mContext = context;
        this.mData = data;
        this.useAnimation = useAnimation;
        this.layoutManager = layoutManager;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_video_summary;
    }

    @Override
    public void bindData(BaseRecyclerViewHolder holder, int position, NeteastVideoSummaryV9LG4B3A0 item) {

        final ImageView imageView = holder.getImageView(R.id.iv_video_summary);
        final ViewGroup.LayoutParams params = imageView.getLayoutParams();
        // KLog.e("图片网址：" + item.kpic);
        if (item.picWidth == -1 && item.picHeight == -1) {
            item.picWidth = MeasureUtil.getScreenSize(mContext).x / 2 - MeasureUtil
                    .dip2px(mContext, 4) * 2 - MeasureUtil.dip2px(mContext, 2);
            item.picHeight = (int) (item.picWidth * (mRandom.nextFloat() / 2 + 0.7));
        }
        params.width = item.picWidth;
        params.height = item.picHeight;
        imageView.setLayoutParams(params);

        Glide.with(mContext).load(item.getCover()).asBitmap()
                .placeholder(R.drawable.ic_loading).error(R.drawable.ic_fail)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

        holder.getTextView(R.id.tv_video_summary).setText(Html.fromHtml(item.getTitle()));

    }
}

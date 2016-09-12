package com.lin.mvp.myplvideotextureviewdemo.contract;

import com.lin.mvp.myplvideotextureviewdemo.base.BasePresenter;
import com.lin.mvp.myplvideotextureviewdemo.base.IBaseFragment;
import com.lin.mvp.myplvideotextureviewdemo.bean.NeteastVideoSummary;

/**
 * Created by mvp on 2016/9/12.
 */

public interface VideoContract {

    public interface IVideoPresenter extends BasePresenter {
        /**
         * 获取最新的日报数据
         *
         * @return
         */
        void getVideoData(String id ,int startPage);
    }

    public interface IVideoFragment extends IBaseFragment {

        void updateVideoData(NeteastVideoSummary videoData);
    }
}

package com.lin.mvp.myplvideotextureviewdemo.api;

import com.lin.mvp.myplvideotextureviewdemo.bean.NeteastVideoSummary;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by mvp on 2016/9/12.
 */

public interface VideoApi {

    /**
     * 视频 http://c.3g.163.com/nc/video/list/V9LG4B3A0/n/10-10.html
     */
    public static final String Video = "nc/video/list/";
    public static final String VIDEO_CENTER = "/n/";
    public static final String VIDEO_END_URL = "-10.html";
    // 热点视频
    public static final String VIDEO_HOT_ID = "V9LG4B3A0";
    // 娱乐视频
    public static final String VIDEO_ENTERTAINMENT_ID = "V9LG4CHOR";
    // 搞笑视频
    public static final String VIDEO_FUN_ID = "V9LG4E6VR";
    // 精品视频
    public static final String VIDEO_CHOICE_ID = "00850FRB";

    /**
     * 网易视频列表 例子：http://c.m.163.com/nc/video/list/V9LG4B3A0/n/0-10.html
     *
     * @param id        视频类别id
     * @param startPage 开始的页码
     * @return 被观察者
     */
    @GET("nc/video/list/{id}/n/{startPage}-10.html")
    Observable<NeteastVideoSummary> getVideoList(
             @Path("id") String id, @Path("startPage") int startPage);

}

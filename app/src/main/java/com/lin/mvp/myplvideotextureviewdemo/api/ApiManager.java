package com.lin.mvp.myplvideotextureviewdemo.api;


import com.lin.mvp.myplvideotextureviewdemo.MyApplication;
import com.lin.mvp.myplvideotextureviewdemo.util.NetWorkUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mvp on 2016/9/2.
 */

public class ApiManager {
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (NetWorkUtil.isNetWorkAvailable(MyApplication.getContext())) {
                int maxAge = 60; // 在线缓存在1分钟内可读取
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // 离线时缓存保存4周
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    public static ApiManager apiManage;
    private static File httpCacheDirectory = new File(MyApplication.getContext().getExternalCacheDir(), "newsCache");
    private static int cacheSize = 10 * 1024 * 1024; // 10 MiB
    private static Cache cache = new Cache(httpCacheDirectory, cacheSize);
    private static OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .cache(cache)
            .build();


    private Object newsClientMonitor = new Object();

    public static ApiManager getInstence() {
        if (apiManage == null) {
            synchronized (ApiManager.class) {
                if (apiManage == null) {
                    apiManage = new ApiManager();
                }
            }
        }
        return apiManage;
    }



    public  VideoApi mVideoApi;
    public static final String GANK_URL = "http://c.3g.163.com/";
    public VideoApi getVideoService() {
        if (mVideoApi == null) {
            synchronized (newsClientMonitor) {
                if (mVideoApi == null) {
                    mVideoApi = new Retrofit.Builder()
                            .baseUrl(GANK_URL)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(VideoApi.class);
                }
            }
        }
        return mVideoApi;
    }
}

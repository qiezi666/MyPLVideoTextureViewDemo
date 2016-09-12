# MyPLVideoTextureViewDemo
这是一个自己学习android视频播放的demo。<br>
基于[PLDroidPlayer](https://github.com/pili-engineering/PLDroidPlayer).<br>
使用PLDroidPlayer的PLVideoTextureView<br>
包含功能：<br>
1. 支持屏幕右边手势上滑  --加大音量<br>
2. 支持屏幕左边手势上滑  --加大亮度<br>
3. 缓冲<br>
4. 根据尺寸自动转换横屏<br>
5. 双击放大


## PLDroidPlayer
PLDroidPlayer 是一个适用于 Android 平台的音视频播放器 SDK，可高度定制化和二次开发，为 Android 开发者提供了简单、快捷的接口，帮助开发者在 Android 平台上快速开发播放器应用。

PLDroidPlayer 目前基于 [ijkplayer](https://github.com/pili-engineering/ijkplayer), 感谢 ijkplayer ，相应的修改详见：https://github.com/pili-engineering/ijkplayer

## 程序预览
![pic1](https://github.com/qiezi666/MyPLVideoTextureViewDemo/blob/master/image/pic1.png)<br>
![pic2](https://github.com/qiezi666/MyPLVideoTextureViewDemo/blob/master/image/pic2.png)<br>
![pic3](https://github.com/qiezi666/MyPLVideoTextureViewDemo/blob/master/image/pic3.png)<br>
![pic4](https://github.com/qiezi666/MyPLVideoTextureViewDemo/blob/master/image/pic4.png)<br>
![pic5](https://github.com/qiezi666/MyPLVideoTextureViewDemo/blob/master/image/pic5.png)<br>
## 用法
### 1. sdk集成
首先，下载最新版本的
 [PLDroidPlayer SDK](https://github.com/pili-engineering/PLDroidPlayer/wiki/4-%E5%BF%AB%E9%80%9F%E5%BC%80%E5%A7%8B#4.1)

### 2. 导入 SDK
![](https://camo.githubusercontent.com/a9eaec2a3cc9736743cf7583f3b36a756a97743c/687474703a2f2f377875696c342e636f6d312e7a302e676c622e636c6f7564646e2e636f6d2f73646b2d696d706f72742d73616d706c652d322e706e67)


### 3. 添加依赖
    compile files('libs/pldroid-player-1.3.1.jar')
    compile 'com.qiniu:happy-dns:0.2.+'
	compile 'com.qiniu.pili:pili-android-qos:0.8.+'
### 4. 添加权限

	<uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WAKE_LOCK"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
### 5. 布局
	<com.pili.pldroid.player.widget.PLVideoTextureView
        android:id="@+id/videoView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_centerInParent="true"/>
### 6. 播放器设置（需要用到sdk中demo里的MediaController）

#### 6.1 初始化
	mVideoView = (PLVideoTextureView) findViewById(R.id.videoView);

#### 6.2 设置加载动画
	mLoadingView = (ProgressBar) findViewById(R.id.LoadingView);
    mVideoView.setBufferingIndicator(mLoadingView);
#### 6.3 音量与亮度的初始化
	mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
    mOperationBg = (ImageView) findViewById(R.id.operation_bg);
    mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
#### 6.4 获取最大音量
	mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
#### 6.5 初始化手势识别器
	mGestureDetector = new GestureDetector(this, new VideoActivity.MyGestureListener());
#### 6.6 播放参数配置
	    AVOptions options = new AVOptions();

        // the unit of timeout is ms

        // 准备超时时间，包括创建资源、建立连接、请求码流等，单位是 ms
        // 默认值是：无
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
        // Some optimization with buffering mechanism when be set to 1
        // 解码方式，codec＝1，硬解; codec=0, 软解
        // 默认值是：0
        int isLiveStreaming =IS_LIVE_STREAMING;
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, isLiveStreaming);
        // 当前播放的是否为在线直播，如果是，则底层会有一些播放优化
        // 默认值是：0
        if (isLiveStreaming == 1) {
            // 是否开启"延时优化"，只在在线直播流中有效
            // 默认值是：0
            options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
        }

        // 1 -> hw codec enable, 0 -> disable [recommended]
	//        int codec = getIntent().getIntExtra("mediaCodec", 0);
        int codec = MEDIA_CODEC;
        // 解码方式，codec＝1，硬解; codec=0, 软解
        // 默认值是：0
        options.setInteger(AVOptions.KEY_MEDIACODEC, codec);

        // whether start play automatically after prepared, default value is 1
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

        mVideoView.setAVOptions(options);
#### 6.7 关联播放控制器
	 // You can also use a custom `MediaController` widget
        mMediaController = new MediaController(this, false, isLiveStreaming == 1);
        mVideoView.setMediaController(mMediaController);


#### 6.8 设置播放监听
	    mVideoView.setOnInfoListener(mOnInfoListener);
	    // mVideoView.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        mVideoView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        mVideoView.setOnCompletionListener(mOnCompletionListener);
        mVideoView.setOnSeekCompleteListener(mOnSeekCompleteListener);
        mVideoView.setOnErrorListener(mOnErrorListener);
        mVideoView.setVideoPath(mVideoPath);
        mVideoView.setDisplayAspectRatio(mDisplayAspectRatio);
        mVideoView.setOnPreparedListener(mOnPreparedListener);

#### 6.9 根据尺寸旋转方向监听
	    mVideoView.setOnVideoSizeChangedListener(new PLMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int width, int height) {
	//                Logger.i("width:" + width + "---heightL:" + height);
                System.out.println("width:" + width + "---heightL:" + height);
                if (width > height&&mVideoRotation==0) {
                    //旋转方向
                    System.out.println("旋转方向");
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                //如果视频角度是90度
                if(mVideoRotation==90)
                {
                    //旋转视频
                    System.out.println("旋转方向" + mVideoRotation);
                    mVideoView.setDisplayOrientation(270);
                }


            }
        });

#### 6.10 开启播放器
		mVideoView.setVideoPath(mVideoPath);
        mVideoView.start();
### 7. 播放器监听实现
#### 7.1 旋转角度
	public void onClickRotate(View v) {
        mRotation = (mRotation + 90) % 360;
        mVideoView.setDisplayOrientation(mRotation);
    }
#### 7.2 缓冲设置
	private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
            switch (what) {
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    System.out.println("正在缓冲----\"");
                    //开始缓存，暂停播放
                    if (mVideoView != null) {
                        mVideoView.pause();
                    }
                    needResume = true;
                    mLoadingView.setVisibility(View.VISIBLE);
                    break;
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
                case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    System.out.println("缓冲完成----");
                    //缓存完成，继续播放
                    if (needResume)
                        if (mVideoView != null) {
                            mVideoView.start();
                        }
                    mLoadingView.setVisibility(View.GONE);
                    break;
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_BYTES_UPDATE:
                    //显示 下载速度
                    System.out.println("download rate:" + extra);
                    //mListener.onDownloadRateChanged(arg2);
                    break;
                case 10001:
                    //保存视频角度
                    mVideoRotation=extra;

                    break;
            }
            System.out.println("onInfo:" + what + "___" + extra);
            return false;
        }
    }
#### 7.3 手势监听实现
	private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        /** 双击 */
        @Override
        public boolean onDoubleTap(MotionEvent e) {

            if(mDisplayAspectRatio == PLVideoTextureView.ASPECT_RATIO_FIT_PARENT){
                mDisplayAspectRatio = PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT;
            }else {
                mDisplayAspectRatio = PLVideoTextureView.ASPECT_RATIO_FIT_PARENT;
            }

            if (mVideoView != null) mVideoView.setDisplayAspectRatio(mDisplayAspectRatio);
            return true;
        }

        /** 滑动 */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();

            if (mOldX > windowWidth * 4.0 / 5)// 右边滑动
                onVolumeSlide((mOldY - y) / windowHeight);
            else if (mOldX < windowWidth / 5.0)// 左边滑动
                onBrightnessSlide((mOldY - y) / windowHeight);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

#### 7.4 改变声音大小
	private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            // 显示
            mOperationBg.setImageResource(R.drawable.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        // 变更进度条
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width
                * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);
    }
#### 7.5 滑动改变亮度
	private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            // 显示
            mOperationBg.setImageResource(R.drawable.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        mOperationPercent.setLayoutParams(lp);
    }
## 总结
  >这是自己在学习android视频播放的一个demo，只是基本使用，其中还有许多缺陷。


## 鸣谢

[PLDroidPlayer](https://github.com/pili-engineering/PLDroidPlayer)

[weyye](http://www.weyye.me/detail/Android-PLDroidPlayer%E6%92%AD%E6%94%BE/)

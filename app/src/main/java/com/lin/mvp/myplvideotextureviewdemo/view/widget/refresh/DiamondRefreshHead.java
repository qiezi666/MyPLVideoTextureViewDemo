package com.lin.mvp.myplvideotextureviewdemo.view.widget.refresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.lin.mvp.myplvideotextureviewdemo.R;


/**
 * ClassName: UcRerfreshHead<p>
 * Author:oubowu<p>
 * Fuction: 下拉刷新的头部，方块风格<p>
 * CreateDate:2016/2/9 14:05<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public class DiamondRefreshHead extends RefreshHead {

    // 画笔
    private Paint mPaint;
    // 方块的长宽
    private int mCubeSize;
    // view的宽度
    private int mWidth;
    // view的长度
    private int mHeight;
    // 获取屏幕宽高的Point
    private Point mScreenSize = new Point();
    // 整个方块矩阵的总宽度
    private int mTotalWidth;
    // 第1组矩阵相对view底部的偏移量
    private int mFirstBottomOffset;
    // 第2组矩阵相对view底部的偏移量
    private int mSecondBottomOffset;
    // 第3组矩阵相对view底部的偏移量
    private int mThirdBottomOffset;
    // 第4组矩阵相对view底部的偏移量
    private int mFourthBottomOffset;
    // 相对view底部的偏移量的基准
    private int mBaseBottomOffset;
    // 加载动画时矩阵的偏移量
    private int mLoadingOffset;
    // 用于做加载动画的值动画
    private ValueAnimator mLoadingAnimator;
    // 透明度
    private int mAlpha = 255;

    public DiamondRefreshHead(Context context) {
        super(context);
        init();
    }

    public DiamondRefreshHead(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        // 画笔填充
        mPaint.setStyle(Paint.Style.FILL);

        // 获取屏幕宽高
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(mScreenSize);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 算出宽度
        mWidth = measureSize(widthMeasureSpec, mScreenSize.x + getPaddingLeft() + getPaddingRight());

        // 初始化方块的长宽
        if (mCubeSize == 0) {
            mCubeSize = (int) (mWidth * 1.0f / 100);
        }

        // 初始化方块矩阵的总宽度
        if (mTotalWidth == 0) {
            mTotalWidth = mCubeSize * 9;
        }

        // 初始化相对底部的偏移量
        if (mBaseBottomOffset == 0) {
            mBaseBottomOffset = mFirstBottomOffset = mSecondBottomOffset = mThirdBottomOffset = mFourthBottomOffset = mCubeSize * 14;
        }

        // 算出高度
        mHeight = measureSize(heightMeasureSpec, mCubeSize * 9 + getPaddingTop() + getPaddingBottom());

        // 若padding值与矩阵总的高度和为负值，为了避免算出 mHeight=-1即mHeight=ViewGroup.LayoutParams.MATCH_PARENT
        // 负值时高度强制设为0
        if (mCubeSize * 9 + getPaddingTop() + getPaddingBottom() < 0) {
            mHeight = 0;
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    // 测量尺寸
    private int measureSize(int measureSpec, int defaultSize) {

        final int mode = MeasureSpec.getMode(measureSpec);
        final int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            return size;
        } else if (mode == MeasureSpec.AT_MOST) {
            return Math.min(size, defaultSize);
        } else if (mode == MeasureSpec.UNSPECIFIED) {
            return defaultSize;
        }

        return size;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        // KLog.e("alpha: " + mAlpha);

        // 绘制第一组方块矩阵
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.material_green_a700));
        mPaint.setAlpha(mAlpha);

        for (int i = 0; i < 2; i++) {
            int offset = i == 1 ? mLoadingOffset : 0;
            canvas.drawRect((mWidth - mTotalWidth) / 2 + mLoadingOffset * 2, mCubeSize * 2 + mCubeSize * 2 * (i + 1) - mFirstBottomOffset - offset + getPaddingTop(),
                    (mWidth - mTotalWidth) / 2 + mCubeSize + mLoadingOffset * 2,
                    mCubeSize * 2 + mCubeSize + mCubeSize * 2 * (i + 1) - mFirstBottomOffset - offset + getPaddingTop(), mPaint);
        }

        canvas.drawRect((mWidth - mTotalWidth) / 2 + mCubeSize * 2 + mLoadingOffset,
                mCubeSize * 2 + mCubeSize * 2 * 2 - mFirstBottomOffset - mLoadingOffset + getPaddingTop(),
                (mWidth - mTotalWidth) / 2 + mCubeSize * 2 + mCubeSize * 5 - mLoadingOffset,
                mCubeSize * 2 + mCubeSize * 2 * 2 + mCubeSize - mFirstBottomOffset - mLoadingOffset + getPaddingTop(), mPaint);

        // 绘制第二组方块矩阵
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.material_purple_700));
        mPaint.setAlpha(mAlpha);

        for (int i = 0; i < 3; i++) {
            int offset = i == 0 ? mLoadingOffset : i == 2 ? -mLoadingOffset : 0;
            canvas.drawRect((mWidth - mTotalWidth) / 2 + mCubeSize * 2 * (i + 1) + offset, mCubeSize * 2 + mCubeSize * 2 - mSecondBottomOffset + getPaddingTop(),
                    (mWidth - mTotalWidth) / 2 + mCubeSize + mCubeSize * 2 * (i + 1) + offset,
                    mCubeSize * 2 + mCubeSize + mCubeSize * 2 - mSecondBottomOffset + getPaddingTop(), mPaint);
        }

        // 绘制第三组方块矩阵
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.material_deep_purple_a700));
        mPaint.setAlpha(mAlpha);

        for (int i = 0; i < 3; i++) {
            int offset = i == 0 ? mLoadingOffset : i == 2 ? -mLoadingOffset : 0;
            canvas.drawRect((mWidth - mTotalWidth) / 2 + mCubeSize * 2 * 4 - mLoadingOffset * 2,
                    mCubeSize * 2 + mCubeSize * 2 * i - mThirdBottomOffset + offset + getPaddingTop(),
                    (mWidth - mTotalWidth) / 2 + mCubeSize + mCubeSize * 2 * 4 - mLoadingOffset * 2,
                    mCubeSize * 2 + mCubeSize + mCubeSize * 2 * i - mThirdBottomOffset + offset + getPaddingTop(), mPaint);
        }

        // 绘制第四组方块矩阵
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.material_deep_orange_a200));
        mPaint.setAlpha(mAlpha);

        for (int i = 0; i < 4; i++) {
            int offset = i == 0 ? mLoadingOffset * 2 : i == 1 ? mLoadingOffset : i == 3 ? -mLoadingOffset : 0;
            canvas.drawRect((mWidth - mTotalWidth) / 2 + mCubeSize * 2 * i + offset, mCubeSize * 2 - mFourthBottomOffset + mLoadingOffset + getPaddingTop(),
                    (mWidth - mTotalWidth) / 2 + mCubeSize + mCubeSize * 2 * i + offset,
                    mCubeSize * 2 + mCubeSize - mFourthBottomOffset + mLoadingOffset + getPaddingTop(), mPaint);
        }
    }

    /**
     * 下拉的时候，方块矩阵的下降上升效果
     *
     * @param ratio 比值
     */
    public void performPull(float ratio) {

        ratio = ratio - 0.90f;
        if (ratio > 1) {
            // 大于100的话，四组矩阵已经降到底
            mFirstBottomOffset = mSecondBottomOffset = mThirdBottomOffset = mFourthBottomOffset = 0;
        } else if (mLoadingAnimator != null && mLoadingAnimator.isRunning()) {
            // 如果当前view已经处于加载的动画效果时，四组矩阵维持降到底的效果
            mFirstBottomOffset = mSecondBottomOffset = mThirdBottomOffset = mFourthBottomOffset = 0;
        } else if (ratio <= 0.25) {
            // 0~25对应第一组下降上升效果
            mFirstBottomOffset = (int) (mBaseBottomOffset * (0.25 - ratio) * 1.0f / 0.25);
            // 第一组在下降上升时，二三四组必定处于在顶部情况
            mSecondBottomOffset = mThirdBottomOffset = mFourthBottomOffset = mBaseBottomOffset;
        } else if (ratio <= 0.50) {
            // 25~50对应第二组下降上升效果
            mSecondBottomOffset = (int) (mBaseBottomOffset * (0.50 - ratio) * 1.0f / 0.25);
            // 此时第一组必定在底部
            mFirstBottomOffset = 0;
            // 此时三四组必定处于在顶部情况
            mThirdBottomOffset = mFourthBottomOffset = mBaseBottomOffset;
        } else if (ratio <= 0.75) {
            // 50~75对应第三组下降上升效果
            mThirdBottomOffset = (int) (mBaseBottomOffset * (0.75 - ratio) * 1.0f / 0.25);
            // 此时第一二组必定在底部
            mFirstBottomOffset = mSecondBottomOffset = 0;
            // 此时四组必定处于在顶部情况
            mFourthBottomOffset = mBaseBottomOffset;
        } else if (ratio <= 1) {
            // 75~100对应第四组下降上升效果
            mFourthBottomOffset = (int) (mBaseBottomOffset * (1 - ratio) * 1.0f / 0.25);
            // 此时第一二三组必定在底部
            mFirstBottomOffset = mSecondBottomOffset = mThirdBottomOffset = 0;
        }

        // 强制刷新绘制
        postInvalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 销毁时移除动画
        stopAnimator(true);
    }

    @Override
    public boolean isLoading() {
        // 正在执行加载动画
        return mLoadingAnimator != null && mLoadingAnimator.isRunning();
    }

    @Override
    public boolean isReadyLoad() {
        // 第四组都到底部的时候，说明准备好执行动画了
        return 0 == mFourthBottomOffset;
    }

    @Override
    public void performLoaded() {
        stopAnimator(false);
        postInvalidate();
    }

    @Override
    public void performLoading() {
        if (mLoadingAnimator != null && mLoadingAnimator.isRunning()) {
            return;
        } else if (mLoadingAnimator != null) {
            mLoadingAnimator.start();
            return;
        }

        mLoadingAnimator = new ValueAnimator();
        mLoadingAnimator.setIntValues(0, mCubeSize * 2);
        mLoadingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLoadingOffset = (int) animation.getAnimatedValue();
                mAlpha = (int) (255 * (1 - animation.getAnimatedFraction() / 1.5));
                postInvalidate();
            }
        });
        mLoadingAnimator.setDuration(300);
        mLoadingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mLoadingAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mLoadingAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mLoadingAnimator.start();
    }

    /**
     * 停止动画
     */
    private void stopAnimator(boolean removeListener) {
        if (mLoadingAnimator != null && mLoadingAnimator.isRunning()) {
            if (removeListener) {
                mLoadingAnimator.removeAllUpdateListeners();
                mLoadingAnimator.removeAllListeners();
            }
            mLoadingAnimator.cancel();
        }
        mLoadingOffset = 0;
        mAlpha = 255;
    }

}

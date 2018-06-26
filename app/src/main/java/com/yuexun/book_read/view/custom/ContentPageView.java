package com.yuexun.book_read.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.yuexun.book_read.control.DataConstants;
import com.yuexun.book_read.view.custom.animation.AnimationProvider;
import com.yuexun.book_read.view.custom.animation.CoverAnimation;
import com.yuexun.book_read.view.custom.animation.NoneAnimation;
import com.yuexun.book_read.view.custom.animation.SimulationAnimation;
import com.yuexun.book_read.view.custom.animation.SlideAnimation;

/**
 * Created by yuexun on 2018/6/21.
 */

public class ContentPageView extends View {

    private final static String TAG = ContentPageView.class.getSimpleName();

    private Context mContext;

    private int mScreenWidth = 0; // 屏幕宽
    private int mScreenHeight = 0; // 屏幕高
    Bitmap mCurPageBitmap = null; // 当前页背景
    Bitmap mNextPageBitmap = null; //下一页背景
    Scroller mScroller;
    private AnimationProvider mAnimationProvider; //动画效果基础类
    private int mBgColor = 0xFFCEC29C;//背景颜色
    //外部注册的封装后的触摸事件监听
    private TouchListener touchListener;
    //触摸初始点
    private int downX = 0;
    private int downY = 0;
    //正在触摸点
    private int moveX = 0;
    private int moveY = 0;
    //是否移动了
    private Boolean isMove = false;
    //是否翻到下一页
    private Boolean isNext = false;
    //是否取消翻页
    private Boolean cancelPage = false;
    //是否没下一页或者上一页
    private Boolean noNext = false;
    //翻页动画是否在执行
    private Boolean isRuning = false;

    public ContentPageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initPage();
    }

    /**
     * 获取当前屏幕宽高，初始化滚动条、基础切换动画
     */

    private void initPage() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565);      //android:LargeHeap=true  use in  manifest application
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565);
        mScroller = new Scroller(getContext(), new LinearInterpolator());
        //加载基础切换动画 默认为无
        mAnimationProvider = new NoneAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mBgColor);
        Log.e("onDraw", "isNext:" + isNext + "          isRuning:" + isRuning);
        if (isRuning) {
            mAnimationProvider.drawMove(canvas);
        } else {
            mAnimationProvider.drawStatic(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int x = (int) event.getX();
        int y = (int) event.getY();
        mAnimationProvider.setTouchPoint(x, y);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = (int) event.getX();
            downY = (int) event.getY();
            moveX = 0;
            moveY = 0;
            isMove = false;
            noNext = false;
            isNext = false;
            isRuning = false;
            mAnimationProvider.setStartPoint(downX, downY);
            abortAnimation();
            Log.e(TAG, "ACTION_DOWN");
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //获取标准判定移动长度
            final int slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            //判断是否移动了
            if (!isMove) {
                isMove = Math.abs(downX - x) > slop || Math.abs(downY - y) > slop;
            }
            if (isMove) {
                isMove = true;
                if (moveX == 0 && moveY == 0) {
                    Log.e(TAG, "isMove");
                    //判断翻得是上一页还是下一页
                    if (x - downX > 0) {
                        isNext = false;
                    } else {
                        isNext = true;
                    }
                    cancelPage = false;
                    if (isNext) {
                        Boolean isNext = touchListener.nextPage();
                        mAnimationProvider.setDirection(AnimationProvider.Direction.next);

                        if (!isNext) {
                            noNext = true;
                            return true;
                        }
                    } else {
                        Boolean isPre = touchListener.prePage();
                        mAnimationProvider.setDirection(AnimationProvider.Direction.pre);

                        if (!isPre) {
                            noNext = true;
                            return true;
                        }
                    }
                    Log.e(TAG, "isNext:" + isNext);
                } else {
                    //判断是否取消翻页
                    if (isNext) {
                        if (x - moveX > 0) {
                            cancelPage = true;
                            mAnimationProvider.setCancel(true);
                        } else {
                            cancelPage = false;
                            mAnimationProvider.setCancel(false);
                        }
                    } else {
                        if (x - moveX < 0) {
                            mAnimationProvider.setCancel(true);
                            cancelPage = true;
                        } else {
                            mAnimationProvider.setCancel(false);
                            cancelPage = false;
                        }
                    }
                    Log.e(TAG, "cancelPage:" + cancelPage);
                }
                moveX = x;
                moveY = y;
                isRuning = true;
                this.postInvalidate();
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.e(TAG, "ACTION_UP");
            if (!isMove) {
                cancelPage = false;
                //是否点击了中间
                if (downX > mScreenWidth / 3 && downX < mScreenWidth * 2 / 3 && downY > mScreenHeight / 5 && downY < mScreenHeight * 4 / 5) {
                    if (touchListener != null) {
                        touchListener.center();
                    }
                    Log.e(TAG, "center");
                    return true;
                } else if (x < mScreenWidth / 2) {
                    isNext = false;
                } else {
                    isNext = true;
                }
                if (isNext) {
                    Boolean isNext = touchListener.nextPage();
                    mAnimationProvider.setDirection(AnimationProvider.Direction.next);
                    if (!isNext) {
                        return true;
                    }
                } else {
                    Boolean isPre = touchListener.prePage();
                    mAnimationProvider.setDirection(AnimationProvider.Direction.pre);
                    if (!isPre) {
                        return true;
                    }
                }
            }

            if (cancelPage && touchListener != null) {
                touchListener.cancel();
            }

            Log.e(TAG, "isNext:" + isNext);
            if (!noNext) {
                isRuning = true;
                mAnimationProvider.startAnimation(mScroller);
                this.postInvalidate();
            }
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            float x = mScroller.getCurrX();
            float y = mScroller.getCurrY();
            mAnimationProvider.setTouchPoint(x, y);
            if (mScroller.getFinalX() == x && mScroller.getFinalY() == y) {
                isRuning = false;
            }
            postInvalidate();
        }
        super.computeScroll();
    }

    /**
     * 绘制初始动画
     */

    public void abortAnimation() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
            mAnimationProvider.setTouchPoint(mScroller.getFinalX(), mScroller.getFinalY());
            postInvalidate();
        }
    }

    public void setPageMode(int pageMode) {
        switch (pageMode) {
            case DataConstants.PAGE_MODE_SIMULATION:
                mAnimationProvider = new SimulationAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
                break;
            case DataConstants.PAGE_MODE_COVER:
                mAnimationProvider = new CoverAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
                break;
            case DataConstants.PAGE_MODE_SLIDE:
                mAnimationProvider = new SlideAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
                break;
            case DataConstants.PAGE_MODE_NONE:
                mAnimationProvider = new NoneAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
                break;
            default:
                mAnimationProvider = new SimulationAnimation(mCurPageBitmap, mNextPageBitmap, mScreenWidth, mScreenHeight);
        }
    }

    public Bitmap getCurPage() {
        return mCurPageBitmap;
    }

    public Bitmap getNextPage() {
        return mNextPageBitmap;
    }

    public void setBgColor(int color) {
        mBgColor = color;
    }

    public void setTouchListener(TouchListener touchListener) {
        this.touchListener = touchListener;
    }

    public interface TouchListener {
        void center();

        Boolean prePage();

        Boolean nextPage();

        void cancel();
    }
}

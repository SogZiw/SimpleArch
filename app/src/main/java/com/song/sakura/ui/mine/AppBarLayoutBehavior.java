package com.song.sakura.ui.mine;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.google.android.material.appbar.AppBarLayout;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * 解决appbarLayout若干问题：
 * （1）快速滑动appbarLayout会出现回弹
 * （2）快速滑动appbarLayout到折叠状态下，立马下滑，会出现抖动的问题
 * （3）滑动appbarLayout，无法通过手指按下让其停止滑动
 */
public class AppBarLayoutBehavior extends AppBarLayout.Behavior {

    private boolean isFlinging;
    private boolean shouldBlockNestedScroll;

    public AppBarLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull AppBarLayout child, @NonNull MotionEvent ev) {
        shouldBlockNestedScroll = isFlinging;

        LogUtils.e("onInterceptTouchEvent: " + "shouldBlockNestedScroll: " + shouldBlockNestedScroll + "    isFlinging:  " + isFlinging);
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                stopAppbarLayoutFling(child);  //手指触摸屏幕的时候停止fling事件
                break;
        }

        return super.onInterceptTouchEvent(parent, child, ev);
    }

    /**
     * 反射获取私有的flingRunnable 属性，考虑support 28以后变量名修改的问题
     */
    private Field getFlingRunnableField() throws NoSuchFieldException {
        try {
            // support design 27及以下版本
            Class<?> headerBehaviorType = this.getClass().getSuperclass().getSuperclass();
            return headerBehaviorType.getDeclaredField("mFlingRunnable");
        } catch (NoSuchFieldException e) {
            // 可能是28及以上版本
            Class<?> headerBehaviorType = this.getClass().getSuperclass().getSuperclass().getSuperclass();
            return headerBehaviorType.getDeclaredField("flingRunnable");
        }
    }

    /**
     * 反射获取私有的scroller 属性，考虑support 28以后变量名修改的问题
     */
    private Field getScrollerField() throws NoSuchFieldException {
        try {
            // support design 27及以下版本
            Class<?> headerBehaviorType = this.getClass().getSuperclass().getSuperclass();
            return headerBehaviorType.getDeclaredField("mScroller");
        } catch (NoSuchFieldException e) {
            // 可能是28及以上版本
            Class<?> headerBehaviorType = this.getClass().getSuperclass().getSuperclass().getSuperclass();
            return headerBehaviorType.getDeclaredField("scroller");
        }
    }

    /**
     * 停止appbarLayout的fling事件
     */
    private void stopAppbarLayoutFling(AppBarLayout appBarLayout) {
        //通过反射拿到HeaderBehavior中的flingRunnable变量
        try {
            Field flingRunnableField = getFlingRunnableField();
            Field scrollerField = getScrollerField();
            flingRunnableField.setAccessible(true);
            scrollerField.setAccessible(true);

            Runnable flingRunnable = (Runnable) flingRunnableField.get(this);
            OverScroller overScroller = (OverScroller) scrollerField.get(this);
            if (flingRunnable != null) {
                LogUtils.e("存在flingRunnable");
                appBarLayout.removeCallbacks(flingRunnable);
                flingRunnableField.set(this, null);
            }
            if (overScroller != null && !overScroller.isFinished()) {
                overScroller.abortAnimation();
                LogUtils.e("存在overScroller");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout parent, @NonNull AppBarLayout child, @NonNull View directTargetChild, View target, int nestedScrollAxes, int type) {
        stopAppbarLayoutFling(child);
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        LogUtils.e("onNestedPreScroll:  target:" + target.getClass() + " ," + " child: " + child.getClass() + "  ,consumed[1]: " + consumed[1] + " ," + " ,dy:" + dy + " ,type:" + type);

        //type返回1时，表示当前target处于非touch的滑动，
        //该bug的引起是因为appbar在滑动时，CoordinatorLayout内的实现NestedScrollingChild2接口的滑动子类还未结束其自身的fling
        //所以这里监听子类的非touch时的滑动，然后block掉滑动事件传递给AppBarLayout
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            isFlinging = true;
        }
        LogUtils.e("Offset: " + getTopAndBottomOffset() + "   ,TotalScrollRange: " + child.getTotalScrollRange());

        //修复AppBarLayout在折叠和展开的中间状态时下滑ViewPager内的RecyclerView的滑动速度问题
        if (target instanceof RecyclerView
                && dy < 0 && Math.abs(getTopAndBottomOffset()) < child.getTotalScrollRange()) {
            consumed[1] = dy / 2;
        }
        if (!shouldBlockNestedScroll) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        } else {
            //修复滑动RecyclerView的回弹问题
            if (target instanceof RecyclerView
                    && Math.abs(getTopAndBottomOffset()) == child.getTotalScrollRange()
                    && type == ViewCompat.TYPE_NON_TOUCH) {
                ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH);
//                stopAppbarLayoutFling(child);
            }
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
//        LogUtils.e("onNestedScroll: target:" + target.getClass() + "  ,child:"
//                + child + " ,dyConsumed:" + dyConsumed + " " + ",type:" + type + " " + ",consumed:" + Arrays.toString(consumed));
        if (!shouldBlockNestedScroll) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        }
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, @NonNull View target, float velocityX, float velocityY) {
        LogUtils.e("onNestedPreFling: target:" + target.getClass() + "  , velocityY:" + velocityY);
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout abl, View target, int type) {
        LogUtils.e("onStopNestedScroll");
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
        isFlinging = false;
        shouldBlockNestedScroll = false;
    }

}

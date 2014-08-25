/**
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RemoteViews.RemoteView;

/***
 * 简单的viewanimator，可以在已经添加的两个或多个view之间进行动画切换
 * 一次可以显示一个子view，根据需要可以以特定间隔时间自动切换
 * 如何设置切换动画====》in out animation
 * 学习handler的使用和动态注册广播接收器的使用
 * Simple {@link ViewAnimator} that will animate between two or more views
 * that have been added to it.  Only one child is shown at a time.  If
 * requested, can automatically flip between each child at a regular interval.
 *
 * @attr ref android.R.styleable#ViewFlipper_flipInterval
 * @attr ref android.R.styleable#ViewFlipper_autoStart
 */
@RemoteView
public class ViewFlipper extends ViewAnimator {
    private static final String TAG = "ViewFlipper";
    private static final boolean LOGD = false;

    //默认3秒切换,学习这些良好的写法
    private static final int DEFAULT_INTERVAL = 3000;

    private int mFlipInterval = DEFAULT_INTERVAL;
    //是否自动切换
    private boolean mAutoStart = false;
    //是否正在切换
    private boolean mRunning = false;
    private boolean mStarted = false;
    private boolean mVisible = false;
    private boolean mUserPresent = true;

    public ViewFlipper(Context context) {
        super(context);
    }

    public ViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                com.android.internal.R.styleable.ViewFlipper);
        mFlipInterval = a.getInt(
                com.android.internal.R.styleable.ViewFlipper_flipInterval, DEFAULT_INTERVAL);
        mAutoStart = a.getBoolean(
                com.android.internal.R.styleable.ViewFlipper_autoStart, false);
        a.recycle();
    }

    //定义一个内部广播接收器，用于接受切换广播，动态广播，got it，这个广播主要处理锁屏等事件
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                mUserPresent = false;
                updateRunning();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                mUserPresent = true;
                updateRunning(false);
            }
        }
    };
    //广播接收器何时注册何时解除
    //在attchedtowindow时进行切换，注意该时间点的优点
    //在该时间点注册了广播接收器
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Listen for broadcasts related to user-presence
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        getContext().registerReceiver(mReceiver, filter);

        if (mAutoStart) {
            // Automatically start when requested
            startFlipping();
        }
    }
    //解除该广播接收器
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;

        getContext().unregisterReceiver(mReceiver);
        updateRunning();
    }
    //窗口可见性变化时
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning(false);
    }

    /***
     * How long to wait before flipping to the next view
     *
     * @param milliseconds
     *            time in milliseconds
     */
    @android.view.RemotableViewMethod
    public void setFlipInterval(int milliseconds) {
        mFlipInterval = milliseconds;
    }

    /***
     * Start a timer to cycle through child views
     */
    public void startFlipping() {
        mStarted = true;
        updateRunning();
    }

    /***
     * No more flips
     */
    public void stopFlipping() {
        mStarted = false;
        updateRunning();
    }

    /***
     * Internal method to start or stop dispatching flip {@link Message} based
     * on {@link #mRunning} and {@link #mVisible} state.
     */
    private void updateRunning() {
        updateRunning(true);
    }

    /***
     * Internal method to start or stop dispatching flip {@link Message} based
     * on {@link #mRunning} and {@link #mVisible} state.
     *
     * @param flipNow Determines whether or not to execute the animation now, in
     *            addition to queuing future flips. If omitted, defaults to
     *            true.
     */
    private void updateRunning(boolean flipNow) {
        boolean running = mVisible && mStarted && mUserPresent;
        if (running != mRunning) {
            if (running) {
                showOnly(mWhichChild, flipNow);
                //用一个handler实现
                Message msg = mHandler.obtainMessage(FLIP_MSG);
                //发送切换信号
                mHandler.sendMessageDelayed(msg, mFlipInterval);
            } else {
                mHandler.removeMessages(FLIP_MSG);
            }
            mRunning = running;
        }
        if (LOGD) {
            Log.d(TAG, "updateRunning() mVisible=" + mVisible + ", mStarted=" + mStarted
                    + ", mUserPresent=" + mUserPresent + ", mRunning=" + mRunning);
        }
    }

    /***
     * Returns true if the child views are flipping.
     */
    public boolean isFlipping() {
        return mStarted;
    }

    /***
     * Set if this view automatically calls {@link #startFlipping()} when it
     * becomes attached to a window.
     */
    public void setAutoStart(boolean autoStart) {
        mAutoStart = autoStart;
    }

    /***
     * Returns true if this view automatically calls {@link #startFlipping()}
     * when it becomes attached to a window.
     */
    public boolean isAutoStart() {
        return mAutoStart;
    }

    private final int FLIP_MSG = 1;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FLIP_MSG) {
                if (mRunning) {
                    showNext();
                    msg = obtainMessage(FLIP_MSG);
                    //进行一个循环
                    sendMessageDelayed(msg, mFlipInterval);
                }
            }
        }
    };
}


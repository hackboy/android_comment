// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Ticker.java

package android.widget;

import android.R;
import android.content.Context;
import android.content.Resources;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import java.util.Map;

// Referenced classes of package android.widget:
//            FrameLayout, Scroller

public class Ticker extends FrameLayout
{
    public static interface TickerListener
    {

        public abstract void onTickerFinished(Ticker ticker);
    }


    public Ticker(Context context)
    {
        super(context);
        mScroller = new Scroller(new LinearInterpolator());
        mHandler = new Handler();
        mLayoutDirty = false;
        mStarted = false;
        mStartOffScreen = true;
        mChildWidthUnlimited = true;
        initTicker();
    }

    public Ticker(Context context, AttributeSet attrs, Map inflateParams)
    {
        this(context, attrs, inflateParams, 0x10100d8);
    }

    public Ticker(Context context, AttributeSet attrs, Map inflateParams, int defStyle)
    {
        super(context, attrs, inflateParams, defStyle);
        mScroller = new Scroller(new LinearInterpolator());
        mHandler = new Handler();
        mLayoutDirty = false;
        mStarted = false;
        mStartOffScreen = true;
        mChildWidthUnlimited = true;
        android.content.Resources.StyledAttributes a = context.obtainStyledAttributes(attrs, android.R.styleable.Ticker, defStyle, 0);
        mStartOffScreen = a.getBoolean(0, true);
        mChildWidthUnlimited = a.getBoolean(1, true);
        a.recycle();
        initTicker();
    }

    private void initTicker()
    {
        mLayoutDirty = true;
        setWillNotDraw(false);
    }

    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec)
    {
        if(mChildWidthUnlimited)
        {
            android.view.ViewGroup.LayoutParams lp = child.getLayoutParams();
            int childWidthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, 0);
            int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, mPaddingLeft + mPaddingRight, lp.width);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        } else
        {
            super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
        }
    }

    public void startTicker()
    {
        if(mLayoutDirty)
            postStartTicker();
        else
            startTickerNow();
    }

    public void resetTicker()
    {
        mStarted = false;
        mScrollX = mScrollY = 0;
    }

    public void setTickerListener(TickerListener listener)
    {
        mTickerListener = listener;
    }

    private void postStartTicker()
    {
        View child = getChildAt(0);
        if(child != null)
            child.setVisibility(4);
        mHandler.post(new Runnable() {

            public void run()
            {
                View child = getChildAt(0);
                if(child != null)
                    child.setVisibility(0);
                startTickerNow();
            }

            final Ticker this$0;

            
            {
                this$0 = Ticker.this;
                super();
            }
        });
    }

    private void startTickerNow()
    {
        mStarted = true;
        View child = getChildAt(0);
        mScrollX = mScrollY = 0;
        if(child != null)
        {
            int childRight = child.getRight();
            int myWidth = getWidth() - mPaddingLeft - mPaddingRight;
            int fromXDelta;
            int startX;
            if(mStartOffScreen)
            {
                fromXDelta = myWidth + mPaddingLeft;
                startX = -myWidth;
            } else
            {
                fromXDelta = 0;
                startX = 0;
            }
            int toXDelta = Math.min(0, myWidth - childRight);
            int scrollAmount = Math.abs(fromXDelta - toXDelta);
            if(scrollAmount > 0)
            {
                mScroller.startScroll(startX, 0, scrollAmount, 0, (scrollAmount * 1000) / 100);
                invalidate();
            } else
            {
                doneTicking();
            }
        }
    }

    public void computeScroll()
    {
        if(mScroller.computeScrollOffset())
        {
            mScrollX = mScroller.getCurrX();
            mScrollY = mScroller.getCurrY();
            postInvalidate();
        } else
        {
            doneTicking();
        }
    }

    private void doneTicking()
    {
        if(mStarted && mTickerListener != null)
        {
            mStarted = false;
            mTickerListener.onTickerFinished(this);
        }
    }

    public void requestLayout()
    {
        super.requestLayout();
        mLayoutDirty = true;
    }

    protected void onLayout(boolean changed, int wl, int wt, int l, int t, int r, int b)
    {
        mLayoutDirty = false;
        super.onLayout(changed, wl, wt, l, t, r, b);
    }

    protected int computeHorizontalScrollRange()
    {
        int count = getChildCount();
        return count != 0 ? getChildAt(0).getRight() : getWidth();
    }

    public void setStartOffScreen(boolean startOffScreen)
    {
        mStartOffScreen = startOffScreen;
    }

    public boolean isStartOffScreen()
    {
        return mStartOffScreen;
    }

    public void setChildWidthUnlimited(boolean childWidthUnlimited)
    {
        mChildWidthUnlimited = childWidthUnlimited;
    }

    public boolean isChildWidthUnlimited()
    {
        return mChildWidthUnlimited;
    }

    private static final int DEFAULT_SPEED = 100;
    private Scroller mScroller;
    private Handler mHandler;
    private boolean mLayoutDirty;
    private TickerListener mTickerListener;
    private boolean mStarted;
    private boolean mStartOffScreen;
    private boolean mChildWidthUnlimited;

}

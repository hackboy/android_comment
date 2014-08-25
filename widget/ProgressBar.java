// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProgressBar.java

package android.widget;

import android.R;
import android.content.Context;
import android.content.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Transformation;
import java.util.Map;
//自定义进度条
public class ProgressBar extends View
{

    public ProgressBar(Context context)
    {
        this(context, null, null);
    }

    public ProgressBar(Context context, AttributeSet attrs, Map inflateParams)
    {
        this(context, attrs, inflateParams, 0x1010024);
    }

    public ProgressBar(Context context, AttributeSet attrs, Map inflateParams, int defStyle)
    {
        super(context, attrs, inflateParams, defStyle);
        initProgressBar();
        android.content.Resources.StyledAttributes a = context.obtainStyledAttributes(attrs, android.R.styleable.ProgressBar, defStyle, 0);
        mDuration = a.getInt(7, mDuration);
        mMinWidth = a.getDimensionPixelSize(9, mMinWidth);
        mMaxWidth = a.getDimensionPixelSize(0, mMaxWidth);
        mMinHeight = a.getDimensionPixelSize(10, mMinHeight);
        mMaxHeight = a.getDimensionPixelSize(1, mMaxHeight);
        mBehavior = a.getInt(8, mBehavior);
        mNoInvalidate = true;
        setMax(a.getInt(3, mMax));
        setProgress(a.getInt(4, mProgress));
        setOrientation(a.getInt(2, mOrientation));
        Drawable drawable = a.getDrawable(6);
        if(drawable != null)
            setIndeterminateBackground(drawable);
        setIndeterminate(a.getBoolean(5, mIndeterminate));
        mNoInvalidate = false;
        a.recycle();
    }

    private void initProgressBar()
    {
        mMax = 100;
        mProgress = 0;
        mOrientation = 0;
        mIndeterminate = false;
        mDuration = 4000;
        mBehavior = 1;
        mMinWidth = 24;
        mMaxWidth = 48;
        mMinHeight = 24;
        mMaxHeight = 48;
    }

    public boolean isIndeterminate()
    {
        return mIndeterminate;
    }

    public void setIndeterminate(boolean indeterminate)
    {
        if(indeterminate != mIndeterminate)
        {
            mIndeterminate = indeterminate;
            if(indeterminate)
            {
                Drawable d = getBackground();
                super.setBackground(mIndeterminateBackground);
                mNormalBackground = d;
                startAnimation();
            } else
            {
                super.setBackground(mNormalBackground);
                stopAnimation();
            }
        }
    }

    public Drawable getIndeterminateBackground()
    {
        return mIndeterminateBackground;
    }

    public void setIndeterminateBackground(Drawable background)
    {
        mIndeterminateBackground = background;
        if(mIndeterminate)
        {
            super.setBackground(background);
            postInvalidate();
        }
    }

    public void setBackground(Drawable d)
    {
        mNormalBackground = d;
        super.setBackground(d);
    }

    public int getOrientation()
    {
        return mOrientation;
    }

    public void setOrientation(int orientation)
    {
        switch(orientation)
        {
        case 0: // '\0'
        case 1: // '\001'
            mOrientation = orientation;
            postInvalidate();
            break;
        }
    }

    public void postInvalidate()
    {
        if(!mNoInvalidate)
            super.postInvalidate();
    }

    public void setProgress(int progress)
    {
        if(mIndeterminate)
            return;
        if(progress < 0)
            progress = 0;
        if(progress > mMax)
            progress = mMax;
        if(progress != mProgress)
        {
            mProgress = progress;
            postInvalidate();
        }
    }

    public int getProgress()
    {
        return mIndeterminate ? 0 : mProgress;
    }

    public int getMax()
    {
        return mMax;
    }

    public void setMax(int max)
    {
        if(max < 0)
            max = 0;
        if(max != mMax)
        {
            mMax = max;
            postInvalidate();
            if(mProgress > max)
                mProgress = max;
        }
    }

    public final void incrementProgressBy(int diff)
    {
        setProgress(mProgress + diff);
    }

    protected void startAnimation()
    {
        int visibility = getVisibility();
        if(visibility == 4 || visibility == 8)
        {
            return;
        } else
        {
            mTransformation = new Transformation();
            mAnimation = new AlphaAnimation(0.0F, 1.0F);
            mAnimation.setRepeatMode(mBehavior);
            mAnimation.setDuration(mDuration);
            mAnimation.setStartTime(-1L);
            postInvalidate();
            return;
        }
    }

    protected void stopAnimation()
    {
        mAnimation = null;
        mTransformation = null;
    }

    public void setVisibility(int v)
    {
        if(getVisibility() != v)
        {
            super.setVisibility(v);
            if(v == 8 || v == 4)
                stopAnimation();
            else
            if(mIndeterminate && v == 0)
                startAnimation();
        }
    }

    protected void onDraw(Canvas canvas)
    {
        long time = getDrawingTime();
        float scale;
        if(mAnimation == null)
        {
            scale = mMax <= 0 ? 0.0F : (float)mProgress / (float)mMax;
        } else
        {
            if(time < mLastDrawingTime + 150L)
                return;
            mAnimation.getTransformation(time, mTransformation);
            scale = mTransformation.getAlpha();
        }
        getBackground().setLevel((int)(scale * 10000F));
        super.onDraw(canvas);
        if(mAnimation != null)
        {
            postInvalidateDelayed(150L);
            mLastDrawingTime = time;
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        Drawable background = getBackground();
        int dw = Math.max(mMinWidth, Math.min(mMaxWidth, background.getIntrinsicWidth()));
        int dh = Math.max(mMinHeight, Math.min(mMaxHeight, background.getIntrinsicHeight()));
        setMeasuredDimension(resolveSize(dw, widthMeasureSpec), resolveSize(dh, heightMeasureSpec));
    }

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private static final int MAX_LEVEL = 10000;
    private static final int ANIMATION_RESOLUTION = 150;
    private int mMinWidth;
    private int mMaxWidth;
    private int mMinHeight;
    private int mMaxHeight;
    private int mProgress;
    private int mMax;
    private int mOrientation;
    private int mBehavior;
    private int mDuration;
    private boolean mIndeterminate;
    private Transformation mTransformation;
    private AlphaAnimation mAnimation;
    private Drawable mIndeterminateBackground;
    private Drawable mNormalBackground;
    private long mLastDrawingTime;
    private boolean mNoInvalidate;
}

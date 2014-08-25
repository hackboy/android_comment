// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScrollIndicator.java

package android.widget;

import android.R;
import android.content.Context;
import android.content.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import java.util.Map;

// Referenced classes of package android.widget:
//            ScrollBarDrawable

public class ScrollIndicator extends View
{

    public ScrollIndicator(Context context)
    {
        super(context);
        initScrollIndicator();
    }

    public ScrollIndicator(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
        initScrollIndicator();
        android.content.Resources.StyledAttributes a = context.obtainStyledAttributes(attrs, android.R.styleable.ScrollIndicator);
        setRange(a.getInt(0, mRange));
        setOffset(a.getInt(1, mOffset));
        setExtent(a.getInt(2, mExtent));
        a.recycle();
    }

    private void initScrollIndicator()
    {
        mRange = 100;
        mVertical = true;
        mStateDirty = true;
        mDrawable = new ScrollBarDrawable();
    }

    public boolean isVertical()
    {
        return mVertical;
    }

    public void setVertical(boolean isVert)
    {
        if(mVertical != isVert)
        {
            mVertical = isVert;
            mStateDirty = true;
            invalidate();
        }
    }

    public boolean getDrawBackground()
    {
        return mDrawable.getDrawBackground();
    }

    public void setDrawBackground(boolean doDraw)
    {
        mDrawable.setDrawBackground(doDraw);
    }

    public final int getRange()
    {
        return mRange;
    }

    public void setRange(int range)
    {
        if(range >= 0 && mRange != range)
        {
            mRange = range;
            mStateDirty = true;
            invalidate();
        }
    }

    public final int getOffset()
    {
        return mOffset;
    }

    public void setOffset(int offset)
    {
        if(offset >= 0 && mOffset != offset)
        {
            mOffset = offset;
            mStateDirty = true;
            invalidate();
        }
    }

    public final int getExtent()
    {
        return mExtent;
    }

    public void setExtent(int extent)
    {
        if(extent >= 0 && mExtent != extent)
        {
            mExtent = extent;
            mStateDirty = true;
            invalidate();
        }
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(mStateDirty)
        {
            mStateDirty = false;
            int offset = mOffset;
            int extent = mExtent;
            if(extent > mRange)
                extent = mRange;
            if(offset > mRange - extent)
                offset = mRange - extent;
            mDrawable.setParams(mRange, offset, extent, mVertical);
        }
        mDrawable.draw(canvas);
    }

    protected void onSizeChanged(int w, int h, int ow, int oh)
    {
        super.onSizeChanged(w, h, ow, oh);
        mDrawable.setBounds(0, 0, w, h);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        setMeasuredDimension(getDefaultSize(200, widthMeasureSpec), getDefaultSize(20, heightMeasureSpec));
    }

    private int mRange;
    private int mOffset;
    private int mExtent;
    private boolean mVertical;
    private boolean mStateDirty;
    private ScrollBarDrawable mDrawable;
}

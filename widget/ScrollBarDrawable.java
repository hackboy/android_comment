// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScrollBarDrawable.java

package android.widget;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;

public class ScrollBarDrawable extends Drawable
{
    private class CircularCapsDrawable extends PaintDrawable
    {

        public void setBounds(int left, int top, int right, int bottom)
        {
            super.setBounds(left, top, right, bottom);
            float length = mVertical ? right - left : bottom - top;
            super.setCornerRadii(length * 0.5F, length * 0.5F);
        }

        final ScrollBarDrawable this$0;

        public CircularCapsDrawable(int color)
        {
            this$0 = ScrollBarDrawable.this;
            super(color);
        }
    }


    public ScrollBarDrawable()
    {
        mVerticalTrack = mHorizontalTrack = new PaintDrawable(Color.rgb(204, 204, 204));
        mVerticalThumb = mHorizontalThumb = new CircularCapsDrawable(Color.rgb(68, 68, 255));
        mDrawBackground = true;
    }

    public boolean getDrawBackground()
    {
        return mDrawBackground;
    }

    public void setDrawBackground(boolean doDraw)
    {
        mDrawBackground = doDraw;
    }

    public void setParams(int range, int offset, int extent, boolean vertical)
    {
        mRange = range;
        mOffset = offset;
        mExtent = extent;
        mVertical = vertical;
    }

    public boolean hasNothingToDraw()
    {
        return mExtent <= 0 || mRange <= mExtent;
    }

    public void draw(Canvas canvas)
    {
        if(mExtent <= 0 || mRange <= mExtent)
            return;
        Rect r = getBounds();
        int size = mVertical ? r.height() : r.width();
        int thickness = mVertical ? r.width() : r.height();
        int length = Math.round(((float)size * (float)mExtent) / (float)mRange);
        int offset = Math.round(((float)(size - length) * (float)mOffset) / (float)(mRange - mExtent));
        int minLength = thickness * 2;
        if(length < minLength)
            length = minLength;
        if(offset + length > size)
            offset = size - length;
        drawTrack(canvas, r, mVertical);
        drawThumb(canvas, r, offset, length, mVertical);
    }

    protected void drawTrack(Canvas canvas, Rect bounds, boolean vertical)
    {
        if(mDrawBackground)
            if(vertical)
            {
                if(mVerticalTrack != null)
                {
                    mVerticalTrack.setBounds(bounds);
                    mVerticalTrack.draw(canvas);
                }
            } else
            if(mHorizontalTrack != null)
            {
                mHorizontalTrack.setBounds(bounds);
                mHorizontalTrack.draw(canvas);
            }
    }

    protected void drawThumb(Canvas canvas, Rect bounds, int offset, int length, boolean vertical)
    {
        Rect thumbRect;
        if(vertical)
            thumbRect = new Rect(bounds.left, bounds.top + offset, bounds.right, bounds.top + offset + length);
        else
            thumbRect = new Rect(bounds.left + offset, bounds.top, bounds.left + offset + length, bounds.bottom);
        if(vertical)
        {
            mVerticalThumb.setBounds(thumbRect);
            mVerticalThumb.draw(canvas);
        } else
        {
            mHorizontalThumb.setBounds(thumbRect);
            mHorizontalThumb.draw(canvas);
        }
    }

    public void setVerticalThumbDrawable(Drawable thumb)
    {
        if(thumb != null)
            mVerticalThumb = thumb;
    }

    public void setVerticalTrackDrawable(Drawable track)
    {
        mVerticalTrack = track;
    }

    public void setHorizontalThumbDrawable(Drawable thumb)
    {
        if(thumb != null)
            mHorizontalThumb = thumb;
    }

    public void setHorizontalTrackDrawable(Drawable track)
    {
        mHorizontalTrack = track;
    }

    public int getSize(boolean vertical)
    {
        if(vertical)
            return (mVerticalTrack == null ? mVerticalThumb : mVerticalTrack).getIntrinsicWidth();
        else
            return (mHorizontalTrack == null ? mHorizontalThumb : mHorizontalTrack).getIntrinsicHeight();
    }

    public void setAlpha(int alpha)
    {
        if(mVerticalTrack != null)
            mVerticalTrack.setAlpha(alpha);
        mVerticalThumb.setAlpha(alpha);
        if(mHorizontalTrack != null)
            mHorizontalTrack.setAlpha(alpha);
        mHorizontalThumb.setAlpha(alpha);
    }

    public void setColorFilter(int color, android.graphics.PorterDuff.Mode mode)
    {
        if(mVerticalTrack != null)
            mVerticalTrack.setColorFilter(color, mode);
        mVerticalThumb.setColorFilter(color, mode);
        if(mHorizontalTrack != null)
            mHorizontalTrack.setColorFilter(color, mode);
        mHorizontalThumb.setColorFilter(color, mode);
    }

    public int getOpacity()
    {
        return -3;
    }

    public String toString()
    {
        return (new StringBuilder()).append("ScrollBarDrawable: range=").append(mRange).append(" offset=").append(mOffset).append(" extent=").append(mExtent).append(mVertical ? " V" : " H").toString();
    }

    private Drawable mVerticalTrack;
    private Drawable mHorizontalTrack;
    private Drawable mVerticalThumb;
    private Drawable mHorizontalThumb;
    private int mRange;
    private int mOffset;
    private int mExtent;
    private boolean mVertical;
    private boolean mDrawBackground;

}

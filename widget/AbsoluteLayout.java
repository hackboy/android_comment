// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbsoluteLayout.java

package android.widget;

import android.R;
import android.content.Context;
import android.content.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;

public class AbsoluteLayout extends ViewGroup
{
    public static class LayoutParams extends android.view.ViewGroup.LayoutParams
    {

        public String debug(String output)
        {
            return (new StringBuilder()).append(output).append("Absolute.LayoutParams={width=").append(sizeToString(width)).append(", height=").append(sizeToString(height)).append(" x=").append(x).append(" y=").append(y).append("}").toString();
        }

        public int x;
        public int y;

        public LayoutParams(int width, int height, int x, int y)
        {
            super(width, height);
            this.x = x;
            this.y = y;
        }

        public LayoutParams(Context c, AttributeSet attrs)
        {
            super(c, attrs);
            android.content.Resources.StyledAttributes a = c.obtainStyledAttributes(attrs, android.R.styleable.AbsoluteLayout_Layout);
            x = a.getDimensionPixelOffset(0, 0);
            y = a.getDimensionPixelOffset(1, 0);
            a.recycle();
        }
    }


    public AbsoluteLayout(Context context)
    {
        super(context);
    }

    public AbsoluteLayout(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
    }

    public AbsoluteLayout(Context context, AttributeSet attrs, Map inflateParams, int defStyle)
    {
        super(context, attrs, inflateParams, defStyle);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        for(int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            if(child.getVisibility() != 8)
            {
                int childRight = 0;
                int childBottom = 0;
                LayoutParams lp = (LayoutParams)child.getLayoutParams();
                childRight = lp.x + child.getMeasuredWidth();
                childBottom = lp.y + child.getMeasuredHeight();
                maxWidth = Math.max(maxWidth, childRight);
                maxHeight = Math.max(maxHeight, childBottom);
            }
        }

        maxWidth += mPaddingLeft + mPaddingRight;
        maxHeight += mPaddingTop + mPaddingBottom;
        setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec), resolveSize(maxHeight, heightMeasureSpec));
    }

    protected void onLayout(boolean changed, int wl, int wt, int l, int t, int r, int b)
    {
        int count = getChildCount();
        for(int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            if(child.getVisibility() != 8)
            {
                LayoutParams lp = (LayoutParams)child.getLayoutParams();
                int childLeft = mPaddingLeft + lp.x;
                int childTop = mPaddingTop + lp.y;
                child.layout(mWindowLeft + childLeft, mWindowTop + childTop, childLeft, childTop, childLeft + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());
            }
        }

    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new LayoutParams(getContext(), attrs);
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p)
    {
        return p instanceof LayoutParams;
    }
}

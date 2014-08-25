// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RelativeLayout.java

package android.widget;

import android.R;
import android.content.Context;
import android.content.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.io.PrintStream;
import java.util.Map;

public class RelativeLayout extends ViewGroup
{
    public static class LayoutParams extends android.view.ViewGroup.MarginLayoutParams
    {

        public String debug(String output)
        {
            return (new StringBuilder()).append(output).append("ViewGroup.LayoutParams={ width=").append(sizeToString(width)).append(", height=").append(sizeToString(height)).append(" }").toString();
        }

        public void addRule(int verb)
        {
            addRule(verb, -1);
        }

        public void addRule(int verb, int anchor)
        {
            mRules[verb] = anchor;
        }

        public int[] getRules()
        {
            return mRules;
        }

        int mRules[];
        int left;
        int top;
        int right;
        int bottom;

        public LayoutParams(Context c, AttributeSet attrs)
        {
            super(c, attrs);
            mRules = new int[16];
            android.content.Resources.StyledAttributes a = c.obtainStyledAttributes(attrs, android.R.styleable.RelativeLayout_Layout);
            int id = a.getResourceID(0, -1);
            if(id != -1)
                addRule(0, id);
            id = a.getResourceID(1, -1);
            if(id != -1)
                addRule(1, id);
            id = a.getResourceID(2, -1);
            if(id != -1)
                addRule(2, id);
            id = a.getResourceID(3, -1);
            if(id != -1)
                addRule(3, id);
            id = a.getResourceID(4, -1);
            if(id != -1)
                addRule(4, id);
            id = a.getResourceID(5, -1);
            if(id != -1)
                addRule(5, id);
            id = a.getResourceID(6, -1);
            if(id != -1)
                addRule(6, id);
            id = a.getResourceID(7, -1);
            if(id != -1)
                addRule(7, id);
            id = a.getResourceID(8, -1);
            if(id != -1)
                addRule(8, id);
            if(a.getBoolean(9, false))
                addRule(9, -1);
            if(a.getBoolean(10, false))
                addRule(10, -1);
            if(a.getBoolean(11, false))
                addRule(11, -1);
            if(a.getBoolean(12, false))
                addRule(12, -1);
            if(a.getBoolean(13, false))
                addRule(13, -1);
            if(a.getBoolean(14, false))
                addRule(14, -1);
            if(a.getBoolean(15, false))
                addRule(15, -1);
            a.recycle();
        }

        public LayoutParams(int w, int h)
        {
            super(w, h);
            mRules = new int[16];
        }
    }


    public RelativeLayout(Context context)
    {
        super(context);
        mBaselineView = null;
    }

    public RelativeLayout(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
        mBaselineView = null;
    }

    public int getBaseline()
    {
        return mBaselineView == null ? super.getBaseline() : mBaselineView.getBaseline();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int myWidth = -1;
        int myHeight = -1;
        int width = 0;
        int height = 0;
        int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
        if(widthMode != 0)
            myWidth = widthSize;
        if(heightMode != 0)
            myHeight = heightSize;
        if(widthMode == 0x40000000)
            width = myWidth;
        if(heightMode == 0x40000000)
            height = myHeight;
        int len = getChildCount();
        for(int i = 0; i < len; i++)
        {
            View child = getChildAt(i);
            if(child.getVisibility() == 8)
                continue;
            LayoutParams params = (LayoutParams)child.getLayoutParams();
            applySizeRules(params, myWidth, myHeight);
            measureChild(child, params, myWidth, myHeight);
            positionChild(child, params, myWidth, myHeight);
            if(android.view.View.MeasureSpec.getMode(widthMeasureSpec) != 0x40000000)
                width = Math.max(width, params.right);
            if(android.view.View.MeasureSpec.getMode(heightMeasureSpec) != 0x40000000)
                height = Math.max(height, params.bottom);
        }

        if(widthMode != 0x40000000)
        {
            width += mPaddingLeft + mPaddingRight;
            width = resolveSize(width, widthMeasureSpec);
        }
        if(heightMode != 0x40000000)
        {
            height += mPaddingTop + mPaddingBottom;
            height = resolveSize(height, heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    private void measureChild(View child, LayoutParams params, int myWidth, int myHeight)
    {
        int childWidthMeasureSpec = getChildMeasureSpec(params.left, params.right, params.width, params.leftMargin, params.rightMargin, mPaddingLeft, mPaddingRight, myWidth);
        int childHeightMeasureSpec = getChildMeasureSpec(params.top, params.bottom, params.height, params.topMargin, params.bottomMargin, mPaddingTop, mPaddingBottom, myHeight);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    private int getChildMeasureSpec(int childStart, int childEnd, int childSize, int startMargin, int endMargin, int startPadding, int endPadding, 
            int mySize)
    {
        int childSpecMode = 0;
        int childSpecSize = 0;
        int tempStart = childStart;
        int tempEnd = childEnd;
        if(tempStart < 0)
            tempStart = startPadding + startMargin;
        if(tempEnd < 0)
            tempEnd = mySize - endPadding - endMargin;
        int maxAvailable = tempEnd - tempStart;
        if(childStart >= 0 && childEnd >= 0)
        {
            childSpecMode = 0x40000000;
            childSpecSize = maxAvailable;
        } else
        if(childSize >= 0)
        {
            childSpecMode = 0x40000000;
            if(maxAvailable >= 0)
                childSpecSize = Math.min(maxAvailable, childSize);
            else
                childSpecSize = childSize;
        } else
        if(childSize == -1)
        {
            childSpecMode = 0x40000000;
            childSpecSize = maxAvailable;
        } else
        if(childSize == -2)
            if(maxAvailable >= 0)
            {
                childSpecMode = 0x80000000;
                childSpecSize = maxAvailable;
            } else
            {
                childSpecMode = 0;
                childSpecSize = 0;
            }
        return android.view.View.MeasureSpec.makeMeasureSpec(childSpecSize, childSpecMode);
    }

    private void positionChild(View child, LayoutParams params, int myWidth, int myHeight)
    {
        int rules[] = params.getRules();
        if(params.left < 0 && params.right >= 0)
            params.left = params.right - child.getMeasuredWidth();
        else
        if(params.left >= 0 && params.right < 0)
            params.right = params.left + child.getMeasuredWidth();
        else
        if(params.left < 0 && params.right < 0)
            if(0 != rules[13] || 0 != rules[14])
            {
                centerHorizontal(child, params, myWidth);
            } else
            {
                params.left = mPaddingLeft + params.leftMargin;
                params.right = params.left + child.getMeasuredWidth();
            }
        if(params.top < 0 && params.bottom >= 0)
            params.top = params.bottom - child.getMeasuredHeight();
        else
        if(params.top >= 0 && params.bottom < 0)
            params.bottom = params.top + child.getMeasuredHeight();
        else
        if(params.top < 0 && params.bottom < 0)
            if(0 != rules[13] || 0 != rules[15])
            {
                centerVertical(child, params, myHeight);
            } else
            {
                params.top = mPaddingTop + params.topMargin;
                params.bottom = params.top + child.getMeasuredHeight();
            }
        int anchorBaseline = getRelatedViewBaseline(rules, 4);
        if(anchorBaseline != -1)
        {
            LayoutParams anchorParams = getRelatedViewParams(rules, 4);
            if(anchorParams != null)
            {
                int offset = anchorParams.top + anchorBaseline;
                int baseline = child.getBaseline();
                if(baseline != -1)
                    offset -= baseline;
                int height = params.bottom - params.top;
                params.top = offset;
                params.bottom = params.top + height;
            }
        }
        if(mBaselineView == null)
        {
            mBaselineView = child;
        } else
        {
            LayoutParams lp = (LayoutParams)mBaselineView.getLayoutParams();
            if(params.top < lp.top || params.top == lp.top && params.left < lp.left)
                mBaselineView = child;
        }
    }

    private void applySizeRules(LayoutParams childParams, int myWidth, int myHeight)
    {
        int rules[] = childParams.getRules();
        childParams.left = -1;
        childParams.right = -1;
        LayoutParams anchorParams = getRelatedViewParams(rules, 0);
        if(anchorParams != null)
            childParams.right = anchorParams.left - (anchorParams.leftMargin + childParams.rightMargin);
        anchorParams = getRelatedViewParams(rules, 1);
        if(anchorParams != null)
            childParams.left = anchorParams.right + (anchorParams.rightMargin + childParams.leftMargin);
        anchorParams = getRelatedViewParams(rules, 5);
        if(anchorParams != null)
            childParams.left = anchorParams.left + childParams.leftMargin;
        anchorParams = getRelatedViewParams(rules, 7);
        if(anchorParams != null)
            childParams.right = anchorParams.right - childParams.rightMargin;
        if(0 != rules[9])
            childParams.left = mPaddingLeft + childParams.leftMargin;
        if(0 != rules[11] && myWidth >= 0)
            childParams.right = myWidth - mPaddingRight - childParams.rightMargin;
        childParams.top = -1;
        childParams.bottom = -1;
        anchorParams = getRelatedViewParams(rules, 2);
        if(anchorParams != null)
            childParams.bottom = anchorParams.top - (anchorParams.topMargin + childParams.bottomMargin);
        anchorParams = getRelatedViewParams(rules, 3);
        if(anchorParams != null)
            childParams.top = anchorParams.bottom + (anchorParams.bottomMargin + childParams.topMargin);
        anchorParams = getRelatedViewParams(rules, 6);
        if(anchorParams != null)
            childParams.top = anchorParams.top + childParams.topMargin;
        anchorParams = getRelatedViewParams(rules, 8);
        if(anchorParams != null)
            childParams.bottom = anchorParams.bottom - childParams.bottomMargin;
        if(0 != rules[10])
            childParams.top = mPaddingTop + childParams.topMargin;
        if(0 != rules[12] && myHeight >= 0)
            childParams.bottom = myHeight - mPaddingBottom - childParams.bottomMargin;
    }

    private LayoutParams getRelatedViewParams(int rules[], int relation)
    {
        LayoutParams result = null;
        int id = rules[relation];
        if(id != 0)
        {
            View v = findViewById(id);
            try
            {
                result = (LayoutParams)v.getLayoutParams();
            }
            catch(Exception ex)
            {
                System.err.println((new StringBuilder()).append("RelativeLayout.getRelativeViewParams: caught ").append(ex).toString());
            }
        }
        return result;
    }

    private int getRelatedViewBaseline(int rules[], int relation)
    {
        int result = -1;
        int id = rules[relation];
        if(id != 0)
            result = findViewById(id).getBaseline();
        return result;
    }

    private void centerHorizontal(View child, LayoutParams params, int myWidth)
    {
        int childWidth = child.getMeasuredWidth();
        int left = (myWidth - childWidth) / 2;
        params.left = left;
        params.right = left + childWidth;
    }

    private void centerVertical(View child, LayoutParams params, int myHeight)
    {
        int childHeight = child.getMeasuredHeight();
        int top = (myHeight - childHeight) / 2;
        params.top = top;
        params.bottom = top + childHeight;
    }

    protected void onLayout(boolean changed, int wl, int wt, int l, int t, int r, int b)
    {
        int count = getChildCount();
        for(int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            if(child.getVisibility() != 8)
            {
                LayoutParams st = (LayoutParams)child.getLayoutParams();
                child.layout(mWindowLeft + st.left, mWindowTop + st.top, st.left, st.top, st.right, st.bottom);
            }
        }

    }

    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new LayoutParams(getContext(), attrs);
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p)
    {
        return p instanceof LayoutParams;
    }

    public volatile android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet x0)
    {
        return generateLayoutParams(x0);
    }

    public static final int TRUE = -1;
    public static final int POSITION_TO_LEFT = 0;
    public static final int POSITION_TO_RIGHT = 1;
    public static final int POSITION_ABOVE = 2;
    public static final int POSITION_BELOW = 3;
    public static final int ALIGN_BASELINE = 4;
    public static final int ALIGN_LEFT = 5;
    public static final int ALIGN_TOP = 6;
    public static final int ALIGN_RIGHT = 7;
    public static final int ALIGN_BOTTOM = 8;
    public static final int ALIGN_WITH_PARENT_LEFT = 9;
    public static final int ALIGN_WITH_PARENT_TOP = 10;
    public static final int ALIGN_WITH_PARENT_RIGHT = 11;
    public static final int ALIGN_WITH_PARENT_BOTTOM = 12;
    public static final int CENTER_IN_PARENT = 13;
    public static final int CENTER_HORIZONTAL = 14;
    public static final int CENTER_VERTICAL = 15;
    private static final int VERB_COUNT = 16;
    private View mBaselineView;
}

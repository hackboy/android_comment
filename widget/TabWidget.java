// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TabWidget.java

package android.widget;

import android.R;
import android.content.Context;
import android.content.Resources;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;
//tabWidget和tabhost的关系
public class TabWidget extends ViewGroup
{

    public TabWidget(Context context)
    {
        this(context, null, null);
    }

    public TabWidget(Context context, AttributeSet attrs, Map inflateParams)
    {   
        //传递了默认的样式
        this(context, attrs, inflateParams, 0x101002b);
    }

    public TabWidget(Context context, AttributeSet attrs, Map inflateParams, int defStyle)
    {
        super(context, attrs, inflateParams, defStyle);
        mSelectedIndex = -1;
        mBracketRect = new Rect();
        mTabTempRect = new Rect();
        mSelectedColor = 0xff000000;
        mNormalColor = 0xbb000000;
        mDisabledColor = 0x55000000;
        mColorDirty = true;
        initTabWidget();
        setWillNotDraw(false);
        android.content.Resources.StyledAttributes a = context.obtainStyledAttributes(attrs, android.R.styleable.TabWidget, defStyle, 0);
        int resID = a.getResourceID(0, -1);
        if(resID > 0)
            setBracket(resID);
        int selectedColor = a.getColor(3, 0);
        if(selectedColor != 0)
        {
            int normalColor = a.getColor(2, selectedColor);
            int disabledColor = a.getColor(1, selectedColor);
            setColors(selectedColor, normalColor, disabledColor);
        }
        a.recycle();
    }

    private void initTabWidget()
    {
        mSpaceBetweenUnselectedItems = 10;
        setBracket(0x10600fa);
    }

    public void setBracket(int bracketResource)
    {
        if(bracketResource == 0)
        {
            mBracket = null;
            mLeftBracketWidth = 0;
            mRightBracketWidth = 0;
            mBracketHeight = 0;
        } else
        {
            setBracket(getResources().getDrawable(bracketResource));
        }
    }

    public void setBracket(Drawable bracketDrawable)
    {
        if(mBracket != null)
        {
            mBracket.setCallback(null);
            unscheduleDrawable(mBracket);
        }
        if(bracketDrawable == null)
        {
            mBracket = null;
            mLeftBracketWidth = 0;
            mRightBracketWidth = 0;
            mBracketHeight = 0;
        } else
        {
            mBracket = bracketDrawable;
            mBracket.setCallback(this);
            mBracket.setState(getDrawableState());
        }
        invalidate();
    }

    protected void drawableStateChanged()
    {
        super.drawableStateChanged();
        if(mBracket != null)
            mBracket.setState(getDrawableState());
    }

    public boolean verifyDrawable(Drawable drawable)
    {
        return mBracket == drawable || super.verifyDrawable(drawable);
    }

    public void shiftLeft()
    {
        if(mSelectedIndex > 0)
            setSelectedIndex(mSelectedIndex - 1);
    }

    public void shiftRight()
    {
        if(mSelectedIndex < getChildCount() - 1)
            setSelectedIndex(mSelectedIndex + 1);
    }

    public void setCurrentTab(int index)
    {
        if(index < 0)
            index = 0;
        int n = getChildCount();
        if(index >= n)
            index = n - 1;
        setSelectedIndex(index);
    }

    public int[] createDrawableState()
    {
        return mergeStates(super.createDrawableState(), getBracketState());
    }

    private int[] getBracketState()
    {
        int index = mSelectedIndex;
        int newState[];
        if(index == getChildCount() - 1)
        {
            if(index == 0)
                newState = SINGLE_STATE_SET;
            else
                newState = LAST_STATE_SET;
        } else
        if(index == 0)
            newState = FIRST_STATE_SET;
        else
            newState = MIDDLE_STATE_SET;
        return newState;
    }

    private void setSelectedIndex(int index)
    {
        if(index != mSelectedIndex)
        {
            mSelectedIndex = index;
            refreshDrawableState();
            if(mBracket != null)
            {
                mBracket.getPadding(mTabTempRect);
                mLeftBracketWidth = mTabTempRect.left;
                mRightBracketWidth = mTabTempRect.right;
                mBracketHeight = mBracket.getIntrinsicHeight();
            }
            requestLayout();
            invalidate();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if(mSelectedIndex < 0)
            setSelectedIndex(0);
        int totalWidth = 0;
        int maxHeight = mBracketHeight;
        int count = getChildCount();
        for(int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            if(child.getVisibility() == 8)
                continue;
            if(i == mSelectedIndex)
                child.setSelected(true);
            else
                child.setSelected(false);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            totalWidth += child.getMeasuredWidth();
            maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
        }

        totalWidth += mLeftBracketWidth + mRightBracketWidth;
        if(count > 2)
            totalWidth += mSpaceBetweenUnselectedItems * (count - 2);
        setMeasuredDimension(resolveSize(totalWidth, widthMeasureSpec), resolveSize(maxHeight, heightMeasureSpec));
    }

    public void onLayout(boolean changed, int wl, int wt, int l, int t, int r, int b)
    {
        int height = getHeight();
        int childLeft = 0;
        int count = getChildCount();
        for(int i = 0; i < mSelectedIndex; i++)
        {
            View child = getChildAt(i);
            if(child.getVisibility() == 8)
                continue;
            childLeft -= child.getMeasuredWidth();
            if(i < mSelectedIndex - 1)
                childLeft -= mSpaceBetweenUnselectedItems;
        }

        for(int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();
            int childTop = (height - childHeight) / 2;
            int childBottom = childTop + childHeight;
            if(i == mSelectedIndex)
            {
                childLeft += mLeftBracketWidth;
                int totalBracketWidth = mLeftBracketWidth + mRightBracketWidth + childWidth;
                mTabTempRect.set(0, 0, totalBracketWidth, height);
                if(!mTabTempRect.equals(mBracketRect))
                {
                    invalidate(mBracketRect);
                    mBracketRect.set(mTabTempRect);
                    invalidate(mTabTempRect);
                    if(mBracket != null)
                        mBracket.setBounds(mBracketRect);
                }
            }
            int childRight = childLeft + childWidth;
            child.layout(mWindowLeft + childLeft, mWindowTop + childTop, childLeft, childTop, childRight, childBottom);
            childLeft += childWidth;
            if(i == mSelectedIndex)
            {
                childLeft += mRightBracketWidth;
                continue;
            }
            if(i != mSelectedIndex - 1)
                childLeft += mSpaceBetweenUnselectedItems;
        }

    }

    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        int count = getChildCount();
        for(int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }

        mColorDirty = true;
        invalidate();
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(mBracket != null)
        {
            if(mColorDirty)
            {
                mBracket.setColorFilter(isEnabled() ? mSelectedColor : mDisabledColor, android.graphics.PorterDuff.Mode.SRC_IN);
                mColorDirty = false;
            }
            mBracket.draw(canvas);
        }
    }

    public void setColors(int selectedColor, int normalColor)
    {
        setColors(selectedColor, normalColor, (normalColor & 0xffffff) + 0x55000000);
    }

    public void setColors(int selectedColor, int normalColor, int disabledColor)
    {
        if(mSelectedColor != selectedColor)
        {
            mColorDirty = true;
            mSelectedColor = selectedColor;
        }
        mNormalColor = normalColor;
    }

    public int getSelectedColor()
    {
        return mSelectedColor;
    }

    public int getNormalColor()
    {
        return mNormalColor;
    }

    public int getDisabledColor()
    {
        return mDisabledColor;
    }

    private Drawable mBracket;
    private int mLeftBracketWidth;
    private int mRightBracketWidth;
    private int mBracketHeight;
    private int mSpaceBetweenUnselectedItems;
    private int mSelectedIndex;
    private Rect mBracketRect;
    private Rect mTabTempRect;
    private int mSelectedColor;
    private int mNormalColor;
    private int mDisabledColor;
    private boolean mColorDirty;
}

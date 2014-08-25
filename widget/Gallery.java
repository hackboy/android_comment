// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Gallery.java

package android.widget;

import android.R;
import android.content.Context;
import android.content.Resources;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.*;
import java.util.Map;

// Referenced classes of package android.widget:
//            AbsSpinner, GalleryAdapter, SpinnerAdapter

public class Gallery extends AbsSpinner
{
    public static class LayoutParams extends android.view.ViewGroup.LayoutParams
    {

        float scale;
        float alpha;
        int oldLeft;
        int oldTop;
        float oldScale;
        float oldAlpha;

        public LayoutParams(Context c, AttributeSet attrs)
        {
            super(c, attrs);
        }

        public LayoutParams(int w, int h)
        {
            super(w, h);
        }
    }


    public Gallery(Context context)
    {
        this(context, null, null);
    }

    public Gallery(Context context, AttributeSet attrs, Map inflateParams)
    {
        this(context, attrs, inflateParams, 0x10100d6);
    }

    public Gallery(Context context, AttributeSet attrs, Map inflateParams, int defStyle)
    {
        super(context, attrs, inflateParams, defStyle);
        mSpacing = 0;
        mAnimationDuration = 200;
        mAnimateNextLayout = false;
        android.content.Resources.StyledAttributes a = context.obtainStyledAttributes(attrs, android.R.styleable.Gallery, defStyle, 0);
        int animationDuration = a.getInt(0, -1);
        if(animationDuration > 0)
            setAnimationDuration(animationDuration);
        int spacing = a.getInt(1, -1);
        if(spacing > 0)
            setSpacing(spacing);
        a.recycle();
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new LayoutParams(getContext(), attrs);
    }

    public void setAnimateNextLayout(boolean animate)
    {
        mAnimateNextLayout = animate;
    }

    public void setAnimationDuration(int animationDurationMillis)
    {
        mAnimationDuration = animationDurationMillis;
    }

    public void setSpacing(int spacing)
    {
        mSpacing = spacing;
    }

    protected int computeHorizontalScrollExtent()
    {
        return 1;
    }

    protected int computeHorizontalScrollOffset()
    {
        return mSelectedPosition;
    }

    protected int computeHorizontalScrollRange()
    {
        return mItemCount;
    }

    protected void onFocusChanged(boolean focused, int direction)
    {
        super.onFocusChanged(focused, direction);
        if(getChildCount() > 0)
        {
            mAnimateNextLayout = getWidth() > 0 && getHeight() > 0;
            requestLayout();
        }
    }

    protected android.view.ViewGroup.LayoutParams getDefaultLayoutParams()
    {
        return new LayoutParams(-2, -2);
    }

    protected void onLayout(boolean changed, int wl, int wt, int l, int t, int r, int b)
    {
        if(mDataChanged || mAnimateNextLayout || changed || mNextSelectedRowId != mSelectedRowId)
        {
            mInLayout = true;
            layout(0, false);
            mInLayout = false;
        }
    }

    protected int getChildHeight(View child)
    {
        boolean focused = hasFocus();
        GalleryAdapter adapter = (GalleryAdapter)mAdapter;
        float scale = adapter.getScale(focused, 0);
        int height = child.getMeasuredHeight();
        if(scale != 1.0F)
            height = (int)((float)height * scale);
        return height;
    }

    protected boolean getChildStaticTransformation(View child, Transformation t)
    {
        LayoutParams lp = (LayoutParams)child.getLayoutParams();
        if(lp != null)
        {
            t.clear();
            t.setAlpha(lp.alpha);
            t.getMatrix().setScale(lp.scale, lp.scale);
        } else
        {
            return false;
        }
        return true;
    }

    protected void layout(int delta, boolean animate)
    {
        int childrenLeft = mSpinnerPadding.left;
        int childrenWidth = mRight - mLeft - mSpinnerPadding.left - mSpinnerPadding.right;
        int childrenRight = childrenLeft + childrenWidth;
        boolean animateLayout = animate || mAnimateNextLayout;
        mAnimateNextLayout = false;
        if(mDataChanged)
            handleDataChanged();
        if(mItemCount == 0)
        {
            resetList();
            return;
        }
        if(mNextSelectedPosition >= 0)
            setSelectedPositionInt(mNextSelectedPosition);
        setSelectorState();
        recycleAllViews();
        removeAllViewsInLayout();
        mRightMost = 0;
        mLeftMost = 0;
        mFirstChild = mSelectedPosition;
        View sel = makeAndAddView(mSelectedPosition, 0, 0, animateLayout, true);
        LayoutParams lp = (LayoutParams)sel.getLayoutParams();
        int width = scaleDimension(sel.getMeasuredWidth(), lp);
        int height = scaleDimension(sel.getMeasuredHeight(), lp);
        int selectedOffset = (childrenLeft + childrenWidth / 2) - width / 2;
        sel.offsetLeftAndRight(selectedOffset);
        int selLeft = sel.getLeft();
        int selTop = sel.getTop();
        positionSelector(selLeft, selTop, selLeft + width, selTop + height);
        long animationStart = System.currentTimeMillis();
        if(animateLayout)
            animateChild(sel, animationStart);
        fillRight(childrenRight, animateLayout, sel, lp, animationStart);
        fillLeft(childrenLeft, animateLayout, sel, animationStart);
        mRecycler.clear();
        invalidate();
        checkSelectionChanged();
        updateScrollIndicators();
        mDataChanged = false;
        setNextSelectedPositionInt(mSelectedPosition);
    }

    private void fillLeft(int childrenLeft, boolean animateLayout, View sel, long animationStart)
    {
        int pos = mSelectedPosition - 1;
        for(int nextRight = sel.getLeft() - mSelectionLeftPadding - mSpacing; pos >= 0 && (nextRight > childrenLeft || animateLayout && slidingVisibleToLeft(pos, childrenLeft)); pos--)
        {
            View child = makeAndAddView(pos, pos - mSelectedPosition, nextRight, animateLayout, false);
            if(animateLayout)
                animateChild(child, animationStart);
            mFirstChild = pos;
            nextRight = child.getLeft() - mSpacing;
        }

    }

    private void fillRight(int childrenRight, boolean animateLayout, View sel, LayoutParams lp, long animationStart)
    {
        int pos = mSelectedPosition + 1;
        for(int nextLeft = sel.getLeft() + scaleDimension(sel.getMeasuredWidth(), lp) + mSpacing; pos < mItemCount && (nextLeft < childrenRight || animateLayout && slidingVisibleToRight(pos, childrenRight)); pos++)
        {
            View child = makeAndAddView(pos, pos - mSelectedPosition, nextLeft, animateLayout, true);
            if(animateLayout)
                animateChild(child, animationStart);
            lp = (LayoutParams)child.getLayoutParams();
            nextLeft = child.getLeft() + scaleDimension(child.getMeasuredWidth(), lp) + mSpacing;
        }

    }

    private View makeAndAddView(int position, int offset, int x, boolean animate, boolean fromLeft)
    {
        View child;
        if(!mDataChanged)
        {
            child = mRecycler.get(position);
            if(child != null)
            {
                LayoutParams lp = (LayoutParams)child.getLayoutParams();
                lp.oldScale = lp.scale;
                lp.oldAlpha = lp.alpha;
                lp.oldLeft = child.getLeft();
                lp.oldTop = child.getTop();
                mRightMost = Math.max(mRightMost, lp.oldLeft + scaleDimension(child.getMeasuredWidth(), lp));
                mLeftMost = Math.min(mLeftMost, lp.oldLeft);
                setUpChild(child, offset, x, fromLeft);
                return child;
            }
        }
        child = mAdapter.getView(position, null, this);
        setUpChild(child, offset, x, fromLeft);
        if(animate)
        {
            LayoutParams lp = (LayoutParams)child.getLayoutParams();
            GalleryAdapter adapter = (GalleryAdapter)mAdapter;
            boolean focused = hasFocus();
            if(fromLeft)
            {
                lp.oldScale = adapter.getScale(focused, offset + 1);
                lp.oldAlpha = adapter.getAlpha(focused, offset + 1);
                lp.oldLeft = mRightMost + mSpacing;
                lp.oldTop = calculateTop(child, lp);
                mRightMost = Math.max(mRightMost, lp.oldLeft + scaleDimension(child.getMeasuredWidth(), lp));
            } else
            {
                lp.oldScale = adapter.getScale(focused, offset - 1);
                lp.oldAlpha = adapter.getAlpha(focused, offset - 1);
                lp.oldLeft = mLeftMost - scaleDimension(child.getMeasuredWidth(), lp) - mSpacing;
                lp.oldTop = calculateTop(child, lp);
                mLeftMost = Math.min(mLeftMost, lp.oldLeft);
            }
        }
        return child;
    }

    private boolean slidingVisibleToLeft(int pos, int childrenLeft)
    {
        View oldView = mRecycler.peek(pos);
        if(oldView == null)
        {
            return false;
        } else
        {
            int left = oldView.getLeft();
            int right = left + scaleDimension(oldView.getMeasuredWidth(), (LayoutParams)oldView.getLayoutParams());
            return right > childrenLeft;
        }
    }

    private boolean slidingVisibleToRight(int pos, int childrenRight)
    {
        View oldView = mRecycler.peek(pos);
        if(oldView == null)
        {
            return false;
        } else
        {
            int left = oldView.getLeft();
            return left < childrenRight;
        }
    }

    private void setUpChild(View child, int offset, int x, boolean fromLeft)
    {
        LayoutParams lp = (LayoutParams)child.getLayoutParams();
        if(lp == null)
            lp = (LayoutParams)getDefaultLayoutParams();
        GalleryAdapter adapter = (GalleryAdapter)mAdapter;
        boolean focused = hasFocus();
        lp.alpha = adapter.getAlpha(focused, offset);
        lp.scale = adapter.getScale(focused, offset);
        addViewInLayout(child, fromLeft ? -1 : 0, lp);
        child.setSelected(offset == 0);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(mHeightMeasureSpec, mSpinnerPadding.top + mSpinnerPadding.bottom, lp.height);
        int childWidthSpec = ViewGroup.getChildMeasureSpec(mWidthMeasureSpec, mSpinnerPadding.left + mSpinnerPadding.right, lp.width);
        child.measure(childWidthSpec, childHeightSpec);
        int childTop = calculateTop(child, lp);
        int childBottom = childTop + child.getMeasuredHeight();
        int width = child.getMeasuredWidth();
        int childLeft;
        int childRight;
        if(fromLeft)
        {
            childLeft = x;
            childRight = childLeft + width;
        } else
        {
            childLeft = x - scaleDimension(width, lp);
            childRight = childLeft + width;
        }
        child.layout(mWindowLeft + childLeft, mWindowTop + childTop, childLeft, childTop, childRight, childBottom);
    }

    private int calculateTop(View child, LayoutParams lp)
    {
        int childTop = 0;
        switch(mGravity)
        {
        case 48: // '0'
            childTop = mSpinnerPadding.top;
            break;

        case 16: // '\020'
            int availableSpace = mMeasuredHeight - mSpinnerPadding.bottom - mSpinnerPadding.top - scaleDimension(child.getMeasuredHeight(), lp);
            childTop = mSpinnerPadding.top + availableSpace / 2;
            break;

        case 80: // 'P'
            childTop = mMeasuredHeight - mSpinnerPadding.bottom - scaleDimension(child.getMeasuredHeight(), lp);
            break;
        }
        return childTop;
    }

    private void animateChild(View child, long startTime)
    {
        int newTop = child.getTop();
        int newLeft = child.getLeft();
        LayoutParams lp = (LayoutParams)child.getLayoutParams();
        AnimationSet set = new AnimationSet(true);
        if(mInterpolator == null)
            mInterpolator = new EaseInOutInterpolator();
        set.setInterpolator(mInterpolator);
        if(lp.oldScale != lp.scale || lp.scale != 1.0F)
        {
            ScaleAnimation scale = new ScaleAnimation(lp.oldScale, lp.scale, lp.oldScale, lp.scale);
            scale.setDuration(mAnimationDuration);
            set.addAnimation(scale);
        }
        if(newTop != lp.oldTop || newLeft != lp.oldLeft)
        {
            TranslateAnimation translate = new TranslateAnimation(lp.oldLeft - newLeft, 0.0F, lp.oldTop - newTop, 0.0F);
            translate.setDuration(mAnimationDuration);
            set.addAnimation(translate);
        }
        if(lp.oldAlpha != lp.alpha || lp.alpha != 1.0F)
        {
            AlphaAnimation alpha = new AlphaAnimation(lp.oldAlpha, lp.alpha);
            alpha.setDuration(mAnimationDuration);
            set.addAnimation(alpha);
        }
        set.setStartTime(startTime);
        child.setCurrentAnimation(set);
    }

    private int scaleDimension(int d, LayoutParams lp)
    {
        if(lp.scale != 1.0F)
            d = (int)((float)d * lp.scale);
        return d;
    }

    public volatile android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet x0)
    {
        return generateLayoutParams(x0);
    }

    private int mLeftMost;
    private int mRightMost;
    private int mSpacing;
    private int mAnimationDuration;
    private boolean mAnimateNextLayout;
}

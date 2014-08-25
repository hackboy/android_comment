// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TableRow.java

package android.widget;

import android.R;
import android.content.Context;
import android.content.Resources;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.*;
import java.util.Map;

// Referenced classes of package android.widget:
//            LinearLayout

public class TableRow extends LinearLayout
{
    private class ChildrenTracker
        implements android.view.ViewGroup.OnHierarchyChangeListener
    {

        private void setOnHierarchyChangeListener(android.view.ViewGroup.OnHierarchyChangeListener listener)
        {
            this.listener = listener;
        }

        public void onChildViewAdded(View parent, View child)
        {
            mColumnToChildIndex = null;
            if(listener != null)
                listener.onChildViewAdded(parent, child);
        }

        public void onChildViewRemoved(View parent, View child)
        {
            mColumnToChildIndex = null;
            if(listener != null)
                listener.onChildViewRemoved(parent, child);
        }

        private android.view.ViewGroup.OnHierarchyChangeListener listener;
        final TableRow this$0;


        private ChildrenTracker()
        {
            this$0 = TableRow.this;
            super();
        }

    }

    public static class LayoutParams extends LinearLayout.LayoutParams
    {

        protected void setBaseAttributes(android.content.Resources.StyledAttributes a, int widthAttr, int heightAttr)
        {
            width = -1;
            height = -2;
        }

        public int column;
        public int span;
        private int mOffset[];


        public LayoutParams(Context c, AttributeSet attrs)
        {
            super(c, attrs);
            mOffset = new int[2];
            android.content.Resources.StyledAttributes a = c.obtainStyledAttributes(attrs, android.R.styleable.TableRow_Cell);
            column = a.getInt(0, -1);
            span = a.getInt(1, 1);
            if(span <= 1)
                span = 1;
            a.recycle();
        }

        public LayoutParams(int w, int h)
        {
            super(-1, -2);
            mOffset = new int[2];
            span = 1;
        }

        public LayoutParams(int w, int h, float initWeight)
        {
            super(-1, -2, initWeight);
            mOffset = new int[2];
            span = 1;
        }

        public LayoutParams()
        {
            super(-1, -2);
            mOffset = new int[2];
            span = 1;
        }

        public LayoutParams(int column)
        {
            this();
            this.column = column;
        }
    }


    public TableRow(Context context)
    {
        super(context);
        mNumColumns = 0;
        initTableRow();
    }

    public TableRow(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
        mNumColumns = 0;
        initTableRow();
    }

    private void initTableRow()
    {
        android.view.ViewGroup.OnHierarchyChangeListener oldListener = mOnHierarchyChangeListener;
        mChildrenTracker = new ChildrenTracker();
        if(oldListener != null)
            mChildrenTracker.setOnHierarchyChangeListener(oldListener);
        super.setOnHierarchyChangeListener(mChildrenTracker);
    }

    public void addView(View child)
    {
        android.view.ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
        if(layoutParams == null)
            layoutParams = new LayoutParams();
        super.addView(child, -1, layoutParams);
    }

    public void setOnHierarchyChangeListener(android.view.ViewGroup.OnHierarchyChangeListener listener)
    {
        mChildrenTracker.setOnHierarchyChangeListener(listener);
    }

    void setColumnCollapsed(int columnIndex, boolean collapsed)
    {
        View child = getVirtualChildAt(columnIndex);
        if(child != null)
            child.setVisibility(collapsed ? 8 : 0);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        measureHorizontal(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int wl, int wt, int l, int t, int r, int b)
    {
        layOutHorizontal();
        mAnimateNextLayout = false;
    }

    public View getVirtualChildAt(int i)
    {
        if(mColumnToChildIndex == null)
            mapIndexAndColumns();
        int deflectedIndex = mColumnToChildIndex.get(i, -1);
        if(deflectedIndex != -1)
            return getChildAt(deflectedIndex);
        else
            return null;
    }

    public int getVirtualChildCount()
    {
        if(mColumnToChildIndex == null)
            mapIndexAndColumns();
        return mNumColumns;
    }

    private void mapIndexAndColumns()
    {
        if(mColumnToChildIndex == null)
        {
            int virtualCount = 0;
            int count = getChildCount();
            mColumnToChildIndex = new SparseIntArray();
            for(int i = 0; i < count; i++)
            {
                View child = getChildAt(i);
                LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if(layoutParams.column >= virtualCount)
                    virtualCount = layoutParams.column;
                for(int j = 0; j < layoutParams.span; j++)
                    mColumnToChildIndex.put(virtualCount++, i);

            }

            mNumColumns = virtualCount;
        }
    }

    protected int measureNullChild(int childIndex)
    {
        return mConstrainedColumnWidths[childIndex];
    }

    protected void measureChildBeforeLayout(View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight)
    {
        if(mConstrainedColumnWidths != null)
        {
            LayoutParams lp = (LayoutParams)child.getLayoutParams();
            int measureMode = 0x40000000;
            int columnWidth = 0;
            for(int i = 0; i < lp.span; i++)
                columnWidth += mConstrainedColumnWidths[childIndex + i];

            int gravity = lp.gravity;
            boolean isHorizontalGravity = Gravity.isHorizontal(gravity);
            if(isHorizontalGravity)
                measureMode = 0x80000000;
            int childWidthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(Math.max(0, columnWidth), measureMode);
            int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin + totalHeight, lp.height);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            if(isHorizontalGravity)
            {
                int childWidth = child.getMeasuredWidth();
                lp.mOffset[1] = columnWidth - childWidth;
                switch(gravity & 7)
                {
                case 5: // '\005'
                    lp.mOffset[0] = lp.mOffset[1];
                    break;

                case 1: // '\001'
                    lp.mOffset[0] = lp.mOffset[1] / 2;
                    break;
                }
            } else
            {
                lp.mOffset[0] = lp.mOffset[1] = 0;
            }
        } else
        {
            super.measureChildBeforeLayout(child, childIndex, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
        }
    }

    protected int getChildrenSkipCount(View child, int index)
    {
        LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
        return layoutParams.span - 1;
    }

    protected int getLocationOffset(View child)
    {
        return ((LayoutParams)child.getLayoutParams()).mOffset[0];
    }

    protected int getNextLocationOffset(View child)
    {
        return ((LayoutParams)child.getLayoutParams()).mOffset[1];
    }

    int[] getColumnsWidths()
    {
        int numColumns = getVirtualChildCount();
        if(mColumnWidths == null || numColumns != mColumnWidths.length)
            mColumnWidths = new int[numColumns];
        for(int i = 0; i < numColumns; i++)
        {
            View child = getVirtualChildAt(i);
            if(child != null && child.getVisibility() != 8)
            {
                int freeSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, 0);
                child.measure(freeSpec, freeSpec);
                int width = child.getMeasuredWidth();
                mColumnWidths[i] = width;
                LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                for(int j = 1; j < layoutParams.span; j++)
                    mColumnWidths[i++] = width;

            } else
            {
                mColumnWidths[i] = 0;
            }
        }

        return mColumnWidths;
    }

    void setColumnsWidthConstraints(int columnWidths[])
    {
        if(columnWidths == null || columnWidths.length < getVirtualChildCount())
        {
            throw new IllegalArgumentException("columnWidths should be >= getVirtualChildCount()");
        } else
        {
            mConstrainedColumnWidths = columnWidths;
            return;
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

    public volatile LinearLayout.LayoutParams generateLayoutParams(AttributeSet x0)
    {
        return generateLayoutParams(x0);
    }

    public volatile android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet x0)
    {
        return generateLayoutParams(x0);
    }

    private int mNumColumns;
    private int mColumnWidths[];
    private int mConstrainedColumnWidths[];
    private SparseIntArray mColumnToChildIndex;
    private ChildrenTracker mChildrenTracker;

}

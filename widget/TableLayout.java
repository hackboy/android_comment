// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TableLayout.java

package android.widget;

import android.R;
import android.content.Context;
import android.content.Resources;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;
import java.util.regex.Pattern;

// Referenced classes of package android.widget:
//            LinearLayout, TableRow

public class TableLayout extends LinearLayout
{
    private class PassThroughHierarchyChangeListener
        implements android.view.ViewGroup.OnHierarchyChangeListener
    {

        public void onChildViewAdded(View parent, View child)
        {
            trackCollapsedColumns(child);
            if(mOnHierarchyChangeListener != null)
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
        }

        public void onChildViewRemoved(View parent, View child)
        {
            if(mOnHierarchyChangeListener != null)
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
        }

        private android.view.ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;
        final TableLayout this$0;


        private PassThroughHierarchyChangeListener()
        {
            this$0 = TableLayout.this;
            super();
        }

    }

    public static class LayoutParams extends LinearLayout.LayoutParams
    {

        protected void setBaseAttributes(android.content.Resources.StyledAttributes a, int widthAttr, int heightAttr)
        {
            width = -1;
            if(a.hasValue(heightAttr))
                height = a.getLayoutDimension(heightAttr, "layout_height");
            else
                height = -2;
        }

        public LayoutParams(Context c, AttributeSet attrs)
        {
            super(c, attrs);
        }

        public LayoutParams(int w, int h)
        {
            super(-1, h);
        }

        public LayoutParams(int w, int h, float initWeight)
        {
            super(-1, h, initWeight);
        }

        public LayoutParams()
        {
            super(-1, -2);
        }
    }


    public TableLayout(Context context)
    {
        super(context);
        initTableLayout();
    }

    public TableLayout(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
        android.content.Resources.StyledAttributes a = context.obtainStyledAttributes(attrs, android.R.styleable.TableLayout);
        CharSequence stretchedColumns = a.getString(0);
        if(stretchedColumns != null)
            mStretchableColumns = parseColumns(stretchedColumns);
        CharSequence shrinkedColumns = a.getString(1);
        if(shrinkedColumns != null)
            mShrinkableColumns = parseColumns(shrinkedColumns);
        CharSequence collapsedColumns = a.getString(2);
        if(collapsedColumns != null)
            mCollapsedColumns = parseColumns(collapsedColumns);
        a.recycle();
        initTableLayout();
    }

    private static SparseBooleanArray parseColumns(CharSequence sequence)
    {
        SparseBooleanArray columns = new SparseBooleanArray();
        Pattern pattern = Pattern.compile("\\s*,\\s*");
        String columnDefs[] = pattern.split(sequence);
        String arr$[] = columnDefs;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            String columnIdentifier = arr$[i$];
            try
            {
                int columnIndex = Integer.parseInt(columnIdentifier);
                if(columnIndex >= 0)
                    columns.put(columnIndex, true);
            }
            catch(NumberFormatException e) { }
        }

        return columns;
    }

    private void initTableLayout()
    {
        if(mCollapsedColumns == null)
            mCollapsedColumns = new SparseBooleanArray();
        if(mStretchableColumns == null)
            mStretchableColumns = new SparseBooleanArray();
        if(mShrinkableColumns == null)
            mShrinkableColumns = new SparseBooleanArray();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
        mInitialized = true;
    }

    public void setOnHierarchyChangeListener(android.view.ViewGroup.OnHierarchyChangeListener listener)
    {
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    public void requestLayout()
    {
        if(mInitialized)
        {
            int count = getChildCount();
            for(int i = 0; i < count; i++)
                getChildAt(i).forceLayout();

        }
        super.requestLayout();
    }

    public void setColumnCollapsed(int columnIndex, boolean isCollapsed)
    {
        mCollapsedColumns.put(columnIndex, isCollapsed);
        int count = getChildCount();
        for(int i = 0; i < count; i++)
        {
            View view = getChildAt(i);
            if(view instanceof TableRow)
                ((TableRow)view).setColumnCollapsed(columnIndex, isCollapsed);
        }

        requestLayout();
    }

    public boolean isColumnCollapsed(int columnIndex)
    {
        return mCollapsedColumns.get(columnIndex);
    }

    public void setColumnStretchable(int columnIndex, boolean isStretchable)
    {
        mStretchableColumns.put(columnIndex, isStretchable);
        requestLayout();
    }

    public boolean isColumnStretchable(int columnIndex)
    {
        return mStretchableColumns.get(columnIndex);
    }

    public void setColumnShrinkable(int columnIndex, boolean isShrinkable)
    {
        mShrinkableColumns.put(columnIndex, isShrinkable);
        requestLayout();
    }

    public boolean isColumnShrinkable(int columnIndex)
    {
        return mStretchableColumns.get(columnIndex);
    }

    private void trackCollapsedColumns(View child)
    {
        if(child instanceof TableRow)
        {
            TableRow row = (TableRow)child;
            int count = mCollapsedColumns.size();
            for(int i = 0; i < count; i++)
            {
                int columnIndex = mCollapsedColumns.keyAt(i);
                boolean isCollapsed = mCollapsedColumns.valueAt(i);
                if(isCollapsed)
                    row.setColumnCollapsed(columnIndex, isCollapsed);
            }

        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        measureVertical(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int wl, int wt, int l, int t, int r, int b)
    {
        layOutVertical();
        mAnimateNextLayout = false;
    }

    protected void measureChildBeforeLayout(View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight)
    {
        if(child instanceof TableRow)
            ((TableRow)child).setColumnsWidthConstraints(mMaxWidths);
        super.measureChildBeforeLayout(child, childIndex, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
    }

    protected void measureVertical(int widthMeasureSpec, int heightMeasureSpec)
    {
        findLargestCells();
        shrinkAndStretchColumns(widthMeasureSpec);
        super.measureVertical(widthMeasureSpec, heightMeasureSpec);
    }

    private void findLargestCells()
    {
        boolean firstRow = true;
        int count = getChildCount();
        for(int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            if(child.getVisibility() == 8 || !(child instanceof TableRow))
                continue;
            TableRow row = (TableRow)child;
            android.view.ViewGroup.LayoutParams layoutParams = row.getLayoutParams();
            layoutParams.height = -2;
            int widths[] = row.getColumnsWidths();
            if(firstRow)
            {
                if(mMaxWidths == null)
                    mMaxWidths = new int[widths.length];
                System.arraycopy(widths, 0, mMaxWidths, 0, widths.length);
                firstRow = false;
                continue;
            }
            int difference = widths.length - mMaxWidths.length;
            if(difference > 0)
            {
                int oldMaxWidths[] = mMaxWidths;
                mMaxWidths = new int[widths.length];
                System.arraycopy(oldMaxWidths, 0, mMaxWidths, 0, oldMaxWidths.length);
                System.arraycopy(widths, oldMaxWidths.length, mMaxWidths, oldMaxWidths.length, difference);
            }
            difference = widths.length - mMaxWidths.length;
            int newLength = mMaxWidths.length + difference;
            for(int j = 0; j < newLength; j++)
                mMaxWidths[j] = Math.max(mMaxWidths[j], widths[j]);

        }

    }

    private void shrinkAndStretchColumns(int widthMeasureSpec)
    {
        if(getChildCount() == 0)
            return;
        int totalWidth = 0;
        int arr$[] = mMaxWidths;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            int width = arr$[i$];
            totalWidth += width;
        }

        int size = android.view.View.MeasureSpec.getSize(widthMeasureSpec) - mPaddingLeft - mPaddingRight;
        if(totalWidth > size && mShrinkableColumns.size() > 0)
            mutateColumnsWidth(mShrinkableColumns, size, totalWidth);
        else
        if(totalWidth < size && mStretchableColumns.size() > 0)
            mutateColumnsWidth(mStretchableColumns, size, totalWidth);
    }

    private void mutateColumnsWidth(SparseBooleanArray columns, int size, int totalWidth)
    {
        int skipped = 0;
        int count = columns.size();
        int totalExtraSpace = size - totalWidth;
        int extraSpace = totalExtraSpace / count;
        for(int i = 0; i < count; i++)
        {
            int column = columns.keyAt(i);
            if(!columns.valueAt(i))
                continue;
            if(column < mMaxWidths.length)
                mMaxWidths[column] += extraSpace;
            else
                skipped++;
        }

        if(skipped > 0 && skipped < count)
        {
            extraSpace = (skipped * extraSpace) / (count - skipped);
            for(int i = 0; i < count; i++)
            {
                int column = columns.keyAt(i);
                if(!columns.valueAt(i) || column >= mMaxWidths.length)
                    continue;
                if(extraSpace > mMaxWidths[column])
                    mMaxWidths[column] = 0;
                else
                    mMaxWidths[column] += extraSpace;
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

    public volatile LinearLayout.LayoutParams generateLayoutParams(AttributeSet x0)
    {
        return generateLayoutParams(x0);
    }

    public volatile android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet x0)
    {
        return generateLayoutParams(x0);
    }

    private int mMaxWidths[];
    private SparseBooleanArray mStretchableColumns;
    private SparseBooleanArray mShrinkableColumns;
    private SparseBooleanArray mCollapsedColumns;
    private PassThroughHierarchyChangeListener mPassThroughListener;
    private boolean mInitialized;

}

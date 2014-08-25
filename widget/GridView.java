// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GridView.java

package android.widget;

import android.R;
import android.content.Context;
import android.content.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.*;
import java.util.Map;

// Referenced classes of package android.widget:
//            AbsListView, ListAdapter, AdapterView, Adapter

public class GridView extends AbsListView
{

    public GridView(Context context)
    {
        super(context);
        mNumColumns = -1;
        mHorizontalSpacing = 0;
        mVerticalSpacing = 0;
        mStretchMode = 2;
        mTallestView = null;
        mTallestInSelectedRow = null;
        mGravity = 51;
    }

    public GridView(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
        mNumColumns = -1;
        mHorizontalSpacing = 0;
        mVerticalSpacing = 0;
        mStretchMode = 2;
        mTallestView = null;
        mTallestInSelectedRow = null;
        mGravity = 51;
        android.content.Resources.StyledAttributes a = context.obtainStyledAttributes(attrs, android.R.styleable.GridView);
        int hSpacing = a.getInt(1, 0);
        setHorizontalSpacing(hSpacing);
        int vSpacing = a.getInt(2, 0);
        setVerticalSpacing(vSpacing);
        int index = a.getInt(3, 2);
        if(index >= 0)
            setStretchMode(index);
        int columnWidth = a.getInt(5, -1);
        if(columnWidth > 0)
            setColumnWidth(columnWidth);
        int numColumns = a.getInt(4, 1);
        setNumColumns(numColumns);
        index = a.getInt(0, -1);
        if(index >= 0)
            setGravity(index);
    }

    public ListAdapter getAdapter()
    {
        return mAdapter;
    }

    public void setAdapter(ListAdapter adapter)
    {
        if(null != mAdapter)
        {
            mAdapter.unregisterContentObserver(mChangeObserver);
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
            mDataChanged = true;
        }
        mAdapter = adapter;
        if(mAdapter != null)
        {
            mItemCount = mAdapter.getCount();
            mDataChanged = true;
            checkFocus();
            mChangeObserver = new AdapterView.ChangeObserver(this);
            mDataSetObserver = new AdapterView.MyDataSetObserver(this);
            mAdapter.registerContentObserver(mChangeObserver);
            mAdapter.registerDataSetObserver(mDataSetObserver);
            if(mItemCount > 0)
            {
                setSelectedPositionInt(0);
                setNextSelectedPositionInt(0);
            } else
            {
                setSelectedPositionInt(-1);
                setNextSelectedPositionInt(-1);
                selectionChanged();
            }
        } else
        {
            selectionChanged();
        }
        requestLayout();
    }

    private View fillDown(int pos, int nextTop)
    {
        View selectedView = null;
        for(int end = mBottom - mTop - mListPadding.bottom; nextTop < end && pos < mItemCount; pos += mNumColumns)
        {
            View temp = makeRow(pos, nextTop, true);
            if(temp != null)
                selectedView = temp;
            View tallestChild = mTallestView;
            nextTop = tallestChild.getBottom() + mVerticalSpacing;
        }

        return selectedView;
    }

    private View makeRow(int startPos, int y, boolean flow)
    {
        int last = Math.min(startPos + mNumColumns, mItemCount);
        int nextLeft = mListPadding.left;
        View selectedView = null;
        boolean hasFocus = hasFocus();
        int rowCount = last - startPos;
        mTallestView = null;
        for(int pos = startPos; pos < last; pos++)
        {
            boolean selected = pos == mSelectedPosition;
            int where = flow ? -1 : pos - startPos;
            View child = makeAndAddView(pos, y, flow, nextLeft, selected, where);
            nextLeft += mColumnWidth;
            if(pos < last - 1)
                nextLeft += mHorizontalSpacing;
            if(selected && hasFocus)
                selectedView = child;
            if(flow)
            {
                if(mTallestView == null || child.getBottom() > mTallestView.getBottom())
                    mTallestView = child;
                continue;
            }
            if(mTallestView == null || child.getTop() < mTallestView.getTop())
                mTallestView = child;
        }

        int verticalGravity = mGravity & 0x70;
        if(flow && verticalGravity != 48)
        {
            int tallestBottom = mTallestView.getBottom();
            int first = getChildCount() - rowCount;
            last = first + rowCount;
            for(int i = first; i < last; i++)
            {
                View child = getChildAt(i);
                int childBottom = child.getBottom();
                if(childBottom < tallestBottom)
                    if(verticalGravity == 80)
                        child.offsetTopAndBottom(tallestBottom - childBottom);
                    else
                        child.offsetTopAndBottom((tallestBottom - childBottom) / 2);
            }

        } else
        if(!flow && verticalGravity != 80)
        {
            int tallestTop = mTallestView.getTop();
            for(int i = 0; i < rowCount; i++)
            {
                View child = getChildAt(i);
                int childTop = child.getTop();
                if(childTop <= tallestTop)
                    continue;
                if(verticalGravity == 48)
                    child.offsetTopAndBottom(tallestTop - childTop);
                else
                    child.offsetTopAndBottom((tallestTop - childTop) / 2);
            }

        }
        if(selectedView != null)
            mTallestInSelectedRow = mTallestView;
        return selectedView;
    }

    private View fillUp(int pos, int nextBottom)
    {
        View selectedView = null;
        for(int end = mListPadding.top; nextBottom > end && pos >= 0; pos -= mNumColumns)
        {
            View temp = makeRow(pos, nextBottom, false);
            if(temp != null)
                selectedView = temp;
            nextBottom = mTallestView.getTop() - mVerticalSpacing;
            mFirstPosition = pos;
        }

        return selectedView;
    }

    private View fillFromTop(int nextTop)
    {
        mFirstPosition = Math.min(mFirstPosition, mSelectedPosition);
        mFirstPosition = Math.min(mFirstPosition, mItemCount - 1);
        if(mFirstPosition < 0)
            mFirstPosition = 0;
        mFirstPosition -= mFirstPosition % mNumColumns;
        return fillDown(mFirstPosition, nextTop);
    }

    private View fillFromMiddle(int childrenTop, int childrenBottom)
    {
        int rowStart = mSelectedPosition - mSelectedPosition % mNumColumns;
        View sel = makeRow(rowStart, 0, true);
        mFirstPosition = rowStart;
        int height = childrenBottom - childrenTop;
        View tallestChild = mTallestView;
        int selHeight = tallestChild.getMeasuredHeight();
        int count = getChildCount();
        int offset = (height - selHeight) / 2;
        for(int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            child.offsetTopAndBottom(offset);
        }

        fillUp(rowStart - mNumColumns, tallestChild.getTop() - mVerticalSpacing);
        adjustViewsUp();
        fillDown(rowStart + mNumColumns, tallestChild.getBottom() + mVerticalSpacing);
        return sel;
    }

    protected int findMotionRow(int y)
    {
        int childCount = getChildCount();
        if(childCount > 0)
        {
            for(int i = 0; i < childCount; i += mNumColumns)
            {
                View v = getChildAt(i);
                if(y <= v.getBottom())
                    return mFirstPosition + i;
            }

            return (mFirstPosition + childCount) - 1;
        } else
        {
            return -1;
        }
    }

    private View fillScroll()
    {
        int motionRowStart = mMotionPosition - mMotionPosition % mNumColumns;
        View temp = makeRow(motionRowStart, mMotionViewNewTop, true);
        mFirstPosition = motionRowStart;
        View tallestChild = mTallestView;
        View above = fillUp(motionRowStart - mNumColumns, tallestChild.getTop() - mVerticalSpacing);
        adjustViewsUp();
        View below = fillDown(motionRowStart + mNumColumns, tallestChild.getBottom() + mVerticalSpacing);
        if(temp != null)
            return temp;
        if(above != null)
            return above;
        else
            return below;
    }

    private void adjustViewsUp()
    {
        if(mFirstPosition == 0)
        {
            int childCount = getChildCount();
            if(childCount > 0)
            {
                View child = getChildAt(0);
                int delta = child.getTop() - mListPadding.top;
                if(delta < 0)
                    delta = 0;
                if(delta != 0)
                {
                    for(int i = 0; i < childCount; i++)
                    {
                        child = getChildAt(i);
                        child.offsetTopAndBottom(-delta);
                    }

                }
            }
        }
    }

    private View moveSelection(View oldSel, int delta, int childrenTop, int childrenBottom)
    {
        int fadingEdgeLength = getVerticalFadingEdgeLength();
        int selectedPosition = mSelectedPosition;
        int numColumns = mNumColumns;
        int verticalSpacing = mVerticalSpacing;
        int oldRowStart = selectedPosition - delta - (selectedPosition - delta) % numColumns;
        int rowStart = selectedPosition - selectedPosition % numColumns;
        int rowDelta = rowStart - oldRowStart;
        int topSelectionPixel = childrenTop;
        if(rowStart > 0)
            topSelectionPixel += fadingEdgeLength;
        int bottomSelectionPixel = childrenBottom;
        if((rowStart + numColumns) - 1 < mItemCount - 1)
            bottomSelectionPixel -= fadingEdgeLength;
        mFirstPosition = rowStart;
        View sel;
        View tallestChild;
        if(rowDelta > 0)
        {
            int oldBottom = mTallestInSelectedRow.getBottom();
            sel = makeRow(rowStart, oldBottom + verticalSpacing, true);
            tallestChild = mTallestView;
            if(tallestChild.getBottom() > bottomSelectionPixel)
            {
                int spaceAbove = tallestChild.getTop() - topSelectionPixel;
                int spaceBelow = tallestChild.getBottom() - bottomSelectionPixel;
                int offset = Math.min(spaceAbove, spaceBelow);
                offsetViews(-offset);
            }
        } else
        if(rowDelta < 0)
        {
            int oldTop = mTallestInSelectedRow.getTop();
            sel = makeRow(rowStart, oldTop - verticalSpacing, false);
            tallestChild = mTallestView;
            if(tallestChild.getTop() < topSelectionPixel)
            {
                int spaceAbove = topSelectionPixel - tallestChild.getTop();
                int spaceBelow = bottomSelectionPixel - tallestChild.getBottom();
                int offset = Math.min(spaceAbove, spaceBelow);
                offsetViews(offset);
            }
        } else
        {
            int oldTop = mTallestInSelectedRow.getTop();
            sel = makeRow(rowStart, oldTop, true);
            tallestChild = mTallestView;
        }
        fillDown(rowStart + numColumns, tallestChild.getBottom() + verticalSpacing);
        fillUp(rowStart - numColumns, tallestChild.getTop() - verticalSpacing);
        return sel;
    }

    private void offsetViews(int offset)
    {
        int childCount = getChildCount();
        for(int i = 0; i < childCount; i++)
        {
            View child = getChildAt(i);
            child.offsetTopAndBottom(offset);
        }

    }

    private void determineColumns(int availableSpace)
    {
        if(mRequestedNumColumns == -1)
            mNumColumns = (availableSpace + mRequestedHorizontalSpacing) / (mRequestedColumnWidth + mRequestedHorizontalSpacing);
        else
            mNumColumns = mRequestedNumColumns;
        switch(mStretchMode)
        {
        case 0: // '\0'
            mColumnWidth = mRequestedColumnWidth;
            mHorizontalSpacing = mRequestedHorizontalSpacing;
            break;

        default:
            int spaceLeftOver = availableSpace - mNumColumns * mRequestedColumnWidth - (mNumColumns - 1) * mRequestedHorizontalSpacing;
            switch(mStretchMode)
            {
            case 2: // '\002'
                mColumnWidth = mRequestedColumnWidth + spaceLeftOver / mNumColumns;
                mHorizontalSpacing = mRequestedHorizontalSpacing;
                break;

            case 1: // '\001'
                mColumnWidth = mRequestedColumnWidth;
                mHorizontalSpacing = mRequestedHorizontalSpacing + spaceLeftOver / (mNumColumns - 1);
                break;
            }
            break;
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
        int childWidth = widthSize - mListPadding.left - mListPadding.right;
        determineColumns(childWidth);
        if(widthMode == 0 || heightMode == 0)
            throw new RuntimeException("List views can't have UNSPECIFIED size");
        if(heightMode == 0x80000000)
        {
            mListPadding.top = mPaddingTop <= mSelectionTopPadding ? mSelectionTopPadding : mPaddingTop;
            mListPadding.bottom = mPaddingBottom <= mSelectionBottomPadding ? mSelectionBottomPadding : mPaddingBottom;
            int ourHeight = mListPadding.top + mListPadding.bottom;
            int maxHeight = 0;
            int numColumns = mNumColumns;
            int i = 0;
            do
            {
                if(i >= mItemCount)
                    break;
                View child = mAdapter.getView(i, null, this);
                mBlockLayoutRequests = true;
                child.setSelected(i == 0);
                mBlockLayoutRequests = false;
                child.measure(android.view.View.MeasureSpec.makeMeasureSpec(mColumnWidth, 0x40000000), android.view.View.MeasureSpec.makeMeasureSpec(0, 0));
                int childHeight = child.getMeasuredHeight();
                maxHeight = Math.max(maxHeight, childHeight);
                if(i % numColumns == numColumns - 1)
                {
                    ourHeight += maxHeight;
                    maxHeight = 0;
                }
                if(ourHeight >= heightSize)
                {
                    ourHeight += maxHeight;
                    break;
                }
                i++;
            } while(true);
            heightSize = Math.min(ourHeight, heightSize);
        }
        setMeasuredDimension(widthSize, heightSize);
        mWidthMeasureSpec = widthMeasureSpec;
    }

    protected void layout()
    {
        super.layout();
        int childrenTop = mListPadding.top;
        int childrenBottom = mBottom - mTop - mListPadding.bottom;
        int childrenHeight = childrenBottom - childrenTop;
        int childCount = getChildCount();
        int delta = 0;
        View sel = null;
        View oldSel = null;
        int index = mSelectedPosition - mFirstPosition;
        if(index >= 0 && index < childCount)
            oldSel = getChildAt(index);
        if(mDataChanged)
            handleDataChanged();
        if(mItemCount == 0)
        {
            resetList();
            return;
        }
        if(mNextSelectedPosition >= 0)
        {
            delta = mNextSelectedPosition - mSelectedPosition;
            setSelectedPositionInt(mNextSelectedPosition);
        }
        int firstFullyVisiblePosition = -1;
        int lastFullyVisiblePosition = -1;
        for(int i = 0; i < childCount; i++)
        {
            index = mFirstPosition + i;
            View v = getChildAt(i);
            mRecycler.put(index, v);
            if(firstFullyVisiblePosition < 0 && v.getTop() >= childrenTop)
                firstFullyVisiblePosition = index;
            if(v.getBottom() <= childrenBottom)
                lastFullyVisiblePosition = index;
        }

        int topOffset = 0;
        if(childCount > 0)
        {
            View topView = getChildAt(0);
            topOffset = topView.getTop();
        }
        removeAllViewsInLayout();
        switch(mLayoutMode)
        {
        case 2: // '\002'
            sel = fillFromMiddle(childrenTop, childrenBottom);
            break;

        case 4: // '\004'
        case 6: // '\006'
            break;

        case 5: // '\005'
            sel = fillScroll();
            break;

        case 1: // '\001'
            mFirstPosition = 0;
            sel = fillFromTop(childrenTop);
            break;

        case 3: // '\003'
        default:
            if(oldSel == null)
                sel = fillFromTop(childrenTop);
            else
                sel = moveSelection(oldSel, delta, childrenTop, childrenBottom);
            break;
        }
        mRecycler.clear();
        childCount = getChildCount();
        if(childCount > 0)
        {
            View child = getChildAt(0);
            delta = child.getTop() - mListPadding.top;
            if(delta < 0)
                delta = 0;
            if(delta != 0 && mFirstPosition == 0)
            {
                for(int i = 0; i < childCount; i++)
                {
                    child = getChildAt(i);
                    child.offsetTopAndBottom(-delta);
                }

            }
        }
        if(sel != null)
        {
            positionSelector(sel);
            mSelectedTop = sel.getTop();
        } else
        {
            mSelectedTop = 0;
            mSelectorRect.setEmpty();
        }
        mLayoutMode = 0;
        mDataChanged = false;
        mNeedSync = false;
        setNextSelectedPositionInt(mSelectedPosition);
        updateScrollIndicators();
        if(mLastSelectedRowID != mSelectedRowId)
        {
            if(mItemCount > 0)
                selectionChanged();
            mLastSelectedRowID = mSelectedRowId;
        }
        invalidate();
    }

    private View makeAndAddView(int position, int y, boolean flow, int childrenLeft, boolean selected, int where)
    {
        View child;
        if(!mDataChanged)
        {
            child = mRecycler.get(position);
            if(child != null)
            {
                setUpChild(child, y, flow, childrenLeft, selected, true, where);
                return child;
            }
        }
        child = obtainView(position);
        setUpChild(child, y, flow, childrenLeft, selected, false, where);
        return child;
    }

    private void setUpChild(View child, int y, boolean flow, int childrenLeft, boolean selected, boolean recycled, int where)
    {
        boolean isSelected = selected && hasFocus();
        boolean needToMeasure = !recycled || isSelected != child.isSelected() || child.isLayoutRequested();
        android.view.ViewGroup.LayoutParams p = child.getLayoutParams();
        if(p == null)
            p = new android.view.ViewGroup.LayoutParams(-1, -2);
        addViewInLayout(child, where, p);
        child.setSelected(isSelected);
        if(isSelected)
            requestFocus();
        android.view.ViewGroup.LayoutParams lp = child.getLayoutParams();
        if(needToMeasure)
        {
            int childHeightSpec = ViewGroup.getChildMeasureSpec(android.view.View.MeasureSpec.makeMeasureSpec(0, 0), 0, lp.height);
            int childWidthSpec = ViewGroup.getChildMeasureSpec(android.view.View.MeasureSpec.makeMeasureSpec(mColumnWidth, 0x40000000), 0, lp.width);
            child.measure(childWidthSpec, childHeightSpec);
        }
        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int childLeft = 0;
        int childTop = flow ? y : y - h;
        int childBottom = childTop + h;
        switch(mGravity & 7)
        {
        case 3: // '\003'
            childLeft = childrenLeft;
            break;

        case 1: // '\001'
            childLeft = childrenLeft + (mColumnWidth - w) / 2;
            break;

        case 5: // '\005'
            childLeft = (childrenLeft + mColumnWidth) - w;
            break;
        }
        int childRight = childLeft + w;
        child.layout(mWindowLeft + childLeft, mWindowTop + childTop, childLeft, childTop, childRight, childBottom);
    }

    public void setSelection(int position)
    {
        setNextSelectedPositionInt(position);
        mLayoutMode = 2;
        requestLayout();
    }

    private void setSelectionInt(int position)
    {
        mBlockLayoutRequests = true;
        int previousSelectedPosition = mNextSelectedPosition;
        setNextSelectedPositionInt(position);
        if(mNextSelectedPosition != mSelectedPosition - 1)
            if(mNextSelectedPosition != mSelectedPosition + 1);
        layout();
        int nextRow = mNextSelectedPosition / mNumColumns;
        int previousRow = previousSelectedPosition / mNumColumns;
        if(nextRow != previousRow)
            awakenScrollBars();
        mBlockLayoutRequests = false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(mItemCount > 0)
            switch(keyCode)
            {
            case 21: // '\025'
                if(mSelectedPosition != 0)
                {
                    setSelectionInt(mSelectedPosition - 1);
                    return true;
                }
                break;

            case 22: // '\026'
                if(mSelectedPosition < mItemCount - 1)
                {
                    setSelectionInt(mSelectedPosition + 1);
                    return true;
                }
                break;

            case 19: // '\023'
                if(mSelectedPosition >= mNumColumns)
                {
                    int newSelection = mSelectedPosition - mNumColumns;
                    setSelectionInt(newSelection);
                    return true;
                }
                break;

            case 20: // '\024'
                if(mSelectedPosition <= mItemCount - 1 - mNumColumns)
                {
                    int newSelection = mSelectedPosition + mNumColumns;
                    setSelectionInt(newSelection);
                    return true;
                }
                int startOfLastRow = ((mItemCount - 1) / mNumColumns) * mNumColumns;
                if(mSelectedPosition < startOfLastRow)
                    setSelectionInt(startOfLastRow);
                break;

            case 23: // '\027'
            case 64: // '@'
                if(getChildCount() > 0)
                {
                    setSelectorPressed();
                    if(mOnItemClickListener != null)
                    {
                        int index = mSelectedPosition - mFirstPosition;
                        View v = getChildAt(index);
                        mOnItemClickListener.onItemClick(this, v, mSelectedPosition, mSelectedRowId);
                    }
                }
                return true;

            default:
                int newSelection = mAdapter.getNewSelectionForKey(getSelectedItemIndex(), keyCode, event);
                if(0x80000000 != newSelection)
                {
                    setSelection(newSelection);
                    return true;
                }
                break;
            }
        return super.onKeyDown(keyCode, event);
    }

    protected void onFocusChanged(boolean gainFocus, int direction)
    {
        super.onFocusChanged(gainFocus, direction);
        requestLayout();
    }

    public void setGravity(int gravity)
    {
        if(mGravity != gravity)
        {
            mGravity = gravity;
            requestLayout();
        }
    }

    public void setHorizontalSpacing(int horizontalSpacing)
    {
        if(horizontalSpacing != mRequestedHorizontalSpacing)
        {
            mRequestedHorizontalSpacing = horizontalSpacing;
            relayoutIfNecessary();
        }
    }

    public void setVerticalSpacing(int verticalSpacing)
    {
        if(verticalSpacing != mVerticalSpacing)
        {
            mVerticalSpacing = verticalSpacing;
            relayoutIfNecessary();
        }
    }

    public void setStretchMode(int stretchMode)
    {
        if(stretchMode != mStretchMode)
        {
            mStretchMode = stretchMode;
            relayoutIfNecessary();
        }
    }

    public int getStretchMode()
    {
        return mStretchMode;
    }

    public void setColumnWidth(int columnWidth)
    {
        if(columnWidth != mRequestedColumnWidth)
        {
            mRequestedColumnWidth = columnWidth;
            relayoutIfNecessary();
        }
    }

    public void setNumColumns(int numColumns)
    {
        if(numColumns != mRequestedNumColumns)
        {
            mRequestedNumColumns = numColumns;
            relayoutIfNecessary();
        }
    }

    private void relayoutIfNecessary()
    {
        if(getChildCount() > 0)
        {
            resetList();
            requestLayout();
            invalidate();
        }
    }

    public volatile void setAdapter(Adapter x0)
    {
        setAdapter((ListAdapter)x0);
    }

    public volatile Adapter getAdapter()
    {
        return getAdapter();
    }

    private int mNumColumns;
    private int mHorizontalSpacing;
    private int mRequestedHorizontalSpacing;
    private int mVerticalSpacing;
    private int mStretchMode;
    private int mColumnWidth;
    private int mRequestedColumnWidth;
    private int mRequestedNumColumns;
    private boolean mForceToMiddle;
    private View mTallestView;
    private View mTallestInSelectedRow;
    private int mGravity;
    public static final int NO_STRETCH = 0;
    public static final int STRETCH_SPACING = 1;
    public static final int STRETCH_COLUMN_WIDTH = 2;
    public static final int AUTO_FIT = -1;
}

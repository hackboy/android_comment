// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AdapterView.java

package android.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.database.DataSetObserver;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;

//彻底搞定该类，顺便狂补viewgroup
public abstract class AdapterView extends ViewGroup
{   
    //数据观察者，怎么用呢
    protected class MyDataSetObserver extends DataSetObserver
    {

        public void onChanged()
        {
            mDataChanged = true;
            Adapter adapter = getAdapter();
            if(!adapter.stableIds() && adapter.getCount() > 0)
                setNextSelectedPositionInt(0);
            //这就是数据变化时做的事情
            checkFocus();
            invalidate();
            requestLayout();
        }

        public void onInvalidated()
        {
            mDataChanged = true;
            Adapter adapter = getAdapter();
            if(!adapter.stableIds() && adapter.getCount() > 0)
                setNextSelectedPositionInt(0);
            checkFocus();
            invalidate();
            requestLayout();
        }

        final AdapterView this$0;

        protected MyDataSetObserver()
        {   //操蛋
            this$0 = AdapterView.this;
            super();
        }
    }
    //操蛋，不懂啊
    protected class ChangeObserver extends ContentObserver
    {

        public boolean deliverSelfNotifications()
        {
            return true;
        }

        public void onChange(boolean selfChange)
        {
            mDataChanged = true;
            syncToRow(mSelectedRowId, mSelectedPosition);
            checkFocus();
            requestLayout();
            invalidate();
        }

        final AdapterView this$0;

        public ChangeObserver()
        {
            this$0 = AdapterView.this;
            super(new Handler());
        }
    }
    //选择item时的处理
    private class SelectionNotifier extends Handler
        implements Runnable
    {

        public void run()
        {
            if(mDataChanged)
                //运行该对象的run方法，在handler所在的线程执行该run方法
                post(this);
            else
                fireOnSelected();
        }

        final AdapterView this$0;

        private SelectionNotifier()
        {
            this$0 = AdapterView.this;
            super();
        }

    }

    public static interface OnItemSelectedListener
    {

        public abstract void onItemSelected(AdapterView adapterview, View view, int i, long l);

        public abstract void onNothingSelected(AdapterView adapterview);
    }

    public static interface OnItemClickListener
    {

        public abstract void onItemClick(AdapterView adapterview, View view, int i, long l);
    }


    public AdapterView(Context context)
    {
        super(context);
        mSyncRowId = 0x8000000000000000L;
        mNeedSync = false;
        mInLayout = false;
        mNextSelectedPosition = 0;
        mNextSelectedRowId = 0L;
        mSelectedPosition = 0;
        mSelectedRowId = 0L;
    }

    public AdapterView(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
        mSyncRowId = 0x8000000000000000L;
        mNeedSync = false;
        mInLayout = false;
        mNextSelectedPosition = 0;
        mNextSelectedRowId = 0L;
        mSelectedPosition = 0;
        mSelectedRowId = 0L;
    }

    public AdapterView(Context context, AttributeSet attrs, Map inflateParams, int defStyle)
    {
        super(context, attrs, inflateParams, defStyle);
        mSyncRowId = 0x8000000000000000L;
        mNeedSync = false;
        mInLayout = false;
        mNextSelectedPosition = 0;
        mNextSelectedRowId = 0L;
        mSelectedPosition = 0;
        mSelectedRowId = 0L;
    }

    public void setOnItemClickListener(OnItemClickListener l)
    {
        mOnItemClickListener = l;
    }

    public final OnItemClickListener getOnItemClickListener()
    {
        return mOnItemClickListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener l)
    {
        mOnItemSelectedListener = l;
    }

    public final OnItemSelectedListener getOnItemSelectedListener()
    {
        return mOnItemSelectedListener;
    }

    public abstract Adapter getAdapter();

    public abstract void setAdapter(Adapter adapter);

    public int getSelectedItemIndex()
    {
        if(mNextSelectedPosition != mSelectedPosition)
            return mNextSelectedPosition;
        else
            return mSelectedPosition;
    }

    public long getSelectedItemId()
    {
        if(mNextSelectedRowId != mSelectedRowId)
            return mNextSelectedRowId;
        else
            return mSelectedRowId;
    }

    public abstract View getSelectedView();

    public Object getSelectedItem()
    {
        Adapter adapter = getAdapter();
        int selection = getSelectedItemIndex();
        if(adapter != null && adapter.getCount() > 0 && selection >= 0)
            return adapter.getItem(selection);
        else
            return null;
    }

    public int getCount()
    {
        return mItemCount;
    }

    public abstract void setSelection(int i);

    public void setEmptyView(View emptyView)
    {
        mEmptyView = emptyView;
    }

    public View getEmptyView()
    {
        return mEmptyView;
    }

    protected void checkFocus()
    {
        Adapter adapter = getAdapter();
        boolean empty = adapter == null || adapter.getCount() == 0;
        setFocusable(!empty);
        if(mEmptyView != null)
            if(empty)
            {
                mEmptyView.setVisibility(0);
                setVisibility(8);
                if(mDataChanged)
                    mItemCount = 0;
            } else
            {
                mEmptyView.setVisibility(8);
                setVisibility(0);
            }
    }

    public Object obtainItem(int position)
    {
        Adapter adapter = getAdapter();
        return adapter != null && position >= 0 ? adapter.getItem(position) : null;
    }

    public long obtainItemID(int position)
    {
        Adapter adapter = getAdapter();
        return adapter != null && position >= 0 ? adapter.getItemId(position) : 0x8000000000000000L;
    }

    public void setOnClickListener(android.view.View.OnClickListener l)
    {   //一般不会对整个adapter设置click监听
        throw new RuntimeException("Don't call setOnClickListener for an AdapterView. You probably want setOnItemClickListener instead");
    }
    //这个是干啥
    protected void selectionChanged()
    {
        if(mOnItemSelectedListener != null)
            if(mInLayout)
            {
                if(mSelectionNotifier == null)
                    mSelectionNotifier = new SelectionNotifier();
                mSelectionNotifier.post(mSelectionNotifier);
            } else
            {
                fireOnSelected();
            }
    }

    private void fireOnSelected()
    {
        if(mOnItemSelectedListener == null)
            return;
        int selection = getSelectedItemIndex();
        if(selection >= 0)
        {
            View v = getSelectedView();
            mOnItemSelectedListener.onItemSelected(this, v, selection, getAdapter().getItemId(selection));
        } else
        {
            mOnItemSelectedListener.onNothingSelected(this);
        }
    }
    //处理数据的变化
    protected void handleDataChanged()
    {
        int count = getAdapter().getCount();
        mItemCount = count;
        boolean found = false;
        if(count > 0)
        {
            int newPos = -1;
            if(mNeedSync)
            {
                mNeedSync = false;
                newPos = findSyncRow();
                if(newPos >= 0)
                {
                    int selectablePos = lookForSelectablePosition(newPos, true);
                    if(selectablePos == newPos)
                    {
                        setNextSelectedPositionInt(newPos);
                        found = true;
                    }
                }
            }
            if(!found)
            {
                newPos = getSelectedItemIndex();
                if(newPos >= count)
                    newPos = count - 1;
                if(newPos < 0)
                    newPos = 0;
                int selectablePos = lookForSelectablePosition(newPos, true);
                if(selectablePos >= 0)
                {
                    setNextSelectedPositionInt(selectablePos);
                    selectionChanged();
                    found = true;
                } else
                {
                    selectablePos = lookForSelectablePosition(newPos, false);
                    if(selectablePos >= 0)
                    {
                        setNextSelectedPositionInt(selectablePos);
                        selectionChanged();
                        found = true;
                    }
                }
            }
        }
        if(!found)
        {
            mSelectedPosition = -1;
            mSelectedRowId = 0x8000000000000000L;
            mNextSelectedPosition = -1;
            mNextSelectedRowId = 0x8000000000000000L;
            mNeedSync = false;
            selectionChanged();
        }
    }

    protected int findSyncRow()
    {
        int count = getCount();
        if(count == 0)
            return -1;
        long match = mSyncRowId;
        int seed = mSyncPosition;
        if(match == 0x8000000000000000L)
            return -1;
        seed = Math.max(0, seed);
        seed = Math.min(count - 1, seed);
        long rowID = obtainItemID(seed);
        if(rowID == match)
            return seed;
        int pos = seed - 20;
        if(pos < 0)
            pos = 0;
        int end = pos + 40;
        if(end > count)
            end = count;
        for(; pos < end; pos++)
        {
            rowID = obtainItemID(pos);
            if(rowID == match)
                return pos;
        }

        if(count < 200 && (seed - 20 > 0 || end != count))
            for(pos = 0; pos < count; pos++)
            {
                rowID = obtainItemID(pos);
                if(rowID == match)
                    return pos;
            }

        return -1;
    }

    protected int lookForSelectablePosition(int position, boolean lookDown)
    {
        return position;
    }

    protected void setSelectedPositionInt(int position)
    {
        mSelectedPosition = position;
        mSelectedRowId = obtainItemID(position);
    }

    protected void setNextSelectedPositionInt(int position)
    {
        if(!mNeedSync)
        {
            mNextSelectedPosition = position;
            mNextSelectedRowId = obtainItemID(position);
        } else
        {
            mSyncPosition = position;
            mSyncRowId = obtainItemID(position);
        }
    }

    protected void syncToRow(long searchRowId, int searchPosition)
    {
        mNeedSync = true;
        mSyncRowId = searchRowId;
        mSyncPosition = searchPosition;
    }

    private SelectionNotifier mSelectionNotifier;
    protected int mSyncPosition;
    protected long mSyncRowId;
    protected boolean mNeedSync;
    protected boolean mInLayout;
    protected OnItemSelectedListener mOnItemSelectedListener;
    protected OnItemClickListener mOnItemClickListener;
    protected boolean mDataChanged;
    protected int mNextSelectedPosition;
    protected long mNextSelectedRowId;
    protected int mSelectedPosition;
    protected long mSelectedRowId;
    protected View mEmptyView;
    protected int mItemCount;

}

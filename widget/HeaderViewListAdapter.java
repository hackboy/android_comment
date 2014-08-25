// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HeaderViewListAdapter.java

package android.widget;

import android.database.ContentObserver;
import android.database.DataSetObserver;
import android.view.*;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package android.widget:
//            ListAdapter, ListView

public class HeaderViewListAdapter
    implements ListAdapter
{

    public HeaderViewListAdapter(ArrayList headerViewInfos, ArrayList footerViewInfos, ListAdapter adapter)
    {
        mAdapter = adapter;
        mHeaderViewInfos = headerViewInfos;
        mHeaderViewsLen = headerViewInfos == null ? 0 : headerViewInfos.size();
        mFooterViewInfos = footerViewInfos;
        mFooterViewsLen = footerViewInfos == null ? 0 : footerViewInfos.size();
        mAreAllFixedViewsSelectable = areAllListInfosSelectable(mHeaderViewInfos) && areAllListInfosSelectable(mFooterViewInfos);
    }

    private boolean areAllListInfosSelectable(ArrayList infos)
    {
label0:
        {
            if(infos == null)
                break label0;
            Iterator i$ = infos.iterator();
            ListView.FixedViewInfo info;
            do
            {
                if(!i$.hasNext())
                    break label0;
                info = (ListView.FixedViewInfo)i$.next();
            } while(info.isSelectable);
            return false;
        }
        return true;
    }

    public boolean removeHeader(View v)
    {
        for(int i = 0; i < mHeaderViewInfos.size(); i++)
        {
            ListView.FixedViewInfo info = (ListView.FixedViewInfo)mHeaderViewInfos.get(i);
            if(info.view == v)
            {
                mHeaderViewInfos.remove(i);
                mHeaderViewsLen--;
                mAreAllFixedViewsSelectable = areAllListInfosSelectable(mHeaderViewInfos) && areAllListInfosSelectable(mFooterViewInfos);
                return true;
            }
        }

        return false;
    }

    public boolean removeFooter(View v)
    {
        for(int i = 0; i < mFooterViewInfos.size(); i++)
        {
            ListView.FixedViewInfo info = (ListView.FixedViewInfo)mFooterViewInfos.get(i);
            if(info.view == v)
            {
                mFooterViewInfos.remove(i);
                mFooterViewsLen--;
                mAreAllFixedViewsSelectable = areAllListInfosSelectable(mHeaderViewInfos) && areAllListInfosSelectable(mFooterViewInfos);
                return true;
            }
        }

        return false;
    }

    public int getCount()
    {
        if(mAdapter != null)
            return mHeaderViewsLen + mFooterViewsLen + mAdapter.getCount();
        else
            return mHeaderViewsLen + mFooterViewsLen;
    }

    public boolean areAllItemsSelectable()
    {
        if(mAdapter != null)
            return mAreAllFixedViewsSelectable && mAdapter.areAllItemsSelectable();
        else
            return true;
    }

    public boolean isSelectable(int position)
    {
        if(mAdapter != null && position >= mHeaderViewsLen)
        {
            int adjPosition = position - mHeaderViewsLen;
            int adapterCount = mAdapter.getCount();
            if(adjPosition >= adapterCount && mFooterViewInfos != null)
                return ((ListView.FixedViewInfo)mFooterViewInfos.get(adjPosition - adapterCount)).isSelectable;
            else
                return mAdapter.isSelectable(adjPosition);
        }
        if(position < mHeaderViewsLen && mHeaderViewInfos != null)
            return ((ListView.FixedViewInfo)mHeaderViewInfos.get(position)).isSelectable;
        else
            return true;
    }

    public Object getItem(int position)
    {
        if(mAdapter != null && position >= mHeaderViewsLen)
        {
            int adjPosition = position - mHeaderViewsLen;
            int adapterCount = mAdapter.getCount();
            if(adjPosition >= adapterCount && mFooterViewInfos != null)
                return ((ListView.FixedViewInfo)mFooterViewInfos.get(adjPosition - adapterCount)).data;
            else
                return mAdapter.getItem(adjPosition);
        }
        if(position < mHeaderViewsLen && mHeaderViewInfos != null)
            return ((ListView.FixedViewInfo)mHeaderViewInfos.get(position)).data;
        else
            return null;
    }

    public long getItemId(int position)
    {
        if(mAdapter != null && position >= mHeaderViewsLen)
        {
            int adjPosition = position - mHeaderViewsLen;
            int adapterCnt = mAdapter.getCount();
            if(adjPosition < adapterCnt)
                return mAdapter.getItemId(adjPosition);
        }
        return -1L;
    }

    public boolean stableIds()
    {
        if(mAdapter != null)
            return mAdapter.stableIds();
        else
            return false;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(mAdapter != null && position >= mHeaderViewsLen)
        {
            int adjPosition = position - mHeaderViewsLen;
            int adapterCount = mAdapter.getCount();
            if(adjPosition >= adapterCount && mFooterViewInfos != null)
                return ((ListView.FixedViewInfo)mFooterViewInfos.get(adjPosition - adapterCount)).view;
            else
                return mAdapter.getView(adjPosition, convertView, parent);
        }
        if(position < mHeaderViewsLen)
            return ((ListView.FixedViewInfo)mHeaderViewInfos.get(position)).view;
        else
            return null;
    }

    public void registerContentObserver(ContentObserver observer)
    {
        if(mAdapter != null)
            mAdapter.registerContentObserver(observer);
    }

    public void unregisterContentObserver(ContentObserver observer)
    {
        if(mAdapter != null)
            mAdapter.unregisterContentObserver(observer);
    }

    public void registerDataSetObserver(DataSetObserver observer)
    {
        if(mAdapter != null)
            mAdapter.registerDataSetObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer)
    {
        if(mAdapter != null)
            mAdapter.unregisterDataSetObserver(observer);
    }

    public int getNewSelectionForKey(int currentSelection, int keyCode, KeyEvent event)
    {
        int newpos = mAdapter.getNewSelectionForKey(currentSelection - mHeaderViewsLen, keyCode, event);
        if(newpos != 0x80000000)
            newpos += mHeaderViewsLen;
        return newpos;
    }

    private ListAdapter mAdapter;
    ArrayList mHeaderViewInfos;
    int mHeaderViewsLen;
    ArrayList mFooterViewInfos;
    int mFooterViewsLen;
    boolean mAreAllFixedViewsSelectable;
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BaseGroupListAdapter.java

package android.widget;

import android.database.*;

// Referenced classes of package android.widget:
//            GroupListAdapter

public abstract class BaseGroupListAdapter
    implements GroupListAdapter
{

    public BaseGroupListAdapter()
    {
    }

    public void registerContentObserver(ContentObserver observer)
    {
        mContentObservable.registerObserver(observer);
    }

    public void unregisterContentObserver(ContentObserver observer)
    {
        mContentObservable.unregisterObserver(observer);
    }

    public void registerCursorObserver(DataSetObserver observer)
    {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterCursorObserver(DataSetObserver observer)
    {
        mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetInvalidated()
    {
        mDataSetObservable.notifyInvalidated();
    }

    protected final DataSetObservable mDataSetObservable = new DataSetObservable();
    protected final ContentObservable mContentObservable = new ContentObservable();
}

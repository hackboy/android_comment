// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BaseAdapter.java

package android.widget;

import android.database.*;
import android.view.*;

// Referenced classes of package android.widget:
//            Adapter, ListAdapter, GalleryAdapter

public abstract class BaseAdapter
    implements Adapter, ListAdapter, GalleryAdapter
{

    public BaseAdapter()
    {
    }

    public boolean stableIds()
    {
        return false;
    }

    public void registerContentObserver(ContentObserver observer)
    {
        mContentObservable.registerObserver(observer);
    }

    public void unregisterContentObserver(ContentObserver observer)
    {
        mContentObservable.unregisterObserver(observer);
    }

    public void registerDataSetObserver(DataSetObserver observer)
    {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer)
    {
        mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyChange(boolean selfChange)
    {
        mContentObservable.notifyChange(selfChange);
    }

    public void notifyDataSetChanged()
    {
        mDataSetObservable.notifyChanged();
    }

    public int getNewSelectionForKey(int currentSelection, int keyCode, KeyEvent event)
    {
        return 0x80000000;
    }

    public void notifyDataSetInvalidated()
    {
        mDataSetObservable.notifyInvalidated();
    }

    public void notifyChange()
    {
        notifyChange(false);
    }

    public boolean areAllItemsSelectable()
    {
        return true;
    }

    public boolean isSelectable(int position)
    {
        return true;
    }

    public float getScale(boolean focused, int offset)
    {
        return 1.0F;
    }

    public float getAlpha(boolean focused, int offset)
    {
        return 1.0F;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getView(position, convertView, parent);
    }

    public View getMeasurementView(ViewGroup parent)
    {
        return getView(0, null, parent);
    }

    private final DataSetObservable mDataSetObservable = new DataSetObservable();
    private final ContentObservable mContentObservable = new ContentObservable();
}

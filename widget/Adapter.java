// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Adapter.java

package android.widget;

import android.database.ContentObserver;
import android.database.DataSetObserver;
import android.view.*;

public interface Adapter
{

    public abstract void registerContentObserver(ContentObserver contentobserver);

    public abstract void unregisterContentObserver(ContentObserver contentobserver);

    public abstract void registerDataSetObserver(DataSetObserver datasetobserver);

    public abstract void unregisterDataSetObserver(DataSetObserver datasetobserver);

    public abstract int getCount();

    public abstract Object getItem(int i);

    public abstract long getItemId(int i);

    public abstract boolean stableIds();

    public abstract View getView(int i, View view, ViewGroup viewgroup);

    public abstract int getNewSelectionForKey(int i, int j, KeyEvent keyevent);

    public static final int NO_SELECTION = 0x80000000;
}

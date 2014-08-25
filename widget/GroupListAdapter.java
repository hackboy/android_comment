// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GroupListAdapter.java

package android.widget;

import android.database.ContentObserver;
import android.database.DataSetObserver;
import android.view.*;

// Referenced classes of package android.widget:
//            GroupListConnector

public interface GroupListAdapter
{

    public abstract void registerContentObserver(ContentObserver contentobserver);

    public abstract void unregisterContentObserver(ContentObserver contentobserver);

    public abstract void registerCursorObserver(DataSetObserver datasetobserver);

    public abstract void unregisterCursorObserver(DataSetObserver datasetobserver);

    public abstract int getGroupCount();

    public abstract int getChildrenCount(int i);

    public abstract Object getGroup(int i);

    public abstract Object getChild(int i, int j);

    public abstract long getGroupId(int i);

    public abstract long getChildId(int i, int j);

    public abstract boolean stableIds();

    public abstract View getGroupView(int i, boolean flag, View view, ViewGroup viewgroup);

    public abstract View getChildView(int i, int j, View view, ViewGroup viewgroup);

    public abstract GroupListConnector.GenericPosition getNewSelectionForKey(GroupListConnector.GenericPosition genericposition, int i, KeyEvent keyevent);

    public abstract boolean isChildSelectable(int i, int j);
}

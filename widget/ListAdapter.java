// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ListAdapter.java

package android.widget;


// Referenced classes of package android.widget:
//            Adapter

public interface ListAdapter
    extends Adapter
{

    public abstract boolean areAllItemsSelectable();

    public abstract boolean isSelectable(int i);
}

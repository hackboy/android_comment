// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ResourceCursorAdapter.java

package android.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.*;

// Referenced classes of package android.widget:
//            CursorAdapter

public abstract class ResourceCursorAdapter extends CursorAdapter
{

    public ResourceCursorAdapter(Context context, int layout, Cursor c)
    {
        super(c, context);
        mLayout = mDropDownLayout = layout;
        mInflate = (ViewInflate)context.getSystemService("inflate");
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return mInflate.inflate(mLayout, parent, false, null);
    }

    public View newDropDownView(Context context, Cursor cursor, ViewGroup parent)
    {
        return mInflate.inflate(mDropDownLayout, parent, false, null);
    }

    public void setDropDownViewResource(int dropDownLayout)
    {
        mDropDownLayout = dropDownLayout;
    }

    private int mLayout;
    private int mDropDownLayout;
    private ViewInflate mInflate;
}

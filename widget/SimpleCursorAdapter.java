// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SimpleCursorAdapter.java

package android.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

// Referenced classes of package android.widget:
//            ResourceCursorAdapter, TextView

public class SimpleCursorAdapter extends ResourceCursorAdapter
{

    public SimpleCursorAdapter(Context context, int layout, Cursor c, String from[], int to[])
    {
        super(context, layout, c);
        mTo = to;
        findColumns(from);
    }

    public void bindView(View view, Context context, Cursor cursor)
    {
        for(int i = 0; i < mTo.length; i++)
        {
            TextView v = (TextView)view.findViewById(mTo[i]);
            if(v == null)
                continue;
            String text = cursor.getString(mFrom[i]);
            if(text == null)
                text = "";
            setViewText(v, text);
        }

    }

    public void setViewText(TextView v, CharSequence text)
    {
        v.setText(text);
    }

    private void findColumns(String from[])
    {
        int count = from.length;
        mFrom = new int[count];
        for(int i = 0; i < count; i++)
            mFrom[i] = mCursor.getColumnIndex(from[i]);

    }

    protected int mFrom[];
    private int mTo[];
}

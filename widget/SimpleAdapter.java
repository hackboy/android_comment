// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SimpleAdapter.java

package android.widget;

import android.content.Context;
import android.view.*;
import java.util.List;
import java.util.Map;

// Referenced classes of package android.widget:
//            BaseAdapter, TextView

public class SimpleAdapter extends BaseAdapter
{

    public SimpleAdapter(Context context, List data, int resource, String from[], int to[])
    {
        mData = data;
        mResource = mDropDownResource = resource;
        mFrom = from;
        mTo = to;
        mInflate = (ViewInflate)context.getSystemService("inflate");
    }

    public int getCount()
    {
        return mData.size();
    }

    public Object getItem(int position)
    {
        return mData.get(position);
    }

    public long getItemId(int position)
    {
        return (long)position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource)
    {
        View v;
        if(convertView == null)
            v = mInflate.inflate(resource, parent, false, null);
        else
            v = convertView;
        setupView(position, v);
        return v;
    }

    public void setDropDownViewResource(int resource)
    {
        mDropDownResource = resource;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return createViewFromResource(position, convertView, parent, mDropDownResource);
    }

    private void setupView(int position, View view)
    {
        Map h = (Map)mData.get(position);
        int len = mTo.length;
        for(int i = 0; i < len; i++)
        {
            TextView v = (TextView)view.findViewById(mTo[i]);
            if(v != null)
                v.setText((String)h.get(mFrom[i]));
        }

    }

    private List mData;
    private int mResource;
    private int mDropDownResource;
    private int mTo[];
    private String mFrom[];
    private ViewInflate mInflate;
}

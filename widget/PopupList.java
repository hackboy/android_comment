// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PopupList.java

package android.widget;

import android.content.Context;
import android.test.Assert;
import android.view.*;

// Referenced classes of package android.widget:
//            LinearLayout, ArrayAdapter, ListView, ScrollIndicator, 
//            AbsoluteLayout, AdapterView, ListAdapter

public class PopupList extends LinearLayout
    implements ListView.OnListViewScrollListener
{
    private class MyArrayListAdapter extends ArrayAdapter
    {

        public boolean areAllItemsSelectable()
        {
            return false;
        }

        public boolean isSelectable(int position)
        {
            return mEnabledArray[position];
        }

        boolean mEnabledArray[];
        final PopupList this$0;

        public MyArrayListAdapter(Context context, Object objects[], boolean enabled[])
        {
            this$0 = PopupList.this;
            super(context, 0x1040001, objects);
            Assert.assertTrue(objects.length == enabled.length);
            mEnabledArray = enabled;
        }
    }


    public boolean dispatchKeyEvent(KeyEvent event)
    {
        if(event.isDown())
            switch(event.getKeyCode())
            {
            case 4: // '\004'
                mListView.getOnItemClickListener().onItemClick(mListView, null, -1, 0L);
                return true;
            }
        super.dispatchKeyEvent(event);
        return true;
    }

    public PopupList(Context context, CharSequence data[], AdapterView.OnItemClickListener listener)
    {
        super(context);
        init(context, new ArrayAdapter(context, 0x1040001, data), listener);
    }

    public PopupList(Context context, CharSequence data[], boolean enabled[], AdapterView.OnItemClickListener listener)
    {
        super(context);
        if(data.length == enabled.length)
            init(context, new MyArrayListAdapter(context, data, enabled), listener);
        else
            init(context, new ArrayAdapter(context, 0x1040001, data), listener);
    }

    public PopupList(Context context, ListAdapter adapter, AdapterView.OnItemClickListener listener)
    {
        super(context);
        init(context, adapter, listener);
    }

    private void init(Context context, ListAdapter adapter, AdapterView.OnItemClickListener listener)
    {
        ViewInflate factory = (ViewInflate)context.getSystemService("inflate");
        factory.inflate(0x1040012, this, null);
        mListView = (ListView)findViewById(0x1050005);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(listener);
        mScrollIndicator = (ScrollIndicator)findViewById(0x1050063);
        mListView.setOnListViewScrollListener(this);
    }

    public void onListViewScroll(int firstCell, int cellCount, int itemCount)
    {
        mScrollIndicator.setOffset(firstCell);
        mScrollIndicator.setExtent(cellCount);
        mScrollIndicator.setRange(itemCount);
    }

    public void setSelection(int position)
    {
        mListView.setSelection(position);
    }

    public void requestFocus(int direction)
    {
        mListView.requestFocus(direction);
    }

    public static void install(View popup, ViewGroup host)
    {
        int left = host.getScrollX();
        int top = host.getScrollY();
        int width = host.getWidth();
        int height = host.getHeight();
        int popupWidth = (width * 2) / 3;
        int popupHeight = (height * 2) / 3;
        int x = left + (width - popupWidth) / 2;
        int y = top + (height - popupHeight) / 2;
        host.addView(popup, new AbsoluteLayout.LayoutParams(popupWidth, popupHeight, x, y));
        popup.requestFocus();
    }

    private ListView mListView;
    private ScrollIndicator mScrollIndicator;
}

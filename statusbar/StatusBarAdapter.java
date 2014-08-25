// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StatusBarAdapter.java

package android.server.status;

import android.content.Context;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;

// Referenced classes of package android.server.status:
//            StatusBarManager, StatusBarData

public class StatusBarAdapter extends BaseAdapter
{
    private class ChangeObserver
        implements StatusBarManager.ChangeObserver
    {

        public void onUpdate(String tag)
        {
            notifyChange(false);
        }

        public void onDelete(String tag)
        {
            notifyChange(false);
        }

        public void onInsert(String tag)
        {
            notifyChange(false);
        }

        final StatusBarAdapter this$0;

        private ChangeObserver()
        {
            this$0 = StatusBarAdapter.this;
            super();
        }

    }


    public StatusBarAdapter(Context c, boolean reverse)
    {
        mContext = c;
        mReverse = reverse;
        mObserver = new ChangeObserver();
        mStatus = StatusBarManager.getInstance().getNotificationList();
        mStatus.registerChangeObserver(mObserver);
    }

    public int getCount()
    {
        return mStatus.getCount();
    }

    private StatusBarData getNth(int position)
    {
        if(mReverse)
            return mStatus.getItem(mStatus.getCount() - position - 1);
        else
            return mStatus.getItem(position);
    }

    public int getFirstInterestingItemPosition()
    {
        int position = 0;
        if(position >= 0)
            if(!mReverse);
        return position;
    }

    public Object getItem(int position)
    {
        return getNth(position);
    }

    public long getItemId(int position)
    {
        return getNth(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        StatusBarData data = getNth(position);
        View result = null;
        switch(data.getType())
        {
        default:
            break;

        case 1: // '\001'
        {
            TextView t = new TextView(mContext);
            android.widget.Gallery.LayoutParams lp = new android.widget.Gallery.LayoutParams(-2, 21);
            t.setLayoutParams(lp);
            t.setText(data.getText());
            t.setTextSize(12F);
            t.setTextColor(-1);
            t.setTypeface(Typeface.DEFAULT_BOLD);
            t.setGravity(16);
            result = t;
            break;
        }

        case 2: // '\002'
        case 3: // '\003'
        {
            ImageView i = new ImageView(mContext);
            i.setImageDrawable(data.getIcon(mContext));
            if(data.getType() == 3)
                i.setImageLevel(data.getIconLevel());
            i.setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
            android.widget.Gallery.LayoutParams lp = new android.widget.Gallery.LayoutParams(28, 21);
            i.setLayoutParams(lp);
            i.setScaleType(android.widget.ImageView.ScaleType.CENTER);
            result = i;
            break;
        }
        }
        return result;
    }

    public final int getNewSelectionForKey(int currentSelection, int key, KeyEvent event)
    {
        return 0x80000000;
    }

    private Context mContext;
    private StatusBarManager.StatusBarList mStatus;
    private boolean mReverse;
    private ChangeObserver mObserver;
    static final String LOG_TAG = "statusbaradapter";
}

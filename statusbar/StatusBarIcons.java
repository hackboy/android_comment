// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StatusBarIcons.java

package android.server.status;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package android.server.status:
//            StatusBarData, StatusBarManager
//statusbaricons集成于linearlayout
public class StatusBarIcons extends LinearLayout
{
    private class ChangeObserver
        implements StatusBarManager.ChangeObserver
    {

        public void onUpdate(String tag)
        {
            updateView(tag);
        }

        public void onDelete(String tag)
        {
            removeView(tag);
        }

        public void onInsert(String tag)
        {
            rebuildViews();
        }

        final StatusBarIcons this$0;

        public ChangeObserver()
        {
            this$0 = StatusBarIcons.this;
            super();
        }
    }


    public StatusBarIcons(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
        mRecycle = new HashMap();
        mChangeObserver = new ChangeObserver();
    }

    public void bind(StatusBarManager.StatusBarList source, boolean addToFront)
    {
        source.registerChangeObserver(mChangeObserver);
        mSource = source;
        mAddToFront = addToFront;
        rebuildViews();
    }

    private void rebuildViews()
    {
        int childCount = getChildCount();
        for(int i = 0; i < childCount; i++)
        {
            View v = getChildAt(i);
            mRecycle.put(Integer.valueOf(v.getId()), v);
        }

        removeAllViews();
        int count = mSource.getCount();
        for(int i = 0; i < count; i++)
        {
            StatusBarData data = mSource.getItem(i);
            makeAndAddView(data);
        }

        mRecycle.clear();
    }

    private void removeView(String tag)
    {
        View v = findViewWithTag(tag);
        if(v != null)
            removeView(v);
    }

    private void updateView(String tag)
    {
        StatusBarData data = mSource.getItem(tag);
        if(data != null)
        {
            View v = findViewWithTag(tag);
            if(v != null)
                switch(data.getType())
                {
                case 1: // '\001'
                    updateTextView(data, (TextView)v);
                    break;

                case 2: // '\002'
                case 3: // '\003'
                    updateImageView(data, (ImageView)v);
                    break;
                }
        }
    }

    private void makeAndAddView(StatusBarData data)
    {
        String tag = data.getTag();
        View v = (View)mRecycle.get(tag);
        switch(data.getType())
        {
        case 1: // '\001'
            if(v == null)
            {
                TextView t = new TextView(mContext);
                android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(-2, 19);
                t.setTextSize(12F);
                t.setTextColor(-1);
                t.setTypeface(Typeface.DEFAULT_BOLD);
                t.setGravity(16);
                t.setPadding(6, 0, 0, 0);
                t.setLayoutParams(lp);
                t.setTag(tag);
                updateTextView(data, t);
                v = t;
            }
            break;

        case 2: // '\002'
        case 3: // '\003'
            if(v == null)
            {
                ImageView i = new ImageView(mContext);
                android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(-2, -2);
                lp.setMargins(8, 0, 0, 0);
                i.setLayoutParams(lp);
                i.setTag(tag);
                updateImageView(data, i);
                v = i;
            }
            break;
        }
        if(mAddToFront)
            addView(v, 0, v.getLayoutParams());
        else
            addView(v, v.getLayoutParams());
    }

    private void updateImageView(StatusBarData data, ImageView i)
    {
        i.setImageDrawable(data.getIcon(mContext));
        if(data.getType() == 3)
            i.setImageLevel(data.getIconLevel());
    }

    private void updateTextView(StatusBarData data, TextView t)
    {
        t.setText(data.getText());
    }

    static final String LOG_TAG = "statusbar";
    private static final int ICON_GAP = 8;
    private boolean mAddToFront;
    private StatusBarManager.StatusBarList mSource;
    HashMap mRecycle;
    private ChangeObserver mChangeObserver;



}

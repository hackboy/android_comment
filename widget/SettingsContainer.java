// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SettingsContainer.java

package android.widget;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

// Referenced classes of package android.widget:
//            BaseAdapter, WidgetInflate, Setting, LinearLayout, 
//            TextView

public class SettingsContainer extends BaseAdapter
{

    public SettingsContainer(Context context, Handler uiHandler)
    {
        mContext = context;
        mUiHandler = uiHandler;
        mSettings = new ArrayList();
        mInflater = new WidgetInflate(context);
    }

    public Setting getSetting(int position)
    {
        return (Setting)mSettings.get(position);
    }

    public int getCount()
    {
        return mSettings.size();
    }

    public Context getContext()
    {
        return mContext;
    }

    public Handler getUiHandler()
    {
        return mUiHandler;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Setting setting = (Setting)mSettings.get(position);
        LinearLayout settingView = (LinearLayout)mInflater.inflate(0x104001d, null, null);
        TextView settingTextView = (TextView)settingView.findViewById(0x105006a);
        settingTextView.setText(setting.getName());
        LinearLayout valueLayout = (LinearLayout)settingView.findViewById(0x105006b);
        View valueView = setting.getValueView(true);
        if(valueView != null)
            valueLayout.addView(valueView, valueLayout.getLayoutParams());
        return settingView;
    }

    public void addSetting(Setting setting)
    {
        mSettings.add(setting);
        notifyDataSetInvalidated();
    }

    public boolean areAllItemsSelectable()
    {
        return false;
    }

    public boolean isSelectable(int position)
    {
        if(position >= getCount())
            return true;
        else
            return ((Setting)mSettings.get(position)).isSeparator();
    }

    public Object getItem(int position)
    {
        return mSettings.get(position);
    }

    public long getItemId(int position)
    {
        return (long)position;
    }

    protected Context mContext;
    protected Handler mUiHandler;
    protected WidgetInflate mInflater;
    protected ArrayList mSettings;
}

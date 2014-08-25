// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TextSetting.java

package android.widget;

import android.view.View;

// Referenced classes of package android.widget:
//            BaseSetting, TextView, WidgetInflate, SettingsContainer

public class TextSetting extends BaseSetting
{

    public TextSetting(SettingsContainer container)
    {
        super(container);
        mValue = "";
    }

    public void setValue(String value)
    {
        if(value != null)
            mValue = value;
        else
            mValue = "";
        TextView valueTextView = (TextView)getValueView(false);
        valueTextView.setText(value);
    }

    public final View createValueView()
    {
        WidgetInflate inflater = new WidgetInflate(mContainer.getContext());
        TextView settingValueTextView = (TextView)inflater.inflate(0x104001e, null, null);
        settingValueTextView.setText(mValue);
        return settingValueTextView;
    }

    private String mValue;
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BaseSetting.java

package android.widget;

import android.content.Intent;
import android.view.View;

// Referenced classes of package android.widget:
//            Setting, SettingsContainer

public class BaseSetting
    implements Setting
{

    public BaseSetting(SettingsContainer container)
    {
        mContainer = container;
        mName = "";
        mSeparator = false;
        mShownInSummary = true;
    }

    public Intent getIntent()
    {
        return mIntent;
    }

    public void setIntent(Intent intent)
    {
        mIntent = intent;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        if(name != null)
            mName = name;
        else
            mName = "";
    }

    protected View createValueView()
    {
        return null;
    }

    public final View getValueView(boolean forceRecreate)
    {
        if(mValueView == null || forceRecreate)
            mValueView = createValueView();
        return mValueView;
    }

    public boolean isSeparator()
    {
        return mSeparator;
    }

    public void setSeparator(boolean separator)
    {
        mSeparator = separator;
    }

    public boolean isShownInSummary()
    {
        return mShownInSummary;
    }

    public void setShownInSummary(boolean shownInSummary)
    {
        mShownInSummary = shownInSummary;
    }

    public void onContainerCollapse(SettingsContainer container)
    {
        mValueView = null;
    }

    public void onContainerExpand(SettingsContainer settingscontainer)
    {
    }

    protected SettingsContainer mContainer;
    protected String mName;
    protected Intent mIntent;
    private View mValueView;
    protected boolean mSeparator;
    protected boolean mShownInSummary;
}

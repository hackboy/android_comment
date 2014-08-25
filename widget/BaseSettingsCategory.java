// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BaseSettingsCategory.java

package android.widget;

import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package android.widget:
//            SettingsContainer, Setting, SettingsCategory, SettingsGroupListModel

public class BaseSettingsCategory extends SettingsContainer
    implements SettingsCategory
{

    public BaseSettingsCategory(SettingsGroupListModel model)
    {
        super(model.getContext(), model.getHandler());
        mModel = model;
        mHasExpanded = false;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        mName = name;
    }

    public String getDescription()
    {
        if(mDescription == null || mDescription.length() == 0)
        {
            StringBuffer desc = new StringBuffer();
            int numSettingsMinusOne = mSettings.size() - 1;
            int i = 0;
            for(i = 0; i < numSettingsMinusOne; i++)
            {
                Setting setting = (Setting)mSettings.get(i);
                if(setting.isShownInSummary() && !setting.isSeparator())
                    desc.append(setting.getName()).append(", ");
            }

            desc.append(((Setting)mSettings.get(i)).getName());
            mDescription = desc.toString();
        }
        return mDescription;
    }

    public void onCollapse()
    {
        Setting setting;
        for(Iterator i$ = mSettings.iterator(); i$.hasNext(); setting.onContainerCollapse(this))
            setting = (Setting)i$.next();

    }

    public void onExpand()
    {
        if(!mHasExpanded)
        {
            mHasExpanded = true;
            onInitialExpand();
        }
        Setting setting;
        for(Iterator i$ = mSettings.iterator(); i$.hasNext(); setting.onContainerExpand(this))
            setting = (Setting)i$.next();

    }

    public void onInitialExpand()
    {
    }

    protected SettingsGroupListModel mModel;
    private String mName;
    private String mDescription;
    private boolean mHasExpanded;
}

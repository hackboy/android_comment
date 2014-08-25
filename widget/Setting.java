// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Setting.java

package android.widget;

import android.content.Intent;
import android.view.View;

// Referenced classes of package android.widget:
//            SettingsContainer

public interface Setting
{

    public abstract String getName();

    public abstract View getValueView(boolean flag);

    public abstract Intent getIntent();

    public abstract boolean isSeparator();

    public abstract boolean isShownInSummary();

    public abstract void onContainerCollapse(SettingsContainer settingscontainer);

    public abstract void onContainerExpand(SettingsContainer settingscontainer);
}

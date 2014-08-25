// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SettingsCategory.java

package android.widget;


// Referenced classes of package android.widget:
//            Adapter, Setting

public interface SettingsCategory
    extends Adapter
{

    public abstract String getName();

    public abstract String getDescription();

    public abstract Setting getSetting(int i);

    public abstract void onExpand();

    public abstract void onCollapse();
}

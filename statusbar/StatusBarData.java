// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StatusBarData.java

package android.server.status;

import android.content.*;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.RemoteViews;

public final class StatusBarData
{

    public StatusBarData(String t, int statusType, ComponentName className)
    {
        mTag = t;
        mType = statusType;
        mVisible = false;
        mIconId = -1;
        mText = null;
        mId = sLastId;
        if(className != null)
            setActivity(className);
        sLastId++;
    }

    public final void setActivity(ComponentName className)
    {
        mIntent = new Intent("android.intent.action.MAIN");
        mIntent.setComponent(className);
    }

    public Drawable getIcon(Context context)
    {
        Resources r;
        r = null;
        if(mIconPackage != null)
            try
            {
                r = context.getPackageManager().getResourcesForApplication(mIconPackage);
            }
            catch(android.content.PackageManager.NameNotFoundException ex)
            {
                Log.e("StatusBarMgr", (new StringBuilder()).append("Icon package not found: ").append(mIconPackage).toString(), ex);
                return null;
            }
        else
            r = context.getResources();
        return r.getDrawable(mIconId);
        Throwable e;
        e;
        Log.e("StatusBarMgr", (new StringBuilder()).append("Icon not found in ").append(mIconPackage == null ? "<system>" : mIconPackage).append(": ").append(Integer.toHexString(mIconId)).toString(), e);
        return null;
    }

    public int getIconId()
    {
        return mIconId;
    }

    public void setIconId(int iconId)
    {
        mIconId = iconId;
    }

    public String getIconPackage()
    {
        return mIconPackage;
    }

    public void setIconPackage(String iconPackage)
    {
        mIconPackage = iconPackage;
    }

    public int getIconLevel()
    {
        return mIconLevel;
    }

    public void setIconLevel(int iconLevel)
    {
        mIconLevel = iconLevel;
    }

    public Intent getIntent()
    {
        return mIntent;
    }

    public void setIntent(Intent intent)
    {
        mIntent = intent;
    }

    public CharSequence getTickerText()
    {
        return mTickerText;
    }

    public void setTickerText(CharSequence tickerText)
    {
        mTickerText = tickerText;
    }

    public String getTag()
    {
        return mTag;
    }

    public long getId()
    {
        return mId;
    }

    public void setTag(String tag)
    {
        mTag = tag;
    }

    public CharSequence getText()
    {
        return mText;
    }

    public void setText(CharSequence text)
    {
        mText = text;
    }

    public CharSequence getBalloonText()
    {
        return mBalloonText;
    }

    public void setBalloonText(CharSequence balloonText)
    {
        mBalloonText = balloonText;
    }

    public RemoteViews getBalloon()
    {
        return mBalloon;
    }

    public void setBalloon(RemoteViews balloon)
    {
        mBalloon = balloon;
    }

    public int getType()
    {
        return mType;
    }

    public void setType(int type)
    {
        mType = type;
    }

    public boolean isVisible()
    {
        return mVisible;
    }

    public void setVisible(boolean visible)
    {
        mVisible = visible;
    }

    static final String LOG_TAG = "StatusBarMgr";
    public static final int TEXT = 1;
    public static final int ICON = 2;
    public static final int LEVEL_ICON = 3;
    private String mTag;
    private int mType;
    private CharSequence mBalloonText;
    private RemoteViews mBalloon;
    private Intent mIntent;
    private CharSequence mTickerText;
    private String mIconPackage;
    private int mIconId;
    private int mIconLevel;
    private CharSequence mText;
    private boolean mVisible;
    private long mId;
    private static long sLastId = 0L;

}

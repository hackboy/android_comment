// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DigitalClock.java

package android.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.DateFormat;
import java.util.Calendar;
import java.util.Map;

// Referenced classes of package android.widget:
//            TextView

public class DigitalClock extends TextView
{
    private class FormatChangeObserver extends ContentObserver
    {

        public void onChange(boolean selfChange)
        {
            setFormat();
        }

        final DigitalClock this$0;

        public FormatChangeObserver()
        {
            this$0 = DigitalClock.this;
            super(new Handler());
        }
    }


    public DigitalClock(Context context)
    {
        super(context);
        mTickerStopped = false;
        initClock(context);
    }

    public DigitalClock(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
        mTickerStopped = false;
        initClock(context);
    }

    private void initClock(Context context)
    {
        android.content.Resources r = mContext.getResources();
        if(mCalendar == null)
            mCalendar = Calendar.getInstance();
        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mFormatChangeObserver);
        setFormat();
    }
    //为什么此时进行初始化？好处何在
    protected void onAttachedToWindow()
    {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();
        mTicker = new Runnable() {

            public void run()
            {
                if(mTickerStopped)
                {
                    return;
                } else
                {
                    mCalendar.setTimeInMillis(java.lang.System.currentTimeMillis());
                    setText(DateFormat.format(mFormat, mCalendar));
                    invalidate();
                    //计算各种时间啊
                    long now = SystemClock.uptimeMillis();
                    //这是多久？now%1000得到的是系统启动到现在的秒数，为什么用1000去减呢，正好凑成一个整数秒？
                    long next = now + (1000L - now % 1000L);
                    mHandler.postAtTime(mTicker, next);
                    return;
                }
            }

            final DigitalClock this$0;

            
            {
                this$0 = DigitalClock.this;
                super();
            }
        };
        //还能这样用？
        mTicker.run();
    }

    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }

    private boolean get24HourMode()
    {
        String value = android.provider.Settings.System.getString(getContext().getContentResolver(), "time_12_24");
        return value != null && !value.equals("12");
    }

    private void setFormat()
    {
        if(get24HourMode())
            mFormat = "k:mm:ss";
        else
            mFormat = "h:mm:ss aa";
    }

    Calendar mCalendar;
    private static final String m12 = "h:mm:ss aa";
    private static final String m24 = "k:mm:ss";
    private FormatChangeObserver mFormatChangeObserver;
    private Runnable mTicker;
    private Handler mHandler;
    private boolean mTickerStopped;
    String mFormat;




}

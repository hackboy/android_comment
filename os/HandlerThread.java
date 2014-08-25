// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HandlerThread.java

package android.os;

import android.util.Log;

// Referenced classes of package android.os:
//            HandlerHelper, Looper, HandlerInterface, Handler

public class HandlerThread extends Thread
{

    public HandlerThread(HandlerInterface h, Runnable setup, String name)
    {
        super(name);
        mMonitor = new Object();
        mhi = h;
        mSetup = setup;
        synchronized(mMonitor)
        {
            start();
            while(mh == null && mtr == null) 
                try
                {
                    mMonitor.wait();
                }
                catch(InterruptedException ex) { }
        }
        mMonitor = null;
        if(mtr != null)
            throw new RuntimeException("exception while starting", mtr);
        else
            return;
    }

    public void run()
    {
        try
        {
            synchronized(mMonitor)
            {
                try
                {
                    Looper.prepare();
                    mh = new HandlerHelper(mhi);
                    if(mSetup != null)
                    {
                        mSetup.run();
                        mSetup = null;
                    }
                }
                catch(Throwable tr)
                {
                    mtr = tr;
                }
                mMonitor.notify();
            }
            if(mtr == null)
                Looper.loop();
        }
        catch(Throwable tr)
        {
            Log.e("system", "Uncaught Exception", tr);
        }
    }

    public Handler getHandler()
    {
        return mh;
    }

    Runnable mSetup;
    HandlerInterface mhi;
    Handler mh;
    Throwable mtr;
    Object mMonitor;
}

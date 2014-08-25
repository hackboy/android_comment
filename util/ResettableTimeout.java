// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ResettableTimeout.java

package android.util;

import android.os.ConditionVariable;
import android.os.SystemClock;

public abstract class ResettableTimeout
{
    private class T extends Thread
    {

        public void run()
        {
            mLock.open();
_L3:
            long diff;
label0:
            {
                synchronized(this)
                {
                    diff = mOffAt - SystemClock.uptimeMillis();
                    if(diff > 0L)
                        break label0;
                    mOffCalled = true;
                    off();
                    mThread = null;
                }
                break; /* Loop/switch isn't completed */
            }
            t;
            JVM INSTR monitorexit ;
              goto _L1
            exception;
            throw exception;
_L1:
            try
            {
                sleep(diff);
            }
            catch(InterruptedException e) { }
            if(true) goto _L3; else goto _L2
_L2:
        }

        final ResettableTimeout this$0;

        private T()
        {
            this$0 = ResettableTimeout.this;
            super();
        }

    }


    public ResettableTimeout()
    {
        mLock = new ConditionVariable();
    }

    public abstract void on(boolean flag);

    public abstract void off();

    public void go(long milliseconds)
    {
        synchronized(this)
        {
            mOffAt = SystemClock.uptimeMillis() + milliseconds;
            boolean alreadyOn;
            if(mThread == null)
            {
                alreadyOn = false;
                mLock.close();
                mThread = new T();
                mThread.start();
                mLock.block();
                mOffCalled = false;
            } else
            {
                alreadyOn = true;
                mThread.interrupt();
            }
            on(alreadyOn);
        }
    }

    public void cancel()
    {
        synchronized(this)
        {
            mOffAt = 0L;
            if(mThread != null)
            {
                mThread.interrupt();
                mThread = null;
            }
            if(!mOffCalled)
            {
                mOffCalled = true;
                off();
            }
        }
    }

    private ConditionVariable mLock;
    private volatile long mOffAt;
    private volatile boolean mOffCalled;
    private Thread mThread;




}

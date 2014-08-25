// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TokenWatcher.java

package android.os;

import android.util.Log;
import java.util.*;

// Referenced classes of package android.os:
//            IBinder, Handler

public abstract class TokenWatcher
{
    private class Death
        implements IBinder.DeathRecipient
    {

        public void binderDied()
        {
            cleanup(token, false);
        }

        protected void finalize()
            throws Throwable
        {
            if(token != null)
            {
                Log.w(mTag, (new StringBuilder()).append("cleaning up leaked reference: ").append(tag).toString());
                release(token);
            }
            super.finalize();
            break MISSING_BLOCK_LABEL_65;
            Exception exception;
            exception;
            super.finalize();
            throw exception;
        }

        IBinder token;
        String tag;
        final TokenWatcher this$0;

        Death(IBinder token, String tag)
        {
            this$0 = TokenWatcher.this;
            super();
            this.token = token;
            this.tag = tag;
        }
    }


    public TokenWatcher(Handler h, String tag)
    {
        mNotificationTask = new Runnable() {

            public void run()
            {
                int value;
                synchronized(mTokens)
                {
                    value = mNotificationQueue;
                    mNotificationQueue = -1;
                }
                if(value == 1)
                    acquired();
                else
                if(value == 0)
                    released();
            }

            final TokenWatcher this$0;

            
            {
                this$0 = TokenWatcher.this;
                super();
            }
        };
        mTokens = new WeakHashMap();
        mNotificationQueue = -1;
        mAcquired = false;
        mHandler = h;
        mTag = tag == null ? "TokenWatcher" : tag;
    }

    public abstract void acquired();

    public abstract void released();

    public void acquire(IBinder token, String tag)
    {
        synchronized(mTokens)
        {
            int oldSize = mTokens.size();
            Death d = new Death(token, tag);
            token.linkToDeath(d, 0);
            mTokens.put(token, d);
            if(oldSize == 0 && !mAcquired)
            {
                sendNotificationLocked(true);
                mAcquired = true;
            }
        }
    }

    public void cleanup(IBinder token, boolean unlink)
    {
        synchronized(mTokens)
        {
            Death d = (Death)mTokens.remove(token);
            if(unlink && d != null)
            {
                d.token.unlinkToDeath(d, 0);
                d.token = null;
            }
            if(mTokens.size() == 0 && mAcquired)
            {
                sendNotificationLocked(false);
                mAcquired = false;
            }
        }
    }

    public void release(IBinder token)
    {
        cleanup(token, true);
    }

    public boolean isAcquired()
    {
        WeakHashMap weakhashmap = mTokens;
        JVM INSTR monitorenter ;
        return mAcquired;
        Exception exception;
        exception;
        throw exception;
    }

    public void dump()
    {
        synchronized(mTokens)
        {
            Set keys = mTokens.keySet();
            Log.i(mTag, (new StringBuilder()).append("Token count: ").append(mTokens.size()).toString());
            int i = 0;
            for(Iterator i$ = keys.iterator(); i$.hasNext();)
            {
                IBinder b = (IBinder)i$.next();
                Log.i(mTag, (new StringBuilder()).append("[").append(i).append("] ").append(((Death)mTokens.get(b)).tag).append(" - ").append(b).toString());
                i++;
            }

        }
    }

    private void sendNotificationLocked(boolean on)
    {
        int value = on ? 1 : 0;
        if(mNotificationQueue == -1)
        {
            mNotificationQueue = value;
            mHandler.post(mNotificationTask);
        } else
        if(mNotificationQueue != value)
        {
            mNotificationQueue = -1;
            mHandler.removeCallbacks(mNotificationTask);
        }
    }

    private Runnable mNotificationTask;
    private WeakHashMap mTokens;
    private Handler mHandler;
    private String mTag;
    private int mNotificationQueue;
    private volatile boolean mAcquired;




}

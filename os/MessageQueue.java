// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MessageQueue.java

package android.os;

import android.util.Log;
import java.util.ArrayList;

// Referenced classes of package android.os:
//            SystemClock, RuntimeInit, Message, Handler

public class MessageQueue
{
    public static interface IdleHandler
    {

        public abstract boolean queueIdle();
    }


    public final void addIdleHandler(IdleHandler handler)
    {
        if(handler == null)
            throw new NullPointerException("Can't add a null IdleHandler");
        synchronized(this)
        {
            mIdleHandlers.add(handler);
        }
    }

    public final void removeIdleHandler(IdleHandler handler)
    {
        synchronized(this)
        {
            mIdleHandlers.remove(handler);
        }
    }

    MessageQueue()
    {
        mQuiting = false;
    }

    final Message next()
    {
        boolean tryIdle = true;
_L3:
        Object idlers[] = null;
        MessageQueue messagequeue = this;
        JVM INSTR monitorenter ;
        long now;
        Message msg;
        now = SystemClock.uptimeMillis();
        msg = pullNextLocked(now);
        if(msg != null)
            return msg;
        if(tryIdle && mIdleHandlers.size() > 0)
            idlers = mIdleHandlers.toArray();
        messagequeue;
        JVM INSTR monitorexit ;
          goto _L1
        Exception exception;
        exception;
        throw exception;
_L1:
        boolean didIdle = false;
        if(idlers != null)
        {
            Object arr$[] = idlers;
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                Object idler = arr$[i$];
                boolean keep = false;
                try
                {
                    didIdle = true;
                    keep = ((IdleHandler)idler).queueIdle();
                }
                catch(Throwable e)
                {
                    Log.e("MessageQueue", "IdleHandler threw exception", e);
                    RuntimeInit.crash("MessageQueue", e);
                }
                if(keep)
                    continue;
                synchronized(this)
                {
                    mIdleHandlers.remove(idler);
                }
            }

        }
        if(didIdle)
            tryIdle = false;
        else
            synchronized(this)
            {
                try
                {
                    if(mMessages != null)
                    {
                        if(mMessages.when - now > 0L)
                            wait(mMessages.when - now);
                    } else
                    {
                        wait();
                    }
                }
                catch(InterruptedException e) { }
            }
        if(true) goto _L3; else goto _L2
_L2:
    }

    final Message pullNextLocked(long now)
    {
        Message msg = mMessages;
        if(msg != null && now >= msg.when)
        {
            mMessages = msg.next;
            return msg;
        } else
        {
            return null;
        }
    }

    final boolean enqueueMessage(Message msg, long when)
    {
        if(msg.when != 0L)
        {
            Log.e("MessageQueue", (new StringBuilder()).append(msg).append(" This message has been used.  You must not recycle them.").toString());
            return false;
        }
        MessageQueue messagequeue = this;
        JVM INSTR monitorenter ;
        if(!mQuiting)
            break MISSING_BLOCK_LABEL_97;
        RuntimeException e = new RuntimeException((new StringBuilder()).append(msg.target).append(" sending message to a Handler on a dead thread").toString());
        Log.w("MessageQueue", e.getMessage(), e);
        return false;
        if(msg.target == null)
            mQuiting = true;
        msg.when = when;
        Message p = mMessages;
        if(p == null || msg.when < p.when)
        {
            msg.next = mMessages;
            mMessages = msg;
            notify();
        } else
        {
            Message prev = null;
            for(; p != null && p.when <= msg.when; p = p.next)
                prev = p;

            msg.next = prev.next;
            prev.next = msg;
            notify();
        }
        messagequeue;
        JVM INSTR monitorexit ;
          goto _L1
        Exception exception;
        exception;
        throw exception;
_L1:
        return true;
    }

    final void removeMessages(Handler h, int what, Object object)
    {
        synchronized(this)
        {
            Message p;
            Message n;
            for(p = mMessages; p != null && p.target == h && p.what == what && (object == null || p.obj == object); p = n)
            {
                n = p.next;
                mMessages = n;
                p.recycle();
            }

            while(p != null) 
            {
                Message n = p.next;
                if(n != null && n.target == h && n.what == what && (object == null || n.obj == object))
                {
                    Message nn = n.next;
                    n.recycle();
                    p.next = nn;
                } else
                {
                    p = n;
                }
            }
        }
    }

    final void removeMessages(Handler h, Runnable r, Object object)
    {
        synchronized(this)
        {
            Message p;
            Message n;
            for(p = mMessages; p != null && p.target == h && p.callback == r && (object == null || p.obj == object); p = n)
            {
                n = p.next;
                mMessages = n;
                p.recycle();
            }

            while(p != null) 
            {
                Message n = p.next;
                if(n != null && n.target == h && n.callback == r && (object == null || n.obj == object))
                {
                    Message nn = n.next;
                    n.recycle();
                    p.next = nn;
                } else
                {
                    p = n;
                }
            }
        }
    }

    final void removeCallbacksAndMessages(Handler h, Object object)
    {
        synchronized(this)
        {
            Message p;
            Message n;
            for(p = mMessages; p != null && p.target == h && (object == null || p.obj == object); p = n)
            {
                n = p.next;
                mMessages = n;
                p.recycle();
            }

            while(p != null) 
            {
                Message n = p.next;
                if(n != null && n.target == h && (object == null || n.obj == object))
                {
                    Message nn = n.next;
                    n.recycle();
                    p.next = nn;
                } else
                {
                    p = n;
                }
            }
        }
    }

    void poke()
    {
        synchronized(this)
        {
            notify();
        }
    }

    Message mMessages;
    private final ArrayList mIdleHandlers = new ArrayList();
    private boolean mQuiting;
}

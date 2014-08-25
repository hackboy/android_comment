// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Handler.java

package android.os;

import android.util.Log;

// Referenced classes of package android.os:
//            HandlerInterface, Message, Looper, MessageQueue, 
//            SystemClock

public class Handler
    implements HandlerInterface
{

    public void handleMessage(Message message)
    {
    }

    public void dispatchMessage(Message msg)
    {
        if(msg.callback != null)
            handleCallback(msg);
        else
            handleMessage(msg);
    }

    public Handler()
    {
        mLooper = Looper.myLooper();
        if(mLooper == null)
        {
            throw new RuntimeException("Can't create handler inside thread that has not called Looper.prepare()");
        } else
        {
            mQueue = mLooper.mQueue;
            return;
        }
    }

    public Handler(Looper looper)
    {
        mLooper = looper;
        mQueue = looper.mQueue;
    }

    public final Message obtainMessage()
    {
        return Message.obtain(this);
    }

    public final Message obtainMessage(int what)
    {
        return Message.obtain(this, what);
    }

    public final Message obtainMessage(int what, Object obj)
    {
        return Message.obtain(this, what, obj);
    }

    public final Message obtainMessage(int what, int arg1, int arg2)
    {
        return Message.obtain(this, what, arg1, arg2);
    }

    public final Message obtainMessage(int what, int arg1, int arg2, Object obj)
    {
        return Message.obtain(this, what, arg1, arg2, obj);
    }

    public final boolean post(Runnable r)
    {
        return sendMessageDelayed(getPostMessage(r), 0L);
    }

    public final boolean postAtTime(Runnable r, long uptimeMillis)
    {
        return sendMessageAtTime(getPostMessage(r), uptimeMillis);
    }

    public final boolean postAtTime(Runnable r, Object token, long uptimeMillis)
    {
        return sendMessageAtTime(getPostMessage(r, token), uptimeMillis);
    }

    public final boolean postDelayed(Runnable r, long delayMillis)
    {
        return sendMessageDelayed(getPostMessage(r), delayMillis);
    }

    public final boolean postAtFrontOfQueue(Runnable r)
    {
        return sendMessageAtFrontOfQueue(getPostMessage(r));
    }

    public final void removeCallbacks(Runnable r)
    {
        mQueue.removeMessages(this, r, null);
    }

    public final void removeCallbacks(Runnable r, Object token)
    {
        mQueue.removeMessages(this, r, token);
    }

    public final boolean sendMessage(Message msg)
    {
        return sendMessageDelayed(msg, 0L);
    }

    public final boolean sendEmptyMessage(int what)
    {
        Message msg = Message.obtain();
        msg.what = what;
        return sendMessageDelayed(msg, 0L);
    }

    public final boolean sendMessageDelayed(Message msg, long delayMillis)
    {
        if(delayMillis < 0L)
            delayMillis = 0L;
        return sendMessageAtTime(msg, SystemClock.uptimeMillis() + delayMillis);
    }

    public boolean sendMessageAtTime(Message msg, long uptimeMillis)
    {
        boolean sent = false;
        MessageQueue queue = mQueue;
        if(queue != null)
        {
            msg.target = this;
            sent = queue.enqueueMessage(msg, uptimeMillis);
        } else
        {
            RuntimeException e = new RuntimeException((new StringBuilder()).append(this).append(" sendMessageAtTime() called with no mQueue").toString());
            Log.w("Looper", e.getMessage(), e);
        }
        return sent;
    }

    public final boolean sendMessageAtFrontOfQueue(Message msg)
    {
        boolean sent = false;
        MessageQueue queue = mQueue;
        if(queue != null)
        {
            msg.target = this;
            sent = queue.enqueueMessage(msg, 0L);
        } else
        {
            RuntimeException e = new RuntimeException((new StringBuilder()).append(this).append(" sendMessageAtTime() called with no mQueue").toString());
            Log.w("Looper", e.getMessage(), e);
        }
        return sent;
    }

    public final void removeMessages(int what)
    {
        mQueue.removeMessages(this, what, null);
    }

    public final void removeMessages(int what, Object object)
    {
        mQueue.removeMessages(this, what, object);
    }

    public final void removeCallbacksAndMessages(Object token)
    {
        mQueue.removeCallbacksAndMessages(this, token);
    }

    public final Looper getLooper()
    {
        return mLooper;
    }

    private final Message getPostMessage(Runnable r)
    {
        Message m = Message.obtain();
        m.callback = r;
        return m;
    }

    private final Message getPostMessage(Runnable r, Object token)
    {
        Message m = Message.obtain();
        m.obj = token;
        m.callback = r;
        return m;
    }

    private final void handleCallback(Message message)
    {
        message.callback.run();
    }

    final MessageQueue mQueue;
    final Looper mLooper;
}

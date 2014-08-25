// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Looper.java

package android.os;


// Referenced classes of package android.os:
//            MessageQueue, Message, Handler

public class Looper
{
    static class HandlerException extends Exception
    {

        static String createMessage(Throwable cause)
        {
            String causeMsg = cause.getMessage();
            if(causeMsg == null)
                causeMsg = cause.toString();
            return causeMsg;
        }

        HandlerException(Message message, Throwable cause)
        {
            super(createMessage(cause), cause);
        }
    }


    public static final void prepare()
    {
        if(sThreadLocal.get() != null)
        {
            throw new RuntimeException("Only one Looper may be created per thread");
        } else
        {
            sThreadLocal.set(new Looper());
            return;
        }
    }

    public static final void loop()
    {
        MessageQueue queue;
        Looper me = myLooper();
        queue = me.mQueue;
_L2:
        Message msg;
        do
            msg = queue.next();
        while(msg == null);
        if(msg.target == null)
            return;
        msg.target.dispatchMessage(msg);
        msg.recycle();
        if(true) goto _L2; else goto _L1
_L1:
        Exception exception;
        exception;
        msg.recycle();
        throw exception;
    }

    public static final Looper myLooper()
    {
        return (Looper)sThreadLocal.get();
    }

    public static final MessageQueue myQueue()
    {
        return myLooper().mQueue;
    }

    private Looper()
    {
        mRun = true;
        mThread = Thread.currentThread();
    }

    public void quit()
    {
        Message msg = Message.obtain();
        mQueue.enqueueMessage(msg, 0L);
    }

    private static final boolean DEBUG = false;
    private static final boolean localLOGV = false;
    private static final ThreadLocal sThreadLocal = new ThreadLocal();
    final MessageQueue mQueue = new MessageQueue();
    volatile boolean mRun;
    Thread mThread;

}

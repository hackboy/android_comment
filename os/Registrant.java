// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Registrant.java

package android.os;

import java.lang.ref.WeakReference;

// Referenced classes of package android.os:
//            AsyncResult, Handler, Message

public class Registrant
{

    public Registrant(Handler h, int what, Object obj)
    {
        refH = new WeakReference(h);
        this.what = what;
        userObj = obj;
    }

    public void clear()
    {
        refH = null;
        userObj = null;
    }

    public void notifyRegistrant()
    {
        internalNotifyRegistrant(null, null);
    }

    public void notifyResult(Object result)
    {
        internalNotifyRegistrant(result, null);
    }

    public void notifyException(Throwable exception)
    {
        internalNotifyRegistrant(null, exception);
    }

    public void notifyRegistrant(AsyncResult ar)
    {
        internalNotifyRegistrant(ar.result, ar.exception);
    }

    void internalNotifyRegistrant(Object result, Throwable exception)
    {
        Handler h = getHandler();
        if(h == null)
        {
            clear();
        } else
        {
            Message msg = Message.obtain();
            msg.what = what;
            msg.obj = new AsyncResult(userObj, result, exception);
            h.sendMessage(msg);
        }
    }

    public Message messageForRegistrant()
    {
        Handler h = getHandler();
        if(h == null)
        {
            clear();
            return null;
        } else
        {
            Message msg = h.obtainMessage();
            msg.what = what;
            msg.obj = userObj;
            return msg;
        }
    }

    public Handler getHandler()
    {
        if(refH == null)
            return null;
        else
            return (Handler)refH.get();
    }

    WeakReference refH;
    int what;
    Object userObj;
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HandlerHelper.java

package android.os;


// Referenced classes of package android.os:
//            Handler, HandlerInterface, Message

public class HandlerHelper extends Handler
{

    public HandlerHelper(HandlerInterface target)
    {
        mTarget = target;
    }

    public void handleMessage(Message msg)
    {
        mTarget.handleMessage(msg);
    }

    private HandlerInterface mTarget;
}

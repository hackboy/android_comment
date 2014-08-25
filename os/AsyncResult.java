// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AsyncResult.java

package android.os;


// Referenced classes of package android.os:
//            Message

public class AsyncResult
{

    public static AsyncResult forMessage(Message m, Object r, Throwable ex)
    {
        AsyncResult ret = new AsyncResult(m.obj, r, ex);
        m.obj = ret;
        return ret;
    }

    public static AsyncResult forMessage(Message m)
    {
        AsyncResult ret = new AsyncResult(m.obj, null, null);
        m.obj = ret;
        return ret;
    }

    public AsyncResult(Object uo, Object r, Throwable ex)
    {
        userObj = uo;
        result = r;
        exception = ex;
    }

    public Object userObj;
    public Throwable exception;
    public Object result;
}

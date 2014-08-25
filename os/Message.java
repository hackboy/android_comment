// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Message.java

package android.os;

import java.util.HashMap;

// Referenced classes of package android.os:
//            Parcelable, Handler, Parcel

public final class Message
    implements Parcelable
{

    public static Message obtain()
    {
        Object obj1 = mPoolSync;
        JVM INSTR monitorenter ;
        Message m;
        if(mPool == null)
            break MISSING_BLOCK_LABEL_32;
        m = mPool;
        mPool = m.next;
        m.next = null;
        return m;
        obj1;
        JVM INSTR monitorexit ;
          goto _L1
        Exception exception;
        exception;
        throw exception;
_L1:
        return new Message();
    }

    public static Message obtain(Handler h)
    {
        Message m = obtain();
        m.target = h;
        return m;
    }

    public static Message obtain(Handler h, int what)
    {
        Message m = obtain();
        m.target = h;
        m.what = what;
        return m;
    }

    public static Message obtain(Handler h, int what, Object obj)
    {
        Message m = obtain();
        m.target = h;
        m.what = what;
        m.obj = obj;
        return m;
    }

    public static Message obtain(Handler h, int what, int arg1, int arg2)
    {
        Message m = obtain();
        m.target = h;
        m.what = what;
        m.arg1 = arg1;
        m.arg2 = arg2;
        return m;
    }

    public static Message obtain(Handler h, int what, int arg1, int arg2, Object obj)
    {
        Message m = obtain();
        m.target = h;
        m.what = what;
        m.arg1 = arg1;
        m.arg2 = arg2;
        m.obj = obj;
        return m;
    }

    public void recycle()
    {
        synchronized(mPoolSync)
        {
            if(mPoolSize < 10)
            {
                clearForRecycle();
                next = mPool;
                mPool = this;
            }
        }
    }

    public void copyFrom(Message o)
    {
        what = o.what;
        arg1 = o.arg1;
        arg2 = o.arg2;
        obj = o.obj;
        when = o.when;
        target = o.target;
        callback = o.callback;
        if(o.data != null)
            data = (HashMap)o.data.clone();
        else
            data = null;
    }

    public HashMap getData()
    {
        if(data == null)
            data = new HashMap();
        return data;
    }

    public HashMap peekData()
    {
        return data;
    }

    public void setData(HashMap data)
    {
        this.data = data;
    }

    public void sendToTarget()
    {
        target.sendMessage(this);
    }

    void clearForRecycle()
    {
        what = 0;
        arg1 = 0;
        arg2 = 0;
        obj = null;
        when = 0L;
        target = null;
        callback = null;
        data = null;
    }

    public Message()
    {
    }

    public String toString()
    {
        StringBuilder b = new StringBuilder();
        b.append("{ what=");
        b.append(what);
        if(arg1 != 0)
        {
            b.append(" arg1=");
            b.append(arg1);
        }
        if(arg2 != 0)
        {
            b.append(" arg2=");
            b.append(arg2);
        }
        if(obj != null)
        {
            b.append(" obj=");
            b.append(obj);
        }
        b.append(" }");
        return b.toString();
    }

    public void writeToParcel(Parcel dest)
    {
        if(obj != null || callback != null)
        {
            throw new RuntimeException("Can't marshal objects across processes.");
        } else
        {
            dest.writeInt(what);
            dest.writeInt(arg1);
            dest.writeInt(arg2);
            dest.writeLong(when);
            dest.writeMap(data);
            return;
        }
    }

    private Message(Parcel source)
    {
        what = source.readInt();
        arg1 = source.readInt();
        arg2 = source.readInt();
        when = source.readLong();
        data = source.readHashMap();
    }


    public int what;
    public int arg1;
    public int arg2;
    public Object obj;
    public long when;
    public Handler target;
    public Runnable callback;
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Message createFromParcel(Parcel source)
        {
            return new Message(source);
        }

        public Message[] newArray(int size)
        {
            return new Message[size];
        }

        public volatile Object[] newArray(int x0)
        {
            return newArray(x0);
        }

        public volatile Object createFromParcel(Parcel x0)
        {
            return createFromParcel(x0);
        }

    };
    HashMap data;
    Message next;
    private static Object mPoolSync = new Object();
    private static Message mPool;
    private static int mPoolSize = 0;
    private static final int MAX_POOL_SIZE = 10;

}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BinderNative.java

package android.os;


// Referenced classes of package android.os:
//            IBinder, DeadObjectException, Parcel, IInterface

public class BinderNative
    implements IBinder
{
    //binder调用者
    public static final native int getCallingPid();

    public static final native int getCallingUid();

    public BinderNative()
    {
        init();
    }
    //什么
    public void attachInterface(IInterface owner, String descriptor)
    {
        mOwner = owner;
        mDescriptor = descriptor;
    }

    public boolean pingBinder()
    {
        return true;
    }

    public IInterface queryLocalInterface(String descriptor)
    {
        if(mDescriptor.equals(descriptor))
            return mOwner;
        else
            return null;
    }

    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags)
    {
        if(code == 0x5f444d50)
        {
            String result = dump();
            if(result != null)
                reply.writeString(result);
            return true;
        } else
        {
            return false;
        }
    }

    protected String dump()
    {
        return null;
    }

    public final boolean transact(int code, Parcel data, Parcel reply, int flags)
        throws DeadObjectException
    {
        if(data != null)
            data.setDataPosition(0);
        boolean r;
        if(code == 0x5f444d50)
        {
            String result = dump();
            if(result != null)
                reply.writeString(result);
            r = true;
        } else
        {
            r = onTransact(code, data, reply, flags);
        }
        if(reply != null)
            reply.setDataPosition(0);
        return r;
    }

    public int getConstantData(Parcel outData)
    {
        return 0;
    }

    public void linkToDeath(IBinder.DeathRecipient deathrecipient, int i)
    {
    }

    public void unlinkToDeath(IBinder.DeathRecipient deathrecipient, int i)
    {
    }

    public static final native IBinder getContextObject();

    public static final native void joinThreadPool();

    protected void finalize()
        throws Throwable
    {
        destroy();
    }

    private final native void init();

    private final native void destroy();

    private boolean execTransact(int code, int dataObj, int replyObj, int flags)
    {
        Parcel data = Parcel.obtain(dataObj);
        Parcel reply = Parcel.obtain(replyObj);
        boolean res = onTransact(code, data, reply, flags);
        reply.recycle();
        data.recycle();
        return res;
    }

    private int execGetConstantData(int outDataObj)
    {
        Parcel outData = Parcel.obtain(outDataObj);
        int res = getConstantData(outData);
        outData.recycle();
        return res;
    }

    private int mObject;
    private IInterface mOwner;
    private String mDescriptor;
}

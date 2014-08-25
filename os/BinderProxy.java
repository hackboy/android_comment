// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BinderNative.java

package android.os;

import android.util.Log;

// Referenced classes of package android.os:
//            IBinder, DeadObjectException, IInterface, Parcel

final class BinderProxy
    implements IBinder
{

    BinderProxy()
    {
    }

    public native boolean pingBinder();
    //先查看本地是否有该服务，如果有就不需要进行底层的binder传输
    public IInterface queryLocalInterface(String descriptor)
    {
        return null;
    }
    //进行传输
    public native boolean transact(int i, Parcel parcel, Parcel parcel1, int j)
        throws DeadObjectException;

    public native int getConstantData(Parcel parcel);

    public native void linkToDeath(IBinder.DeathRecipient deathrecipient, int i);

    public native void unlinkToDeath(IBinder.DeathRecipient deathrecipient, int i);

    protected void finalize()
        throws Throwable
    {
        destroy();
    }

    private final native void destroy();

    private static final void sendDeathNotice(IBinder.DeathRecipient recipient)
    {
        try
        {
            recipient.binderDied();
        }
        catch(Throwable t)
        {
            Log.w("BinderNative", "Uncaught exception from death notification", t);
        }
    }

    private int mObject;
}

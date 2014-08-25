// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ServiceManagerNative.java

package android.os;


// Referenced classes of package android.os:
//            IServiceManager, DeadObjectException, Parcel, IBinder

class ServiceManagerProxy
    implements IServiceManager
{

    public ServiceManagerProxy(IBinder remote)
    {
        mRemote = remote;
    }

    public IBinder asBinder()
    {
        return mRemote;
    }

    public IBinder getService(String name)
        throws DeadObjectException
    {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeString(name);
        mRemote.transact(1, data, reply, 0);
        IBinder binder = reply.readStrongBinder();
        reply.recycle();
        data.recycle();
        return binder;
    }

    public IBinder checkService(String name)
        throws DeadObjectException
    {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeString(name);
        mRemote.transact(2, data, reply, 0);
        IBinder binder = reply.readStrongBinder();
        reply.recycle();
        data.recycle();
        return binder;
    }

    public void addService(String name, IBinder service)
        throws DeadObjectException
    {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeString(name);
        data.writeStrongBinder(service);
        mRemote.transact(3, data, reply, 0);
        reply.recycle();
        data.recycle();
    }

    public String[] listServices()
        throws DeadObjectException
    {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        mRemote.transact(4, data, reply, 0);
        String list[] = reply.readStringArray();
        reply.recycle();
        data.recycle();
        return list;
    }

    private IBinder mRemote;
}

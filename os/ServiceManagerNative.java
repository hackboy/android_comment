// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ServiceManagerNative.java

package android.os;


// Referenced classes of package android.os:
//            BinderNative, IServiceManager, ServiceManagerProxy, DeadObjectException, 
//            IBinder, Parcel

public abstract class ServiceManagerNative extends BinderNative
    implements IServiceManager
{

    public static IServiceManager asInterface(IBinder obj)
    {
        if(obj == null)
            return null;
        IServiceManager in = (IServiceManager)obj.queryLocalInterface("android.os.IServiceManager");
        if(in != null)
            return in;
        else
            return new ServiceManagerProxy(obj);
    }

    public static IServiceManager getDefault()
    {
        if(gDefault != null)
        {
            return gDefault;
        } else
        {
            gDefault = asInterface(getContextObject());
            return gDefault;
        }
    }

    public ServiceManagerNative()
    {
        attachInterface(this, "android.os.IServiceManager");
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
    {
        code;
        JVM INSTR tableswitch 1 4: default 112
    //                   1 32
    //                   2 54
    //                   3 76
    //                   4 98;
           goto _L1 _L2 _L3 _L4 _L5
_L2:
        String name = data.readString();
        IBinder service = getService(name);
        reply.writeStrongBinder(service);
        return true;
_L3:
        String name = data.readString();
        IBinder service = checkService(name);
        reply.writeStrongBinder(service);
        return true;
_L4:
        String name = data.readString();
        IBinder service = data.readStrongBinder();
        addService(name, service);
        return true;
_L5:
        String list[] = listServices();
        reply.writeStringArray(list);
        return true;
        DeadObjectException e;
        e;
_L1:
        return false;
    }

    public IBinder asBinder()
    {
        return this;
    }

    private static IServiceManager gDefault;
}

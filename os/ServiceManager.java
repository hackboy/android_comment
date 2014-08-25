// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ServiceManager.java

package android.os;


// Referenced classes of package android.os:
//            DeadObjectException, ServiceManagerNative, IServiceManager, IBinder

public class ServiceManager
{

    public ServiceManager()
    {
    }

    public static IBinder getService(String name)
    {
        return ServiceManagerNative.getDefault().getService(name);
        DeadObjectException e;
        e;
        return null;
    }
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IServiceManager.java

package android.os;


// Referenced classes of package android.os:
//            IInterface, DeadObjectException, IBinder

public interface IServiceManager
    extends IInterface
{

    public abstract IBinder getService(String s)
        throws DeadObjectException;

    public abstract IBinder checkService(String s)
        throws DeadObjectException;

    public abstract void addService(String s, IBinder ibinder)
        throws DeadObjectException;

    public abstract String[] listServices()
        throws DeadObjectException;

    public static final String descriptor = "android.os.IServiceManager";
    public static final int GET_SERVICE_TRANSACTION = 1;
    public static final int CHECK_SERVICE_TRANSACTION = 2;
    public static final int ADD_SERVICE_TRANSACTION = 3;
    public static final int LIST_SERVICES_TRANSACTION = 4;
}

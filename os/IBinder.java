// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IBinder.java

package android.os;


// Referenced classes of package android.os:
//            DeadObjectException, IInterface, Parcel

public interface IBinder
{
    public static interface DeathRecipient
    {

        public abstract void binderDied();
    }


    public abstract boolean pingBinder();
    //这个方法的作用
    public abstract IInterface queryLocalInterface(String s);
    //进行数据的传输
    public abstract boolean transact(int i, Parcel parcel, Parcel parcel1, int j)
        throws DeadObjectException;

    public abstract int getConstantData(Parcel parcel);

    public abstract void linkToDeath(DeathRecipient deathrecipient, int i);

    public abstract void unlinkToDeath(DeathRecipient deathrecipient, int i);

    public static final int FIRST_CALL_TRANSACTION = 1;
    public static final int LAST_CALL_TRANSACTION = 0xffffff;
    public static final int PING_TRANSACTION = 0x5f504e47;
    public static final int DUMP_TRANSACTION = 0x5f444d50;
}

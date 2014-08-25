// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IInterface.java

package android.os;


// Referenced classes of package android.os:
//            IBinder
//asBinder和asInterface应该是对应的
//将接口转换为binder对象和将binder转换为接口
public interface IInterface
{

    public abstract IBinder asBinder();
}

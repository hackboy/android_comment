// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NetStat.java

package android.os;


public class NetStat
{

    public NetStat()
    {
    }

    public static native int netStatGetTxPkts();

    public static native int netStatGetRxPkts();

    public static native int netStatGetTxBytes();

    public static native int netStatGetRxBytes();
}

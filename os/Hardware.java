// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Hardware.java

package android.os;


public class Hardware
{

    public Hardware()
    {
    }

    public static native int setLedState(int i, int j, int k);

    public static native boolean setBtAndWiFiLedState(boolean flag);
}

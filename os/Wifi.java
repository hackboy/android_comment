// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Wifi.java

package android.os;


public class Wifi
{

    public Wifi()
    {
    }

    public static native int wifiStatus();

    public static native int wifiManualActivate(String s, String s1, String s2, String s3);

    public static native int wifiDhcpActivate(String s);

    public static native int wifiDeactivate();

    public static native String wifiGetErrorString(int i);
}

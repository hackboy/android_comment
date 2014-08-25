// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Power.java

package android.os;


public class Power
{

    private Power()
    {
    }

    public static native void acquireWakeLock(int i, String s);

    public static native void releaseWakeLock(String s);

    public static native int setLightState(int i, int j);

    public static native int setScreenState(boolean flag);

    public static native int setLastUserActivityTimeout(long l);

    public static native void shutdown();

    public static final int PARTIAL_WAKE_LOCK = 1;
    public static final int FULL_WAKE_LOCK = 2;
    public static final int KEYBOARD_LIGHT = 1;
    public static final int SCREEN_LIGHT = 2;
}

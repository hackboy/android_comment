// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SystemService.java

package android.os;


// Referenced classes of package android.os:
//            SystemProperties

public class SystemService
{

    public SystemService()
    {
    }

    public static void start(String name)
    {
        SystemProperties.set("ctl.start", name);
    }

    public static void stop(String name)
    {
        SystemProperties.set("ctl.stop", name);
    }
}

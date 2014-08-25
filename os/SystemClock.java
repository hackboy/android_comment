// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SystemClock.java

package android.os;


public final class SystemClock
{

    private SystemClock()
    {
    }

    public static void sleep(long ms)
    {
        long start = uptimeMillis();
        long duration = ms;
        boolean interrupted = false;
        do
        {
            try
            {
                Thread.sleep(duration);
            }
            catch(InterruptedException e)
            {
                interrupted = true;
            }
            duration = (start + ms) - uptimeMillis();
        } while(duration > 0L);
        if(interrupted)
            Thread.currentThread().interrupt();
    }

    public static native boolean setCurrentTimeMillis(long l);

    public static native long uptimeMillis();

    public static native long elapsedRealtime();
}

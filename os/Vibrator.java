// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Vibrator.java

package android.os;


// Referenced classes of package android.os:
//            DeadObjectException, BinderNative, ServiceManager, IVibratorService

public class Vibrator
{

    public Vibrator()
    {
        mService = IVibratorService.Stub.asInterface(ServiceManager.getService("vibrator"));
    }

    public void vibrate(long milliseconds)
    {
        try
        {
            mService.vibrate(milliseconds);
        }
        catch(DeadObjectException e) { }
    }

    public void vibrate(long pattern[], int repeat)
    {
        if(repeat < pattern.length)
            try
            {
                mService.vibratePattern(pattern, repeat, new BinderNative());
            }
            catch(DeadObjectException e) { }
        else
            throw new ArrayIndexOutOfBoundsException();
    }

    public void cancel()
    {
        try
        {
            mService.cancel();
        }
        catch(DeadObjectException e) { }
    }

    IVibratorService mService;
}

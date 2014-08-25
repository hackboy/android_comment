// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PowerManager.java

package android.os;


// Referenced classes of package android.os:
//            DeadObjectException, IPowerManager, Handler, BinderNative, 
//            RuntimeInit, IBinder

public class PowerManager
{
    public class WakeLock
    {

        public void setReferenceCounted(boolean value)
        {
            mRefCounted = value;
        }

        public void acquire()
        {
            synchronized(mToken)
            {
                if(!mRefCounted || mCount++ == 0)
                {
                    try
                    {
                        mService.acquireWakeLock(mFlags, mToken, mTag);
                    }
                    catch(DeadObjectException e) { }
                    mHeld = true;
                }
            }
        }

        public void acquire(long timeout)
        {
            acquire();
            mHandler.postDelayed(mReleaser, timeout);
        }

        public void release()
        {
            synchronized(mToken)
            {
                if(!mRefCounted || --mCount == 0)
                {
                    try
                    {
                        mService.releaseWakeLock(mToken);
                    }
                    catch(DeadObjectException e) { }
                    mHeld = false;
                }
                if(mCount < 0)
                    throw new RuntimeException((new StringBuilder()).append("WakeLock under-locked ").append(mTag).toString());
            }
        }

        public boolean isHeld()
        {
            IBinder ibinder = mToken;
            JVM INSTR monitorenter ;
            return mHeld;
            Exception exception;
            exception;
            throw exception;
        }

        protected void finalize()
            throws Throwable
        {
            synchronized(mToken)
            {
                if(mHeld)
                {
                    try
                    {
                        mService.releaseWakeLock(mToken);
                    }
                    catch(DeadObjectException e) { }
                    RuntimeInit.crash("PowerManager", new Exception((new StringBuilder()).append("WakeLock finalized while still held: ").append(mTag).toString()));
                }
            }
        }

        static final int RELEASE_WAKE_LOCK = 1;
        Runnable mReleaser;
        int mFlags;
        String mTag;
        IBinder mToken;
        int mCount;
        boolean mRefCounted;
        boolean mHeld;
        final PowerManager this$0;

        WakeLock(int flags, String tag)
        {
            this$0 = PowerManager.this;
            super();
            mReleaser = new Runnable() {

                public void run()
                {
                    release();
                }

                final WakeLock this$1;

                
                {
                    this$1 = WakeLock.this;
                    super();
                }
            };
            mCount = 0;
            mRefCounted = true;
            mHeld = false;
            mFlags = flags;
            mTag = tag;
            mToken = new BinderNative();
        }
    }


    public WakeLock newWakeLock(int flags, String tag)
    {
        return new WakeLock(flags, tag);
    }

    public void userActivity(long when, boolean noChangeLights)
    {
        try
        {
            mService.userActivity(when, noChangeLights);
        }
        catch(DeadObjectException e) { }
    }

    private PowerManager()
    {
    }

    public PowerManager(IPowerManager service, Handler handler)
    {
        mService = service;
        mHandler = handler;
    }

    private static final String TAG = "PowerManager";
    public static final int PARTIAL_WAKE_LOCK = 1;
    public static final int FULL_WAKE_LOCK = 2;
    public static final int SCREEN_BRIGHT_WAKE_LOCK = 3;
    public static final int SCREEN_DIM_WAKE_LOCK = 4;
    public static final int ACQUIRE_CAUSES_WAKEUP = 0x10000000;
    public static final int ON_AFTER_RELEASE = 0x20000000;
    IPowerManager mService;
    Handler mHandler;
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ConditionVariable.java

package android.os;


public class ConditionVariable
{

    public ConditionVariable()
    {
        mCondition = false;
    }

    public ConditionVariable(boolean state)
    {
        mCondition = state;
    }

    public void open()
    {
        synchronized(this)
        {
            boolean old = mCondition;
            mCondition = true;
            if(!old)
                notifyAll();
        }
    }

    public void close()
    {
        synchronized(this)
        {
            mCondition = false;
        }
    }

    public void block()
    {
        synchronized(this)
        {
            while(!mCondition) 
                try
                {
                    wait();
                }
                catch(InterruptedException e) { }
        }
    }

    public boolean block(long timeout)
    {
        if(timeout == 0L)
            break MISSING_BLOCK_LABEL_72;
        ConditionVariable conditionvariable = this;
        JVM INSTR monitorenter ;
        long now = System.currentTimeMillis();
        for(long end = now + timeout; !mCondition && now < end; now = System.currentTimeMillis())
            try
            {
                wait(end - now);
            }
            catch(InterruptedException e) { }

        return mCondition;
        Exception exception;
        exception;
        throw exception;
        block();
        return true;
    }

    private volatile boolean mCondition;
}

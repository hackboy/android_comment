// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DoubleDigitManager.java

package android.widget;

import android.os.Handler;

class DoubleDigitManager
{
    static interface CallBack
    {

        public abstract boolean singleDigitIntermediate(int i);

        public abstract void singleDigitFinal(int i);

        public abstract boolean twoDigitsFinal(int i, int j);
    }


    public DoubleDigitManager(long timeoutInMillis, CallBack callBack)
    {
        this.timeoutInMillis = timeoutInMillis;
        mCallBack = callBack;
    }

    public void reportDigit(int digit)
    {
        if(intermediateDigit == null)
        {
            intermediateDigit = Integer.valueOf(digit);
            (new Handler()).postDelayed(new Runnable() {

                public void run()
                {
                    if(intermediateDigit != null)
                    {
                        mCallBack.singleDigitFinal(intermediateDigit.intValue());
                        intermediateDigit = null;
                    }
                }

                final DoubleDigitManager this$0;

            
            {
                this$0 = DoubleDigitManager.this;
                super();
            }
            }, timeoutInMillis);
            if(!mCallBack.singleDigitIntermediate(digit))
            {
                intermediateDigit = null;
                mCallBack.singleDigitFinal(digit);
            }
        } else
        if(mCallBack.twoDigitsFinal(intermediateDigit.intValue(), digit))
            intermediateDigit = null;
    }

    private final long timeoutInMillis;
    private final CallBack mCallBack;
    private Integer intermediateDigit;



}

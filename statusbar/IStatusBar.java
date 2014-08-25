// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IStatusBar.java

package android.server.status;

import android.os.*;

public interface IStatusBar
    extends IInterface
{
    public static abstract class Stub extends BinderNative
        implements IStatusBar
    {
        private static class Proxy
            implements IStatusBar
        {

            public IBinder asBinder()
            {
                return mRemote;
            }

            public void activate()
                throws DeadObjectException
            {
                Parcel _data = Parcel.obtain();
                mRemote.transact(1, _data, null, 0);
                _data.recycle();
                break MISSING_BLOCK_LABEL_32;
                Exception exception;
                exception;
                _data.recycle();
                throw exception;
            }

            public void deactivate()
                throws DeadObjectException
            {
                Parcel _data = Parcel.obtain();
                mRemote.transact(2, _data, null, 0);
                _data.recycle();
                break MISSING_BLOCK_LABEL_32;
                Exception exception;
                exception;
                _data.recycle();
                throw exception;
            }

            private IBinder mRemote;

            Proxy(IBinder remote)
            {
                mRemote = remote;
            }
        }


        public static IStatusBar asInterface(IBinder obj)
        {
            if(obj == null)
                return null;
            IStatusBar in = (IStatusBar)obj.queryLocalInterface("android.server.status.IStatusBar");
            if(in != null)
                return in;
            else
                return new Proxy(obj);
        }

        public IBinder asBinder()
        {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
        {
            code;
            JVM INSTR lookupswitch 2: default 40
        //                       1: 28
        //                       2: 34;
               goto _L1 _L2 _L3
_L2:
            activate();
            return true;
_L3:
            deactivate();
            return true;
            DeadObjectException e;
            e;
_L1:
            return super.onTransact(code, data, reply, flags);
        }

        private static final String DESCRIPTOR = "android.server.status.IStatusBar";
        static final int TRANSACTION_activate = 1;
        static final int TRANSACTION_deactivate = 2;

        public Stub()
        {
            attachInterface(this, "android.server.status.IStatusBar");
        }
    }


    public abstract void activate()
        throws DeadObjectException;

    public abstract void deactivate()
        throws DeadObjectException;
}

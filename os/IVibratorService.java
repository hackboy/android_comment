// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IVibratorService.java

package android.os;


// Referenced classes of package android.os:
//            IInterface, DeadObjectException, IBinder, BinderNative, 
//            Parcel

public interface IVibratorService
    extends IInterface
{
    public static abstract class Stub extends BinderNative
        implements IVibratorService
    {
        private static class Proxy
            implements IVibratorService
        {

            public IBinder asBinder()
            {
                return mRemote;
            }

            public void vibrate(long milliseconds)
                throws DeadObjectException
            {
                Parcel _data = Parcel.obtain();
                _data.writeLong(milliseconds);
                mRemote.transact(1, _data, null, 0);
                _data.recycle();
                break MISSING_BLOCK_LABEL_39;
                Exception exception;
                exception;
                _data.recycle();
                throw exception;
            }

            public void vibratePattern(long pattern[], int repeat, IBinder token)
                throws DeadObjectException
            {
                Parcel _data = Parcel.obtain();
                _data.writeLongArray(pattern);
                _data.writeInt(repeat);
                _data.writeStrongBinder(token);
                mRemote.transact(2, _data, null, 0);
                _data.recycle();
                break MISSING_BLOCK_LABEL_56;
                Exception exception;
                exception;
                _data.recycle();
                throw exception;
            }

            public void cancel()
                throws DeadObjectException
            {
                Parcel _data = Parcel.obtain();
                mRemote.transact(3, _data, null, 0);
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


        public static IVibratorService asInterface(IBinder obj)
        {
            if(obj == null)
                return null;
            IVibratorService in = (IVibratorService)obj.queryLocalInterface("android.os.IVibratorService");
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
            JVM INSTR tableswitch 1 3: default 78
        //                       1 28
        //                       2 42
        //                       3 72;
               goto _L1 _L2 _L3 _L4
_L2:
            long _arg0 = data.readLong();
            vibrate(_arg0);
            return true;
_L3:
            long _arg0[] = data.createLongArray();
            int _arg1 = data.readInt();
            IBinder _arg2 = data.readStrongBinder();
            vibratePattern(_arg0, _arg1, _arg2);
            return true;
_L4:
            cancel();
            return true;
            DeadObjectException e;
            e;
_L1:
            return super.onTransact(code, data, reply, flags);
        }

        private static final String DESCRIPTOR = "android.os.IVibratorService";
        static final int TRANSACTION_vibrate = 1;
        static final int TRANSACTION_vibratePattern = 2;
        static final int TRANSACTION_cancel = 3;

        public Stub()
        {
            attachInterface(this, "android.os.IVibratorService");
        }
    }


    public abstract void vibrate(long l)
        throws DeadObjectException;

    public abstract void vibratePattern(long al[], int i, IBinder ibinder)
        throws DeadObjectException;

    public abstract void cancel()
        throws DeadObjectException;
}

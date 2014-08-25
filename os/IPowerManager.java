// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IPowerManager.java

package android.os;


// Referenced classes of package android.os:
//            IInterface, DeadObjectException, IBinder, BinderNative, 
//            Parcel

public interface IPowerManager
    extends IInterface
{
    public static abstract class Stub extends BinderNative
        implements IPowerManager
    {
        private static class Proxy
            implements IPowerManager
        {

            public IBinder asBinder()
            {
                return mRemote;
            }

            public void acquireWakeLock(int flags, IBinder lock, String tag)
                throws DeadObjectException
            {
                Parcel _data = Parcel.obtain();
                _data.writeInt(flags);
                _data.writeStrongBinder(lock);
                _data.writeString(tag);
                mRemote.transact(1, _data, null, 0);
                _data.recycle();
                break MISSING_BLOCK_LABEL_56;
                Exception exception;
                exception;
                _data.recycle();
                throw exception;
            }

            public void releaseWakeLock(IBinder lock)
                throws DeadObjectException
            {
                Parcel _data = Parcel.obtain();
                _data.writeStrongBinder(lock);
                mRemote.transact(2, _data, null, 0);
                _data.recycle();
                break MISSING_BLOCK_LABEL_37;
                Exception exception;
                exception;
                _data.recycle();
                throw exception;
            }

            public void userActivity(long when, boolean noChangeLights)
                throws DeadObjectException
            {
                Parcel _data = Parcel.obtain();
                _data.writeLong(when);
                _data.writeInt(noChangeLights ? 1 : 0);
                mRemote.transact(3, _data, null, 0);
                _data.recycle();
                break MISSING_BLOCK_LABEL_58;
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


        public static IPowerManager asInterface(IBinder obj)
        {
            if(obj == null)
                return null;
            IPowerManager in = (IPowerManager)obj.queryLocalInterface("android.os.IPowerManager");
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
            JVM INSTR tableswitch 1 3: default 103
        //                       1 28
        //                       2 58
        //                       3 72;
               goto _L1 _L2 _L3 _L4
_L2:
            int _arg0 = data.readInt();
            IBinder _arg1 = data.readStrongBinder();
            String _arg2 = data.readString();
            acquireWakeLock(_arg0, _arg1, _arg2);
            return true;
_L3:
            IBinder _arg0 = data.readStrongBinder();
            releaseWakeLock(_arg0);
            return true;
_L4:
            long _arg0 = data.readLong();
            boolean _arg1 = 0 != data.readInt();
            userActivity(_arg0, _arg1);
            return true;
            DeadObjectException e;
            e;
_L1:
            return super.onTransact(code, data, reply, flags);
        }

        private static final String DESCRIPTOR = "android.os.IPowerManager";
        static final int TRANSACTION_acquireWakeLock = 1;
        static final int TRANSACTION_releaseWakeLock = 2;
        static final int TRANSACTION_userActivity = 3;

        public Stub()
        {
            attachInterface(this, "android.os.IPowerManager");
        }
    }


    public abstract void acquireWakeLock(int i, IBinder ibinder, String s)
        throws DeadObjectException;

    public abstract void releaseWakeLock(IBinder ibinder)
        throws DeadObjectException;

    public abstract void userActivity(long l, boolean flag)
        throws DeadObjectException;
}

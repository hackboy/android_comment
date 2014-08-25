// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IUsb.java

package android.os;


// Referenced classes of package android.os:
//            IInterface, DeadObjectException, BinderNative, IBinder, 
//            Parcel

public interface IUsb
    extends IInterface
{
    public static abstract class Stub extends BinderNative
        implements IUsb
    {
        private static class Proxy
            implements IUsb
        {

            public IBinder asBinder()
            {
                return mRemote;
            }

            public boolean getMassStorageEnabled()
                throws DeadObjectException
            {
                Parcel _data;
                Parcel _reply;
                _data = Parcel.obtain();
                _reply = Parcel.obtain();
                boolean _result;
                mRemote.transact(1, _data, _reply, 0);
                _result = 0 != _reply.readInt();
                _reply.recycle();
                _data.recycle();
                break MISSING_BLOCK_LABEL_60;
                Exception exception;
                exception;
                _reply.recycle();
                _data.recycle();
                throw exception;
                return _result;
            }

            public void setMassStorageEnabled(boolean enabled)
                throws DeadObjectException
            {
                Parcel _data = Parcel.obtain();
                _data.writeInt(enabled ? 1 : 0);
                mRemote.transact(2, _data, null, 0);
                _data.recycle();
                break MISSING_BLOCK_LABEL_45;
                Exception exception;
                exception;
                _data.recycle();
                throw exception;
            }

            public boolean getMassStorageConnected()
                throws DeadObjectException
            {
                Parcel _data;
                Parcel _reply;
                _data = Parcel.obtain();
                _reply = Parcel.obtain();
                boolean _result;
                mRemote.transact(3, _data, _reply, 0);
                _result = 0 != _reply.readInt();
                _reply.recycle();
                _data.recycle();
                break MISSING_BLOCK_LABEL_60;
                Exception exception;
                exception;
                _reply.recycle();
                _data.recycle();
                throw exception;
                return _result;
            }

            public void disconnectMassStorage()
                throws DeadObjectException
            {
                Parcel _data = Parcel.obtain();
                mRemote.transact(4, _data, null, 0);
                _data.recycle();
                break MISSING_BLOCK_LABEL_32;
                Exception exception;
                exception;
                _data.recycle();
                throw exception;
            }

            public void mountMedia(String mountPoint)
                throws DeadObjectException
            {
                Parcel _data = Parcel.obtain();
                _data.writeString(mountPoint);
                mRemote.transact(5, _data, null, 0);
                _data.recycle();
                break MISSING_BLOCK_LABEL_37;
                Exception exception;
                exception;
                _data.recycle();
                throw exception;
            }

            public void unmountMedia(String mountPoint)
                throws DeadObjectException
            {
                Parcel _data = Parcel.obtain();
                _data.writeString(mountPoint);
                mRemote.transact(6, _data, null, 0);
                _data.recycle();
                break MISSING_BLOCK_LABEL_38;
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


        public static IUsb asInterface(IBinder obj)
        {
            if(obj == null)
                return null;
            IUsb in = (IUsb)obj.queryLocalInterface("android.os.IUsb");
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
            JVM INSTR tableswitch 1 6: default 141
        //                       1 40
        //                       2 62
        //                       3 85
        //                       4 107
        //                       5 113
        //                       6 127;
               goto _L1 _L2 _L3 _L4 _L5 _L6 _L7
_L2:
            boolean _result = getMassStorageEnabled();
            reply.writeInt(_result ? 1 : 0);
            return true;
_L3:
            boolean _arg0 = 0 != data.readInt();
            setMassStorageEnabled(_arg0);
            return true;
_L4:
            boolean _result = getMassStorageConnected();
            reply.writeInt(_result ? 1 : 0);
            return true;
_L5:
            disconnectMassStorage();
            return true;
_L6:
            String _arg0 = data.readString();
            mountMedia(_arg0);
            return true;
_L7:
            String _arg0 = data.readString();
            unmountMedia(_arg0);
            return true;
            DeadObjectException e;
            e;
_L1:
            return super.onTransact(code, data, reply, flags);
        }

        private static final String DESCRIPTOR = "android.os.IUsb";
        static final int TRANSACTION_getMassStorageEnabled = 1;
        static final int TRANSACTION_setMassStorageEnabled = 2;
        static final int TRANSACTION_getMassStorageConnected = 3;
        static final int TRANSACTION_disconnectMassStorage = 4;
        static final int TRANSACTION_mountMedia = 5;
        static final int TRANSACTION_unmountMedia = 6;

        public Stub()
        {
            attachInterface(this, "android.os.IUsb");
        }
    }


    public abstract boolean getMassStorageEnabled()
        throws DeadObjectException;

    public abstract void setMassStorageEnabled(boolean flag)
        throws DeadObjectException;

    public abstract boolean getMassStorageConnected()
        throws DeadObjectException;

    public abstract void disconnectMassStorage()
        throws DeadObjectException;

    public abstract void mountMedia(String s)
        throws DeadObjectException;

    public abstract void unmountMedia(String s)
        throws DeadObjectException;
}

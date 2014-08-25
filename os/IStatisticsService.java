// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IStatisticsService.java

package android.os;


// Referenced classes of package android.os:
//            IInterface, DeadObjectException, BinderNative, IBinder, 
//            Parcel

public interface IStatisticsService
    extends IInterface
{
    public static abstract class Stub extends BinderNative
        implements IStatisticsService
    {
        private static class Proxy
            implements IStatisticsService
        {

            public IBinder asBinder()
            {
                return mRemote;
            }

            public void report(String tag, String value)
                throws DeadObjectException
            {
                Parcel _data = Parcel.obtain();
                _data.writeString(tag);
                _data.writeString(value);
                mRemote.transact(1, _data, null, 0);
                _data.recycle();
                break MISSING_BLOCK_LABEL_44;
                Exception exception;
                exception;
                _data.recycle();
                throw exception;
            }

            public void reportCrash(byte crashData[])
                throws DeadObjectException
            {
                Parcel _data = Parcel.obtain();
                _data.writeByteArray(crashData);
                mRemote.transact(2, _data, null, 0);
                _data.recycle();
                break MISSING_BLOCK_LABEL_37;
                Exception exception;
                exception;
                _data.recycle();
                throw exception;
            }

            public void uploadCrashes()
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

            public void uploadStatistics()
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

            public boolean isEnabled()
                throws DeadObjectException
            {
                Parcel _data;
                Parcel _reply;
                _data = Parcel.obtain();
                _reply = Parcel.obtain();
                boolean _result;
                mRemote.transact(5, _data, _reply, 0);
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

            public String getStatusLabel()
                throws DeadObjectException
            {
                Parcel _data;
                Parcel _reply;
                _data = Parcel.obtain();
                _reply = Parcel.obtain();
                String _result;
                mRemote.transact(6, _data, _reply, 0);
                _result = _reply.readString();
                _reply.recycle();
                _data.recycle();
                break MISSING_BLOCK_LABEL_52;
                Exception exception;
                exception;
                _reply.recycle();
                _data.recycle();
                throw exception;
                return _result;
            }

            public long getLastSuccessfulCheckinTime()
                throws DeadObjectException
            {
                Parcel _data;
                Parcel _reply;
                _data = Parcel.obtain();
                _reply = Parcel.obtain();
                long _result;
                mRemote.transact(7, _data, _reply, 0);
                _result = _reply.readLong();
                _reply.recycle();
                _data.recycle();
                break MISSING_BLOCK_LABEL_52;
                Exception exception;
                exception;
                _reply.recycle();
                _data.recycle();
                throw exception;
                return _result;
            }

            public long getNextCheckinTime()
                throws DeadObjectException
            {
                Parcel _data;
                Parcel _reply;
                _data = Parcel.obtain();
                _reply = Parcel.obtain();
                long _result;
                mRemote.transact(8, _data, _reply, 0);
                _result = _reply.readLong();
                _reply.recycle();
                _data.recycle();
                break MISSING_BLOCK_LABEL_52;
                Exception exception;
                exception;
                _reply.recycle();
                _data.recycle();
                throw exception;
                return _result;
            }

            public int getFailureCount()
                throws DeadObjectException
            {
                Parcel _data;
                Parcel _reply;
                _data = Parcel.obtain();
                _reply = Parcel.obtain();
                int _result;
                mRemote.transact(9, _data, _reply, 0);
                _result = _reply.readInt();
                _reply.recycle();
                _data.recycle();
                break MISSING_BLOCK_LABEL_52;
                Exception exception;
                exception;
                _reply.recycle();
                _data.recycle();
                throw exception;
                return _result;
            }

            public String getPendingUpgradeLabel()
                throws DeadObjectException
            {
                Parcel _data;
                Parcel _reply;
                _data = Parcel.obtain();
                _reply = Parcel.obtain();
                String _result;
                mRemote.transact(10, _data, _reply, 0);
                _result = _reply.readString();
                _reply.recycle();
                _data.recycle();
                break MISSING_BLOCK_LABEL_52;
                Exception exception;
                exception;
                _reply.recycle();
                _data.recycle();
                throw exception;
                return _result;
            }

            public String getDownloadStatusLabel()
                throws DeadObjectException
            {
                Parcel _data;
                Parcel _reply;
                _data = Parcel.obtain();
                _reply = Parcel.obtain();
                String _result;
                mRemote.transact(11, _data, _reply, 0);
                _result = _reply.readString();
                _reply.recycle();
                _data.recycle();
                break MISSING_BLOCK_LABEL_52;
                Exception exception;
                exception;
                _reply.recycle();
                _data.recycle();
                throw exception;
                return _result;
            }

            private IBinder mRemote;

            Proxy(IBinder remote)
            {
                mRemote = remote;
            }
        }


        public static IStatisticsService asInterface(IBinder obj)
        {
            if(obj == null)
                return null;
            IStatisticsService in = (IStatisticsService)obj.queryLocalInterface("android.os.IStatisticsService");
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
            JVM INSTR tableswitch 1 11: default 214
        //                       1 60
        //                       2 82
        //                       3 96
        //                       4 102
        //                       5 108
        //                       6 130
        //                       7 144
        //                       8 158
        //                       9 172
        //                       10 186
        //                       11 200;
               goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8 _L9 _L10 _L11 _L12
_L2:
            String _arg0 = data.readString();
            String _arg1 = data.readString();
            report(_arg0, _arg1);
            return true;
_L3:
            byte _arg0[] = data.createByteArray();
            reportCrash(_arg0);
            return true;
_L4:
            uploadCrashes();
            return true;
_L5:
            uploadStatistics();
            return true;
_L6:
            boolean _result = isEnabled();
            reply.writeInt(_result ? 1 : 0);
            return true;
_L7:
            String _result = getStatusLabel();
            reply.writeString(_result);
            return true;
_L8:
            long _result = getLastSuccessfulCheckinTime();
            reply.writeLong(_result);
            return true;
_L9:
            long _result = getNextCheckinTime();
            reply.writeLong(_result);
            return true;
_L10:
            int _result = getFailureCount();
            reply.writeInt(_result);
            return true;
_L11:
            String _result = getPendingUpgradeLabel();
            reply.writeString(_result);
            return true;
_L12:
            String _result = getDownloadStatusLabel();
            reply.writeString(_result);
            return true;
            DeadObjectException e;
            e;
_L1:
            return super.onTransact(code, data, reply, flags);
        }

        private static final String DESCRIPTOR = "android.os.IStatisticsService";
        static final int TRANSACTION_report = 1;
        static final int TRANSACTION_reportCrash = 2;
        static final int TRANSACTION_uploadCrashes = 3;
        static final int TRANSACTION_uploadStatistics = 4;
        static final int TRANSACTION_isEnabled = 5;
        static final int TRANSACTION_getStatusLabel = 6;
        static final int TRANSACTION_getLastSuccessfulCheckinTime = 7;
        static final int TRANSACTION_getNextCheckinTime = 8;
        static final int TRANSACTION_getFailureCount = 9;
        static final int TRANSACTION_getPendingUpgradeLabel = 10;
        static final int TRANSACTION_getDownloadStatusLabel = 11;

        public Stub()
        {
            attachInterface(this, "android.os.IStatisticsService");
        }
    }


    public abstract void report(String s, String s1)
        throws DeadObjectException;

    public abstract void reportCrash(byte abyte0[])
        throws DeadObjectException;

    public abstract void uploadCrashes()
        throws DeadObjectException;

    public abstract void uploadStatistics()
        throws DeadObjectException;

    public abstract boolean isEnabled()
        throws DeadObjectException;

    public abstract String getStatusLabel()
        throws DeadObjectException;

    public abstract long getLastSuccessfulCheckinTime()
        throws DeadObjectException;

    public abstract long getNextCheckinTime()
        throws DeadObjectException;

    public abstract int getFailureCount()
        throws DeadObjectException;

    public abstract String getPendingUpgradeLabel()
        throws DeadObjectException;

    public abstract String getDownloadStatusLabel()
        throws DeadObjectException;
}

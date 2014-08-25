// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Parcel.java

package android.os;

import android.text.TextUtils;
import android.util.Log;
import java.lang.reflect.Field;
import java.util.*;

// Referenced classes of package android.os:
//            Parcelable, Mailbox, IBinder, Bundle, 
//            IInterface, MailSender

public final class Parcel
{

    public static final Parcel obtain()
    {
        Parcel pool[] = sOwnedPool;
        Parcel aparcel[] = pool;
        JVM INSTR monitorenter ;
        int i = 0;
_L1:
        Parcel p;
        if(i >= 6)
            break MISSING_BLOCK_LABEL_38;
        p = pool[i];
        if(p == null)
            break MISSING_BLOCK_LABEL_32;
        pool[i] = null;
        return p;
        i++;
          goto _L1
        aparcel;
        JVM INSTR monitorexit ;
          goto _L2
        Exception exception;
        exception;
        throw exception;
_L2:
        return new Parcel(0);
    }

    public final void recycle()
    {
        freeBuffer();
        Parcel pool[] = mOwnObject == 0 ? sHolderPool : sOwnedPool;
        Parcel aparcel[] = pool;
        JVM INSTR monitorenter ;
        int i = 0;
_L2:
        if(i >= 6)
            break MISSING_BLOCK_LABEL_52;
        if(pool[i] != null)
            break MISSING_BLOCK_LABEL_46;
        pool[i] = this;
        return;
        i++;
        if(true) goto _L2; else goto _L1
_L1:
        break MISSING_BLOCK_LABEL_64;
        Exception exception;
        exception;
        throw exception;
    }

    public final native int dataSize();

    public final native int dataAvail();

    public final native int dataPosition();

    public final native int dataCapacity();

    public final native void setDataSize(int i);

    public final native void setDataPosition(int i);

    public final native void setDataCapacity(int i);

    public final native byte[] marshall();

    public final native void unmarshall(byte abyte0[], int i, int j);

    public final void writeByteArray(byte b[])
    {
        writeByteArray(b, 0, b == null ? 0 : b.length);
    }

    public final void writeByteArray(byte b[], int offset, int len)
    {
        if(b == null)
        {
            writeInt(-1);
            return;
        }
        if(b.length < offset + len || len < 0 || offset < 0)
        {
            throw new ArrayIndexOutOfBoundsException();
        } else
        {
            writeNative(b, offset, len);
            return;
        }
    }

    private final native void writeNative(byte abyte0[], int i, int j);

    public final native void writeInt(int i);

    public final native void writeLong(long l);

    public final native void writeFloat(float f);

    public final native void writeDouble(double d);

    public final native void writeString(String s);

    public final native void writeStrongBinder(IBinder ibinder);

    public final void writeStrongInterface(IInterface val)
    {
        writeStrongBinder(val != null ? val.asBinder() : null);
    }

    public final void writeByte(byte val)
    {
        writeInt(val);
    }

    /**
     * @deprecated Method writeMap is deprecated
     */

    public final void writeMap(Map val)
    {
        if(val == null)
        {
            writeInt(-1);
            return;
        }
        Set s = val.entrySet();
        Iterator i = s.iterator();
        writeInt(s.size());
        java.util.Map.Entry e;
        for(; i.hasNext(); writeValue(e.getValue()))
        {
            e = (java.util.Map.Entry)i.next();
            writeValue(e.getKey());
        }

    }

    public final void writeBundle(Bundle val)
    {
        writeMap(val != null ? val.getAsMap() : null);
    }

    public final void writeList(List val)
    {
        if(val == null)
        {
            writeInt(-1);
            return;
        }
        int N = val.size();
        int i = 0;
        writeInt(N);
        for(; i < N; i++)
            writeValue(val.get(i));

    }

    public final void writeArray(Object val[])
    {
        if(val == null)
        {
            writeInt(-1);
            return;
        }
        int N = val.length;
        int i = 0;
        writeInt(N);
        for(; i < N; i++)
            writeValue(val[i]);

    }

    public final void writeBooleanArray(boolean val[])
    {
        if(val != null)
        {
            int N = val.length;
            writeInt(N);
            for(int i = 0; i < N; i++)
                writeInt(val[i] ? 1 : 0);

        } else
        {
            writeInt(-1);
        }
    }

    public final boolean[] createBooleanArray()
    {
        int N = readInt();
        if(N >= 0)
        {
            boolean val[] = new boolean[N];
            for(int i = 0; i < N; i++)
                val[i] = readInt() != 0;

            return val;
        } else
        {
            return null;
        }
    }

    public final void readBooleanArray(boolean val[])
    {
        int N = readInt();
        if(N == val.length)
        {
            for(int i = 0; i < N; i++)
                val[i] = readInt() != 0;

        } else
        {
            throw new RuntimeException("bad array lengths");
        }
    }

    public final void writeCharArray(char val[])
    {
        if(val != null)
        {
            int N = val.length;
            writeInt(N);
            for(int i = 0; i < N; i++)
                writeInt(val[i]);

        } else
        {
            writeInt(-1);
        }
    }

    public final char[] createCharArray()
    {
        int N = readInt();
        if(N >= 0)
        {
            char val[] = new char[N];
            for(int i = 0; i < N; i++)
                val[i] = (char)readInt();

            return val;
        } else
        {
            return null;
        }
    }

    public final void readCharArray(char val[])
    {
        int N = readInt();
        if(N == val.length)
        {
            for(int i = 0; i < N; i++)
                val[i] = (char)readInt();

        } else
        {
            throw new RuntimeException("bad array lengths");
        }
    }

    public final void writeIntArray(int val[])
    {
        if(val != null)
        {
            int N = val.length;
            writeInt(N);
            for(int i = 0; i < N; i++)
                writeInt(val[i]);

        } else
        {
            writeInt(-1);
        }
    }

    public final int[] createIntArray()
    {
        int N = readInt();
        if(N >= 0)
        {
            int val[] = new int[N];
            for(int i = 0; i < N; i++)
                val[i] = readInt();

            return val;
        } else
        {
            return null;
        }
    }

    public final void readIntArray(int val[])
    {
        int N = readInt();
        if(N == val.length)
        {
            for(int i = 0; i < N; i++)
                val[i] = readInt();

        } else
        {
            throw new RuntimeException("bad array lengths");
        }
    }

    public final void writeLongArray(long val[])
    {
        if(val != null)
        {
            int N = val.length;
            writeInt(N);
            for(int i = 0; i < N; i++)
                writeLong(val[i]);

        } else
        {
            writeInt(-1);
        }
    }

    public final long[] createLongArray()
    {
        int N = readInt();
        if(N >= 0)
        {
            long val[] = new long[N];
            for(int i = 0; i < N; i++)
                val[i] = readLong();

            return val;
        } else
        {
            return null;
        }
    }

    public final void readLongArray(long val[])
    {
        int N = readInt();
        if(N == val.length)
        {
            for(int i = 0; i < N; i++)
                val[i] = readLong();

        } else
        {
            throw new RuntimeException("bad array lengths");
        }
    }

    public final void writeFloatArray(float val[])
    {
        if(val != null)
        {
            int N = val.length;
            writeInt(N);
            for(int i = 0; i < N; i++)
                writeFloat(val[i]);

        } else
        {
            writeInt(-1);
        }
    }

    public final float[] createFloatArray()
    {
        int N = readInt();
        if(N >= 0)
        {
            float val[] = new float[N];
            for(int i = 0; i < N; i++)
                val[i] = readFloat();

            return val;
        } else
        {
            return null;
        }
    }

    public final void readFloatArray(float val[])
    {
        int N = readInt();
        if(N == val.length)
        {
            for(int i = 0; i < N; i++)
                val[i] = readFloat();

        } else
        {
            throw new RuntimeException("bad array lengths");
        }
    }

    public final void writeDoubleArray(double val[])
    {
        if(val != null)
        {
            int N = val.length;
            writeInt(N);
            for(int i = 0; i < N; i++)
                writeDouble(val[i]);

        } else
        {
            writeInt(-1);
        }
    }

    public final double[] createDoubleArray()
    {
        int N = readInt();
        if(N >= 0)
        {
            double val[] = new double[N];
            for(int i = 0; i < N; i++)
                val[i] = readDouble();

            return val;
        } else
        {
            return null;
        }
    }

    public final void readDoubleArray(double val[])
    {
        int N = readInt();
        if(N == val.length)
        {
            for(int i = 0; i < N; i++)
                val[i] = readDouble();

        } else
        {
            throw new RuntimeException("bad array lengths");
        }
    }

    public final void writeStringArray(String val[])
    {
        if(val != null)
        {
            int N = val.length;
            writeInt(N);
            for(int i = 0; i < N; i++)
                writeString(val[i]);

        } else
        {
            writeInt(-1);
        }
    }

    public final String[] createStringArray()
    {
        int N = readInt();
        if(N >= 0)
        {
            String val[] = new String[N];
            for(int i = 0; i < N; i++)
                val[i] = readString();

            return val;
        } else
        {
            return null;
        }
    }

    public final void readStringArray(String val[])
    {
        int N = readInt();
        if(N == val.length)
        {
            for(int i = 0; i < N; i++)
                val[i] = readString();

        } else
        {
            throw new RuntimeException("bad array lengths");
        }
    }

    public final void writeTypedList(List val)
    {
        if(val == null)
        {
            writeInt(-1);
            return;
        }
        int N = val.size();
        int i = 0;
        writeInt(N);
        for(; i < N; i++)
            ((Parcelable)val.get(i)).writeToParcel(this);

    }

    public final void writeTypedArray(Parcelable val[])
    {
        writeParcelableArray(val);
    }

    public final void writeValue(Object v)
    {
        if(v == null)
            writeInt(-1);
        else
        if(v instanceof String)
        {
            writeInt(0);
            writeString((String)v);
        } else
        if(v instanceof Integer)
        {
            writeInt(1);
            writeInt(((Integer)v).intValue());
        } else
        if(v instanceof Map)
        {
            writeInt(2);
            writeMap((Map)v);
        } else
        if(v instanceof Parcelable)
        {
            writeInt(3);
            writeParcelable((Parcelable)v);
        } else
        if(v instanceof Short)
        {
            writeInt(4);
            writeInt(((Short)v).intValue());
        } else
        if(v instanceof Long)
        {
            writeInt(5);
            writeLong(((Long)v).longValue());
        } else
        if(v instanceof Float)
        {
            writeInt(6);
            writeFloat(((Float)v).floatValue());
        } else
        if(v instanceof Double)
        {
            writeInt(7);
            writeDouble(((Double)v).doubleValue());
        } else
        if(v instanceof Mailbox)
        {
            writeInt(8);
            writeStrongBinder(((Mailbox)v).ms.asBinder());
        } else
        if(v instanceof Boolean)
        {
            writeInt(9);
            writeInt(((Boolean)v).booleanValue() ? 1 : 0);
        } else
        if(v instanceof CharSequence)
        {
            writeInt(10);
            TextUtils.writeToParcel((CharSequence)v, this);
        } else
        if(v instanceof List)
        {
            writeInt(11);
            writeList((List)v);
        } else
        if(v instanceof byte[])
        {
            writeInt(12);
            writeByteArray((byte[])(byte[])v);
        } else
        if(v instanceof String[])
        {
            writeInt(13);
            writeStringArray((String[])(String[])v);
        } else
        if(v instanceof IBinder)
        {
            writeInt(14);
            writeStrongBinder((IBinder)v);
        } else
        if(v instanceof Object[])
        {
            writeInt(15);
            writeArray((Object[])(Object[])v);
        } else
        if(v instanceof int[])
        {
            writeInt(16);
            writeIntArray((int[])(int[])v);
        } else
        if(v instanceof long[])
        {
            writeInt(17);
            writeLongArray((long[])(long[])v);
        } else
        if(v instanceof Byte)
        {
            writeInt(18);
            writeInt(((Byte)v).byteValue());
        } else
        {
            throw new RuntimeException((new StringBuilder()).append("Parcel: unable to marshal value ").append(v).toString());
        }
    }

    /**
     * @deprecated Method checkCanWriteMap is deprecated
     */

    public static void checkCanWriteMap(Map m)
    {
        if(m == null)
            return;
        Set s = m.entrySet();
        java.util.Map.Entry e;
        for(Iterator i = s.iterator(); i.hasNext(); checkCanWriteValue(e.getValue()))
        {
            e = (java.util.Map.Entry)i.next();
            if(!(e.getKey() instanceof String))
                throw new RuntimeException((new StringBuilder()).append("Unable to marshal key: ").append(e.getKey()).toString());
        }

    }

    public static void checkCanWriteList(List l)
    {
        if(l == null)
            return;
        int N = l.size();
        for(int i = 0; i < N; i++)
            checkCanWriteValue(l.get(i));

    }

    public static void checkCanWriteArray(Object l[])
    {
        if(l == null)
            return;
        int N = l.length;
        for(int i = 0; i < N; i++)
            checkCanWriteValue(l[i]);

    }

    /**
     * @deprecated Method checkCanWriteValue is deprecated
     */

    public static void checkCanWriteValue(Object v)
    {
        if(v instanceof Map)
            checkCanWriteMap((Map)v);
        else
        if(v instanceof List)
            checkCanWriteList((List)v);
        else
        if(v instanceof Object[])
            checkCanWriteArray((Object[])(Object[])v);
        else
        if(v != null && !(v instanceof String) && !(v instanceof Integer) && !(v instanceof Parcelable) && !(v instanceof Short) && !(v instanceof Long) && !(v instanceof Float) && !(v instanceof Double) && !(v instanceof Mailbox) && !(v instanceof Boolean) && !(v instanceof CharSequence) && !(v instanceof byte[]) && !(v instanceof String[]) && !(v instanceof IBinder) && !(v instanceof Object[]) && !(v instanceof int[]) && !(v instanceof long[]) && !(v instanceof Byte))
            throw new RuntimeException((new StringBuilder()).append("Parcel: unable to marshal value ").append(v).toString());
    }

    public final void writeParcelable(Parcelable p)
    {
        if(p == null)
        {
            writeString(null);
            return;
        } else
        {
            writeString(p.getClass().getName());
            p.writeToParcel(this);
            return;
        }
    }

    public final void writeException(Exception e)
    {
        int code = 0;
        if(e instanceof SecurityException)
            code = -1;
        if(code == 0)
        {
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;
            else
                throw new RuntimeException(e);
        } else
        {
            writeInt(code);
            writeString(e.getMessage());
            return;
        }
    }

    public final void writeNoException()
    {
        writeInt(0);
    }

    public final void readException()
    {
        int code = readInt();
        if(code == 0)
            return;
        String msg = readString();
        switch(code)
        {
        case -1: 
            throw new SecurityException(msg);
        }
        throw new RuntimeException((new StringBuilder()).append("Unknown exception code: ").append(code).append(" msg ").append(msg).toString());
    }

    public final native int readInt();

    public final native long readLong();

    public final native float readFloat();

    public final native double readDouble();

    public final native String readString();

    public final native IBinder readStrongBinder();

    public final byte readByte()
    {
        return (byte)(readInt() & 0xff);
    }

    /**
     * @deprecated Method readMap is deprecated
     */

    public final void readMap(Map outVal)
    {
        int N = readInt();
        readMapInternal(outVal, N);
    }

    public final void readList(List outVal)
    {
        int N = readInt();
        readListInternal(outVal, N);
    }

    /**
     * @deprecated Method readHashMap is deprecated
     */

    public final HashMap readHashMap()
    {
        int N = readInt();
        if(N < 0)
        {
            return null;
        } else
        {
            HashMap m = new HashMap(N);
            readMapInternal(m, N);
            return m;
        }
    }

    public final Bundle readBundle()
    {
        int N = readInt();
        if(N < 0)
        {
            return null;
        } else
        {
            Bundle m = new Bundle(N);
            readMapInternal(m.getAsMap(), N);
            return m;
        }
    }

    /**
     * @deprecated Method readByteArray is deprecated
     */

    public final native byte[] readByteArray();

    public final byte[] createByteArray()
    {
        return readByteArray();
    }

    public final void readByteAarray(byte val[])
    {
        byte ba[] = readByteArray();
        if(ba.length == val.length)
            System.arraycopy(ba, 0, val, 0, ba.length);
        else
            throw new RuntimeException("bad array lengths");
    }

    public final String[] readStringArray()
    {
        String array[] = null;
        int length = readInt();
        if(length >= 0)
        {
            array = new String[length];
            for(int i = 0; i < length; i++)
                array[i] = readString();

        }
        return array;
    }

    public final ArrayList readArrayList()
    {
        int N = readInt();
        if(N < 0)
        {
            return null;
        } else
        {
            ArrayList l = new ArrayList(N);
            readListInternal(l, N);
            return l;
        }
    }

    public final Object[] readArray()
    {
        int N = readInt();
        if(N < 0)
        {
            return null;
        } else
        {
            Object l[] = new Object[N];
            readArrayInternal(l, N);
            return l;
        }
    }

    public final ArrayList readTypedArrayList(Parcelable.Creator c)
    {
        int N = readInt();
        if(N < 0)
            return null;
        ArrayList l = new ArrayList(N);
        for(; N > 0; N--)
            l.add(c.createFromParcel(this));

        return l;
    }

    public final void readTypedList(List list, Parcelable.Creator c)
    {
        int M = list.size();
        int N = readInt();
        int i;
        for(i = 0; i < M && i < N; i++)
            list.set(i, c.createFromParcel(this));

        for(; i < N; i++)
            list.add(c.createFromParcel(this));

        for(; i < M; i++)
            list.remove(N);

    }

    public final Object[] readTypedArray(Parcelable.Creator c)
    {
        return createParcelableArray(c);
    }

    public final Object[] createParcelableArray(Parcelable.Creator c)
    {
        int N = readInt();
        if(N < 0)
            return null;
        Object l[] = (Object[])c.newArray(N);
        for(int i = 0; i < N; i++)
            l[i] = c.createFromParcel(this);

        return l;
    }

    public final void readParcelableArray(Parcelable.Creator c, Parcelable value[])
    {
        int N = readInt();
        if(N == value.length)
        {
            for(int i = 0; i < N; i++)
                value[i] = (Parcelable)c.createFromParcel(this);

        } else
        {
            throw new RuntimeException("bad array lengths");
        }
    }

    public final void writeParcelableArray(Parcelable value[])
    {
        if(value != null)
        {
            int N = value.length;
            writeInt(N);
            for(int i = 0; i < N; i++)
                value[i].writeToParcel(this);

        } else
        {
            writeInt(-1);
        }
    }

    public final Object readValue()
    {
        int type = readInt();
        switch(type)
        {
        case -1: 
            return null;

        case 0: // '\0'
            return readString();

        case 1: // '\001'
            return Integer.valueOf(readInt());

        case 2: // '\002'
            return readHashMap();

        case 3: // '\003'
            return readParcelable();

        case 4: // '\004'
            return new Short((short)readInt());

        case 5: // '\005'
            return new Long(readLong());

        case 6: // '\006'
            return new Float(readFloat());

        case 7: // '\007'
            return new Double(readDouble());

        case 8: // '\b'
            return new Mailbox(null, MailSender.fromBinderInterface(readStrongBinder()));

        case 9: // '\t'
            return Boolean.valueOf(readInt() == 1);

        case 10: // '\n'
            return TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(this);

        case 11: // '\013'
            return readArrayList();

        case 12: // '\f'
            return readByteArray();

        case 13: // '\r'
            return readStringArray();

        case 14: // '\016'
            return readStrongBinder();

        case 15: // '\017'
            return ((Object) (readArray()));

        case 16: // '\020'
            return createIntArray();

        case 17: // '\021'
            return createLongArray();

        case 18: // '\022'
            return Byte.valueOf(readByte());
        }
        throw new RuntimeException("Unmarshalling unknown type code");
    }

    public final Object readParcelable()
    {
        String name = readString();
        if(name == null)
            return null;
        Parcelable.Creator creator = null;
        synchronized(mCreators)
        {
            creator = (Parcelable.Creator)mCreators.get(name);
            if(creator == null)
            {
                try
                {
                    Class c = Class.forName(name);
                    Field f = c.getField("CREATOR");
                    creator = (Parcelable.Creator)f.get(null);
                }
                catch(IllegalAccessException e)
                {
                    Log.e("Parcel", (new StringBuilder()).append("Class not found when unmarshalling: ").append(name).append(", e: ").append(e).toString());
                    throw new RuntimeException(e);
                }
                catch(ClassNotFoundException e)
                {
                    Log.e("Parcel", (new StringBuilder()).append("Class not found when unmarshalling: ").append(name).append(", e: ").append(e).toString());
                    throw new RuntimeException(e);
                }
                catch(ClassCastException e)
                {
                    throw new RuntimeException((new StringBuilder()).append("Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class ").append(name).toString());
                }
                catch(NoSuchFieldException e)
                {
                    throw new RuntimeException((new StringBuilder()).append("Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class ").append(name).toString());
                }
                if(creator == null)
                    throw new RuntimeException((new StringBuilder()).append("Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class ").append(name).toString());
                mCreators.put(name, creator);
            }
        }
        return creator.createFromParcel(this);
    }

    protected static final Parcel obtain(int obj)
    {
        Parcel pool[] = sHolderPool;
        Parcel aparcel[] = pool;
        JVM INSTR monitorenter ;
        int i = 0;
_L1:
        Parcel p;
        if(i >= 6)
            break MISSING_BLOCK_LABEL_47;
        p = pool[i];
        if(p == null)
            break MISSING_BLOCK_LABEL_41;
        pool[i] = null;
        p.init(obj);
        return p;
        i++;
          goto _L1
        aparcel;
        JVM INSTR monitorexit ;
          goto _L2
        Exception exception;
        exception;
        throw exception;
_L2:
        return new Parcel(obj);
    }

    private Parcel(int obj)
    {
        init(obj);
    }

    protected void finalize()
        throws Throwable
    {
        destroy();
    }

    private final native void freeBuffer();

    private final native void init(int i);

    private final native void destroy();

    private final void readMapInternal(Map outVal, int N)
    {
        for(; N > 0; N--)
        {
            Object key = readValue();
            Object value = readValue();
            outVal.put(key, value);
        }

    }

    private final void readListInternal(List outVal, int N)
    {
        for(; N > 0; N--)
        {
            Object value = readValue();
            outVal.add(value);
        }

    }

    private final void readArrayInternal(Object outVal[], int N)
    {
        for(int i = 0; i < N; i++)
        {
            Object value = readValue();
            outVal[i] = value;
        }

    }

    private static final boolean DEBUG_RECYCLE = false;
    private int mObject;
    private int mOwnObject;
    private RuntimeException mStack;
    private static final int POOL_SIZE = 6;
    private static final Parcel sOwnedPool[] = new Parcel[6];
    private static final Parcel sHolderPool[] = new Parcel[6];
    private static HashMap mCreators = new HashMap();

}

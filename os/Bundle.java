// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Bundle.java

package android.os;

import android.content.Intent;
import java.util.*;

// Referenced classes of package android.os:
//            Parcelable, Parcel

public final class Bundle
    implements Parcelable, Cloneable
{

    public Bundle()
    {
        mMap = new HashMap();
    }

    public Bundle(int capacity)
    {
        mMap = new HashMap(capacity);
    }

    public Bundle(Bundle b)
    {
        mMap = new HashMap(b.mMap);
    }

    public Object clone()
    {
        Bundle b = new Bundle();
        if(mMap == Collections.EMPTY_MAP)
            b.mMap = mMap;
        else
            b.mMap = (HashMap)((HashMap)mMap).clone();
        return b;
    }

    public int size()
    {
        return mMap.size();
    }

    public boolean isEmpty()
    {
        return mMap.isEmpty();
    }

    public void clear()
    {
        mMap.clear();
    }

    public boolean containsKey(String key)
    {
        return mMap.containsKey(key);
    }

    public void remove(String key)
    {
        mMap.remove(key);
    }

    public void putAll(Bundle map)
    {
        mMap.putAll(map.mMap);
    }

    /**
     * @deprecated Method getAsMap is deprecated
     */

    public Map getAsMap()
    {
        return mMap;
    }

    public void putBoolean(String key, Boolean value)
    {
        mMap.put(key, value);
    }

    public void putByte(String key, Byte value)
    {
        mMap.put(key, value);
    }

    public void putCharacter(String key, Character value)
    {
        mMap.put(key, value);
    }

    public void putShort(String key, Short value)
    {
        mMap.put(key, value);
    }

    public void putInteger(String key, Integer value)
    {
        mMap.put(key, value);
    }

    public void putLong(String key, Long value)
    {
        mMap.put(key, value);
    }

    public void putFloat(String key, Float value)
    {
        mMap.put(key, value);
    }

    public void putDouble(String key, Double value)
    {
        mMap.put(key, value);
    }

    public void putString(String key, String value)
    {
        mMap.put(key, value);
    }

    public void putCharSequence(String key, CharSequence value)
    {
        mMap.put(key, value);
    }

    public void putByteArray(String key, byte value[])
    {
        mMap.put(key, value);
    }

    public void putIntArray(String key, int value[])
    {
        mMap.put(key, value);
    }

    public void putLongArray(String key, long value[])
    {
        mMap.put(key, value);
    }

    public void putStringArray(String key, String value[])
    {
        mMap.put(key, value);
    }

    public void putParcelable(String key, Parcelable value)
    {
        mMap.put(key, value);
    }

    public void putBundle(String key, Bundle value)
    {
        mMap.put(key, value);
    }

    public void putIntent(String key, Intent value)
    {
        mMap.put(key, value);
    }

    /**
     * @deprecated Method putStringArrayList is deprecated
     */

    public void putStringArrayList(String key, ArrayList value)
    {
        mMap.put(key, value);
    }

    /**
     * @deprecated Method putCharSequenceArrayList is deprecated
     */

    public void putCharSequenceArrayList(String key, ArrayList value)
    {
        mMap.put(key, value);
    }

    /**
     * @deprecated Method putBooleanArrayList is deprecated
     */

    public void putBooleanArrayList(String key, ArrayList value)
    {
        mMap.put(key, value);
    }

    /**
     * @deprecated Method putByteArrayArrayList is deprecated
     */

    public void putByteArrayArrayList(String key, ArrayList value)
    {
        mMap.put(key, value);
    }

    /**
     * @deprecated Method putIntArrayArrayList is deprecated
     */

    public void putIntArrayArrayList(String key, ArrayList value)
    {
        mMap.put(key, value);
    }

    /**
     * @deprecated Method putParcelableArrayList is deprecated
     */

    public void putParcelableArrayList(String key, ArrayList value)
    {
        mMap.put(key, value);
    }

    public Boolean getBoolean(String key)
    {
        return (Boolean)mMap.get(key);
    }

    public Byte getByte(String key)
    {
        return (Byte)mMap.get(key);
    }

    public Character getCharacter(String key)
    {
        return (Character)mMap.get(key);
    }

    public Short getShort(String key)
    {
        return (Short)mMap.get(key);
    }

    public Integer getInteger(String key)
    {
        return (Integer)mMap.get(key);
    }

    public Long getLong(String key)
    {
        return (Long)mMap.get(key);
    }

    public Float getFloat(String key)
    {
        return (Float)mMap.get(key);
    }

    public Double getDouble(String key)
    {
        return (Double)mMap.get(key);
    }

    public String getString(String key)
    {
        return (String)mMap.get(key);
    }

    public CharSequence getCharSequence(String key)
    {
        return (CharSequence)mMap.get(key);
    }

    public byte[] getByteArray(String key)
    {
        return (byte[])(byte[])mMap.get(key);
    }

    public int[] getIntArray(String key)
    {
        return (int[])(int[])mMap.get(key);
    }

    public long[] getLongArray(String key)
    {
        return (long[])(long[])mMap.get(key);
    }

    public String[] getStringArray(String key)
    {
        return (String[])(String[])mMap.get(key);
    }

    public Bundle getBundle(String key)
    {
        return (Bundle)mMap.get(key);
    }

    public Intent getIntent(String key)
    {
        return (Intent)mMap.get(key);
    }

    public Parcelable getParcelable(String key)
    {
        return (Parcelable)mMap.get(key);
    }

    /**
     * @deprecated Method getStringArrayList is deprecated
     */

    public ArrayList getStringArrayList(String key)
    {
        return (ArrayList)mMap.get(key);
    }

    /**
     * @deprecated Method getCharSequenceArrayList is deprecated
     */

    public ArrayList getCharSequenceArrayList(String key)
    {
        return (ArrayList)mMap.get(key);
    }

    /**
     * @deprecated Method getBooleanArrayList is deprecated
     */

    public ArrayList getBooleanArrayList(String key)
    {
        return (ArrayList)mMap.get(key);
    }

    /**
     * @deprecated Method getByteArrayArrayList is deprecated
     */

    public ArrayList getByteArrayArrayList(String key)
    {
        return (ArrayList)mMap.get(key);
    }

    /**
     * @deprecated Method getIntArrayArrayList is deprecated
     */

    public ArrayList getIntArrayArrayList(String key)
    {
        return (ArrayList)mMap.get(key);
    }

    /**
     * @deprecated Method getParcelableArrayList is deprecated
     */

    public ArrayList getParcelableArrayList(String key)
    {
        return (ArrayList)mMap.get(key);
    }

    public void writeToParcel(Parcel parcel)
    {
        parcel.writeBundle(this);
    }

    private Map mMap;
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Bundle createFromParcel(Parcel in)
        {
            return in.readBundle();
        }

        public Bundle[] newArray(int size)
        {
            return new Bundle[size];
        }

        public volatile Object[] newArray(int x0)
        {
            return newArray(x0);
        }

        public volatile Object createFromParcel(Parcel x0)
        {
            return createFromParcel(x0);
        }

    };

}

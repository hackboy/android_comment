// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SparseBooleanArray.java

package android.util;


// Referenced classes of package android.util:
//            ArrayUtils

public class SparseBooleanArray
{

    public SparseBooleanArray()
    {
        int n = ArrayUtils.idealBooleanArraySize(0);
        mKeys = new int[n];
        mValues = new boolean[n];
        mSize = 0;
    }

    public boolean get(int key)
    {
        return get(key, false);
    }

    public boolean get(int key, boolean valueIfKeyNotFound)
    {
        int i = binarySearch(mKeys, 0, mSize, key);
        if(i < 0)
            return valueIfKeyNotFound;
        else
            return mValues[i];
    }

    public void delete(int key)
    {
        int i = binarySearch(mKeys, 0, mSize, key);
        if(i >= 0)
        {
            System.arraycopy(mKeys, i + 1, mKeys, i, mSize - (i + 1));
            System.arraycopy(mValues, i + 1, mValues, i, mSize - (i + 1));
            mSize--;
        }
    }

    public void put(int key, boolean value)
    {
        int i = binarySearch(mKeys, 0, mSize, key);
        if(i >= 0)
        {
            mValues[i] = value;
        } else
        {
            if(mSize + 1 >= mKeys.length)
            {
                int n = ArrayUtils.idealBooleanArraySize(mSize + 1);
                int nkeys[] = new int[n];
                boolean nvalues[] = new boolean[n];
                System.arraycopy(mKeys, 0, nkeys, 0, mKeys.length);
                System.arraycopy(mValues, 0, nvalues, 0, mValues.length);
                mKeys = nkeys;
                mValues = nvalues;
            }
            i = ~i;
            System.arraycopy(mKeys, i, mKeys, i + 1, mSize - i);
            System.arraycopy(mValues, i, mValues, i + 1, mSize - i);
            mKeys[i] = key;
            mValues[i] = value;
            mSize++;
        }
    }

    public int size()
    {
        return mSize;
    }

    public int keyAt(int index)
    {
        return mKeys[index];
    }

    public boolean valueAt(int index)
    {
        return mValues[index];
    }

    public int indexOfKey(int key)
    {
        return binarySearch(mKeys, 0, mSize, key);
    }

    public void clear()
    {
        int n = ArrayUtils.idealBooleanArraySize(0);
        mKeys = new int[n];
        mValues = new boolean[n];
        mSize = 0;
    }

    private static int binarySearch(int a[], int start, int len, int key)
    {
        int high = start + len;
        for(int low = start - 1; high - low > 1;)
        {
            int guess = (high + low) / 2;
            if(a[guess] < key)
                low = guess;
            else
                high = guess;
        }

        if(high == start + len)
            return ~(start + len);
        if(a[high] == key)
            return high;
        else
            return ~high;
    }

    private int mKeys[];
    private boolean mValues[];
    private int mSize;
}

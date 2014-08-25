// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SparseArray.java

package android.util;


// Referenced classes of package android.util:
//            ArrayUtils

//顺便学习一下二分法查找
//稀疏数组？？
public class SparseArray
{

    public SparseArray()
    {
        int n = ArrayUtils.idealIntArraySize(0);
        mKeys = new int[n];
        mValues = new Object[n];
        mSize = 0;
    }

    public Object get(int key)
    {
        return get(key, null);
    }

    public Object get(int key, Object valueIfKeyNotFound)
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
            System.arraycopy(((Object) (mValues)), i + 1, ((Object) (mValues)), i, mSize - (i + 1));
            mSize--;
        }
    }

    public void put(int key, Object value)
    {
        int i = binarySearch(mKeys, 0, mSize, key);
        if(i >= 0)
        {
            mValues[i] = value;
        } else
        {
            if(mSize + 1 >= mKeys.length)
            {
                int n = ArrayUtils.idealIntArraySize(mSize + 1);
                int nkeys[] = new int[n];
                Object nvalues[] = new Object[n];
                System.arraycopy(mKeys, 0, nkeys, 0, mKeys.length);
                System.arraycopy(((Object) (mValues)), 0, ((Object) (nvalues)), 0, mValues.length);
                mKeys = nkeys;
                mValues = nvalues;
            }
            i = ~i;
            System.arraycopy(mKeys, i, mKeys, i + 1, mSize - i);
            System.arraycopy(((Object) (mValues)), i, ((Object) (mValues)), i + 1, mSize - i);
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

    public Object valueAt(int index)
    {
        return mValues[index];
    }

    public int indexOfKey(int key)
    {
        return binarySearch(mKeys, 0, mSize, key);
    }

    public int indexOfValue(Object value)
    {
        for(int i = 0; i < mSize; i++)
            if(mValues[i] == value)
                return i;

        return -1;
    }

    public void clear()
    {
        int n = ArrayUtils.idealIntArraySize(0);
        mKeys = new int[n];
        mValues = new Object[n];
        mSize = 0;
    }
    //二分法查找？
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
    private Object mValues[];
    private int mSize;
}

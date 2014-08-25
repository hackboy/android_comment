// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ArrayUtils.java

package android.util;


public class ArrayUtils
{

    private ArrayUtils()
    {
    }

    public static int idealByteArraySize(int need)
    {
        for(int i = 4; i < 32; i++)
            if(need <= (1 << i) - 12)
                return (1 << i) - 12;

        return need;
    }

    public static int idealBooleanArraySize(int need)
    {
        return idealByteArraySize(need);
    }

    public static int idealShortArraySize(int need)
    {
        return idealByteArraySize(need * 2) / 2;
    }

    public static int idealCharArraySize(int need)
    {
        return idealByteArraySize(need * 2) / 2;
    }

    public static int idealIntArraySize(int need)
    {
        return idealByteArraySize(need * 4) / 4;
    }

    public static int idealFloatArraySize(int need)
    {
        return idealByteArraySize(need * 4) / 4;
    }

    public static int idealObjectArraySize(int need)
    {
        return idealByteArraySize(need * 4) / 4;
    }

    public static int idealLongArraySize(int need)
    {
        return idealByteArraySize(need * 8) / 8;
    }

    public static boolean equals(byte array1[], byte array2[], int length)
    {
        if(array1 == array2)
            return true;
        if(array1 == null || array2 == null || array1.length < length || array2.length < length)
            return false;
        for(int i = 0; i < length; i++)
            if(array1[i] != array2[i])
                return false;

        return true;
    }
}

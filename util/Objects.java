// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Objects.java

package android.util;


public class Objects
{

    public Objects()
    {
    }

    public static Object nonNull(Object t)
    {
        if(t == null)
            throw new NullPointerException();
        else
            return t;
    }

    public static Object nonNull(Object t, String message)
    {
        if(t == null)
            throw new NullPointerException(message);
        else
            return t;
    }
}

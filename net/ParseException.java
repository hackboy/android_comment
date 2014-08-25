// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ParseException.java

package android.net;


public class ParseException extends RuntimeException
{

    ParseException(String response)
    {
        this.response = response;
    }

    public String response;
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LocalSocketAddress.java

package android.net;

import java.net.SocketAddress;

public class LocalSocketAddress extends SocketAddress
{

    public LocalSocketAddress(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    String name;
}

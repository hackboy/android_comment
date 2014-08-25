// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SocketImplProvider.java

package android.net;

import java.net.*;

// Referenced classes of package android.net:
//            AndroidSocketImpl, AndroidDatagramSocketImpl

public class SocketImplProvider
{

    public SocketImplProvider()
    {
    }

    public static SocketImpl getSocketImpl()
    {
        return new AndroidSocketImpl(true);
    }

    public static SocketImpl getLocalSocketImpl()
    {
        return new AndroidSocketImpl(false);
    }

    public static SocketImpl getSocketImpl(Proxy proxy)
    {
        return new AndroidSocketImpl(true);
    }

    public static SocketImpl getServerSocketImpl()
    {
        return new AndroidSocketImpl(true);
    }

    public static DatagramSocketImpl getDatagramSocketImpl()
    {
        return new AndroidDatagramSocketImpl();
    }
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LocalServerSocket.java

package android.net;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.ServerSocketChannel;

// Referenced classes of package android.net:
//            AndroidSocketImpl, LocalSocketAddress, LocalSocket

public class LocalServerSocket
{

    public LocalServerSocket(String name)
        throws IOException
    {
        impl = new AndroidSocketImpl(false);
        impl.create(true);
        localAddress = new LocalSocketAddress(name);
        impl.bind(localAddress);
        impl.listen(50);
    }

    public SocketAddress getLocalSocketAddress()
    {
        return localAddress;
    }

    public ServerSocketChannel getChannel()
    {
        return impl.getServerChannel();
    }

    public void bind(SocketAddress endpoint)
        throws IOException
    {
        localAddress = (LocalSocketAddress)endpoint;
        impl.bind(localAddress);
        impl.listen(50);
    }

    public LocalSocket accept()
        throws IOException
    {
        AndroidSocketImpl acceptedImpl = new AndroidSocketImpl(false);
        impl.accept(acceptedImpl);
        return new LocalSocket(acceptedImpl);
    }

    public void setReuseAddress(boolean on)
        throws SocketException
    {
        impl.setOption(4, new Integer(on ? 1 : 0));
    }

    public boolean getReuseAddress()
        throws SocketException
    {
        return ((Boolean)impl.getOption(4)).booleanValue();
    }

    public void close()
        throws IOException
    {
        impl.close();
    }

    AndroidSocketImpl impl;
    LocalSocketAddress localAddress;
}

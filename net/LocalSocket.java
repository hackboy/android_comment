// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LocalSocket.java

package android.net;

import java.io.*;
import java.net.*;
import java.nio.channels.SocketChannel;

// Referenced classes of package android.net:
//            AndroidSocketImpl

public class LocalSocket
{

    public LocalSocket()
        throws SocketException
    {
        this(new AndroidSocketImpl(false));
        isBound = false;
        isConnected = false;
    }

    LocalSocket(AndroidSocketImpl impl)
        throws SocketException
    {
        this.impl = impl;
        isConnected = false;
        isBound = false;
    }

    public String toString()
    {
        return (new StringBuilder()).append(super.toString()).append(" impl:").append(impl).toString();
    }

    private void implCreateIfNeeded()
        throws IOException
    {
        if(!implCreated)
            synchronized(this)
            {
                if(!implCreated)
                {
                    implCreated = true;
                    impl.create(true);
                }
            }
    }

    public void connect(SocketAddress endpoint)
        throws IOException
    {
        synchronized(this)
        {
            if(isConnected)
                throw new IOException("already connected");
            implCreateIfNeeded();
            impl.connect(endpoint, 0);
            isConnected = true;
            isBound = true;
        }
    }

    public void bind(SocketAddress bindpoint)
        throws IOException
    {
        implCreateIfNeeded();
        synchronized(this)
        {
            if(isBound)
                throw new IOException("already bound");
            InetAddress localAddress;
            int localPort;
            if(bindpoint != null)
            {
                InetSocketAddress isa = (InetSocketAddress)bindpoint;
                localAddress = isa.getAddress();
                localPort = isa.getPort();
            } else
            {
                localAddress = null;
                localPort = 0;
            }
            if(localAddress == null)
                localAddress = ANY_ADDRESS;
            impl.bind(localAddress, localPort);
            isBound = true;
        }
    }

    public InetAddress getInetAddress()
    {
        return impl.getInetAddress();
    }

    public int getPort()
    {
        if(impl.getInetAddress() != null)
            return impl.getPort();
        else
            return 0;
    }

    public int getLocalPort()
    {
        if(localAddress != null)
            return localAddress.getPort();
        else
            return -1;
    }

    public SocketAddress getLocalSocketAddress()
    {
        return localAddress;
    }

    public InputStream getInputStream()
        throws IOException
    {
        implCreateIfNeeded();
        return impl.getInputStream();
    }

    public OutputStream getOutputStream()
        throws IOException
    {
        implCreateIfNeeded();
        return impl.getOutputStream();
    }

    public void close()
        throws IOException
    {
        implCreateIfNeeded();
        impl.close();
    }

    public void shutdownInput()
        throws IOException
    {
        implCreateIfNeeded();
        impl.shutdownInput();
    }

    public void shutdownOutput()
        throws IOException
    {
        implCreateIfNeeded();
        impl.shutdownOutput();
    }

    public void setReceiveBufferSize(int size)
        throws SocketException
    {
        impl.setOption(4098, new Integer(size));
    }

    public int getReceiveBufferSize()
        throws SocketException
    {
        return ((Integer)impl.getOption(4098)).intValue();
    }

    public void setSoTimeout(int i)
        throws SocketException
    {
    }

    public int getSoTimeout()
        throws SocketException
    {
        return 0;
    }

    public void setTcpNoDelay(boolean n)
        throws SocketException
    {
        impl.setOption(1, new Integer(n ? 1 : 0));
    }

    public boolean getTcpNoDelay()
        throws SocketException
    {
        return ((Integer)impl.getOption(1)).intValue() != 0;
    }

    public void setSoLinger(boolean on, int n)
        throws SocketException
    {
        impl.setOption(128, on ? ((Object) (new Integer(n))) : ((Object) (Boolean.FALSE)));
    }

    public int getSoLinger()
        throws SocketException
    {
        return ((Integer)impl.getOption(128)).intValue();
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

    public void setSendBufferSize(int n)
        throws SocketException
    {
        impl.setOption(4097, new Integer(n));
    }

    public SocketChannel getChannel()
    {
        return null;
    }

    public int getSendBufferSize()
        throws SocketException
    {
        return ((Integer)impl.getOption(4097)).intValue();
    }

    public SocketAddress getRemoteSocketAddress()
    {
        throw new UnsupportedOperationException();
    }

    public InetAddress getLocalAddress()
    {
        throw new UnsupportedOperationException();
    }

    public synchronized boolean isConnected()
    {
        return isConnected;
    }

    public void setKeepAlive(boolean on)
        throws SocketException
    {
        throw new UnsupportedOperationException();
    }

    public void setTrafficClass(int tos)
        throws SocketException
    {
        throw new UnsupportedOperationException();
    }

    public boolean getOOBInline()
        throws SocketException
    {
        throw new UnsupportedOperationException();
    }

    public boolean getKeepAlive()
        throws SocketException
    {
        throw new UnsupportedOperationException();
    }

    public int getTrafficClass()
        throws SocketException
    {
        throw new UnsupportedOperationException();
    }

    public boolean isClosed()
    {
        throw new UnsupportedOperationException();
    }

    public synchronized boolean isBound()
    {
        return isBound;
    }

    public boolean isOutputShutdown()
    {
        throw new UnsupportedOperationException();
    }

    public boolean isInputShutdown()
    {
        throw new UnsupportedOperationException();
    }

    public void connect(SocketAddress endpoint, int timeout)
        throws IOException
    {
        connect(endpoint);
    }

    private static final InetAddress ANY_ADDRESS;
    private AndroidSocketImpl impl;
    private boolean implCreated;
    private InetSocketAddress localAddress;
    private boolean isBound;
    private boolean isConnected;

    static 
    {
        try
        {
            ANY_ADDRESS = InetAddress.getByAddress(new byte[4]);
        }
        catch(IOException ex)
        {
            throw new RuntimeException("shouldn't happen", ex);
        }
    }
}

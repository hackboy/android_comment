// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AndroidSocketFactory.java

package android.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.SocketFactory;

public class AndroidSocketFactory extends SocketFactory
{

    public AndroidSocketFactory()
    {
    }

    public Socket createSocket()
    {
        return new Socket();
    }

    public Socket createSocket(InetAddress host, int port)
        throws IOException
    {
        return new Socket(host, port);
    }

    public Socket createSocket(InetAddress host, int port, InetAddress localAddress, int localPort)
        throws IOException
    {
        return new Socket(host, port, localAddress, localPort);
    }

    public Socket createSocket(String host, int port)
        throws IOException
    {
        return new Socket(host, port);
    }

    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort)
        throws IOException
    {
        return new Socket(host, port, localAddress, localPort);
    }
}

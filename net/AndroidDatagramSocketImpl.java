// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AndroidDatagramSocketImpl.java

package android.net;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.*;

public class AndroidDatagramSocketImpl extends DatagramSocketImpl
{

    public AndroidDatagramSocketImpl()
    {
    }

    protected void bind(int port, InetAddress addr)
        throws SocketException
    {
        if(fd == null)
        {
            throw new SocketException("Datagram socket not created");
        } else
        {
            localPort = nativeBind(fd, addr, port);
            setOption(32, Boolean.TRUE);
            return;
        }
    }

    protected void connect(InetAddress addr, int port)
        throws SocketException
    {
        if(fd == null)
        {
            throw new SocketException("Datagram socket not created");
        } else
        {
            connected = true;
            connectedAddress = addr;
            connectedPort = port;
            return;
        }
    }

    protected void close()
    {
        if(fd != null)
        {
            nativeClose(fd);
            fd = null;
        }
    }

    protected void create()
        throws SocketException
    {
        if(fd != null)
        {
            throw new SocketException("Datagram socket already created");
        } else
        {
            fd = nativeCreate();
            return;
        }
    }

    protected void disconnect()
    {
        connected = false;
    }

    public Object getOption(int optID)
        throws SocketException
    {
        if(fd == null)
            throw new SocketException("Datagram socket not created");
        else
            return Boolean.valueOf(nativeGetOption(fd, optID) != 0);
    }

    protected byte getTTL()
        throws IOException
    {
        return (byte)getTimeToLive();
    }

    protected int getTimeToLive()
        throws IOException
    {
        return 0;
    }

    protected void join(InetAddress inetaddress)
        throws IOException
    {
    }

    protected void joinGroup(SocketAddress socketaddress, NetworkInterface networkinterface)
        throws IOException
    {
    }

    protected void leave(InetAddress inetaddress)
        throws IOException
    {
    }

    protected void leaveGroup(SocketAddress socketaddress, NetworkInterface networkinterface)
        throws IOException
    {
    }

    private native int nativeBind(FileDescriptor filedescriptor, InetAddress inetaddress, int i);

    private native void nativeClose(FileDescriptor filedescriptor);

    private native FileDescriptor nativeCreate();

    private native int nativeGetOption(FileDescriptor filedescriptor, int i);

    private native int nativeReceiveFrom(FileDescriptor filedescriptor, DatagramPacket datagrampacket, InetAddress inetaddress, boolean flag);

    private native void nativeSendTo(FileDescriptor filedescriptor, DatagramPacket datagrampacket, InetAddress inetaddress, int i);

    private native void nativeSetOption(FileDescriptor filedescriptor, int i, int j);

    protected int peek(InetAddress sender)
        throws IOException
    {
        DatagramPacket packet = new DatagramPacket(new byte[10], 10);
        return nativeReceiveFrom(fd, packet, sender, true);
    }

    protected int peekData(DatagramPacket pack)
        throws IOException
    {
        InetAddress sender = InetAddress.getByAddress(new byte[] {
            1, 2, 3, 4
        });
        int port = nativeReceiveFrom(fd, pack, sender, true);
        pack.setAddress(sender);
        pack.setPort(port);
        return port;
    }

    protected void receive(DatagramPacket pack)
        throws IOException
    {
        InetAddress sender = InetAddress.getByAddress(new byte[] {
            1, 2, 3, 4
        });
        int port = nativeReceiveFrom(fd, pack, sender, false);
        pack.setAddress(sender);
        pack.setPort(port);
    }

    protected void send(DatagramPacket pack)
        throws IOException
    {
        if(connected)
            nativeSendTo(fd, pack, connectedAddress, connectedPort);
        else
            nativeSendTo(fd, pack, pack.getAddress(), pack.getPort());
    }

    public void setOption(int optID, Object val)
        throws SocketException
    {
        nativeSetOption(fd, optID, Boolean.TRUE.equals(val) ? 1 : 0);
    }

    protected void setTTL(byte ttl)
        throws IOException
    {
        setTimeToLive(ttl & 0xff);
    }

    protected void setTimeToLive(int i)
        throws IOException
    {
    }

    private boolean connected;
    private InetAddress connectedAddress;
    private int connectedPort;
}

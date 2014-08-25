// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AndroidSocketImpl.java

package android.net;

import java.io.*;
import java.net.*;
import java.nio.channels.SocketChannel;

// Referenced classes of package android.net:
//            LocalSocketAddress, AndroidServerSocketChannel

public class AndroidSocketImpl extends SocketImpl
{
    class MyServerSocketChannel extends AndroidServerSocketChannel
    {

        public FileDescriptor getFileDescriptor()
        {
            return AndroidSocketImpl.this.getFileDescriptor();
        }

        public SocketChannel accept()
            throws IOException
        {
            return null;
        }

        public ServerSocket socket()
        {
            return null;
        }

        protected void implCloseSelectableChannel()
            throws IOException
        {
        }

        protected void implConfigureBlocking(boolean flag)
            throws IOException
        {
        }

        final AndroidSocketImpl this$0;

        MyServerSocketChannel()
        {
            this$0 = AndroidSocketImpl.this;
            super();
        }
    }


    AndroidSocketImpl(boolean inetOrLocal)
    {
        serverChannel = new MyServerSocketChannel();
        this.inetOrLocal = inetOrLocal;
    }

    public String toString()
    {
        return (new StringBuilder()).append(super.toString()).append(" fd:").append(fd).toString();
    }

    public void create(boolean stream)
        throws IOException
    {
        if(fd == null)
            fd = create_native(inetOrLocal, stream);
    }

    private native FileDescriptor create_native(boolean flag, boolean flag1)
        throws IOException;

    public void connect(String host, int port)
        throws IOException
    {
        connect(InetAddress.getByName(host), port);
    }

    public void connect(InetAddress address, int port)
        throws IOException
    {
        this.address = address;
        if(fd == null)
        {
            throw new IOException("socket not created");
        } else
        {
            connect(fd, address.getAddress(), port);
            return;
        }
    }

    private native void connect(FileDescriptor filedescriptor, byte abyte0[], int i)
        throws IOException;

    private native void connectLocal(FileDescriptor filedescriptor, String s)
        throws IOException;

    private native void bindLocal(FileDescriptor filedescriptor, String s)
        throws IOException;

    protected void connect(SocketAddress address, int timeout)
        throws IOException
    {
        if(address instanceof InetSocketAddress)
        {
            if(!inetOrLocal)
                throw new IOException("use InetSocketAddress with java.net.Socket only");
            InetSocketAddress isa = (InetSocketAddress)address;
            connect(isa.getAddress(), isa.getPort());
        } else
        if(address instanceof LocalSocketAddress)
        {
            if(fd == null)
                throw new IOException("socket not created");
            if(inetOrLocal)
                throw new IOException("use LocalSocketAddress with android.net.LocalSocket only");
            connectLocal(fd, ((LocalSocketAddress)address).getName());
        } else
        {
            throw new ClassCastException(address.toString());
        }
    }

    public void bind(LocalSocketAddress endpoint)
        throws IOException
    {
        if(fd == null)
        {
            throw new IOException("socket not created");
        } else
        {
            bindLocal(fd, endpoint.getName());
            return;
        }
    }

    public void bind(InetAddress host, int port)
        throws IOException
    {
        if(host == null)
            throw new NullPointerException("host == null");
        if(fd == null)
        {
            throw new IOException("socket not created");
        } else
        {
            bind(fd, host.getAddress(), port);
            return;
        }
    }

    private native void bind(FileDescriptor filedescriptor, byte abyte0[], int i)
        throws IOException;

    protected void listen(int backlog)
        throws IOException
    {
        if(fd == null)
        {
            throw new IOException("socket not created");
        } else
        {
            listen_native(fd, backlog);
            return;
        }
    }

    private native void listen_native(FileDescriptor filedescriptor, int i)
        throws IOException;

    protected void accept(SocketImpl s)
        throws IOException
    {
        if(fd == null)
        {
            throw new IOException("socket not created");
        } else
        {
            AndroidSocketImpl as = (AndroidSocketImpl)s;
            as.fd = accept(fd, as, inetOrLocal);
            return;
        }
    }

    private native FileDescriptor accept(FileDescriptor filedescriptor, AndroidSocketImpl androidsocketimpl, boolean flag)
        throws IOException;

    protected InputStream getInputStream()
        throws IOException
    {
        if(fd == null)
            throw new IOException("socket not created");
        AndroidSocketImpl androidsocketimpl = this;
        JVM INSTR monitorenter ;
        if(fis == null)
            fis = new FileInputStream(fd);
        return fis;
        Exception exception;
        exception;
        throw exception;
    }

    protected OutputStream getOutputStream()
        throws IOException
    {
        if(fd == null)
            throw new IOException("socket not created");
        AndroidSocketImpl androidsocketimpl = this;
        JVM INSTR monitorenter ;
        if(fos == null)
            fos = new FileOutputStream(fd);
        return fos;
        Exception exception;
        exception;
        throw exception;
    }

    protected int available()
        throws IOException
    {
        return getInputStream().available();
    }

    protected void close()
        throws IOException
    {
        getInputStream().close();
        close(fd);
    }

    private native void close(FileDescriptor filedescriptor);

    protected void shutdownInput()
        throws IOException
    {
        if(fd == null)
        {
            throw new IOException("socket not created");
        } else
        {
            shutdown(fd, true);
            return;
        }
    }

    protected void shutdownOutput()
        throws IOException
    {
        if(fd == null)
        {
            throw new IOException("socket not created");
        } else
        {
            shutdown(fd, false);
            return;
        }
    }

    private native void shutdown(FileDescriptor filedescriptor, boolean flag);

    protected FileDescriptor getFileDescriptor()
    {
        return fd;
    }

    public InetAddress getInetAddress()
    {
        return address;
    }

    public int getPort()
    {
        return port;
    }

    protected boolean supportsUrgentData()
    {
        return false;
    }

    protected void sendUrgentData(int data)
        throws IOException
    {
        throw new RuntimeException("not impled");
    }

    protected int getLocalPort()
    {
        return localport;
    }

    protected void setPerformancePreferences(int i, int j, int k)
    {
    }

    public native int getOption_native(FileDescriptor filedescriptor, int i);

    public native void setOption_native(FileDescriptor filedescriptor, int i, int j, int k);

    public Object getOption(int optID)
        throws SocketException
    {
        if(fd == null)
            throw new SocketException("socket not created");
        if(optID == 4102)
            return new Integer(0);
        int value = getOption_native(fd, optID);
        switch(optID)
        {
        case 4097: 
        case 4098: 
            return new Integer(value);

        case 4: // '\004'
        default:
            return Boolean.valueOf(value != 0);
        }
    }

    public void setOption(int optID, Object value)
        throws SocketException
    {
        int boolValue = -1;
        int intValue = 0;
        if(fd == null)
            throw new SocketException("socket not created");
        if(value instanceof Integer)
            intValue = ((Integer)value).intValue();
        else
        if(value instanceof Boolean)
            boolValue = ((Boolean)value).booleanValue() ? 1 : 0;
        else
            throw new SocketException((new StringBuilder()).append("bad value: ").append(value).toString());
        setOption_native(fd, optID, boolValue, intValue);
    }

    public AndroidServerSocketChannel getServerChannel()
    {
        return serverChannel;
    }

    protected FileInputStream fis;
    protected FileOutputStream fos;
    protected boolean inetOrLocal;
    protected boolean useAdbNetworking;
    MyServerSocketChannel serverChannel;
}

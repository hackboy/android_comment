// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SSLSocket.java

package android.net;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;

public class SSLSocket extends javax.net.ssl.SSLSocket
{
    private class SSLOutputStream extends OutputStream
    {

        public void write(int b)
            throws IOException
        {
            SSLSocket.this.write(b);
        }

        public void write(byte b[], int off, int len)
            throws IOException
        {
            int bLength = b.length;
            if((off | len) < 0 || off > bLength || len > bLength - off)
            {
                throw new IndexOutOfBoundsException();
            } else
            {
                SSLSocket.this.write(b, off, len);
                return;
            }
        }

        final SSLSocket this$0;

        private SSLOutputStream()
        {
            this$0 = SSLSocket.this;
            super();
        }

    }

    private class SSLInputStream extends InputStream
    {

        public int read()
            throws IOException
        {
            return SSLSocket.this.read();
        }

        public int read(byte b[], int off, int len)
            throws IOException
        {
            int bLength = b.length;
            if((off | len) < 0 || off > bLength || len > bLength - off)
                throw new IndexOutOfBoundsException();
            else
                return SSLSocket.this.read(b, off, len);
        }

        final SSLSocket this$0;

        private SSLInputStream()
        {
            this$0 = SSLSocket.this;
            super();
        }

    }


    public SSLSocket(Socket socket)
        throws IOException
    {
        this(socket, true);
    }

    public SSLSocket(Socket socket, boolean autoclose)
        throws IOException
    {
        m_autoclose = autoclose;
        m_socket = socket;
        input = null;
        output = null;
        init1();
        if(m_socket.isConnected())
            connect0();
    }

    protected void finalize()
        throws Throwable
    {
        nativeFinalize();
    }

    public void connect(SocketAddress endpoint)
        throws IOException
    {
        synchronized(m_socket)
        {
            if(m_socket.isConnected())
                throw new IOException("already connected");
            m_socket.connect(endpoint);
            connect0();
        }
    }

    public void connect(SocketAddress endpoint, int timeout)
        throws IOException
    {
        synchronized(m_socket)
        {
            if(m_socket.isConnected())
                throw new IOException("already connected");
            m_socket.connect(endpoint, timeout);
            connect0();
        }
    }

    private void connect0()
        throws IOException
    {
        FileInputStream fin = (FileInputStream)m_socket.getInputStream();
        if(fin == null)
        {
            throw new IOException();
        } else
        {
            init2(m_ctx, fin.getFD());
            return;
        }
    }

    public void bind(SocketAddress bindpoint)
        throws IOException
    {
        synchronized(m_socket)
        {
            if(m_socket.isBound())
                throw new IOException("already bound");
            m_socket.bind(bindpoint);
        }
    }

    public boolean isBound()
    {
        return m_socket.isBound();
    }

    public boolean isConnected()
    {
        return m_socket.isConnected();
    }

    public boolean isInputShutdown()
    {
        return false;
    }

    public boolean isOutputShutdown()
    {
        return false;
    }

    public InetAddress getInetAddress()
    {
        return m_socket.getInetAddress();
    }

    public int getPort()
    {
        return m_socket.getPort();
    }

    public int getLocalPort()
    {
        return m_socket.getLocalPort();
    }

    public SocketAddress getLocalSocketAddress()
    {
        return m_socket.getLocalSocketAddress();
    }

    public InputStream getInputStream()
        throws IOException
    {
        if(input == null)
            input = new SSLInputStream();
        return input;
    }

    public OutputStream getOutputStream()
        throws IOException
    {
        if(output == null)
            output = new SSLOutputStream();
        return output;
    }

    public void close()
        throws IOException
    {
        if(m_autoclose)
            m_socket.close();
    }

    public void shutdownInput()
        throws IOException
    {
    }

    public void shutdownOutput()
        throws IOException
    {
    }

    public void setReceiveBufferSize(int size)
        throws SocketException
    {
        m_socket.setReceiveBufferSize(size);
    }

    public int getReceiveBufferSize()
        throws SocketException
    {
        return m_socket.getReceiveBufferSize();
    }

    public void setSoTimeout(int n)
        throws SocketException
    {
        m_socket.setSoTimeout(n);
    }

    public int getSoTimeout()
        throws SocketException
    {
        return m_socket.getSoTimeout();
    }

    public void setTcpNoDelay(boolean n)
        throws SocketException
    {
        m_socket.setTcpNoDelay(n);
    }

    public boolean getTcpNoDelay()
        throws SocketException
    {
        return m_socket.getTcpNoDelay();
    }

    public void setSoLinger(boolean on, int n)
        throws SocketException
    {
        m_socket.setSoLinger(on, n);
    }

    public int getSoLinger()
        throws SocketException
    {
        return m_socket.getSoLinger();
    }

    public void setSendBufferSize(int n)
        throws SocketException
    {
        m_socket.setSendBufferSize(n);
    }

    public int getSendBufferSize()
        throws SocketException
    {
        return m_socket.getSendBufferSize();
    }

    private static native void initStatic();

    private native int init1();

    private native int init2(int i, FileDescriptor filedescriptor)
        throws IOException;

    private native void nativeFinalize();

    private native int read()
        throws IOException;

    private native int read(byte abyte0[], int i, int j)
        throws IOException;

    private native void write(int i)
        throws IOException;

    private native void write(byte abyte0[], int i, int j)
        throws IOException;

    public void addHandshakeCompletedListener(HandshakeCompletedListener handshakecompletedlistener)
    {
    }

    public String[] getEnabledCipherSuites()
    {
        return null;
    }

    public String[] getEnabledProtocols()
    {
        return null;
    }

    public boolean getEnableSessionCreation()
    {
        return false;
    }

    public boolean getNeedClientAuth()
    {
        return false;
    }

    public SSLSession getSession()
    {
        return null;
    }

    public String[] getSupportedCipherSuites()
    {
        return null;
    }

    public String[] getSupportedProtocols()
    {
        return null;
    }

    public boolean getUseClientMode()
    {
        return false;
    }

    public boolean getWantClientAuth()
    {
        return false;
    }

    public void removeHandshakeCompletedListener(HandshakeCompletedListener handshakecompletedlistener)
    {
    }

    public void setEnabledCipherSuites(String as[])
    {
    }

    public void setEnabledProtocols(String as[])
    {
    }

    public void setEnableSessionCreation(boolean flag1)
    {
    }

    public void setNeedClientAuth(boolean flag)
    {
    }

    public void setUseClientMode(boolean flag)
    {
    }

    public void setWantClientAuth(boolean flag)
    {
    }

    public void startHandshake()
        throws IOException
    {
    }

    private int m_ctx;
    private int m_ssl;
    private SSLInputStream input;
    private SSLOutputStream output;
    private boolean m_autoclose;
    private Socket m_socket;

    static 
    {
        initStatic();
    }




}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SSLSocketFactory.java

package android.net;

import java.io.IOException;
import java.net.Socket;

// Referenced classes of package android.net:
//            SSLSocket

public final class SSLSocketFactory
{

    private SSLSocketFactory()
    {
    }

    public static Socket createSocket(Socket socket)
        throws IOException
    {
        return new SSLSocket(socket);
    }
}

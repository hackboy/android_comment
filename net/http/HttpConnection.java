// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HttpConnection.java

package android.net.http;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.params.BasicHttpParams;

// Referenced classes of package android.net.http:
//            Connection, RequestQueue, Request

class HttpConnection extends Connection
{

    HttpConnection(RequestQueue requestQueue, HttpHost host)
    {
        super(requestQueue, host);
    }

    HttpClientConnection openConnection(Request req)
        throws IOException
    {
        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
        BasicHttpParams params = new BasicHttpParams(null);
        Socket sock = new Socket(mHost.getHostName(), mHost.getPort());
        params.setIntParameter("http.socket.buffer-size", 8192);
        conn.bind(sock, params);
        return conn;
    }

    void closeConnection()
    {
        try
        {
            mHttpClientConnection.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    void restartConnection(boolean flag)
    {
    }

    String getScheme()
    {
        return "http";
    }
}

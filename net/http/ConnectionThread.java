// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Connection.java

package android.net.http;

import org.apache.http.HttpHost;

// Referenced classes of package android.net.http:
//            Connection

class ConnectionThread extends Thread
{

    ConnectionThread(Connection connection)
    {
        mConnection = connection;
    }

    public void run()
    {
        setName(mConnection.mHost.toHostString());
        mConnection.processRequests();
        mConnection.mConnectionThread = null;
        mConnection = null;
    }

    Connection mConnection;
}

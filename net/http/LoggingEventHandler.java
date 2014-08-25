// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LoggingEventHandler.java

package android.net.http;

import android.util.Log;
import java.util.Iterator;

// Referenced classes of package android.net.http:
//            EventHandler, Headers, SslCertificate

public class LoggingEventHandler
    implements EventHandler
{

    public LoggingEventHandler()
    {
    }

    public void requestSent()
    {
        Log.v("http", "LoggingEventHandler:requestSent()");
    }

    public void status(int i, int j, int k, String s)
    {
    }

    public void headers(Iterator iterator)
    {
    }

    public void headers(Headers headers1)
    {
    }

    public void locationChanged(String s, boolean flag)
    {
    }

    public void data(byte abyte0[], int i)
    {
    }

    public void endData()
    {
    }

    public void error(int i, String s)
    {
    }

    public void handleSslErrorRequest(int i, String s, SslCertificate sslcertificate)
    {
    }

    protected static final String LOGTAG = "http";
}

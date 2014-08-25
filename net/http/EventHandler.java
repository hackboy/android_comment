// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EventHandler.java

package android.net.http;

import java.util.Iterator;

// Referenced classes of package android.net.http:
//            Headers, SslCertificate

public interface EventHandler
{

    public abstract void status(int i, int j, int k, String s);

    public abstract void headers(Iterator iterator);

    public abstract void headers(Headers headers1);

    public abstract void data(byte abyte0[], int i);

    public abstract void endData();

    public abstract void error(int i, String s);

    public abstract void handleSslErrorRequest(int i, String s, SslCertificate sslcertificate);

    public static final int OK = 0;
    public static final int ERROR = 16;
    public static final int LOOKUP = 32;
    public static final int AUTH = 48;
    public static final int PROXYAUTH = 64;
    public static final int CONNECT = 80;
    public static final int TIMEOUT = 96;
    public static final int REDIRECT_LOOP = 112;
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Proxy.java

package android.net;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import android.test.Assert;
import android.util.Log;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;

public final class Proxy
{

    public Proxy()
    {
    }

    public static final String getHost(Context ctx)
    {
        android.content.ContentResolver contentResolver = ctx.getContentResolver();
        Assert.assertNotNull(contentResolver);
        String host = android.provider.Settings.System.getString(contentResolver, "http_proxy");
        if(host != null)
        {
            int i = host.indexOf(':');
            if(i == -1)
                return null;
            else
                return host.substring(0, i);
        } else
        {
            return getDefaultHost();
        }
    }

    public static final int getPort(Context ctx)
    {
        android.content.ContentResolver contentResolver = ctx.getContentResolver();
        Assert.assertNotNull(contentResolver);
        String host = android.provider.Settings.System.getString(contentResolver, "http_proxy");
        if(host != null)
        {
            int i = host.indexOf(':');
            if(i == -1)
                return -1;
            else
                return Integer.parseInt(host.substring(i + 1));
        } else
        {
            return getDefaultPort();
        }
    }

    public static final String getDefaultHost()
    {
        String host = SystemProperties.get("net.gprs.http-proxy");
        URI u = new URI(host);
        host = u.getHost();
        return host;
        URIException e;
        e;
        Log.d("Proxy", (new StringBuilder()).append("Could not parse host string '").append(host).append("' from system property").toString(), e);
        return null;
    }

    public static final int getDefaultPort()
    {
        String host = SystemProperties.get("net.gprs.http-proxy");
        URI u = new URI(host);
        return u.getPort();
        URIException e;
        e;
        Log.d("Proxy", (new StringBuilder()).append("Could not parse host string '").append(host).append("' from system property").toString(), e);
        return -1;
    }

    public static final String PROXY_CHANGE_ACTION = "android.intent.action.PROXY_CHANGE";
}

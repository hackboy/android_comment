// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Request.java

package android.net.http;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.GZIPInputStream;
import org.apache.http.*;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.RequestContent;

// Referenced classes of package android.net.http:
//            HttpsConnection, Connection, EventHandler, RequestQueue

class Request
{

    Request(Connection connection, String method, HttpHost host, String path, InputStream bodyProvider, int bodyLength, EventHandler eventHandler, 
            Map headers, boolean highPriority)
    {
        mState = 0;
        mFailCount = 0;
        mConnection = connection;
        mEventHandler = eventHandler;
        mHost = host;
        mPath = path;
        mHighPriority = highPriority;
        if(bodyProvider == null)
        {
            mHttpRequest = new BasicHttpRequest(method, getUri());
        } else
        {
            mHttpRequest = new BasicHttpEntityEnclosingRequest(method, getUri());
            setBodyProvider(bodyProvider, bodyLength);
        }
        addHeader("Host", getHostPort());
        addHeader("Accept-Encoding", "gzip");
        addHeaders(headers);
    }

    EventHandler getEventHandler()
    {
        return mEventHandler;
    }

    void addHeader(String name, String value)
    {
        if(name == null)
        {
            String damage = "Null http header name";
            Log.e("http", damage);
            throw new NullPointerException(damage);
        }
        if(value == null || value.length() == 0)
        {
            String damage = (new StringBuilder()).append("Null or empty value for header \"").append(name).append("\"").toString();
            Log.e("http", damage);
            throw new RuntimeException(damage);
        } else
        {
            mHttpRequest.addHeader(name, value);
            return;
        }
    }

    void addHeaders(Map headers)
    {
        if(headers == null)
            return;
        java.util.Map.Entry entry;
        for(Iterator i = headers.entrySet().iterator(); i.hasNext(); addHeader((String)entry.getKey(), (String)entry.getValue()))
            entry = (java.util.Map.Entry)i.next();

    }

    void sendRequest(HttpClientConnection httpClientConnection)
        throws HttpException, IOException
    {
        if(mState != 0)
            return;
        mState = 1;
        requestContentProcessor.process(mHttpRequest, mConnection.getHttpContext());
        httpClientConnection.sendRequestHeader(mHttpRequest);
        if(mHttpRequest instanceof HttpEntityEnclosingRequest)
            httpClientConnection.sendRequestEntity((HttpEntityEnclosingRequest)mHttpRequest);
    }

    void readResponse(HttpClientConnection httpClientConnection)
        throws HttpException, IOException
    {
        if(mState != 1)
            return;
        mState = 2;
        HttpResponse response = null;
        int statuscode = 0;
        boolean hasBody = false;
        boolean reuse = false;
        httpClientConnection.flush();
        do
        {
            response = httpClientConnection.receiveResponseHeader(mHttpRequest.getParams());
            hasBody = canResponseHaveBody(mHttpRequest, response);
            if(hasBody)
                httpClientConnection.receiveResponseEntity(response);
            statuscode = response.getStatusLine().getStatusCode();
        } while(statuscode < 200);
        StatusLine statusLine = response.getStatusLine();
        HttpVersion version = statusLine.getHttpVersion();
        mEventHandler.status(version.getMajor(), version.getMinor(), statuscode, statusLine.getReasonPhrase());
        mEventHandler.headers(response.headerIterator());
        if(hasBody)
        {
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            Header contentEncoding = entity.getContentEncoding();
            InputStream nis;
            if(contentEncoding != null && contentEncoding.getValue().equals("gzip"))
                nis = new GZIPInputStream(is);
            else
                nis = is;
            byte buf[] = mConnection.getBuf();
            int len;
            while((len = nis.read(buf)) != -1) 
                mEventHandler.data(buf, len);
            if(nis != is)
                nis.close();
        }
        mConnection.setCanPersist(response);
        mEventHandler.endData();
        mState = 3;
    }

    synchronized void cancel()
    {
        switch(mState)
        {
        case 0: // '\0'
            mState = 3;
            break;

        case 1: // '\001'
        case 2: // '\002'
            mState = 3;
            mConnection.cancel();
            break;
        }
    }

    String getHostPort()
    {
        String myScheme = mHost.getSchemeName();
        int myPort = mHost.getPort();
        if(myPort != 80 && myScheme.equals("http") || myPort != 443 && myScheme.equals("https"))
            return mHost.toHostString();
        else
            return mHost.getHostName();
    }

    String getUri()
    {
        if(mConnection.mRequestQueue.getProxyHost() == null || mHost.getSchemeName().equals("https"))
            return mPath;
        else
            return (new StringBuilder()).append(mHost.getSchemeName()).append("://").append(getHostPort()).append(mPath).toString();
    }

    public String toString()
    {
        return (new StringBuilder()).append(mHighPriority ? "P*" : "").append(mPath).toString();
    }

    private static boolean canResponseHaveBody(HttpRequest request, HttpResponse response)
    {
        if("HEAD".equalsIgnoreCase(request.getRequestLine().getMethod()))
        {
            return false;
        } else
        {
            int status = response.getStatusLine().getStatusCode();
            return status >= 200 && status != 204 && status != 304 && status != 205;
        }
    }

    private void setBodyProvider(InputStream bodyProvider, int bodyLength)
    {
        if(!bodyProvider.markSupported())
        {
            throw new IllegalArgumentException("bodyProvider must support mark()");
        } else
        {
            bodyProvider.mark(0x7fffffff);
            ((BasicHttpEntityEnclosingRequest)mHttpRequest).setEntity(new InputStreamEntity(bodyProvider, bodyLength));
            return;
        }
    }

    public void handleSslErrorResponse(boolean proceed)
    {
        HttpsConnection connection = (HttpsConnection)(HttpsConnection)mConnection;
        if(connection != null)
            connection.restartConnection(proceed);
    }

    private static final String LOGTAG = "http";
    EventHandler mEventHandler;
    private Connection mConnection;
    BasicHttpRequest mHttpRequest;
    String mPath;
    HttpHost mHost;
    boolean mHighPriority;
    int mState;
    static final int INIT = 0;
    static final int SEND = 1;
    static final int RECEIVE = 2;
    static final int DONE = 3;
    int mFailCount;
    private static final String HOST_HEADER = "Host";
    private static final String ACCEPT_ENCODING_HEADER = "Accept-Encoding";
    private static RequestContent requestContentProcessor = new RequestContent();

}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RequestQueue.java

package android.net.http;

import android.content.*;
import android.net.Proxy;
import android.net.WebAddress;
import android.util.Log;
import java.io.InputStream;
import java.util.*;
import org.apache.http.HttpHost;

// Referenced classes of package android.net.http:
//            LoggingEventHandler, Connection, Request, RequestHandle, 
//            EventHandler

public class RequestQueue
{

    private synchronized void setPipeConfig()
    {
        String host = Proxy.getHost(mContext);
        if(host == null)
            mProxyHost = null;
        else
            mProxyHost = new HttpHost(host, Proxy.getPort(mContext), "http");
    }

    public RequestQueue(Context context)
    {
        mWaiting = false;
        mActive = null;
        mProxyHost = null;
        mContext = context;
        mContext.registerReceiver(new IntentReceiver() {

            public void onReceiveIntent(Context ctx, Intent intent)
            {
                setPipeConfig();
            }

            final RequestQueue this$0;

            
            {
                this$0 = RequestQueue.this;
                super();
            }
        }, new IntentFilter("android.intent.action.PROXY_CHANGE"));
        setPipeConfig();
        mConnections = new HashMap(32);
        mHighPriority = new TreeSet(Connection.sRankComparator);
        mLive = new TreeSet(Connection.sRankComparator);
        mPending = new TreeSet(Connection.sRankComparator);
    }

    public synchronized HttpHost getProxyHost()
    {
        return mProxyHost;
    }

    public RequestHandle queueRequest(String url, String method, Map headers, EventHandler eventHandler, InputStream bodyProvider, int bodyLength, boolean highPriority)
    {
        WebAddress uri = new WebAddress(url);
        if(eventHandler == null)
            eventHandler = new LoggingEventHandler();
        Connection connection = null;
        HttpHost httpHost = new HttpHost(uri.mHost, uri.mPort, uri.mScheme);
        synchronized(this)
        {
            HttpHost host = mProxyHost != null && !uri.mScheme.equals("https") ? mProxyHost : httpHost;
            if(mConnections.containsKey(host))
            {
                connection = (Connection)mConnections.get(host);
                if(highPriority && mPending.contains(connection))
                {
                    mPending.remove(connection);
                    mHighPriority.add(connection);
                }
            } else
            {
                connection = Connection.getConnection(this, host);
                mConnections.put(host, connection);
                if(highPriority)
                    mHighPriority.add(connection);
                else
                    mPending.add(connection);
            }
        }
        Request req = new Request(connection, method, httpHost, uri.mPath, bodyProvider, bodyLength, eventHandler, headers, highPriority);
        connection.queueRequest(req);
        startRequests();
        return new RequestHandle(this, url, method, headers, bodyProvider, bodyLength, req);
    }

    public synchronized void waitUntilComplete()
    {
        try
        {
            mWaiting = true;
            wait();
        }
        catch(InterruptedException e) { }
    }

    private synchronized void startRequests()
    {
        if(mActive != null)
            return;
        if(!mHighPriority.isEmpty())
        {
            mActive = (Connection)mHighPriority.first();
            mHighPriority.remove(mActive);
        } else
        if(!mLive.isEmpty())
        {
            mActive = (Connection)mLive.first();
            mLive.remove(mActive);
        } else
        if(!mPending.isEmpty())
        {
            mActive = (Connection)mPending.first();
            mPending.remove(mActive);
        } else
        {
            return;
        }
        mActive.start();
    }

    synchronized boolean highPriorityPending()
    {
        return !mHighPriority.isEmpty();
    }

    synchronized boolean requestsPending()
    {
        return !mHighPriority.isEmpty() || !mLive.isEmpty() || !mPending.isEmpty();
    }

    synchronized boolean connectionCompleted(Connection connection)
    {
        if(connection.isEmpty())
            mConnections.remove(connection.getHost());
        else
            mLive.add(connection);
        mActive = null;
        boolean ret;
        if(mConnections.isEmpty())
        {
            if(mWaiting)
            {
                notify();
                mWaiting = false;
            }
            ret = false;
        } else
        {
            startRequests();
            ret = true;
        }
        return ret;
    }

    synchronized void dump()
    {
        Log.v("http", "dump()");
        StringBuffer dump = new StringBuffer();
        int count = 0;
        if(mActive != null)
            dump.append((new StringBuilder()).append("a ").append(mActive).append("\n").toString());
        if(!mHighPriority.isEmpty())
        {
            Connection connection;
            for(Iterator iter = mHighPriority.iterator(); iter.hasNext(); dump.append((new StringBuilder()).append("h").append(count++).append(" ").append(connection).append("\n").toString()))
                connection = (Connection)iter.next();

        }
        if(!mLive.isEmpty())
        {
            Connection connection;
            for(Iterator iter = mLive.iterator(); iter.hasNext(); dump.append((new StringBuilder()).append("l").append(count++).append(" ").append(connection).append("\n").toString()))
                connection = (Connection)iter.next();

        }
        if(!mPending.isEmpty())
        {
            Connection connection;
            for(Iterator iter = mPending.iterator(); iter.hasNext(); dump.append((new StringBuilder()).append("p").append(count++).append(" ").append(connection).append("\n").toString()))
                connection = (Connection)iter.next();

        }
        Log.v("http", dump.toString());
    }

    public Context getContext()
    {
        return mContext;
    }

    private static final String LOGTAG = "http";
    private Context mContext;
    private HashMap mConnections;
    private TreeSet mHighPriority;
    private TreeSet mLive;
    private TreeSet mPending;
    private boolean mWaiting;
    private Connection mActive;
    private HttpHost mProxyHost;

}

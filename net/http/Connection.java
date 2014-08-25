// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Connection.java

package android.net.http;

import android.os.SystemClock;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import javax.net.ssl.SSLException;
import org.apache.http.*;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpExecutionContext;

// Referenced classes of package android.net.http:
//            HttpConnection, HttpsConnection, ConnectionThread, Request, 
//            RequestQueue, EventHandler

abstract class Connection
{
    private static class IdleCache
    {

        private synchronized void cacheConnection(HttpHost host, HttpClientConnection connection)
        {
            if(mIdleCache.containsKey(host))
            {
                return;
            } else
            {
                mIdleCache.put(host, connection);
                return;
            }
        }

        private synchronized HttpClientConnection getConnection(HttpHost host)
        {
            if(mIdleCache.containsKey(host))
                return (HttpClientConnection)mIdleCache.remove(host);
            else
                return null;
        }

        private synchronized void removeConnection(HttpHost host)
        {
            if(mIdleCache.containsKey(host))
            {
                HttpClientConnection dead = (HttpClientConnection)mIdleCache.remove(host);
                try
                {
                    dead.close();
                }
                catch(IOException e) { }
            }
        }

        private synchronized void clear()
        {
            mIdleCache.clear();
        }

        private final int IDLE_CACHE_MAX = 8;
        private final LinkedHashMap mIdleCache;





        private IdleCache()
        {
            mIdleCache = new LinkedHashMap(16, 0.75F, true) {

                protected boolean removeEldestEntry(java.util.Map.Entry eldest)
                {
                    return size() == 8;
                }

                final IdleCache this$0;

                
                {
                    this$0 = IdleCache.this;
                    super(x0, x1, x2);
                }
            };
        }

    }


    protected Connection(RequestQueue requestQueue, HttpHost host)
    {
        mHttpClientConnection = null;
        mActive = STATE_NORMAL;
        MIN_PIPE = 2;
        MAX_PIPE = 4;
        synchronized(android/net/http/Connection)
        {
            mId = sId++;
        }
        init(requestQueue, host);
    }

    HttpHost getHost()
    {
        return mHost;
    }

    static Connection getConnection(RequestQueue requestQueue, HttpHost host)
    {
        RequestQueue requestqueue = requestQueue;
        JVM INSTR monitorenter ;
        if(host.getSchemeName().equals("http"))
            return new HttpConnection(requestQueue, host);
        new HttpsConnection(requestQueue, host);
        requestqueue;
        JVM INSTR monitorexit ;
        return;
        Exception exception;
        exception;
        throw exception;
    }

    void cancel()
    {
        mActive = STATE_CANCEL_REQUESTED;
        if(mHttpClientConnection != null && mHttpClientConnection.isOpen())
            closeConnection();
    }

    private void init(RequestQueue requestQueue, HttpHost host)
    {
        mRequestQueue = requestQueue;
        mHost = host;
        mCanPersist = false;
        mHttpContext = new HttpExecutionContext(null);
        mRequests = new LinkedList();
    }

    void queueRequest(Request request)
    {
        synchronized(mRequests)
        {
            if(request.mHighPriority)
                mRequests.add(0, request);
            else
                mRequests.add(request);
        }
    }

    void start()
    {
        mConnectionThread = new ConnectionThread(this);
        mConnectionThread.start();
    }

    boolean isEmpty()
    {
        LinkedList linkedlist = mRequests;
        JVM INSTR monitorenter ;
        return mRequests.isEmpty();
        Exception exception;
        exception;
        throw exception;
    }

    void processRequests()
    {
        Request req;
        LinkedList pipe;
        int minPipe;
        int maxPipe;
        int state;
        req = null;
        pipe = new LinkedList();
        minPipe = MIN_PIPE;
        maxPipe = MAX_PIPE;
        int SEND = 0;
        int READ = 1;
        int DRAIN = 2;
        int PAUSE = 3;
        int DONE = 4;
        String states[] = {
            "SEND", "READ", "DRAIN", "PAUSE", "DONE"
        };
        state = 0;
_L7:
        if(state == 4)
            break; /* Loop/switch isn't completed */
        state;
        JVM INSTR tableswitch 0 3: default 814
    //                   0 108
    //                   1 478
    //                   2 478
    //                   3 762;
           goto _L1 _L2 _L3 _L3 _L4
_L1:
        continue; /* Loop/switch isn't completed */
_L2:
        LinkedList linkedlist = mRequests;
        JVM INSTR monitorenter ;
        if(mRequests.isEmpty() || pipe.size() == maxPipe)
        {
            state = 1;
            continue; /* Loop/switch isn't completed */
        }
        req = (Request)mRequests.removeFirst();
        linkedlist;
        JVM INSTR monitorexit ;
          goto _L5
        Exception exception;
        exception;
        throw exception;
_L5:
        req;
        if(req.mState != 0)
            continue; /* Loop/switch isn't completed */
        if(mActive != STATE_NORMAL)
        {
            if(mActive == STATE_CANCEL_REQUESTED)
            {
                try
                {
                    Thread.sleep(100L);
                }
                catch(InterruptedException x) { }
                mActive = STATE_CANCELLED;
            }
            continue; /* Loop/switch isn't completed */
        }
        if(!req.mHighPriority && mRequestQueue.highPriorityPending())
        {
            synchronized(mRequests)
            {
                mRequests.addFirst(req);
            }
            state = pipe.isEmpty() ? 4 : 2;
            continue; /* Loop/switch isn't completed */
        }
        if((mHttpClientConnection == null || !mHttpClientConnection.isOpen()) && !openHttpConnection(req))
        {
            if(req.mFailCount < 2)
                synchronized(mRequests)
                {
                    mRequests.addFirst(req);
                }
            continue; /* Loop/switch isn't completed */
        }
        try
        {
            req.sendRequest(mHttpClientConnection);
        }
        catch(Exception e)
        {
            if(httpFailure(req, 16, e))
            {
                req;
                req.mState = 0;
                pipe.addLast(req);
            }
            boolean empty;
            synchronized(mRequests)
            {
                for(; !pipe.isEmpty(); mRequests.addFirst((Request)pipe.removeLast()));
                empty = mRequests.isEmpty();
            }
            state = empty ? 4 : 0;
            continue; /* Loop/switch isn't completed */
        }
        pipe.addLast(req);
        if(!mCanPersist)
            state = 1;
        continue; /* Loop/switch isn't completed */
_L3:
        boolean empty;
        synchronized(mRequests)
        {
            empty = mRequests.isEmpty();
        }
        int pipeSize = pipe.size();
        if(state != 2 && pipeSize <= minPipe && !empty && mCanPersist)
        {
            state = 0;
            continue; /* Loop/switch isn't completed */
        }
        if(pipeSize == 0)
        {
            if(empty)
                state = mRequestQueue.requestsPending() ? 4 : 3;
            else
                state = 0;
            continue; /* Loop/switch isn't completed */
        }
        req = (Request)pipe.removeFirst();
        try
        {
            req.readResponse(mHttpClientConnection);
        }
        catch(Exception e)
        {
            if(httpFailure(req, 16, e))
            {
                req;
                req.mState = 0;
                pipe.addFirst(req);
            }
            mCanPersist = false;
        }
        if(mCanPersist)
            continue; /* Loop/switch isn't completed */
        if(mHttpClientConnection != null && mHttpClientConnection.isOpen())
        {
            closeConnection();
            sIdleCache.removeConnection(getHost());
        }
        mHttpContext.removeAttribute("http.connection");
        synchronized(mRequests)
        {
            Request tReq;
            for(; !pipe.isEmpty(); mRequests.addFirst(tReq))
            {
                tReq = (Request)pipe.removeLast();
                req;
                tReq.mState = 0;
            }

        }
        state = state != 1 && !req.mHighPriority ? 4 : 0;
        continue; /* Loop/switch isn't completed */
_L4:
        try
        {
            Thread.sleep(100L);
        }
        catch(InterruptedException x) { }
        boolean empty;
        synchronized(mRequests)
        {
            empty = mRequests.isEmpty();
        }
        state = empty ? 4 : 0;
        if(true) goto _L7; else goto _L6
_L6:
        boolean moreWork = mRequestQueue.connectionCompleted(this);
        if(!moreWork)
        {
            sIdleCache.clear();
        } else
        {
            boolean empty;
            synchronized(mRequests)
            {
                empty = mRequests.isEmpty();
            }
            if(empty && mHttpClientConnection != null && mHttpClientConnection.isOpen())
            {
                mHttpContext.removeAttribute("http.connection");
                sIdleCache.cacheConnection(getHost(), mHttpClientConnection);
            }
        }
        return;
    }

    private boolean openHttpConnection(Request req)
    {
        boolean okay = true;
        if((mHttpClientConnection = sIdleCache.getConnection(getHost())) != null)
            return true;
        long now = SystemClock.uptimeMillis();
        try
        {
            mHttpClientConnection = openConnection(req);
            if(mHttpClientConnection != null)
                mHttpContext.setAttribute("http.connection", mHttpClientConnection);
            else
                okay = false;
        }
        catch(UnknownHostException e)
        {
            httpFailure(req, 32, e);
            okay = false;
        }
        catch(SSLException e)
        {
            if(req.mFailCount < 2)
                req.mFailCount = 2;
            okay = false;
        }
        catch(IOException e)
        {
            httpFailure(req, 80, e);
            okay = false;
        }
        long now2 = SystemClock.uptimeMillis();
        return okay;
    }

    private boolean httpFailure(Request req, int errorId, Exception e)
    {
        sIdleCache.removeConnection(getHost());
        if(req.mFailCount > 0)
        {
            Throwable cause = e.getCause();
            String error = cause == null ? e.getMessage() : cause.toString();
            req.mEventHandler.error(errorId, error);
        }
        if(mHttpClientConnection != null && mHttpClientConnection.isOpen())
        {
            closeConnection();
            mHttpContext.removeAttribute("http.connection");
        }
        return ++req.mFailCount < 2;
    }

    HttpContext getHttpContext()
    {
        return mHttpContext;
    }

    void setCanPersist(HttpResponse response)
    {
        mCanPersist = mConnectionReuseStrategy.keepAlive(response, mHttpContext);
    }

    abstract String getScheme();

    abstract void closeConnection();

    abstract HttpClientConnection openConnection(Request request)
        throws IOException;

    public synchronized String toString()
    {
        LinkedList linkedlist = mRequests;
        JVM INSTR monitorenter ;
        StringBuffer line;
        ListIterator iter = mRequests.listIterator(0);
        line = new StringBuffer((new StringBuilder()).append(mHost.getHostName()).append(" ").toString());
        Request request;
        for(; iter.hasNext(); line.append((new StringBuilder()).append(request).append(" ").toString()))
            request = (Request)iter.next();

        return line.toString();
        Exception exception;
        exception;
        throw exception;
    }

    byte[] getBuf()
    {
        if(mBuf == null)
            mBuf = new byte[8192];
        return mBuf;
    }

    protected static final String LOGTAG = "http";
    static final int KEEPALIVE_TIMEOUT = 60000;
    RequestQueue mRequestQueue;
    protected HttpClientConnection mHttpClientConnection;
    HttpHost mHost;
    private boolean mCanPersist;
    ConnectionThread mConnectionThread;
    private HttpContext mHttpContext;
    private static int STATE_NORMAL = 0;
    private static int STATE_CANCEL_REQUESTED = 1;
    private static int STATE_CANCELLED = 2;
    private int mActive;
    private static final int RETRY_REQUEST_LIMIT = 2;
    private static int sId = 0;
    final int mId;
    private static ConnectionReuseStrategy mConnectionReuseStrategy = new DefaultConnectionReuseStrategy();
    private int MIN_PIPE;
    private int MAX_PIPE;
    private LinkedList mRequests;
    private static final IdleCache sIdleCache = new IdleCache();
    private byte mBuf[];
    static Comparator sRankComparator = new Comparator() {

        public int compare(Object o1, Object o2)
        {
            Connection c1 = (Connection)o1;
            Connection c2 = (Connection)o2;
            return c1.mId <= c2.mId ? c1.mId >= c2.mId ? 0 : -1 : 1;
        }

    };

}

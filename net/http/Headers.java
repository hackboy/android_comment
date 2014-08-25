// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Headers.java

package android.net.http;

import java.util.HashMap;
import java.util.Iterator;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public class Headers
{
    public class HeaderIterator
        implements Iterator
    {

        public boolean hasNext()
        {
            return mNext < Headers.sKeywords.length && mHeaders[mNext] != null;
        }

        public Header next()
        {
            if(mNext < Headers.sKeywords.length)
            {
                mIndex = mNext;
                findNext();
                return mHeaders[mIndex];
            } else
            {
                return null;
            }
        }

        public void remove()
        {
            mHeaders[mIndex] = null;
        }

        private void findNext()
        {
            int length = Headers.sKeywords.length;
            int i;
            for(i = mNext + 1; i < length && mHeaders[i] == null; i++);
            mNext = i;
        }

        public volatile Object next()
        {
            return next();
        }

        int mIndex;
        int mNext;
        final Headers this$0;

        HeaderIterator()
        {
            this$0 = Headers.this;
            super();
            mIndex = -1;
            mNext = -1;
            findNext();
        }
    }

    public static interface SpecialHandler
    {

        public abstract void setCookie(String s);
    }


    public Headers()
    {
        mHeaders = new Header[sKeywords.length];
        synchronized(android/net/http/Headers)
        {
            if(sKeywordMap == null)
            {
                int keywordCount = sKeywords.length;
                sKeywordMap = new HashMap(keywordCount);
                for(int i = 0; i < keywordCount; i++)
                    sKeywordMap.put(sKeywords[i], Integer.valueOf(i));

            }
        }
    }

    public void load(Iterator headerIterator, SpecialHandler specialHandler)
    {
        do
        {
            if(!headerIterator.hasNext())
                break;
            Header header = (Header)headerIterator.next();
            String headerKey = header.getName().toLowerCase();
            if(sKeywordMap.containsKey(headerKey))
                mHeaders[((Integer)sKeywordMap.get(headerKey)).intValue()] = header;
            else
            if(headerKey.equals("set-cookie"))
                specialHandler.setCookie(header.getValue());
        } while(true);
    }

    public String getValue(int idx)
    {
        Header header = mHeaders[idx];
        return header != null ? header.getValue() : null;
    }

    public static String getHeaderName(int idx)
    {
        return sKeywords[idx];
    }

    public void set(int idx, String value)
    {
        mHeaders[idx] = new BasicHeader(sKeywords[idx], value);
    }

    public String toString()
    {
        StringBuffer dump = new StringBuffer();
        for(int i = 0; i < sKeywords.length; i++)
        {
            Header header = mHeaders[i];
            if(header != null)
                dump.append((new StringBuilder()).append(sKeywords[i]).append(": ").append(mHeaders[i].getValue()).append("\n").toString());
        }

        return dump.toString();
    }

    public HeaderIterator iterator()
    {
        return new HeaderIterator();
    }

    public static final int CONTENT_TYPE = 0;
    public static final int CONTENT_LENGTH = 1;
    public static final int LOCATION = 2;
    public static final int WWW_AUTHENTICATE = 3;
    public static final int PROXY_AUTHENTICATE = 4;
    public static final int CONTENT_DISPOSITION = 5;
    public static final int ACCEPT_RANGES = 6;
    public static final int EXPIRES = 7;
    public static final int CACHE_CONTROL = 8;
    public static final int LAST_MODIFIED = 9;
    public static final int ETAG = 10;
    static final String sKeywords[] = {
        "content-type", "content-length", "location", "www-authenticate", "proxy-authenticate", "content-disposition", "accept-ranges", "expires", "cache-control", "last-modified", 
        "etag"
    };
    static HashMap sKeywordMap;
    final Header mHeaders[];
    static final String SET_COOKIE = "set-cookie";

}

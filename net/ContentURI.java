// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ContentURI.java

package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;

public class ContentURI
    implements Parcelable, Comparable
{

    public static ContentURI create(String str)
    {
        return new ContentURI(str);
        URISyntaxException e;
        e;
        throw (IllegalArgumentException)(new IllegalArgumentException(str)).initCause(e);
    }

    public static ContentURI create(String scheme, String ssp, String fragment)
    {
        return new ContentURI(scheme, ssp, fragment);
        URISyntaxException e;
        e;
        throw (IllegalArgumentException)(new IllegalArgumentException((new StringBuilder()).append(scheme).append(":").append(ssp).append("#").append(fragment).toString())).initCause(e);
    }

    public static ContentURI create(File file)
    {
        StringBuffer buffer;
        buffer = new StringBuffer("file://");
        String path = file.getPath();
        int length = path.length();
        for(int i = 0; i < length; i++)
        {
            char ch = path.charAt(i);
            if(ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9' || ch == '_' || ch == '/' || ch == '.')
            {
                buffer.append(ch);
                continue;
            }
            int value = ch;
            buffer.append('%');
            if(value <= 127)
            {
                buffer.append("0123456789ABCDEF".charAt((value & 0xf0) >> 4));
                buffer.append("0123456789ABCDEF".charAt(value & 0xf));
                continue;
            }
            if(value <= 2047)
            {
                int ch1 = value >> 6 | 0xc0;
                int ch2 = value & 0x3f | 0x80;
                buffer.append("0123456789ABCDEF".charAt((ch1 & 0xf0) >> 4));
                buffer.append("0123456789ABCDEF".charAt(ch1 & 0xf));
                buffer.append('%');
                buffer.append("0123456789ABCDEF".charAt((ch2 & 0xf0) >> 4));
                buffer.append("0123456789ABCDEF".charAt(ch2 & 0xf));
            } else
            {
                int ch1 = value >> 12 | 0xe0;
                int ch2 = value >> 6 & 0x3f | 0x80;
                int ch3 = value & 0x3f | 0x80;
                buffer.append("0123456789ABCDEF".charAt((ch1 & 0xf0) >> 4));
                buffer.append("0123456789ABCDEF".charAt(ch1 & 0xf));
                buffer.append('%');
                buffer.append("0123456789ABCDEF".charAt((ch2 & 0xf0) >> 4));
                buffer.append("0123456789ABCDEF".charAt(ch2 & 0xf));
                buffer.append('%');
                buffer.append("0123456789ABCDEF".charAt((ch3 & 0xf0) >> 4));
                buffer.append("0123456789ABCDEF".charAt(ch3 & 0xf));
            }
        }

        return new ContentURI(buffer.toString());
        URISyntaxException e;
        e;
        throw (IllegalArgumentException)(new IllegalArgumentException(file.toString())).initCause(e);
    }

    public ContentURI(String string)
        throws URISyntaxException
    {
        this(new URI(string));
    }

    public ContentURI(String scheme, String ssp, String fragment)
        throws URISyntaxException
    {
        this(new URI(scheme, ssp, fragment));
    }

    public ContentURI(URI uri)
    {
        mURI = uri;
        String path = mURI.getPath();
        if(path != null)
        {
            int i = 0;
            int length = path.length();
            int prevIndex;
            int index;
            for(prevIndex = 0; (index = path.indexOf('/', prevIndex)) >= 0; prevIndex = index + 1)
                if(prevIndex < index)
                    i++;

            if(prevIndex < length)
                i++;
            mPath = new String[i];
            i = 0;
            int index;
            for(prevIndex = 0; (index = path.indexOf('/', prevIndex)) >= 0; prevIndex = index + 1)
                if(prevIndex < index)
                {
                    mPath[i] = path.substring(prevIndex, index);
                    i++;
                }

            if(prevIndex < length)
                mPath[i] = path.substring(prevIndex);
        } else
        {
            mPath = null;
        }
    }

    public String getScheme()
    {
        return mURI.getScheme();
    }

    public String getSchemeSpecificPart()
    {
        return mURI.getSchemeSpecificPart();
    }

    public String getAuthority()
    {
        return mURI.getAuthority();
    }

    public String getUserInfo()
    {
        return mURI.getUserInfo();
    }

    public String getHost()
    {
        return mURI.getHost();
    }

    public int getPort()
    {
        return mURI.getPort();
    }

    public String getPath()
    {
        return mURI.getPath();
    }

    public int countPathSegments()
    {
        return mPath == null ? 0 : mPath.length;
    }

    public String getPathSegment(int index)
    {
        return mPath[index];
    }

    public String getPathLeaf()
    {
        return mPath == null ? null : mPath[mPath.length - 1];
    }

    public long getPathLeafId()
    {
        String leaf = getPathLeaf();
        return leaf == null ? -1L : Long.parseLong(leaf);
    }

    public String getQuery()
    {
        return mURI.getQuery();
    }

    public String getFragment()
    {
        return mURI.getFragment();
    }

    public ContentURI addPath(String path)
    {
        String scheme;
        String authority;
        String query;
        String fragment;
        scheme = mURI.getScheme();
        authority = mURI.getAuthority();
        String oldPath = mURI.getPath();
        query = mURI.getQuery();
        fragment = mURI.getFragment();
        if(oldPath != null)
            path = (new StringBuilder()).append(oldPath).append("/").append(path).toString();
        return new ContentURI(new URI(scheme, authority, path, query, fragment));
        URISyntaxException e;
        e;
        throw (IllegalArgumentException)(new IllegalArgumentException()).initCause(e);
    }

    public ContentURI addId(long id)
    {
        return addPath(Long.toString(id));
    }

    public URI toURI()
    {
        return mURI;
    }

    public void writeToParcel(Parcel out)
    {
        out.writeString(mURI.toString());
    }

    public static void writeToParcel(Parcel out, ContentURI uri)
    {
        out.writeString(uri == null ? null : uri.toString());
    }

    public boolean equals(Object obj)
    {
        if(!(obj instanceof ContentURI))
        {
            return false;
        } else
        {
            ContentURI uriObj = (ContentURI)obj;
            return mURI.equals(uriObj.mURI);
        }
    }

    public int hashCode()
    {
        return mURI.hashCode();
    }

    public int compareTo(Object obj)
        throws ClassCastException
    {
        return mURI.compareTo(((ContentURI)obj).mURI);
    }

    public String toString()
    {
        return mURI.toString();
    }

    public ContentURI addQueryParameter(String key, String value)
    {
        String scheme;
        String authority;
        String path;
        String fragment;
        String newQuery;
        scheme = mURI.getScheme();
        authority = mURI.getAuthority();
        path = mURI.getPath();
        String oldQuery = mURI.getQuery();
        fragment = mURI.getFragment();
        String parameter;
        try
        {
            parameter = (new StringBuilder()).append(URLEncoder.encode(key, "UTF-8")).append("=").append(URLEncoder.encode(value, "UTF-8")).toString();
        }
        catch(UnsupportedEncodingException e)
        {
            throw (IllegalArgumentException)(new IllegalArgumentException()).initCause(e);
        }
        newQuery = oldQuery;
        if(TextUtils.isEmpty(newQuery))
            newQuery = null;
        if(newQuery == null)
            newQuery = parameter;
        else
            newQuery = (new StringBuilder()).append(newQuery).append("&").append(parameter).toString();
        return new ContentURI(new URI(scheme, authority, path, newQuery, fragment));
        URISyntaxException e;
        e;
        throw (IllegalArgumentException)(new IllegalArgumentException()).initCause(e);
    }

    public List getQueryParameters(String key)
        throws URISyntaxException
    {
        String query = mURI.getQuery();
        if(TextUtils.isEmpty(query))
            return new ArrayList();
        if(mQueryParameters == null)
            parseQueryParameters(query);
        return (List)mQueryParameters.get(key);
    }

    public String getQueryParameter(String key, String defaultValue)
        throws URISyntaxException
    {
        List values = getQueryParameters(key);
        if(values == null)
            return defaultValue;
        else
            return values.size() <= 0 ? defaultValue : (String)values.get(0);
    }

    private void parseQueryParameters(String query)
        throws URISyntaxException
    {
        mQueryParameters = new HashMap();
        int length = query.length();
        int ampIndex;
        for(int index = 0; index < length; index = ampIndex + 1)
        {
            ampIndex = query.indexOf('&', index);
            if(ampIndex < 0)
                ampIndex = length;
            int equalsIndex = query.indexOf('=', index);
            if(equalsIndex < 0)
                throw new URISyntaxException(query, "no equals", index);
            String encodedKey = query.substring(index, equalsIndex);
            String encodedValue = query.substring(equalsIndex + 1, ampIndex);
            String key;
            try
            {
                key = URLDecoder.decode(encodedKey, "UTF-8");
            }
            catch(UnsupportedEncodingException e)
            {
                mQueryParameters = null;
                throw new URISyntaxException(query, "unable to decode key", index);
            }
            String value;
            try
            {
                value = URLDecoder.decode(encodedValue, "UTF-8");
            }
            catch(UnsupportedEncodingException e)
            {
                mQueryParameters = null;
                throw new URISyntaxException(query, "unable to decode value", equalsIndex + 1);
            }
            ArrayList values = (ArrayList)mQueryParameters.get(key);
            if(values == null)
            {
                values = new ArrayList();
                mQueryParameters.put(key, values);
            }
            values.add(value);
        }

    }

    private Map mQueryParameters;
    private final URI mURI;
    private final String mPath[];
    private static final String kHexDigits = "0123456789ABCDEF";
    public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

        public ContentURI createFromParcel(Parcel in)
        {
            String str = in.readString();
            return str == null ? null : ContentURI.create(str);
        }

        public ContentURI[] newArray(int size)
        {
            return new ContentURI[size];
        }

        public volatile Object[] newArray(int x0)
        {
            return newArray(x0);
        }

        public volatile Object createFromParcel(Parcel x0)
        {
            return createFromParcel(x0);
        }

    };

}

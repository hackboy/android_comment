// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WebAddress.java

package android.net;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Referenced classes of package android.net:
//            ParseException

public class WebAddress
{

    public WebAddress(String address)
        throws ParseException
    {
        if(address == null)
            throw new NullPointerException();
        mScheme = "";
        mHost = "";
        mPort = -1;
        mPath = "/";
        mAuthInfo = "";
        Matcher m = sAddressPattern.matcher(address);
        if(m.find(0))
        {
            String t = m.group(1);
            if(t != null)
                mScheme = t;
            t = m.group(2);
            if(t != null)
                mAuthInfo = t;
            t = m.group(3);
            if(t != null)
                mHost = t;
            t = m.group(4);
            if(t != null)
                try
                {
                    mPort = Integer.parseInt(t);
                }
                catch(NumberFormatException ex)
                {
                    throw new ParseException("Bad port");
                }
            t = m.group(5);
            if(t != null)
                mPath = t;
        } else
        {
            throw new ParseException("Bad address");
        }
        if(mPort == 443 && mScheme.equals(""))
            mScheme = "https";
        else
        if(mPort == -1)
            if(mScheme.equals("https"))
                mPort = 443;
            else
                mPort = 80;
        if(mScheme.equals(""))
            mScheme = "http";
    }

    public String toString()
    {
        String port = "";
        if(mPort != 443 && mScheme.equals("https") || mPort != 80 && mScheme.equals("http"))
            port = (new StringBuilder()).append(":").append(Integer.toString(mPort)).toString();
        String authInfo = "";
        if(mAuthInfo.length() > 0)
            authInfo = (new StringBuilder()).append(mAuthInfo).append("@").toString();
        return (new StringBuilder()).append(mScheme).append("://").append(authInfo).append(mHost).append(port).append(mPath).toString();
    }

    private static final String LOGTAG = "http";
    public String mScheme;
    public String mHost;
    public int mPort;
    public String mPath;
    public String mAuthInfo;
    static final int MATCH_GROUP_SCHEME = 1;
    static final int MATCH_GROUP_AUTHORITY = 2;
    static final int MATCH_GROUP_HOST = 3;
    static final int MATCH_GROUP_PORT = 4;
    static final int MATCH_GROUP_PATH = 5;
    static Pattern sAddressPattern = Pattern.compile("(?:(http|HTTP|https|HTTPS|file|FILE)\\:\\/\\/)?(?:([-A-Za-z0-9]+(?:\\:[-A-Za-z0-9]+)?)@)?([-A-Za-z0-9]+(?:\\.[-A-Za-z0-9]+)*)(?:\\:([0-9]+))?(\\/.*)?");

}

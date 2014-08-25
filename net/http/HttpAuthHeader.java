// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HttpAuthHeader.java

package android.net.http;


public class HttpAuthHeader
{

    public HttpAuthHeader(String header)
    {
        if(header != null)
            parseHeader(header);
    }

    public boolean isProxy()
    {
        return mIsProxy;
    }

    public void setProxy()
    {
        mIsProxy = true;
    }

    public String getUsername()
    {
        return mUsername;
    }

    public void setUsername(String username)
    {
        mUsername = username;
    }

    public String getPassword()
    {
        return mPassword;
    }

    public void setPassword(String password)
    {
        mPassword = password;
    }

    public boolean isBasic()
    {
        return mScheme == 1;
    }

    public boolean isDigest()
    {
        return mScheme == 2;
    }

    public int getScheme()
    {
        return mScheme;
    }

    public boolean getStale()
    {
        return mStale;
    }

    public String getRealm()
    {
        return mRealm;
    }

    public String getNonce()
    {
        return mNonce;
    }

    public String getOpaque()
    {
        return mOpaque;
    }

    public String getQop()
    {
        return mQop;
    }

    public String getAlgorithm()
    {
        return mAlgorithm;
    }

    public boolean isSupportedScheme()
    {
        if(mRealm != null)
        {
            if(mScheme == 1)
                return true;
            if(mScheme == 2)
                return mAlgorithm.equals("md5") && (mQop == null || mQop.equals("auth"));
        }
        return false;
    }

    private void parseHeader(String header)
    {
        if(header != null)
        {
            String parameters = parseScheme(header);
            if(parameters != null && mScheme != 0)
                parseParameters(parameters);
        }
    }

    private String parseScheme(String header)
    {
        if(header != null)
        {
            int i = header.indexOf(' ');
            if(i >= 0)
            {
                String scheme = header.substring(0, i).trim();
                if(scheme.equalsIgnoreCase("Digest"))
                {
                    mScheme = 2;
                    mAlgorithm = "md5";
                } else
                if(scheme.equalsIgnoreCase("Basic"))
                    mScheme = 1;
                return header.substring(i + 1);
            }
        }
        return null;
    }

    private void parseParameters(String parameters)
    {
        int i;
        if(parameters != null)
            do
            {
                i = parameters.indexOf(',');
                if(i < 0)
                {
                    parseParameter(parameters);
                } else
                {
                    parseParameter(parameters.substring(0, i));
                    parameters = parameters.substring(i + 1);
                }
            } while(i >= 0);
    }

    private void parseParameter(String parameter)
    {
        if(parameter != null)
        {
            int i = parameter.indexOf('=');
            if(i >= 0)
            {
                String token = parameter.substring(0, i).trim();
                String value = trimDoubleQuotesIfAny(parameter.substring(i + 1).trim());
                if(token.equalsIgnoreCase("realm"))
                    mRealm = value;
                else
                if(mScheme == 2)
                    parseParameter(token, value);
            }
        }
    }

    private void parseParameter(String token, String value)
    {
        if(token != null && value != null)
        {
            if(token.equalsIgnoreCase("nonce"))
            {
                mNonce = value;
                return;
            }
            if(token.equalsIgnoreCase("stale"))
            {
                parseStale(value);
                return;
            }
            if(token.equalsIgnoreCase("opaque"))
            {
                mOpaque = value;
                return;
            }
            if(token.equalsIgnoreCase("qop"))
            {
                mQop = value.toLowerCase();
                return;
            }
            if(token.equalsIgnoreCase("algorithm"))
            {
                mAlgorithm = value.toLowerCase();
                return;
            }
        }
    }

    private void parseStale(String value)
    {
        if(value != null && value.equalsIgnoreCase("true"))
            mStale = true;
    }

    private static String trimDoubleQuotesIfAny(String value)
    {
        if(value != null)
        {
            int len = value.length();
            if(len > 2 && value.charAt(0) == '"' && value.charAt(len - 1) == '"')
                return value.substring(1, len - 1);
        }
        return value;
    }

    public static final String BASIC_TOKEN = "Basic";
    public static final String DIGEST_TOKEN = "Digest";
    private static final String REALM_TOKEN = "realm";
    private static final String NONCE_TOKEN = "nonce";
    private static final String STALE_TOKEN = "stale";
    private static final String OPAQUE_TOKEN = "opaque";
    private static final String QOP_TOKEN = "qop";
    private static final String ALGORITHM_TOKEN = "algorithm";
    private static final String LOGTAG = "webkit";
    private int mScheme;
    public static final int UNKNOWN = 0;
    public static final int BASIC = 1;
    public static final int DIGEST = 2;
    private boolean mStale;
    private String mRealm;
    private String mNonce;
    private String mOpaque;
    private String mQop;
    private String mAlgorithm;
    private boolean mIsProxy;
    private String mUsername;
    private String mPassword;
}

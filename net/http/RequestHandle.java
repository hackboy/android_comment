// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RequestHandle.java

package android.net.http;

import android.security.Md5MessageDigest;
import android.test.Assert;
import android.webkit.CookieManager;
import java.io.InputStream;
import java.util.*;
import org.apache.commons.codec.binary.Base64;

// Referenced classes of package android.net.http:
//            Request, RequestQueue

public class RequestHandle
{

    public RequestHandle(RequestQueue requestQueue, String url, String method, Map headers, InputStream bodyProvider, int bodyLength, Request request)
    {
        mRedirectCount = 0;
        if(headers == null)
            headers = new HashMap();
        mHeaders = headers;
        mBodyProvider = bodyProvider;
        mBodyLength = bodyLength;
        mMethod = method == null ? "GET" : method;
        mUrl = url;
        mRequestQueue = requestQueue;
        mRequest = request;
    }

    public void cancel()
    {
        if(mRequest != null)
            mRequest.cancel();
    }

    public void handleSslErrorResponse(boolean proceed)
    {
        if(mRequest != null)
            mRequest.handleSslErrorResponse(proceed);
    }

    public boolean isRedirectMax()
    {
        return mRedirectCount == 16;
    }

    public boolean setupRedirect(String redirectTo, int statusCode)
    {
        if(mRedirectCount++ == 16)
            return false;
        mUrl = redirectTo;
        mHeaders.remove("cookie");
        String cookie = CookieManager.getInstance().getCookie(redirectTo);
        if(cookie != null && cookie.length() > 0)
            mHeaders.put("cookie", cookie);
        if((statusCode == 302 || statusCode == 303) && mMethod.equals("POST"))
            mMethod = "GET";
        mHeaders.remove("Content-Type");
        mBodyProvider = null;
        if(mUrl.startsWith("https:") && redirectTo.startsWith("http:"))
            mHeaders.remove("Referer");
        createAndQueueNewRequest();
        return true;
    }

    public void setupBasicAuthResponse(boolean isProxy, String username, String password)
    {
        String response = computeBasicAuthResponse(username, password);
        mBodyProvider = null;
        mHeaders.put(authorizationHeader(isProxy), (new StringBuilder()).append("Basic ").append(response).toString());
        createAndQueueNewRequest();
    }

    public void setupDigestAuthResponse(boolean isProxy, String username, String password, String realm, String nonce, String QOP, String algorithm, 
            String opaque)
    {
        String response = computeDigestAuthResponse(username, password, realm, nonce, QOP, algorithm, opaque);
        mBodyProvider = null;
        mHeaders.put(authorizationHeader(isProxy), (new StringBuilder()).append("Digest ").append(response).toString());
        createAndQueueNewRequest();
    }

    public String getMethod()
    {
        return mMethod;
    }

    public static String computeBasicAuthResponse(String username, String password)
    {
        Assert.assertNotNull(username);
        Assert.assertNotNull(password);
        return new String(Base64.encodeBase64((new StringBuilder()).append(username).append(':').append(password).toString().getBytes()));
    }

    private String computeDigestAuthResponse(String username, String password, String realm, String nonce, String QOP, String algorithm, String opaque)
    {
        Assert.assertNotNull(username);
        Assert.assertNotNull(password);
        Assert.assertNotNull(realm);
        String A1 = (new StringBuilder()).append(username).append(":").append(realm).append(":").append(password).toString();
        String A2 = (new StringBuilder()).append(mMethod).append(":").append(mUrl).toString();
        String nc = "000001";
        String cnonce = computeCnonce();
        String digest = computeDigest(A1, A2, nonce, QOP, nc, cnonce);
        String response = "";
        response = (new StringBuilder()).append(response).append("username=").append(doubleQuote(username)).append(", ").toString();
        response = (new StringBuilder()).append(response).append("realm=").append(doubleQuote(realm)).append(", ").toString();
        response = (new StringBuilder()).append(response).append("nonce=").append(doubleQuote(nonce)).append(", ").toString();
        response = (new StringBuilder()).append(response).append("uri=").append(doubleQuote(mUrl)).append(", ").toString();
        response = (new StringBuilder()).append(response).append("response=").append(doubleQuote(digest)).toString();
        if(opaque != null)
            response = (new StringBuilder()).append(response).append(", opaque=").append(doubleQuote(opaque)).toString();
        if(algorithm != null)
            response = (new StringBuilder()).append(response).append(", algorithm=").append(algorithm).toString();
        if(QOP != null)
            response = (new StringBuilder()).append(response).append(", qop=").append(QOP).append(", nc=").append(nc).append(", cnonce=").append(doubleQuote(cnonce)).toString();
        return response;
    }

    public static String authorizationHeader(boolean isProxy)
    {
        if(!isProxy)
            return "Authorization";
        else
            return "Proxy-Authorization";
    }

    private String computeDigest(String A1, String A2, String nonce, String QOP, String nc, String cnonce)
    {
        if(QOP == null)
            return KD(H(A1), (new StringBuilder()).append(nonce).append(":").append(H(A2)).toString());
        if(QOP.equalsIgnoreCase("auth"))
            return KD(H(A1), (new StringBuilder()).append(nonce).append(":").append(nc).append(":").append(cnonce).append(":").append(QOP).append(":").append(H(A2)).toString());
        else
            return null;
    }

    private String KD(String secret, String data)
    {
        return H((new StringBuilder()).append(secret).append(":").append(data).toString());
    }

    private String H(String param)
    {
        if(param != null)
        {
            Md5MessageDigest md5 = new Md5MessageDigest();
            byte d[] = md5.digest(param.getBytes());
            if(d != null)
                return bufferToHex(d);
        }
        return null;
    }

    private String bufferToHex(byte buffer[])
    {
        char hexChars[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
            'a', 'b', 'c', 'd', 'e', 'f'
        };
        if(buffer != null)
        {
            int length = buffer.length;
            if(length > 0)
            {
                StringBuffer hex = new StringBuffer(2 * length);
                for(int i = 0; i < length; i++)
                {
                    byte l = (byte)(buffer[i] & 0xf);
                    byte h = (byte)((buffer[i] & 0xf0) >> 4);
                    hex.append(hexChars[h]);
                    hex.append(hexChars[l]);
                }

                return hex.toString();
            } else
            {
                return "";
            }
        } else
        {
            return null;
        }
    }

    private String computeCnonce()
    {
        Random rand = new Random();
        return Integer.toString(Math.abs(rand.nextInt()), 16);
    }

    private String doubleQuote(String param)
    {
        if(param != null)
            return (new StringBuilder()).append("\"").append(param).append("\"").toString();
        else
            return null;
    }

    private void createAndQueueNewRequest()
    {
        mRequest = mRequestQueue.queueRequest(mUrl, mMethod, mHeaders, mRequest.mEventHandler, mBodyProvider, mBodyLength, mRequest.mHighPriority).mRequest;
    }

    private String mUrl;
    private String mMethod;
    private Map mHeaders;
    private RequestQueue mRequestQueue;
    private Request mRequest;
    private InputStream mBodyProvider;
    private int mBodyLength;
    private int mRedirectCount;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String PROXY_AUTHORIZATION_HEADER = "Proxy-Authorization";
    private static final String LOGTAG = "http";
    private static final int MAX_REDIRECT_COUNT = 16;
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SslError.java

package android.net.http;


// Referenced classes of package android.net.http:
//            SslCertificate

public class SslError
{

    public SslError(int errors, String description, SslCertificate certificate)
    {
        mErrors = errors;
        mDescription = description;
        mCertificate = certificate;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public SslCertificate getCertificate()
    {
        return mCertificate;
    }

    public int getErrors()
    {
        return mErrors;
    }

    public int getPrimaryError()
    {
        return getPrimaryError(mErrors);
    }

    public static boolean isSslError(int error)
    {
        return (error & 0xf) != 0;
    }

    public static int getPrimaryError(int errors)
    {
        int primary = 0;
        if(isSslError(errors))
            if((errors & 8) != 0)
                primary = 8;
            else
            if((errors & 4) != 0)
                primary = 4;
            else
            if((errors & 2) != 0)
                primary = 2;
            else
                primary = 1;
        return primary;
    }

    public static final int SSL_NOTYETVALID = 1;
    public static final int SSL_EXPIRED = 2;
    public static final int SSL_IDMISMATCH = 4;
    public static final int SSL_UNTRUSTED = 8;
    public static final int SSL_FAILMASK = 15;
    private int mErrors;
    private String mDescription;
    private SslCertificate mCertificate;
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SslCertificate.java

package android.net.http;

import java.util.Vector;
import org.bouncycastle.asn1.x509.X509Name;

public class SslCertificate
{
    private class Name
    {

        public boolean initFromDName(String dName)
        {
            boolean rval = dName != null;
            if(rval)
            {
                X509Name xName = new X509Name(dName);
                rval = xName != null;
                if(rval)
                {
                    Vector val = xName.getValues();
                    Vector oid = xName.getOIDs();
                    for(int i = 0; i < oid.size(); i++)
                    {
                        if(oid.elementAt(i).equals(X509Name.CN))
                        {
                            mCName = (String)val.elementAt(i);
                            continue;
                        }
                        if(oid.elementAt(i).equals(X509Name.O))
                        {
                            mOName = (String)val.elementAt(i);
                            continue;
                        }
                        if(oid.elementAt(i).equals(X509Name.OU))
                            mUName = (String)val.elementAt(i);
                    }

                }
            }
            return rval;
        }

        public String getCName()
        {
            return mCName;
        }

        public String getOName()
        {
            return mOName;
        }

        public String getUName()
        {
            return mUName;
        }

        public boolean isEmpty()
        {
            return mCName.length() == 0 && mOName.length() == 0 && mUName.length() == 0;
        }

        public String toString()
        {
            return (new StringBuilder()).append(mCName).append("; ").append(mOName).append("; ").append(mUName).toString();
        }

        private String mCName;
        private String mOName;
        private String mUName;
        final SslCertificate this$0;

        public Name()
        {
            this$0 = SslCertificate.this;
            super();
        }
    }


    public SslCertificate()
    {
        mIssuedToName = new Name();
        mIssuedByName = new Name();
        mValidLifeBeg = "";
        mValidLifeEnd = "";
    }

    public SslCertificate(String issuedToName, String issuedByName, String validLifeBeg, String validLifeEnd)
    {
        this();
        mIssuedToName.initFromDName(issuedToName);
        mIssuedByName.initFromDName(issuedByName);
        if(validLifeBeg != null)
            mValidLifeBeg = validLifeBeg;
        if(validLifeEnd != null)
            mValidLifeEnd = validLifeEnd;
    }

    public String getValidLifeBeg()
    {
        return mValidLifeBeg;
    }

    public String getValidLifeEnd()
    {
        return mValidLifeEnd;
    }

    public String getIssuedToCName()
    {
        return mIssuedToName.getCName();
    }

    public String getIssuedByCName()
    {
        return mIssuedByName.getCName();
    }

    public String getIssuedToOName()
    {
        return mIssuedToName.getOName();
    }

    public String getIssuedByOName()
    {
        return mIssuedByName.getOName();
    }

    public String getIssuedToUName()
    {
        return mIssuedToName.getUName();
    }

    public String getIssuedByUName()
    {
        return mIssuedByName.getUName();
    }

    public boolean isEmpty()
    {
        return mValidLifeBeg.length() == 0 && mValidLifeEnd.length() == 0 && mIssuedToName.isEmpty() && mIssuedByName.isEmpty();
    }

    public String toString()
    {
        return (new StringBuilder()).append("Issued to: ").append(mIssuedToName).append(";\n").append("Issued by: ").append(mIssuedByName).append(";\n").toString();
    }

    private static final String LOGTAG = "http";
    private String mValidLifeBeg;
    private String mValidLifeEnd;
    private Name mIssuedToName;
    private Name mIssuedByName;
}

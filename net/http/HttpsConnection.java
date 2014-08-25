// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HttpsConnection.java

package android.net.http;

import android.test.Assert;
import java.io.IOException;
import java.security.*;
import java.security.cert.*;
import java.util.Date;
import java.util.Vector;
import javax.net.ssl.*;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.params.BasicHttpParams;
import org.bouncycastle.asn1.x509.X509Name;

// Referenced classes of package android.net.http:
//            Connection, SslCertificate, Request, EventHandler, 
//            RequestQueue

class HttpsConnection extends Connection
{

    HttpsConnection(RequestQueue requestQueue, HttpHost host)
    {
        super(requestQueue, host);
        mSuspendLock = new Object();
        mSuspended = false;
        mAborted = false;
    }

    HttpClientConnection openConnection(Request req)
        throws IOException
    {
        SSLSocket sslSock;
        java.security.cert.Certificate certs[];
        X509Certificate currentCert;
        X509Certificate previousCert;
        int validationError;
        String validationMessage;
        SSLSocketFactory factory;
        try
        {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, mTrustManager, null);
            factory = context.getSocketFactory();
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new IOException();
        }
        catch(KeyManagementException e)
        {
            throw new IOException();
        }
        sslSock = null;
        try
        {
            sslSock = (SSLSocket)(SSLSocket)factory.createSocket(mHost.getHostName(), mHost.getPort());
        }
        catch(IOException e)
        {
            if(sslSock != null)
                sslSock.close();
            throw e;
        }
        certs = null;
        try
        {
            sslSock.setUseClientMode(true);
            sslSock.startHandshake();
            certs = sslSock.getSession().getPeerCertificates();
        }
        catch(IOException e)
        {
            sslSock.close();
            throw e;
        }
        currentCert = null;
        previousCert = null;
        validationError = 0;
        validationMessage = "";
        Assert.assertNotNull(certs);
        if(mDefaultTrustManager != null && certs.length > 0)
        {
            try
            {
                X509Certificate c[] = {
                    (X509Certificate)(X509Certificate)certs[certs.length - 1]
                };
                mDefaultTrustManager.checkServerTrusted(c, "RSA");
            }
            catch(GeneralSecurityException e)
            {
                e.printStackTrace();
                validationError = 8;
                validationMessage = "Certificate untrusted";
            }
        } else
        {
            validationError = 8;
            validationMessage = "Certificate untrusted";
        }
        if(validationError != 0) goto _L2; else goto _L1
_L1:
        int i = certs.length - 1;
_L4:
        if(i < 0) goto _L2; else goto _L3
_L3:
        currentCert = (X509Certificate)(X509Certificate)certs[i];
        if(previousCert == null)
            break MISSING_BLOCK_LABEL_301;
        try
        {
            if(!previousCert.getSubjectDN().equals(currentCert.getIssuerDN()))
            {
                validationError = 8;
                validationMessage = "Certificate untrusted by chain";
                break; /* Loop/switch isn't completed */
            }
        }
        catch(GeneralSecurityException e)
        {
            validationError = 8;
            validationMessage = "Certificate key verification failed.";
            break; /* Loop/switch isn't completed */
        }
        currentCert.verify(previousCert.getPublicKey());
        if(validationError < 2)
            try
            {
                currentCert.checkValidity();
            }
            catch(CertificateExpiredException e)
            {
                validationError = 2;
                validationMessage = "Certificate expired";
            }
            catch(CertificateNotYetValidException e)
            {
                validationError = 1;
                validationMessage = "Certificate not yet valid";
            }
        previousCert = currentCert;
        i--;
          goto _L4
_L2:
        String certHost = null;
        if(currentCert != null)
        {
            X509Name xName = new X509Name(currentCert.getSubjectDN().getName());
            Vector val = xName.getValues();
            Vector oid = xName.getOIDs();
            int i = 0;
            do
            {
                if(i >= oid.size())
                    break;
                if(oid.elementAt(i).equals(X509Name.CN))
                {
                    certHost = (String)(String)val.elementAt(i);
                    break;
                }
                i++;
            } while(true);
        }
        if(validationError < 4 && !hostnameMatch(certHost))
        {
            validationError = 4;
            validationMessage = "Certificate not for this host";
        }
        if(validationError > 0)
        {
            EventHandler eh = req.getEventHandler();
            SslCertificate sc = new SslCertificate();
            if(currentCert != null)
                sc = new SslCertificate(currentCert.getSubjectDN().getName(), currentCert.getIssuerDN().getName(), currentCert.getNotBefore().toString(), currentCert.getNotAfter().toString());
            synchronized(mSuspendLock)
            {
                mSuspended = true;
            }
            eh.handleSslErrorRequest(validationError, validationMessage, sc);
            synchronized(mSuspendLock)
            {
                if(mSuspended)
                    try
                    {
                        mSuspendLock.wait();
                    }
                    catch(InterruptedException e) { }
                if(mAborted)
                {
                    sslSock.close();
                    throw new SSLException("connection closed by the user");
                }
            }
        }
        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
        conn.bind(sslSock, new BasicHttpParams(null));
        return conn;
    }

    private boolean hostnameMatch(String onRecord)
    {
        String hostname = mHost == null ? null : mHost.getHostName();
        boolean rval = hostname != null && onRecord != null;
        if(rval)
        {
            rval = hostname.equalsIgnoreCase(onRecord);
            if(!rval)
            {
                int i = hostname.indexOf(".");
                int j = onRecord.indexOf("*");
                if(i > 0 && j >= 0)
                {
                    String hostnameSuffix = hostname.substring(i);
                    String onRecordSuffix = onRecord.substring(j + 1);
                    rval = hostnameSuffix.equalsIgnoreCase(onRecordSuffix);
                }
            }
        }
        return rval;
    }

    void closeConnection()
    {
        try
        {
            mHttpClientConnection.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    void restartConnection(boolean proceed)
    {
        synchronized(mSuspendLock)
        {
            if(mSuspended)
            {
                mSuspended = false;
                mAborted = !proceed;
                mSuspendLock.notify();
            }
        }
    }

    String getScheme()
    {
        return "https";
    }

    static X509TrustManager mDefaultTrustManager;
    private Object mSuspendLock;
    private boolean mSuspended;
    private boolean mAborted;
    private static TrustManager mTrustManager[] = {
        new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }

            public void checkClientTrusted(X509Certificate ax509certificate[], String s)
            {
            }

            public void checkServerTrusted(X509Certificate ax509certificate[], String s)
            {
            }

        }
    };

    static 
    {
        try
        {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init((KeyStore)null);
            TrustManager tms[] = tmf.getTrustManagers();
            if(tms != null)
            {
                TrustManager arr$[] = tms;
                int len$ = arr$.length;
                int i$ = 0;
                do
                {
                    if(i$ >= len$)
                        break;
                    TrustManager tm = arr$[i$];
                    if(tm instanceof X509TrustManager)
                    {
                        mDefaultTrustManager = (X509TrustManager)tm;
                        break;
                    }
                    i$++;
                } while(true);
            }
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
    }
}

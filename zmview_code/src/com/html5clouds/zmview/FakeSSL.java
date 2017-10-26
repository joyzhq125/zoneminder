package com.html5clouds.zmview;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class FakeSSL
  implements X509TrustManager
{
  private static TrustManager[] trustManagers;
  private final X509Certificate[] _AcceptedIssuers = new X509Certificate[0];

  public static void allowAllSSL()
  {
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
    {
      public boolean verify(String paramAnonymousString, SSLSession paramAnonymousSSLSession)
      {
        return false;
      }
    });
    SSLContext localSSLContext = null;
    if (trustManagers == null)
    {
      TrustManager[] arrayOfTrustManager = new TrustManager[1];
      arrayOfTrustManager[0] = new FakeSSL();
      trustManagers = arrayOfTrustManager;
    }
    try
    {
      localSSLContext = SSLContext.getInstance("TLS");
      localSSLContext.init(null, trustManagers, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(localSSLContext.getSocketFactory());
      return;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      while (true)
        localNoSuchAlgorithmException.printStackTrace();
    }
    catch (KeyManagementException localKeyManagementException)
    {
      while (true)
        localKeyManagementException.printStackTrace();
    }
  }

  public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    throws CertificateException
  {
  }

  public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    throws CertificateException
  {
  }

  public X509Certificate[] getAcceptedIssuers()
  {
    return this._AcceptedIssuers;
  }

  public boolean isClientTrusted(X509Certificate[] paramArrayOfX509Certificate)
  {
    return true;
  }

  public boolean isServerTrusted(X509Certificate[] paramArrayOfX509Certificate)
  {
    return true;
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.FakeSSL
 * JD-Core Version:    0.6.2
 */
package com.html5clouds.zmview;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Base64;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetDataService extends Service
{
  public static final String BIND = "zmview.BIND";
  public static final String FIRST = "zmview.FIRST";
  private final IBinder binder = new ServiceDiscoveryBinder();
  private Boolean debug = Boolean.valueOf(false);
  private ArrayList<ZmCamera> myZmCamera;
  private ArrayList<zmEvents> myZmEvents;
  private SharedPreferences prefs;
  private HttpURLConnection uc;
  private HttpsURLConnection ucs;
  private String zm_host;
  private String zm_pass;
  private String zm_user;

  public ArrayList<zmEvents> getEvents(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    this.myZmEvents = new ArrayList();
    this.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    this.zm_host = this.prefs.getString("zm_host", null);
    this.zm_user = this.prefs.getString("zm_user", null);
    this.zm_pass = this.prefs.getString("zm_pass", null);
    String str = "&skin=mobile&view=events&page=" + String.valueOf(1 + Integer.valueOf(paramString2).intValue()) + "&filter[terms][0][attr]=DateTime" + "&filter[terms][0][op]=%3E%3D&filter[terms][0][val]=" + paramString4 + "&filter[terms][1][cnj]=and" + "&filter[terms][1][attr]=MonitorId&filter[terms][1][op]=%3D" + "&filter[terms][1][val]=" + paramString1 + "&" + paramString3;
    if (this.debug.booleanValue())
      System.out.println("ZmView url=" + this.zm_host + "/index.php?" + str);
    if (open_web_connection(this.zm_host + "/index.php?" + str).booleanValue())
      try
      {
        zmEvents localzmEvents = new zmEvents();
        Document localDocument;
        int i = 0;
        Iterator localIterator = null;
        if (this.ucs == null)
        {
          localDocument = Jsoup.parse(this.uc.getInputStream(), "UTF-8", this.zm_host);
          Elements localElements1 = localDocument.select("td");
          i = 0;
          localIterator = localElements1.iterator();
        }
        else
        {
            if (!localIterator.hasNext())
            {              
                localDocument = Jsoup.parse(this.ucs.getInputStream(), "UTF-8", this.zm_host);
            }
            else
            {
                Element localElement = (Element)localIterator.next();
                if (i == 0)
                  localzmEvents = new zmEvents();
                Elements localElements2 = localElement.getElementsByClass("colId");
                Elements localElements3 = localElement.getElementsByClass("colTime");
                Elements localElements4 = localElement.getElementsByClass("colDuration");
                Elements localElements5 = localElement.getElementsByClass("colFrames");
                Elements localElements6 = localElement.getElementsByClass("colScore");
                if (localElements2.hasText())
                {
                  i = 1;
                  localzmEvents.setID(localElements2.get(0).text());
                }
                if (localElements3.hasText())
                {
                  localzmEvents.setTime(localElements3.get(0).text());
                  if (this.debug.booleanValue())
                    System.out.println("ZmView : add event " + localElements3.get(0).text());
                }
                if (localElements4.hasText())
                  localzmEvents.setDuration(localElements4.get(0).text());
                if (localElements5.hasText())
                  localzmEvents.setFrames(localElements5.get(0).text());
                if (localElements6.hasText())
                {
                  localzmEvents.setScore(localElements6.get(0).text());
                  i = 2;
                }
                if (i == 2)
                {
                  this.myZmEvents.add(localzmEvents);
                  i = 0;
                }   
               return this.myZmEvents; 
            }
            
        }
        /*
        while (true)
        {
          if (!localIterator.hasNext())
          {
            return this.myZmEvents;
            localDocument = Jsoup.parse(this.ucs.getInputStream(), "UTF-8", this.zm_host);
            break;
          }
          Element localElement = (Element)localIterator.next();
          if (i == 0)
            localzmEvents = new zmEvents();
          Elements localElements2 = localElement.getElementsByClass("colId");
          Elements localElements3 = localElement.getElementsByClass("colTime");
          Elements localElements4 = localElement.getElementsByClass("colDuration");
          Elements localElements5 = localElement.getElementsByClass("colFrames");
          Elements localElements6 = localElement.getElementsByClass("colScore");
          if (localElements2.hasText())
          {
            i = 1;
            localzmEvents.setID(localElements2.get(0).text());
          }
          if (localElements3.hasText())
          {
            localzmEvents.setTime(localElements3.get(0).text());
            if (this.debug.booleanValue())
              System.out.println("ZmView : add event " + localElements3.get(0).text());
          }
          if (localElements4.hasText())
            localzmEvents.setDuration(localElements4.get(0).text());
          if (localElements5.hasText())
            localzmEvents.setFrames(localElements5.get(0).text());
          if (localElements6.hasText())
          {
            localzmEvents.setScore(localElements6.get(0).text());
            i = 2;
          }
          if (i == 2)
          {
            this.myZmEvents.add(localzmEvents);
            i = 0;
          }
        } */
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
    return null;
  }

  public IBinder onBind(Intent paramIntent)
  {
    return this.binder;
  }

  public void onCreate()
  {
    this.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    this.zm_host = this.prefs.getString("zm_host", null);
    this.zm_user = this.prefs.getString("zm_user", null);
    this.zm_pass = this.prefs.getString("zm_pass", null);
  }

  public void onStart(Intent paramIntent, int paramInt)
  {
    super.onStart(paramIntent, paramInt);
    if (this.debug.booleanValue())
      System.out.println("ZmView GetDataService is started");
  }

  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    "zmview.FIRST".equals(paramIntent.getAction());
    return 2;
  }

  public Boolean open_web_connection(String paramString)
  {
    this.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    this.zm_host = this.prefs.getString("zm_host", null);
    this.zm_user = this.prefs.getString("zm_user", null);
    this.zm_pass = this.prefs.getString("zm_pass", null);
    try
    {
      URL localURL = new URL(paramString + "&username=" + URLEncoder.encode(this.zm_user, "UTF-8") + "&password=" + URLEncoder.encode(this.zm_pass, "UTF-8") + "&action=login");
      if (paramString.contains("https:"))
      {
        new FakeSSL();
        FakeSSL.allowAllSSL();
        this.ucs = ((HttpsURLConnection)localURL.openConnection());
        this.ucs.setConnectTimeout(60000);
        this.ucs.setRequestProperty("Authorization", "Basic " + Base64.encodeToString(new StringBuilder(String.valueOf(this.zm_user)).append(":").append(this.zm_pass).toString().getBytes(), 0).trim());
        this.ucs.setHostnameVerifier(new X509HostnameVerifier()
        {
          public void verify(String paramAnonymousString, X509Certificate paramAnonymousX509Certificate)
            throws SSLException
          {
          }

          public void verify(String paramAnonymousString, SSLSocket paramAnonymousSSLSocket)
            throws IOException
          {
          }

          public void verify(String paramAnonymousString, String[] paramAnonymousArrayOfString1, String[] paramAnonymousArrayOfString2)
            throws SSLException
          {
          }

          public boolean verify(String paramAnonymousString, SSLSession paramAnonymousSSLSession)
          {
            return true;
          }
        });
      }
      else
      {
          this.uc = ((HttpURLConnection)localURL.openConnection());
          this.uc.setConnectTimeout(60000);
          this.uc.setRequestProperty("Authorization", "Basic " + Base64.encodeToString(new StringBuilder(String.valueOf(this.zm_user)).append(":").append(this.zm_pass).toString().getBytes(), 0).trim());
      }
      return Boolean.valueOf(true);
      /*
      while (true)
      {
        return Boolean.valueOf(true);
        this.uc = ((HttpURLConnection)localURL.openConnection());
        this.uc.setConnectTimeout(60000);
        this.uc.setRequestProperty("Authorization", "Basic " + Base64.encodeToString(new StringBuilder(String.valueOf(this.zm_user)).append(":").append(this.zm_pass).toString().getBytes(), 0).trim());
      }*/
    }
    catch (MalformedURLException localMalformedURLException)
    {
      localMalformedURLException.printStackTrace();
      return Boolean.valueOf(false);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return Boolean.valueOf(false);
  }

  public class ServiceDiscoveryBinder extends Binder
  {
    public ServiceDiscoveryBinder()
    {
    }

    public GetDataService getService()
    {
      return GetDataService.this;
    }
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.GetDataService
 * JD-Core Version:    0.6.2
 */
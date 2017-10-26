package com.html5clouds.zmview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

//for compile
//import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
// for compile
//import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
//import com.google.ads.AdRequest;
//import com.google.ads.AdSize;
//import com.google.ads.AdView;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;

@SuppressLint({"NewApi"})
public class LiveMonitor extends Activity
{
  private static final long DOUBLE_PRESS_INTERVAL = 250L;
  private static final String TAG = "LiveMonitor";
  private ObscuredSharedPreferences Eprefs;
  protected boolean ServiceConnected;
  private Boolean ShowControl = Boolean.valueOf(false);
  private String URL;
  private int ViewDisplayMode = 10;
  private int ZmID;
  private GetDataService _getDataService;
  private ServiceConnection _getDataServiceConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      if (debug.booleanValue())
        System.out.println("ZmView we try to connect to service");
      _getDataService = ((GetDataService.ServiceDiscoveryBinder)paramAnonymousIBinder).getService();
      ServiceConnected = true;
    }

    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
    }
  };
  private InitTask _initTask = null;
  private ProgressBar _pload;
  private int actionId;
  //private AdView adView;
  //private RelativeLayout ads;
  private int bheight;
  private ImageButton btn1;
  private ImageButton btn2;
  private ImageButton btn3;
  private int bwidth;
  private Boolean debug = Boolean.valueOf(true);
  protected DoRead doread;
  private String event_id;
  private FrameLayout fl;
  private int i_index;
  private boolean isPro;
  protected long lastPressTime;
  protected View last_img_selected = null;
  protected boolean mHasDoubleClicked;
  private RelativeLayout main;
  private LiveViewImg mv;
  private ArrayList<ZmCamera> myZmCamera;
  private ArrayList<ImageView> myZmImageView;
  private SharedPreferences prefs;
  private int selected_page;
  private boolean showloading = true;
  private String zm_auth;
  private String zm_get_auth;
  private String zm_host;
  private String zm_pass;
  private String zm_user;

  private void unbindDrawables(View paramView)
  {
    if (paramView.getBackground() != null)
      paramView.getBackground().setCallback(null);
    if ((paramView instanceof ViewGroup));
    for (int j = 0; ; j++)
    {
      if (j >= ((ViewGroup)paramView).getChildCount())
      {
        ((ViewGroup)paramView).removeAllViews();
        return;
      }
      unbindDrawables(((ViewGroup)paramView).getChildAt(j));
    }
  }

  protected int Dip(float paramFloat)
  {
    return Math.round(paramFloat * getBaseContext().getResources().getDisplayMetrics().density);
  }

  public void TouchControl()
  {
    if (this.debug.booleanValue())
      System.out.println("ZmView mvwidth=" + String.valueOf(this.mv.getWidth()));
    if (this.debug.booleanValue())
      System.out.println("ZmView BMX=" + String.valueOf(this.mv.GetBmX()));
    if (this.debug.booleanValue())
      System.out.println("ZmView BMY=" + String.valueOf(this.mv.GetBmY()));
    if (this.debug.booleanValue())
      System.out.println("ZmView BMW=" + String.valueOf(this.mv.GetBmW()));
    if (this.debug.booleanValue())
      System.out.println("ZmView BMH=" + String.valueOf(this.mv.GetBmH()));
    if (this.debug.booleanValue())
      System.out.println("ZmView ShowControl=" + String.valueOf(this.ShowControl));
    if (this.ShowControl.booleanValue())
    {
      this.main.setVisibility(8);
      this.ShowControl = Boolean.valueOf(false);
      return;
    }
    this.main.setVisibility(0);
    this.ShowControl = Boolean.valueOf(true);
  }

  public LiveStream getHttpRes()
  {
    DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
    Log.d("LiveMonitor", "1. Sending http request");
    try
    {
      HttpGet localHttpGet = new HttpGet(URI.create(this.URL));
      if (this.URL.contains("https:"))
        localDefaultHttpClient = getNewHttpClient();
      HttpResponse localHttpResponse = localDefaultHttpClient.execute(localHttpGet);
      localHttpGet.setHeader("Authorization", "Basic " + Base64.encodeToString(new StringBuilder(String.valueOf(this.zm_user)).append(":").append(this.zm_pass).toString().getBytes(), 0).trim());
      Log.d("LiveMonitor", "2. Request finished, status = " + localHttpResponse.getStatusLine().getStatusCode());
      if (localHttpResponse.getStatusLine().getStatusCode() == 401)
        return null;
      if (this.debug.booleanValue())
        System.out.println("ZmView loop3");
      LiveStream localLiveStream = new LiveStream(localHttpResponse.getEntity().getContent());
      return localLiveStream;
    }
    catch (ClientProtocolException localClientProtocolException)
    {
      localClientProtocolException.printStackTrace();
      Log.d("LiveMonitor", "Request failed-ClientProtocolException", localClientProtocolException);
      return null;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      Log.d("LiveMonitor", "Request failed-IOException", localIOException);
    }
    return null;
  }
  //capture single bit
  public InputStream getHttpRes2(Integer paramInteger)
  {
    DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
    Log.d("LiveMonitor", "1. Sending http request");
    
    try
    {
      if (this.zm_get_auth == null);
      HttpResponse localHttpResponse;
      //mode=single
      String str = this.prefs.getString("zm_bin_path", "/../cgi-bin/nph-zms") + "?mode=mpeg&monitor=" + ((ZmCamera)this.myZmCamera.get(paramInteger.intValue())).getID() + "&scale=75&maxfps=5&bitrate=8000&format=swf";
      //String str = this.prefs.getString("zm_bin_path", "/../cgi-bin/nph-zms") + "?mode=single&monitor=" + ((ZmCamera)this.myZmCamera.get(paramInteger.intValue())).getID() + "&scale=100&maxfps=15&buffer=1000&" + this.zm_get_auth + "&user=" + URLEncoder.encode(this.zm_user, "UTF-8") + "&pass=" + URLEncoder.encode(this.zm_pass, "UTF-8");
      //String str = this.prefs.getString("zm_bin_path", "../nph-zms") + "?mode=single&monitor=" + ((ZmCamera)myZmCamera.get(paramInteger.intValue())).getID() + "&scale=100&maxfps=15&buffer=1000&" ;//+ this.zm_get_auth;
      //for (String str = this.prefs.getString("zm_bin_path", "../nph-zms") + "?mode=single&monitor=" + ((ZmCamera)this.myZmCamera.get(paramInteger.intValue())).getID() + "&scale=100&maxfps=15&buffer=1000&" + this.zm_get_auth + "&user=" + URLEncoder.encode(this.zm_user, "UTF-8") + "&pass=" + URLEncoder.encode(this.zm_pass, "UTF-8"); ; str = this.prefs.getString("zm_bin_path", "../nph-zms") + "?mode=single&monitor=" + ((ZmCamera)this.myZmCamera.get(paramInteger.intValue())).getID() + "&scale=100&maxfps=15&buffer=1000&" + this.zm_get_auth)
      {
        if (this.debug.booleanValue())
          System.out.println("ZmView URL=" + str);
        str = zm_host + str;
        //str = "http://192.168.0.153/zm/index.php?view=watch&mid=1/cgi-bin/nph-zms?mode=single&monitor=1&scale=100";
        //URI url;
        
        HttpGet localHttpGet = new HttpGet(URI.create(str));
        //http://192.168.0.153/zm/index.php?view=watch&mid=1/
        //localHttpGet.setHeader("host", "192.168.0.153");
        //localHttpGet.setHeader("Referer", "http://192.168.0.153/zm/index.php?view=watch&mid=1");
        if (str.contains("https:"))
          localDefaultHttpClient = getNewHttpClient();
        localHttpResponse = localDefaultHttpClient.execute(localHttpGet);
        localHttpGet.setHeader("Authorization", "Basic " + Base64.encodeToString(new StringBuilder(String.valueOf(this.zm_user)).append(":").append(this.zm_pass).toString().getBytes(), 0).trim());
        Log.d("LiveMonitor", "2. Request finished, status = " + localHttpResponse.getStatusLine().getStatusCode());
        if (localHttpResponse.getStatusLine().getStatusCode() != 401)
        {
        	//  break;
        	return null;
        }
        
      }
      if (this.debug.booleanValue())
        System.out.println("ZmView loop3");
      InputStream localInputStream = localHttpResponse.getEntity().getContent();
      return localInputStream;
    }
    catch (ClientProtocolException localClientProtocolException)
    {
      localClientProtocolException.printStackTrace();
      Log.d("LiveMonitor", "Request failed-ClientProtocolException", localClientProtocolException);
      return null;
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        localIOException.printStackTrace();
        Log.d("LiveMonitor", "Request failed-IOException", localIOException);
      }
    }
  }

  public DefaultHttpClient getNewHttpClient()
  {
    try
    {
      KeyStore localKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      localKeyStore.load(null, null);
      MySSLSocketFactory localMySSLSocketFactory = new MySSLSocketFactory(localKeyStore);
      localMySSLSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
      BasicHttpParams localBasicHttpParams = new BasicHttpParams();
      HttpProtocolParams.setVersion(localBasicHttpParams, HttpVersion.HTTP_1_1);
      HttpProtocolParams.setContentCharset(localBasicHttpParams, "UTF-8");
      SchemeRegistry localSchemeRegistry = new SchemeRegistry();
      localSchemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
      localSchemeRegistry.register(new Scheme("https", localMySSLSocketFactory, 443));
      DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(localBasicHttpParams, localSchemeRegistry), localBasicHttpParams);
      return localDefaultHttpClient;
    }
    catch (Exception localException)
    {
    }
    return new DefaultHttpClient();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (debug.booleanValue())
      System.out.println("ZmView we start onCreate");
    getWindow().addFlags(128);
    Intent localIntent = new Intent("zmview.BIND");
    getApplicationContext().bindService(localIntent, this._getDataServiceConnection, 1);
    if (debug.booleanValue())
    {
    	System.out.println("ZmView we try to connect to service2");
    }
    ObscuredSharedPreferences localObscuredSharedPreferences = new ObscuredSharedPreferences(this, getSharedPreferences("zmview.cfg", 0));
    Eprefs = localObscuredSharedPreferences;
    isPro = Eprefs.getBoolean("isPro", false);
    prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    Bundle localBundle = getIntent().getExtras();
    zm_user = localBundle.getString("zm_user");
    zm_pass = localBundle.getString("zm_pass");
    zm_auth = localBundle.getString("zm_auth");
    zm_host = localBundle.getString("zm_host");
    zm_get_auth = localBundle.getString("zm_get_auth");
    ZmID = localBundle.getInt("ZmID");
    //
    URL = localBundle.getString("URL");
    actionId = localBundle.getInt("actionId");
    if (actionId == 2)
    {
      event_id = localBundle.getString("event_id");
      selected_page = localBundle.getInt("selected_page");
    }
    if (localBundle.getSerializable("myZmCamera") != null)
    {
    	myZmCamera = ((ArrayList)localBundle.getSerializable("myZmCamera"));
    }
    myZmImageView = new ArrayList();
    if (paramBundle != null)
    {
      ZmID = paramBundle.getInt("ZmID");
      ViewDisplayMode = paramBundle.getInt("ViewDisplayMode");
    }
    if (debug.booleanValue())
    {
      PrintStream localPrintStream = System.out;
      StringBuilder localStringBuilder = new StringBuilder("ZmView Camera1=");
      localPrintStream.println(((ZmCamera)this.myZmCamera.get(0)).getName());
    }
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    //LiveViewImg localLiveViewImg 
    mv = new LiveViewImg(this);
    mv.setMrun(Boolean.valueOf(false));
    fl = new FrameLayout(this);
    btn1 = new ImageButton(this);
    btn2 = new ImageButton(this);

    btn3 = new ImageButton(this);

    bwidth = Dip(80.0F);
    bheight = Dip(33.0F);
    btn1.setBackgroundResource(R.drawable.btn_fullscreen);
    btn1.setMaxWidth(bwidth);
    btn1.setMaxHeight(bheight);
    btn1.setMinimumWidth(bwidth);
    btn1.setMinimumHeight(bheight);
    
    btn2.setBackgroundResource(R.drawable.btn_bestfit);
    btn2.setMaxWidth(bwidth);
    btn2.setMaxHeight(bheight);
    btn2.setMinimumWidth(bwidth);
    btn2.setMinimumHeight(bheight);
    
    btn3.setBackgroundResource(R.drawable.btn_normalsize);
    btn3.setMaxWidth(bwidth);
    btn3.setMaxHeight(bheight);
    btn3.setMinimumWidth(bwidth);
    btn3.setMinimumHeight(bheight);
    
    //ImageButton localImageButton4 = btn1;
    btn1OnClick localbtn1OnClick = new btn1OnClick();
    btn1.setOnClickListener(localbtn1OnClick);
    
    //ImageButton localImageButton5 = btn2;
    btn2OnClick localbtn2OnClick = new btn2OnClick();
    btn2.setOnClickListener(localbtn2OnClick);
    
    //ImageButton localImageButton6 = btn3;
    btn3OnClick localbtn3OnClick = new btn3OnClick();
    btn3.setOnClickListener(localbtn3OnClick);
    
    HorizontalScrollView localHorizontalScrollView = new HorizontalScrollView(this);
    LinearLayout localLinearLayout1 = new LinearLayout(this);
    localLinearLayout1.setOrientation(0);
    myZmImageView = new ArrayList();
    if (actionId == 1)
    {
    	i_index = 0;
    }
    
    //最后的一个
    //if (i_index >= myZmCamera.size())
    {
      localHorizontalScrollView.addView(localLinearLayout1);
      localHorizontalScrollView.setHorizontalScrollBarEnabled(false);
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
      localLayoutParams.gravity = 1;
      localLayoutParams.bottomMargin = Dip(6.0F);
      localLayoutParams.topMargin = Dip(6.0F);
      localLayoutParams.leftMargin = Dip(6.0F);
      localLayoutParams.rightMargin = Dip(6.0F);
      LinearLayout localLinearLayout2 = new LinearLayout(this);
      localLinearLayout2.setOrientation(0);
      View localView1 = new View(this);
      localView1.setLayoutParams(new LinearLayout.LayoutParams(Dip(15.0F), -1));
      localLinearLayout2.addView(this.btn1);
      localLinearLayout2.addView(localView1);
      localLinearLayout2.addView(this.btn2);
      View localView2 = new View(this);
      localView2.setLayoutParams(new LinearLayout.LayoutParams(Dip(15.0F), -1));
      localLinearLayout2.addView(localView2);
      localLinearLayout2.addView(this.btn3);
      localLinearLayout2.setLayoutParams(localLayoutParams);
      LinearLayout localLinearLayout3 = new LinearLayout(this);
      localLinearLayout3.setOrientation(0);
      localLinearLayout3.setLayoutParams(localLayoutParams);
      localLinearLayout3.addView(localHorizontalScrollView);
      LinearLayout localLinearLayout4 = new LinearLayout(this);
      localLinearLayout4.setOrientation(1);
      RelativeLayout.LayoutParams localLayoutParams1 = new RelativeLayout.LayoutParams(-2, -2);
      localLayoutParams1.addRule(8, -1);
      localLayoutParams1.addRule(14, -1);
      localLayoutParams1.addRule(12, -1);
      RelativeLayout.LayoutParams localLayoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
      localLayoutParams2.addRule(14, -1);
      localLayoutParams2.addRule(15, -1);
      ProgressBar localProgressBar = new ProgressBar(this, null, 16842874);
      _pload = localProgressBar;
      _pload.setVisibility(0);
      _pload.setLayoutParams(localLayoutParams2);
      localLinearLayout4.setLayoutParams(localLayoutParams1);
      localLinearLayout4.addView(localLinearLayout3);
      localLinearLayout4.addView(localLinearLayout2);
      RelativeLayout localRelativeLayout1 = new RelativeLayout(this);
      main = localRelativeLayout1;
      main.addView(localLinearLayout4);
      main.addView(this._pload);
      if (!isPro)
      {
      	/*
        RelativeLayout.LayoutParams localLayoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
        localLayoutParams3.addRule(14, -1);
        localLayoutParams3.addRule(10, -1);
        AdView localAdView = new AdView(this, AdSize.BANNER, "8895357a04684bb8");
        this.adView = localAdView;
        this.adView.loadAd(new AdRequest());
        this.adView.setLayoutParams(localLayoutParams3);
        RelativeLayout localRelativeLayout2 = new RelativeLayout(this);
        this.ads = localRelativeLayout2;
        this.ads.addView(this.adView);
        */
      }
      main.setVisibility(8);
      fl.addView(mv);
      fl.addView(main);
      //if (!this.isPro)
      //  this.fl.addView(this.ads);
      fl.setFocusable(true);
      //FrameLayout localFrameLayout2 = fl;
      View.OnTouchListener local3 = new View.OnTouchListener()
      {
        public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
        {
          long l = System.currentTimeMillis();
          if (l - lastPressTime <= 250L)
          {
            mHasDoubleClicked = true;
            lastPressTime = l;
            if (mHasDoubleClicked)
            {
              if (mv.getDisplayMode() != 8)
              {
              	//break label127;
                  //break;
                  if (mv.getDisplayMode() == 4)
                    mv.setDisplayMode(1);
                  else if (mv.getDisplayMode() == 1)
                    mv.setDisplayMode(8);
              }
              mv.setDisplayMode(4);
            }
          }
          //while (true)
          else
          {
            /*
            if (main.getVisibility() == 0)
              TouchControl();
            return false;
            */
            /**/mHasDoubleClicked = false;
            new Handler()
            {
              public void handleMessage(Message paramAnonymous2Message)
              {
                if (!mHasDoubleClicked)
                  TouchControl();
              }
            }
            .sendMessageDelayed(new Message(), 250L);

          }
          if (main.getVisibility() == 0)
              TouchControl();
          return false;
        }
      };
      fl.setOnTouchListener(local3);
      setContentView(this.fl);
      
	  //初始化线程
	  _initTask = ((InitTask)getLastNonConfigurationInstance());
	  if (_initTask != null)
	  {
		  //break label2266;
		  _initTask.attach(this);        	  
	  }
	  else
	  {
	      //InitTask localInitTask = new InitTask(this);
	      _initTask = new InitTask(this);
	  }
      if (Build.VERSION.SDK_INT > 10)
      {
    	  _initTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Context[] { this });
      }
      else
	  {
    	  _initTask.execute(new Context[] { this });
	  }
      
      
      doread = new DoRead();
      if (Build.VERSION.SDK_INT > 10)
      {
      	//break label2098;
          //label2098: 
          //DoRead localDoRead2 = doread;
    	  
    	  
	  
	      Executor localExecutor1 = AsyncTask.THREAD_POOL_EXECUTOR;
	      String[] arrayOfString1 = new String[4];
	      arrayOfString1[0] = URL;
	      arrayOfString1[1] = zm_user;
	      arrayOfString1[2] = zm_pass;
	      arrayOfString1[3] = zm_auth;
	      doread.executeOnExecutor(localExecutor1, arrayOfString1);
          
    	 
          
          //break label1479;        	
      }
      else
      {
      		//DoRead localDoRead3 = doread;
	        String[] arrayOfString2 = new String[4];
	        arrayOfString2[0] = URL;
	        arrayOfString2[1] = zm_user;
	        arrayOfString2[2] = zm_pass;
	        arrayOfString2[3] = zm_auth;
	        doread.execute(arrayOfString2);
	        //label1479: if (this.actionId != 1);
      }
      
    }
    i_index = 0;
    while (i_index <= myZmCamera.size())
    {
    	//if (zm_get_auth == null);
		 //URL = (prefs.getString("zm_bin_path", "/../cgi-bin/nph-zms") + "?mode=mpeg&monitor=" + ((ZmCamera)this.myZmCamera.get(this.ZmID)).getID() + "&scale=75&maxfps=5&bitrate=8000&format=swf") ;
		 //try {
			//URL = (prefs.getString("zm_bin_path", "/../cgi-bin/nph-zms") + "?mode=jpeg&monitor=" + ((ZmCamera)this.myZmCamera.get(this.ZmID)).getID() + "&scale=100&maxfps=15&buffer=1000&" + this.zm_get_auth + "&user=" + URLEncoder.encode(this.zm_user, "UTF-8") + "&pass=" + URLEncoder.encode(this.zm_pass, "UTF-8"));
			URL = (prefs.getString("zm_bin_path", "/../cgi-bin/nph-zms") + "?mode=mpeg&monitor=" + ((ZmCamera)this.myZmCamera.get(this.ZmID)).getID() + "&scale=75&maxfps=5&bitrate=8000&format=swf") ;
		 //} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		//URL = (prefs.getString("zm_bin_path", "/../cgi-bin/nph-zms") + "?mode=jpeg&monitor=" + ((ZmCamera)this.myZmCamera.get(this.ZmID)).getID() + "&scale=100&maxfps=15&buffer=1000&" + this.zm_get_auth);
		//URL = this.prefs.getString("zm_bin_path", "/../cgi-bin/nph-zms") + "?mode=jpeg&monitor=" + ((ZmCamera)this.myZmCamera.get(this.ZmID)).getID() + "&scale=100&maxfps=15&buffer=1000&" + this.zm_get_auth + "&user=" + URLEncoder.encode(this.zm_user, "UTF-8") + "&pass=" + URLEncoder.encode(this.zm_pass, "UTF-8");
//for (this.URL = (this.prefs.getString("zm_bin_path", "../nph-zms") + "?mode=jpeg&monitor=" + ((ZmCamera)this.myZmCamera.get(this.ZmID)).getID() + "&scale=100&maxfps=15&buffer=1000&" + this.zm_get_auth + "&user=" + URLEncoder.encode(this.zm_user, "UTF-8") + "&pass=" + URLEncoder.encode(this.zm_pass, "UTF-8")); ;
//		this.URL = (this.prefs.getString("zm_bin_path", "../nph-zms") + "?mode=jpeg&monitor=" + ((ZmCamera)this.myZmCamera.get(this.ZmID)).getID() + "&scale=100&maxfps=15&buffer=1000&" + this.zm_get_auth))
		  URL  = zm_host + URL;
		  if (debug.booleanValue())
		  {
			  System.out.println("ZmView URL=" + this.URL);
		  }	
		  if(i_index != ZmID)
		  {
		      LinearLayout localLinearLayout5 = new LinearLayout(this);
		      localLinearLayout5.setOrientation(1);
		      final ImageView localImageView = new ImageView(this);
		      localImageView.setTag(Integer.valueOf(this.i_index));
		      //localImageView.setImageResource(2130837647);
		      localImageView.setScaleType(ImageView.ScaleType.FIT_XY);
		      localImageView.setLayoutParams(new Gallery.LayoutParams(Dip(70.0F), Dip(70.0F)));
		      localImageView.setPadding(Dip(4.0F), Dip(4.0F), Dip(4.0F), Dip(4.0F));
		      myZmImageView.add(localImageView);

		  }
		  //while (true)
		  //selected camera
		  else //if (i_index == ZmID)
		  {	
			LinearLayout localLinearLayout5 = new LinearLayout(this);
			last_img_selected = new ImageView(this);
			last_img_selected.setBackgroundResource(R.drawable.cam_selected);
		    //last_img_selected = localImageView;
		   
		    TextView localTextView = new TextView(this);
		    localTextView.setText(((ZmCamera)this.myZmCamera.get(this.i_index)).getName());
		    localTextView.setTextSize(Dip(12.0F));
		    localTextView.setGravity(17);
		    //localTextView.setBackgroundColor(-16777216);
		    View localView3 = new View(this);
		    localView3.setLayoutParams(new LinearLayout.LayoutParams(Dip(15.0F), -1));
		    localLinearLayout5.addView(last_img_selected);
		    localLinearLayout5.addView(localTextView);
		    localLinearLayout1.addView(localLinearLayout5);
		    localLinearLayout1.addView(localView3);
		    View.OnClickListener local2 = new View.OnClickListener()
		    {
		      public void onClick(View paramAnonymousView)
		      {
		        String str = String.valueOf(last_img_selected.getTag());
		        try
		        {  
		          //if (zm_get_auth == null);
		          URL = (prefs.getString("zm_bin_path", "../nph-zms") + "?mode=jpeg&monitor=" + ((ZmCamera)myZmCamera.get(Integer.valueOf(str).intValue())).getID() + "&scale=100&maxfps=30&buffer=1000&" + zm_get_auth + "&user=" + URLEncoder.encode(zm_user, "UTF-8") + "&pass=" + URLEncoder.encode(zm_pass, "UTF-8"));
		          //for (URL = (prefs.getString("zm_bin_path", "../nph-zms") + "?mode=jpeg&monitor=" + ((ZmCamera)myZmCamera.get(Integer.valueOf(str).intValue())).getID() + "&scale=100&maxfps=30&buffer=1000&" + zm_get_auth + "&user=" + URLEncoder.encode(zm_user, "UTF-8") + "&pass=" + URLEncoder.encode(zm_pass, "UTF-8")); ;
		          //	  URL = (prefs.getString("zm_bin_path", "../nph-zms") + "?mode=jpeg&monitor=" + ((ZmCamera)myZmCamera.get(Integer.valueOf(str).intValue())).getID() + "&scale=100&maxfps=30&buffer=1000&" + zm_get_auth))
		          {
		            ZmID = Integer.valueOf(str).intValue();
		            _pload.setVisibility(0);
		            if (last_img_selected != null)
		              last_img_selected.setBackgroundDrawable(null);
		            paramAnonymousView.setBackgroundResource(R.drawable.cam_selected);
		            last_img_selected = paramAnonymousView;
		            paramAnonymousView.buildDrawingCache();
		            Bitmap localBitmap = paramAnonymousView.getDrawingCache();
		            mv.setBitmap(localBitmap);
		            mv.setMrun(Boolean.valueOf(false));
		            showloading = true;
		            //doread = null;
		            doread = new LiveMonitor.DoRead(/*LiveMonitor.this*/);
		            //LiveMonitor.DoRead localDoRead = doread;
		            String[] arrayOfString = new String[4];
		            arrayOfString[0] = URL;
		            arrayOfString[1] = zm_user;
		            arrayOfString[2] = zm_pass;
		            arrayOfString[3] = zm_auth;
		            doread.execute(arrayOfString);
		            //return;
		          }
		        }
		        catch (NumberFormatException localNumberFormatException)
		        {
		          //while (true)
		            localNumberFormatException.printStackTrace();
		        }
		        catch (UnsupportedEncodingException localUnsupportedEncodingException)
		        {
		          //while (true)
		            localUnsupportedEncodingException.printStackTrace();
		        }
		      }
		    };
		    last_img_selected.setOnClickListener(local2);
		    //i_index ++;//= (1 + this.i_index);
		    //break;
		    //Executor localExecutor2 = AsyncTask.THREAD_POOL_EXECUTOR;
		    //Integer[] arrayOfInteger1 = new Integer[1];
		    //arrayOfInteger1[0] = Integer.valueOf(this.i);
		    //localDoRead21.executeOnExecutor(localExecutor2, arrayOfInteger1);
		  }
		  DoRead2 localDoRead21 = new DoRead2();
		  if (Build.VERSION.SDK_INT <= 10)
		  {
		      Integer[] arrayOfInteger2 = new Integer[1];
		      arrayOfInteger2[0] = Integer.valueOf(i_index);
		      localDoRead21.execute(arrayOfInteger2);
		  }
		  else
		  {	  Executor localExecutor2 = AsyncTask.THREAD_POOL_EXECUTOR;
			  Integer[] arrayOfInteger1 = new Integer[1];
			  arrayOfInteger1[0] = Integer.valueOf(this.i_index);
			  localDoRead21.executeOnExecutor(localExecutor2, arrayOfInteger1);
		  }
		  i_index ++;
      
    } 
    
 
	if (this.debug.booleanValue())
		System.out.println("ZmView we finish onCreate");
	TouchControl();	
    
  }

  protected void onDestroy()
  {
    super.onDestroy();
  }

  public void onPause()
  {
    super.onPause();
    this.mv.stopPlayback();
    unbindDrawables(this.fl);
    for (int j = 0; ; j++)
    {
      if (j >= this.myZmImageView.size())
      {
        if (this.debug.booleanValue())
          System.out.println("ZmView we are onPause");
        return;
      }
      ((ImageView)this.myZmImageView.get(j)).setImageBitmap(null);
    }
  }

  public Object onRetainNonConfigurationInstance()
  {
    return this._initTask;
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("ZmID", ZmID);
    paramBundle.putInt("ViewDisplayMode", ViewDisplayMode);
  }

  public void onStart()
  {
    super.onStart();
    if (debug.booleanValue())
      System.out.println("ZmView we are onStart");
    if (_initTask.isCancelled())
    {
      _initTask = new InitTask(this);
      if (Build.VERSION.SDK_INT <= 10)
      {
    	  _initTask.execute(new Context[] { this });
      }
      else
      {
      	  _initTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Context[] { this });	
      }
    }
    else
    {
    	_initTask.attach(this);	
    }
    
    if (mv != null)
        mv.startPlayback();
    /*while (true)
    {
      if (this.mv != null)
        this.mv.startPlayback();
      return;
      this._initTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Context[] { this });
      continue;
      this._initTask.attach(this);
    }*/
  }

  public void onStop()
  {
    super.onStop();
    this._initTask.cancel(true);
    if (this.debug.booleanValue())
      System.out.println("ZmView we are onStop");
  }

  //DoRead read stream 
  public class DoRead extends AsyncTask<String, Void, LiveStream>
  {
    public DoRead()
    {
    }

    protected LiveStream doInBackground(String[] paramArrayOfString)
    {
      return new LiveStream(getHttpRes());
    }

    protected void onCancelled()
    {
      super.onCancelled();
      Log.i("makemachine", "onCancelled()");
    }

    protected void onPostExecute(LiveStream paramLiveStream)
    {
      mv.setSource(paramLiveStream);
      if (ViewDisplayMode == 10)
        mv.setDisplayMode(mv.getDisplayMode());
      //while (true)
      else
      {
    	/*
        mv.showFps(true);
        mv.setMrun(Boolean.valueOf(true));
        return;
        */
        mv.setDisplayMode(ViewDisplayMode);
      }
      mv.showFps(true);
      mv.setMrun(Boolean.valueOf(true));
    }
  }
  
  //DoRead2 read stream 
  public class DoRead2 extends AsyncTask<Integer, Void, Drawable>
  {
    int ID;
    public DoRead2()
    {
    }
    @Override
    protected Drawable doInBackground(Integer[] paramArrayOfInteger)
    {
      this.ID = paramArrayOfInteger[0].intValue();
      ID = 0;
      return Drawable.createFromStream(getHttpRes2(Integer.valueOf(this.ID)), "src name");
      //return null;
    }
    @Override
    protected void onCancelled()
    {
      super.onCancelled();
      Log.i("makemachine", "onCancelled()");
    }
    @Override
    protected void onPostExecute(Drawable paramDrawable)
    {
      ID = 0;
      if (paramDrawable == null)
      {
        ((ImageView)myZmImageView.get(this.ID)).setImageResource(R.drawable.loading_icon);
        //return;
      }
      else
      {
    	  ((ImageView)myZmImageView.get(this.ID)).setImageDrawable(paramDrawable);
      }
    }
  }

  protected class InitTask extends AsyncTask<Context, Integer, String>
  {
    LiveMonitor activity = null;

    InitTask(LiveMonitor arg2)
    {
      /*
      LiveMonitor localLiveMonitor;
      */
      attach(/*localLiveMonitor*/arg2);
    }

    void attach(LiveMonitor paramLiveMonitor)
    {
      this.activity = paramLiveMonitor;
    }

    void detach()
    {
      this.activity = null;
    }
    @Override
    protected String doInBackground(Context[] paramArrayOfContext)
    {
      while (true)
      {
        if (isCancelled())
          return "COMPLETE!";
        try
        {
          Thread.sleep(1000L);
          if ((showloading) && (mv != null) && (mv.getImg()))
            showloading = false;
          Integer[] arrayOfInteger = new Integer[1];
          arrayOfInteger[0] = Integer.valueOf(0);
          publishProgress(arrayOfInteger);
        }
        catch (Exception localException)
        {
          Log.i("makemachine", localException.getMessage());
        }
      }
    }
    @Override
    protected void onCancelled()
    {
      super.onCancelled();
    }
    @Override
    protected void onPostExecute(String paramString)
    {
      super.onPostExecute(paramString);
    }
    @Override
    protected void onPreExecute()
    {
      Log.i("makemachine", "onPreExecute()");
      super.onPreExecute();
    }
    @Override
    protected void onProgressUpdate(Integer[] paramArrayOfInteger)
    {
      if ((!showloading) && (_pload != null))
        _pload.setVisibility(8);
      super.onProgressUpdate(paramArrayOfInteger);
    }
  }

  protected class btn1OnClick
    implements View.OnClickListener
  {
    protected btn1OnClick()
    {
    }

    public void onClick(View paramView)
    {
      mv.setDisplayMode(8);
    }
  }

  protected class btn2OnClick
    implements View.OnClickListener
  {
    protected btn2OnClick()
    {
    }

    public void onClick(View paramView)
    {
      mv.setDisplayMode(4);
    }
  }

  protected class btn3OnClick
    implements View.OnClickListener
  {
    protected btn3OnClick()
    {
    }

    public void onClick(View paramView)
    {
      mv.setDisplayMode(1);
    }
  }
}

/* Location:           E:\android 鍙峔jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.LiveMonitor
 * JD-Core Version:    0.6.2
 */
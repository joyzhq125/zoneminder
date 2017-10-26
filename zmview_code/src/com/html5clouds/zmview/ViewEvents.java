package com.html5clouds.zmview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
//for compile
//import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
//for compile
//import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
//import com.google.ads.AdRequest;
//import com.google.ads.AdSize;
//import com.google.ads.AdView;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import javax.net.ssl.HttpsURLConnection;
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
public class ViewEvents extends Activity
{
  private static final long DOUBLE_PRESS_INTERVAL = 250L;
  private ObscuredSharedPreferences Eprefs;
  private AlertDialog ProAlert;
  private Boolean ServiceConnected = Boolean.valueOf(false);
  private boolean ShowControl;
  private String URL;
  private int ViewDisplayMode = 10;
  private int ZmID;
  private GetDataService _getDataService;
  private ServiceConnection _getDataServiceConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      if (ViewEvents.this.debug)
        System.out.println("ZmView we try to connect to service");
      ViewEvents.this._getDataService = ((GetDataService.ServiceDiscoveryBinder)paramAnonymousIBinder).getService();
      ViewEvents.this.ServiceConnected = Boolean.valueOf(true);
    }

    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
    }
  };
  private InitTask _initTask = null;
  private ProgressBar _pload;
  private int actionId;
  //private AdView adView;
  private RelativeLayout ads;
  private int bheight;
  private ImageButton btn1;
  private ImageButton btn2;
  private ImageButton btn3;
  private int bwidth;
  private boolean debug = false;
  protected DoRead doread;
  protected int ev_id = -1;
  private String event_id;
  private int event_time = 0;
  private FrameLayout fl;
  private boolean isPro;
  protected View last_img_selected = null;
  private RelativeLayout ll1;
  private LinearLayout ll12;
  private RelativeLayout ll2;
  private RelativeLayout ll3;
  private RelativeLayout ll4;
  private RelativeLayout ll5;
  private int load_events = 0;
  private boolean load_first_page = false;
  private RelativeLayout main;
  private LiveViewImg mv;
  private ArrayList<ZmCamera> myZmCamera;
  private ArrayList<zmEvents> myZmEvents;
  private int nr_pages;
  private SharedPreferences prefs;
  private String sFilterEvents;
  protected HorizontalScrollView scrollView;
  private int selected_page;
  private boolean showloading = true;
  private HttpsURLConnection ucs;
  private String zm_auth;
  private String zm_get_auth;
  private String zm_host;
  private String zm_pass;
  private String zm_user;


  protected int Dip(float paramFloat)
  {
    return Math.round(paramFloat * getBaseContext().getResources().getDisplayMetrics().density);
  }

  public void ShowDialogPro()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setMessage("This option is only available in Pro version !").setCancelable(true).setPositiveButton("Go Pro", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setData(Uri.parse("market://details?id=com.html5cloud.zmview"));
        ViewEvents.this.startActivity(localIntent);
      }
    }).setNegativeButton("Back", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        ViewEvents.this.ProAlert.dismiss();
      }
    });
    this.ProAlert = localBuilder.create();
    this.ProAlert.show();
  }

  public void TouchControl()
  {
    if (this.debug)
      System.out.println("ZmView mvwidth=" + String.valueOf(this.mv.getWidth()));
    if (this.debug)
      System.out.println("ZmView BMX=" + String.valueOf(this.mv.GetBmX()));
    if (this.debug)
      System.out.println("ZmView BMY=" + String.valueOf(this.mv.GetBmY()));
    if (this.debug)
      System.out.println("ZmView BMW=" + String.valueOf(this.mv.GetBmW()));
    if (this.debug)
      System.out.println("ZmView BMH=" + String.valueOf(this.mv.GetBmH()));
    if (this.debug)
      System.out.println("ZmView ShowControl=" + String.valueOf(this.ShowControl));
    if (this.ShowControl)
    {
      this.main.setVisibility(8);
      this.ShowControl = false;
      return;
    }
    this.main.setVisibility(0);
    this.ShowControl = true;
  }

  protected void display_events()
  {
    this.mv.setTime(this.event_time);
    this.fl = new FrameLayout(this);
    this.btn1 = new ImageButton(this);
    this.btn2 = new ImageButton(this);
    this.btn3 = new ImageButton(this);
    this.bwidth = Dip(80.0F);
    this.bheight = Dip(33.0F);
    this.btn1.setBackgroundResource(2130837622);
    this.btn1.setMaxWidth(this.bwidth);
    this.btn1.setMaxHeight(this.bheight);
    this.btn1.setMinimumWidth(this.bwidth);
    this.btn1.setMinimumHeight(this.bheight);
    this.btn2.setBackgroundResource(2130837621);
    this.btn2.setMaxWidth(this.bwidth);
    this.btn2.setMaxHeight(this.bheight);
    this.btn2.setMinimumWidth(this.bwidth);
    this.btn2.setMinimumHeight(this.bheight);
    this.btn3.setBackgroundResource(2130837623);
    this.btn3.setMaxWidth(this.bwidth);
    this.btn3.setMaxHeight(this.bheight);
    this.btn3.setMinimumWidth(this.bwidth);
    this.btn3.setMinimumHeight(this.bheight);
    this.btn1.setOnClickListener(new btn1OnClick());
    this.btn2.setOnClickListener(new btn2OnClick());
    this.btn3.setOnClickListener(new btn3OnClick());
    RelativeLayout.LayoutParams localLayoutParams1 = new RelativeLayout.LayoutParams(-2, -2);
    localLayoutParams1.addRule(14, -1);
    localLayoutParams1.addRule(15, -1);
    this._pload = new ProgressBar(this, null, 16842874);
    this._pload.setVisibility(0);
    this._pload.setLayoutParams(localLayoutParams1);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams.gravity = 1;
    localLayoutParams.bottomMargin = Dip(6.0F);
    localLayoutParams.topMargin = Dip(6.0F);
    localLayoutParams.leftMargin = Dip(6.0F);
    localLayoutParams.rightMargin = Dip(6.0F);
    LinearLayout localLinearLayout1 = new LinearLayout(this);
    localLinearLayout1.setOrientation(0);
    View localView1 = new View(this);
    localView1.setLayoutParams(new LinearLayout.LayoutParams(Dip(15.0F), -1));
    localLinearLayout1.addView(this.btn1);
    localLinearLayout1.addView(localView1);
    localLinearLayout1.addView(this.btn2);
    View localView2 = new View(this);
    localView2.setLayoutParams(new LinearLayout.LayoutParams(Dip(15.0F), -1));
    localLinearLayout1.addView(localView2);
    localLinearLayout1.addView(this.btn3);
    localLinearLayout1.setLayoutParams(localLayoutParams);
    this.ll12 = new LinearLayout(this);
    this.ll12.setOrientation(0);
    this.ll12.setLayoutParams(localLayoutParams);
    if (this.scrollView != null)
      this.ll12.addView(this.scrollView);
    LinearLayout localLinearLayout2 = new LinearLayout(this);
    localLinearLayout2.setOrientation(1);
    RelativeLayout.LayoutParams localLayoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
    localLayoutParams2.addRule(8, -1);
    localLayoutParams2.addRule(14, -1);
    localLayoutParams2.addRule(12, -1);
    localLinearLayout2.setLayoutParams(localLayoutParams2);
    localLinearLayout2.addView(this.ll12);
    localLinearLayout2.addView(localLinearLayout1);
    this.main = new RelativeLayout(this);
    this.main.addView(localLinearLayout2);
    this.main.addView(this._pload);
    if (!this.isPro)
    {
    	/*
      RelativeLayout.LayoutParams localLayoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
      localLayoutParams3.addRule(14, -1);
      localLayoutParams3.addRule(10, -1);
      this.adView = new AdView(this, AdSize.BANNER, "8895357a04684bb8");
      this.adView.loadAd(new AdRequest());
      this.adView.setLayoutParams(localLayoutParams3);
      this.ads = new RelativeLayout(this);
      this.ads.addView(this.adView);
      */
    }
    this.main.setVisibility(8);
    this.fl.addView(this.mv);
    this.fl.addView(this.main);
    if (!this.isPro)
      this.fl.addView(this.ads);
    this.fl.setFocusable(true);
    this.fl.setOnTouchListener(new View.OnTouchListener()
    {
      private long lastPressTime;
      private boolean mHasDoubleClicked;

      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        long l = System.currentTimeMillis();
        if (l - this.lastPressTime <= 250L)
        {
          this.mHasDoubleClicked = true;
          this.lastPressTime = l;
          if (this.mHasDoubleClicked)
          {
            if (ViewEvents.this.mv.getDisplayMode() != 8)
            {
            	//break label112;
                if (ViewEvents.this.mv.getDisplayMode() == 4)
                    ViewEvents.this.mv.setDisplayMode(1);
                  else if (ViewEvents.this.mv.getDisplayMode() == 1)
                    ViewEvents.this.mv.setDisplayMode(8);
            }
            else
            {
            	ViewEvents.this.mv.setDisplayMode(4);
            }
          }
        }
        //while (true)
        else
        {
     
          this.mHasDoubleClicked = false;
          new Handler()
          {
            public void handleMessage(Message paramAnonymous2Message)
            {
            	//for compile
            	/*
              if (!ViewEvents.7.this.mHasDoubleClicked)
                ViewEvents.this.TouchControl();
                */
            }
          }
          .sendMessageDelayed(new Message(), 250L);
        }
        if (ViewEvents.this.ll1.getVisibility() == 0)
            ViewEvents.this.TouchControl();
          return false;
      }
    });
    setContentView(this.fl);
  }

  public LiveStream getHttpRes()
  {
    DefaultHttpClient localDefaultHttpClient1 = new DefaultHttpClient();
    HttpResponse localHttpResponse;
    if (this.debug)
      System.out.println("ZmView : we are in getHTTPRes");
    try
    {
      if (URL.contains("https:"))
      {
        DefaultHttpClient localDefaultHttpClient2 = getNewHttpClient();
        HttpGet localHttpGet2 = new HttpGet(URI.create(this.URL));
        localHttpResponse = localDefaultHttpClient2.execute(localHttpGet2);
        localHttpGet2.setHeader("Authorization", "Basic " + Base64.encodeToString(new StringBuilder(String.valueOf(this.zm_user)).append(":").append(this.zm_pass).toString().getBytes(), 0).trim());
      }
      else //(localHttpResponse.getStatusLine().getStatusCode() == 401)
      {
        //return null;
        HttpGet localHttpGet1 = new HttpGet(URI.create(this.URL));
        localHttpResponse = localDefaultHttpClient1.execute(localHttpGet1);
        localHttpGet1.setHeader("Authorization", "Basic " + Base64.encodeToString(new StringBuilder(String.valueOf(this.zm_user)).append(":").append(this.zm_pass).toString().getBytes(), 0).trim());
      }
      LiveStream localLiveStream = new LiveStream(localHttpResponse.getEntity().getContent());
      return localLiveStream;
    }
    catch (ClientProtocolException localClientProtocolException)
    {
      
      localClientProtocolException.printStackTrace();
      return null;
      //LiveStream localLiveStream = new LiveStream(localHttpResponse.getEntity().getContent());
      //return localLiveStream;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return null;
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
    getWindow().addFlags(128);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    this.Eprefs = new ObscuredSharedPreferences(this, getSharedPreferences("zmview.cfg", 0));
    this.isPro = this.Eprefs.getBoolean("isPro", false);
    this.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    Intent localIntent = new Intent("zmview.BIND");
    getApplicationContext().bindService(localIntent, this._getDataServiceConnection, 1);
    if (this.debug)
      System.out.println("ZmView we try to connect to service2");
    Bundle localBundle = getIntent().getExtras();
    this.zm_user = localBundle.getString("zm_user");
    this.zm_pass = localBundle.getString("zm_pass");
    this.zm_auth = localBundle.getString("zm_auth");
    this.zm_host = localBundle.getString("zm_host");
    this.event_time = localBundle.getInt("event_time");
    this.sFilterEvents = localBundle.getString("sFilterEvents");
    this.zm_get_auth = localBundle.getString("zm_get_auth");
    this.ZmID = localBundle.getInt("ZmID");
    this.actionId = localBundle.getInt("actionId");
    this.event_id = localBundle.getString("event_id");
    this.selected_page = localBundle.getInt("selected_page");
    this.nr_pages = localBundle.getInt("nr_pages");
    if (this.debug)
      System.out.println("ZmView we receive nr_pages=" + String.valueOf(this.nr_pages) + " selected_page" + String.valueOf(this.selected_page));
    if (localBundle.getSerializable("myZmCamera") != null)
      this.myZmCamera = ((ArrayList)localBundle.getSerializable("myZmCamera"));
    this.mv = new LiveViewImg(this);
    this.mv.setBitmap(BitmapFactory.decodeResource(getResources(), 17301567));
    this.mv.setMrun(Boolean.valueOf(false));
    display_events();
    if (this.debug)
      System.out.println("ZmView mv-thread is running");
    this._initTask = ((InitTask)getLastNonConfigurationInstance());
    if (this._initTask == null)
    {
      if (this.debug)
        System.out.println("ZmView : create new task");
      this._initTask = new InitTask(this);
      if (Build.VERSION.SDK_INT <= 10)
        this._initTask.execute(new Context[] { this });
    }
    try
    {
      if (this.zm_get_auth == null);
      for (this.URL = (this.prefs.getString("zm_bin_path", "../nph-zms") + "?source=event&mode=jpeg&event=" + this.event_id + "&frame=1&scale=100&rate=100&maxfps=5&replay=single&" + this.zm_get_auth + "&user=" + URLEncoder.encode(this.zm_user, "UTF-8") + "&pass=" + URLEncoder.encode(this.zm_pass, "UTF-8")); ; this.URL = (this.prefs.getString("zm_bin_path", "../nph-zms") + "?source=event&mode=jpeg&event=" + this.event_id + "&frame=1&scale=100&rate=100&maxfps=5&replay=single&" + this.zm_get_auth))
      {
        if (this.debug)
          System.out.println("ZmView : URL=" + this.URL);
        this.doread = new DoRead();
        if (Build.VERSION.SDK_INT > 10)
        {
            //continue;
            //label816: 
            DoRead localDoRead1 = this.doread;
            Executor localExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
            String[] arrayOfString1 = new String[4];
            arrayOfString1[0] = this.URL;
            arrayOfString1[1] = this.zm_user;
            arrayOfString1[2] = this.zm_pass;
            arrayOfString1[3] = this.zm_auth;
            localDoRead1.executeOnExecutor(localExecutor, arrayOfString1);
        }
        else 
        {
        	DoRead localDoRead2 = this.doread;       
	        String[] arrayOfString2 = new String[4];
	        arrayOfString2[0] = this.URL;
	        arrayOfString2[1] = this.zm_user;
	        arrayOfString2[2] = this.zm_pass;
	        arrayOfString2[3] = this.zm_auth;
	        localDoRead2.execute(arrayOfString2);
	        TouchControl();
	        /*
	        return;
	        this._initTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Context[] { this });
	        break;
	        this._initTask.attach(this);
	        if (!this.debug)
	          break;
	        System.out.println("ZmView : Attach to task onCreate");
	        break;
	        */
        }
        _initTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Context[] { this });
        //break;
        _initTask.attach(this);
        //if (!this.debug)
        //  break;
        System.out.println("ZmView : Attach to task onCreate");
      }
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      //while (true)
      {
        localUnsupportedEncodingException.printStackTrace();
  
      }
    }
  }

  public void onPause()
  {
    super.onPause();
    this.mv.stopPlayback();
  }

  public Object onRetainNonConfigurationInstance()
  {
    this._initTask.detach();
    if (this.debug)
      System.out.println("ZmView : We do resume OnRetain");
    return this._initTask;
  }

  public void onStart()
  {
    super.onStart();
    if (this._initTask.isCancelled())
    {
      if (this.debug)
        System.out.println("ZmView : Task is not running onStart ");
      if (this.debug)
        System.out.println("ZmView : create new task from onStart");
      this._initTask = new InitTask(this);
      if (Build.VERSION.SDK_INT <= 10)
        this._initTask.execute(new Context[] { this });
    }
    //while (true)
    else 
    {
      //return;
      this._initTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Context[] { this });
      //continue;
      //this._initTask.attach(this);
    }
    if (this.mv != null)
        this.mv.startPlayback();
      if (this.debug)
        System.out.println("ZmView : We finish onStart ");
  }

  public void onStop()
  {
    super.onStop();
    if (this.debug)
      System.out.println("ZmView : We finish onStop ");
    this._initTask.cancel(true);
  }

  protected void show_scrollview()
  {
	/*
    if (this.scrollView != null)
      this.ll12.removeView(this.scrollView);
    this.scrollView = new HorizontalScrollView(this);
    LinearLayout localLinearLayout1 = new LinearLayout(this);
    localLinearLayout1.setOrientation(0);
    LinearLayout localLinearLayout2 = new LinearLayout(this);
    localLinearLayout2.setOrientation(1);
    ImageView localImageView1 = new ImageView(this);
    int i;
    label185: LinearLayout localLinearLayout3;
    ImageView localImageView2;
    if ((this.selected_page > 0) && (!this.load_first_page))
    {
      localImageView1.setImageResource(2130837660);
      localImageView1.setScaleType(ImageView.ScaleType.FIT_XY);
      localImageView1.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
      View localView1 = new View(this);
      localView1.setLayoutParams(new LinearLayout.LayoutParams(Dip(15.0F), -1));
      localLinearLayout2.addView(localImageView1);
      localLinearLayout1.addView(localLinearLayout2);
      localLinearLayout1.addView(localView1);
      if (this.selected_page > 0)
        localImageView1.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramAnonymousView)
          {
            if (ViewEvents.this.isPro)
            {
              ViewEvents.this._pload.setVisibility(0);
              ViewEvents localViewEvents = ViewEvents.this;
              localViewEvents.selected_page = (-1 + localViewEvents.selected_page);
              ViewEvents.this.load_events = -1;
              return;
            }
            ViewEvents.this.ShowDialogPro();
          }
        });
      if (this.myZmEvents != null)
      {
        i = 0;
        if (i < this.myZmEvents.size())
          break label427;
      }
      localLinearLayout3 = new LinearLayout(this);
      localLinearLayout3.setOrientation(1);
      localImageView2 = new ImageView(this);
      if (1 + this.selected_page >= this.nr_pages)
        break label663;
      localImageView2.setImageResource(2130837655);
    }
    while (true)
    {
      localImageView2.setScaleType(ImageView.ScaleType.FIT_XY);
      localImageView2.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
      View localView2 = new View(this);
      localView2.setLayoutParams(new LinearLayout.LayoutParams(Dip(15.0F), -1));
      localLinearLayout3.addView(localImageView2);
      localLinearLayout1.addView(localLinearLayout3);
      localLinearLayout1.addView(localView2);
      if (1 + this.selected_page < this.nr_pages)
        localImageView2.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramAnonymousView)
          {
            if (ViewEvents.this.isPro)
            {
              ViewEvents.this._pload.setVisibility(0);
              ViewEvents localViewEvents = ViewEvents.this;
              localViewEvents.selected_page = (1 + localViewEvents.selected_page);
              ViewEvents.this.load_events = 0;
              return;
            }
            ViewEvents.this.ShowDialogPro();
          }
        });
      this.scrollView.addView(localLinearLayout1);
      RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
      localLayoutParams.addRule(8, -1);
      localLayoutParams.addRule(14, -1);
      localLayoutParams.addRule(12, -1);
      this.scrollView.setHorizontalScrollBarEnabled(false);
      this.scrollView.setLayoutParams(localLayoutParams);
      this.ll12.addView(this.scrollView);
      return;
      localImageView1.setImageResource(2130837661);
      break;
      label427: LinearLayout localLinearLayout4 = new LinearLayout(this);
      localLinearLayout4.setOrientation(1);
      final TextView localTextView = new TextView(this);
      localTextView.setTag(Integer.valueOf(i));
      localTextView.setMaxLines(2);
      localTextView.setText(((zmEvents)this.myZmEvents.get(i)).getID() + "\n" + ((zmEvents)this.myZmEvents.get(i)).getTime());
      localTextView.setPadding(0, Dip(10.0F), 0, 0);
      localTextView.setTextSize(Dip(9.0F));
      localTextView.setGravity(17);
      localTextView.setTextColor(-16777216);
      localTextView.setBackgroundResource(2130837625);
      localTextView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
      View localView3 = new View(this);
      localView3.setLayoutParams(new LinearLayout.LayoutParams(Dip(15.0F), -1));
      localLinearLayout4.addView(localTextView);
      localLinearLayout1.addView(localLinearLayout4);
      localLinearLayout1.addView(localView3);
      localTextView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          String str = String.valueOf(localTextView.getTag());
          ViewEvents.this.ev_id = Integer.valueOf(str).intValue();
          try
          {
            if (ViewEvents.this.zm_get_auth == null);
            for (ViewEvents.this.URL = (ViewEvents.this.prefs.getString("zm_bin_path", "../nph-zms") + "?source=event&mode=jpeg&event=" + ((zmEvents)ViewEvents.this.myZmEvents.get(Integer.valueOf(str).intValue())).getID() + "&frame=1&scale=100&rate=100&maxfps=5&replay=single&" + ViewEvents.this.zm_get_auth + "&user=" + URLEncoder.encode(ViewEvents.this.zm_user, "UTF-8") + "&pass=" + URLEncoder.encode(ViewEvents.this.zm_pass, "UTF-8")); ; ViewEvents.this.URL = (ViewEvents.this.prefs.getString("zm_bin_path", "../nph-zms") + "?source=event&mode=jpeg&event=" + ((zmEvents)ViewEvents.this.myZmEvents.get(Integer.valueOf(str).intValue())).getID() + "&frame=1&scale=100&rate=100&maxfps=5&replay=single&" + ViewEvents.this.zm_get_auth))
            {
              if (ViewEvents.this.debug)
                System.out.println("ZmView  url : " + ViewEvents.this.URL);
              if (ViewEvents.this.last_img_selected != null)
                ViewEvents.this.last_img_selected.setBackgroundResource(2130837625);
              paramAnonymousView.setBackgroundResource(2130837626);
              ViewEvents.this.last_img_selected = paramAnonymousView;
              paramAnonymousView.buildDrawingCache();
              paramAnonymousView.getDrawingCache();
              ViewEvents.this._pload.setVisibility(0);
              ViewEvents.this.showloading = true;
              ViewEvents.this.mv.surfaceCreated(null);
              ViewEvents.this.mv.setMrun(Boolean.valueOf(false));
              ViewEvents.this.mv.setTime(Integer.valueOf(((zmEvents)ViewEvents.this.myZmEvents.get(ViewEvents.this.ev_id)).getDuration()).intValue());
              ViewEvents.this.doread = null;
              ViewEvents.this.doread = new ViewEvents.DoRead(ViewEvents.this);
              if (Build.VERSION.SDK_INT > 10)
                break;
              ViewEvents.DoRead localDoRead2 = ViewEvents.this.doread;
              String[] arrayOfString2 = new String[4];
              arrayOfString2[0] = ViewEvents.this.URL;
              arrayOfString2[1] = ViewEvents.this.zm_user;
              arrayOfString2[2] = ViewEvents.this.zm_pass;
              arrayOfString2[3] = ViewEvents.this.zm_auth;
              localDoRead2.execute(arrayOfString2);
              return;
            }
          }
          catch (NumberFormatException localNumberFormatException)
          {
            while (true)
              localNumberFormatException.printStackTrace();
          }
          catch (UnsupportedEncodingException localUnsupportedEncodingException)
          {
            while (true)
              localUnsupportedEncodingException.printStackTrace();
            ViewEvents.DoRead localDoRead1 = ViewEvents.this.doread;
            Executor localExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
            String[] arrayOfString1 = new String[4];
            arrayOfString1[0] = ViewEvents.this.URL;
            arrayOfString1[1] = ViewEvents.this.zm_user;
            arrayOfString1[2] = ViewEvents.this.zm_pass;
            arrayOfString1[3] = ViewEvents.this.zm_auth;
            localDoRead1.executeOnExecutor(localExecutor, arrayOfString1);
          }
        }
      });
      i++;
      break label185;
      label663: localImageView2.setImageResource(2130837656);
    }
    */
  }

  public class DoRead extends AsyncTask<String, Void, LiveStream>
  {
    public DoRead()
    {
    }

    protected LiveStream doInBackground(String[] paramArrayOfString)
    {
      if (ViewEvents.this.debug)
        System.out.println("ZmView : we are in LiveStream Doread");
      return new LiveStream(ViewEvents.this.getHttpRes());
    }

    protected void onCancelled()
    {
      super.onCancelled();
      Log.i("makemachine", "onCancelled()");
    }

    protected void onPostExecute(LiveStream paramLiveStream)
    {
      ViewEvents.this.mv.setSource(paramLiveStream);
      if (ViewEvents.this.ViewDisplayMode == 10)
        ViewEvents.this.mv.setDisplayMode(ViewEvents.this.mv.getDisplayMode());
      //while (true)
      else
      {
        ViewEvents.this.mv.setDisplayMode(ViewEvents.this.ViewDisplayMode);
      }
      ViewEvents.this.mv.showFps(true);
      ViewEvents.this.mv.setMrun(Boolean.valueOf(true));
      
    }
  }

  protected class InitTask extends AsyncTask<Context, Integer, String>
  {
    ViewEvents activity = null;

    InitTask(ViewEvents arg2)
    {
      //ViewEvents localViewEvents;
      attach(arg2/*localViewEvents*/);
    }

    void attach(ViewEvents paramViewEvents)
    {
      this.activity = paramViewEvents;
    }

    void detach()
    {
      this.activity = null;
    }

    protected String doInBackground(Context[] paramArrayOfContext)
    {
      if (isCancelled())
        return "COMPLETE!";
      while (true)
      {
        ArrayList localArrayList2;
        //for compile
        ArrayList localArrayList1;
        int j;
        try
        {
          Thread.sleep(1000L);
          if ((ViewEvents.this.ServiceConnected.booleanValue()) && ((ViewEvents.this.load_events == 0) || (ViewEvents.this.load_events == -1)))
          {
            if (ViewEvents.this.selected_page == 0)
              ViewEvents.this.load_first_page = true;
            if (ViewEvents.this.load_events != 0)
            {
            	//break label410;
	        	localArrayList1 = ViewEvents.this.myZmEvents;
	            ViewEvents.this.myZmEvents = ViewEvents.this._getDataService.getEvents(((ZmCamera)ViewEvents.this.myZmCamera.get(ViewEvents.this.ZmID)).getID(), String.valueOf(ViewEvents.this.selected_page), ViewEvents.this.zm_get_auth, ViewEvents.this.sFilterEvents);
	            for (int i = 0; i < localArrayList1.size(); i++)
	              ViewEvents.this.myZmEvents.add((zmEvents)localArrayList1.get(i));
            }
            else
            {
	            ViewEvents.this.load_events = 1;
	            if (ViewEvents.this.myZmEvents == null)
	            {
	            	//break label343;
	            	myZmEvents = _getDataService.getEvents(((ZmCamera)myZmCamera.get(ZmID)).getID(), String.valueOf(selected_page), zm_get_auth, sFilterEvents);
	                //continue;
	            }
	            else	            	
	            {
		            localArrayList2 = ViewEvents.this._getDataService.getEvents(((ZmCamera)ViewEvents.this.myZmCamera.get(ViewEvents.this.ZmID)).getID(), String.valueOf(ViewEvents.this.selected_page), ViewEvents.this.zm_get_auth, ViewEvents.this.sFilterEvents);         
		            j = 0;
		            if (j < localArrayList2.size())
		            {
		            	//break label316;
		                //label316: 
		                ViewEvents.this.myZmEvents.add((zmEvents)localArrayList2.get(j));
		            	j++;
		            }
		            else
		            {
		            	if (ViewEvents.this.debug)
						{
		            		System.out.println("ZmView load events from page " + String.valueOf(ViewEvents.this.selected_page) + " size=" + String.valueOf(ViewEvents.this.myZmEvents.size()));						
							ViewEvents.this.load_events = 2;
							if ((ViewEvents.this.showloading) && (ViewEvents.this.mv != null) && (ViewEvents.this.mv.getImg()))
							  ViewEvents.this.showloading = false;
						}
		            }
	            }
            }
          }
          Integer[] arrayOfInteger = new Integer[1];
          arrayOfInteger[0] = Integer.valueOf(0);
          publishProgress(arrayOfInteger);
        }
        catch (Exception localException)
        {
          Log.i("makemachine", localException.getMessage());
        }
        
        //break;
        //label316: ViewEvents.this.myZmEvents.add((zmEvents)localArrayList2.get(j));
        //j++;
        //continue;
        //label343: ViewEvents.this.myZmEvents = ViewEvents.this._getDataService.getEvents(((ZmCamera)ViewEvents.this.myZmCamera.get(ViewEvents.this.ZmID)).getID(), String.valueOf(ViewEvents.this.selected_page), ViewEvents.this.zm_get_auth, ViewEvents.this.sFilterEvents);
        //continue;
        //label410: /*ArrayList*/ localArrayList1 = ViewEvents.this.myZmEvents;
        //ViewEvents.this.myZmEvents = ViewEvents.this._getDataService.getEvents(((ZmCamera)ViewEvents.this.myZmCamera.get(ViewEvents.this.ZmID)).getID(), String.valueOf(ViewEvents.this.selected_page), ViewEvents.this.zm_get_auth, ViewEvents.this.sFilterEvents);
        //for (int i = 0; i < localArrayList1.size(); i++)
        //  ViewEvents.this.myZmEvents.add((zmEvents)localArrayList1.get(i));
        
      }
    }

    protected void onCancelled()
    {
      super.onCancelled();
    }

    protected void onPostExecute(String paramString)
    {
      super.onPostExecute(paramString);
    }

    protected void onPreExecute()
    {
      Log.i("makemachine", "onPreExecute()");
      super.onPreExecute();
    }

    protected void onProgressUpdate(Integer[] paramArrayOfInteger)
    {
      super.onProgressUpdate(paramArrayOfInteger);
      if (ViewEvents.this.load_events == 2)
      {
        ViewEvents.this.load_events = 3;
        ViewEvents.this.show_scrollview();
      }
      if (ViewEvents.this.load_events < 3)
        ViewEvents.this._pload.setVisibility(0);
      //while (true)
      else
      {
        ViewEvents.this._pload.setVisibility(8);
      }
      if ((!ViewEvents.this.showloading) && (ViewEvents.this._pload != null))
          ViewEvents.this._pload.setVisibility(8);
        
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
      ViewEvents.this.mv.setDisplayMode(8);
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
      ViewEvents.this.mv.setDisplayMode(4);
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
      ViewEvents.this.mv.setDisplayMode(1);
    }
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.ViewEvents
 * JD-Core Version:    0.6.2
 */
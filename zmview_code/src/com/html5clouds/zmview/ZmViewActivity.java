package com.html5clouds.zmview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
//import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
//import com.google.ads.AdRequest;
//import com.google.ads.AdView;
import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ZmViewActivity extends SherlockActivity
{
  private static final String BASIC_AUTH = "Basic Auth";
  private static final int ID_MONITOR_EVENTS = 2;
  private static final int ID_MONITOR_EVENTSD = 4;
  private static final int ID_MONITOR_EVENTSH = 3;
  private static final int ID_MONITOR_EVENTSM = 6;
  private static final int ID_MONITOR_EVENTSW = 5;
  private static final int ID_MONITOR_LIVE = 1;
  static final int START_SETTINGS_FOR_EDIT = 2;
  static final int START_SETTINGS_FOR_NEW = 1;
  private SharedPreferences Eprefs;
  private ExpandableListView MainServerList;
  protected String MonitorID;
  protected String MonitorName;
  private ProgressDialog PLoad;
  private AlertDialog ProAlert;
  public View VIEW;
  protected int ZmID;
  private ZmServersAdapter _ZmServersAdapter;
  private InitTask _initTask = null;
  //private AdView adView;
  private AlertDialog alert;
  private int android_version;
  private Boolean debug = Boolean.valueOf(true);
  private Dialog dialog_monitor;
  private int downloadedSize;
  private int edit_server;
  private int events_type = 0;
  private View header;
  protected boolean isPro = false;
  protected MenuItem item1;
  private Integer last_page_selected = Integer.valueOf(-1);
  private ListView list;
  private ArrayList<HashMap<String, String>> listEvents;
  protected int loading_server;
  protected boolean mPLoad;
  protected QuickAction mQuickAction;
  protected Menu menu;
  private String messageLog;
  private String monitor;
  private XmlHandler myCfgHandler;
  private ArrayList<ZmCamera> myZmCamera;
  private ArrayList<ZmServers> myZmServers;
  public Integer nr_pages = Integer.valueOf(0);
  private int nr_servers;
  private SharedPreferences prefs;
  private String sFilterEvents;
  private Spinner scontrol;
  private Integer selected_page = Integer.valueOf(1);
  private int show_events = 0;
  private int show_monitors = 4;
  private String str;
  private View tmpImgRefresh;
  private View tmpProgRefresh;
  private int totalSize;
  private HttpURLConnection uc;
  private HttpsURLConnection ucs;
  private URL url1;
  private String zm_auth;
  public String zm_get_auth;
  private String zm_host;
  private String zm_pass;
  private String zm_user;

  protected static boolean isProInstalled(Context paramContext)
  {
    return paramContext.getPackageManager().checkSignatures(paramContext.getPackageName(), "com.html5clouds.zmview.pro") == 0;
  }

  private static int roundUp(double paramDouble)
  {
    if (paramDouble > (int)paramDouble)
      return 1 + (int)paramDouble;
    return (int)paramDouble;
  }

  private void show_message(String paramString)
  {
    TextView localTextView = new TextView(this);
    localTextView.setText(paramString.split(":")[0]);
    localTextView.setTextSize(18.0F);
    localTextView.setSingleLine(false);
    localTextView.setGravity(1);
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setView(localTextView).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
      }
    });
    localBuilder.create().show();
  }

  public void DeleteServer(final Integer paramInteger)
  {
    if (paramInteger.intValue() != 0)
    {
      this.tmpProgRefresh = null;
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
      localBuilder.setMessage("Are you sure you want to delete server " + ((ZmServers)this.myZmServers.get(paramInteger.intValue())).getName() + " ?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          int i = -1;
          if (tmpProgRefresh != null)
          {
            tmpProgRefresh.setVisibility(8);
            tmpImgRefresh.setVisibility(0);
          }
          myZmServers.clear();
          for (int j = 0; ; j++)
          {
            if (j >= nr_servers)
            {
              ZmViewActivity localZmViewActivity = ZmViewActivity.this;
              localZmViewActivity.nr_servers = (-1 + localZmViewActivity.nr_servers);
              prefs.edit().putInt("nr_servers", nr_servers).commit();
              create_server_list();
              return;
            }
            if (j != paramInteger.intValue())
            {
              i++;
              prefs.edit().putString("zm_name_" + String.valueOf(i), prefs.getString("zm_name_" + String.valueOf(j), "Unknown")).commit();
              prefs.edit().putString("zm_host_" + String.valueOf(i), prefs.getString("zm_host_" + String.valueOf(j), null)).commit();
              prefs.edit().putString("zm_user_" + String.valueOf(i), prefs.getString("zm_user_" + String.valueOf(j), null)).commit();
              prefs.edit().putString("zm_pass_" + String.valueOf(i), prefs.getString("zm_pass_" + String.valueOf(j), null)).commit();
              ZmServers localZmServers = new ZmServers();
              localZmServers.setName(prefs.getString("zm_name_" + String.valueOf(j), "Unknown"));
              localZmServers.setHost(prefs.getString("zm_host_" + String.valueOf(j), ""));
              localZmServers.setUser(prefs.getString("zm_user_" + String.valueOf(j), ""));
              localZmServers.setPass(prefs.getString("zm_pass_" + String.valueOf(j), ""));
              myZmServers.add(localZmServers);
            }
          }
        }
      }).setNegativeButton("No", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          ProAlert.dismiss();
        }
      });
      this.ProAlert = localBuilder.create();
      this.ProAlert.show();
    }
  }

  public ArrayList<ZmCamera> GetmyZmCamera()
  {
    return this.myZmCamera;
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
        startActivity(localIntent);
      }
    }).setNegativeButton("Back", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        ProAlert.dismiss();
      }
    });
    this.ProAlert = localBuilder.create();
    this.ProAlert.show();
  }

  public void ShowLoading(Boolean paramBoolean)
  {
    if (this.PLoad == null)
      this.PLoad = ProgressDialog.show(this, "", "Loading....", true, true, new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface paramAnonymousDialogInterface)
        {
          PLoad.dismiss();
        }
      });
    if (paramBoolean.booleanValue())
      this.PLoad.show();
    if (!paramBoolean.booleanValue())
      this.PLoad.dismiss();
  }

  public void ShowLoadingMenu(Boolean paramBoolean)
  {
    if (paramBoolean.booleanValue())
    {
      this.item1.setActionView(2130903069);
      return;
    }
    this.item1.setActionView(null);
  }

  public void create_monitor_action()
  {
    Drawable localDrawable = getResources().getDrawable(R.drawable.event_icon_locked);
    if (isPro)
      localDrawable = getResources().getDrawable(R.drawable.event_icon);
    ActionItem localActionItem1 = new ActionItem(1, "Live", getResources().getDrawable(R.drawable.video_icon));
    ActionItem localActionItem2 = new ActionItem(3, "Last Hour", getResources().getDrawable(R.drawable.event_icon));
    ActionItem localActionItem3 = new ActionItem(4, "Last Day", localDrawable);
    ActionItem localActionItem4 = new ActionItem(5, "Last Week", localDrawable);
    ActionItem localActionItem5 = new ActionItem(6, "Last Month", localDrawable);
    ActionItem localActionItem6 = new ActionItem(2, "All Events", localDrawable);
    localActionItem1.setSticky(true);
    localActionItem6.setSticky(false);
    mQuickAction = new QuickAction(this, 1);
    mQuickAction.addActionItem(localActionItem1);
    mQuickAction.addActionItem(localActionItem2);
    mQuickAction.addActionItem(localActionItem3);
    mQuickAction.addActionItem(localActionItem4);
    mQuickAction.addActionItem(localActionItem5);
    mQuickAction.addActionItem(localActionItem6);
    mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener()
    {
      public void onItemClick(QuickAction paramAnonymousQuickAction, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        paramAnonymousQuickAction.getActionItem(paramAnonymousInt1);
        if (paramAnonymousInt2 == 1)
        {
          if(false)
          {
        	  Intent localIntent = new Intent(ZmViewActivity.this, LiveMonitor.class);
	          localIntent.putExtra("URL", zm_host + "/../cgi-bin/nph-zms?mode=mpeg&monitor=" + MonitorID + "&scale=75&maxfps=5&bitrate=8000&format=swf");
	          localIntent.putExtra("myZmCamera", myZmCamera);
	          localIntent.putExtra("ZmID", ZmID);
	          localIntent.putExtra("zm_user", zm_user);
	          localIntent.putExtra("zm_pass", zm_pass);
	          localIntent.putExtra("zm_auth", zm_auth);
	          localIntent.putExtra("zm_host", zm_host);
	          localIntent.putExtra("actionId", paramAnonymousInt2);
	          localIntent.putExtra("zm_get_auth", zm_get_auth);
	          startActivity(localIntent);
          }
          else
          {
        	  Intent localIntent_1 = new Intent(ZmViewActivity.this, ZmFlashPlayerView.class);
         	  String VideoUrls = zm_host + "/../cgi-bin/nph-zms?mode=mpeg&monitor=" + MonitorID + "&scale=75&maxfps=5&bitrate=8000&format=swf";
         	  //VideoUrls  = zm_host + VideoUrls;
         	  localIntent_1.putExtra("VideoUrls", VideoUrls);
               //localIntent.putExtra("vid", localVideoDetailData.getVideoId());
               //localIntent.putExtra("reqpage", 0);
               //localIntent.putExtra("flashparam", localVideoDetailData.getFlashParameters());
              startActivity(localIntent_1);
          }
          
          
        }
        int i;
        if ((paramAnonymousInt2 >= 2) && (paramAnonymousInt2 <= 6))
        {
          i = 0;
          if (paramAnonymousInt2 == 2)
            i = 1;
          if (paramAnonymousInt2 == 3)
            i = 1;
          if (paramAnonymousInt2 == 4)
            i = 1;
          if (paramAnonymousInt2 == 5)
            i = 1;
          if (paramAnonymousInt2 == 6)
            i = 1;
          if ((!isPro) && ((paramAnonymousInt2 == 2) || (paramAnonymousInt2 == 4) || (paramAnonymousInt2 == 5) || (paramAnonymousInt2 == 6)))
            ShowDialogPro();
        }
        else
        {
          return;
        }
        if (i != 0)
        {
          events_type = paramAnonymousInt2;
          setContentView(R.layout.mainviewevents);
          show_monitors = 0;
          if (debug.booleanValue())
            System.out.println("ZmView ZmID2=" + String.valueOf(ZmID));
          last_page_selected = Integer.valueOf(-1);
          nr_pages = Integer.valueOf(0);
          show_events(Integer.valueOf(ZmID), MonitorID, "1", MonitorName);
          ShowLoading(Boolean.valueOf(true));
          if (!isPro)
          {/*
            adView = ((AdView)findViewById(2131099704));
            adView.setVisibility(0);
            adView.loadAd(new AdRequest());
            */
          }
          selected_page = Integer.valueOf(0);
          create_prev_next();
          return;
        }
        Toast.makeText(getApplicationContext(), "No Events !", 0).show();
      }
    });
    this.mQuickAction.setOnDismissListener(new QuickAction.OnDismissListener()
    {
      public void onDismiss()
      {
      }
    });
  }

  public void create_monitor_list()
  {
    if (debug.booleanValue())
      System.out.println("ZmView - listview add header");
    camviewAdapter localcamviewAdapter = new camviewAdapter(this, R.layout.monitorview1);
    list.setAdapter(localcamviewAdapter);
    list.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        ZmID = paramAnonymousInt;
        MonitorID = ((ZmCamera)myZmCamera.get(paramAnonymousInt)).getID();
        MonitorName = ((ZmCamera)myZmCamera.get(paramAnonymousInt)).getName();
        if (debug.booleanValue())
          System.out.println("ZmView : ZmID=" + String.valueOf(ZmID));
        create_monitor_action();
        mQuickAction.show(paramAnonymousView);
      }
    });
  }

  public void create_prev_next()
  {
    ImageButton localImageButton1 = (ImageButton)findViewById(R.id.btnprev);
    ImageButton localImageButton2 = (ImageButton)findViewById(R.id.btnnext);
    localImageButton1.setOnClickListener(new btnPrevClick());
    localImageButton2.setOnClickListener(new btnNextClick());
  }

  public void create_server_list()
  {
    MainServerList = ((ExpandableListView)findViewById(R.id.listView1));
    _ZmServersAdapter = new ZmServersAdapter(this, myZmServers);
    MainServerList.setAdapter(_ZmServersAdapter);
    MainServerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
    {
      public boolean onGroupClick(ExpandableListView paramAnonymousExpandableListView, View paramAnonymousView, final int paramAnonymousInt, long paramAnonymousLong)
      {
        final ImageView localImageView1 = (ImageView)paramAnonymousView.findViewById(R.id.imgRefresh);
        final ProgressBar localProgressBar = (ProgressBar)paramAnonymousView.findViewById(R.id.progRefresh);
        ImageView localImageView2 = (ImageView)paramAnonymousView.findViewById(R.id.imgSettings);
        ImageView localImageView3 = (ImageView)paramAnonymousView.findViewById(R.id.imgDelete);
        localImageView1.setTag(Integer.valueOf(paramAnonymousInt));
        localImageView2.setTag(Integer.valueOf(paramAnonymousInt));
        localImageView3.setTag(Integer.valueOf(paramAnonymousInt));
        System.out.println("ZmView : we selected server " + String.valueOf(paramAnonymousInt));
        localProgressBar.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramAnonymous2View)
          {
            if (tmpProgRefresh.getVisibility() == 0)
            {
              localProgressBar.setVisibility(8);
              localImageView1.setVisibility(0);
              if (ucs == null)
            	  uc.disconnect();
              else
        	  {
                tmpProgRefresh = localProgressBar;
                tmpImgRefresh = localImageView1;
        	  	ucs.disconnect();
        	  }
            }
            /*
            while (true)
            {
              tmpProgRefresh = localProgressBar;
              tmpImgRefresh = localImageView1;
              return;
              label88: uc.disconnect();
            }*/
          }
        });
        localImageView1.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramAnonymous2View)
          {
            if ((tmpProgRefresh == null) || (tmpProgRefresh.getVisibility() != 0))
            {
              localProgressBar.setVisibility(0);
              localImageView1.setVisibility(8);
              tmpProgRefresh = localProgressBar;
              tmpImgRefresh = localImageView1;
              prefs.edit().putString("zm_name", prefs.getString("zm_name_" + String.valueOf(paramAnonymousInt), "Unknown")).commit();
              prefs.edit().putString("zm_host", prefs.getString("zm_host_" + String.valueOf(paramAnonymousInt), "http://www.yourserver.com/zm")).commit();
              prefs.edit().putString("zm_user", prefs.getString("zm_user_" + String.valueOf(paramAnonymousInt), "admin")).commit();
              prefs.edit().putString("zm_pass", prefs.getString("zm_pass_" + String.valueOf(paramAnonymousInt), "admin")).commit();
              prefs.edit().putString("zm_bin_path", prefs.getString("zm_bin_path_" + String.valueOf(paramAnonymousInt), null)).commit();
              load_settings();
              loading_server = ((Integer)localImageView1.getTag()).intValue();
              show_monitors = 1;
            }
          }
        });
        localImageView2.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramAnonymous2View)
          {
            Intent localIntent = new Intent(ZmViewActivity.this, Settings.class);
            prefs.edit().putString("zm_name", prefs.getString("zm_name_" + String.valueOf(paramAnonymousInt), "Unknown")).commit();
            prefs.edit().putString("zm_host", prefs.getString("zm_host_" + String.valueOf(paramAnonymousInt), "http://www.yourserver.com/zm")).commit();
            prefs.edit().putString("zm_user", prefs.getString("zm_user_" + String.valueOf(paramAnonymousInt), "admin")).commit();
            prefs.edit().putString("zm_pass", prefs.getString("zm_pass_" + String.valueOf(paramAnonymousInt), "admin")).commit();
            prefs.edit().putString("zm_bin_path", prefs.getString("zm_bin_path_" + String.valueOf(paramAnonymousInt), null)).commit();
            edit_server = paramAnonymousInt;
            localIntent.putExtra("setting_options", 2);
            startActivityForResult(localIntent, 2);
          }
        });
        localImageView3.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramAnonymous2View)
          {
            DeleteServer(Integer.valueOf(paramAnonymousInt));
          }
        });
        if (((ZmServers)myZmServers.get(paramAnonymousInt)).getItems() != null)
          return false;
        if ((tmpProgRefresh == null) || (tmpProgRefresh.getVisibility() != 0))
        {
          tmpProgRefresh = localProgressBar;
          tmpImgRefresh = localImageView1;
          localProgressBar.setVisibility(0);
          localImageView1.setVisibility(8);
          loading_server = paramAnonymousInt;
          prefs.edit().putString("zm_name", prefs.getString("zm_name_" + String.valueOf(loading_server), "Unknown")).commit();
          prefs.edit().putString("zm_host", prefs.getString("zm_host_" + String.valueOf(loading_server), "http://www.yourserver.com/zm")).commit();
          prefs.edit().putString("zm_user", prefs.getString("zm_user_" + String.valueOf(loading_server), "admin")).commit();
          prefs.edit().putString("zm_pass", prefs.getString("zm_pass_" + String.valueOf(loading_server), "admin")).commit();
          prefs.edit().putString("zm_auth", prefs.getString("zm_auth_" + String.valueOf(loading_server), null)).commit();
          prefs.edit().putString("zm_bin_path", prefs.getString("zm_bin_path_" + String.valueOf(loading_server), null)).commit();
          load_settings();
          show_monitors = 1;
        }
        return true;
      }
    });
    this.MainServerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
    {
      public boolean onChildClick(ExpandableListView paramAnonymousExpandableListView, View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, long paramAnonymousLong)
      {
        ZmID = paramAnonymousInt2;
        myZmCamera = ((ZmServers)myZmServers.get(paramAnonymousInt1)).getItems();
        MonitorID = ((ZmCamera)myZmCamera.get(ZmID)).getID();
        MonitorName = ((ZmCamera)myZmCamera.get(ZmID)).getName();
        prefs.edit().putString("zm_name", prefs.getString("zm_name_" + String.valueOf(paramAnonymousInt1), "Unknown")).commit();
        prefs.edit().putString("zm_host", prefs.getString("zm_host_" + String.valueOf(paramAnonymousInt1), "http://www.yourserver.com/zm")).commit();
        prefs.edit().putString("zm_user", prefs.getString("zm_user_" + String.valueOf(paramAnonymousInt1), "admin")).commit();
        prefs.edit().putString("zm_pass", prefs.getString("zm_pass_" + String.valueOf(paramAnonymousInt1), "admin")).commit();
        prefs.edit().putString("zm_auth", prefs.getString("zm_auth_" + String.valueOf(paramAnonymousInt1), null)).commit();
        prefs.edit().putString("zm_bin_path", prefs.getString("zm_bin_path_" + String.valueOf(paramAnonymousInt1), null)).commit();
        load_settings();
        if (debug.booleanValue())
          System.out.println("ZmView : HOST=" + prefs.getString(new StringBuilder("zm_bin_path_").append(String.valueOf(paramAnonymousInt1)).toString(), "http://www.yourserver.com/zm"));
        if (debug.booleanValue())
          System.out.println("ZmView : HOST=" + zm_auth);
        if (debug.booleanValue())
          System.out.println("ZmView : ZmID=" + String.valueOf(ZmID) + " ZmServer=" + String.valueOf(paramAnonymousInt1));
        create_monitor_action();
        mQuickAction.show(paramAnonymousView);
        return true;
      }
    });
    this.MainServerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        ZmID = paramAnonymousInt;
        MonitorID = ((ZmCamera)myZmCamera.get(paramAnonymousInt)).getID();
        MonitorName = ((ZmCamera)myZmCamera.get(paramAnonymousInt)).getName();
        if (debug.booleanValue())
          System.out.println("ZmView : ZmID=" + String.valueOf(ZmID));
        create_monitor_action();
        mQuickAction.show(paramAnonymousView);
      }
    });
  }

  public String getAuth(String paramString)
  {
    boolean bool1 = open_web_connection(this.zm_host + "/index.php", "skin=mobile&view=watch&mid=" + paramString).booleanValue();
    String str1 = null;
    String str2 = null;
    try
    {
      if (this.debug.booleanValue())
        System.out.println("ZmView getting auth");
      HttpsURLConnection localHttpsURLConnection = this.ucs;
      InputStreamReader localInputStreamReader;
      BufferedReader localBufferedReader;
      if (localHttpsURLConnection == null)
      {
         localInputStreamReader = new InputStreamReader(this.uc.getInputStream());
        //if (str2 != null)
        //  break label236;
      }
      //while (true)
      else
      {
    	 localInputStreamReader = new InputStreamReader(this.ucs.getInputStream());
      }
      //else 
      //{

        
        //localInputStreamReader = new InputStreamReader(this.ucs.getInputStream());
        //break;
        
        //str2 = localBufferedReader.readLine();
      	localBufferedReader = new BufferedReader(localInputStreamReader);
      	str2 = localBufferedReader.readLine();
        if (debug.booleanValue())
          System.out.println("ZmView " + str2);
        if ((!str2.contains("img id")) && (!str2.contains("src")))
          return str1;

        if (debug.booleanValue())
          System.out.println("ZmView we are in getAuth");
        
        //boolean bool5 = str2.contains("auth=");
        //str1 = null;
        if (str2.contains("auth="))
        { 
	        if (debug.booleanValue())
	          System.out.println("ZmView Found img src=" + str2);
	        String[] arrayOfString1 = str2.split("\\?")[0].split("src=\"");
	
	        if (this.debug.booleanValue())
	          System.out.println("ZmView Found nph_path=" + arrayOfString1[1]);
	        this.prefs.edit().putString("zm_bin_path_" + String.valueOf(this.loading_server), arrayOfString1[1]).commit();
	
	        if (this.debug.booleanValue())
	          System.out.println("ZmView zm_bin_path_" + String.valueOf(this.loading_server) + "=" + arrayOfString1[1]);
	        String str3 = Uri.decode(str2);
	
	        if (this.debug.booleanValue())
	          System.out.println("ZmView " + str3);
	        
	        Integer localInteger1 = Integer.valueOf(str3.indexOf("auth="));
	        Integer localInteger2 = Integer.valueOf(str3.indexOf("&amp;rand"));
	        str1 = str2.substring(localInteger1.intValue(), localInteger2.intValue());
	        if (this.debug.booleanValue())
	          System.out.println("ZmView We are doing auth " + str1);
        }
        localBufferedReader.close();
        if (this.debug.booleanValue())
          System.out.println("ZmView We are doing auth " + str1);
        this.prefs.edit().putString("zm_auth_" + String.valueOf(this.loading_server), str1).commit();
        return str1;
      //}
    }
    catch (IOException localIOException)
    {
      //while (true)
      {
        //String str2;
        localIOException.printStackTrace();
        /*
        continue;
        label680: String[] arrayOfString2 = str2.split("\\?")[0].split("src=\"");

        if (debug.booleanValue())
          System.out.println("ZmView Found nph_path=" + arrayOfString2[1]);
        this.prefs.edit().putString("zm_bin_path_" + String.valueOf(this.loading_server), arrayOfString2[1]).commit();
        str1 = null;
        */
        return null;
      }
    }
  }

  // ERROR //
  public Boolean load_events(String paramString, int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: new 339	java/util/ArrayList
    //   4: dup
    //   5: invokespecial 683	java/util/ArrayList:<init>	()V
    //   8: putfield 231	com/html5clouds/zmview/ZmViewActivity:listEvents	Ljava/util/ArrayList;
    //   11: new 685	java/util/HashMap
    //   14: dup
    //   15: invokespecial 686	java/util/HashMap:<init>	()V
    //   18: astore_3
    //   19: iload_2
    //   20: iconst_2
    //   21: if_icmpne +10 -> 31
    //   24: aload_0
    //   25: ldc_w 394
    //   28: putfield 234	com/html5clouds/zmview/ZmViewActivity:sFilterEvents	Ljava/lang/String;
    //   31: iload_2
    //   32: iconst_3
    //   33: if_icmpne +10 -> 43
    //   36: aload_0
    //   37: ldc_w 688
    //   40: putfield 234	com/html5clouds/zmview/ZmViewActivity:sFilterEvents	Ljava/lang/String;
    //   43: iload_2
    //   44: iconst_4
    //   45: if_icmpne +10 -> 55
    //   48: aload_0
    //   49: ldc_w 690
    //   52: putfield 234	com/html5clouds/zmview/ZmViewActivity:sFilterEvents	Ljava/lang/String;
    //   55: iload_2
    //   56: iconst_5
    //   57: if_icmpne +10 -> 67
    //   60: aload_0
    //   61: ldc_w 692
    //   64: putfield 234	com/html5clouds/zmview/ZmViewActivity:sFilterEvents	Ljava/lang/String;
    //   67: iload_2
    //   68: bipush 6
    //   70: if_icmpne +10 -> 80
    //   73: aload_0
    //   74: ldc_w 694
    //   77: putfield 234	com/html5clouds/zmview/ZmViewActivity:sFilterEvents	Ljava/lang/String;
    //   80: new 333	java/lang/StringBuilder
    //   83: dup
    //   84: ldc_w 696
    //   87: invokespecial 337	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   90: iconst_1
    //   91: aload_0
    //   92: getfield 113	com/html5clouds/zmview/ZmViewActivity:selected_page	Ljava/lang/Integer;
    //   95: invokevirtual 331	java/lang/Integer:intValue	()I
    //   98: iadd
    //   99: invokestatic 622	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   102: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   105: ldc_w 698
    //   108: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   111: ldc_w 700
    //   114: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   117: aload_0
    //   118: getfield 234	com/html5clouds/zmview/ZmViewActivity:sFilterEvents	Ljava/lang/String;
    //   121: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   124: ldc_w 702
    //   127: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   130: ldc_w 704
    //   133: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   136: ldc_w 706
    //   139: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   142: aload_1
    //   143: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: invokevirtual 357	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   149: astore 4
    //   151: aload_0
    //   152: new 333	java/lang/StringBuilder
    //   155: dup
    //   156: aload_0
    //   157: getfield 177	com/html5clouds/zmview/ZmViewActivity:zm_host	Ljava/lang/String;
    //   160: invokestatic 575	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
    //   163: invokespecial 337	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   166: ldc_w 577
    //   169: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   172: invokevirtual 357	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   175: aload 4
    //   177: invokevirtual 583	com/html5clouds/zmview/ZmViewActivity:open_web_connection	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Boolean;
    //   180: invokevirtual 408	java/lang/Boolean:booleanValue	()Z
    //   183: ifeq +1195 -> 1378
    //   186: aload_0
    //   187: getfield 218	com/html5clouds/zmview/ZmViewActivity:ucs	Ljavax/net/ssl/HttpsURLConnection;
    //   190: ifnonnull +478 -> 668
    //   193: aload_0
    //   194: getfield 222	com/html5clouds/zmview/ZmViewActivity:uc	Ljava/net/HttpURLConnection;
    //   197: invokevirtual 593	java/net/HttpURLConnection:getInputStream	()Ljava/io/InputStream;
    //   200: astore 6
    //   202: aload_0
    //   203: aload_0
    //   204: getfield 222	com/html5clouds/zmview/ZmViewActivity:uc	Ljava/net/HttpURLConnection;
    //   207: invokevirtual 709	java/net/HttpURLConnection:getContentLength	()I
    //   210: putfield 711	com/html5clouds/zmview/ZmViewActivity:totalSize	I
    //   213: aload_0
    //   214: getfield 122	com/html5clouds/zmview/ZmViewActivity:debug	Ljava/lang/Boolean;
    //   217: invokevirtual 408	java/lang/Boolean:booleanValue	()Z
    //   220: ifeq +12 -> 232
    //   223: getstatic 491	java/lang/System:out	Ljava/io/PrintStream;
    //   226: ldc_w 713
    //   229: invokevirtual 498	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   232: new 587	java/io/InputStreamReader
    //   235: dup
    //   236: aload 6
    //   238: invokespecial 596	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   241: astore 7
    //   243: new 598	java/io/BufferedReader
    //   246: dup
    //   247: aload 7
    //   249: invokespecial 601	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   252: astore 8
    //   254: new 333	java/lang/StringBuilder
    //   257: dup
    //   258: aload 6
    //   260: invokevirtual 718	java/io/InputStream:available	()I
    //   263: invokespecial 720	java/lang/StringBuilder:<init>	(I)V
    //   266: astore 9
    //   268: iconst_0
    //   269: invokestatic 120	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   272: astore 10
    //   274: aconst_null
    //   275: astore 11
    //   277: aload 8
    //   279: invokevirtual 604	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   282: astore 12
    //   284: aload 12
    //   286: ifnonnull +417 -> 703
    //   289: aload 9
    //   291: invokevirtual 357	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   294: ldc_w 722
    //   297: invokestatic 728	org/jsoup/Jsoup:parse	(Ljava/lang/String;Ljava/lang/String;)Lorg/jsoup/nodes/Document;
    //   300: ldc_w 730
    //   303: invokevirtual 736	org/jsoup/nodes/Document:select	(Ljava/lang/String;)Lorg/jsoup/select/Elements;
    //   306: astore 13
    //   308: iconst_0
    //   309: istore 14
    //   311: aload 13
    //   313: invokevirtual 742	org/jsoup/select/Elements:iterator	()Ljava/util/Iterator;
    //   316: astore 15
    //   318: aload_3
    //   319: astore 16
    //   321: aload 15
    //   323: invokeinterface 747 1 0
    //   328: ifne +518 -> 846
    //   331: iload_2
    //   332: iconst_2
    //   333: if_icmpne +59 -> 392
    //   336: aload_0
    //   337: getfield 138	com/html5clouds/zmview/ZmViewActivity:myZmCamera	Ljava/util/ArrayList;
    //   340: aload_0
    //   341: getfield 749	com/html5clouds/zmview/ZmViewActivity:ZmID	I
    //   344: invokevirtual 343	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   347: checkcast 751	com/html5clouds/zmview/ZmCamera
    //   350: aload 11
    //   352: invokevirtual 754	com/html5clouds/zmview/ZmCamera:setEvents	(Ljava/lang/String;)V
    //   355: aload_0
    //   356: aload_0
    //   357: getfield 138	com/html5clouds/zmview/ZmViewActivity:myZmCamera	Ljava/util/ArrayList;
    //   360: aload_0
    //   361: getfield 749	com/html5clouds/zmview/ZmViewActivity:ZmID	I
    //   364: invokevirtual 343	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   367: checkcast 751	com/html5clouds/zmview/ZmCamera
    //   370: invokevirtual 757	com/html5clouds/zmview/ZmCamera:getEvents	()Ljava/lang/String;
    //   373: invokestatic 762	java/lang/Double:valueOf	(Ljava/lang/String;)Ljava/lang/Double;
    //   376: invokevirtual 766	java/lang/Double:doubleValue	()D
    //   379: ldc2_w 767
    //   382: ddiv
    //   383: invokestatic 770	com/html5clouds/zmview/ZmViewActivity:roundUp	(D)I
    //   386: invokestatic 111	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   389: putfield 115	com/html5clouds/zmview/ZmViewActivity:nr_pages	Ljava/lang/Integer;
    //   392: iload_2
    //   393: iconst_3
    //   394: if_icmpne +59 -> 453
    //   397: aload_0
    //   398: getfield 138	com/html5clouds/zmview/ZmViewActivity:myZmCamera	Ljava/util/ArrayList;
    //   401: aload_0
    //   402: getfield 749	com/html5clouds/zmview/ZmViewActivity:ZmID	I
    //   405: invokevirtual 343	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   408: checkcast 751	com/html5clouds/zmview/ZmCamera
    //   411: aload 11
    //   413: invokevirtual 773	com/html5clouds/zmview/ZmCamera:setEventsH	(Ljava/lang/String;)V
    //   416: aload_0
    //   417: aload_0
    //   418: getfield 138	com/html5clouds/zmview/ZmViewActivity:myZmCamera	Ljava/util/ArrayList;
    //   421: aload_0
    //   422: getfield 749	com/html5clouds/zmview/ZmViewActivity:ZmID	I
    //   425: invokevirtual 343	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   428: checkcast 751	com/html5clouds/zmview/ZmCamera
    //   431: invokevirtual 776	com/html5clouds/zmview/ZmCamera:getEventsH	()Ljava/lang/String;
    //   434: invokestatic 762	java/lang/Double:valueOf	(Ljava/lang/String;)Ljava/lang/Double;
    //   437: invokevirtual 766	java/lang/Double:doubleValue	()D
    //   440: ldc2_w 767
    //   443: ddiv
    //   444: invokestatic 770	com/html5clouds/zmview/ZmViewActivity:roundUp	(D)I
    //   447: invokestatic 111	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   450: putfield 115	com/html5clouds/zmview/ZmViewActivity:nr_pages	Ljava/lang/Integer;
    //   453: iload_2
    //   454: iconst_4
    //   455: if_icmpne +59 -> 514
    //   458: aload_0
    //   459: getfield 138	com/html5clouds/zmview/ZmViewActivity:myZmCamera	Ljava/util/ArrayList;
    //   462: aload_0
    //   463: getfield 749	com/html5clouds/zmview/ZmViewActivity:ZmID	I
    //   466: invokevirtual 343	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   469: checkcast 751	com/html5clouds/zmview/ZmCamera
    //   472: aload 11
    //   474: invokevirtual 779	com/html5clouds/zmview/ZmCamera:setEventsD	(Ljava/lang/String;)V
    //   477: aload_0
    //   478: aload_0
    //   479: getfield 138	com/html5clouds/zmview/ZmViewActivity:myZmCamera	Ljava/util/ArrayList;
    //   482: aload_0
    //   483: getfield 749	com/html5clouds/zmview/ZmViewActivity:ZmID	I
    //   486: invokevirtual 343	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   489: checkcast 751	com/html5clouds/zmview/ZmCamera
    //   492: invokevirtual 782	com/html5clouds/zmview/ZmCamera:getEventsD	()Ljava/lang/String;
    //   495: invokestatic 762	java/lang/Double:valueOf	(Ljava/lang/String;)Ljava/lang/Double;
    //   498: invokevirtual 766	java/lang/Double:doubleValue	()D
    //   501: ldc2_w 767
    //   504: ddiv
    //   505: invokestatic 770	com/html5clouds/zmview/ZmViewActivity:roundUp	(D)I
    //   508: invokestatic 111	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   511: putfield 115	com/html5clouds/zmview/ZmViewActivity:nr_pages	Ljava/lang/Integer;
    //   514: iload_2
    //   515: iconst_5
    //   516: if_icmpne +59 -> 575
    //   519: aload_0
    //   520: getfield 138	com/html5clouds/zmview/ZmViewActivity:myZmCamera	Ljava/util/ArrayList;
    //   523: aload_0
    //   524: getfield 749	com/html5clouds/zmview/ZmViewActivity:ZmID	I
    //   527: invokevirtual 343	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   530: checkcast 751	com/html5clouds/zmview/ZmCamera
    //   533: aload 11
    //   535: invokevirtual 785	com/html5clouds/zmview/ZmCamera:setEventsW	(Ljava/lang/String;)V
    //   538: aload_0
    //   539: aload_0
    //   540: getfield 138	com/html5clouds/zmview/ZmViewActivity:myZmCamera	Ljava/util/ArrayList;
    //   543: aload_0
    //   544: getfield 749	com/html5clouds/zmview/ZmViewActivity:ZmID	I
    //   547: invokevirtual 343	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   550: checkcast 751	com/html5clouds/zmview/ZmCamera
    //   553: invokevirtual 788	com/html5clouds/zmview/ZmCamera:getEventsW	()Ljava/lang/String;
    //   556: invokestatic 762	java/lang/Double:valueOf	(Ljava/lang/String;)Ljava/lang/Double;
    //   559: invokevirtual 766	java/lang/Double:doubleValue	()D
    //   562: ldc2_w 767
    //   565: ddiv
    //   566: invokestatic 770	com/html5clouds/zmview/ZmViewActivity:roundUp	(D)I
    //   569: invokestatic 111	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   572: putfield 115	com/html5clouds/zmview/ZmViewActivity:nr_pages	Ljava/lang/Integer;
    //   575: iload_2
    //   576: bipush 6
    //   578: if_icmpne +59 -> 637
    //   581: aload_0
    //   582: getfield 138	com/html5clouds/zmview/ZmViewActivity:myZmCamera	Ljava/util/ArrayList;
    //   585: aload_0
    //   586: getfield 749	com/html5clouds/zmview/ZmViewActivity:ZmID	I
    //   589: invokevirtual 343	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   592: checkcast 751	com/html5clouds/zmview/ZmCamera
    //   595: aload 11
    //   597: invokevirtual 791	com/html5clouds/zmview/ZmCamera:setEventsM	(Ljava/lang/String;)V
    //   600: aload_0
    //   601: aload_0
    //   602: getfield 138	com/html5clouds/zmview/ZmViewActivity:myZmCamera	Ljava/util/ArrayList;
    //   605: aload_0
    //   606: getfield 749	com/html5clouds/zmview/ZmViewActivity:ZmID	I
    //   609: invokevirtual 343	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   612: checkcast 751	com/html5clouds/zmview/ZmCamera
    //   615: invokevirtual 794	com/html5clouds/zmview/ZmCamera:getEventsM	()Ljava/lang/String;
    //   618: invokestatic 762	java/lang/Double:valueOf	(Ljava/lang/String;)Ljava/lang/Double;
    //   621: invokevirtual 766	java/lang/Double:doubleValue	()D
    //   624: ldc2_w 767
    //   627: ddiv
    //   628: invokestatic 770	com/html5clouds/zmview/ZmViewActivity:roundUp	(D)I
    //   631: invokestatic 111	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   634: putfield 115	com/html5clouds/zmview/ZmViewActivity:nr_pages	Ljava/lang/Integer;
    //   637: aload_0
    //   638: getfield 122	com/html5clouds/zmview/ZmViewActivity:debug	Ljava/lang/Boolean;
    //   641: invokevirtual 408	java/lang/Boolean:booleanValue	()Z
    //   644: ifeq +12 -> 656
    //   647: getstatic 491	java/lang/System:out	Ljava/io/PrintStream;
    //   650: ldc_w 796
    //   653: invokevirtual 498	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   656: iconst_1
    //   657: invokestatic 120	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   660: astore 18
    //   662: aload 16
    //   664: pop
    //   665: aload 18
    //   667: areturn
    //   668: aload_0
    //   669: getfield 218	com/html5clouds/zmview/ZmViewActivity:ucs	Ljavax/net/ssl/HttpsURLConnection;
    //   672: invokevirtual 634	javax/net/ssl/HttpsURLConnection:getInputStream	()Ljava/io/InputStream;
    //   675: astore 6
    //   677: aload_0
    //   678: aload_0
    //   679: getfield 218	com/html5clouds/zmview/ZmViewActivity:ucs	Ljavax/net/ssl/HttpsURLConnection;
    //   682: invokevirtual 797	javax/net/ssl/HttpsURLConnection:getContentLength	()I
    //   685: putfield 711	com/html5clouds/zmview/ZmViewActivity:totalSize	I
    //   688: goto -475 -> 213
    //   691: astore 5
    //   693: aload 5
    //   695: invokevirtual 680	java/io/IOException:printStackTrace	()V
    //   698: iconst_0
    //   699: invokestatic 120	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   702: areturn
    //   703: aload 12
    //   705: ldc_w 799
    //   708: invokevirtual 642	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
    //   711: ifeq +11 -> 722
    //   714: aload 10
    //   716: invokevirtual 408	java/lang/Boolean:booleanValue	()Z
    //   719: ifeq +22 -> 741
    //   722: aload 12
    //   724: ldc_w 801
    //   727: invokevirtual 642	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
    //   730: ifeq +105 -> 835
    //   733: aload 10
    //   735: invokevirtual 408	java/lang/Boolean:booleanValue	()Z
    //   738: ifne +97 -> 835
    //   741: aload 12
    //   743: ldc_w 803
    //   746: invokevirtual 642	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
    //   749: ifne +86 -> 835
    //   752: aload 12
    //   754: ldc_w 801
    //   757: ldc_w 394
    //   760: invokevirtual 807	java/lang/String:replace	(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   763: ldc_w 809
    //   766: ldc_w 394
    //   769: invokevirtual 813	java/lang/String:replaceAll	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   772: astore 34
    //   774: iconst_1
    //   775: invokestatic 120	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   778: astore 10
    //   780: aload 34
    //   782: astore 11
    //   784: aload_0
    //   785: getfield 122	com/html5clouds/zmview/ZmViewActivity:debug	Ljava/lang/Boolean;
    //   788: invokevirtual 408	java/lang/Boolean:booleanValue	()Z
    //   791: ifeq +44 -> 835
    //   794: getstatic 491	java/lang/System:out	Ljava/io/PrintStream;
    //   797: new 333	java/lang/StringBuilder
    //   800: dup
    //   801: ldc_w 815
    //   804: invokespecial 337	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   807: aload 34
    //   809: invokevirtual 816	java/lang/String:toString	()Ljava/lang/String;
    //   812: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   815: ldc_w 818
    //   818: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   821: aload 12
    //   823: invokevirtual 816	java/lang/String:toString	()Ljava/lang/String;
    //   826: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   829: invokevirtual 357	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   832: invokevirtual 498	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   835: aload 9
    //   837: aload 12
    //   839: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   842: pop
    //   843: goto -566 -> 277
    //   846: aload 15
    //   848: invokeinterface 822 1 0
    //   853: checkcast 824	org/jsoup/nodes/Element
    //   856: astore 20
    //   858: iload 14
    //   860: ifne +531 -> 1391
    //   863: new 685	java/util/HashMap
    //   866: dup
    //   867: invokespecial 686	java/util/HashMap:<init>	()V
    //   870: astore 21
    //   872: aload 20
    //   874: ldc_w 826
    //   877: invokevirtual 829	org/jsoup/nodes/Element:getElementsByClass	(Ljava/lang/String;)Lorg/jsoup/select/Elements;
    //   880: astore 22
    //   882: aload 20
    //   884: ldc_w 831
    //   887: invokevirtual 829	org/jsoup/nodes/Element:getElementsByClass	(Ljava/lang/String;)Lorg/jsoup/select/Elements;
    //   890: astore 23
    //   892: aload 20
    //   894: ldc_w 833
    //   897: invokevirtual 829	org/jsoup/nodes/Element:getElementsByClass	(Ljava/lang/String;)Lorg/jsoup/select/Elements;
    //   900: astore 24
    //   902: aload 20
    //   904: ldc_w 835
    //   907: invokevirtual 829	org/jsoup/nodes/Element:getElementsByClass	(Ljava/lang/String;)Lorg/jsoup/select/Elements;
    //   910: astore 25
    //   912: aload 20
    //   914: ldc_w 837
    //   917: invokevirtual 829	org/jsoup/nodes/Element:getElementsByClass	(Ljava/lang/String;)Lorg/jsoup/select/Elements;
    //   920: astore 26
    //   922: aload 22
    //   924: invokevirtual 840	org/jsoup/select/Elements:hasText	()Z
    //   927: ifeq +79 -> 1006
    //   930: iconst_1
    //   931: istore 14
    //   933: aload 21
    //   935: ldc_w 842
    //   938: aload 22
    //   940: iconst_0
    //   941: invokevirtual 845	org/jsoup/select/Elements:get	(I)Lorg/jsoup/nodes/Element;
    //   944: invokevirtual 848	org/jsoup/nodes/Element:text	()Ljava/lang/String;
    //   947: invokevirtual 852	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   950: pop
    //   951: aload_0
    //   952: getfield 122	com/html5clouds/zmview/ZmViewActivity:debug	Ljava/lang/Boolean;
    //   955: invokevirtual 408	java/lang/Boolean:booleanValue	()Z
    //   958: ifeq +48 -> 1006
    //   961: getstatic 491	java/lang/System:out	Ljava/io/PrintStream;
    //   964: new 333	java/lang/StringBuilder
    //   967: dup
    //   968: ldc_w 854
    //   971: invokespecial 337	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   974: iload 14
    //   976: invokestatic 622	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   979: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   982: ldc_w 856
    //   985: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   988: aload 22
    //   990: iconst_0
    //   991: invokevirtual 845	org/jsoup/select/Elements:get	(I)Lorg/jsoup/nodes/Element;
    //   994: invokevirtual 848	org/jsoup/nodes/Element:text	()Ljava/lang/String;
    //   997: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1000: invokevirtual 357	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1003: invokevirtual 498	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   1006: aload 23
    //   1008: invokevirtual 840	org/jsoup/select/Elements:hasText	()Z
    //   1011: ifeq +76 -> 1087
    //   1014: aload 21
    //   1016: ldc_w 858
    //   1019: aload 23
    //   1021: iconst_0
    //   1022: invokevirtual 845	org/jsoup/select/Elements:get	(I)Lorg/jsoup/nodes/Element;
    //   1025: invokevirtual 848	org/jsoup/nodes/Element:text	()Ljava/lang/String;
    //   1028: invokevirtual 852	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1031: pop
    //   1032: aload_0
    //   1033: getfield 122	com/html5clouds/zmview/ZmViewActivity:debug	Ljava/lang/Boolean;
    //   1036: invokevirtual 408	java/lang/Boolean:booleanValue	()Z
    //   1039: ifeq +48 -> 1087
    //   1042: getstatic 491	java/lang/System:out	Ljava/io/PrintStream;
    //   1045: new 333	java/lang/StringBuilder
    //   1048: dup
    //   1049: ldc_w 860
    //   1052: invokespecial 337	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   1055: iload 14
    //   1057: invokestatic 622	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   1060: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1063: ldc_w 662
    //   1066: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1069: aload 23
    //   1071: iconst_0
    //   1072: invokevirtual 845	org/jsoup/select/Elements:get	(I)Lorg/jsoup/nodes/Element;
    //   1075: invokevirtual 848	org/jsoup/nodes/Element:text	()Ljava/lang/String;
    //   1078: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1081: invokevirtual 357	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1084: invokevirtual 498	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   1087: aload 24
    //   1089: invokevirtual 840	org/jsoup/select/Elements:hasText	()Z
    //   1092: ifeq +76 -> 1168
    //   1095: aload 21
    //   1097: ldc_w 862
    //   1100: aload 24
    //   1102: iconst_0
    //   1103: invokevirtual 845	org/jsoup/select/Elements:get	(I)Lorg/jsoup/nodes/Element;
    //   1106: invokevirtual 848	org/jsoup/nodes/Element:text	()Ljava/lang/String;
    //   1109: invokevirtual 852	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1112: pop
    //   1113: aload_0
    //   1114: getfield 122	com/html5clouds/zmview/ZmViewActivity:debug	Ljava/lang/Boolean;
    //   1117: invokevirtual 408	java/lang/Boolean:booleanValue	()Z
    //   1120: ifeq +48 -> 1168
    //   1123: getstatic 491	java/lang/System:out	Ljava/io/PrintStream;
    //   1126: new 333	java/lang/StringBuilder
    //   1129: dup
    //   1130: ldc_w 860
    //   1133: invokespecial 337	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   1136: iload 14
    //   1138: invokestatic 622	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   1141: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1144: ldc_w 662
    //   1147: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1150: aload 24
    //   1152: iconst_0
    //   1153: invokevirtual 845	org/jsoup/select/Elements:get	(I)Lorg/jsoup/nodes/Element;
    //   1156: invokevirtual 848	org/jsoup/nodes/Element:text	()Ljava/lang/String;
    //   1159: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1162: invokevirtual 357	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1165: invokevirtual 498	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   1168: aload 25
    //   1170: invokevirtual 840	org/jsoup/select/Elements:hasText	()Z
    //   1173: ifeq +76 -> 1249
    //   1176: aload 21
    //   1178: ldc_w 864
    //   1181: aload 25
    //   1183: iconst_0
    //   1184: invokevirtual 845	org/jsoup/select/Elements:get	(I)Lorg/jsoup/nodes/Element;
    //   1187: invokevirtual 848	org/jsoup/nodes/Element:text	()Ljava/lang/String;
    //   1190: invokevirtual 852	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1193: pop
    //   1194: aload_0
    //   1195: getfield 122	com/html5clouds/zmview/ZmViewActivity:debug	Ljava/lang/Boolean;
    //   1198: invokevirtual 408	java/lang/Boolean:booleanValue	()Z
    //   1201: ifeq +48 -> 1249
    //   1204: getstatic 491	java/lang/System:out	Ljava/io/PrintStream;
    //   1207: new 333	java/lang/StringBuilder
    //   1210: dup
    //   1211: ldc_w 860
    //   1214: invokespecial 337	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   1217: iload 14
    //   1219: invokestatic 622	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   1222: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1225: ldc_w 662
    //   1228: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1231: aload 25
    //   1233: iconst_0
    //   1234: invokevirtual 845	org/jsoup/select/Elements:get	(I)Lorg/jsoup/nodes/Element;
    //   1237: invokevirtual 848	org/jsoup/nodes/Element:text	()Ljava/lang/String;
    //   1240: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1243: invokevirtual 357	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1246: invokevirtual 498	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   1249: aload 26
    //   1251: invokevirtual 840	org/jsoup/select/Elements:hasText	()Z
    //   1254: ifeq +79 -> 1333
    //   1257: aload 21
    //   1259: ldc_w 866
    //   1262: aload 26
    //   1264: iconst_0
    //   1265: invokevirtual 845	org/jsoup/select/Elements:get	(I)Lorg/jsoup/nodes/Element;
    //   1268: invokevirtual 848	org/jsoup/nodes/Element:text	()Ljava/lang/String;
    //   1271: invokevirtual 852	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1274: pop
    //   1275: aload_0
    //   1276: getfield 122	com/html5clouds/zmview/ZmViewActivity:debug	Ljava/lang/Boolean;
    //   1279: invokevirtual 408	java/lang/Boolean:booleanValue	()Z
    //   1282: ifeq +116 -> 1398
    //   1285: getstatic 491	java/lang/System:out	Ljava/io/PrintStream;
    //   1288: new 333	java/lang/StringBuilder
    //   1291: dup
    //   1292: ldc_w 860
    //   1295: invokespecial 337	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   1298: iload 14
    //   1300: invokestatic 622	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   1303: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1306: ldc_w 662
    //   1309: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1312: aload 26
    //   1314: iconst_0
    //   1315: invokevirtual 845	org/jsoup/select/Elements:get	(I)Lorg/jsoup/nodes/Element;
    //   1318: invokevirtual 848	org/jsoup/nodes/Element:text	()Ljava/lang/String;
    //   1321: invokevirtual 352	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1324: invokevirtual 357	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1327: invokevirtual 498	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   1330: goto +68 -> 1398
    //   1333: iload 14
    //   1335: iconst_2
    //   1336: if_icmpne +16 -> 1352
    //   1339: aload_0
    //   1340: getfield 231	com/html5clouds/zmview/ZmViewActivity:listEvents	Ljava/util/ArrayList;
    //   1343: aload 21
    //   1345: invokevirtual 870	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   1348: pop
    //   1349: iconst_0
    //   1350: istore 14
    //   1352: aload_0
    //   1353: getfield 122	com/html5clouds/zmview/ZmViewActivity:debug	Ljava/lang/Boolean;
    //   1356: invokevirtual 408	java/lang/Boolean:booleanValue	()Z
    //   1359: ifeq +12 -> 1371
    //   1362: getstatic 491	java/lang/System:out	Ljava/io/PrintStream;
    //   1365: ldc_w 872
    //   1368: invokevirtual 498	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   1371: aload 21
    //   1373: astore 16
    //   1375: goto -1054 -> 321
    //   1378: iconst_0
    //   1379: invokestatic 120	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   1382: areturn
    //   1383: astore 5
    //   1385: aload 16
    //   1387: pop
    //   1388: goto -695 -> 693
    //   1391: aload 16
    //   1393: astore 21
    //   1395: goto -523 -> 872
    //   1398: iconst_2
    //   1399: istore 14
    //   1401: goto -68 -> 1333
    //
    // Exception table:
    //   from	to	target	type
    //   186	213	691	java/io/IOException
    //   213	232	691	java/io/IOException
    //   232	274	691	java/io/IOException
    //   277	284	691	java/io/IOException
    //   289	308	691	java/io/IOException
    //   311	318	691	java/io/IOException
    //   668	688	691	java/io/IOException
    //   703	722	691	java/io/IOException
    //   722	741	691	java/io/IOException
    //   741	780	691	java/io/IOException
    //   784	835	691	java/io/IOException
    //   835	843	691	java/io/IOException
    //   872	930	691	java/io/IOException
    //   933	1006	691	java/io/IOException
    //   1006	1087	691	java/io/IOException
    //   1087	1168	691	java/io/IOException
    //   1168	1249	691	java/io/IOException
    //   1249	1330	691	java/io/IOException
    //   1339	1349	691	java/io/IOException
    //   1352	1371	691	java/io/IOException
    //   321	331	1383	java/io/IOException
    //   336	392	1383	java/io/IOException
    //   397	453	1383	java/io/IOException
    //   458	514	1383	java/io/IOException
    //   519	575	1383	java/io/IOException
    //   581	637	1383	java/io/IOException
    //   637	656	1383	java/io/IOException
    //   656	662	1383	java/io/IOException
    //   846	858	1383	java/io/IOException
    //   863	872	1383	java/io/IOException
	return true;
  }

  public Boolean load_monitors()
  {
    if (this.debug.booleanValue())
      System.out.println("We start load_monitor open_web_conn");
    if (open_web_connection(this.zm_host + "/index.php", "skin=mobile").booleanValue())
    //while (true)
    {
      InputStream localInputStream1;
      InputStream localInputStream2;
      int j;
      int k;
      Iterator localIterator1;
      Iterator localIterator3;
      Iterator localIterator2;
      try
      {
        this.myZmCamera = new ArrayList();
        if (this.debug.booleanValue())
          System.out.println("We start load_monitor uc.getInputStream");
        byte[] arrayOfByte;
        StringBuilder localStringBuilder;
        int iSize;
        if(this.ucs == null)
        {
          localInputStream1 = this.uc.getInputStream();
          this.totalSize = this.uc.getContentLength();
          //if (!this.debug.booleanValue())
          //  break label1123;
          System.out.println("Reading from uc");
          localInputStream2 = localInputStream1;
          DoneHandlerInputStream localDoneHandlerInputStream = new DoneHandlerInputStream(localInputStream2);
          if (this.debug.booleanValue())
            System.out.println("We finish load_monitor uc.getInputStream");
          arrayOfByte = new byte[1024];
          this.downloadedSize = 0;
          
          localStringBuilder = new StringBuilder(localDoneHandlerInputStream.available());
          
          if (this.debug.booleanValue())
            System.out.println("We start load_monitor get stream");
          while(true)
	      {
        	  iSize = localDoneHandlerInputStream.read(arrayOfByte);
	          
	          if (iSize <= 0)
	          {
	            if (this.debug.booleanValue())
	              System.out.println("We start load_monitor finish stream");
	            this.downloadedSize = 0;
	            
	            Document localDocument = Jsoup.parse(localStringBuilder.toString(), "UTF-8");
	            if (this.debug.booleanValue())
	              System.out.println("We start load_monitor selecting_events");
	            
	            Elements localElements1 = localDocument.select("td");
	            j = 0;
	            k = 0;
	            Elements localElements2 = localDocument.select("a[href]");
	            localIterator1 = localElements2.iterator();
	            /*if*/ while(localIterator1.hasNext())
	            {
	            	//break label588;
	            	String str1 = ((Element)localIterator1.next()).attr("href");
	                if ((str1.trim().contains("mid=")) && (str1.trim().contains("view=function")))
	                {
	                  String str2 = str1.substring(4 + str1.indexOf("mid="));
	                  ZmCamera localZmCamera1 = new ZmCamera();
	                  localZmCamera1.setID(str2);
	                  this.myZmCamera.add(localZmCamera1);
	                }
	            }
	                   
	            if (this.myZmCamera.size() == 0)
	            {
	              localIterator3 = localElements2.iterator();
	              /*if*/ while (localIterator3.hasNext())
	              {  //break label680;
	            	  String str3 = ((Element)localIterator3.next()).attr("href");
	                  if ((str3.trim().contains("mid=")) && (str3.trim().contains("view=watch")))
	                  {
	                    String str4 = str3.substring(4 + str3.indexOf("mid="));
	                    ZmCamera localZmCamera2 = new ZmCamera();
	                    localZmCamera2.setID(str4);
	                    this.myZmCamera.add(localZmCamera2);
	                  }
	              }
	            }
	            if (this.debug.booleanValue())
	              System.out.println("We start load_monitor selecting_monitor");
	            localIterator2 = localElements1.iterator();
	            /*if*/ while (localIterator2.hasNext())
	            {
	            	//break label772;
	            	Element localElement = (Element)localIterator2.next();
	                Elements localElements3 = localElement.getElementsByClass("colName");
	                Elements localElements4 = localElement.getElementsByClass("colFunction");
	                Elements localElements5 = localElement.getElementsByClass("colEvents");
	                if(localElements3.hasText())
	                {
	                  j++;
	                  ((ZmCamera)this.myZmCamera.get(j - 1)).setName(localElements3.get(0).text());
	                  boolean bool = this.debug.booleanValue();
	                  k = 0;
	                  if (bool)
	                    System.out.println("ZmView : Name[" + String.valueOf(j) + "]=" + localElements3.get(0).text());
	                }
	                if ((localElements4.hasText()) && (j != 0))
	                {
	                  ((ZmCamera)this.myZmCamera.get(j - 1)).setStatus(localElements4.get(0).text());
	                  if (this.debug.booleanValue())
	                    System.out.println("ZmView : Function[" + String.valueOf(j) + "]=" + localElements4.get(0).text());
	                }
	                if ((localElements5.hasText()) && (j != 0) && (k < 1))
	                {
	                  if (k == 0)
	                    ((ZmCamera)this.myZmCamera.get(j - 1)).setEventsH(localElements5.get(0).text());
	                  k++;
	                  if (this.debug.booleanValue())
	                  {
	                    System.out.println("ZmView : Event[" + String.valueOf(j) + "]=" + localElements5.get(0).text());
	                  }
	            	
	              }
	            }
	            if (this.debug.booleanValue())
	              System.out.println("We finish load_monitor");
	            localDoneHandlerInputStream.close();
	            return Boolean.valueOf(true);
	         
	          
	        }
	        else
	        {
	          //localInputStream1 = this.ucs.getInputStream();
	          //this.totalSize = this.ucs.getContentLength();
	          //if (!this.debug.booleanValue())
	          //  break label1123;
	          
	          //System.out.println("Reading from ucs");
	          //break label1123;
	        }
	        String str5 = new String(arrayOfByte, 0, iSize);
	        localStringBuilder.append(str5);
	        this.downloadedSize = (iSize + this.downloadedSize);
	        //continue;
	      }
       }
        //for compile
        return Boolean.valueOf(false);
      }
      catch (IOException localIOException)
      {
        this.show_monitors = 10;
        this.messageLog = localIOException.toString();
        if (this.debug.booleanValue())
          System.out.println("ZmView error2 " + localIOException.getMessage().toString());
        if (this.debug.booleanValue())
          System.out.println("ZmView error3 " + localIOException.toString());
        localIOException.printStackTrace();
        return Boolean.valueOf(false);
      }
     }
      /*
      label588: String str1 = ((Element)localIterator1.next()).attr("href");
      if ((str1.trim().contains("mid=")) && (str1.trim().contains("view=function")))
      {
        String str2 = str1.substring(4 + str1.indexOf("mid="));
        ZmCamera localZmCamera1 = new ZmCamera();
        localZmCamera1.setID(str2);
        this.myZmCamera.add(localZmCamera1);
        continue;
        
        label680: String str3 = ((Element)localIterator3.next()).attr("href");
        if ((str3.trim().contains("mid=")) && (str3.trim().contains("view=watch")))
        {
          String str4 = str3.substring(4 + str3.indexOf("mid="));
          ZmCamera localZmCamera2 = new ZmCamera();
          localZmCamera2.setID(str4);
          this.myZmCamera.add(localZmCamera2);
          continue;
          label772: Element localElement = (Element)localIterator2.next();
          Elements localElements3 = localElement.getElementsByClass("colName");
          Elements localElements4 = localElement.getElementsByClass("colFunction");
          Elements localElements5 = localElement.getElementsByClass("colEvents");
          if (localElements3.hasText())
          {
            j++;
            ((ZmCamera)this.myZmCamera.get(j - 1)).setName(localElements3.get(0).text());
            boolean bool = this.debug.booleanValue();
            k = 0;
            if (bool)
              System.out.println("ZmView : Name[" + String.valueOf(j) + "]=" + localElements3.get(0).text());
          }
          if ((localElements4.hasText()) && (j != 0))
          {
            ((ZmCamera)this.myZmCamera.get(j - 1)).setStatus(localElements4.get(0).text());
            if (this.debug.booleanValue())
              System.out.println("ZmView : Function[" + String.valueOf(j) + "]=" + localElements4.get(0).text());
          }
          if ((localElements5.hasText()) && (j != 0) && (k < 1))
          {
            if (k == 0)
              ((ZmCamera)this.myZmCamera.get(j - 1)).setEventsH(localElements5.get(0).text());
            k++;
            if (this.debug.booleanValue())
            {
              System.out.println("ZmView : Event[" + String.valueOf(j) + "]=" + localElements5.get(0).text());
              continue;
              return Boolean.valueOf(false);
              //label1123: localInputStream2 = localInputStream1;
            }
          }
        }
      }*/
      //return Boolean.valueOf(false);
    //}
	return Boolean.valueOf(false);
  }

  public void load_settings()
  {
    this.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    this.zm_host = this.prefs.getString("zm_host", null);
    this.zm_user = this.prefs.getString("zm_user", null);
    this.zm_pass = this.prefs.getString("zm_pass", null);
    this.zm_get_auth = this.prefs.getString("zm_auth", null);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    switch (paramInt1)
    {
    default:
      return;
    case 1:
      this.prefs.edit().putString("zm_name_" + String.valueOf(this.nr_servers), this.prefs.getString("zm_name", "Unknown")).commit();
      this.prefs.edit().putString("zm_host_" + String.valueOf(this.nr_servers), this.prefs.getString("zm_host", null)).commit();
      this.prefs.edit().putString("zm_user_" + String.valueOf(this.nr_servers), this.prefs.getString("zm_user", null)).commit();
      this.prefs.edit().putString("zm_pass_" + String.valueOf(this.nr_servers), this.prefs.getString("zm_pass", null)).commit();
      this.nr_servers = (1 + this.nr_servers);
      ZmServers localZmServers = new ZmServers();
      localZmServers.setName(this.prefs.getString("zm_name", "Unknown"));
      localZmServers.setHost(this.prefs.getString("zm_host", ""));
      localZmServers.setUser(this.prefs.getString("zm_user", ""));
      localZmServers.setPass(this.prefs.getString("zm_pass", ""));
      this.myZmServers.add(localZmServers);
      this.prefs.edit().putInt("nr_servers", this.nr_servers).commit();
      this._ZmServersAdapter.notifyDataSetChanged();
      return;
    case 2:
    }
    this.prefs.edit().putString("zm_name_" + String.valueOf(this.edit_server), this.prefs.getString("zm_name", "Unknown")).commit();
    this.prefs.edit().putString("zm_host_" + String.valueOf(this.edit_server), this.prefs.getString("zm_host", null)).commit();
    this.prefs.edit().putString("zm_user_" + String.valueOf(this.edit_server), this.prefs.getString("zm_user", null)).commit();
    this.prefs.edit().putString("zm_pass_" + String.valueOf(this.edit_server), this.prefs.getString("zm_pass", null)).commit();
    ((ZmServers)this.myZmServers.get(this.edit_server)).setName(this.prefs.getString("zm_name", "Unknown"));
    ((ZmServers)this.myZmServers.get(this.edit_server)).setHost(this.prefs.getString("zm_host", "http://www.yourserver.com/zm"));
    ((ZmServers)this.myZmServers.get(this.edit_server)).setUser(this.prefs.getString("zm_user", "admin"));
    ((ZmServers)this.myZmServers.get(this.edit_server)).setPass(this.prefs.getString("zm_pass", "admin"));
    this._ZmServersAdapter.notifyDataSetChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.myZmServers = new ArrayList();
    this.Eprefs = new ObscuredSharedPreferences(this, getSharedPreferences("zmview.cfg", 0));
    this.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    this.isPro = isProInstalled(getApplicationContext());
    load_settings();
    this.Eprefs.edit().putBoolean("isPro", this.isPro).commit();
    if ((this.isPro) && (this.debug.booleanValue()))
      System.out.println("ZmView we have Pro");
    if (!this.prefs.getBoolean("config", false))
    {
      startActivity(new Intent(this, Settings.class));
      finish();
      System.out.println("ZmView noi continuam fara settings");
      //return;
    }
    this.nr_servers = this.prefs.getInt("nr_servers", 0);
    if (this.nr_servers == 0)
    {
      this.prefs.edit().putString("zm_name_" + String.valueOf(this.nr_servers), this.prefs.getString("zm_name", "Unknown")).commit();
      this.prefs.edit().putString("zm_host_" + String.valueOf(this.nr_servers), this.prefs.getString("zm_host", null)).commit();
      this.prefs.edit().putString("zm_user_" + String.valueOf(this.nr_servers), this.prefs.getString("zm_user", null)).commit();
      this.prefs.edit().putString("zm_pass_" + String.valueOf(this.nr_servers), this.prefs.getString("zm_pass", null)).commit();
      this.nr_servers = (1 + this.nr_servers);
      ZmServers localZmServers2 = new ZmServers();
      localZmServers2.setName(this.prefs.getString("zm_name", "Unknown"));
      localZmServers2.setHost(this.prefs.getString("zm_host", ""));
      localZmServers2.setUser(this.prefs.getString("zm_user", ""));
      localZmServers2.setPass(this.prefs.getString("zm_pass", ""));
      this.myZmServers.add(localZmServers2);
      
    }
    else
    {      
      for (int i = 0; i < this.nr_servers; i++)
      {
        ZmServers localZmServers1 = new ZmServers();
        localZmServers1.setName(this.prefs.getString("zm_name_" + String.valueOf(i), "Unknown"));
        localZmServers1.setHost(this.prefs.getString("zm_host_" + String.valueOf(i), ""));
        localZmServers1.setUser(this.prefs.getString("zm_user_" + String.valueOf(i), ""));
        localZmServers1.setPass(this.prefs.getString("zm_pass_" + String.valueOf(i), ""));
        this.myZmServers.add(localZmServers1);
      }
    }
      this.prefs.edit().putInt("nr_servers", this.nr_servers).commit();
      startService(new Intent("zmview.FIRST"));
      if (paramBundle == null)
      {
    	  //break label1245;
    	  setContentView(R.layout.main);
	      if (!this.isPro)
	      {
	    	  /*
	        this.adView = ((AdView)findViewById(2131099704));
	        this.adView.setVisibility(0);
	        this.adView.loadAd(new AdRequest());
	        */
	      }
	      create_server_list();
	      //break label974;
      }
      else
      {
	      this.show_monitors = paramBundle.getInt("show_monitors");
	      this.show_events = paramBundle.getInt("show_events");
	      this.selected_page = Integer.valueOf(paramBundle.getInt("selected_page"));
	      if ((this.show_monitors == 4) || ((this.show_events > 0) && (this.show_events <= 4)))
	      {
	        this.myZmCamera = ((ArrayList)paramBundle.getSerializable("myZmCamera"));
	        this.myZmServers = ((ArrayList)paramBundle.getSerializable("myZmServers"));
	        this.loading_server = paramBundle.getInt("loading_server");
	      }
	      this.events_type = paramBundle.getInt("events_type");
	      this.nr_pages = Integer.valueOf(paramBundle.getInt("nr_pages"));
	      this.ZmID = paramBundle.getInt("ZmID");
	      this.MonitorID = paramBundle.getString("MonitorID");
	      this.MonitorName = paramBundle.getString("MonitorName");
	      if ((this.show_events > 0) && (this.show_events <= 4))
	      {
	        setContentView(R.layout.mainviewevents);
	        create_prev_next();
	        this.show_events = 1;
	        show_events(Integer.valueOf(this.ZmID), this.MonitorID, String.valueOf(1 + this.selected_page.intValue()), this.MonitorName);
	        ShowLoading(Boolean.valueOf(true));
	        if (!this.isPro)
	        {
	          /*
	          this.adView = ((AdView)findViewById(2131099704));
	          this.adView.setVisibility(0);
	          this.adView.loadAd(new AdRequest());
	          */
	        }
	      }
	      if ((this.show_monitors > 0) && (this.show_monitors <= 4))
	      {
	        setContentView(R.layout.main);
	        create_server_list();
	        if (!this.isPro)
	        {
	          /*
	          this.adView = ((AdView)findViewById(2131099704));
	          this.adView.setVisibility(0);
	          this.adView.loadAd(new AdRequest());
	          */
	        	
	        }
	        if (this.debug.booleanValue())
	          System.out.println("ZmView : we resume from changing the display show_monitors=" + String.valueOf(this.show_monitors));
	        if (this.show_monitors != 4)
	          this.show_monitors = 1;
	      }
	      if (this.debug.booleanValue())
	        System.out.println("ZmView : we resume from changing the display");
      }
      //label974: 
      this.android_version = Build.VERSION.SDK_INT;
      this._initTask = ((InitTask)getLastNonConfigurationInstance());
      if (this._initTask != null)
      {
    	  //break label1302;
    	  this._initTask.attach(this);
          if (this.debug.booleanValue())
            System.out.println("ZmView : Attach to task onCreate");
      }
      else
      {
    	  if (this.debug.booleanValue())
    		  System.out.println("ZmView : create new task");
	      this._initTask = new InitTask(this);
	      this._initTask.execute(new Context[] { this });
      }
    //}
    //while (true)
    
    //else
    //{
      //this.list = ((ListView)findViewById(R.id.listView1));
      //return;
    //  for (int i = 0; i < this.nr_servers; i++)
    //  {
    //    ZmServers localZmServers1 = new ZmServers();
    //    localZmServers1.setName(this.prefs.getString("zm_name_" + String.valueOf(i), "Unknown"));
    //    localZmServers1.setHost(this.prefs.getString("zm_host_" + String.valueOf(i), ""));
    //    localZmServers1.setUser(this.prefs.getString("zm_user_" + String.valueOf(i), ""));
    //    localZmServers1.setPass(this.prefs.getString("zm_pass_" + String.valueOf(i), ""));
     //   this.myZmServers.add(localZmServers1);
     // }
     //break;
      /*
      label1245: setContentView(R.layout.main);
      if (!this.isPro)
      {
    	  
        this.adView = ((AdView)findViewById(2131099704));
        this.adView.setVisibility(0);
        this.adView.loadAd(new AdRequest());
        
      }     
      create_server_list();
      break label974;
      label1302: this._initTask.attach(this);
      if (this.debug.booleanValue())
        System.out.println("ZmView : Attach to task onCreate");
      */
    //}
    this.list = ((ListView)findViewById(R.id.listView1));
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    super.onCreateOptionsMenu(paramMenu);
    getSupportMenuInflater().inflate(R.menu.menu, paramMenu);
    return true;
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramKeyEvent.getKeyCode() == 4)
    {
      Log.d("KEYCODE_BACK", "KEYCODE_BACK");
      if (this.show_events == 4)
      {
        this.show_events = 0;
        setContentView(R.layout.main);
        create_server_list();
        this.show_monitors = 4;
        return true;
      }
      moveTaskToBack(true);
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    /*int i = 1;*/
	boolean i = true;
    int j = 1;
    switch (paramMenuItem.getItemId())
    {
    default:
      return super.onOptionsItemSelected(paramMenuItem);
    case R.id.mRefresh:
      do
      {
        //return i;
        if (this.debug.booleanValue())
          System.out.println("ZmView selected refresh show_monitor=" + String.valueOf(this.show_monitors));
        if (this.show_monitors > 0)
        {
          this.show_monitors = j;
          ShowLoadingMenu(i/*Boolean.valueOf(i)*/);
        }
      }
      while (this.show_events != 4);
      this.show_events = j;
      ShowLoadingMenu(i/*Boolean.valueOf(i)*/);
      return i;
    case R.id.mexit:
      finish();
      return i;
    case R.id.mPro:
      Intent localIntent2 = new Intent("android.intent.action.VIEW");
      localIntent2.setData(Uri.parse("market://details?id=com.html5clouds.zmview.pro"));
      startActivity(localIntent2);
      return i;
    case R.id.mAbout:
      startActivity(new Intent(this, About.class));
      return i;
    case R.id.mSettings:
	 if (this.isPro)
	 {
      if (this.debug.booleanValue())
        System.out.println("ZmView selected settings");
      this.prefs.edit().putString("zm_name", "New Server").commit();
      this.prefs.edit().putString("zm_host", "http://www.yourserver.com/zm").commit();
      this.prefs.edit().putString("zm_user", "admin").commit();
      this.prefs.edit().putString("zm_pass", "admin").commit();
      Intent localIntent1 = new Intent(this, Settings.class);
      localIntent1.putExtra("setting_options", i);
      startActivityForResult(localIntent1, j);
      return i;
	  }
    }
   
    ShowDialogPro();
 
    return i;
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    this.item1 = paramMenu.findItem(R.id.mRefresh);
    paramMenu.getItem(0).setVisible(false);
    if (this.isPro)
      paramMenu.getItem(2).setVisible(false);
    return super.onPrepareOptionsMenu(paramMenu);
  }

  public Object onRetainNonConfigurationInstance()
  {
    this._initTask.detach();
    if (this.debug.booleanValue())
      System.out.println("ZmView : We do resume OnRetain");
    return this._initTask;
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("loading_server", this.loading_server);
    paramBundle.putInt("show_events", this.show_events);
    paramBundle.putInt("show_monitors", this.show_monitors);
    paramBundle.putInt("selected_page", this.selected_page.intValue());
    paramBundle.putInt("nr_pages", this.nr_pages.intValue());
    paramBundle.putInt("events_type", this.events_type);
    paramBundle.putInt("ZmID", this.ZmID);
    paramBundle.putString("MonitorID", this.MonitorID);
    paramBundle.putString("MonitorName", this.MonitorName);
    paramBundle.putSerializable("myZmCamera", this.myZmCamera);
    paramBundle.putSerializable("myZmServers", this.myZmServers);
  }

  public void onStart()
  {
    super.onStart();
    load_settings();
    this.isPro = isProInstalled(getApplicationContext());
    if (this.isPro)
    {
    	/*
      if (this.menu != null)
        this.menu.getItem(2).setVisible(false);
      if (this.adView != null)
        this.adView.setVisibility(8);
        */
    }
    if (this.debug.booleanValue())
      System.out.println("ZmView we are on start and isPro=" + String.valueOf(this.isPro));
    if (this._initTask.isCancelled())
    {
      if (this.debug.booleanValue())
        System.out.println("ZmView : Task is not running onStart ");
      if (this.debug.booleanValue())
        System.out.println("ZmView : create new task from onStart");
      this._initTask = new InitTask(this);
      this._initTask.execute(new Context[] { this });
    }
    //while (true)
    else
    {
      //return;
      this._initTask.attach(this);
    }
    if (this.debug.booleanValue())
        System.out.println("ZmView : We finish onStart ");
  }

  public void onStop()
  {
    super.onStop();
    if (this.debug.booleanValue())
      System.out.println("ZmView : We finish onStop ");
    this._initTask.cancel(true);
  }

  public Boolean open_web_connection(String paramString1, String paramString2)
  {
    if (this.debug.booleanValue())
      System.out.println("We are on open_web_conect show_monitors=" + String.valueOf(this.show_monitors) + " show_events=" + String.valueOf(this.show_events));
    try
    {
      String str1 = paramString2 + "&username=" + URLEncoder.encode(this.zm_user, "UTF-8") + "&password=" + URLEncoder.encode(this.zm_pass, "UTF-8") + "&action=login";
      this.url1 = new URL(paramString1 + "?" + str1);
      this.ucs = null;
      this.uc = null;
      if (paramString1.contains("https:"))
      {
        new FakeSSL();
        FakeSSL.allowAllSSL();
        if (this.ucs != null)
          this.ucs.disconnect();
        this.ucs = ((HttpsURLConnection)this.url1.openConnection());
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
        if (this.ucs.getResponseCode() >= 400)
        {
          System.out.println("ZmView apache error code " + String.valueOf(this.ucs.getResponseCode()));
          show_message(this.uc.getErrorStream().toString());
          this.show_monitors = 10;
          this.messageLog = new HttpErrorMsg().Error(this.ucs.getResponseCode());
          return Boolean.valueOf(false);
        }
      }
      else
      {
        if (this.uc != null)
          this.uc.disconnect();
        this.uc = ((HttpURLConnection)this.url1.openConnection());
        this.uc.setRequestProperty("Authorization", "Basic " + Base64.encodeToString(new StringBuilder(String.valueOf(this.zm_user)).append(":").append(this.zm_pass).toString().getBytes(), 0).trim());
        if (this.uc.getResponseCode() >= 400)
        {
          System.out.println("ZmView apache error code " + String.valueOf(this.uc.getResponseCode()));
          this.show_monitors = 10;
          this.messageLog = new HttpErrorMsg().Error(this.uc.getResponseCode());
          return Boolean.valueOf(false);
        }
      }
      if (this.debug.booleanValue())
        System.out.println("ZmView : connecting to " + paramString1 + "?" + str1);
      return Boolean.valueOf(true);
    }
    catch (MalformedURLException localMalformedURLException)
    {
      if (this.debug.booleanValue())
        System.out.println("ZmView : Connection failed on open_web_connection");
      this.show_monitors = 10;
      if (this.debug.booleanValue())
        System.out.println("We are on open_web_conect2 show_monitors=" + String.valueOf(this.show_monitors) + " show_events=" + String.valueOf(this.show_events));
      this.messageLog = localMalformedURLException.getMessage();
      localMalformedURLException.printStackTrace();
      return Boolean.valueOf(true);
    }
    catch (IOException localIOException)
    {
      this.show_monitors = 10;
      this.messageLog = localIOException.getMessage();
      if (this.debug.booleanValue())
        System.out.println("We are on open_web_conect show_monitors=" + String.valueOf(this.show_monitors) + " show_events=" + String.valueOf(this.show_events));
      if (this.debug.booleanValue())
        System.out.println("ZmView : Connection failed2 on open_web_connection");
      localIOException.printStackTrace();
    }
    return Boolean.valueOf(false);
  }

  public void show_events(Integer paramInteger, String paramString1, String paramString2, String paramString3)
  {
    Integer localInteger = Integer.valueOf(0);
    ArrayList localArrayList = new ArrayList();
    if (this.nr_pages.intValue() == 0)
      localInteger = Integer.valueOf(1);
    if (this.debug.booleanValue())
      System.out.println("ZMVIEW : pages" + String.valueOf(localInteger));
    this.nr_pages = localInteger;
    for (int i = 1; ; i++)
    {
      if (i > localInteger.intValue())
      {
        this.scontrol = ((Spinner)findViewById(R.id.spinner2));
        this.scontrol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
          public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
          {
            selected_page = Integer.valueOf(scontrol.getSelectedItemPosition());
            if (last_page_selected != selected_page)
            {
              last_page_selected = selected_page;
              if ((isPro) || (scontrol.getSelectedItemPosition() == 0))
              {
                selected_page = Integer.valueOf(scontrol.getSelectedItemPosition());
                show_events = 1;
                ShowLoading(Boolean.valueOf(true));
              }
            }
            else
            {
              return;
            }
            ShowDialogPro();
          }

          public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView)
          {
          }
        });
        ArrayAdapter localArrayAdapter = new ArrayAdapter(this, 17367048, localArrayList);
        localArrayAdapter.setDropDownViewResource(17367049);
        this.scontrol.setAdapter(localArrayAdapter);
        return;
      }
      localArrayList.add("Page " + String.valueOf(i));
    }
  }

  public void update_events()
  {
    ListView localListView = (ListView)findViewById(R.id.listView2);
    localListView.setAdapter(new SimpleAdapter(this, this.listEvents, 2130903078, new String[] { "col1", "col2", "col3", "col4", "col5" }, new int[] { 2131099718, 2131099719, 2131099720, 2131099721, 2131099722 }));
    localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
    	  if (true)
        {
	        Intent localIntent = new Intent(ZmViewActivity.this, ViewEvents.class);
	        String str = ((String)((HashMap)listEvents.get(paramAnonymousInt)).get("col1")).toString();
	        int i = Integer.valueOf(((String)((HashMap)listEvents.get(paramAnonymousInt)).get("col3")).toString()).intValue();
	        localIntent.putExtra("URL", zm_host + "/../cgi-bin/nph-zms?source=event&mode=jpeg&event=" + str + "&frame=1&scale=100&rate=100&maxfps=5&replay=single&" + zm_get_auth);
	        localIntent.putExtra("myZmCamera", myZmCamera);
	        localIntent.putExtra("ZmID", ZmID);
	        localIntent.putExtra("zm_user", zm_user);
	        localIntent.putExtra("zm_pass", zm_pass);
	        localIntent.putExtra("zm_auth", zm_auth);
	        localIntent.putExtra("zm_host", zm_host);
	        localIntent.putExtra("actionId", 2);
	        localIntent.putExtra("event_id", str);
	        localIntent.putExtra("event_time", i);
	        localIntent.putExtra("selected_page", selected_page);
	        localIntent.putExtra("nr_pages", nr_pages);
	        localIntent.putExtra("sFilterEvents", sFilterEvents);
	        localIntent.putExtra("zm_get_auth", zm_get_auth);
	        startActivity(localIntent);
        }
    	else
    	{
    	  //Intent localIntent_1 = new Intent(ZmViewActivity.this, ZmFlashPlayerView.class);
    	 // String VideoUrls = (prefs.getString("zm_bin_path", "/../cgi-bin/nph-zms") + "?mode=mpeg&monitor=" + ((ZmCamera)this.myZmCamera.get(this.ZmID)).getID() + "&scale=75&maxfps=5&bitrate=8000&format=swf") ;
    	  //VideoUrls  = zm_host + VideoUrls;
    	  //localIntent_1.putExtra("VideoUrls", VideoUrls);
          //localIntent.putExtra("vid", localVideoDetailData.getVideoId());
          //localIntent.putExtra("reqpage", 0);
          //localIntent.putExtra("flashparam", localVideoDetailData.getFlashParameters());
          //startActivity(localIntent_1);
    	 }
      }
    });
    ArrayList localArrayList = new ArrayList();
    for (int i = 1; ; i++)
    {
      if (i > this.nr_pages.intValue())
      {
        ArrayAdapter localArrayAdapter = new ArrayAdapter(this, 17367048, localArrayList);
        this.scontrol.setAdapter(localArrayAdapter);
        this.scontrol.setSelection(this.last_page_selected.intValue());
        return;
      }
      localArrayList.add("Page " + String.valueOf(i));
    }
  }

  final class DoneHandlerInputStream extends FilterInputStream
  {
    private boolean done;

    public DoneHandlerInputStream(InputStream arg2)
    {
      //for compile
      //super();
      super(arg2);
    }

    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      if (!this.done)
      {
        int i = super.read(paramArrayOfByte, paramInt1, paramInt2);
        if (i != -1)
          return i;
      }
      this.done = true;
      return -1;
    }
  }

  protected class InitTask extends AsyncTask<Context, Integer, String>
  {
    ZmViewActivity activity = null;

    InitTask(ZmViewActivity arg2)
    {
      //??????
      //ZmViewActivity localZmViewActivity;
      attach(arg2 /*localZmViewActivity*/);
    }

    void attach(ZmViewActivity paramZmViewActivity)
    {
      this.activity = paramZmViewActivity;
    }

    void detach()
    {
      this.activity = null;
    }

    protected String doInBackground(Context[] paramArrayOfContext)
    {
      while (true)
      {
        if (isCancelled())
          return "COMPLETE!";
        try
        {
          Thread.sleep(1000L);
          if (show_monitors == 1)
          {
            show_monitors = 2;
            if (load_monitors().booleanValue())
            {
              zm_get_auth = getAuth(((ZmCamera)myZmCamera.get(0)).getID());
              show_monitors = 3;
            }
            if (debug.booleanValue())
              System.out.println("ZmView : loading camera...");
          }
          if (show_events == 1)
          {
            show_events = 2;
            if (myZmCamera.size() < 1)
              load_monitors();
            if (debug.booleanValue())
              System.out.println("ZmView : we are starting load_events");
            load_events(MonitorID, events_type);
            show_events = 3;
            if (debug.booleanValue())
              System.out.println("ZmView : loading events...monitor=" + MonitorID + " page = " + String.valueOf(1 + selected_page.intValue()));
          }
          if (debug.booleanValue())
            System.out.println("ZmView : main loop is on...selected_page=" + String.valueOf(selected_page));
          Integer[] arrayOfInteger = new Integer[1];
          arrayOfInteger[0] = Integer.valueOf(0);
          publishProgress(arrayOfInteger);
        }
        catch (Exception localException)
        {
        }
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
      if ((show_monitors == 10) || (show_events == 10))
      {
        show_monitors = 4;
        show_events = 0;
        ShowLoading(Boolean.valueOf(false));
        if ((tmpProgRefresh != null) && (tmpProgRefresh.getVisibility() == 0))
        {
          tmpProgRefresh.setVisibility(8);
          tmpProgRefresh = null;
          tmpImgRefresh.setVisibility(0);
        }
        show_message("Connection failed.\n" + messageLog.toString());
      }
      if ((show_monitors == 3) || (show_events == 3))
      {
        ShowLoading(Boolean.valueOf(false));
        if ((tmpProgRefresh != null) && (tmpProgRefresh.getVisibility() == 0))
        {
          tmpProgRefresh.setVisibility(8);
          tmpProgRefresh = null;
          tmpImgRefresh.setVisibility(0);
        }
      }
      if (show_monitors == 3)
      {
        ((ZmServers)myZmServers.get(loading_server)).setItems(myZmCamera);
        _ZmServersAdapter.notifyDataSetChanged();
        MainServerList.expandGroup(loading_server);
        show_monitors = 4;
      }
      if (show_events == 3)
      {
        update_events();
        show_events = 4;
      }
      if (debug.booleanValue())
        System.out.println("ZmView showevents=" + String.valueOf(show_events));
      if (debug.booleanValue())
        System.out.println("ZmView showmonitors=" + String.valueOf(show_monitors));
    }
  }

  protected class btnNextClick
    implements View.OnClickListener
  {
    protected btnNextClick()
    {
    }

    public void onClick(View paramView)
    {
      if (selected_page.intValue() < -1 + nr_pages.intValue())
      {
        if (isPro)
        {
          ZmViewActivity localZmViewActivity = ZmViewActivity.this;
          localZmViewActivity.selected_page = Integer.valueOf(1 + localZmViewActivity.selected_page.intValue());
          scontrol.setSelection(selected_page.intValue());
        }
      }
      else
        return;
      ShowDialogPro();
    }
  }

  protected class btnPrevClick
    implements View.OnClickListener
  {
    protected btnPrevClick()
    {
    }

    public void onClick(View paramView)
    {
      if (selected_page.intValue() > 0)
      {
        if (isPro)
        {
          ZmViewActivity localZmViewActivity = ZmViewActivity.this;
          localZmViewActivity.selected_page = Integer.valueOf(-1 + localZmViewActivity.selected_page.intValue());
          scontrol.setSelection(selected_page.intValue());
        }
      }
      else
        return;
      ShowDialogPro();
    }
  }

  public class camviewAdapter extends BaseAdapter
  {
    Context context;
    int layoutResourceId;

    public camviewAdapter(Context paramInt, int arg3)
    {
      //????
      //int i;
      //this.layoutResourceId = i;
      this.layoutResourceId = arg3;
      this.context = paramInt;
    }

    public int getCount()
    {
      return myZmCamera.size();
    }

    public Object getItem(int paramInt)
    {
      return null;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView = ((Activity)this.context).getLayoutInflater().inflate(this.layoutResourceId, paramViewGroup, false);
      TextView localTextView1 = (TextView)localView.findViewById(R.id.ID);
      TextView localTextView2 = (TextView)localView.findViewById(R.id.NAME);
      TextView localTextView3 = (TextView)localView.findViewById(R.id.STATUS);
      TextView localTextView4 = (TextView)localView.findViewById(R.id.EVENTS);
      VIEW = localView.findViewById(R.id.View01);
      localTextView1.setText(((ZmCamera)myZmCamera.get(paramInt)).getID());
      localTextView2.setText(((ZmCamera)myZmCamera.get(paramInt)).getName());
      if (((ZmCamera)myZmCamera.get(paramInt)).getStatus().equals("None"))
        localTextView3.setText("None");
      if (((ZmCamera)myZmCamera.get(paramInt)).getStatus().equals("Moni"))
        localTextView3.setText("Monitor");
      if (((ZmCamera)myZmCamera.get(paramInt)).getStatus().equals("Mode"))
        localTextView3.setText("Modect");
      if (((ZmCamera)myZmCamera.get(paramInt)).getStatus().equals("Reco"))
        localTextView3.setText("Record");
      if (((ZmCamera)myZmCamera.get(paramInt)).getStatus().equals("Moco"))
        localTextView3.setText("Mocord");
      if (((ZmCamera)myZmCamera.get(paramInt)).getStatus().equals("Node"))
        localTextView3.setText("Nodect");
      localTextView4.setText(((ZmCamera)myZmCamera.get(paramInt)).getEventsH());
      float[] arrayOfFloat = new float[3];
      arrayOfFloat[0] = (180 - Math.min(6 * Integer.valueOf(((ZmCamera)myZmCamera.get(paramInt)).getEventsH()).intValue(), 180));
      arrayOfFloat[1] = 1.0F;
      arrayOfFloat[2] = 1.0F;
      VIEW.setBackgroundColor(Color.HSVToColor(arrayOfFloat));
      return localView;
    }
  }
}

/* Location:           E:\android 反\工具\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.ZmViewActivity
 * JD-Core Version:    0.6.2
 */
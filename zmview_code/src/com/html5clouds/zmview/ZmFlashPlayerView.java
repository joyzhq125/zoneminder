package com.html5clouds.zmview;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class ZmFlashPlayerView extends Activity
{
	private WebView preview = null;
	private String srcHtml;
	private String flashUrl;
	
	private void playFlash()
	{
		//preview.loadDataWithBaseURL(null, this.srcHtml, "text/html", "utf-8", "about:blank");
		//preview.loadData(srcHtml, "text/html", "utf-8");
		//preview.loadUrl("http://www.baidu.com");
		//AssetManager assets = getAssets();
		if (check_install_flashplayer())
		{
			preview.loadDataWithBaseURL(null, this.srcHtml, "text/html", "utf-8", "about:blank");
			//preview.loadUrl("file:///android_asset/nph-zmsmode=mpeg.swf");
			preview.requestFocus();
		}
		else
		{
			install_flashplayer();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle localBundle = getIntent().getExtras();
	    setContentView(R.layout.flashplay);
	    preview = ((WebView)findViewById(R.id.webView1));
	    getWindow().addFlags(128);
	    
	    Display localDisplay = getWindowManager().getDefaultDisplay();
	    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
	    localDisplay.getMetrics(localDisplayMetrics);
	    int i = (int)(localDisplay.getWidth() / localDisplayMetrics.scaledDensity);
	    if (i % 2 == 1)
	    	i++;
	    int j = (int)(localDisplay.getHeight() / localDisplayMetrics.scaledDensity);
	    if (j % 2 == 1)
	    	j++;
	    //flashUrl = localBundle.getString("VideoUrls");
	    /*
	    srcHtml = ("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
	    		"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"ja\" lang=\"ja\">" +
	    		"<head>" +
		    		"<meta http-equiv=\"Content-Type\" content=\"text/html;" +
		    		" charset=utf-8\" />" +
		    	"</head>" +
	    		"<body style=\"margin:0px\" scroll=\"no\" bgcolor=\"#000000\" onload=\"document.getElementById(\"flashview\").focus();\">" +
	    		"<object id=\"flashview\" classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0\" width=\"" +
	    		i + "\" height=\"" + j + "\" id=\"flv2\" align=\"middle\" open_target=\"_self\">" + 
	    		"<param name=\"allowScriptAccess\" value=\"always\" />" + 
	    		"<param name=\"movie\" value=\"" + this.flashUrl + "\" />" + 
	    		"<param name=\"quality\" value=\"high\" />" +
	    		"<param name=\"bgcolor\" value=\"#000000\" />" + 
	    		"<param name=\"allowFullScreen\" value=\"true\" />" + 
	    		"<param name=\"wmode\" value=\"window\" />" +
	    		"<embed " +
	    			"src=\"" + this.flashUrl + "\" quality=\"high\" wmode=\"winodw\" bgcolor=\"#000000\" " +
	    			"width=\"" + i + "\" height=\"" + j + "\" " +
	    			"name=\"flv2\" align=\"middle\" open_target=\"_self\" allowScriptAccess=\"always\" " +
	    			"type=\"application/x-shockwave-flash\" " +
	    			"pluginspage=\"http://www.macromedia.com/go/getflashplayer\" " +
	    			"allowFullScreen=\"true\" />" +
	    		"</object>" + "</body>" + "</html>"); 
	    */
	    srcHtml= "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> "+
	    "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"ja\" lang=\"ja\"> "+
	    "<head> "+
	    "</head> "+
	    "<object id=\"liveStream\" width=\""+ i +"\" height=\"" + j +"\" classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,40,0\" type=\"application/x-shockwave-flash\"> " +
	    "<param name=\"movie\" value=\"http://192.168.0.153/cgi-bin/nph-zms?mode=mpeg&monitor=1&scale=100&bitrate=75000&maxfps=10&format=swf\"> "+
	    "<param name=quality value=\"high\"> "+
	    "<param name=bgcolor value=\"#000000\"> "+
	    "<embed type=\"application/x-shockwave-flash\" "+
	    "pluginspage=\"http://www.macromedia.com/go/getflashplayer\" "+
	    "src=\"http://192.168.0.153/cgi-bin/nph-zms?mode=mpeg&monitor=1&scale=100&bitrate=75000&maxfps=10&format=swf\" "+
	    "name=\"Monitor-1\" " +
	    "width=\"" + i + "\" "+
	    "height=\""+j + "\" "+
	    "quality=\"high\" "+
	    "bgcolor=\"#000000\" "+
	    "</embed> "+
	    "</object> "+
	    "</body> "+
	    "</html>";
		preview.clearCache(false);
		preview.getSettings().setPluginsEnabled(true);
		preview.getSettings().setJavaScriptEnabled(true);
		preview.getSettings().setPluginsEnabled(true);
		preview.getSettings().setBuiltInZoomControls(true);
		preview.setWebChromeClient(new WebChromeClient());
		//preview.getSettings().setBlockNetworkImage(false);
		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().startSync();
		CookieManager.getInstance().setAcceptCookie(true);
		CookieManager.getInstance().removeExpiredCookie();
		
		playFlash();
	}

	@Override
	protected void onDestroy() {
	// TODO Auto-generated method stub		
	 if (preview != null)
	 {
		 preview.stopLoading();
		 preview.clearCache(true);
		 preview.destroy();
		 preview = null;
	 }
	 getWindow().clearFlags(128);
	 super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	private boolean check_install_flashplayer() {  
		PackageManager pm = getPackageManager();  
		List<PackageInfo> infoList = pm.getInstalledPackages(PackageManager.GET_SERVICES);  
		       for (PackageInfo info : infoList) {  
		            if ("com.adobe.flashplayer".equals(info.packageName)) {  
		                return true;  
		            }  
		        }  
		       return false;  
		}  
	
	private void install_flashplayer() {  
			preview.addJavascriptInterface(new AndroidBridge(), "android");  
			preview.loadUrl("file:///android_asset/go_market.html");  
		}  

	private class AndroidBridge {  
		public void goMarket() { 
			Handler handler = new Handler();
			handler.post(new Runnable() {  
				public void run() { 
					try
					{     Intent installIntent = new Intent(  
		                            "android.intent.action.VIEW");  
		                    installIntent.setData(Uri  
		                            .parse("market://details?id=com.adobe.flashplayer"));  
		                    startActivity(installIntent);  
		            }  
					catch(android.content.ActivityNotFoundException e)
					{
						Toast toast = Toast.makeText(ZmFlashPlayerView.this, "未找到市场安装，请手动安装flashplayer!", Toast.LENGTH_SHORT); 
						toast.show(); 
					}
		          }
				});  
		          
		}  

	}
}
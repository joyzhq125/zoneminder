package com.html5clouds.zmview;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class Html5View extends Activity
{
  private RelativeLayout sv;
  private WebView wv;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    LinearLayout localLinearLayout = new LinearLayout(this);
    localLinearLayout.setOrientation(1);
    localLinearLayout.setGravity(1);
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
    localLinearLayout.setBackgroundColor(17170445);
    localLinearLayout.setGravity(17);
    WebView localWebView = new WebView(this);
    localWebView.getSettings().setJavaScriptEnabled(true);
    localWebView.getSettings().setBuiltInZoomControls(true);
    localWebView.loadUrl("file:///android_asset/live.html");
    localLinearLayout.addView(localWebView);
    //localWebView.setBackgroundColor(17170445);
    setContentView(localLinearLayout);
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.Html5View
 * JD-Core Version:    0.6.2
 */
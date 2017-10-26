package com.html5clouds.zmview;

import android.os.Bundle;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;

public class About extends SherlockActivity
{
  public static final String[] DIALOGUE = { "\tZMView is a ZoneMinder tool for remotely control your ZoneMinder servers from android os.\n", "\tFeatures :", "\t\t - Live View", "\t\t - Event View, browse events", "\t\t - Android compatible", "\t\t - Unlimited video on 3G/4G  and wifi", "\t\t - Authentication", "\t\t - Complete privacy for your streams, link is made directly to your device", "\t\t - Landscape and portrait mode, full size/stretch", "\t\t - Added https support" };

  protected void onCreate(Bundle paramBundle)
  {
    requestWindowFeature(9L);
    super.onCreate(paramBundle);
    setContentView(R.layout.aboutlayout);
    StringBuilder localStringBuilder = new StringBuilder();
    String[] arrayOfString = DIALOGUE;
    int i = arrayOfString.length;
    for (int j = 0; ; j++)
    {
      if (j >= i)
      {
        ((TextView)findViewById(R.id.bunch_of_text)).setText(localStringBuilder.toString());
        return;
      }
      localStringBuilder.append(arrayOfString[j]).append("\n");
    }
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.About
 * JD-Core Version:    0.6.2
 */
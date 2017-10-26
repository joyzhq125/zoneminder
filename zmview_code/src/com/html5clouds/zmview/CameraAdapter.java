package com.html5clouds.zmview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.PrintStream;
import java.util.ArrayList;

public class CameraAdapter extends BaseAdapter
{
  protected Context context;
  private LayoutInflater inflater;
  protected ArrayList<ZmCamera> myZmCamera;

  public CameraAdapter(Context paramContext, ArrayList<ZmCamera> paramArrayList)
  {
    this.context = paramContext;
    this.myZmCamera = paramArrayList;
    this.inflater = LayoutInflater.from(paramContext);
  }

  public int getCount()
  {
    return this.myZmCamera.size();
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
    LinearLayout localLinearLayout = new LinearLayout(this.context);
    localLinearLayout.setOrientation(1);
    ImageView localImageView = new ImageView(this.context);
    localImageView.setImageResource(17301567);
    localImageView.setScaleType(ImageView.ScaleType.FIT_XY);
    localImageView.setLayoutParams(new Gallery.LayoutParams(50, 50));
    TextView localTextView = new TextView(this.context);
    localTextView.setText(((ZmCamera)this.myZmCamera.get(paramInt)).getName());
    localTextView.setTextSize(12.0F);
    localTextView.setGravity(17);
    localLinearLayout.addView(localImageView);
    localLinearLayout.addView(localTextView);
    localLinearLayout.setBackgroundColor(-16777216);
    System.out.println("ZmView : Add gallery camera " + ((ZmCamera)this.myZmCamera.get(paramInt)).getName());
    return localLinearLayout;
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.CameraAdapter
 * JD-Core Version:    0.6.2
 */
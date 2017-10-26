package com.html5clouds.zmview;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ZmServersAdapter extends BaseExpandableListAdapter
{
  private Context context;
  private ArrayList<ZmServers> groups;

  public ZmServersAdapter(Context paramContext, ArrayList<ZmServers> paramArrayList)
  {
    this.context = paramContext;
    this.groups = paramArrayList;
  }
  //@Override
  public void addItem(ZmCamera paramZmCamera, ZmServers paramZmServers)
  {
    if (!this.groups.contains(paramZmServers))
      this.groups.add(paramZmServers);
    int i = this.groups.indexOf(paramZmServers);
    ArrayList localArrayList = ((ZmServers)this.groups.get(i)).getItems();
    localArrayList.add(paramZmCamera);
    ((ZmServers)this.groups.get(i)).setItems(localArrayList);
  }
  @Override
  public Object getChild(int paramInt1, int paramInt2)
  {
    return ((ZmServers)this.groups.get(paramInt1)).getItems().get(paramInt2);
  }
  @Override
  public long getChildId(int paramInt1, int paramInt2)
  {
    return paramInt2;
  }
  @Override
  public View getChildView(int paramInt1, int paramInt2, boolean paramBoolean, View paramView, ViewGroup paramViewGroup)
  {
    ZmCamera localZmCamera = (ZmCamera)getChild(paramInt1, paramInt2);
    if (paramView == null)
    {
    	paramView = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(R.layout.monitorview1, null);
    }
    if (localZmCamera != null)
    {
      float[] arrayOfFloat = new float[3];
      TextView localTextView1 = (TextView)paramView.findViewById(R.id.ID);
      TextView localTextView2 = (TextView)paramView.findViewById(R.id.NAME);
      TextView localTextView3 = (TextView)paramView.findViewById(R.id.STATUS);
      TextView localTextView4 = (TextView)paramView.findViewById(R.id.EVENTS);
      View localView = paramView.findViewById(R.id.View01);
      localTextView1.setText(localZmCamera.getID());
      localTextView2.setText(localZmCamera.getName());
      if (localZmCamera.getStatus() != null) 
      {
	      if (localZmCamera.getStatus().equals("None"))
	        localTextView3.setText("None");
	      if (localZmCamera.getStatus().equals("Moni"))
	        localTextView3.setText("Monitor");
	      if (localZmCamera.getStatus().equals("Mode"))
	        localTextView3.setText("Modect");
	      if (localZmCamera.getStatus().equals("Reco"))
	        localTextView3.setText("Record");
	      if (localZmCamera.getStatus().equals("Moco"))
	        localTextView3.setText("Mocord");
	      if (localZmCamera.getStatus().equals("Node"))
	        localTextView3.setText("Nodect");
	      localTextView4.setText(localZmCamera.getEventsH());
      }

      if (localZmCamera.getEventsH() != null)
      {
    	  arrayOfFloat[0] = (180 - Math.min(6 * Integer.valueOf(localZmCamera.getEventsH()).intValue(), 180));
      }
      arrayOfFloat[1] = 1.0F;
      arrayOfFloat[2] = 1.0F;
      localView.setBackgroundColor(Color.HSVToColor(arrayOfFloat));
    }
    return paramView;
  }
  @Override
  public int getChildrenCount(int paramInt)
  {
    ArrayList localArrayList = ((ZmServers)this.groups.get(paramInt)).getItems();
    if (localArrayList != null)
      return localArrayList.size();
    return 0;
  }
  @Override
  public Object getGroup(int paramInt)
  {
    return this.groups.get(paramInt);
  }
  @Override
  public int getGroupCount()
  {
    return this.groups.size();
  }
  @Override
  public long getGroupId(int paramInt)
  {
    return paramInt;
  }
  @Override
  public View getGroupView(int paramInt, boolean paramBoolean, View paramView, ViewGroup paramViewGroup)
  {
    ZmServers localZmServers = (ZmServers)getGroup(paramInt);
    if (paramView == null)
      paramView = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(R.layout.servers_layout, null);
    ((TextView)paramView.findViewById(R.id.serverName)).setText(localZmServers.getName());
    ((TextView)paramView.findViewById(R.id.serverHost)).setText(localZmServers.getHost());
    return paramView;
  }
  @Override
  public boolean hasStableIds()
  {
    return true;
  }
  @Override
  public boolean isChildSelectable(int paramInt1, int paramInt2)
  {
    return true;
  }
  @Override
  public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    super.registerDataSetObserver(paramDataSetObserver);
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.ZmServersAdapter
 * JD-Core Version:    0.6.2
 */
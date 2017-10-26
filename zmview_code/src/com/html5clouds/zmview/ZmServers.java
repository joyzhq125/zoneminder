package com.html5clouds.zmview;

import java.io.Serializable;
import java.util.ArrayList;

public class ZmServers
  implements Serializable
{
  private static final long serialVersionUID = 7889810846688102813L;
  private String Host;
  private ArrayList<ZmCamera> Items;
  private String Name;
  private String Pass;
  private String User;

  public String getHost()
  {
    return this.Host;
  }

  public ArrayList<ZmCamera> getItems()
  {
    return this.Items;
  }

  public String getName()
  {
    return this.Name;
  }

  public String getPass()
  {
    return this.Pass;
  }

  public String getUser()
  {
    return this.User;
  }

  public void setHost(String paramString)
  {
    this.Host = paramString;
  }

  public void setItems(ArrayList<ZmCamera> paramArrayList)
  {
    this.Items = paramArrayList;
  }

  public void setName(String paramString)
  {
    this.Name = paramString;
  }

  public void setPass(String paramString)
  {
    this.Pass = paramString;
  }

  public void setUser(String paramString)
  {
    this.User = paramString;
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.ZmServers
 * JD-Core Version:    0.6.2
 */
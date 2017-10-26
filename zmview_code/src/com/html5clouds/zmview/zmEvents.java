package com.html5clouds.zmview;

import java.io.Serializable;

public class zmEvents
  implements Serializable
{
  private static final long serialVersionUID = 615623187259381000L;
  private String duration;
  private String frames;
  private String id;
  private String score;
  private String time;

  public String getDuration()
  {
    return this.duration;
  }

  public String getFrames()
  {
    return this.frames;
  }

  public String getID()
  {
    return this.id;
  }

  public String getScore()
  {
    return this.score;
  }

  public String getTime()
  {
    return this.time;
  }

  public void setDuration(String paramString)
  {
    this.duration = paramString;
  }

  public void setFrames(String paramString)
  {
    this.frames = paramString;
  }

  public void setID(String paramString)
  {
    this.id = paramString;
  }

  public void setScore(String paramString)
  {
    this.score = paramString;
  }

  public void setTime(String paramString)
  {
    this.time = paramString;
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.zmEvents
 * JD-Core Version:    0.6.2
 */
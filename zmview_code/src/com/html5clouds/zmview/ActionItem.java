package com.html5clouds.zmview;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class ActionItem
{
  private int actionId = -1;
  private Drawable icon;
  private boolean selected;
  private boolean sticky;
  private Bitmap thumb;
  private String title;

  public ActionItem()
  {
    this(-1, null, null);
  }

  public ActionItem(int paramInt, Drawable paramDrawable)
  {
    this(paramInt, null, paramDrawable);
  }

  public ActionItem(int paramInt, String paramString)
  {
    this(paramInt, paramString, null);
  }

  public ActionItem(int paramInt, String paramString, Drawable paramDrawable)
  {
    this.title = paramString;
    this.icon = paramDrawable;
    this.actionId = paramInt;
  }

  public ActionItem(Drawable paramDrawable)
  {
    this(-1, null, paramDrawable);
  }

  public int getActionId()
  {
    return this.actionId;
  }

  public Drawable getIcon()
  {
    return this.icon;
  }

  public Bitmap getThumb()
  {
    return this.thumb;
  }

  public String getTitle()
  {
    return this.title;
  }

  public boolean isSelected()
  {
    return this.selected;
  }

  public boolean isSticky()
  {
    return this.sticky;
  }

  public void setActionId(int paramInt)
  {
    this.actionId = paramInt;
  }

  public void setIcon(Drawable paramDrawable)
  {
    this.icon = paramDrawable;
  }

  public void setSelected(boolean paramBoolean)
  {
    this.selected = paramBoolean;
  }

  public void setSticky(boolean paramBoolean)
  {
    this.sticky = paramBoolean;
  }

  public void setThumb(Bitmap paramBitmap)
  {
    this.thumb = paramBitmap;
  }

  public void setTitle(String paramString)
  {
    this.title = paramString;
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.ActionItem
 * JD-Core Version:    0.6.2
 */
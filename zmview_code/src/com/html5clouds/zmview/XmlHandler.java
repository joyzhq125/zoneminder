package com.html5clouds.zmview;

import java.io.PrintStream;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlHandler extends DefaultHandler
{
  private boolean in_monitor = false;
  private boolean in_monitor_events = false;
  private boolean in_monitor_id = false;
  private boolean in_monitor_list = false;
  private boolean in_monitor_name = false;
  private boolean in_monitor_status = false;
  private ArrayList<ZmCamera> myZmCamera = new ArrayList();
  private ZmCamera one_monitor;

  public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    String str = new String(paramArrayOfChar, paramInt1, paramInt2);
    System.out.println("ZmView : XX" + str.toString());
    if ((this.in_monitor_list) && (this.in_monitor_id))
    {
      this.one_monitor.setID(new String(paramArrayOfChar, paramInt1, paramInt2));
      System.out.println("ZmView : XX" + str.toString());
    }
    if ((this.in_monitor_list) && (this.in_monitor_name))
      this.one_monitor.setName(new String(paramArrayOfChar, paramInt1, paramInt2));
    if ((this.in_monitor_list) && (this.in_monitor_events))
      this.one_monitor.setEvents(new String(paramArrayOfChar, paramInt1, paramInt2));
    if ((this.in_monitor_list) && (this.in_monitor_status))
      this.one_monitor.setStatus(new String(paramArrayOfChar, paramInt1, paramInt2));
  }

  public void endDocument()
    throws SAXException
  {
  }

  public void endElement(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    if (paramString2.equals("MONITOR_LIST"))
      this.in_monitor_list = false;
    //do
    if (!paramString2.equals("STATE"))
    {
      //return;
      if (paramString2.equals("MONITOR"))
      {
        this.in_monitor = false;
        this.myZmCamera.add(this.one_monitor);
        System.out.println("ZmView : Adaugam un monitor");
        return;
      }
      if (paramString2.equals("ID"))
      {
        this.in_monitor_id = false;
        return;
      }
      if (paramString2.equals("NAME"))
      {
        this.in_monitor_name = false;
        return;
      }
      if (paramString2.equals("NUMEVENTS"))
      {
        this.in_monitor_events = false;
        return;
      }
    }
    //while (!paramString2.equals("STATE"));
    this.in_monitor_status = false;
  }

  public ArrayList<ZmCamera> getParsedData()
  {
    return this.myZmCamera;
  }

  public void startDocument()
    throws SAXException
  {
    this.myZmCamera = new ArrayList();
  }

  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    System.out.println("ZmView: local=" + paramString2.toString());
    if (paramString2.equals("MONITOR_LIST"))
      this.in_monitor_list = true;
    //do
    if (!paramString2.equals("STATE"))
    {
      //return;
      if (paramString2.equals("MONITOR"))
      {
        this.one_monitor = new ZmCamera();
        this.in_monitor = true;
        return;
      }
      if (paramString2.equals("ID"))
      {
        this.in_monitor_id = true;
        return;
      }
      if (paramString2.equals("NAME"))
      {
        this.in_monitor_name = true;
        return;
      }
      if (paramString2.equals("NUMEVENTS"))
      {
        this.in_monitor_events = true;
        return;
      }
    }
    //while (!paramString2.equals("STATE"));
    this.in_monitor_status = true;
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.XmlHandler
 * JD-Core Version:    0.6.2
 */
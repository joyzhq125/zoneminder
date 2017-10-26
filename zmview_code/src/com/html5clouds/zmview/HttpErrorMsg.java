package com.html5clouds.zmview;

public class HttpErrorMsg
{
  public String Error(int paramInt)
  {
    String str = "";
    if (paramInt == 400)
      str = " Bad Request";
    if (paramInt == 401)
      str = " Authorization Required";
    if (paramInt == 402)
      str = " Payment Required (not used yet)";
    if (paramInt == 403)
      str = " Forbidden";
    if (paramInt == 404)
      str = " Not Found";
    if (paramInt == 405)
      str = " Method Not Allowed";
    if (paramInt == 406)
      str = " Not Acceptable (encoding)";
    if (paramInt == 407)
      str = " Proxy Authentication Required";
    if (paramInt == 408)
      str = " Request Timed Out";
    if (paramInt == 409)
      str = " Conflicting Request";
    if (paramInt == 410)
      str = " Gone";
    if (paramInt == 411)
      str = " Content Length Required";
    if (paramInt == 412)
      str = " Precondition Failed";
    if (paramInt == 413)
      str = " Request Entity Too Long";
    if (paramInt == 414)
      str = " Request URI Too Long";
    if (paramInt == 415)
      str = " Unsupported Media Type";
    if (paramInt == 500)
      str = " Internal Server Error";
    if (paramInt == 501)
      str = " Not Implemented";
    if (paramInt == 502)
      str = " Bad Gateway";
    if (paramInt == 503)
      str = " Service Unavailable";
    if (paramInt == 504)
      str = " Gateway Timeout";
    if (paramInt == 505)
      str = " HTTP Version Not Supported";
    return "ErrorCode - " + String.valueOf(paramInt) + "\n" + str;
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.HttpErrorMsg
 * JD-Core Version:    0.6.2
 */
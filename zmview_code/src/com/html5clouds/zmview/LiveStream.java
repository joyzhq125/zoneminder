package com.html5clouds.zmview;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LiveStream extends DataInputStream
{
  private static final int FRAME_MAX_LENGTH = 250000;/*40100;*/
  private static final int HEADER_MAX_LENGTH = 100;
  private static final String TAG = "ZmView";
  private final String CONTENT_LENGTH = "Content-Length";
  private final byte[] EOF_MARKER = { -1, -39 };
  private final byte[] SOI_MARKER = { -1, -40 };
  private int mContentLength = -1;

  public LiveStream(InputStream paramInputStream)
  {
    super(new BufferedInputStream(paramInputStream, FRAME_MAX_LENGTH));
  }

  private int getEndOfSeqeunce(DataInputStream paramDataInputStream, byte[] paramArrayOfByte)
    throws IOException
  {
    int i = 0;
    for (int j = 0; ; j++)
    {
      if (j >= FRAME_MAX_LENGTH)
        return -1;
      if ((byte)paramDataInputStream.readUnsignedByte() == paramArrayOfByte[i])
      {
        i++;
        if (i == paramArrayOfByte.length)
          return j + 1;
      }
      else
      {
        i = 0;
      }
    }
  }

  private int getStartOfSequence(DataInputStream paramDataInputStream, byte[] paramArrayOfByte)
    throws IOException
  {
    int i = getEndOfSeqeunce(paramDataInputStream, paramArrayOfByte);
    if (i < 0)
      return -1;
    return i - paramArrayOfByte.length;
  }

  private int parseContentLength(byte[] paramArrayOfByte)
    throws IOException, NumberFormatException
  {
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
    Properties localProperties = new Properties();
    localProperties.load(localByteArrayInputStream);
    return Integer.parseInt(localProperties.getProperty(CONTENT_LENGTH));
  }

  public Bitmap readLiveFrame()
    throws IOException
  {
	boolean b_swf = true;
    if (!b_swf)
	{
	    	//string aaaa;
	    mark(FRAME_MAX_LENGTH);
	
	    int i = getStartOfSequence(this, SOI_MARKER);
	    reset();
	    byte[] arrayOfByte1 = new byte[i];
	    readFully(arrayOfByte1);
	    String aaaa = new String(arrayOfByte1);
	    try
	    {
	      mContentLength = parseContentLength(arrayOfByte1);
	      reset();
	      byte[] arrayOfByte2 = new byte[mContentLength];
	      skipBytes(i);
	      readFully(arrayOfByte2);
	      return BitmapFactory.decodeStream(new ByteArrayInputStream(arrayOfByte2));
	    }
	    catch (NumberFormatException localNumberFormatException)
	    {
	      //while (true)
	      {
	        localNumberFormatException.getStackTrace();
	        mContentLength = getEndOfSeqeunce(this, EOF_MARKER);
	      }
	      return null;
	    }
	  }
	  else
	  {
		    byte[] arrayOfByte1 = new byte[FRAME_MAX_LENGTH];
		    read(arrayOfByte1);
		    String aaaa = new String(arrayOfByte1);
		    return BitmapFactory.decodeStream(new ByteArrayInputStream(arrayOfByte1));
		  
	  } 
    
  }
  
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.LiveStream
 * JD-Core Version:    0.6.2
 */
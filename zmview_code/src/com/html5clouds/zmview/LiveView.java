package com.html5clouds.zmview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import java.io.IOException;

public class LiveView extends SurfaceView
  implements SurfaceHolder.Callback
{
  public static final int POSITION_LOWER_LEFT = 12;
  public static final int POSITION_LOWER_RIGHT = 6;
  public static final int POSITION_UPPER_LEFT = 9;
  public static final int POSITION_UPPER_RIGHT = 3;
  public static final int SIZE_BEST_FIT = 4;
  public static final int SIZE_FULLSCREEN = 8;
  public static final int SIZE_STANDARD = 1;
  private static final String TAG = "ZmView";
  private Bitmap bm;
  private int bmH;
  private int bmW;
  private int bmX;
  private int bmY;
  private int dispHeight;
  private int dispWidth;
  private int displayMode;
  private LiveStream mIn = null;
  private boolean mRun = false;
  private int overlayBackgroundColor;
  private Paint overlayPaint;
  private int overlayTextColor;
  private int ovlPos;
  private boolean showFps = false;
  private boolean surfaceDone = false;
  private LiveViewThread thread;

  public LiveView(Context paramContext)
  {
    super(paramContext);
    init(paramContext);
  }

  public LiveView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }

  private void init(Context paramContext)
  {
    SurfaceHolder localSurfaceHolder = getHolder();
    localSurfaceHolder.addCallback(this);
    this.thread = new LiveViewThread(localSurfaceHolder, paramContext);
    setFocusable(true);
    this.overlayPaint = new Paint();
    this.overlayPaint.setTextAlign(Paint.Align.LEFT);
    this.overlayPaint.setTextSize(12.0F);
    this.overlayPaint.setTypeface(Typeface.DEFAULT);
    this.overlayTextColor = -1;
    this.overlayBackgroundColor = -16777216;
    this.ovlPos = 3;
    this.displayMode = 8;
    this.dispWidth = getWidth();
    this.dispHeight = getHeight();
  }

  public int GetBmH()
  {
    return this.bmH;
  }

  public int GetBmW()
  {
    return this.bmW;
  }

  public int GetBmX()
  {
    return this.bmX;
  }

  public int GetBmY()
  {
    return this.bmY;
  }

  public int getDisplayMode()
  {
    return this.displayMode;
  }

  public void setDisplayMode(int paramInt)
  {
    this.displayMode = paramInt;
  }

  public void setOverlayBackgroundColor(int paramInt)
  {
    this.overlayBackgroundColor = paramInt;
  }

  public void setOverlayPaint(Paint paramPaint)
  {
    this.overlayPaint = paramPaint;
  }

  public void setOverlayPosition(int paramInt)
  {
    this.ovlPos = paramInt;
  }

  public void setOverlayTextColor(int paramInt)
  {
    this.overlayTextColor = paramInt;
  }

  public void setSource(LiveStream paramLiveStream)
  {
    this.mIn = paramLiveStream;
    startPlayback();
  }

  public void setSource2(LiveStream paramLiveStream)
  {
    this.mIn = paramLiveStream;
  }

  public void showFps(boolean paramBoolean)
  {
    this.showFps = paramBoolean;
  }

  public void startPlayback()
  {
    if (this.mIn != null)
    {
      this.mRun = true;
      if (!this.thread.isAlive())
        this.thread.start();
    }
  }

  public void stopPlayback()
  {
    this.mRun = false;
    int i = 1;
    while (true)
    {
      if (i == 0)
        return;
      try
      {
        this.thread.join();
        i = 0;
      }
      catch (InterruptedException localInterruptedException)
      {
        localInterruptedException.getStackTrace();
        Log.d("ZmView", "catch IOException hit in stopPlayback", localInterruptedException);
      }
    }
  }

  public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
  {
    this.thread.setSurfaceSize(paramInt2, paramInt3);
  }

  public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
  {
    this.surfaceDone = true;
  }

  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
  {
    this.surfaceDone = false;
    stopPlayback();
  }

  public class LiveViewThread extends Thread
  {
    private int frameCounter = 0;
    private SurfaceHolder mSurfaceHolder;
    private Bitmap ovl;
    private long start;

    public LiveViewThread(SurfaceHolder paramContext, Context arg3)
    {
      this.mSurfaceHolder = paramContext;
    }

    private Rect destRect(int paramInt1, int paramInt2)
    {
      if (LiveView.this.displayMode == 1)
      {
        int m = LiveView.this.dispWidth / 2 - paramInt1 / 2;
        LiveView.this.bmX = m;
        LiveView.this.bmY = 0;
        LiveView.this.bmW = paramInt1;
        LiveView.this.bmH = paramInt2;
        return new Rect(m, 0, paramInt1 + m, paramInt2 + 0);
      }
      if (LiveView.this.displayMode == 4)
      {
        float f = paramInt1 / paramInt2;
        int i = LiveView.this.dispWidth;
        int j = (int)(LiveView.this.dispWidth / f);
        if (j > LiveView.this.dispHeight)
        {
          j = LiveView.this.dispHeight;
          i = (int)(f * LiveView.this.dispHeight);
        }
        int k = LiveView.this.dispWidth / 2 - i / 2;
        LiveView.this.bmX = k;
        LiveView.this.bmY = 0;
        LiveView.this.bmW = i;
        LiveView.this.bmH = j;
        return new Rect(k, 0, i + k, j + 0);
      }
      if (LiveView.this.displayMode == 8)
      {
        LiveView.this.bmX = 0;
        LiveView.this.bmY = 0;
        LiveView.this.bmW = LiveView.this.dispWidth;
        LiveView.this.bmH = LiveView.this.dispHeight;
        return new Rect(0, 0, LiveView.this.dispWidth, LiveView.this.dispHeight);
      }
      return null;
    }

    private Rect destRect2(int paramInt1, int paramInt2)
    {
      if (LiveView.this.displayMode == 1)
      {
        int n = LiveView.this.dispWidth / 2 - paramInt1 / 2;
        int i1 = LiveView.this.dispHeight / 2 - paramInt2 / 2;
        LiveView.this.bmX = n;
        LiveView.this.bmY = i1;
        LiveView.this.bmW = paramInt1;
        LiveView.this.bmH = paramInt2;
        return new Rect(n, i1, paramInt1 + n, paramInt2 + i1);
      }
      if (LiveView.this.displayMode == 4)
      {
        float f = paramInt1 / paramInt2;
        int i = LiveView.this.dispWidth;
        int j = (int)(LiveView.this.dispWidth / f);
        if (j > LiveView.this.dispHeight)
        {
          j = LiveView.this.dispHeight;
          i = (int)(f * LiveView.this.dispHeight);
        }
        int k = LiveView.this.dispWidth / 2 - i / 2;
        int m = LiveView.this.dispHeight / 2 - j / 2;
        LiveView.this.bmX = k;
        LiveView.this.bmY = m;
        LiveView.this.bmW = i;
        LiveView.this.bmH = j;
        return new Rect(k, m, i + k, j + m);
      }
      if (LiveView.this.displayMode == 8)
      {
        LiveView.this.bmX = 0;
        LiveView.this.bmY = 0;
        LiveView.this.bmW = LiveView.this.dispWidth;
        LiveView.this.bmH = LiveView.this.dispHeight;
        return new Rect(0, 0, LiveView.this.dispWidth, LiveView.this.dispHeight);
      }
      return null;
    }

    private Bitmap makeFpsOverlay(Paint paramPaint, String paramString)
    {
      Rect localRect = new Rect();
      paramPaint.getTextBounds(paramString, 0, paramString.length(), localRect);
      int i = 2 + localRect.width();
      int j = 2 + localRect.height();
      Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
      Canvas localCanvas = new Canvas(localBitmap);
      paramPaint.setColor(LiveView.this.overlayBackgroundColor);
      localCanvas.drawRect(0.0F, 0.0F, i, j, paramPaint);
      paramPaint.setColor(LiveView.this.overlayTextColor);
      localCanvas.drawText(paramString, 1 + -localRect.left, 1.0F + (j / 2 - (paramPaint.ascent() + paramPaint.descent()) / 2.0F), paramPaint);
      return localBitmap;
    }

    public void run()
    {
    	
      this.start = System.currentTimeMillis();
      PorterDuffXfermode localPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OVER);
      Canvas localCanvas = null;
      Paint localPaint = new Paint();
      while (true)
      {
        if (!LiveView.this.mRun)
          return;
        if (!LiveView.this.surfaceDone)
          continue;
        try
        {
          localCanvas = this.mSurfaceHolder.lockCanvas();
          synchronized (this.mSurfaceHolder)
          {
            try
            {
              LiveView.this.bm = LiveView.this.mIn.readLiveFrame();
              Rect localRect = destRect(LiveView.this.bm.getWidth(), LiveView.this.bm.getHeight());
              //localCanvas.drawColor(-16777216);
              localCanvas.drawBitmap(LiveView.this.bm, null, localRect, localPaint);
              int i;
              int j;
              int k;
              if (LiveView.this.showFps)
              {
                localPaint.setXfermode(localPorterDuffXfermode);
                if (this.ovl != null)
                {
                  //if ((0x1 & LiveView.this.ovlPos) != 1)
                  //  break label319;
                  i = localRect.top;
                  //label175: if ((0x8 & LiveView.this.ovlPos) != 8)
                  //  break label337;
                }
              }
              //label319: label337: int j;

			  j = localRect.right;
			  k = ovl.getWidth();
			  int m = localRect.left;
			  m = j - k;
              //for (int m = localRect.left; ; m = j - k)
              {
				i = localRect.bottom - ovl.getHeight();	
				mSurfaceHolder.unlockCanvasAndPost(localCanvas);
				
                localCanvas.drawBitmap(ovl, m, i, null);
                localPaint.setXfermode(null);
                frameCounter = (1 + frameCounter);
                if (System.currentTimeMillis() - start >= 1000L)
                {
                  String str = String.valueOf(frameCounter) + " fps";
                  frameCounter = 0;
                  start = System.currentTimeMillis();
                  ovl = makeFpsOverlay(overlayPaint, str);
                }
                //if (localCanvas == null)
               //   break;
               // mSurfaceHolder.unlockCanvasAndPost(localCanvas);
                //break;
              
              }
            }
            catch (IOException localIOException)
            {
              //while (true)
              {
                localIOException.getStackTrace();
                Log.d("ZmView", "catch IOException hit in run", localIOException);
              }
            }
          }
        }
        finally
        {
          if (localCanvas != null)
            this.mSurfaceHolder.unlockCanvasAndPost(localCanvas);
        }
      }
    }

    public void setSurfaceSize(int paramInt1, int paramInt2)
    {
      synchronized (this.mSurfaceHolder)
      {
        LiveView.this.dispWidth = paramInt1;
        LiveView.this.dispHeight = paramInt2;
        return;
      }
    }
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.LiveView
 * JD-Core Version:    0.6.2
 */
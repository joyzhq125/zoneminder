package com.html5clouds.zmview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import java.io.IOException;
import java.io.PrintStream;

public class LiveViewImg extends SurfaceView
  implements SurfaceHolder.Callback
{
  static final int DRAG = 1;
  private static final int INVALID_POINTER_ID = -1;
  static final int NONE = 0;
  public static final int POSITION_LOWER_LEFT = 12;
  public static final int POSITION_LOWER_RIGHT = 6;
  public static final int POSITION_UPPER_LEFT = 9;
  public static final int POSITION_UPPER_RIGHT = 3;
  public static final int SIZE_BEST_FIT = 4;
  public static final int SIZE_FULLSCREEN = 8;
  public static final int SIZE_STANDARD = 1;
  private static final String TAG = "ZmView";
  static final int ZOOM = 2;
  private float a;
  private Bitmap bm;
  private int bmH;
  private int bmW;
  private int bmX;
  private int bmY;
  private Bitmap bm_load;
  private boolean debug = false;
  private int dispHeight;
  private int dispWidth;
  private int displayMode;
  private Boolean display_load_img = Boolean.valueOf(false);
  private Boolean get_img = Boolean.valueOf(false);
  private long lastPressTime;
  private Boolean load_img = Boolean.valueOf(true);
  private int mActivePointerId;
  private boolean mHasDoubleClicked;
  private LiveStream mIn = null;
  private float mLastTouchX;
  private float mLastTouchY;
  private float mPosX = 0.0F;
  private float mPosY = 0.0F;
  private boolean mRun = false;
  private ScaleGestureDetector mScaleDetector;
  private float mScaleFactor = 1.0F;
  private Matrix matrix;
  private PointF mid;
  int mode = 0;
  private float oldDist;
  private int overlayBackgroundColor;
  private Paint overlayPaint;
  private int overlayTextColor;
  private int ovlPos;
  private Matrix savedMatrix;
  private boolean showFps = false;
  private PointF start;
  private long start_time;
  private boolean surfaceDone = false;
  private LiveViewThread thread;
  private int time = -1;

  public LiveViewImg(Context paramContext)
  {
    super(paramContext);
    init(paramContext);
  }

  public LiveViewImg(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext);
    if (this.debug)
      System.out.println("ZmViewImg : init mscale");
  }

  private void init(Context paramContext)
  {
    SurfaceHolder localSurfaceHolder = getHolder();
    localSurfaceHolder.addCallback(this);
    this.mScaleDetector = new ScaleGestureDetector(paramContext, new ScaleListener());
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
    this.start = new PointF();
    this.mid = new PointF();
    this.oldDist = 1.0F;
    this.matrix = new Matrix();
    this.savedMatrix = new Matrix();
  }

  private void midPoint(PointF paramPointF, MotionEvent paramMotionEvent)
  {
    float f1 = paramMotionEvent.getX(0) + paramMotionEvent.getX(1);
    float f2 = paramMotionEvent.getY(0) + paramMotionEvent.getY(1);
    paramPointF.set(f1 / 2.0F, f2 / 2.0F);
  }

  private float spacing(MotionEvent paramMotionEvent)
  {
    float f1 = paramMotionEvent.getX(0) - paramMotionEvent.getX(1);
    float f2 = paramMotionEvent.getY(0) - paramMotionEvent.getY(1);
    return FloatMath.sqrt(f1 * f1 + f2 * f2);
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

  public boolean getImg()
  {
    return this.get_img.booleanValue();
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
	/*
    switch (0xFF & paramMotionEvent.getAction())
    {
    
    case 3:
    case 4:
    default:
    case 0:
    case 5:
    case 1:
    case 6:
    case 2:
    }
    while (true)
    {
      //boolean bool1 = true;
      //return bool1;
      this.savedMatrix.set(this.matrix);
      this.start.set(paramMotionEvent.getX(), paramMotionEvent.getY());
      this.mode = 1;
      long l = System.currentTimeMillis();
      if (l - this.lastPressTime <= 250L)
        this.mHasDoubleClicked = true;
      while (true)
      {
        this.lastPressTime = l;
        if (!this.mHasDoubleClicked)
        {
        	//break label181;
        	if (this.debug)
	        { 
	            //System.out.println("ZmViewImg Single Click Event");
	            //continue;
	            this.oldDist = spacing(paramMotionEvent);
	            if (this.oldDist > 10.0F)
	            {
	              this.savedMatrix.set(this.matrix);
	              midPoint(this.mid, paramMotionEvent);
	              this.mode = 2;
	              continue;
	              this.mode = 0;
	              continue;
	              if (this.mode == 1)
	              {
	                this.matrix.set(this.savedMatrix);
	                float[] arrayOfFloat = new float[9];
	                this.matrix.getValues(arrayOfFloat);
	                if (this.debug)
	                  System.out.println("ZmViewImg GlobalX=" + String.valueOf(arrayOfFloat[2]));
	                if (this.debug)
	                  System.out.println("ZmViewImg GlobalY=" + String.valueOf(arrayOfFloat[5]));
	                if (this.debug)
	                  System.out.println("ZmViewImg GlobalX+=" + String.valueOf(arrayOfFloat[2] + this.dispWidth));
	                if (this.debug)
	                  System.out.println("ZmViewImg GlobalY+=" + String.valueOf(arrayOfFloat[5] + this.dispHeight));
	                this.matrix.postTranslate(paramMotionEvent.getX() - this.start.x, paramMotionEvent.getY() - this.start.y);
	              }
	              else if (this.mode == 2)
	              {
	                float f1 = spacing(paramMotionEvent);
	                if (f1 > 10.0F)
	                {
	                  this.matrix.set(this.savedMatrix);
	                  float f2 = f1 / this.oldDist;
	                  if (this.debug)
	                    System.out.println("ZmViewImg BmH=" + String.valueOf(this.bmH));
	                  if (this.debug)
	                    System.out.println("ZmViewImg BmW=" + String.valueOf(this.bmW));
	                  this.matrix.postScale(f2, f2, this.mid.x, this.mid.y);
	                }
	              }
	            }
	         }
        }
        else
        { //boolean bool2 = this.debug;
	        if (!debug)
	          break;
	        System.out.println("ZmViewImg Double Click Event");
	        return false;
	        this.mHasDoubleClicked = false;
	        new Handler()
	        {
	          public void handleMessage(Message paramAnonymousMessage)
	          {
	            if ((!LiveViewImg.this.mHasDoubleClicked) && (LiveViewImg.this.debug))
	              System.out.println("ZmViewImg Single Click Event");
	          }
	        }
	        .sendMessageDelayed(new Message(), 250L);
        }
      }
    }
    */
    return true;
  }

  public void setBitmap(Bitmap paramBitmap)
  {
    this.bm_load = paramBitmap;
  }

  public void setDisplayMode(int paramInt)
  {
    this.matrix = new Matrix();
    this.displayMode = paramInt;
  }

  public void setMrun(Boolean paramBoolean)
  {
    this.load_img = paramBoolean;
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
    this.display_load_img = Boolean.valueOf(false);
    startPlayback();
  }

  public void setSource2(LiveStream paramLiveStream)
  {
    this.mIn = paramLiveStream;
  }

  public void setTime(int paramInt)
  {
    this.time = paramInt;
    this.start_time = System.currentTimeMillis();
  }

  public void showFps(boolean paramBoolean)
  {
    this.showFps = paramBoolean;
  }

  public void showLoading(Boolean paramBoolean)
  {
    this.display_load_img = paramBoolean;
  }

  public void startPlayback()
  {
    if ((mIn != null) && (!mRun))
    {
      mRun = true;
      if (thread.getState() != Thread.State.TERMINATED)
      {
    	  //break label60;
			while (thread.isAlive())
			      return;
			if (debug)
			  System.out.println("ZmView we start thread");
			thread.start();
      }
      else
      {
        thread = new LiveViewThread(getHolder(), getContext());
        thread.start();
      }
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
        boolean bool = this.debug;
        i = 0;
        if (bool)
        {
          System.out.println("ZmView Thread is stopped\n");
          i = 0;
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        localInterruptedException.getStackTrace();
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
    private int k;
    private SurfaceHolder mSurfaceHolder;
    private Bitmap ovl;
    private long start;

    public LiveViewThread(SurfaceHolder paramContext, Context arg3)
    {
      this.mSurfaceHolder = paramContext;
    }

    private Rect destRect(int paramInt1, int paramInt2)
    {
      if (LiveViewImg.this.displayMode == 1)
      {
        int n = LiveViewImg.this.dispWidth / 2 - paramInt1 / 2;
        LiveViewImg.this.bmX = n;
        LiveViewImg.this.bmY = 0;
        LiveViewImg.this.bmW = paramInt1;
        LiveViewImg.this.bmH = paramInt2;
        return new Rect(n, 0, paramInt1 + n, paramInt2 + 0);
      }
      if (LiveViewImg.this.displayMode == 4)
      {
        float f = paramInt1 / paramInt2;
        int i = LiveViewImg.this.dispWidth;
        int j = (int)(LiveViewImg.this.dispWidth / f);
        if (j > LiveViewImg.this.dispHeight)
        {
          j = LiveViewImg.this.dispHeight;
          i = (int)(f * LiveViewImg.this.dispHeight);
        }
        int m = LiveViewImg.this.dispWidth / 2 - i / 2;
        LiveViewImg.this.bmX = m;
        LiveViewImg.this.bmY = 0;
        LiveViewImg.this.bmW = i;
        LiveViewImg.this.bmH = j;
        return new Rect(m, 0, i + m, j + 0);
      }
      if (LiveViewImg.this.displayMode == 8)
      {
        LiveViewImg.this.bmX = 0;
        LiveViewImg.this.bmY = 0;
        LiveViewImg.this.bmW = LiveViewImg.this.dispWidth;
        LiveViewImg.this.bmH = LiveViewImg.this.dispHeight;
        return new Rect(0, 0, LiveViewImg.this.dispWidth, LiveViewImg.this.dispHeight);
      }
      return null;
    }

    private Rect destRect2(int paramInt1, int paramInt2)
    {
      if (LiveViewImg.this.displayMode == 1)
      {
        int i1 = LiveViewImg.this.dispWidth / 2 - paramInt1 / 2;
        int i2 = LiveViewImg.this.dispHeight / 2 - paramInt2 / 2;
        LiveViewImg.this.bmX = i1;
        LiveViewImg.this.bmY = i2;
        LiveViewImg.this.bmW = paramInt1;
        LiveViewImg.this.bmH = paramInt2;
        return new Rect(i1, i2, paramInt1 + i1, paramInt2 + i2);
      }
      if (LiveViewImg.this.displayMode == 4)
      {
        float f = paramInt1 / paramInt2;
        int i = LiveViewImg.this.dispWidth;
        int j = (int)(LiveViewImg.this.dispWidth / f);
        if (j > LiveViewImg.this.dispHeight)
        {
          j = LiveViewImg.this.dispHeight;
          i = (int)(f * LiveViewImg.this.dispHeight);
        }
        int m = LiveViewImg.this.dispWidth / 2 - i / 2;
        int n = LiveViewImg.this.dispHeight / 2 - j / 2;
        LiveViewImg.this.bmX = m;
        LiveViewImg.this.bmY = n;
        LiveViewImg.this.bmW = i;
        LiveViewImg.this.bmH = j;
        return new Rect(m, n, i + m, j + n);
      }
      if (LiveViewImg.this.displayMode == 8)
      {
        LiveViewImg.this.bmX = 0;
        LiveViewImg.this.bmY = 0;
        LiveViewImg.this.bmW = LiveViewImg.this.dispWidth;
        LiveViewImg.this.bmH = LiveViewImg.this.dispHeight;
        return new Rect(0, 0, LiveViewImg.this.dispWidth, LiveViewImg.this.dispHeight);
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
      paramPaint.setColor(LiveViewImg.this.overlayBackgroundColor);
      localCanvas.drawRect(0.0F, 0.0F, i, j, paramPaint);
      paramPaint.setColor(LiveViewImg.this.overlayTextColor);
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
        if (!LiveViewImg.this.mRun)
          return;
        if (!LiveViewImg.this.surfaceDone)
          continue;
        try
        {
          localCanvas = this.mSurfaceHolder.lockCanvas();
          if (LiveViewImg.this.bm != null)
          {
            Rect localRect2 = destRect(LiveViewImg.this.bm.getWidth(), LiveViewImg.this.bm.getHeight());
            //localCanvas.drawColor(-16777216);
            localCanvas.setMatrix(LiveViewImg.this.matrix);
            localCanvas.drawBitmap(LiveViewImg.this.bm, null, localRect2, localPaint);
          }
          synchronized (this.mSurfaceHolder)
          {
            try
            {
              Rect localRect1;
              int m;
              //label305: int n;
              int n;
              int j;
              //label327: int j;
              String str;
              if (LiveViewImg.this.load_img.booleanValue())
              {
                this.k = (1 + this.k);
                if (LiveViewImg.this.bm != null)
                {
                  LiveViewImg.this.bm.recycle();
                  LiveViewImg.this.bm = null;
                }
                LiveViewImg.this.bm = LiveViewImg.this.mIn.readLiveFrame();
                if (LiveViewImg.this.bm != null)
	            {
	                localRect1 = destRect(LiveViewImg.this.bm.getWidth(), LiveViewImg.this.bm.getHeight());
	                //localCanvas.drawColor(-16777216);
	                localCanvas.setMatrix(LiveViewImg.this.matrix);
	                localCanvas.drawBitmap(LiveViewImg.this.bm, null, localRect1, localPaint);
	                if (!LiveViewImg.this.showFps)
	                {
	                	//break label652;
	                	this.k = 0;
	                    LiveViewImg.this.get_img = Boolean.valueOf(false);
	                }
	                else
	                {
	                	localPaint.setXfermode(localPorterDuffXfermode);
		                if (this.ovl != null)
		                {
		                  if ((0x1 & LiveViewImg.this.ovlPos) != 1)
		                  {
		                	  //break label562;
		                	  m = localRect1.bottom - this.ovl.getHeight();
		                  }
		                  else
		                  {
			                  m = localRect1.top;
			                  if ((0x8 & LiveViewImg.this.ovlPos) != 8)
			                  {
			                	  //break label580;
			                	  n = localRect1.right - this.ovl.getWidth();
			                  }
			                  n = localRect1.left;
			                  localCanvas.drawBitmap(this.ovl, n, m, null);
		                  }
		                }
		                localPaint.setXfermode(null);
		                this.frameCounter = (1 + this.frameCounter);
		                int i = LiveViewImg.this.time;
		                j = 0;
		                if (i != -1)
		                  j = (int)(LiveViewImg.this.time - (System.currentTimeMillis() - LiveViewImg.this.start_time) / 1000L);
		                if (System.currentTimeMillis() - this.start >= 1000L)
		                {
		                  str = String.valueOf(this.frameCounter) + " fps";
		                  if (LiveViewImg.this.time != -1)
		                  {
		                    if (j > 0)
		                    {
		                    	//break label598;
		                    	str = "Time: " + String.valueOf(j) + "s" + "  " + String.valueOf(this.frameCounter) + "fps";
		                    }
		                    else
		                    	str = "Time: 0s  0fps";
		                  }
		                  /*label467: */this.frameCounter = 0;
		                  this.start = System.currentTimeMillis();
		                  this.ovl = makeFpsOverlay(LiveViewImg.this.overlayPaint, str);
		                }
		                if (this.k > 2)
		                  LiveViewImg.this.get_img = Boolean.valueOf(true);
		                if ((LiveViewImg.this.time != -1) && (j <= 0))
		                  LiveViewImg.this.load_img = Boolean.valueOf(false);
	                }
	              }
              /*while*/ //if(localCanvas != null)
              //{
                //this.mSurfaceHolder.unlockCanvasAndPost(localCanvas);
                /*
                break;
                label562: m = localRect1.bottom - this.ovl.getHeight();
                break label305;
                label580: n = localRect1.right - this.ovl.getWidth();
                break label327;
                label598: str = "Time: " + String.valueOf(j) + "s" + "  " + String.valueOf(this.frameCounter) + "fps";
                break label467;
                label652: this.k = 0;
                LiveViewImg.this.get_img = Boolean.valueOf(false);
                */
              //}
              }
            }
            catch (IOException localIOException)
            {
              //while (true)
                localIOException.getStackTrace();
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
        LiveViewImg.this.dispWidth = paramInt1;
        LiveViewImg.this.dispHeight = paramInt2;
        return;
      }
    }
  }

  private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
  {
    private ScaleListener()
    {
    }

    public boolean onScale(ScaleGestureDetector paramScaleGestureDetector)
    {
      LiveViewImg localLiveViewImg = LiveViewImg.this;
      localLiveViewImg.mScaleFactor *= paramScaleGestureDetector.getScaleFactor();
      if (LiveViewImg.this.debug)
        System.out.println("ZmView mScaleFactor " + LiveViewImg.this.mScaleFactor);
      LiveViewImg.this.mScaleFactor = Math.max(0.05F, LiveViewImg.this.mScaleFactor);
      LiveViewImg.this.invalidate();
      return true;
    }
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.LiveViewImg
 * JD-Core Version:    0.6.2
 */
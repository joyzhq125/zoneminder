package com.html5clouds.zmview;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
//import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class QuickAction extends PopupWindows
  implements PopupWindow.OnDismissListener
{
  public static final int ANIM_AUTO = 5;
  public static final int ANIM_GROW_FROM_CENTER = 3;
  public static final int ANIM_GROW_FROM_LEFT = 1;
  public static final int ANIM_GROW_FROM_RIGHT = 2;
  public static final int ANIM_REFLECT = 4;
  public static final int HORIZONTAL = 0;
  public static final int VERTICAL = 1;
  private List<ActionItem> actionItems = new ArrayList();
  private int mAnimStyle;
  private ImageView mArrowDown;
  private ImageView mArrowUp;
  private int mChildPos;
  private boolean mDidAction;
  private OnDismissListener mDismissListener;
  private LayoutInflater mInflater;
  private int mInsertPos;
  private OnActionItemClickListener mItemClickListener;
  private int mOrientation;
  private View mRootView;
  private ScrollView mScroller;
  private ViewGroup mTrack;
  private int rootWidth = 0;

  public QuickAction(Context paramContext)
  {
    this(paramContext, 1);
  }
  
  /** 
  * Constructor allowing orientation override 
  *  
  * @param context    Context 
  * @param orientation Layout orientation, can be vartical or horizontal 
  */  

  public QuickAction(Context context, int orientation)
  {
	/*  
    super(paramContext);
    mOrientation = paramInt;
    mInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
    if (mOrientation == 0)
      setRootViewId(R.layout.popup_horizontal);
    //while (true)
    else
    {
      setRootViewId(R.layout.popup_vertical);
    }
    mAnimStyle = 5;
    mChildPos = 0;
    */
    super(context);    
    mOrientation = orientation;      
    mInflater    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  

    if (mOrientation == HORIZONTAL) {  
        setRootViewId(R.layout.popup_horizontal);  
    } else {  
        setRootViewId(R.layout.popup_vertical);  
    }  
    mAnimStyle  = ANIM_AUTO;  
    mChildPos   = 0; 

  }
  /** 
   * Set animation style 
   *  
   * @param screenWidth screen width 
   * @param requestedX distance from left edge 
   * @param onTop flag to indicate where the popup should be displayed. Set TRUE if displayed on top of anchor view 
    *        and vice versa 
   */ 
  private void setAnimationStyle(int screenWidth, int requestedX, boolean onTop) {  
      int arrowPos = requestedX - mArrowUp.getMeasuredWidth()/2;    
      switch (mAnimStyle) {  
      case ANIM_GROW_FROM_LEFT:  
          mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);  
          break;  
                    
      case ANIM_GROW_FROM_RIGHT:  
          mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);  
          break;  
                    
      case ANIM_GROW_FROM_CENTER:  
          mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);  
      break;  
            
      case ANIM_REFLECT:  
          mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Reflect : R.style.Animations_PopDownMenu_Reflect);  
      break;  
        
      case ANIM_AUTO:  
         if (arrowPos <= screenWidth/4) {  
              mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);  
          } else if (arrowPos > screenWidth/4 && arrowPos < 3 * (screenWidth/4)) {  
             mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);  
          } else {  
              mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);  
          }  
                    
          break;  
      }  
  }  
  
   /*
  private void setAnimationStyle(int paramInt1, int paramInt2, boolean paramBoolean)
  {
	 
    int i = R.style.Animations_PopUpMenu_Left;
    int j = R.style.Animations_PopUpMenu_Center;
    int k = R.style.Animations_PopDownMenu_Right;
    int m = paramInt2 - mArrowUp.getMeasuredWidth() / 2;
    switch (mAnimStyle)
    {
    default:
      return;
    case 1:
      PopupWindow localPopupWindow7 = mWindow;
      if (!paramBoolean);
      //if (true)
      {
        i = R.style.Animations_PopDownMenu_Left;
      }
      localPopupWindow7.setAnimationStyle(i);
      return;
    case 2:
      PopupWindow localPopupWindow6 = mWindow;
      if (paramBoolean);
      for (int i2 = R.style.Animations_PopUpMenu_Right; ; i2 = k)
      {
        localPopupWindow6.setAnimationStyle(i2);
        return;
      }
    case 3:
      PopupWindow localPopupWindow5 = mWindow;
      if (paramBoolean);
      for (int i1 = j; ; i1 = R.style.Animations_PopDownMenu_Center)
      {
        localPopupWindow5.setAnimationStyle(i1);
        return;
      }
    case 4:
      PopupWindow localPopupWindow4 = mWindow;
      if (paramBoolean);
      for (int n = R.style.Animations_PopUpMenu_Reflect; ; n = R.style.Animations_PopDownMenu_Reflect)
      {
        localPopupWindow4.setAnimationStyle(n);
        return;
      }
    case 5:
    }
    if (m <= paramInt1 / 4)
    {
      PopupWindow localPopupWindow3 = mWindow;
      if (!paramBoolean);
      //while (true)
      {
        i = R.style.Animations_PopDownMenu_Left;
      }
      localPopupWindow3.setAnimationStyle(i);
      return;
    }
    if ((m > paramInt1 / 4) && (m < 3 * (paramInt1 / 4)))
    {
      PopupWindow localPopupWindow2 = mWindow;
      if (!paramBoolean);
      //while (true)
      {
        j = R.style.Animations_PopDownMenu_Center;
      }
      localPopupWindow2.setAnimationStyle(j);
      return;
    }
    PopupWindow localPopupWindow1 = mWindow;
    if (paramBoolean)
      k = R.style.Animations_PopUpMenu_Right;
    localPopupWindow1.setAnimationStyle(k);
    
  }
  */
  private void showArrow(int whichArrow, int requestedX)
  {
/*
    ImageView localImageView1;
    int i = mArrowUp.getMeasuredWidth();
    if (paramInt1 == R.id.arrow_up)
    {
      localImageView1 = mArrowUp;
      //if (paramInt1 != 2131099728)
      //  break label68;
    }
    else
    {
	    //label68: 
		localImageView1 = mArrowDown;
		//break;
	    //for (ImageView localImageView2 = this.mArrowDown; ; localImageView2 = this.mArrowUp)
    }
	ImageView localImageView2 = mArrowDown;
  
	localImageView1.setVisibility(0);
	((ViewGroup.MarginLayoutParams)localImageView1.getLayoutParams()).leftMargin = (paramInt2 - i / 2);
	localImageView2.setVisibility(4);

	//localImageView2 = mArrowUp;
	 */
	final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp : mArrowDown;  
	final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown : mArrowUp;  
		
	final int arrowWidth = mArrowUp.getMeasuredWidth();  
		
	showArrow.setVisibility(View.VISIBLE);  
		    
	ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams)showArrow.getLayoutParams();  
		   
	param.leftMargin = requestedX - arrowWidth  / 2;  
		    
	hideArrow.setVisibility(View.INVISIBLE);  	 
  }

  /** 
  * Add action item 
  *  
  * @param action  {@link PopuItem} 
  */ 

  public void addActionItem(ActionItem action)
  {
/*	  
	actionItems.add(paramActionItem);
	String str = paramActionItem.getTitle();
	Drawable localDrawable = paramActionItem.getIcon();
	View localView1;
	ImageView localImageView;
	TextView localTextView;
	if (mOrientation == 0)
	{
		localView1 = mInflater.inflate(R.layout.action_item_horizontal, null);
	}
	else
	{
		localView1 = mInflater.inflate(R.layout.action_item_vertical, null);
	}
	localImageView = (ImageView)localView1.findViewById(R.id.iv_icon);
	localTextView = (TextView)localView1.findViewById(R.id.tv_title);
	if (localDrawable == null)
	{
		localImageView.setVisibility(View.GONE);
	}
	localImageView.setImageDrawable(localDrawable);     
	if (str == null)
	{
		localTextView.setVisibility(View.8);
		//localTextView.setVisibility(View.GONE);
	}
	
	localTextView.setText(str);	
	
    final int pos       =  mChildPos;  
    final int actionId  = paramActionItem.getActionId();

	localView1.setOnClickListener(new View.OnClickListener()
	{
		@Override  
		public void onClick(View paramAnonymousView)
		{
		  
		  if (mItemClickListener != null)
		  {
			  mItemClickListener.onItemClick(QuickAction.this, pos, actionId);
		  }
		  if (!getActionItem(pos).isSticky())
		  {
		    mDidAction = true;
		    dismiss();
		  }
		  
		}
	});
	localView1.setFocusable(true);
	localView1.setClickable(true);
	if ((mOrientation == 0) && (mChildPos != 0))
	{
		View localView2 = mInflater.inflate(R.layout.horiz_separator, null);
		localView2.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.-2,  LayoutParams.FILL_PARENT));
		localView2.setPadding(5, 0, 5, 0);
		mTrack.addView(localView2, mInsertPos);
		mInsertPos = (1 + mInsertPos);
	}
	mTrack.addView(localView1, mInsertPos);
	mChildPos = (1 + mChildPos);
	mInsertPos = (1 + mInsertPos);
*/
	  actionItems.add(action);  
      
      String title    = action.getTitle();  
      Drawable icon   = action.getIcon();  
        
      View container;  
        
      if (mOrientation == HORIZONTAL) {  
          container = mInflater.inflate(R.layout.action_item_horizontal, null);  
      } else {  
          container = mInflater.inflate(R.layout.action_item_vertical, null);  
      }  
        
      ImageView img   = (ImageView) container.findViewById(R.id.iv_icon);  
      TextView text   = (TextView) container.findViewById(R.id.tv_title);  
        
      if (icon != null) {  
          img.setImageDrawable(icon);  
      } else {  
          img.setVisibility(View.GONE);  
      }  
        
      if (title != null) {  
          text.setText(title);  
      } else {  
          text.setVisibility(View.GONE);  
      }  
        
      final int pos       =  mChildPos;  
      final int actionId  = action.getActionId();  
        
      container.setOnClickListener(new OnClickListener() {  
          @Override  
          public void onClick(View v) {  
              if (mItemClickListener != null) {  
                  mItemClickListener.onItemClick(QuickAction.this, pos, actionId);  
              }  
                
              if (!getActionItem(pos).isSticky()) {    
                  mDidAction = true;  
                    
                  dismiss();  
              }  
          }  
      });  
        
      container.setFocusable(true);  
      container.setClickable(true);  
             
      if (mOrientation == HORIZONTAL && mChildPos != 0) {  
          View separator = mInflater.inflate(R.layout.horiz_separator, null);  
            
          RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);  
            
          separator.setLayoutParams(params);  
          separator.setPadding(5, 0, 5, 0);  
            
          mTrack.addView(separator, mInsertPos);  
            
          mInsertPos++;  
      }  
        
      mTrack.addView(container, mInsertPos);  
        
      mChildPos++;  
      mInsertPos++;  
	  
  }
  /** 
63.     * Get action item at an index 
64.     *  
65.     * @param index  Index of item (position from callback) 
66.     *  
67.     * @return  Action Item at the position 
68.     */

  public ActionItem getActionItem(int index)
  {
    return (ActionItem)actionItems.get(index);
  }

  public void onDismiss()
  {
    if ((!mDidAction) && (mDismissListener != null))
      mDismissListener.onDismiss();
  }

  public void setAnimStyle(int paramInt)
  {
    mAnimStyle = paramInt;
  }

  public void setOnActionItemClickListener(OnActionItemClickListener paramOnActionItemClickListener)
  {
    mItemClickListener = paramOnActionItemClickListener;
  }

  public void setOnDismissListener(OnDismissListener paramOnDismissListener)
  {
    setOnDismissListener(this);
    mDismissListener = paramOnDismissListener;
  }

  public void setRootViewId(int paramInt)
  {
    mRootView = ((ViewGroup)mInflater.inflate(paramInt, null));
    mTrack = ((ViewGroup)mRootView.findViewById(R.id.tracks));
    mArrowDown = ((ImageView)mRootView.findViewById(R.id.arrow_down));
    mArrowUp = ((ImageView)mRootView.findViewById(R.id.arrow_up));
    mScroller = ((ScrollView)mRootView.findViewById(R.id.scroller));
    mRootView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)  );
    setContentView(mRootView);
  }

 public void show(View paramView)
 {	
		/*
		int[] arrayOfInt = new int[2];
	    int i = mRootView.getMeasuredHeight();
	    int j = this.mWindowManager.getDefaultDisplay().getWidth();
	    int k = this.mWindowManager.getDefaultDisplay().getHeight();
	    int m = 0;
	    int n = 0;
	    int i2 = 0;
	    boolean bool  = true;
	    //label186: 
	    int i3 = 0;
	    preShow();
	    mDidAction = false;
	
	    paramView.getLocationOnScreen(arrayOfInt);
	    Rect localRect = new Rect(arrayOfInt[0], arrayOfInt[1], arrayOfInt[0] + paramView.getWidth(), arrayOfInt[1] + paramView.getHeight());
	    mRootView.measure(-2, -2);
	    if (rootWidth == 0)
	    {
	    	rootWidth = mRootView.getMeasuredWidth();
	    }
	
	    if (localRect.left + rootWidth > j)
	    {
	      m = localRect.left - (rootWidth - paramView.getWidth());
	      if (m < 0)
	        m = 0;
	      n = localRect.centerX() - m;
	      int i1 = localRect.top;
	      i2 = k - localRect.bottom;
	      //if (i1 <= i2)
	      //  break label307;
	      bool = true;
	      //if (!bool)
	      //  break label325;
	      //if (i <= i1)
	      //  break label313;
	      i3 = 15;
	      this.mScroller.getLayoutParams().height = (i1 - paramView.getHeight());
	      //label219: if (!bool)
	      //  break label353;
	    }
	    //label307: label313: label325: label353: 
	    int i4 =  R.id.arrow_up; //R.id.arrow_down;
	    //for (int i4 = R.id.arrow_down; ; i4 = R.id.arrow_up)
	    if (paramView.getWidth() > rootWidth)
	    {
	      //showArrow(i4, n);
	      //setAnimationStyle(j, localRect.centerX(), bool);
	      //mWindow.showAtLocation(paramView, 0, m, i3);
	      //return;
	      //if (paramView.getWidth() > this.rootWidth);
	      //for (m = localRect.centerX() - this.rootWidth / 2; ; m = localRect.left)
	      //{
	      //  n = localRect.centerX() - m;
	      //  break;
	      //}
	      m = localRect.centerX() - rootWidth / 2;
	      n = localRect.centerX() - m;
	      //m = localRect.left;
	      bool = false;
	      //break label186;
	      i3 = localRect.top - i;
	      //break label219;
	      //i3 = localRect.bottom;
	      //if (i <= i2)
	      //  break label219;
	      i2 = k - localRect.bottom;
	      mScroller.getLayoutParams().height = i2; //??
	      //break label219;
	    }
	    i4 = R.id.arrow_up;
	    showArrow(i4, n);
	    setAnimationStyle(j, localRect.centerX(), bool);
	    mWindow.showAtLocation(paramView, 0, m, i3);
	    */
		  
	preShow();          
	int xPos, yPos, arrowPos;          
	mDidAction          = false;            
	int[] location      = new int[2];  
	paramView.getLocationOnScreen(location);  
	Rect anchorRect     = new Rect(location[0], location[1], location[0] + paramView.getWidth(), location[1] + paramView.getHeight());        
	mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
	  
	int rootHeight      = mRootView.getMeasuredHeight();  
	if (rootWidth == 0) 
	{  
		rootWidth       = mRootView.getMeasuredWidth();  
	}      
	int screenWidth     = mWindowManager.getDefaultDisplay().getWidth();  
	int screenHeight    = mWindowManager.getDefaultDisplay().getHeight();  
	      
	//automatically get X coord of popup (top left)  
	if ((anchorRect.left + rootWidth) > screenWidth) 
	{  
	   xPos        = anchorRect.left - (rootWidth-paramView.getWidth());            
	   xPos        = (xPos < 0) ? 0 : xPos;       
	   arrowPos    = anchorRect.centerX()-xPos;                
	} 
	else 
	{  
		if (paramView.getWidth() > rootWidth) 
		{  
		    xPos = anchorRect.centerX() - (rootWidth/2);  
		} 
		else 
		{  
		    xPos = anchorRect.left;  
		}                
		arrowPos = anchorRect.centerX()-xPos;  
	}  
	          
	int dyTop           = anchorRect.top;  
	int dyBottom        = screenHeight - anchorRect.bottom;  
	  
	boolean onTop       = (dyTop > dyBottom) ? true : false;  
	  
	if (onTop) 
	{  
		if (rootHeight > dyTop) 
		{  
	        yPos            = 15;  
	        LayoutParams l  = mScroller.getLayoutParams();  
	        l.height        = dyTop - paramView.getHeight();  
	    } 
		else 
		{  
	         yPos = anchorRect.top - rootHeight;  
	            
		}  
	        
	} 
	else 
	{  
	    yPos = anchorRect.bottom;  
	      
	    if (rootHeight > dyBottom) 
	    {   
	        LayoutParams l  = mScroller.getLayoutParams();  
	        l.height        = dyBottom;  
	    }  
	}  
	          
	showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);  
	  
	setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);  
	  
	mWindow.showAtLocation(paramView, Gravity.NO_GRAVITY, xPos, yPos);  
	   
}

  public static abstract interface OnActionItemClickListener
  {
    public abstract void onItemClick(QuickAction paramQuickAction, int paramInt1, int paramInt2);
  }

  public static abstract interface OnDismissListener
  {
    public abstract void onDismiss();
  }
}

/* Location:           E:\android 反\jd-gui\classes_dex2jar.jar
 * Qualified Name:     com.html5clouds.zmview.QuickAction
 * JD-Core Version:    0.6.2
 */
// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PopupWindow.java

package android.widget;

import android.R;
import android.content.Context;
import android.content.Resources;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.*;

// Referenced classes of package android.widget:
//            FrameLayout, ScrollView

public class PopupWindow
{

    public PopupWindow(Context context)
    {
        this(context, ((AttributeSet) (null)));
    }

    public PopupWindow(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0x10100d7);
    }

    public PopupWindow(Context context, AttributeSet attrs, int defStyle)
    {
        mDrawingLocation = new int[2];
        mContext = context;
        android.content.Resources.StyledAttributes a = context.obtainStyledAttributes(attrs, android.R.styleable.PopupWindow, defStyle, 0);
        mBackground = a.getDrawable(0);
        a.recycle();
    }

    public PopupWindow()
    {
        this(((View) (null)), 0, 0);
    }

    public PopupWindow(View contentView)
    {
        this(contentView, 0, 0);
    }

    public PopupWindow(int width, int height)
    {
        this(((View) (null)), width, height);
    }

    public PopupWindow(View contentView, int width, int height)
    {
        this(contentView, width, height, false);
    }

    public PopupWindow(View contentView, int width, int height, boolean focusable)
    {
        mDrawingLocation = new int[2];
        setContentView(contentView);
        setWidth(width);
        setHeight(height);
        setFocusable(focusable);
    }

    public Drawable getBackground()
    {
        return mBackground;
    }

    public void setBackground(Drawable background)
    {
        mBackground = background;
    }

    public View getContentView()
    {
        return mContentView;
    }

    public void setContentView(View contentView)
    {
        if(isShowing())
        {
            return;
        } else
        {
            mContentView = contentView;
            return;
        }
    }

    public boolean isFocusable()
    {
        return mFocusable;
    }

    public void setFocusable(boolean focusable)
    {
        mFocusable = focusable;
    }

    public int getHeight()
    {
        return mHeight;
    }

    public void setHeight(int height)
    {
        mHeight = Math.max(0, height);
    }

    public int getWidth()
    {
        return mWidth;
    }

    public void setWidth(int width)
    {
        mWidth = Math.max(0, width);
    }

    public boolean isShowing()
    {
        return mIsShowing;
    }

    public void show(View parent, int x, int y)
    {
        if(isShowing() || mContentView == null)
        {
            return;
        } else
        {
            mIsShowing = true;
            android.view.WindowManager.LayoutParams p = createPopupLayout(parent.getWindowToken());
            p.x = x;
            p.y = y;
            preparePopup(p);
            invokePopup(p);
            return;
        }
    }

    public void show(View anchor)
    {
        if(isShowing() || mContentView == null)
        {
            return;
        } else
        {
            mIsShowing = true;
            android.view.WindowManager.LayoutParams p = createPopupLayout(anchor.getWindowToken());
            preparePopup(p);
            boolean onTop = findDropDownPosition(anchor, p);
            p.animation = onTop ? 0x102002d : 0x102002c;
            invokePopup(p);
            return;
        }
    }

    private void preparePopup(android.view.WindowManager.LayoutParams p)
    {
        if(mBackground != null)
        {
            FrameLayout popupViewContainer = new FrameLayout(mContext);
            android.view.ViewGroup.LayoutParams listParams = new android.view.ViewGroup.LayoutParams(-1, -1);
            popupViewContainer.setBackground(mBackground);
            popupViewContainer.addView(mContentView, listParams);
            p.height += popupViewContainer.getPaddingTop() + popupViewContainer.getPaddingBottom();
            mPopupView = popupViewContainer;
        } else
        {
            mPopupView = mContentView;
        }
    }

    private void invokePopup(android.view.WindowManager.LayoutParams p)
    {
        WindowManagerImpl wm = WindowManagerImpl.getDefault();
        wm.addView(mPopupView, p);
    }

    private android.view.WindowManager.LayoutParams createPopupLayout(IBinder token)
    {
        android.view.WindowManager.LayoutParams p = new android.view.WindowManager.LayoutParams();
        p.gravity = 51;
        p.width = mWidth;
        p.height = mHeight;
        p.format = -3;
        p.flags = 16;
        if(!mFocusable)
            p.flags |= 8;
        p.type = 1000;
        p.token = token;
        p.animation = 0x102002c;
        return p;
    }

    private boolean findDropDownPosition(View anchor, android.view.WindowManager.LayoutParams p)
    {
        anchor.getLocationOnScreen(mDrawingLocation);
        p.x = mDrawingLocation[0];
        p.y = mDrawingLocation[1] + anchor.getMeasuredHeight();
        boolean onTop = false;
        View root = anchor.getRootView();
        int delta = (p.y + p.height) - root.getWindowTop() - root.getHeight();
        if(delta > 0)
        {
            if(p.y != anchor.getWindowBottom())
            {
                ScrollView scrollView = (ScrollView)anchor.findParentViewOfType(android/widget/ScrollView);
                if(scrollView != null)
                {
                    int bottom = anchor.getWindowBottom() + p.height;
                    if(bottom > scrollView.getChildAt(scrollView.getChildCount() - 1).getWindowBottom())
                        onTop = true;
                    else
                    if(bottom > scrollView.getWindowBottom())
                    {
                        boolean enabled = scrollView.isVerticalScrollBarEnabled();
                        if(enabled)
                            scrollView.setVerticalScrollBarEnabled(false);
                        scrollView.smoothScrollBy(0, delta);
                        if(enabled)
                            scrollView.setVerticalScrollBarEnabled(enabled);
                        p.y -= delta;
                    } else
                    {
                        onTop = true;
                    }
                } else
                {
                    onTop = true;
                }
            } else
            {
                onTop = true;
            }
            if(onTop)
                p.y -= anchor.getMeasuredHeight() + p.height;
        }
        return onTop;
    }

    public void dismiss()
    {
        if(isShowing() && mPopupView != null)
        {
            WindowManagerImpl wm = WindowManagerImpl.getDefault();
            wm.removeView(mPopupView);
            if(mPopupView != mContentView && (mPopupView instanceof ViewGroup))
                ((ViewGroup)mPopupView).removeView(mContentView);
            mIsShowing = false;
        }
    }

    public void update(int x, int y, int width, int height)
    {
        if(width != -1)
            setWidth(width);
        if(height != -1)
            setHeight(height);
        if(!isShowing() || mContentView == null)
            return;
        android.view.WindowManager.LayoutParams p = (android.view.WindowManager.LayoutParams)mContentView.getLayoutParams();
        boolean update = false;
        if(width != -1 && p.width != width)
        {
            p.width = width;
            update = true;
        }
        if(height != -1 && p.height != height)
        {
            p.height = height;
            update = true;
        }
        if(p.x != x)
        {
            p.x = x;
            update = true;
        }
        if(p.y != y)
        {
            p.y = y;
            update = true;
        }
        if(update)
        {
            WindowManagerImpl wm = WindowManagerImpl.getDefault();
            wm.updateViewLayout(mContentView, p);
        }
    }

    public void update(View anchor, int width, int height)
    {
        if(!isShowing() || mContentView == null)
        {
            return;
        } else
        {
            android.view.WindowManager.LayoutParams p = (android.view.WindowManager.LayoutParams)mContentView.getLayoutParams();
            int x = p.x;
            int y = p.y;
            findDropDownPosition(anchor, p);
            update(x, y, width, height);
            return;
        }
    }

    private boolean mIsShowing;
    private View mContentView;
    private View mPopupView;
    private boolean mFocusable;
    private int mWidth;
    private int mHeight;
    private int mDrawingLocation[];
    private Context mContext;
    private Drawable mBackground;
}

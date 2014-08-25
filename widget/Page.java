// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Page.java

package android.widget;

import android.R;
import android.content.Context;
import android.content.Resources;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import java.util.Map;

// Referenced classes of package android.widget:
//            RelativeLayout, PageTurner

public class Page extends RelativeLayout
{
    public static abstract class Callback
    {

        public void onDrawBackPage(Canvas canvas1)
        {
        }

        public void onDrawBackground(Canvas canvas1)
        {
        }

        public void onPageTurnFinished(Canvas canvas1)
        {
        }

        public Callback()
        {
        }
    }


    public Page(Context context)
    {
        super(context);
    }

    public Page(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
        android.content.Resources.StyledAttributes a = context.obtainStyledAttributes(attrs, android.R.styleable.PageTurner);
        mBackPage = a.getDrawable(1);
        mPageBackground = a.getDrawable(0);
        mCorner = a.getInt(2, -1);
    }

    void setPageTurner(PageTurner pageTurner)
    {
        mPageTurner = pageTurner;
    }

    void setClipPath(Path clipPath)
    {
        mClipPath = clipPath;
    }

    public void setCallback(Callback callback)
    {
        mCallback = callback;
    }

    void drawBackPage(Canvas canvas)
    {
        if(mCallback != null)
            mCallback.onDrawBackPage(canvas);
    }

    void drawBackground(Canvas canvas)
    {
        if(mCallback != null)
            mCallback.onDrawBackground(canvas);
    }

    public void startPageTurn()
    {
        if(mPageTurner != null)
            mPageTurner.startPageTurn();
    }

    void onPageTurnFinished(Canvas canvas)
    {
        mCallback.onPageTurnFinished(canvas);
        mClipPath = null;
    }

    protected void dispatchDraw(Canvas canvas)
    {
        if(mClipPath != null)
        {
            canvas.save();
            canvas.clipPath(mClipPath, android.graphics.Canvas.ClipMode.INTERSECT);
        }
        super.dispatchDraw(canvas);
        if(mClipPath != null)
            canvas.restore();
    }

    public void setCorner(int corner)
    {
        mCorner = corner;
    }

    public int getCorner()
    {
        return mCorner;
    }

    public void setBackPage(Drawable backPage)
    {
        mBackPage = backPage;
    }

    public Drawable getBackPage()
    {
        return mBackPage;
    }

    public void setPageBackground(Drawable background)
    {
        mPageBackground = background;
    }

    public Drawable getPageBackground()
    {
        return mPageBackground;
    }

    public static final int CORNER_BOTTOM_LEFT = 0;
    public static final int CORNER_BOTTOM_RIGHT = 1;
    public static final int CORNER_TOP_LEFT = 2;
    public static final int CORNER_TOP_RIGHT = 3;
    private Path mClipPath;
    private PageTurner mPageTurner;
    private Callback mCallback;
    private int mCorner;
    private Drawable mBackPage;
    private Drawable mPageBackground;
}

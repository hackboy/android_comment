// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PageTurner.java

package android.widget;

import android.R;
import android.content.Context;
import android.content.Resources;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.util.AttributeSet;
import android.view.View;
import java.util.Map;

// Referenced classes of package android.widget:
//            RelativeLayout, Page

public class PageTurner extends RelativeLayout
{

    public PageTurner(Context context)
    {
        super(context);
        mChildRect = new Rect();
        mPageTurnCorner = new PointF();
        mOppositeCorner = new PointF();
        mPageDim = new PointF();
        mHandler = new Handler() {

            public void handleMessage(Message msg)
            {
                if(msg.what != 1)
                    return;
                invalidate();
                if(mStepping)
                    return;
                msg = obtainMessage(1);
                long current = SystemClock.uptimeMillis();
                if(mNextTime < current)
                    mNextTime = current + 10L;
                sendMessageAtTime(msg, mNextTime);
                mNextTime+= = 10L;
            }

            final PageTurner this$0;

            
            {
                this$0 = PageTurner.this;
                super();
            }
        };
    }

    public PageTurner(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
        mChildRect = new Rect();
        mPageTurnCorner = new PointF();
        mOppositeCorner = new PointF();
        mPageDim = new PointF();
        mHandler = new _cls1();
        android.content.Resources.StyledAttributes a = context.obtainStyledAttributes(attrs, android.R.styleable.PageTurner);
        mBackPage = a.getDrawable(1);
        mPageBackground = a.getDrawable(0);
        mPageId = a.getResourceID(3, -1);
        mCorner = a.getInt(2, 0);
    }

    protected void onFinishInflate()
    {
        super.onFinishInflate();
        if(mPageId != -1)
        {
            mPage = (Page)findViewById(mPageId);
            if(mPage != null)
                mPage.setPageTurner(this);
        }
    }

    public void setPageId(int pageId)
    {
        mPageId = pageId;
        mPage = (Page)findViewById(mPageId);
        if(mPage != null)
            mPage.setPageTurner(this);
    }

    public int getPageId()
    {
        return mPageId;
    }

    public void setPage(Page page)
    {
        mPage = page;
    }

    public Page getPage()
    {
        return mPage;
    }

    public void setCorner(int corner)
    {
        mCorner = corner;
    }

    public int getCorner()
    {
        return mCorner;
    }

    protected void dispatchDraw(Canvas canvas)
    {
        if(mPageTurning && mPage != null && computePageTurn())
            mPage.setClipPath(mForegroundPath);
        super.dispatchDraw(canvas);
        if(mPageTurning)
        {
            drawBackground(canvas);
            drawBackPage(canvas);
            if(!updateTimeStep())
            {
                mHandler.removeMessages(1);
                if(mPage != null)
                    mPage.onPageTurnFinished(canvas);
                mPageTurning = false;
                mStepping = false;
                invalidate();
            }
        }
    }

    public void startPageTurn()
    {
        if(mPage == null && mPageId != -1)
            mPage = (Page)findViewById(mPageId);
        if(mPage == null)
            return;
        mPage.setPageTurner(this);
        Drawable d = mPage.getPageBackground();
        if(d != null)
            mPageBackground = d;
        d = mPage.getBackPage();
        if(d != null)
            mBackPage = d;
        int corner = mPage.getCorner();
        if(corner != -1)
            mCorner = corner;
        mPageTurning = true;
        mTimeStep = 0;
        mDrawnTimeStep = -1;
        Message msg = mHandler.obtainMessage(1);
        mNextTime = SystemClock.uptimeMillis() + 100L;
        mHandler.sendMessageAtTime(msg, mNextTime);
    }

    public void stepPageTurn()
    {
        if(!mStepping)
        {
            mStepping = true;
            startPageTurn();
        } else
        {
            Message msg = mHandler.obtainMessage(1);
            mNextTime = SystemClock.uptimeMillis() + 10L;
            mHandler.sendMessageAtTime(msg, mNextTime);
        }
    }

    private boolean updateTimeStep()
    {
        if(mTimeStep >= 30)
        {
            return false;
        } else
        {
            mTimeStep++;
            return true;
        }
    }

    private boolean computePageTurn()
    {
        if(mDrawnTimeStep == mTimeStep || mTimeStep >= 30)
            return false;
        if(mPage == null)
            return false;
        mDrawnTimeStep = mTimeStep;
        View child = mPage.getChildAt(0);
        child.getDrawingRect(mChildRect);
        mOuterOffsetX = child.getWindowLeft() - getWindowLeft();
        mOuterOffsetY = child.getWindowTop() - getWindowTop();
        float width = mChildRect.right;
        float height = mChildRect.bottom;
        mPivotX = ((float)mTimeStep / 30F) * width;
        mForegroundPath = new Path();
        mBackPagePath = new Path();
        mBackgroundPath = new Path();
        float slope = width / (mPivotX - width);
        float y = mPivotX * slope;
        mPageTurnCorner.x = 0.0F;
        mPageTurnCorner.y = height;
        mOppositeCorner.x = width;
        mOppositeCorner.y = 0.0F;
        float x0 = mPivotX;
        float cornerIntersect = (height * width) / (height + width);
        if((mCorner & 1) != 0)
        {
            mPageTurnCorner.x = width;
            mOppositeCorner.x = 0.0F;
            x0 = width - x0;
        }
        if((mCorner & 2) != 0)
        {
            mPageTurnCorner.y = 0.0F;
            mOppositeCorner.y = height;
        }
        mPageDim.x = width;
        mPageDim.y = height;
        float page_slope;
        if(mPivotX <= cornerIntersect)
            page_slope = firstHalfPageTurn(mPageDim, mPageTurnCorner, mOppositeCorner, x0, slope, y);
        else
            page_slope = secondHalfPageTurn(mPageDim, mPageTurnCorner, mOppositeCorner, x0, slope, y);
        mRotation = (float)Math.atan(page_slope);
        mRotation = (float)(((double)(-mRotation) * 180D) / 3.1415926535897931D);
        return true;
    }

    private float firstHalfPageTurn(PointF pageDim, PointF pageTurnCorner, PointF oppositeCorner, float xPivot, float slope, float y)
    {
        float width = pageDim.x;
        float height = pageDim.y;
        View child = mPage.getChildAt(0);
        int innerOffsetX = child.getWindowLeft() - mPage.getWindowLeft();
        int innerOffsetY = child.getWindowTop() - mPage.getWindowTop();
        float y1 = height + y;
        float x2 = (float)((2D * (double)y) / ((double)slope + 1.0D / (double)slope));
        float y2 = height + x2 / slope;
        float page_slope = (height - y2) / (x2 - mPivotX);
        if((mCorner & 1) != 0)
        {
            x2 = width - x2;
            page_slope = -page_slope;
        }
        if((mCorner & 2) != 0)
        {
            y1 = height - y1;
            y2 = height - y2;
            page_slope = -page_slope;
        }
        float x0 = xPivot;
        float x1 = pageTurnCorner.x;
        mForegroundPath.moveTo((float)innerOffsetX + x1, (float)innerOffsetY + y1);
        mForegroundPath.lineTo((float)innerOffsetX + pageTurnCorner.x, (float)innerOffsetY + oppositeCorner.y);
        mForegroundPath.lineTo((float)innerOffsetX + oppositeCorner.x, (float)innerOffsetY + oppositeCorner.y);
        mForegroundPath.lineTo((float)innerOffsetX + oppositeCorner.x, (float)innerOffsetY + pageTurnCorner.y);
        mForegroundPath.lineTo((float)innerOffsetX + x0, (float)innerOffsetY + pageTurnCorner.y);
        mForegroundPath.lineTo((float)innerOffsetX + x2, (float)innerOffsetY + y2);
        mForegroundPath.lineTo((float)innerOffsetX + x1, (float)innerOffsetY + y1);
        mBackPagePath.moveTo((float)mOuterOffsetX + x1, (float)mOuterOffsetY + y1);
        mBackPagePath.lineTo((float)mOuterOffsetX + x2, (float)mOuterOffsetY + y2);
        mBackPagePath.lineTo((float)mOuterOffsetX + x0, (float)mOuterOffsetY + pageTurnCorner.y);
        mBackPagePath.lineTo((float)mOuterOffsetX + x1, (float)mOuterOffsetY + y1);
        mBackgroundPath.moveTo((float)mOuterOffsetX + x1, (float)mOuterOffsetY + y1);
        mBackgroundPath.lineTo((float)mOuterOffsetX + x0, (float)mOuterOffsetY + pageTurnCorner.y);
        mBackgroundPath.lineTo((float)mOuterOffsetX + pageTurnCorner.x, (float)mOuterOffsetY + pageTurnCorner.y);
        mBackgroundPath.lineTo((float)mOuterOffsetX + x1, (float)mOuterOffsetY + y1);
        return page_slope;
    }

    private float secondHalfPageTurn(PointF pageDim, PointF pageTurnCorner, PointF oppositeCorner, float xPivot, float slope, float y)
    {
        float width = pageDim.x;
        float height = pageDim.y;
        View child = mPage.getChildAt(0);
        int xOffset = child.getWindowLeft() - mPage.getWindowLeft();
        int yOffset = child.getWindowTop() - mPage.getWindowTop();
        float y1 = 0.0F;
        float x1 = width - ((height + width) * (width - mPivotX)) / width;
        float x3 = (float)((2D * (double)y) / ((double)slope + 1.0D / (double)slope));
        float y3 = height + x3 / slope;
        float page_slope = (height - y3) / (x3 - mPivotX);
        float b = height - x1 * page_slope;
        float x2 = (float)((double)(-y - b) / ((double)page_slope + 1.0D / (double)page_slope));
        float y2 = (x1 - x2) * page_slope;
        if((mCorner & 1) != 0)
        {
            x1 = width - x1;
            x2 = width - x2;
            x3 = width - x3;
            page_slope = -page_slope;
        }
        if((mCorner & 2) != 0)
        {
            y1 = height - y1;
            y2 = height - y2;
            y3 = height - y3;
            page_slope = -page_slope;
        }
        float x0 = xPivot;
        mForegroundPath.moveTo((float)xOffset + x1, (float)yOffset + y1);
        mForegroundPath.lineTo((float)xOffset + oppositeCorner.x, (float)yOffset + oppositeCorner.y);
        mForegroundPath.lineTo((float)xOffset + oppositeCorner.x, (float)yOffset + pageTurnCorner.y);
        mForegroundPath.lineTo((float)xOffset + x0, (float)yOffset + pageTurnCorner.y);
        mForegroundPath.lineTo((float)xOffset + x1, (float)yOffset + y1);
        mBackPagePath.moveTo((float)mOuterOffsetX + x1, (float)mOuterOffsetY + y1);
        mBackPagePath.lineTo((float)mOuterOffsetX + x2, (float)mOuterOffsetY + y2);
        mBackPagePath.lineTo((float)mOuterOffsetX + x3, (float)mOuterOffsetY + y3);
        mBackPagePath.lineTo((float)mOuterOffsetX + x0, (float)mOuterOffsetY + pageTurnCorner.y);
        mBackPagePath.lineTo((float)mOuterOffsetX + x1, (float)mOuterOffsetY + y1);
        mBackgroundPath.moveTo((float)mOuterOffsetX + x1, (float)mOuterOffsetY + y1);
        mBackgroundPath.lineTo((float)mOuterOffsetX + x0, (float)mOuterOffsetY + pageTurnCorner.y);
        mBackgroundPath.lineTo((float)mOuterOffsetX + pageTurnCorner.x, (float)mOuterOffsetY + pageTurnCorner.y);
        mBackgroundPath.lineTo((float)mOuterOffsetX + pageTurnCorner.x, (float)mOuterOffsetY + oppositeCorner.y);
        mBackgroundPath.lineTo((float)mOuterOffsetX + x1, (float)mOuterOffsetY + y1);
        return page_slope;
    }

    private void drawBackground(Canvas canvas)
    {
        canvas.save();
        canvas.clipPath(mBackgroundPath, android.graphics.Canvas.ClipMode.INTERSECT);
        canvas.translate(mOuterOffsetX, mOuterOffsetY);
        if(mPageBackground != null)
        {
            mPageBackground.setBounds(0, 0, mChildRect.right, mChildRect.bottom);
            mPageBackground.draw(canvas);
        }
        if(mPage != null)
            mPage.drawBackground(canvas);
        canvas.restore();
    }

    private void drawBackPage(Canvas canvas)
    {
        float width = mChildRect.right;
        float height = mChildRect.bottom;
        canvas.save();
        canvas.clipPath(mBackPagePath, android.graphics.Canvas.ClipMode.INTERSECT);
        float xShift = 2.0F * mPivotX - width;
        float xRotate = width - mPivotX;
        float yRotate = height;
        if((mCorner & 1) != 0)
        {
            xShift = width - 2.0F * mPivotX;
            xRotate = mPivotX;
        }
        if((mCorner & 2) != 0)
            yRotate = 0.0F;
        canvas.translate((float)mOuterOffsetX + xShift, mOuterOffsetY);
        canvas.rotate(mRotation, xRotate, yRotate);
        if(mBackPage != null)
        {
            mBackPage.setBounds(0, 0, mChildRect.right, mChildRect.bottom);
            mBackPage.draw(canvas);
        }
        if(mPage != null)
            mPage.drawBackPage(canvas);
        canvas.restore();
    }

    private static final int CORNER_RIGHT_MASK = 1;
    private static final int CORNER_TOP_MASK = 2;
    public static final int CORNER_BOTTOM_LEFT = 0;
    public static final int CORNER_BOTTOM_RIGHT = 1;
    public static final int CORNER_TOP_LEFT = 2;
    public static final int CORNER_TOP_RIGHT = 3;
    private static final int INVALIDATE = 1;
    private static final int INITIAL_TIME_DELAY = 100;
    private static final int TIME_DELAY = 10;
    private static final int TIME_STEPS = 30;
    private boolean mPageTurning;
    private boolean mStepping;
    private long mNextTime;
    private int mTimeStep;
    private int mDrawnTimeStep;
    private int mCorner;
    private Drawable mBackPage;
    private Drawable mPageBackground;
    private Path mForegroundPath;
    private Path mBackPagePath;
    private Path mBackgroundPath;
    private float mRotation;
    private Rect mChildRect;
    private int mOuterOffsetX;
    private int mOuterOffsetY;
    private float mPivotX;
    private int mPageId;
    private Page mPage;
    private PointF mPageTurnCorner;
    private PointF mOppositeCorner;
    private PointF mPageDim;
    private final Handler mHandler;




}

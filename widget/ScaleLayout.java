// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScaleLayout.java

package android.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import java.util.Map;

// Referenced classes of package android.widget:
//            FrameLayout

public class ScaleLayout extends FrameLayout
{

    public ScaleLayout(Context context)
    {
        super(context);
        mScale = 1.0F;
        mChildWidth = 0;
        mChildHeight = 0;
    }
    //一直出现的inflateParams是什么东西
    public ScaleLayout(Context context, AttributeSet attrs, Map inflateParams)
    {
        this(context, attrs, inflateParams, 0);
    }

    public ScaleLayout(Context context, AttributeSet attrs, Map inflateParams, int defStyle)
    {
        super(context, attrs, inflateParams, defStyle);
        mScale = 1.0F;
        mChildWidth = 0;
        mChildHeight = 0;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
        if(widthMode == 0 && heightMode == 0)
        {
            mScale = 1.0F;
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        float heightRatio = 1.0F;
        float widthRatio = 1.0F;
        if(widthMode != 0)
            widthRatio = (float)mChildWidth / (float)((widthSize - mPaddingLeft) + mPaddingRight);
        if(heightMode != 0)
            heightRatio = (float)mChildHeight / (float)((heightSize - mPaddingTop) + mPaddingBottom);
        mScale = Math.min(widthRatio, heightRatio);
        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        int childWidthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(mChildWidth, 0x40000000);
        int childHeightMeasure = android.view.View.MeasureSpec.makeMeasureSpec(mChildHeight, 0x40000000);
        for(int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            if(mMeasureAllChildren || child.getVisibility() != 8)
            {
                child.measure(childWidthMeasureSpec, childHeightMeasure);
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }
        }

        maxWidth = (int)((float)maxWidth * mScale);
        maxHeight = (int)((float)maxHeight * mScale);
        maxWidth += mPaddingLeft + mPaddingRight;
        maxHeight += mPaddingTop + mPaddingBottom;
        setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec), resolveSize(maxHeight, heightMeasureSpec));
    }

    protected boolean getChildStaticTransformation(View child, Transformation t)
    {
        if(mScale != 1.0F)
        {
            t.clear();
            t.getMatrix().setScale(mScale, mScale);
            return true;
        } else
        {
            return false;
        }
    }

    public void setChildSize(int childWidth, int childHeight)
    {
        mChildWidth = childWidth;
        mChildHeight = childHeight;
    }

    private float mScale;
    private int mChildWidth;
    private int mChildHeight;
}

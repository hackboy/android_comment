// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RadioGroup.java

package android.widget;

import android.R;
import android.content.Context;
import android.content.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.Map;

// Referenced classes of package android.widget:
//            LinearLayout, RadioButton, CompoundButton

public class RadioGroup extends LinearLayout
{
    private class PassThroughHierarchyChangeListener
        implements android.view.ViewGroup.OnHierarchyChangeListener
    {

        public void onChildViewAdded(View parent, View child)
        {
            if(parent == RadioGroup.this && (child instanceof RadioButton))
            {
                int id = child.getId();
                if(id == -1)
                {
                    id = child.hashCode();
                    child.setId(id);
                }
                ((RadioButton)child).setOnCheckedChangeWidgetListener(mChildOnCheckedChangeListener);
            }
            if(mOnHierarchyChangeListener != null)
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
        }

        public void onChildViewRemoved(View parent, View child)
        {
            if(parent == RadioGroup.this && (child instanceof RadioButton))
                ((RadioButton)child).setOnCheckedChangeWidgetListener(null);
            if(mOnHierarchyChangeListener != null)
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
        }

        private android.view.ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;
        final RadioGroup this$0;


        private PassThroughHierarchyChangeListener()
        {
            this$0 = RadioGroup.this;
            super();
        }

    }

    private class CheckedStateTracker
        implements CompoundButton.OnCheckedChangeListener
    {

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            if(mProtectFromCheckedChange)
                return;
            mProtectFromCheckedChange = true;
            if(mCheckedId != -1)
                setCheckedStateForView(mCheckedId, false);
            mProtectFromCheckedChange = false;
            int id = buttonView.getId();
            setCheckedId(id);
        }

        final RadioGroup this$0;

        private CheckedStateTracker()
        {
            this$0 = RadioGroup.this;
            super();
        }

    }

    public static interface OnCheckedChangeListener
    {

        public abstract void onCheckedChanged(RadioGroup radiogroup, int i);
    }

    public static class LayoutParams extends LinearLayout.LayoutParams
    {

        protected void setBaseAttributes(android.content.Resources.StyledAttributes a, int widthAttr, int heightAttr)
        {
            if(a.hasValue(widthAttr))
                width = a.getLayoutDimension(widthAttr, "layout_width");
            else
                width = -2;
            if(a.hasValue(heightAttr))
                height = a.getLayoutDimension(heightAttr, "layout_height");
            else
                height = -2;
        }

        public LayoutParams(Context c, AttributeSet attrs)
        {
            super(c, attrs);
        }

        public LayoutParams(int w, int h)
        {
            super(w, h);
        }

        public LayoutParams(int w, int h, float initWeight)
        {
            super(w, h, initWeight);
        }
    }


    public RadioGroup(Context context)
    {
        super(context);
        mCheckedId = -1;
        mProtectFromCheckedChange = false;
    }

    public RadioGroup(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
        mCheckedId = -1;
        mProtectFromCheckedChange = false;
        android.content.Resources.StyledAttributes attributes = context.obtainStyledAttributes(attrs, android.R.styleable.RadioGroup, 0x1010023, 0);
        int value = attributes.getResourceID(0, -1);
        if(value != -1)
            mCheckedId = value;
        attributes.recycle();
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    public void setOnHierarchyChangeListener(android.view.ViewGroup.OnHierarchyChangeListener listener)
    {
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    protected void onFinishInflate()
    {
        super.onFinishInflate();
        if(mCheckedId != -1)
        {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }

    public void check(int id)
    {
        if(id != -1 && id == mCheckedId)
            return;
        if(mCheckedId != -1)
            setCheckedStateForView(mCheckedId, false);
        if(id != -1)
            setCheckedStateForView(id, true);
        setCheckedId(id);
    }

    private void setCheckedId(int id)
    {
        mCheckedId = id;
        if(mOnCheckedChangeListener != null)
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
    }

    private void setCheckedStateForView(int viewId, boolean checked)
    {
        View checkedView = findViewById(viewId);
        if(checkedView != null && (checkedView instanceof RadioButton))
            ((RadioButton)checkedView).setChecked(checked);
    }

    public int getCheckedRadioButtonId()
    {
        return mCheckedId;
    }

    public void clearCheck()
    {
        check(-1);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener)
    {
        mOnCheckedChangeListener = listener;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new LayoutParams(getContext(), attrs);
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p)
    {
        return p instanceof LayoutParams;
    }

    public volatile LinearLayout.LayoutParams generateLayoutParams(AttributeSet x0)
    {
        return generateLayoutParams(x0);
    }

    public volatile android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet x0)
    {
        return generateLayoutParams(x0);
    }

    private int mCheckedId;
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    private boolean mProtectFromCheckedChange;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;






}

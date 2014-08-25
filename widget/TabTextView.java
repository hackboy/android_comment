// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TabTextView.java

package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import java.util.Map;

// Referenced classes of package android.widget:
//            TextView, TabWidget

public class TabTextView extends TextView
{

    public TabTextView(Context context)
    {
        this(context, null, null);
    }

    public TabTextView(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
    }

    public void setSelected(boolean selected)
    {
        super.setSelected(selected);
        android.view.ViewParent parent = getParent();
        if(parent instanceof TabWidget)
            if(selected)
                setTextSize(16F);
            else
                setTextSize(14F);
    }

    public int getSelectedTextColor()
    {
        android.view.ViewParent parent = getParent();
        if(parent instanceof TabWidget)
            return ((TabWidget)parent).getSelectedColor();
        else
            return super.getSelectedTextColor();
    }

    public int getNormalTextColor()
    {
        android.view.ViewParent parent = getParent();
        if(parent instanceof TabWidget)
            return ((TabWidget)parent).getNormalColor();
        else
            return super.getNormalTextColor();
    }

    public int getDisabledTextColor()
    {
        android.view.ViewParent parent = getParent();
        if(parent instanceof TabWidget)
            return ((TabWidget)parent).getDisabledColor();
        else
            return super.getDisabledTextColor();
    }
}

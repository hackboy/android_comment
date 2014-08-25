// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RadioButton.java

package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import java.util.Map;

// Referenced classes of package android.widget:
//            CompoundButton

public class RadioButton extends CompoundButton
{

    public RadioButton(Context context)
    {
        this(context, null, null);
    }

    public RadioButton(Context context, AttributeSet attrs, Map inflateParams)
    {   
        //尼玛，直接用具体数值，坑爹的吧
        this(context, attrs, inflateParams, 0x1010023);
    }

    public RadioButton(Context context, AttributeSet attrs, Map inflateParams, int defStyle)
    {
        super(context, attrs, inflateParams, defStyle);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        boolean result = false;
        switch(keyCode)
        {
        case 23: // '\027'
        case 64: // '@'
            if(!isChecked())
                toggle();
            result = true;
            break;
        }
        return result;
    }
}

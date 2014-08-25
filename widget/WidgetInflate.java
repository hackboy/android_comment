// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WidgetInflate.java

package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewInflate;
import java.util.Map;

public class WidgetInflate extends ViewInflate
{

    public WidgetInflate(Context context)
    {
        super(context);
    }

    protected View onCreateView(String name, AttributeSet attrs, Map inflateParams)
        throws ClassNotFoundException
    {
        String arr$[];
        int len$;
        int i$;
        arr$ = sClassPrefixList;
        len$ = arr$.length;
        i$ = 0;
_L3:
        if(i$ >= len$) goto _L2; else goto _L1
_L1:
        String prefix = arr$[i$];
        View view = createView(name, prefix, attrs, inflateParams);
        if(view != null)
            return view;
        continue; /* Loop/switch isn't completed */
        ClassNotFoundException e;
        e;
        i$++;
          goto _L3
_L2:
        return super.onCreateView(name, attrs, inflateParams);
    }

    private static final String sClassPrefixList[] = {
        "android.widget.", "android.webkit."
    };

}

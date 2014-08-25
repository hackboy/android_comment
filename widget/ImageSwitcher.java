// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ImageSwitcher.java

package android.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ContentURI;
import android.util.AttributeSet;
import java.util.Map;

// Referenced classes of package android.widget:
//            ViewSwitcher, ImageView
//两张图片间进行切换
public class ImageSwitcher extends ViewSwitcher
{

    public ImageSwitcher(Context context)
    {
        super(context);
    }

    public ImageSwitcher(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
    }
    //设置图片资源，通过resid uri 和drawable
    public void setImageResource(int resid)
    {
        ImageView image = (ImageView)getNextView();
        image.setImageResource(resid);
        showNext();
    }

    public void setImageURI(ContentURI uri)
    {
        ImageView image = (ImageView)getNextView();
        image.setImageURI(uri);
        showNext();
    }

    public void setImageDrawable(Drawable drawable)
    {
        ImageView image = (ImageView)getNextView();
        image.setImageDrawable(drawable);
        showNext();
    }
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StatusBarServer.java

package android.server.status;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.util.AttributeSet;
import android.view.*;
import android.widget.WidgetInflate;
import java.util.Map;

// Referenced classes of package android.server.status:
//            StatusBar, IStatusBar, StatusBarIcons

public class StatusBarServer extends IStatusBar.Stub
{
    private static final class InflateFactory
        implements android.view.ViewInflate.Factory
    {

        public View createView(String name, AttributeSet attrs, Map inflateParams)
        {
            if(name.equals("android.server.status.StatusBar"))
                return new StatusBar(mContext, attrs, inflateParams);
            if(name.equals("android.server.status.StatusBarIcons"))
                return new StatusBarIcons(mContext, attrs, inflateParams);
            else
                return null;
        }

        private Context mContext;

        InflateFactory(Context context)
        {
            mContext = context;
        }
    }


    public StatusBarServer(StatusBar sb, android.view.WindowManager.LayoutParams lp)
    {
        mStatusBar = sb;
        mLayoutParams = lp;
        mHandler = new Handler();
    }

    public StatusBar getStatusBar()
    {
        return mStatusBar;
    }

    public void activate()
    {
        mHandler.post(new Runnable() {

            public void run()
            {
                mStatusBar.activate();
            }

            final StatusBarServer this$0;

            
            {
                this$0 = StatusBarServer.this;
                super();
            }
        });
    }

    public void deactivate()
    {
        mHandler.post(new Runnable() {

            public void run()
            {
                mStatusBar.deactivate();
            }

            final StatusBarServer this$0;

            
            {
                this$0 = StatusBarServer.this;
                super();
            }
        });
    }

    public static void main(Context context)
    {
        WidgetInflate factory = new WidgetInflate(context);
        factory.setFactory(new InflateFactory(context));
        StatusBar sb = (StatusBar)factory.inflate(0x1040022, null, null);
        android.view.WindowManager.LayoutParams lp = makeLayoutParams(sb);
        WindowManagerImpl.getDefault().addView(sb, lp);
        sStatusBarServer = new StatusBarServer(sb, lp);
        try
        {
            ServiceManagerNative.getDefault().addService("statusbar", sStatusBarServer);
        }
        catch(DeadObjectException ex) { }
    }

    public static StatusBarServer getInstance()
    {
        return sStatusBarServer;
    }

    private static android.view.WindowManager.LayoutParams makeLayoutParams(StatusBar sb)
    {
        int pixelFormat = -3;
        Drawable bg = sb.getBackground();
        if(bg != null)
            pixelFormat = bg.getOpacity();
        android.view.WindowManager.LayoutParams lp = new android.view.WindowManager.LayoutParams(-1, -2, 2000, 8, pixelFormat);
        lp.gravity = 55;
        lp.setTitle("StatusBar");
        return lp;
    }

    private static StatusBarServer sStatusBarServer = null;
    private StatusBar mStatusBar;
    private android.view.WindowManager.LayoutParams mLayoutParams;
    private Handler mHandler;


}

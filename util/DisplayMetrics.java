// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DisplayMetrics.java

package android.util;


public class DisplayMetrics
{

    public DisplayMetrics()
    {
    }

    public void setTo(DisplayMetrics o)
    {
        widthPixels = o.widthPixels;
        heightPixels = o.heightPixels;
        density = o.density;
        scaledDensity = o.scaledDensity;
        xdpi = o.xdpi;
        ydpi = o.ydpi;
        touchscreen = o.touchscreen;
        keyboard = o.keyboard;
        navigation = o.navigation;
    }

    public void setToDefaults()
    {
        widthPixels = 0;
        heightPixels = 0;
        density = 1.0F;
        scaledDensity = 1.0F;
        xdpi = 160F;
        ydpi = 160F;
        touchscreen = 0;
        keyboard = 0;
        navigation = 0;
    }

    public static final int TOUCHSCREEN_NOTOUCH = 1;
    public static final int TOUCHSCREEN_STYLUS = 2;
    public static final int TOUCHSCREEN_FINGER = 3;
    public static final int KEYBOARD_QWERTY = 1;
    public static final int KEYBOARD_12KEY = 2;
    public static final int NAVIGATION_DPAD = 1;
    public static final int NAVIGATION_TRACKBALL = 2;
    public static final int NAVIGATION_WHEEL = 3;
    public int widthPixels;
    public int heightPixels;
    public float density;
    public float scaledDensity;
    public float xdpi;
    public float ydpi;
    public int touchscreen;
    public int keyboard;
    public int navigation;
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GalleryAdapter.java

package android.widget;


// Referenced classes of package android.widget:
//            SpinnerAdapter

public interface GalleryAdapter
    extends SpinnerAdapter
{

    public abstract float getScale(boolean flag, int i);

    public abstract float getAlpha(boolean flag, int i);
}

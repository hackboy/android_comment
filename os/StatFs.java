// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StatFs.java

package android.os;


public class StatFs
{

    public StatFs(String path)
    {
        native_setup(path);
    }

    protected void finalize()
    {
        native_finalize();
    }

    public native int getBlockSize();

    public native int getBlockCount();

    public native int getFreeBlocks();

    public native int getAvailableBlocks();

    private native void native_setup(String s);

    private native void native_finalize();

    private int mNativeContext;
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Exec.java

package android.os;

import java.io.FileDescriptor;

public class Exec
{

    public Exec()
    {
    }

    public static FileDescriptor createSubprocess(String cmd, String arg0, String arg1)
    {
        return createSubprocess(cmd, arg0, arg1, null);
    }

    public static native FileDescriptor createSubprocess(String s, String s1, String s2, int ai[]);

    public static native void setPtyWindowSize(FileDescriptor filedescriptor, int i, int j, int k, int l);

    public static native int waitFor(int i);
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Log.java

package android.util;

import android.os.RuntimeInit;
import java.io.PrintWriter;
import java.io.StringWriter;

public final class Log
{

    private Log()
    {
    }

    public static int v(String tag, String msg)
    {
        return println(2, tag, msg);
    }

    public static int v(String tag, String msg, Throwable tr)
    {
        return println(2, tag, (new StringBuilder()).append(msg).append('\n').append(getStackTraceString(tr)).toString());
    }

    public static int d(String tag, String msg)
    {
        return println(3, tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr)
    {
        return println(3, tag, (new StringBuilder()).append(msg).append('\n').append(getStackTraceString(tr)).toString());
    }

    public static int i(String tag, String msg)
    {
        return println(4, tag, msg);
    }

    public static int i(String tag, String msg, Throwable tr)
    {
        return println(4, tag, (new StringBuilder()).append(msg).append('\n').append(getStackTraceString(tr)).toString());
    }

    public static int w(String tag, String msg)
    {
        return println(5, tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr)
    {
        return println(5, tag, (new StringBuilder()).append(msg).append('\n').append(getStackTraceString(tr)).toString());
    }

    public static native boolean isLoggable(String s, int j);

    public static int w(String tag, Throwable tr)
    {
        return println(5, tag, getStackTraceString(tr));
    }

    public static int e(String tag, String msg)
    {
        return println(6, tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr)
    {
        println(6, "Log", "Saving exception to database.");
        RuntimeInit.reportException(tag, tr);
        return println(6, tag, (new StringBuilder()).append(msg).append('\n').append(getStackTraceString(tr)).toString());
    }

    public static String getStackTraceString(Throwable tr)
    {
        if(tr == null)
        {
            return "";
        } else
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            tr.printStackTrace(pw);
            return sw.toString();
        }
    }

    public static native int println(int j, String s, String s1);

    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;
}

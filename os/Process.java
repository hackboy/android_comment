// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Process.java

package android.os;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;
import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;

// Referenced classes of package android.os:
//            ZygoteStartFailedEx

public class Process
{

    public Process()
    {
    }

    public static final int start(String processClass, String niceName, int uid, int gid, int gids[])
    {
        if(!supportsProcesses())
            break MISSING_BLOCK_LABEL_38;
        return startViaZygote(processClass, niceName, uid, gid, gids);
        ZygoteStartFailedEx ex;
        ex;
        Log.e("Process", "Starting VM process through Zygote failed");
        throw new RuntimeException("Starting VM process through Zygote failed", ex);
        Runnable runnable = new Runnable(processClass) {

            public void run()
            {
                Process.invokeStaticMain(processClass);
            }

            final String val$processClass;

            
            {
                processClass = s;
                super();
            }
        };
        if(niceName != null)
            (new Thread(runnable, niceName)).start();
        else
            (new Thread(runnable)).start();
        return 0;
    }

    public static final int start(String processClass, int uid, int gid, int gids[])
    {
        return start(processClass, "", uid, gid, gids);
    }

    private static void invokeStaticMain(String className)
    {
        Object args[] = new Object[1];
        args[0] = new String[0];
        try
        {
            Class cl = Class.forName(className);
            cl.getMethod("main", new Class[] {
                [Ljava/lang/String;
            }).invoke(null, args);
        }
        catch(Exception ex)
        {
            Log.e("Process", (new StringBuilder()).append("Exception invoking static main on ").append(className).toString(), ex);
            throw new RuntimeException(ex);
        }
    }

    private static void openZygoteSocketIfNeeded()
        throws ZygoteStartFailedEx
    {
        int retryCount;
        if(sPreviousZygoteOpenFailed)
            retryCount = 0;
        else
            retryCount = 10;
        for(int retry = 0; sZygoteSocket == null && retry < retryCount + 1; retry++)
        {
            if(retry > 0)
                try
                {
                    Log.i("Zygote", "Zygote not up yet, sleeping...");
                    Thread.sleep(500L);
                }
                catch(InterruptedException ex) { }
            try
            {
                sZygoteSocket = new LocalSocket();
                sZygoteSocket.connect(new LocalSocketAddress("android.zygote"));
                sZygoteInputStream = new DataInputStream(sZygoteSocket.getInputStream());
                sZygoteWriter = new BufferedWriter(new OutputStreamWriter(sZygoteSocket.getOutputStream()), 256);
                Log.i("Zygote", "Process: zygote socket opened");
                sPreviousZygoteOpenFailed = false;
                break;
            }
            catch(IOException ex) { }
            if(sZygoteSocket != null)
                try
                {
                    sZygoteSocket.close();
                }
                catch(IOException ex2)
                {
                    Log.e("Process", "I/O exception on close after exception", ex2);
                }
            sZygoteSocket = null;
        }

        if(sZygoteSocket == null)
        {
            sPreviousZygoteOpenFailed = true;
            throw new ZygoteStartFailedEx("connect failed");
        } else
        {
            return;
        }
    }

    private static int zygoteSendArgsAndGetPid(ArrayList args)
        throws ZygoteStartFailedEx
    {
        openZygoteSocketIfNeeded();
        int pid;
        try
        {
            sZygoteWriter.write(Integer.toString(args.size()));
            sZygoteWriter.newLine();
            int sz = args.size();
            for(int i = 0; i < sz; i++)
            {
                sZygoteWriter.write((String)args.get(i));
                sZygoteWriter.newLine();
            }

            sZygoteWriter.flush();
            pid = sZygoteInputStream.readInt();
            if(pid < 0)
                throw new ZygoteStartFailedEx("fork() failed");
        }
        catch(IOException ex)
        {
            try
            {
                if(sZygoteSocket != null)
                    sZygoteSocket.close();
            }
            catch(IOException ex2)
            {
                Log.e("Process", "I/O exception on routine close", ex2);
            }
            sZygoteSocket = null;
            throw new ZygoteStartFailedEx(ex);
        }
        return pid;
    }

    private static int startViaZygote(String processClass, String niceName, int uid, int gid, int gids[])
        throws ZygoteStartFailedEx
    {
        int pid;
        synchronized(android/os/Process)
        {
            ArrayList argsForZygote = new ArrayList();
            argsForZygote.add((new StringBuilder()).append("--setuid=").append(uid).toString());
            argsForZygote.add((new StringBuilder()).append("--setgid=").append(gid).toString());
            if(gids != null && gids.length > 0)
            {
                StringBuilder sb = new StringBuilder();
                sb.append("--setgroups=");
                int sz = gids.length;
                for(int i = 0; i < sz; i++)
                {
                    if(i != 0)
                        sb.append(',');
                    sb.append(gids[i]);
                }

                argsForZygote.add(sb.toString());
            }
            if(niceName != null)
                argsForZygote.add((new StringBuilder()).append("--nice-name=").append(niceName).toString());
            argsForZygote.add(processClass);
            pid = zygoteSendArgsAndGetPid(argsForZygote);
        }
        if(pid <= 0)
            throw new ZygoteStartFailedEx((new StringBuilder()).append("zygote start failed:").append(pid).toString());
        else
            return pid;
    }

    private static final native int startNative(String s, String s1);

    public static final native int myPid();

    public static final native int myTid();

    public static final native void setThreadPriority(int i, int j);

    public static final native void setThreadPriority(int i);

    public static final native boolean supportsProcesses();

    public static final native boolean setOomAdj(int i, int j);

    public static final native void setArgV0(String s);

    public static final native void stopSelf();

    public static final void killProcess(int pid)
    {
        sendSignal(pid, 9);
    }

    public static final native int setUid(int i);

    public static final native int setGid(int i);

    public static final native void sendSignal(int i, int j);

    public static final native int[] getPids(String s, int ai[]);

    public static final native boolean readProcFile(String s, int ai[], String as[], long al[], float af[]);

    private static final String LOG_TAG = "Process";
    private static final String ZYGOTE_SOCKET = "android.zygote";
    public static final String GOOGLE_SHARED_APP_PROCESS = "com.google.process.app";
    public static final String GOOGLE_SHARED_APP_CONTENT = "com.google.process.content";
    public static final int SYSTEM_UID = 1000;
    public static final int PHONE_UID = 1001;
    public static final int FIRST_APPLICATION_UID = 10000;
    public static final int LAST_APPLICATION_UID = 0x1869f;
    public static final int BLUETOOTH_GID = 2000;
    public static final int THREAD_PRIORITY_DEFAULT = 0;
    public static final int THREAD_PRIORITY_LOWEST = 19;
    public static final int THREAD_PRIORITY_BACKGROUND = 5;
    public static final int THREAD_PRIORITY_FOREGROUND = -5;
    public static final int THREAD_PRIORITY_DISPLAY = -10;
    public static final int THREAD_PRIORITY_URGENT_DISPLAY = -15;
    private static final boolean ZYGOTE_FALLBACK = false;
    public static final int SIGNAL_QUIT = 3;
    public static final int SIGNAL_KILL = 9;
    public static final int SIGNAL_USR1 = 10;
    static LocalSocket sZygoteSocket;
    static DataInputStream sZygoteInputStream;
    static BufferedWriter sZygoteWriter;
    static boolean sPreviousZygoteOpenFailed;
    static final int ZYGOTE_RETRY_MILLIS = 500;
    public static final int PROC_TERM_MASK = 255;
    public static final int PROC_ZERO_TERM = 0;
    public static final int PROC_SPACE_TERM = 32;
    public static final int PROC_COMBINE = 256;
    public static final int PROC_PARENS = 512;
    public static final int PROC_OUT_STRING = 4096;
    public static final int PROC_OUT_LONG = 8192;
    public static final int PROC_OUT_FLOAT = 16384;

}

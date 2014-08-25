// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RuntimeInit.java

package android.os;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.ddm.DdmRegister;
import android.server.data.*;
import android.util.Log;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

// Referenced classes of package android.os:
//            DeadObjectException, ZygoteInit, Process, Debug, 
//            ServiceManager, IStatisticsService, Build, SystemProperties, 
//            IBinder

public class RuntimeInit
{
    private static class UncaughtHandler
        implements Thread.UncaughtExceptionHandler
    {

        public void uncaughtException(Thread t, Throwable e)
        {
            Log.e("AndroidRuntime", (new StringBuilder()).append("Uncaught handler: thread ").append(t.getName()).append(" exiting due to uncaught exception").toString());
            RuntimeInit.crash("AndroidRuntime", e);
        }

        private UncaughtHandler()
        {
        }

    }


    public RuntimeInit()
    {
    }

    private static final void commonInit()
    {
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtHandler());
        int hasQwerty = getQwertyKeyboard();
        if(hasQwerty == 1)
            System.setProperty("qwerty", "1");
    }

    private static void invokeStaticMain(String className, String argv[])
        throws ZygoteInit.MethodAndArgsCaller
    {
        Class cl;
        try
        {
            cl = Class.forName(className);
        }
        catch(ClassNotFoundException ex)
        {
            throw new RuntimeException((new StringBuilder()).append("Missing class when invoking static main ").append(className).toString(), ex);
        }
        Method m;
        try
        {
            m = cl.getMethod("main", new Class[] {
                [Ljava/lang/String;
            });
        }
        catch(NoSuchMethodException ex)
        {
            throw new RuntimeException((new StringBuilder()).append("Missing static main on ").append(className).toString(), ex);
        }
        catch(SecurityException ex)
        {
            throw new RuntimeException((new StringBuilder()).append("Problem getting static main on ").append(className).toString(), ex);
        }
        int modifiers = m.getModifiers();
        if(!Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers))
            throw new RuntimeException((new StringBuilder()).append("Main method is not public and static on ").append(className).toString());
        else
            throw new ZygoteInit.MethodAndArgsCaller(m, argv);
    }

    public static final void main(String argv[])
    {
        commonInit();
        finishInit();
    }

    public static final native void finishInit();

    public static final void zygoteInit(String argv[])
        throws ZygoteInit.MethodAndArgsCaller
    {
        commonInit();
        zygoteInitNative();
        int curArg;
        for(curArg = 0; curArg < argv.length; curArg++)
        {
            String arg = argv[curArg];
            if(arg.equals("--"))
            {
                curArg++;
                break;
            }
            if(!arg.startsWith("--"))
                break;
            if(arg.startsWith("--nice-name="))
            {
                String niceName = arg.substring(arg.indexOf('=') + 1);
                Process.setArgV0(niceName);
            }
        }

        if(curArg == argv.length)
        {
            Log.e("AndroidRuntime", "Missing classname argument to RuntimeInit!");
            return;
        } else
        {
            String startClass = argv[curArg++];
            String startArgs[] = new String[argv.length - curArg];
            System.arraycopy(argv, curArg, startArgs, 0, startArgs.length);
            invokeStaticMain(startClass, startArgs);
            return;
        }
    }

    public static final native void zygoteInitNative();

    public static final native int isComputerOn();

    public static final native void turnComputerOn();

    public static native int getQwertyKeyboard();

    public static void crash(String tag, Throwable t)
    {
        boolean reported;
        byte crashData[];
        Throwable t2;
        reported = false;
        if(mApplicationObject == null)
            break MISSING_BLOCK_LABEL_222;
        crashData = null;
        try
        {
            Log.e("AndroidRuntime", Log.getStackTraceString(t));
            crashData = marshallException(tag, t);
            if(crashData == null)
                throw new NullPointerException("Can't marshall crash data");
        }
        // Misplaced declaration of an exception variable
        catch(Throwable t2)
        {
            reported = true;
            Log.e("AndroidRuntime", (new StringBuilder()).append("Error reporting crash: ").append(Log.getStackTraceString(t2)).toString());
        }
        String msg;
        IActivityManager am;
        msg = t.getMessage();
        if(msg == null)
            msg = t.toString();
        am = ActivityManagerNative.getDefault();
        int res = am.handleApplicationError(mApplicationObject, 0, tag, msg, t.toString(), crashData);
        reported = true;
        if(res != 1)
            break MISSING_BLOCK_LABEL_147;
        Debug.waitForDebugger();
        Process.killProcess(Process.myPid());
        System.exit(10);
        return;
        DeadObjectException e;
        e;
        Process.killProcess(Process.myPid());
        System.exit(10);
        break MISSING_BLOCK_LABEL_222;
        msg;
        Log.e("AndroidRuntime", (new StringBuilder()).append("Error reporting crash: ").append(Log.getStackTraceString(msg)).toString());
        Process.killProcess(Process.myPid());
        System.exit(10);
        break MISSING_BLOCK_LABEL_222;
        Exception exception;
        exception;
        Process.killProcess(Process.myPid());
        System.exit(10);
        throw exception;
        if(!reported)
        {
            if(mApplicationObject == null)
                Log.e("AndroidRuntime", "*** EXCEPTION IN SYSTEM PROCESS.  System will crash.");
            reportException(tag, t);
            Process.killProcess(Process.myPid());
            System.exit(10);
        }
        return;
    }

    public static void reportException(String tag, Throwable t)
    {
        try
        {
            Log.e("AndroidRuntime", Log.getStackTraceString(t));
            byte crashData[] = marshallException(tag, t);
            if(crashData == null)
                throw new NullPointerException("Can't marshall crash data");
            IStatisticsService stats = IStatisticsService.Stub.asInterface(ServiceManager.getService("statistics"));
            stats.reportCrash(crashData);
        }
        catch(Throwable t2)
        {
            Log.e("AndroidRuntime", (new StringBuilder()).append("Error reporting exception: ").append(Log.getStackTraceString(t2)).toString());
        }
    }

    public static byte[] marshallException(String tag, Throwable t)
    {
        Build build = Build.getInstance();
        BuildData buildData = new BuildData(build.getLabel(), build.getChangelist(), build.getTime());
        ThrowableData throwableData = new ThrowableData(t);
        CrashData crashData = new CrashData(getImei(), tag, buildData, throwableData);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);
        try
        {
            crashData.write(dout);
            dout.close();
        }
        catch(IOException e)
        {
            return null;
        }
        return bout.toByteArray();
    }

    private static String getImei()
    {
        return SystemProperties.get("gsm.imei");
    }

    public static final void setApplicationObject(IBinder app)
    {
        mApplicationObject = app;
    }

    private static final String TAG = "AndroidRuntime";
    private static IBinder mApplicationObject;

    static 
    {
        Debug.enableTopAllocCounts(0);
        DdmRegister.registerHandlers();
    }
}

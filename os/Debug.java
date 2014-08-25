// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Debug.java

package android.os;

import android.ddmc.*;
import java.io.*;

// Referenced classes of package android.os:
//            VMDebug

public final class Debug
{

    private Debug()
    {
    }

    public static void waitForDebugger()
    {
label0:
        {
            if(isDebuggerConnected())
                return;
            System.out.println("Sending WAIT chunk");
            byte data[] = {
                0
            };
            Chunk waitChunk = new Chunk(ChunkHandler.type("WAIT"), data, 0, 1);
            DdmServer.sendChunk(waitChunk);
            mWaiting = true;
            while(!isDebuggerConnected()) 
                try
                {
                    Thread.sleep(200L);
                }
                catch(InterruptedException ie) { }
            mWaiting = false;
            System.out.println("Debugger has connected");
            long delta;
            do
            {
                delta = VMDebug.lastDebuggerActivity();
                if(delta < 0L)
                {
                    System.out.println("debugger detached?");
                    break label0;
                }
                if(delta >= 1300L)
                    break;
                System.out.println("waiting for debugger to settle...");
                try
                {
                    Thread.sleep(200L);
                }
                catch(InterruptedException ie) { }
            } while(true);
            System.out.println((new StringBuilder()).append("debugger has settled (").append(delta).append(")").toString());
        }
    }

    public static boolean waitingForDebugger()
    {
        return mWaiting;
    }

    public static boolean isDebuggerConnected()
    {
        return VMDebug.isDebuggerConnected();
    }

    public static void changeDebugPort(int i)
    {
    }

    public static void enableTopAllocCounts(int depth)
    {
        VMDebug.enableTopAllocCounts(depth);
    }

    public static void startNativeTracing()
    {
label0:
        {
            PrintWriter outStream = null;
            try
            {
                FileOutputStream fos = new FileOutputStream("/sys/qemu_trace/state");
                outStream = new PrintWriter(new OutputStreamWriter(fos));
                outStream.println("1");
            }
            catch(Exception e)
            {
                if(outStream != null)
                    outStream.close();
                break label0;
            }
            finally
            {
                if(outStream != null)
                    outStream.close();
                throw exception;
            }
            if(outStream != null)
                outStream.close();
            break label0;
        }
    }

    public static void stopNativeTracing()
    {
label0:
        {
            PrintWriter outStream = null;
            try
            {
                FileOutputStream fos = new FileOutputStream("/sys/qemu_trace/state");
                outStream = new PrintWriter(new OutputStreamWriter(fos));
                outStream.println("0");
            }
            catch(Exception e)
            {
                if(outStream != null)
                    outStream.close();
                break label0;
            }
            finally
            {
                if(outStream != null)
                    outStream.close();
                throw exception;
            }
            if(outStream != null)
                outStream.close();
            break label0;
        }
    }

    public static void startMethodTracing()
    {
        startMethodTracing("/tmp/dmtrace.data", "/tmp/dmtrace.key", 0x800000, 0);
    }

    public static void startMethodTracing(String traceName)
    {
        startMethodTracing(traceName, 0x800000, 0);
    }

    public static void startMethodTracing(String traceName, int bufferSize)
    {
        startMethodTracing(traceName, bufferSize, 0);
    }

    public static void startMethodTracing(String traceName, int bufferSize, int flags)
    {
        String pathName = traceName;
        if(pathName.charAt(0) != '/')
            pathName = (new StringBuilder()).append("/tmp/").append(pathName).toString();
        startMethodTracing((new StringBuilder()).append(pathName).append(".data").toString(), (new StringBuilder()).append(pathName).append(".key").toString(), bufferSize, flags);
    }

    private static void startMethodTracing(String dataFileName, String keyFileName, int bufferSize, int flags)
    {
        VMDebug.startMethodTracing(dataFileName, keyFileName, bufferSize, flags);
    }

    public static void stopMethodTracing()
    {
        VMDebug.stopMethodTracing();
    }

    private static void startGC()
    {
    }

    private static void startClassPrep()
    {
    }

    public static void startAllocCounting()
    {
        VMDebug.startAllocCounting();
    }

    public static void stopAllocCounting()
    {
        VMDebug.stopAllocCounting();
    }

    public static int getGlobalAllocCount()
    {
        return VMDebug.getAllocCount(1);
    }

    public static int getGlobalAllocSize()
    {
        return VMDebug.getAllocCount(2);
    }

    public static int getGlobalFreedCount()
    {
        return VMDebug.getAllocCount(4);
    }

    public static int getGlobalFreedSize()
    {
        return VMDebug.getAllocCount(8);
    }

    public static int getGlobalGcInvocationCount()
    {
        return VMDebug.getAllocCount(16);
    }

    public static int getThreadAllocCount()
    {
        return VMDebug.getAllocCount(0x10000);
    }

    public static int getThreadAllocSize()
    {
        return VMDebug.getAllocCount(0x20000);
    }

    public static int getThreadGcInvocationCount()
    {
        return VMDebug.getAllocCount(0x100000);
    }

    public static void resetGlobalAllocCount()
    {
        VMDebug.resetAllocCount(1);
    }

    public static void resetGlobalAllocSize()
    {
        VMDebug.resetAllocCount(2);
    }

    public static void resetGlobalFreedCount()
    {
        VMDebug.resetAllocCount(4);
    }

    public static void resetGlobalFreedSize()
    {
        VMDebug.resetAllocCount(8);
    }

    public static void resetGlobalGcInvocationCount()
    {
        VMDebug.resetAllocCount(16);
    }

    public static void resetThreadAllocCount()
    {
        VMDebug.resetAllocCount(0x10000);
    }

    public static void resetThreadAllocSize()
    {
        VMDebug.resetAllocCount(0x20000);
    }

    public static void resetThreadGcInvocationCount()
    {
        VMDebug.resetAllocCount(0x100000);
    }

    public static void resetAllCounts()
    {
        VMDebug.resetAllocCount(-1);
    }

    public static native long getNativeHeapSize();

    public static native long getNativeHeapAllocatedSize();

    public static native long getNativeHeapFreeSize();

    public static native void getDirtyPages(int ai[]);

    public static void printLoadedClasses(int flags)
    {
        VMDebug.printLoadedClasses(flags);
    }

    public static final int TRACE_COUNT_ALLOCS = 1;
    public static final int SHOW_FULL_DETAIL = 1;
    public static final int SHOW_CLASSLOADER = 2;
    public static final int SHOW_INITIALIZED = 4;
    private static volatile boolean mWaiting = false;
    private static final int MIN_DEBUGGER_IDLE = 1300;
    private static final int SPIN_DELAY = 200;
    private static final String SYSFS_QEMU_TRACE_STATE = "/sys/qemu_trace/state";

}

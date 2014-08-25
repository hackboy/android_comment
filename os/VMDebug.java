// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   VMDebug.java

package android.os;


final class VMDebug
{

    private VMDebug()
    {
    }

    static native long lastDebuggerActivity();

    static native boolean isDebuggerConnected();

    static native void enableTopAllocCounts(int i);

    static native void startMethodTracing(String s, String s1, int i, int j);

    static native void stopMethodTracing();

    static native void startAllocCounting();

    static native void stopAllocCounting();

    static native int getAllocCount(int i);

    static native void resetAllocCount(int i);

    static native void printLoadedClasses(int i);

    static native void printThis(Object obj, int i, int j);

    private static final int KIND_ALLOCATED_OBJECTS = 1;
    private static final int KIND_ALLOCATED_BYTES = 2;
    private static final int KIND_FREED_OBJECTS = 4;
    private static final int KIND_FREED_BYTES = 8;
    private static final int KIND_GC_INVOCATIONS = 16;
    static final int KIND_GLOBAL_ALLOCATED_OBJECTS = 1;
    static final int KIND_GLOBAL_ALLOCATED_BYTES = 2;
    static final int KIND_GLOBAL_FREED_OBJECTS = 4;
    static final int KIND_GLOBAL_FREED_BYTES = 8;
    static final int KIND_GLOBAL_GC_INVOCATIONS = 16;
    static final int KIND_THREAD_ALLOCATED_OBJECTS = 0x10000;
    static final int KIND_THREAD_ALLOCATED_BYTES = 0x20000;
    static final int KIND_THREAD_FREED_OBJECTS = 0x40000;
    static final int KIND_THREAD_FREED_BYTES = 0x80000;
    static final int KIND_THREAD_GC_INVOCATIONS = 0x100000;
    static final int KIND_ALL_COUNTS = -1;
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FileObserver.java

package android.os;

import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.HashMap;

// Referenced classes of package android.os:
//            RuntimeInit

public abstract class FileObserver
{   
    //果然还是要开启一个新的线程
    private static class ObserverThread extends Thread{

        public void run()
        {
            observe(m_fd);
        }

        public int startWatching(String path, int mask, FileObserver observer)
        {
            int wfd = startWatching(m_fd, path, mask);
            Integer i = new Integer(wfd);
            if(wfd >= 0)
                synchronized(m_observers)
                {
                    m_observers.put(i, new WeakReference(observer));
                }
            return i.intValue();
        }

        public void stopWatching(int descriptor)
        {
            stopWatching(m_fd, descriptor);
        }

        public void onEvent(int wfd, int mask, String path)
        {
            synchronized(m_observers)
            {
                WeakReference weak = (WeakReference)m_observers.get(Integer.valueOf(wfd));
                FileObserver observer = (FileObserver)weak.get();
                if(observer != null)
                    try
                    {
                        observer.onEvent(mask, path);
                    }
                    catch(Throwable ex)
                    {
                        Log.e("FileObserver", (new StringBuilder()).append("Unhandled exception ").append(ex.toString()).append(" (returned by observer ").append(observer).append(")").toString(), ex);
                        RuntimeInit.crash("FileObserver", ex);
                    }
                else
                    m_observers.remove(Integer.valueOf(wfd));
            }
        }
        //一系列的本地方法
        private native int init();

        private native void observe(int i);

        private native int startWatching(int i, String s, int j);

        private native void stopWatching(int i, int j);

        private HashMap m_observers;
        private int m_fd;

        public ObserverThread()
        {
            super("FileObserver");
            m_observers = new HashMap();
            m_fd = init();
        }
    }


    public FileObserver(String path)
    {
        this(path, 4095);
    }

    public FileObserver(String path, int mask)
    {
        m_path = path;
        m_mask = mask;
        m_descriptor = Integer.valueOf(-1);
    }

    protected void finalize()
    {
        stopWatching();
    }

    public void startWatching()
    {
        if(m_descriptor.intValue() < 0)
            m_descriptor = Integer.valueOf(s_observerThread.startWatching(m_path, m_mask, this));
    }

    public void stopWatching()
    {
        if(m_descriptor.intValue() >= 0)
        {
            s_observerThread.stopWatching(m_descriptor.intValue());
            m_descriptor = Integer.valueOf(-1);
        }
    }

    public abstract void onEvent(int i, String s);

    public static final int ACCESS = 1;
    public static final int MODIFY = 2;
    public static final int ATTRIB = 4;
    public static final int CLOSE_WRITE = 8;
    public static final int CLOSE_NOWRITE = 16;
    public static final int OPEN = 32;
    public static final int MOVED_FROM = 64;
    public static final int MOVED_TO = 128;
    public static final int CREATE = 256;
    public static final int DELETE = 512;
    public static final int DELETE_SELF = 1024;
    public static final int MOVE_SELF = 2048;
    public static final int ALL_EVENTS = 4095;
    private static final String LOG_TAG = "FileObserver";
    private static ObserverThread s_observerThread;
    private String m_path;
    private Integer m_descriptor;
    private int m_mask;

    static 
    {
        s_observerThread = new ObserverThread();
        s_observerThread.start();
    }
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UsbListener.java

package android.os;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;
import java.io.*;

// Referenced classes of package android.os:
//            UsbService, SystemClock, SystemProperties, Environment

final class UsbListener
    implements Runnable
{

    UsbListener(UsbService service)
    {
        mService = service;
    }

    private void handleEvent(String event)
    {
        log((new StringBuilder()).append("UsbListener.handleEvent ").append(event).toString());
        int colonIndex = event.indexOf(':');
        String path = colonIndex <= 0 ? null : event.substring(colonIndex + 1);
        if(event.equals("ums_enabled"))
            mUmsEnabled = true;
        else
        if(event.equals("ums_disabled"))
            mUmsEnabled = false;
        else
        if(event.equals("ums_connected"))
        {
            mUmsConnected = true;
            mService.notifyUmsConnected();
        } else
        if(event.equals("ums_disconnected"))
        {
            mUmsConnected = false;
            mService.notifyUmsDisconnected();
        } else
        if(event.startsWith("media_removed:"))
            mService.notifyMediaRemoved(path);
        else
        if(event.startsWith("media_unmounted:"))
            mService.notifyMediaUnmounted(path);
        else
        if(event.startsWith("media_mounted:"))
            mService.notifyMediaMounted(path, false);
        else
        if(event.startsWith("media_mounted_ro:"))
            mService.notifyMediaMounted(path, true);
        else
        if(event.startsWith("media_shared:"))
            mService.notifyMediaShared(path);
        else
        if(event.startsWith("media_bad_removal:"))
        {
            mService.notifyMediaBadRemoval(path);
            mService.notifyMediaEject(path);
        } else
        if(event.startsWith("request_eject:"))
            mService.notifyMediaEject(path);
    }

    private void writeCommand(String command)
    {
        writeCommand2(command, null);
    }

    private void writeCommand2(String command, String argument)
    {
_L2:
        UsbListener usblistener = this;
        JVM INSTR monitorenter ;
        if(mOutputStream == null)
            break MISSING_BLOCK_LABEL_60;
        try
        {
            if(argument != null)
            {
                StringBuilder builder = new StringBuilder(command);
                builder.append(argument);
                command = builder.toString();
            }
            mOutputStream.write(command.getBytes());
        }
        catch(IOException ex)
        {
            log("IOException in writeCommand");
            break MISSING_BLOCK_LABEL_72;
        }
        return;
        Exception exception;
        exception;
        throw exception;
        SystemClock.sleep(2000L);
        if(true) goto _L2; else goto _L1
_L1:
    }

    private void listenToSocket()
    {
        LocalSocket socket = null;
        try
        {
            socket = new LocalSocket();
            LocalSocketAddress address = new LocalSocketAddress("usbd");
            socket.connect(address);
            InputStream inputStream = socket.getInputStream();
            mOutputStream = socket.getOutputStream();
            if(!mRestoredSettings)
            {
                mService.restorePersistentSettings();
                mRestoredSettings = true;
            }
            byte buffer[] = new byte[100];
            writeCommand("send_status");
            do
            {
                int count = inputStream.read(buffer);
                if(count < 0)
                    break;
                int start = 0;
                int i = 0;
                while(i < count) 
                {
                    if(buffer[i] == 0)
                    {
                        String event = new String(buffer, start, i - start);
                        handleEvent(event);
                        start = i + 1;
                    }
                    i++;
                }
            } while(true);
        }
        catch(IOException ex) { }
        synchronized(this)
        {
            if(mOutputStream != null)
            {
                try
                {
                    mOutputStream.close();
                }
                catch(IOException e)
                {
                    log("IOException closing output stream");
                }
                mOutputStream = null;
            }
        }
        try
        {
            if(socket != null)
                socket.close();
        }
        catch(IOException ex)
        {
            log("IOException closing socket");
        }
        SystemClock.sleep(2000L);
    }

    public void run()
    {
        String product = SystemProperties.get("ro.build.product");
        if(product == null || product.length() == 0)
        {
            mService.notifyMediaMounted(Environment.getExternalStorageDirectory().getPath(), false);
            return;
        }
        try
        {
            do
                listenToSocket();
            while(true);
        }
        catch(Throwable t)
        {
            error((new StringBuilder()).append("Fatal error ").append(t).append(" in UsbListener thread!").toString());
        }
    }

    boolean getMassStorageEnabled()
    {
        return mUmsEnabled;
    }

    void setMassStorageEnabled(boolean enable)
    {
        writeCommand(enable ? "enable_ums" : "disable_ums");
    }

    boolean getMassStorageConnected()
    {
        return mUmsConnected;
    }

    public void disconnectMassStorage()
    {
        writeCommand("disconnect_ums");
    }

    public void mountMedia(String mountPoint)
    {
        writeCommand2("mount_media:", mountPoint);
    }

    public void ejectMedia(String mountPoint)
    {
        writeCommand2("eject_media:", mountPoint);
    }

    private void log(String msg)
    {
        Log.d("usb", (new StringBuilder()).append("[UsbListener] ").append(msg).toString());
    }

    private void error(String msg)
    {
        Log.e("usb", (new StringBuilder()).append("[UsbListener] ").append(msg).toString());
    }

    private static final String USBD_SOCKET = "usbd";
    private static final String USBD_ENABLE_UMS = "enable_ums";
    private static final String USBD_DISABLE_UMS = "disable_ums";
    private static final String USBD_SEND_STATUS = "send_status";
    private static final String USBD_DISCONNECT_UMS = "disconnect_ums";
    private static final String USBD_MOUNT_MEDIA = "mount_media:";
    private static final String USBD_EJECT_MEDIA = "eject_media:";
    private static final String USBD_UMS_ENABLED = "ums_enabled";
    private static final String USBD_UMS_DISABLED = "ums_disabled";
    private static final String USBD_UMS_CONNECTED = "ums_connected";
    private static final String USBD_UMS_DISCONNECTED = "ums_disconnected";
    private static final String USBD_MEDIA_REMOVED = "media_removed:";
    private static final String USBD_MEDIA_UNMOUNTED = "media_unmounted:";
    private static final String USBD_MEDIA_MOUNTED = "media_mounted:";
    private static final String USBD_MEDIA_MOUNTED_READ_ONLY = "media_mounted_ro:";
    private static final String USBD_MEDIA_SHARED = "media_shared:";
    private static final String USBD_MEDIA_BAD_REMOVAL = "media_bad_removal:";
    private static final String USBD_REQUEST_EJECT = "request_eject:";
    private UsbService mService;
    private boolean mRestoredSettings;
    private OutputStream mOutputStream;
    private boolean mUmsEnabled;
    private boolean mUmsConnected;
}

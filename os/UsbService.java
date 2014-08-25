// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UsbService.java

package android.os;

import android.content.Context;
import android.content.Intent;
import android.net.ContentURI;
import android.provider.Settings;

// Referenced classes of package android.os:
//            UsbListener, DeadObjectException, IUsb

public class UsbService extends IUsb.Stub
{

    public UsbService(Context context)
    {
        mContext = context;
        mListener = new UsbListener(this);
        Thread thread = new Thread(mListener, android/os/UsbListener.getName());
        thread.start();
    }

    void restorePersistentSettings()
    {
        int enable = android.provider.Settings.System.getInt(mContext.getContentResolver(), "usb_mass_storage_enabled", 0);
        mListener.setMassStorageEnabled(enable != 0);
    }

    public boolean getMassStorageEnabled()
        throws DeadObjectException
    {
        return mListener.getMassStorageEnabled();
    }

    public void setMassStorageEnabled(boolean enable)
        throws DeadObjectException
    {
        mListener.setMassStorageEnabled(enable);
        android.provider.Settings.System.putInt(mContext.getContentResolver(), "usb_mass_storage_enabled", enable ? 1 : 0);
    }

    public boolean getMassStorageConnected()
        throws DeadObjectException
    {
        return mListener.getMassStorageConnected();
    }

    public void disconnectMassStorage()
        throws DeadObjectException
    {
        mListener.disconnectMassStorage();
    }

    public void mountMedia(String mountPath)
        throws DeadObjectException
    {
        mListener.mountMedia(mountPath);
    }

    public void unmountMedia(String mountPath)
        throws DeadObjectException
    {
        notifyMediaEject(mountPath);
        mListener.ejectMedia(mountPath);
    }

    void notifyUmsConnected()
    {
        Intent intent = new Intent("android.intent.action.UMS_CONNECTED");
        mContext.broadcastIntent(intent);
    }

    void notifyUmsDisconnected()
    {
        Intent intent = new Intent("android.intent.action.UMS_DISCONNECTED");
        mContext.broadcastIntent(intent);
    }

    void notifyMediaRemoved(String path)
    {
        Intent intent = new Intent("android.intent.action.MEDIA_REMOVED", ContentURI.create((new StringBuilder()).append("file://").append(path).toString()));
        mContext.broadcastIntent(intent);
    }

    void notifyMediaUnmounted(String path)
    {
        Intent intent = new Intent("android.intent.action.MEDIA_UNMOUNTED", ContentURI.create((new StringBuilder()).append("file://").append(path).toString()));
        mContext.broadcastIntent(intent);
    }

    void notifyMediaMounted(String path, boolean readOnly)
    {
        Intent intent = new Intent("android.intent.action.MEDIA_MOUNTED", ContentURI.create((new StringBuilder()).append("file://").append(path).toString()));
        intent.putExtra("read-only", new Boolean(readOnly));
        mContext.broadcastIntent(intent);
    }

    void notifyMediaShared(String path)
    {
        Intent intent = new Intent("android.intent.action.MEDIA_SHARED", ContentURI.create((new StringBuilder()).append("file://").append(path).toString()));
        mContext.broadcastIntent(intent);
    }

    void notifyMediaBadRemoval(String path)
    {
        Intent intent = new Intent("android.intent.action.MEDIA_BAD_REMOVAL", ContentURI.create((new StringBuilder()).append("file://").append(path).toString()));
        mContext.broadcastIntent(intent);
    }

    void notifyMediaEject(String path)
    {
        Intent intent = new Intent("android.intent.action.MEDIA_EJECT", ContentURI.create((new StringBuilder()).append("file://").append(path).toString()));
        mContext.broadcastIntent(intent);
    }

    private Context mContext;
    private UsbListener mListener;
}

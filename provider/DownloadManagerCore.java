// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DownloadManagerCore.java

package android.webkit;

import android.content.*;
import android.media.IMediaScannerService;
import android.os.*;
import android.util.Log;
import java.io.File;
import java.util.*;

// Referenced classes of package android.webkit:
//            LoadListener, FrameLoader, URLUtil, CallbackProxy

class DownloadManagerCore extends Handler
{
    private static class ScanFile
    {

        String path;
        String mimeType;

        ScanFile(String path, String mimeType)
        {
            this.path = path;
            this.mimeType = mimeType;
        }
    }


    DownloadManagerCore(Context context, CallbackProxy proxy)
    {
        mFilesToScan = new ArrayList();
        mConnection = new ServiceConnection() {

            public void onServiceConnected(ComponentName className, IBinder service)
            {
                Log.d("DownloadManager", "onServiceConnected()...");
                Log.d("DownloadManager", (new StringBuilder()).append("  className: ").append(className).toString());
                Log.d("DownloadManager", (new StringBuilder()).append("  service: ").append(service).toString());
                mMediaScannerService = android.media.IMediaScannerService.Stub.asInterface(service);
                if(mMediaScannerService != null)
                    scanFiles();
            }

            public void onServiceDisconnected(ComponentName className)
            {
                Log.d("DownloadManager", "onServiceDisconnected()...");
                mMediaScannerService = null;
            }

            final DownloadManagerCore this$0;

            
            {
                this$0 = DownloadManagerCore.this;
                super();
            }
        };
        mContext = context;
        mCallbackProxy = proxy;
        mPendingDownloads = new HashMap();
    }

    public void handleMessage(Message msg)
    {
        switch(msg.what)
        {
        case 1: // '\001'
            String filepath = (String)msg.obj;
            resumeDownload(filepath);
            break;
        }
    }

    String generateSavePath(String url, String suggestedFileName, String mimeType)
    {
        if(suggestedFileName == null)
        {
            suggestedFileName = "downloadfile";
            String urlPath = new String(URLUtil.decode(url.getBytes()));
            if(!urlPath.endsWith("/"))
            {
                int index = urlPath.lastIndexOf('/') + 1;
                if(index > 0)
                    suggestedFileName = urlPath.substring(index);
                index = suggestedFileName.indexOf('?');
                if(index > 0)
                    suggestedFileName = suggestedFileName.substring(0, index);
            }
        }
        File dl;
        if("application/vnd.oma.drm.message".equals(mimeType) || "application/vnd.oma.drm.rights+xml".equals(mimeType) || "application/vnd.oma.drm.rights+wbxml".equals(mimeType))
        {
            dl = Environment.getDrmContentDirectory();
        } else
        {
            File base = Environment.getExternalStorageDirectory();
            if(!base.exists())
            {
                mCallbackProxy.onDownloadFailed(null, 1);
                return null;
            }
            dl = new File((new StringBuilder()).append(base.getPath()).append("/download/").toString());
            dl.mkdir();
        }
        String downloadFilePath = (new StringBuilder()).append(dl.getPath()).append(File.separator).append(suggestedFileName).append("-part").toString();
        return downloadFilePath;
    }

    int pendingDownloads()
    {
        return mPendingDownloads.size();
    }

    void addDownload(LoadListener listener)
    {
        String mimeType = listener.mimeType();
        if(!"application/vnd.oma.drm.message".equals(mimeType) && !"application/vnd.oma.drm.content".equals(mimeType) && !"application/vnd.oma.drm.rights+xml".equals(mimeType) && !"application/vnd.oma.drm.rights+wbxml".equals(mimeType))
        {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setType(mimeType);
            PackageManager pm = mContext.getPackageManager();
            List list = pm.queryIntentActivities(intent, 0x10000);
            if(list.size() == 0)
            {
                mCallbackProxy.onDownloadFailed(null, 2);
                listener.downloadFileCancel();
                return;
            }
        }
        String path = listener.downloadFileName();
        Message cancelMsg = listener.downloadCancelMessage();
        Message suspendMsg = listener.downloadSuspendMessage();
        mCallbackProxy.onDownloadStart(path, mimeType, listener.contentLength(), suspendMsg, cancelMsg);
        mPendingDownloads.put(listener.downloadFileName(), listener);
        if(mMediaScannerService == null)
            mContext.bindService((new Intent()).setClassName("com.google.android.providers.media", "com.google.android.providers.media.MediaScannerService"), null, mConnection, 1);
    }

    int removeDownload(LoadListener listener, boolean canceled)
    {
        mPendingDownloads.remove(listener.downloadFileName());
        if(!canceled)
        {
            String path = listener.downloadFileName();
            if(path.endsWith("-part"))
            {
                String newPath = path.substring(0, path.length() - 5);
                File oldFile = new File(path);
                File newFile = new File(newPath);
                oldFile.renameTo(newFile);
                path = newPath;
            }
            scanFile(path, listener.mimeType());
            mCallbackProxy.onDownloadFinished(listener.downloadFileName());
        }
        return mPendingDownloads.size();
    }

    void suspendDownload(LoadListener listener)
    {
        String path = listener.downloadFileName();
        Message resumeMsg = obtainMessage(1, path);
        mCallbackProxy.onDownloadSuspended(path, resumeMsg);
    }

    void reportSaveError(LoadListener listener)
    {
        suspendDownload(listener);
        mCallbackProxy.onDownloadFailed(null, 4);
    }

    void reportOpenError(LoadListener listener)
    {
        suspendDownload(listener);
        mCallbackProxy.onDownloadFailed(null, 3);
    }

    void resumeDownload(String filepath)
    {
        LoadListener listener = (LoadListener)mPendingDownloads.get(filepath);
        listener.resetCancel();
        FrameLoader fl = new FrameLoader(listener, "ResumeDownload", "GET", false);
        fl.setCacheMode(2);
        HashMap headers = new HashMap();
        long amount = listener.downloadFileSize();
        headers.put("Range", (new StringBuilder()).append("bytes=").append((new Long(amount)).toString()).append('-').toString());
        fl.setHeaders(headers);
        fl.executeLoad();
        String path = listener.downloadFileName();
        Message cancelMsg = listener.downloadCancelMessage();
        Message suspendMsg = listener.downloadSuspendMessage();
        mCallbackProxy.onDownloadStart(path, listener.mimeType(), listener.contentLength(), suspendMsg, cancelMsg);
    }

    private void scanFile(String path, String mimeType)
    {
        mFilesToScan.add(new ScanFile(path, mimeType));
        if(mMediaScannerService != null)
            scanFiles();
    }

    private void scanFiles()
    {
        for(Iterator iterator = mFilesToScan.iterator(); iterator.hasNext();)
        {
            ScanFile file = (ScanFile)iterator.next();
            try
            {
                mMediaScannerService.scanFile(file.path, file.mimeType);
            }
            catch(DeadObjectException e)
            {
                Log.d("DownloadManager", (new StringBuilder()).append("Failed to scan file ").append(file.path).toString());
            }
        }

        mFilesToScan.clear();
        if(mPendingDownloads.size() == 0)
        {
            Log.d("DownloadManager", "releasing mMediaScannerService");
            mContext.unbindService(mConnection);
            mMediaScannerService = null;
        }
    }

    private Context mContext;
    private HashMap mPendingDownloads;
    private CallbackProxy mCallbackProxy;
    static final int MSG_RETRY = 1;
    private static final String DEFAULT_DL_FILENAME = "downloadfile";
    private static final String DEFAULT_DL_SUBDIR = "/download/";
    private static final String TAG = "DownloadManager";
    private IMediaScannerService mMediaScannerService;
    private ArrayList mFilesToScan;
    private ServiceConnection mConnection;



}

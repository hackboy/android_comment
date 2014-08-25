// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Environment.java

package android.os;

import java.io.File;

// Referenced classes of package android.os:
//            SystemProperties

public class Environment
{

    public Environment()
    {
    }

    public static File getRootDirectory()
    {
        return ROOT_DIRECTORY;
    }

    public static File getDataDirectory()
    {
        return DATA_DIRECTORY;
    }

    public static File getExternalStorageDirectory()
    {
        return EXTERNAL_STORAGE_DIRECTORY;
    }

    public static File getDrmContentDirectory()
    {
        return DRM_CONTENT_DIRECTORY;
    }

    public static String getExternalStorageState()
    {
        return SystemProperties.get("EXTERNAL_STORAGE_STATE", "removed");
    }

    static File getDirectory(String variableName, String defaultPath)
    {
        String path = System.getenv(variableName);
        return path != null ? new File(path) : new File(defaultPath);
    }

    private static final File ROOT_DIRECTORY = getDirectory("ANDROID_ROOT", "/system");
    private static final File DATA_DIRECTORY = getDirectory("ANDROID_DATA", "/data");
    private static final File EXTERNAL_STORAGE_DIRECTORY = getDirectory("EXTERNAL_STORAGE", "/sdcard");
    private static final File DRM_CONTENT_DIRECTORY = getDirectory("DRM_CONTENT", "/data/drm/content");
    public static final String MEDIA_REMOVED = "removed";
    public static final String MEDIA_UNMOUNTED = "unmounted";
    public static final String MEDIA_MOUNTED = "mounted";
    public static final String MEDIA_MOUNTED_READ_ONLY = "mounted_ro";
    public static final String MEDIA_SHARED = "shared";
    public static final String MEDIA_BAD_REMOVAL = "bad_removal";

}

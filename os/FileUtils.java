// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FileUtils.java

package android.os;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils
{

    public FileUtils()
    {
    }

    public static native int setPermissions(String s, int i, int j, int k);

    public static native int getPermissions(String s, int ai[]);

    public static boolean copyFile(File srcFile, File destFile)
    {
        InputStream in = new FileInputStream(srcFile);
        OutputStream out = new FileOutputStream(destFile);
        byte buffer[] = new byte[4096];
        int bytesRead;
        while((bytesRead = in.read(buffer)) >= 0) 
            out.write(buffer, 0, bytesRead);
        out.close();
        in.close();
        return true;
        IOException e;
        e;
        return false;
    }

    public static boolean isFilenameSafe(File file)
    {
        return SAFE_FILENAME_PATTERN.matcher(file.getPath()).matches();
    }

    public static final int S_IRWXU = 448;
    public static final int S_IRUSR = 256;
    public static final int S_IWUSR = 128;
    public static final int S_IXUSR = 64;
    public static final int S_IRWXG = 56;
    public static final int S_IRGRP = 32;
    public static final int S_IWGRP = 16;
    public static final int S_IXGRP = 8;
    public static final int S_IRWXO = 7;
    public static final int S_IROTH = 4;
    public static final int S_IWOTH = 2;
    public static final int S_IXOTH = 1;
    private static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("[\\w%+,./=_-]+");

}

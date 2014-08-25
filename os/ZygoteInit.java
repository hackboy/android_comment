// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ZygoteInit.java

package android.os;

import android.dalvik.Zygote;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.util.Log;
import java.io.*;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.ServerSocketChannel;
import java.text.Collator;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.*;

// Referenced classes of package android.os:
//            RuntimeInit

public class ZygoteInit
{
    static class MethodAndArgsCaller extends Exception
        implements Runnable
    {

        public void run()
        {
            try
            {
                mMethod.invoke(null, new Object[] {
                    mArgs
                });
            }
            catch(IllegalAccessException ex)
            {
                throw new RuntimeException(ex);
            }
            catch(InvocationTargetException ex)
            {
                Throwable cause = ex.getCause();
                if(cause instanceof RuntimeException)
                    throw (RuntimeException)cause;
                if(cause instanceof Error)
                    throw (Error)cause;
                else
                    throw new RuntimeException(ex);
            }
        }

        private final Method mMethod;
        private final String mArgs[];

        public MethodAndArgsCaller(Method method, String args[])
        {
            mMethod = method;
            mArgs = args;
        }
    }

    static class Arguments
    {

        private void parseArgs(String args[])
            throws NumberFormatException
        {
            int curArg;
            for(curArg = 0; curArg < args.length; curArg++)
            {
                String arg = args[curArg];
                if(arg.equals("--"))
                {
                    curArg++;
                    break;
                }
                if(arg.startsWith("--allow-disconnect"))
                {
                    allowDisconnect = true;
                    continue;
                }
                if(arg.startsWith("--setuid="))
                {
                    uid = Integer.parseInt(arg.substring(arg.indexOf('=') + 1));
                    continue;
                }
                if(arg.startsWith("--setgid="))
                {
                    gid = Integer.parseInt(arg.substring(arg.indexOf('=') + 1));
                    continue;
                }
                if(!arg.startsWith("--setgroups="))
                    break;
                String params[] = arg.substring(arg.indexOf('=') + 1).split(",");
                gids = new int[params.length];
                for(int i = params.length - 1; i >= 0; i--)
                    gids[i] = Integer.parseInt(params[i]);

            }

            remainingArgs = new String[args.length - curArg];
            System.arraycopy(args, curArg, remainingArgs, 0, remainingArgs.length);
        }

        int uid;
        int gid;
        int gids[];
        boolean allowDisconnect;
        String remainingArgs[];

        Arguments(String args[])
            throws NumberFormatException
        {
            uid = 0;
            gid = 0;
            allowDisconnect = false;
            parseArgs(args);
        }
    }


    private static String[] readArgumentList(BufferedReader r)
        throws IOException
    {
        String s;
        s = r.readLine();
        if(s != null)
            break MISSING_BLOCK_LABEL_19;
        Log.e("Zygote", "Got null argc");
        return null;
        int argc;
        try
        {
            argc = Integer.valueOf(s).intValue();
        }
        catch(NumberFormatException ex)
        {
            Log.e("Zygote", "invalid Zygote wire format: non-int at argc");
            throw new IOException("invalid wire format");
        }
        String result[] = new String[argc];
        for(int i = 0; i < argc; i++)
        {
            result[i] = r.readLine();
            if(result[i] == null)
            {
                Log.e("Zygote", "Got null argument");
                throw new IOException("truncated request");
            }
        }

        return result;
    }

    private static void zygoteCommandLoop()
        throws MethodAndArgsCaller
    {
        int loopCount = 0;
        boolean needsAccept = false;
        boolean allowDisconnect = false;
        do
        {
            if(needsAccept)
                if(allowDisconnect)
                {
                    Log.e("Zygote", "Reaccepting command socket connection");
                    openZygoteSocket();
                    if(sSocket == null)
                    {
                        Log.e("Zygote", "Reaccept failed");
                        return;
                    }
                    needsAccept = false;
                } else
                {
                    Log.i("Zygote", "Command socket closed");
                    return;
                }
            if(loopCount <= 0)
            {
                Runtime.getRuntime().gc();
                loopCount = 10;
            } else
            {
                loopCount--;
            }
            Arguments parsedArgs = null;
            String args[];
            try
            {
                args = readArgumentList(sSocketReader);
            }
            catch(IOException ex)
            {
                Log.i("Zygote", "IOException on command socket", ex);
                needsAccept = true;
                continue;
            }
            if(args == null)
            {
                needsAccept = true;
            } else
            {
                int pid;
                try
                {
                    parsedArgs = new Arguments(args);
                    allowDisconnect = parsedArgs.allowDisconnect;
                    pid = Zygote.fork(parsedArgs.uid, parsedArgs.gid, parsedArgs.gids);
                }
                catch(NumberFormatException ex)
                {
                    Log.e("Zygote", "Non-number where number expected in zygote arg", ex);
                    pid = -1;
                }
                if(pid == 0)
                {
                    try
                    {
                        sSocket.close();
                    }
                    catch(IOException ex)
                    {
                        Log.e("Zygote", "Zygote Child: error closing sockets", ex);
                    }
                    try
                    {
                        sServerSocket.close();
                    }
                    catch(IOException ex)
                    {
                        Log.e("Zygote", "Zygote Child: error closing sockets", ex);
                    }
                    sSocket = null;
                    sServerSocket = null;
                    RuntimeInit.zygoteInit(parsedArgs.remainingArgs);
                    return;
                }
                if(pid < 0)
                {
                    Log.e("Zygote", "Zygote: fork() failed");
                    pid = -1;
                }
                try
                {
                    sSocketOutStream.writeInt(pid);
                }
                catch(IOException ex)
                {
                    Log.e("Zygote", "Error reading from command socket", ex);
                    needsAccept = true;
                }
            }
        } while(true);
    }

    private static void openZygoteSocket()
    {
        if(sSocket != null)
        {
            try
            {
                sSocket.close();
            }
            catch(IOException ex)
            {
                Log.e("Zygote", "Exception while closing old command socket", ex);
            }
            sSocket = null;
        }
        if(sServerSocket == null)
            try
            {
                sServerSocket = new LocalServerSocket("android.zygote");
            }
            catch(IOException ex)
            {
                throw new RuntimeException("Cannot bind to 'android.zygote'", ex);
            }
        try
        {
            sSocket = sServerSocket.accept();
            sSocketOutStream = new DataOutputStream(sSocket.getOutputStream());
            sSocketReader = new BufferedReader(new InputStreamReader(sSocket.getInputStream()), 256);
        }
        catch(IOException ex)
        {
            throw new RuntimeException("IOException during accept()", ex);
        }
    }

    private static void closeZygoteSocket()
    {
        try
        {
            if(sSocket != null)
                sSocket.close();
        }
        catch(IOException ex)
        {
            Log.e("Zygote", "Zygote:  error closing sockets", ex);
        }
        try
        {
            if(sServerSocket != null)
                sServerSocket.close();
        }
        catch(IOException ex)
        {
            Log.e("Zygote", "Zygote:  error closing sockets", ex);
        }
    }

    private static void preloadClasses()
    {
        int errno = setregid(0, 9999);
        if(errno != 0)
            Log.e("Zygote", (new StringBuilder()).append("setregid failed errno ").append(errno).toString());
        errno = setreuid(0, 9999);
        if(errno != 0)
            Log.e("Zygote", (new StringBuilder()).append("setreuid failed errno ").append(errno).toString());
        int numToLoad = sClassToLoadAndInit.length;
        for(int i = 0; i < numToLoad; i++)
            Zygote.initClass(sClassToLoadAndInit[i]);

        int numToInit = sClassNamesInit.length;
        for(int i = 0; i < numToInit; i++)
            try
            {
                Zygote.initClass(Class.forName(sClassNamesInit[i]));
            }
            catch(LinkageError ex)
            {
                Log.w("Zygote", (new StringBuilder()).append("Zygote class init exception ").append(ex).append(" with class ").append(sClassNamesInit[i]).toString());
            }
            catch(ClassNotFoundException ex)
            {
                Log.w("Zygote", (new StringBuilder()).append("Zygote class init exception ").append(ex).append(" with class ").append(sClassNamesInit[i]).toString());
            }

        errno = setreuid(0, 0);
        if(errno != 0)
            Log.e("Zygote", (new StringBuilder()).append("setreuid failed errno ").append(errno).toString());
        errno = setregid(0, 0);
        if(errno != 0)
            Log.e("Zygote", (new StringBuilder()).append("setregid failed errno ").append(errno).toString());
    }

    public static final void main(String argv[])
    {
        try
        {
            openZygoteSocket();
            preloadClasses();
            zygoteCommandLoop();
            closeZygoteSocket();
        }
        catch(MethodAndArgsCaller caller)
        {
            caller.run();
        }
        catch(RuntimeException ex)
        {
            Log.e("Zygote", "Zygote died with exception", ex);
            throw ex;
        }
    }

    private static native int setreuid(int i, int j);

    private static native int setregid(int i, int j);

    private ZygoteInit()
    {
    }

    private static final String TAG = "Zygote";
    private static LocalServerSocket sServerSocket;
    private static LocalSocket sSocket;
    private static BufferedReader sSocketReader;
    private static DataOutputStream sSocketOutStream;
    private static final int GC_LOOP_COUNT = 10;
    private static final boolean ACCEPT_MULTIPLE_CONNECTIONS = false;
    private static final Class sClassToLoadAndInit[] = {
        java/io/BufferedInputStream, java/io/BufferedReader, java/io/BufferedWriter, java/io/ByteArrayOutputStream, java/io/Closeable, java/io/DataInput, java/io/DataOutput, java/io/File, java/io/FileDescriptor, java/io/FileInputStream, 
        java/io/FileNotFoundException, java/io/FileOutputStream, java/io/FilterInputStream, java/io/FilterOutputStream, java/io/Flushable, java/io/IOException, java/io/InputStream, java/io/InputStreamReader, java/io/OutputStream, java/io/OutputStreamWriter, 
        java/io/PrintStream, java/io/PrintWriter, java/io/RandomAccessFile, java/io/Reader, java/io/Serializable, java/io/StringWriter, java/io/SyncFailedException, java/io/Writer, java/lang/Boolean, java/lang/Byte, 
        java/lang/CharSequence, java/lang/Character, java/lang/Class, java/lang/ClassLoader, java/lang/ClassNotFoundException, java/lang/Cloneable, java/lang/Comparable, java/lang/Double, java/lang/Enum, java/lang/Error, 
        java/lang/Exception, java/lang/Float, java/lang/Integer, java/lang/InternalError, java/lang/Iterable, java/lang/LinkageError, java/lang/Long, java/lang/Math, java/lang/NoClassDefFoundError, java/lang/Number, 
        java/lang/Object, java/lang/OutOfMemoryError, java/lang/Runnable, java/lang/RuntimeException, java/lang/Short, java/lang/StackOverflowError, java/lang/StackTraceElement, java/lang/String, java/lang/StringBuffer, java/lang/StringBuilder, 
        java/lang/System, java/lang/Thread, java/lang/ThreadGroup, java/lang/ThreadLocal, java/lang/Throwable, java/lang/UnsatisfiedLinkError, java/lang/VirtualMachineError, java/lang/ref/Reference, java/lang/ref/WeakReference, java/lang/reflect/AccessibleObject, 
        java/lang/reflect/Constructor, java/lang/reflect/Field, java/lang/reflect/Method, java/net/InetAddress, java/net/Socket, java/net/SocketImpl, java/net/SocketImplFactory, java/net/SocketOptions, java/net/URI, java/nio/Buffer, 
        java/nio/ByteBuffer, java/nio/ByteOrder, java/nio/channels/ServerSocketChannel, java/text/Collator, java/util/AbstractCollection, java/util/AbstractList, java/util/AbstractMap, java/util/AbstractSet, java/util/ArrayList, java/util/Arrays, 
        java/util/Calendar, java/util/Collection, java/util/Collections, java/util/Comparator, java/util/Date, java/util/Dictionary, java/util/GregorianCalendar, java/util/HashMap, java/util/HashSet, java/util/Hashtable, 
        java/util/Iterator, java/util/LinkedList, java/util/List, java/util/ListIterator, java/util/Locale, java/util/Map, java/util/Properties, java/util/Queue, java/util/Random, java/util/RandomAccess, 
        java/util/ResourceBundle, java/util/Set, java/util/StringTokenizer, java/util/TimeZone, java/util/Vector, java/util/logging/Logger, java/util/regex/Matcher, java/util/regex/Pattern, java/util/zip/Adler32, java/util/zip/CRC32, 
        java/util/zip/Checksum, java/util/zip/Deflater, java/util/zip/Inflater, java/util/zip/ZipEntry, java/util/zip/ZipFile
    };
    static final String sClassNamesInit[] = {
        "java.net.URIEncoderDecoder", "android.os.Debug", "android.app.Activity", "android.app.Activity$LocalWindowManager", "android.app.ActivityManagerNative", "android.app.ActivityManagerProxy", "android.app.ActivityThread", "android.app.ActivityThread$ActivityRecord", "android.app.ActivityThread$ApplicationThread", "android.app.ActivityThread$H", 
        "android.app.ActivityThread$Idler", "android.app.ActivityThread$PackageInfo", "android.app.Application", "android.app.ApplicationContext$ApplicationContentResolver", "android.app.ApplicationContext$ApplicationPackageManager", "android.app.ApplicationContext", "android.app.ApplicationLoaders", "android.app.ApplicationThreadNative", "android.app.Instrumentation", "android.app.Instrumentation$ActivityGoing", 
        "android.app.Instrumentation$ActivityWaiter", "android.app.Instrumentation$EmptyRunnable", "android.app.Instrumentation$Idler", "android.app.Instrumentation$InstrumentationThread", "android.content.AssetManager", "android.content.AssetManager$AssetInputStream", "android.content.ContentResolver", "android.content.Context", "android.content.Intent", "android.content.Intent$1", 
        "android.content.IntentFilter", "android.content.IntentFilter$1", "android.content.IntentFilter$AuthorityEntry", "android.content.IntentFilter$PathEntry", "android.content.IntentReceiver", "android.content.PackageManager", "android.content.PackageManager$ActivityInfo", "android.content.PackageManager$ActivityInfo$1", "android.content.PackageManagerProxy", "android.content.Resources", 
        "android.content.Resources$Configuration", "android.content.Resources$Configuration$1", "android.content.Resources$StyledAttributes", "android.content.Resources$Theme", "android.content.QueryBuilder", "android.content.StringBlock", "android.content.StringBlock$StyleIDs", "android.content.XmlBlock", "android.content.XmlBlock$Parser", "android.dalvik.DexFile", 
        "android.graphics.Bitmap", "android.graphics.Bitmap$1", "android.graphics.BitmapFactory", "android.graphics.BitmapFactory$CacheEntry", "android.graphics.Camera", "android.graphics.Canvas", "android.graphics.Canvas$EdgeType", "android.graphics.Color", "android.graphics.ColorFilter", "android.graphics.Graphics", 
        "android.graphics.Matrix", "android.graphics.Matrix$ScaleToFit", "android.graphics.NinePatch", "android.graphics.Paint", "android.graphics.Paint$Align", "android.graphics.Paint$Cap", "android.graphics.Paint$FontMetrics", "android.graphics.Paint$Join", "android.graphics.Paint$Style", "android.graphics.Point", 
        "android.graphics.PointF", "android.graphics.PorterDuffColorFilter", "android.graphics.Rect", "android.graphics.Rect$1", "android.graphics.RectF", "android.graphics.Region", "android.graphics.Typeface", "android.graphics.drawable.AnimationDrawable", "android.graphics.drawable.AnimationDrawable$AnimationState", "android.graphics.drawable.BitmapDrawable", 
        "android.graphics.drawable.BitmapDrawable$BitmapState", "android.graphics.drawable.Drawable", "android.graphics.drawable.Drawable$ConstantState", "android.graphics.drawable.DrawableContainer", "android.graphics.drawable.DrawableContainer$DrawableContainerState", "android.graphics.drawable.LevelListDrawable", "android.graphics.drawable.LevelListDrawable$LevelListState", "android.graphics.drawable.NinePatchDrawable", "android.graphics.drawable.NinePatchDrawable$NinePatchState", "android.graphics.drawable.PaintDrawable", 
        "android.graphics.drawable.StateListDrawable", "android.graphics.drawable.StateListDrawable$StateListState", "android.hardware.CameraDevice", "android.hardware.CameraDevice$CaptureParams", "android.media.AudioSystem", "android.media.MediaFile", "android.media.MediaPlayer", "android.media.MediaRecorder", "android.media.MidiFile", "android.net.LocalServerSocket", 
        "android.net.AndroidLocalSocketImplFactory", "android.net.AndroidServerSocketChannel", "android.net.AndroidSocketImpl", "android.net.AndroidSocketImpl$MyServerSocketChannel", "android.net.AndroidSocketImplFactory", "android.net.ContentURI", "android.net.ContentURI$1", "android.net.LocalSocketAddress", "android.net.SSLSocket", "android.net.WebAddress", 
        "android.net.http.EventHandler", "android.net.http.Request", "android.net.http.RequestQueue", "android.net.http.SslCertificate", "android.os.Handler", "android.os.HandlerHelper", "android.os.HandlerThread", "android.os.Message", "android.os.MessageQueue", "android.os.Registrant", 
        "android.os.RegistrantList", "android.os.SystemProperties", "android.os.AsyncResult", "android.os.BinderNative", "android.os.BinderProxy", "android.os.Looper", "android.os.Parcel", "android.os.SystemClock", "android.os.ServiceManager", "android.os.ServiceManagerNative", 
        "android.os.ServiceManagerProxy", "android.pim.EventRecurrence", "android.policy.PhoneWindow", "android.policy.PhoneWindow$DecorView", "android.security.Md5MessageDigest", "android.security.MessageDigest", "android.security.Sha1MessageDigest", "android.test.LaunchPerformanceBase", "android.text.AutoText", "android.text.BoringLayout", 
        "android.text.BoringLayout$Direct", "android.text.BoringLayout$Metrics", "android.text.DynamicLayout", "android.text.DynamicLayout$ChangeWatcher", "android.text.Layout", "android.text.Layout$Alignment", "android.text.Selection", "android.text.SpannableString", "android.text.SpannableStringBuilder", "android.text.SpannableStringInternal", 
        "android.text.SpannedString", "android.text.StaticLayout", "android.text.Styled", "android.text.TextUtils", "android.text.TextUtils$1", "android.text.method.ArrowKeyMovementMethod", "android.text.method.BaseInputMethod", "android.text.method.InputMethod", "android.text.method.MetaKeyInputMethod", "android.text.method.MovementMethod", 
        "android.text.method.MultiTapInputMethod", "android.text.method.QwertyInputMethod", "android.text.method.TextInputMethod", "android.text.method.TextInputMethod$Capitalize", "android.text.method.TransformationMethod", "android.text.style.CharacterStyle", "android.text.style.CharacterStyle$DrawState", "android.text.style.MetricAffectingSpan", "android.text.style.StyleSpan", "android.util.ArrayUtils", 
        "android.util.AttributeSet", "android.util.DateFormat", "android.util.DateUtils", "android.util.DisplayMetrics", "android.util.ExpatXmlPullParser", "android.util.Log", "android.util.PackedIntVector", "android.util.PackedObjectVector", "android.util.ResettableTimeout", "android.util.SparseArray", 
        "android.util.SparseIntArray", "android.util.Time", "android.util.TypedValue", "android.util.XmlUtils", "android.view.Display", "android.view.Gravity", "android.view.IApplicationToken$Stub", "android.view.IWindow$Stub", "android.view.IWindowManager$Stub", "android.view.IWindowManager$Stub$Proxy", 
        "android.view.IWindowSession$Stub", "android.view.IWindowSession$Stub$Proxy", "android.view.Surface", "android.view.Surface$1", "android.view.SurfaceSession", "android.view.View", "android.view.View$1", "android.view.View$AttachInfo", "android.view.View$MeasureSpec", "android.view.ViewGroup", 
        "android.view.ViewGroup$LayoutParams", "android.view.ViewGroup$MarginLayoutParams", "android.view.ViewInflate", "android.view.ViewRoot", "android.view.ViewRoot$W", "android.view.WindowManager", "android.view.WindowManager$LayoutParams", "android.view.WindowManager$LayoutParams$1", "android.view.Window", "android.view.animation.AlphaAnimation", 
        "android.view.animation.AnimationSet", "android.view.animation.AnimationUtils", "android.view.animation.BaseAnimation", "android.view.animation.BaseAnimation$Description", "android.view.animation.EaseInInterpolator", "android.view.animation.EaseOutInterpolator", "android.view.animation.ScaleAnimation", "android.view.animation.Transformation", "android.webkit.AutoCompleteDialog", "android.webkit.BrowserCallback", 
        "android.webkit.BrowserCallbackAdapter", "android.webkit.BrowserFrame", "android.webkit.ByteArrayBuilder", "android.webkit.CacheLoader", "android.webkit.CacheManager", "android.webkit.CacheSyncManager", "android.webkit.CookieManager", "android.webkit.CookieSyncManager", "android.webkit.DataLoader", "android.webkit.DownloadManagerCore", 
        "android.webkit.DummyMovementMethod", "android.webkit.EnterEnabledEditText", "android.webkit.FileLoader", "android.webkit.FrameLoader", "android.webkit.HttpDateTime", "android.webkit.JWebCoreJavaBridge", "android.webkit.JsDialogHandlerInterface", "android.webkit.LoadListener", "android.webkit.Network", "android.webkit.PerfChecker", 
        "android.webkit.SpannedPassword", "android.webkit.StreamLoader", "android.webkit.URLUtil", "android.webkit.WebBackForwardList", "android.webkit.WebHistoryItem", "android.webkit.WebIconDatabase", "android.webkit.WebSettings", "android.webkit.WebSyncManager", "android.webkit.WebView", "android.webkit.WebViewCore", 
        "android.widget.AbsListView", "android.widget.AbsListView$MyContentObserver", "android.widget.AbsListView$RecycleBin", "android.widget.AbsSpinner", "android.widget.AbsSpinner$MyContentObserver", "android.widget.AbsSpinner$RecycleBin", "android.widget.AdapterView", "android.widget.AdapterView$SelectionNotifier", "android.widget.AnalogClock", "android.widget.AnalogClock$1", 
        "android.widget.ArrayAdapter", "android.widget.BaseAdapter", "android.widget.EditText", "android.widget.FrameLayout", "android.widget.Gallery", "android.widget.Gallery$LayoutParams", "android.widget.GridView", "android.widget.ImageButton", "android.widget.ImageView", "android.widget.ImageView$ScaleType", 
        "android.widget.LinearLayout", "android.widget.LinearLayout$LayoutParams", "android.widget.RelativeLayout", "android.widget.RelativeLayout$LayoutParams", "android.widget.ScrollBarDrawable", "android.widget.ScrollBarDrawable$CircularCapsDrawable", "android.widget.TextView", "android.widget.TextView$Blink", "android.widget.TextView$BufferType", "android.widget.TextView$ChangeWatcher", 
        "android.widget.ViewAnimator", "android.widget.ViewSwitcher", "android.widget.WidgetInflate"
    };
    private static final int UNPRIVILEGED_UID = 9999;
    private static final int UNPRIVILEGED_GID = 9999;
    private static final int ROOT_UID = 0;
    private static final int ROOT_GID = 0;

}

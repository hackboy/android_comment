// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TimeUtils.java

package android.util;

import android.content.Resources;
import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

// Referenced classes of package android.util:
//            XmlUtils, Log

public class TimeUtils
{

    public TimeUtils()
    {
    }

    public static TimeZone getTimeZone(int offset, boolean dst, long when, String country)
    {
        TimeZone best;
        XmlPullParser parser;
        Date d;
        TimeZone current;
        String currentName;
        int currentOffset;
        boolean currentDst;
        if(country == null)
            return null;
        best = null;
        Resources r = Resources.getSystem();
        parser = r.getXml(0x1070004);
        AttributeSet attrs = XmlUtils.getAttributeSet(parser);
        d = new Date(when);
        current = TimeZone.getDefault();
        currentName = current.getID();
        currentOffset = current.getOffset(when);
        currentDst = current.inDaylightTime(d);
        XmlUtils.beginDocument(parser, "timezones");
_L2:
        String maybe;
        String code;
        do
        {
            XmlUtils.nextElement(parser);
            String element = parser.getName();
            if(element == null || !element.equals("timezone"))
                break MISSING_BLOCK_LABEL_246;
            code = parser.getAttributeValue(null, "code");
        } while(!country.equals(code) || parser.next() != 4);
        maybe = parser.getText();
        if(maybe.equals(currentName) && currentOffset == offset && currentDst == dst)
            return current;
        if(best != null) goto _L2; else goto _L1
_L1:
        TimeZone tz = TimeZone.getTimeZone(maybe);
        if(tz.getOffset(when) == offset && tz.inDaylightTime(d) == dst)
            best = tz;
          goto _L2
        XmlPullParserException e;
        e;
        Log.e("TimeUtils", "Got exception while getting preferred time zone.", e);
        break MISSING_BLOCK_LABEL_246;
        e;
        Log.e("TimeUtils", "Got exception while getting preferred time zone.", e);
        return best;
    }
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DateUtils.java

package android.util;

import android.content.Resources;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

// Referenced classes of package android.util:
//            DateFormat, Log, XmlUtils, AttributeSet

public class DateUtils
{

    public DateUtils()
    {
    }

    public static String getDayOfWeekStr(int dayOfWeek)
    {
        return getDayOfWeekStr(dayOfWeek, true);
    }

    public static String getDayOfWeekStr(int dayOfWeek, boolean abbrev)
    {
        if(null == sDaysLong)
            init();
        String list[] = abbrev ? sDaysShort : sDaysLong;
        return list[dayOfWeek - 1];
    }

    public static String getAMPMStr(int ampm, boolean abbrev)
    {
        if(null == sDaysLong)
            init();
        String list[] = abbrev ? sAMPMShort : sAMPMLong;
        return list[ampm];
    }

    public static String getMonthStr(int month)
    {
        return getMonthStr(month, true);
    }

    public static String getMonthStr(int month, boolean abbrev)
    {
        if(null == sDaysLong)
            init();
        String list[] = abbrev ? sMonthsShort : sMonthsLong;
        return list[month - 0];
    }

    public static CharSequence getRelativeTimeSpanString(long startTime)
    {
        return getRelativeTimeSpanString(startTime, System.currentTimeMillis(), 60000L);
    }

    public static CharSequence getRelativeTimeSpanString(long time, long now, long minResolution)
    {
        long duration = Math.abs(now - time);
        if(duration < 60000L && minResolution < 60000L)
            return pluralizedSpan(duration / 1000L, "second", "seconds");
        if(duration < 0x36ee80L && minResolution < 0x36ee80L)
            return pluralizedSpan(duration / 60000L, "minute", "minutes");
        if(duration < 0x5265c00L && minResolution < 0x5265c00L)
            return pluralizedSpan(duration / 0x36ee80L, "hour", "hours");
        if(duration < 0x240c8400L && minResolution < 0x240c8400L)
            return daysDurationString(time, now);
        else
            return dateString(time);
    }

    private static final String pluralizedSpan(long count, String singular, String plural)
    {
        StringBuilder s = new StringBuilder();
        s.append(count);
        s.append(' ');
        s.append(count != 0L && count <= 1L ? singular : plural);
        s.append(" ago");
        return s.toString();
    }

    private static final String daysDurationString(long startTime, long endTime)
    {
        Calendar c = new GregorianCalendar();
        long hours = Math.abs(endTime - startTime) / 0x36ee80L;
        c.setTimeInMillis(startTime);
        if(hours >= 24L && hours <= 48L)
            return (new StringBuilder()).append("Yesterday, ").append(timeString(c)).toString();
        else
            return (new StringBuilder()).append(getDayOfWeekStr(c.get(7))).append(", ").append(timeString(c)).toString();
    }

    public static final CharSequence timeString(Calendar c)
    {
        if(null == sDaysLong)
            init();
        return DateFormat.format(sTimeFormat, c);
    }

    public static final CharSequence dateString(long startTime)
    {
        if(null == sDaysLong)
            init();
        return DateFormat.format(sClockFormat, startTime);
    }

    private static final void init()
    {
        try
        {
            load_data();
        }
        catch(XmlPullParserException e)
        {
            Log.e("DateUtils", "Got execption while loading data.", e);
        }
        catch(IOException e)
        {
            Log.e("DateUtils", "Got execption while loading data.", e);
        }
    }

    private static final void load_data()
        throws XmlPullParserException, IOException
    {
        XmlPullParser parser;
label0:
        {
            Resources r = Resources.getSystem();
            parser = r.getXml(0x1070002);
            AttributeSet attrs = XmlUtils.getAttributeSet(parser);
            XmlUtils.beginDocument(parser, "local-data");
            XmlUtils.nextElement(parser);
            if(!parser.getName().equals("locale-strings"))
            {
                Log.e("DateUtils", "No locale-strings tag found in date_time_strings!");
                return;
            }
            int outerDepth = parser.getDepth();
            do
            {
                XmlPullParser _tmp = parser;
                if((type = parser.next()) == 1)
                    break label0;
                XmlPullParser _tmp1 = parser;
                if(type == 3 && parser.getDepth() <= outerDepth)
                    break label0;
                XmlPullParser _tmp2 = parser;
                if(type != 3)
                {
                    XmlPullParser _tmp3 = parser;
                    if(type != 4)
                    {
                        String name = parser.getName();
                        if(name.equals("days-long"))
                            sDaysLong = parseStringList(parser, attrs, 7);
                        else
                        if(name.equals("days-short"))
                            sDaysShort = parseStringList(parser, attrs, 7);
                        else
                        if(name.equals("months-long"))
                            sMonthsLong = parseStringList(parser, attrs, 12);
                        else
                        if(name.equals("months-short"))
                            sMonthsShort = parseStringList(parser, attrs, 12);
                        else
                        if(name.equals("ampm-long"))
                            sAMPMLong = parseStringList(parser, attrs, 2);
                        else
                        if(name.equals("ampm-short"))
                            sAMPMShort = parseStringList(parser, attrs, 2);
                    }
                }
            } while(true);
        }
label1:
        {
            XmlUtils.nextElement(parser);
            if(!parser.getName().equals("locale-formats"))
            {
                Log.e("DateUtils", "No locale-formats tag found in date_time_strings!");
                return;
            }
            int outerDepth = parser.getDepth();
            do
            {
                XmlPullParser _tmp4 = parser;
                if((type = parser.next()) == 1)
                    break label1;
                XmlPullParser _tmp5 = parser;
                if(type == 3 && parser.getDepth() <= outerDepth)
                    break label1;
                XmlPullParser _tmp6 = parser;
                if(type != 3)
                {
                    XmlPullParser _tmp7 = parser;
                    if(type != 4)
                    {
                        String name = parser.getName();
                        if(name.equals("status-clock-format"))
                            sClockFormat = parser.nextText();
                        else
                        if(name.equals("time-format"))
                            sTimeFormat = parser.nextText();
                    }
                }
            } while(true);
        }
label2:
        {
            XmlUtils.nextElement(parser);
            if(!parser.getName().equals("locale-settings"))
            {
                Log.e("DateUtils", "No locale-settings tag found in date_time_strings!");
                return;
            }
            int outerDepth = parser.getDepth();
            do
            {
                XmlPullParser _tmp8 = parser;
                if((type = parser.next()) == 1)
                    break label2;
                XmlPullParser _tmp9 = parser;
                if(type == 3 && parser.getDepth() <= outerDepth)
                    break label2;
                XmlPullParser _tmp10 = parser;
                if(type != 3)
                {
                    XmlPullParser _tmp11 = parser;
                    if(type != 4)
                    {
                        String name = parser.getName();
                        if(name.equals("first-weekday"))
                            sFirstDay = Integer.parseInt(parser.nextText());
                    }
                }
            } while(true);
        }
    }

    private static final String[] parseStringList(XmlPullParser parser, AttributeSet attrs, int numKeys)
        throws XmlPullParserException, IOException
    {
        String values[];
label0:
        {
            int index = 0;
            values = new String[numKeys];
            int depth = 0;
            do
            {
                XmlPullParser _tmp = parser;
                if((type = parser.next()) == 1)
                    break label0;
                XmlPullParser _tmp1 = parser;
                if(type == 2)
                {
                    depth++;
                } else
                {
                    XmlPullParser _tmp2 = parser;
                    if(type == 3)
                    {
                        if(--depth == 0)
                            index++;
                        else
                        if(depth < 0)
                            return values;
                    } else
                    {
                        XmlPullParser _tmp3 = parser;
                        if(type == 4 && depth > 0)
                            if(values[index] == null)
                                values[index] = parser.getText();
                            else
                                values[index] = (new StringBuilder()).append(values[index]).append(parser.getText()).toString();
                    }
                }
            } while(true);
        }
        return values;
    }

    private static final int getFirstDayOfWeek()
    {
        if(null == sDaysLong)
            init();
        return sFirstDay;
    }

    private static String sDaysLong[];
    private static String sDaysShort[];
    private static String sMonthsLong[];
    private static String sMonthsShort[];
    private static String sAMPMLong[];
    private static String sAMPMShort[];
    private static int sFirstDay;
    private static String sClockFormat;
    private static String sTimeFormat;
    public static final long SECOND_IN_MILLIS = 1000L;
    public static final long MINUTE_IN_MILLIS = 60000L;
    public static final long HOUR_IN_MILLIS = 0x36ee80L;
    public static final long DAY_IN_MILLIS = 0x5265c00L;
    public static final long WEEK_IN_MILLIS = 0x240c8400L;
    public static final long YEAR_IN_MILLIS = 0x7528ad000L;
}

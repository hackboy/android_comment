// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DateFormat.java

package android.util;

import android.text.*;
import java.util.*;

// Referenced classes of package android.util:
//            DateUtils

public class DateFormat
{

    public DateFormat()
    {
    }

    public static final CharSequence format(CharSequence inFormat, long inTimeInMillis)
    {
        return format(inFormat, new Date(inTimeInMillis));
    }

    public static final CharSequence format(CharSequence inFormat, Date inDate)
    {
        Calendar c = new GregorianCalendar();
        c.setTime(inDate);
        return format(inFormat, c);
    }

    public static final CharSequence format(CharSequence inFormat, Calendar inDate)
    {
        SpannableStringBuilder s = new SpannableStringBuilder(inFormat);
        int len = inFormat.length();
        int count;
        for(int i = 0; i < len; i += count)
        {
            count = 1;
            int c = s.charAt(i);
            if(c == 39)
            {
                count = appendQuotedText(s, i, len);
                len = s.length();
            } else
            {
                for(; i + count < len && s.charAt(i + count) == c; count++);
                switch(c)
                {
                default:
                    break;

                case 97: // 'a'
                {
                    count = appendAMPM(inDate, s, i, count, false);
                    len = s.length();
                    break;
                }

                case 65: // 'A'
                {
                    count = appendAMPM(inDate, s, i, count, true);
                    len = s.length();
                    break;
                }

                case 100: // 'd'
                {
                    count = appendNumber(inDate.get(5), s, i, count);
                    len = s.length();
                    break;
                }

                case 69: // 'E'
                {
                    int temp = inDate.get(7);
                    count = appendText(DateUtils.getDayOfWeekStr(temp, true), DateUtils.getDayOfWeekStr(temp, false), s, i, count);
                    len = s.length();
                    break;
                }

                case 104: // 'h'
                {
                    int temp = inDate.get(10);
                    if(0 == temp)
                        temp = 12;
                    count = appendNumber(temp, s, i, count);
                    len = s.length();
                    break;
                }

                case 107: // 'k'
                {
                    count = appendNumber(inDate.get(11), s, i, count);
                    len = s.length();
                    break;
                }

                case 109: // 'm'
                {
                    count = appendNumber(inDate.get(12), s, i, count);
                    len = s.length();
                    break;
                }

                case 77: // 'M'
                {
                    count = appendMonth(inDate, s, i, count);
                    len = s.length();
                    break;
                }

                case 115: // 's'
                {
                    count = appendNumber(inDate.get(13), s, i, count);
                    len = s.length();
                    break;
                }

                case 122: // 'z'
                {
                    count = appendTimeZone(inDate, s, i, count);
                    len = s.length();
                    break;
                }

                case 121: // 'y'
                {
                    count = appendYear(inDate, s, i, count);
                    len = s.length();
                    break;
                }
                }
            }
        }

        if(inFormat instanceof Spanned)
            return new SpannedString(s);
        else
            return s.toString();
    }

    private static final int appendAMPM(Calendar inDate, SpannableStringBuilder outText, int inWhere, int inCount, boolean inCaps)
    {
        String ampm = DateUtils.getAMPMStr(inDate.get(9), inCount == 1);
        if(inCaps)
            return outText.replace(inWhere, inWhere + inCount, ampm.toUpperCase());
        else
            return outText.replace(inWhere, inWhere + inCount, ampm.toLowerCase());
    }

    private static final int appendMonth(Calendar inDate, SpannableStringBuilder outText, int where, int count)
    {
        int month = inDate.get(2);
        if(count >= 4)
            return outText.replace(where, where + count, DateUtils.getMonthStr(month, false));
        if(count == 3)
            return outText.replace(where, where + count, DateUtils.getMonthStr(month, true));
        else
            return zeroPad(month + 1, count, outText, where, count);
    }

    private static final int appendTimeZone(Calendar inDate, SpannableStringBuilder outText, int where, int count)
    {
        TimeZone tz = inDate.getTimeZone();
        if(count < 2)
        {
            return formatZoneOffset(inDate.get(16) + inDate.get(15), outText, where, count);
        } else
        {
            boolean dst = inDate.get(16) != 0;
            return outText.replace(where, where + count, tz.getDisplayName(dst, 0));
        }
    }

    private static final int formatZoneOffset(int offset, SpannableStringBuilder outText, int where, int count)
    {
        offset /= 1000;
        SpannableStringBuilder tb = new SpannableStringBuilder();
        if(offset < 0)
        {
            tb.insert(0, "-");
            offset = -offset;
        } else
        {
            tb.insert(0, "+");
        }
        int hours = offset / 3600;
        int minutes = (offset % 3600) / 60;
        zeroPad(hours, 2, tb, tb.length(), 0);
        zeroPad(minutes, 2, tb, tb.length(), 0);
        return outText.replace(where, where + count, tb);
    }

    private static final int appendYear(Calendar inDate, SpannableStringBuilder outText, int where, int count)
    {
        int year = inDate.get(1);
        if(count <= 2)
            return zeroPad(year % 100, 2, outText, where, count);
        else
            return appendNumberNoPadding(year, outText, where, count);
    }

    private static final int appendNumber(int inValue, SpannableStringBuilder outText, int inWhere, int inCount)
    {
        return zeroPad(inValue, inCount, outText, inWhere, inCount);
    }

    private static final int appendNumberNoPadding(int n, SpannableStringBuilder sb, int where, int count)
    {
        return sb.replace(where, where + count, String.valueOf(n));
    }

    private static final int appendText(CharSequence inShort, CharSequence inLong, SpannableStringBuilder outText, int where, int count)
    {
        if(count < 4)
            return outText.replace(where, where + count, inShort);
        else
            return outText.replace(where, where + count, inLong);
    }

    private static final int appendQuotedText(SpannableStringBuilder s, int i, int len)
    {
        if(i + 1 < len && s.charAt(i + 1) == '\'')
        {
            s.delete(i, i + 1);
            return 1;
        }
        int count = 0;
        s.delete(i, i + 1);
        len--;
        do
        {
            if(i >= len)
                break;
            char c = s.charAt(i);
            if(c == '\'')
            {
                if(i + 1 < len && s.charAt(i + 1) == '\'')
                {
                    s.delete(i, i + 1);
                    len--;
                    count++;
                    i++;
                    continue;
                }
                s.delete(i, i + 1);
                break;
            }
            i++;
            count++;
        } while(true);
        return count;
    }

    private static final int zeroPad(int inValue, int inMinDigits, SpannableStringBuilder outText, int where, int count)
    {
        String val = String.valueOf(inValue);
        if(val.length() < inMinDigits)
        {
            char buf[] = new char[inMinDigits];
            for(int i = 0; i < inMinDigits; i++)
                buf[i] = '0';

            val.getChars(0, val.length(), buf, inMinDigits - val.length());
            val = new String(buf);
        }
        return outText.replace(where, where + count, val);
    }

    public static final char QUOTE = 39;
    public static final char AM_PM = 97;
    public static final char CAPITAL_AM_PM = 65;
    public static final char DATE = 100;
    public static final char DAY = 69;
    public static final char HOUR = 104;
    public static final char HOUR_OF_DAY = 107;
    public static final char MINUTE = 109;
    public static final char MONTH = 77;
    public static final char SECONDS = 115;
    public static final char TIME_ZONE = 122;
    public static final char YEAR = 121;
}

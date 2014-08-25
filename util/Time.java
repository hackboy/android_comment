// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Time.java

package android.util;

import java.util.TimeZone;

public class Time
{

    public Time(String timezone)
    {
        if(timezone == null)
        {
            throw new NullPointerException("timezone is null!");
        } else
        {
            this.timezone = timezone;
            hasTime = false;
            year = 1970;
            monthDay = 1;
            isDst = -1;
            return;
        }
    }

    public Time()
    {
        this(TimeZone.getDefault().getID());
    }

    public Time(Time other)
    {
        set(other);
    }

    public native void normalize();

    public native void switchTimezone(String s);

    public int getActualMaximum(int field)
    {
        switch(field)
        {
        case 1: // '\001'
            return 59;

        case 2: // '\002'
            return 59;

        case 3: // '\003'
            return 23;

        case 4: // '\004'
            int n = DAYS_PER_MONTH[month];
            if(n != 28)
            {
                return n;
            } else
            {
                int y = year;
                return y % 4 != 0 || y % 100 == 0 && y % 400 != 0 ? 28 : 29;
            }

        case 5: // '\005'
            return 11;

        case 6: // '\006'
            return 2037;

        case 7: // '\007'
            return 6;

        case 8: // '\b'
            int y = year;
            return y % 4 != 0 || y % 100 == 0 && y % 400 != 0 ? '\u016C' : 365;

        case 9: // '\t'
            throw new RuntimeException("WEEK_NUM not implemented");
        }
        throw new RuntimeException((new StringBuilder()).append("bad field=").append(field).toString());
    }

    public int getYearDay()
    {
        int m = month;
        int n = CUMULATIVE_DAYS[m];
        if(m > 1)
        {
            int y = year;
            if(y % 4 == 0 && (y % 100 != 0 || y % 400 == 0))
                n++;
        }
        return (n + monthDay) - 1;
    }

    static int leaps_thru_end_of(int y)
    {
        return y < 0 ? -(leaps_thru_end_of(-(y + 1)) + 1) : (y / 4 - y / 100) + y / 400;
    }

    public int getWeekDay()
    {
        int y = year;
        int wd = ((4 + ((y - 1970) % 7) * 1 + leaps_thru_end_of(y - 1)) - leaps_thru_end_of(1969)) + getYearDay();
        wd %= 7;
        if(wd < 0)
            wd += 7;
        return wd;
    }

    public void clear(String timezone)
    {
        if(timezone == null)
        {
            throw new NullPointerException("timezone is null!");
        } else
        {
            this.timezone = timezone;
            hasTime = false;
            second = 0;
            minute = 0;
            hour = 0;
            monthDay = 0;
            month = 0;
            year = 0;
            weekDay = 0;
            yearDay = 0;
            gmtoff = 0L;
            isDst = -1;
            return;
        }
    }

    public static native int compare(Time time, Time time1);

    public native String format(String s);

    public native String toString();

    public native void parse(String s);

    public native boolean parse2445(String s);

    public native void parse3339(String s, boolean flag);

    public static String getCurrentTimezone()
    {
        return TimeZone.getDefault().getID();
    }

    public native void setToNow();

    public native long toMillis();

    public native void set(long l);

    public native String format2445();

    public void set(Time that)
    {
        timezone = that.timezone;
        hasTime = that.hasTime;
        second = that.second;
        minute = that.minute;
        hour = that.hour;
        monthDay = that.monthDay;
        month = that.month;
        year = that.year;
        weekDay = that.weekDay;
        yearDay = that.yearDay;
        isDst = that.isDst;
        gmtoff = that.gmtoff;
    }

    public void set(int second, int minute, int hour, int monthDay, int month, int year, int isDst)
    {
        hasTime = true;
        this.second = second;
        this.minute = minute;
        this.hour = hour;
        this.monthDay = monthDay;
        this.month = month;
        this.year = year;
        weekDay = 0;
        yearDay = 0;
        this.isDst = isDst;
        gmtoff = 0L;
    }

    public void set(int monthDay, int month, int year)
    {
        hasTime = false;
        second = 0;
        minute = 0;
        hour = 0;
        this.monthDay = monthDay;
        this.month = month;
        this.year = year;
        weekDay = 0;
        yearDay = 0;
        isDst = -1;
        gmtoff = 0L;
    }

    public void add(int field, int value)
    {
        switch(field)
        {
        case 1: // '\001'
            second += value;
            break;

        case 2: // '\002'
            minute += value;
            break;

        case 3: // '\003'
            hour += value;
            break;

        case 4: // '\004'
            monthDay += value;
            break;

        case 5: // '\005'
            month += value;
            break;

        case 6: // '\006'
            year += value;
            break;

        case 7: // '\007'
            monthDay += value;
            break;

        case 8: // '\b'
            monthDay += value;
            break;

        default:
            throw new RuntimeException((new StringBuilder()).append("bad field=").append(field).toString());
        }
    }

    public boolean before(Time that)
    {
        return compare(this, that) < 0;
    }

    public boolean after(Time that)
    {
        return compare(this, that) > 0;
    }

    public int weekno(int wkst)
    {
        throw new RuntimeException("WEEKNO not implemented");
    }

    public String format3339(boolean allDay)
    {
        if(allDay)
            return format("%Y-%m-%d");
        else
            return format("%Y-%m-%dT%H:%M:%S.000Z");
    }

    public int getJulianDay()
    {
        long millis = toMillis();
        long offsetMillis = gmtoff * 1000L;
        long julianDay = (millis + offsetMillis) / 0x5265c00L;
        return (int)julianDay + 0x253d8c;
    }

    public void setJulianDay(int julianDay)
    {
        long offsetMillis = gmtoff * 1000L;
        long millis = (long)(julianDay - 0x253d8c) * 0x5265c00L;
        millis -= offsetMillis;
        set(millis);
    }

    public static final String TIMEZONE_UTC = "UTC";
    public static final int EPOCH_JULIAN_DAY = 0x253d8c;
    public boolean hasTime;
    public int second;
    public int minute;
    public int hour;
    public int monthDay;
    public int month;
    public int year;
    public int weekDay;
    public int yearDay;
    public int isDst;
    public long gmtoff;
    public String timezone;
    public static final int SECOND = 1;
    public static final int MINUTE = 2;
    public static final int HOUR = 3;
    public static final int MONTH_DAY = 4;
    public static final int MONTH = 5;
    public static final int YEAR = 6;
    public static final int WEEK_DAY = 7;
    public static final int YEAR_DAY = 8;
    public static final int WEEK_NUM = 9;
    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    private static final int DAYS_PER_MONTH[] = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 
        30, 31
    };
    private static int CUMULATIVE_DAYS[] = {
        0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 
        304, 334
    };
    private static final int EPOCH_WDAY = 4;
    private static final int EPOCH_YEAR = 1970;
    private static final int DAYSPERWEEK = 7;
    private static final int DAYSPERNYEAR = 365;

}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MonthDisplayHelper.java

package android.util;

import java.util.Calendar;

public class MonthDisplayHelper
{

    public MonthDisplayHelper(int year, int month, int weekStartDay)
    {
        if(weekStartDay < 1 || weekStartDay > 7)
        {
            throw new IllegalArgumentException();
        } else
        {
            mWeekStartDay = weekStartDay;
            mCalendar = Calendar.getInstance();
            mCalendar.set(1, year);
            mCalendar.set(2, month);
            mCalendar.set(5, 1);
            mCalendar.set(11, 0);
            mCalendar.set(12, 0);
            mCalendar.set(13, 0);
            mCalendar.getTimeInMillis();
            recalculate();
            return;
        }
    }

    public MonthDisplayHelper(int year, int month)
    {
        this(year, month, 1);
    }

    public int getYear()
    {
        return mCalendar.get(1);
    }

    public int getMonth()
    {
        return mCalendar.get(2);
    }

    public int getWeekStartDay()
    {
        return mWeekStartDay;
    }

    public int getFirstDayOfMonth()
    {
        return mCalendar.get(7);
    }

    public int getNumberOfDaysInMonth()
    {
        return mNumDaysInMonth;
    }

    public int getOffset()
    {
        return mOffset;
    }

    public int[] getDigitsForRow(int row)
    {
        if(row < 0 || row > 5)
            throw new IllegalArgumentException((new StringBuilder()).append("row ").append(row).append(" out of range (0-5)").toString());
        int result[] = new int[7];
        for(int column = 0; column < 7; column++)
            result[column] = getDayAt(row, column);

        return result;
    }

    public int getDayAt(int row, int column)
    {
        if(row == 0 && column < mOffset)
        {
            return ((mNumDaysInPrevMonth + column) - mOffset) + 1;
        } else
        {
            int day = ((7 * row + column) - mOffset) + 1;
            return day <= mNumDaysInMonth ? day : day - mNumDaysInMonth;
        }
    }

    public int getRowOf(int day)
    {
        return ((day + mOffset) - 1) / 7;
    }

    public int getColumnOf(int day)
    {
        return ((day + mOffset) - 1) % 7;
    }

    public void previousMonth()
    {
        mCalendar.add(2, -1);
        recalculate();
    }

    public void nextMonth()
    {
        mCalendar.add(2, 1);
        recalculate();
    }

    public boolean isWithinCurrentMonth(int row, int column)
    {
        if(row < 0 || column < 0 || row > 5 || column > 6)
            return false;
        if(row == 0 && column < mOffset)
            return false;
        int day = ((7 * row + column) - mOffset) + 1;
        return day <= mNumDaysInMonth;
    }

    private void recalculate()
    {
        mNumDaysInMonth = mCalendar.getActualMaximum(5);
        mCalendar.add(2, -1);
        mNumDaysInPrevMonth = mCalendar.getActualMaximum(5);
        mCalendar.add(2, 1);
        int firstDayOfMonth = getFirstDayOfMonth();
        int offset = firstDayOfMonth - mWeekStartDay;
        if(offset < 0)
            offset += 7;
        mOffset = offset;
    }

    private final int mWeekStartDay;
    private Calendar mCalendar;
    private int mNumDaysInMonth;
    private int mNumDaysInPrevMonth;
    private int mOffset;
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DayOfMonthCursor.java

package android.util;


// Referenced classes of package android.util:
//            MonthDisplayHelper

public class DayOfMonthCursor extends MonthDisplayHelper
{

    public DayOfMonthCursor(int year, int month, int dayOfMonth, int weekStartDay)
    {
        super(year, month, weekStartDay);
        mRow = getRowOf(dayOfMonth);
        mColumn = getColumnOf(dayOfMonth);
    }

    public int getSelectedRow()
    {
        return mRow;
    }

    public int getSelectedColumn()
    {
        return mColumn;
    }

    public int getSelectedDayOfMonth()
    {
        return getDayAt(mRow, mColumn);
    }

    public boolean up()
    {
        if(isWithinCurrentMonth(mRow - 1, mColumn))
        {
            mRow--;
            return false;
        }
        previousMonth();
        for(mRow = 5; !isWithinCurrentMonth(mRow, mColumn); mRow--);
        return true;
    }

    public boolean down()
    {
        if(isWithinCurrentMonth(mRow + 1, mColumn))
        {
            mRow++;
            return false;
        }
        nextMonth();
        for(mRow = 0; !isWithinCurrentMonth(mRow, mColumn); mRow++);
        return true;
    }

    public boolean left()
    {
        if(mColumn == 0)
        {
            mRow--;
            mColumn = 6;
        } else
        {
            mColumn--;
        }
        if(isWithinCurrentMonth(mRow, mColumn))
        {
            return false;
        } else
        {
            previousMonth();
            int lastDay = getNumberOfDaysInMonth();
            mRow = getRowOf(lastDay);
            mColumn = getColumnOf(lastDay);
            return true;
        }
    }

    public boolean right()
    {
        if(mColumn == 6)
        {
            mRow++;
            mColumn = 0;
        } else
        {
            mColumn++;
        }
        if(isWithinCurrentMonth(mRow, mColumn))
            return false;
        nextMonth();
        mRow = 0;
        for(mColumn = 0; !isWithinCurrentMonth(mRow, mColumn); mColumn++);
        return true;
    }

    private int mRow;
    private int mColumn;
}

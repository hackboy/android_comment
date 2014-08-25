// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DatePicker.java

package android.widget;

import android.content.Context;
import android.content.Resources;
import android.text.Layout;
import android.util.*;
import android.view.*;
import java.util.Map;

// Referenced classes of package android.widget:
//            FrameLayout, TextView, TableLayout, TableRow

public class DatePicker extends FrameLayout
{
    public static interface OnDateSetListener
    {

        public abstract void dateSet(DatePicker datepicker, int i, int j, int k);
    }


    public DatePicker(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
        mWeekStartDay = 1;
        mDayLabels = new String[7];
        mKeyListener = new android.view.View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getAction() != 0)
                    return false;
                if(keyCode == 19)
                {
                    unhighlight(mDayCursor.getSelectedRow(), mDayCursor.getSelectedColumn());
                    if(mDayCursor.up())
                        refreshMonthDisplay();
                    highlight(mDayCursor.getSelectedRow(), mDayCursor.getSelectedColumn());
                    return true;
                }
                if(keyCode == 20)
                {
                    unhighlight(mDayCursor.getSelectedRow(), mDayCursor.getSelectedColumn());
                    if(mDayCursor.down())
                        refreshMonthDisplay();
                    highlight(mDayCursor.getSelectedRow(), mDayCursor.getSelectedColumn());
                    return true;
                }
                if(keyCode == 21)
                {
                    unhighlight(mDayCursor.getSelectedRow(), mDayCursor.getSelectedColumn());
                    if(mDayCursor.left())
                        refreshMonthDisplay();
                    highlight(mDayCursor.getSelectedRow(), mDayCursor.getSelectedColumn());
                    return true;
                }
                if(keyCode == 22)
                {
                    unhighlight(mDayCursor.getSelectedRow(), mDayCursor.getSelectedColumn());
                    if(mDayCursor.right())
                        refreshMonthDisplay();
                    highlight(mDayCursor.getSelectedRow(), mDayCursor.getSelectedColumn());
                    return true;
                }
                if(keyCode == 23 || keyCode == 64)
                    mCallBack.dateSet(DatePicker.this, mDayCursor.getYear(), mDayCursor.getMonth(), mDayCursor.getSelectedDayOfMonth());
                return false;
            }

            final DatePicker this$0;

            
            {
                this$0 = DatePicker.this;
                super();
            }
        };
        mDayLabels = context.getResources().getStringArray(0x10b0003);
        mSelectedColor = context.getResources().getColor(0x106010a);
        ViewInflate inflater = (ViewInflate)context.getSystemService("inflate");
        inflater.inflate(0x1040008, this, true, inflateParams);
        mMonthLabel = (TextView)findViewById(0x1050056);
        mTable = (TableLayout)findViewById(0x1050057);
        mTable.setFocusable(true);
        mTable.setKeyListener(mKeyListener);
        TableRow daysOfWeek = new TableRow(context, attrs, inflateParams);
        int start = mWeekStartDay - 1;
        for(int i = 0; i < 7; i++)
        {
            TextView child = new TextView(context, attrs, inflateParams);
            child.setAlignment(android.text.Layout.Alignment.ALIGN_NORMAL);
            halfAlpha(child);
            int index = (start + i) % 7;
            child.setText(mDayLabels[index]);
            child.setPadding(2, 2, 2, 2);
            daysOfWeek.addView(child, new TableRow.LayoutParams());
        }

        mTable.addView(daysOfWeek, new TableLayout.LayoutParams());
        for(int i = 0; i < 6; i++)
        {
            TableRow row = new TableRow(context);
            for(int day = 0; day < 7; day++)
            {
                TextView child = new TextView(context, attrs, inflateParams);
                child.setAlignment(android.text.Layout.Alignment.ALIGN_NORMAL);
                child.setPadding(2, 2, 2, 2);
                row.addView(child, new TableRow.LayoutParams());
            }

            mTable.addView(row, new TableLayout.LayoutParams());
        }

    }

    public void init(int year, int monthOfYear, int dayOfMonth, int weekStartDay, OnDateSetListener callback)
    {
        mWeekStartDay = weekStartDay;
        mDayCursor = new DayOfMonthCursor(year, monthOfYear, dayOfMonth, weekStartDay);
        mCallBack = callback;
        refreshMonthDisplay();
        highlight(mDayCursor.getSelectedRow(), mDayCursor.getSelectedColumn());
    }

    public int getWeekStartDay()
    {
        return mWeekStartDay;
    }

    private void highlight(int row, int column)
    {
        getBox(row, column).setBackgroundColor(mSelectedColor);
    }

    private void unhighlight(int row, int column)
    {
        getBox(row, column).setBackgroundColor(0xffffff);
    }

    private TextView getBox(int row, int column)
    {
        TableRow tableRow = (TableRow)mTable.getChildAt(row + 1);
        return (TextView)tableRow.getChildAt(column);
    }

    private void refreshMonthDisplay()
    {
        int month = mDayCursor.getMonth();
        int year = mDayCursor.getYear();
        mMonthLabel.setText((new StringBuilder()).append(DateUtils.getMonthStr(month, false)).append(" ").append(year).toString());
        for(int i = 0; i < 6; i++)
        {
            TableRow row = (TableRow)mTable.getChildAt(i + 1);
            int column = 0;
            int arr$[] = mDayCursor.getDigitsForRow(i);
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                int day = arr$[i$];
                TextView child = (TextView)row.getChildAt(column);
                child.setText(String.valueOf(day));
                if(mDayCursor.isWithinCurrentMonth(i, column))
                    fullAlpha(child);
                else
                    quarterAlpha(child);
                column++;
            }

        }

    }

    private void halfAlpha(TextView child)
    {
        int currentColor = child.getNormalTextColor();
        child.setTextColor(currentColor & 0xffffff | 0x80000000);
    }

    private void quarterAlpha(TextView child)
    {
        int currentColor = child.getNormalTextColor();
        child.setTextColor(currentColor & 0xffffff | 0x40000000);
    }

    private void fullAlpha(TextView child)
    {
        int currentColor = child.getNormalTextColor();
        child.setTextColor(currentColor | 0xff000000);
    }

    private int mWeekStartDay;
    private OnDateSetListener mCallBack;
    private TextView mMonthLabel;
    private TableLayout mTable;
    private String mDayLabels[];
    private int mSelectedColor;
    private DayOfMonthCursor mDayCursor;
    private android.view.View.OnKeyListener mKeyListener;





}

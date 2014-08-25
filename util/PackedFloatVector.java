// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PackedFloatVector.java

package android.util;

import java.io.PrintStream;

// Referenced classes of package android.util:
//            ArrayUtils

public class PackedFloatVector
{

    public PackedFloatVector(int columns)
    {
        mColumns = columns;
        mRows = ArrayUtils.idealIntArraySize(0) / mColumns;
        mRowGapStart = 0;
        mRowGapLength = mRows;
        mValues = new float[mRows * mColumns];
        mValueGapStarts = new int[columns];
        mValueGapLengths = new float[columns];
    }

    public float getValue(int row, int column)
    {
        if(row >= mRowGapStart)
            row += mRowGapLength;
        float value = mValues[row * mColumns + column];
        if(row >= mValueGapStarts[column])
            value += mValueGapLengths[column];
        return value;
    }

    public void setValue(int row, int column, float value)
    {
        if(row >= mRowGapStart)
            row += mRowGapLength;
        if(row >= mValueGapStarts[column])
            value -= mValueGapLengths[column];
        mValues[row * mColumns + column] = value;
    }

    public void adjustValuesBelow(int start_row, int column, float delta)
    {
        if(start_row >= mRowGapStart)
            start_row += mRowGapLength;
        moveValueGapTo(column, start_row);
        mValueGapLengths[column] += delta;
    }

    public void insertAt(int row, float values[])
    {
        moveRowGapTo(row);
        if(mRowGapLength == 0)
            growBuffer();
        mRowGapStart++;
        mRowGapLength--;
        if(values == null)
        {
            for(int i = 0; i < mColumns; i++)
                setValue(row, i, 0.0F);

        } else
        {
            for(int i = 0; i < mColumns; i++)
                setValue(row, i, values[i]);

        }
    }

    public void deleteAt(int row, int count)
    {
        moveRowGapTo(row + count);
        mRowGapStart -= count;
        mRowGapLength += count;
        if(mRowGapLength <= size() * 2);
    }

    public int size()
    {
        return mRows - mRowGapLength;
    }

    public int width()
    {
        return mColumns;
    }

    private void growBuffer()
    {
        int newsize = size() + 1;
        newsize = ArrayUtils.idealIntArraySize(newsize * mColumns) / mColumns;
        float newvalues[] = new float[newsize * mColumns];
        int after = mRows - (mRowGapStart + mRowGapLength);
        System.arraycopy(mValues, 0, newvalues, 0, mColumns * mRowGapStart);
        System.arraycopy(mValues, (mRows - after) * mColumns, newvalues, (newsize - after) * mColumns, after * mColumns);
        for(int i = 0; i < mColumns; i++)
        {
            if(mValueGapStarts[i] < mRowGapStart)
                continue;
            mValueGapStarts[i] += newsize - mRows;
            if(mValueGapStarts[i] < mRowGapStart)
                mValueGapStarts[i] = mRowGapStart;
        }

        mRowGapLength += newsize - mRows;
        mRows = newsize;
        mValues = newvalues;
    }

    private void moveValueGapTo(int column, int where)
    {
        if(where == mValueGapStarts[column])
            return;
        if(where > mValueGapStarts[column])
        {
            for(int i = mValueGapStarts[column]; i < where; i++)
                mValues[i * mColumns + column] += mValueGapLengths[column];

        } else
        {
            for(int i = where; i < mValueGapStarts[column]; i++)
                mValues[i * mColumns + column] -= mValueGapLengths[column];

        }
        mValueGapStarts[column] = where;
    }

    private void moveRowGapTo(int where)
    {
        if(where == mRowGapStart)
            return;
        if(where > mRowGapStart)
        {
            int moving = (where + mRowGapLength) - (mRowGapStart + mRowGapLength);
            for(int i = mRowGapStart + mRowGapLength; i < mRowGapStart + mRowGapLength + moving; i++)
            {
                int destrow = (i - (mRowGapStart + mRowGapLength)) + mRowGapStart;
                for(int j = 0; j < mColumns; j++)
                {
                    float val = mValues[i * mColumns + j];
                    if(i >= mValueGapStarts[j])
                        val += mValueGapLengths[j];
                    if(destrow >= mValueGapStarts[j])
                        val -= mValueGapLengths[j];
                    mValues[destrow * mColumns + j] = val;
                }

            }

        } else
        {
            int moving = mRowGapStart - where;
            for(int i = (where + moving) - 1; i >= where; i--)
            {
                int destrow = ((i - where) + mRowGapStart + mRowGapLength) - moving;
                for(int j = 0; j < mColumns; j++)
                {
                    float val = mValues[i * mColumns + j];
                    if(i >= mValueGapStarts[j])
                        val += mValueGapLengths[j];
                    if(destrow >= mValueGapStarts[j])
                        val -= mValueGapLengths[j];
                    mValues[destrow * mColumns + j] = val;
                }

            }

        }
        mRowGapStart = where;
    }

    public void dump()
    {
        for(int i = 0; i < mColumns; i++)
            System.out.print((new StringBuilder()).append(mValueGapStarts[i]).append(" ").toString());

        System.out.print("\n");
        for(int i = 0; i < mColumns; i++)
            System.out.print((new StringBuilder()).append(mValueGapLengths[i]).append(" ").toString());

        System.out.print("\n\n");
        for(int i = 0; i < mRows; i++)
        {
            for(int j = 0; j < mColumns; j++)
            {
                float val = mValues[i * mColumns + j];
                if(i >= mValueGapStarts[j])
                    val += mValueGapLengths[j];
                if(i < mRowGapStart || i >= mRowGapStart + mRowGapLength)
                    System.out.print((new StringBuilder()).append(val).append(" ").toString());
                else
                    System.out.print((new StringBuilder()).append("(").append(val).append(") ").toString());
            }

            System.out.print(" << \n");
        }

        System.out.print("-----\n\n");
    }

    private int mColumns;
    private int mRows;
    private int mRowGapStart;
    private int mRowGapLength;
    private float mValues[];
    private int mValueGapStarts[];
    private float mValueGapLengths[];
}

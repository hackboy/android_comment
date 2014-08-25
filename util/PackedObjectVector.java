// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PackedObjectVector.java

package android.util;

import java.io.PrintStream;

// Referenced classes of package android.util:
//            ArrayUtils

public class PackedObjectVector
{

    public PackedObjectVector(int columns)
    {
        mColumns = columns;
        mRows = ArrayUtils.idealIntArraySize(0) / mColumns;
        mRowGapStart = 0;
        mRowGapLength = mRows;
        mValues = new Object[mRows * mColumns];
    }

    public Object getValue(int row, int column)
    {
        if(row >= mRowGapStart)
            row += mRowGapLength;
        Object value = mValues[row * mColumns + column];
        return value;
    }

    public void setValue(int row, int column, Object value)
    {
        if(row >= mRowGapStart)
            row += mRowGapLength;
        mValues[row * mColumns + column] = value;
    }

    public void insertAt(int row, Object values[])
    {
        moveRowGapTo(row);
        if(mRowGapLength == 0)
            growBuffer();
        mRowGapStart++;
        mRowGapLength--;
        if(values == null)
        {
            for(int i = 0; i < mColumns; i++)
                setValue(row, i, null);

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
        Object newvalues[] = new Object[newsize * mColumns];
        int after = mRows - (mRowGapStart + mRowGapLength);
        System.arraycopy(((Object) (mValues)), 0, ((Object) (newvalues)), 0, mColumns * mRowGapStart);
        System.arraycopy(((Object) (mValues)), (mRows - after) * mColumns, ((Object) (newvalues)), (newsize - after) * mColumns, after * mColumns);
        mRowGapLength += newsize - mRows;
        mRows = newsize;
        mValues = newvalues;
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
                    Object val = mValues[i * mColumns + j];
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
                    Object val = mValues[i * mColumns + j];
                    mValues[destrow * mColumns + j] = val;
                }

            }

        }
        mRowGapStart = where;
    }

    public void dump()
    {
        for(int i = 0; i < mRows; i++)
        {
            for(int j = 0; j < mColumns; j++)
            {
                Object val = mValues[i * mColumns + j];
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
    private Object mValues[];
}

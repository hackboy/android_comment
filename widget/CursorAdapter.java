// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CursorAdapter.java

package android.widget;

import android.content.Context;
import android.database.*;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

// Referenced classes of package android.widget:
//            BaseAdapter, Filterable, Filter

public abstract class CursorAdapter extends BaseAdapter
    implements Filterable
{
    private class MyDataSetObserver extends DataSetObserver
    {

        public void onChanged()
        {
            mDataValid = true;
            if(!mAutoRequeryInProgress)
                notifyDataSetChanged();
        }

        public void onInvalidated()
        {
            mDataValid = false;
            notifyDataSetInvalidated();
        }

        final CursorAdapter this$0;

        private MyDataSetObserver()
        {
            this$0 = CursorAdapter.this;
            super();
        }

    }

    private class ChangeObserver extends ContentObserver
    {

        public boolean deliverSelfNotifications()
        {
            return true;
        }

        public void onChange(boolean selfChange)
        {
            if(mAutoRequery)
            {
                mAutoRequeryInProgress = true;
                mCursor.requery();
                mAutoRequeryInProgress = false;
            }
            notifyChange(selfChange);
        }

        final CursorAdapter this$0;

        public ChangeObserver()
        {
            this$0 = CursorAdapter.this;
            super(new Handler());
        }
    }

    private class CursorFilter extends Filter
    {

        public String convertResultToString(Object resultValue)
        {
            return convertToString((Cursor)resultValue);
        }

        protected Filter.FilterResults performFiltering(CharSequence constraint)
        {
            Cursor cursor = runQuery(constraint);
            Filter.FilterResults results = new Filter.FilterResults();
            results.count = cursor.count();
            results.values = cursor;
            return results;
        }

        protected void publishResults(CharSequence constraint, Filter.FilterResults results)
        {
            if(results.values != mCursor)
                changeCursor((Cursor)results.values);
        }

        final CursorAdapter this$0;

        private CursorFilter()
        {
            this$0 = CursorAdapter.this;
            super();
        }

    }


    public CursorAdapter(Cursor c, Context context)
    {
        mDataSetObserver = new MyDataSetObserver();
        init(c, context, true);
    }

    public CursorAdapter(Cursor c, Context context, boolean autoRequery)
    {
        mDataSetObserver = new MyDataSetObserver();
        init(c, context, autoRequery);
    }

    protected void init(Cursor c, Context context, boolean autoRequery)
    {
        boolean cursorPresent = c != null;
        mAutoRequery = autoRequery;
        mCursor = c;
        mDataValid = cursorPresent;
        mContext = context;
        mRowIDColumn = cursorPresent ? c.getColumnIndex("_id") : -1;
        mChangeObserver = new ChangeObserver();
        if(cursorPresent)
        {
            c.registerContentObserver(mChangeObserver);
            c.registerDataSetObserver(mDataSetObserver);
        }
    }

    public final int getCount()
    {
        if(mDataValid && mCursor != null)
            return mCursor.count();
        else
            return 0;
    }

    public final Object getItem(int position)
    {
        if(mDataValid && mCursor != null)
        {
            mCursor.moveTo(position);
            return mCursor;
        } else
        {
            return null;
        }
    }

    public final long getItemId(int position)
    {
        if(mDataValid && mCursor != null)
        {
            if(mCursor.moveTo(position))
                return mCursor.getLong(mRowIDColumn);
            else
                return 0L;
        } else
        {
            return 0L;
        }
    }

    public boolean stableIds()
    {
        return true;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(!mDataValid)
            throw new IllegalStateException("this should only be called when the cursor is valid");
        mCursor.moveTo(position);
        View v;
        if(convertView == null)
            v = newView(mContext, mCursor, parent);
        else
            v = convertView;
        bindView(v, mContext, mCursor);
        return v;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        if(mDataValid)
        {
            mCursor.moveTo(position);
            View v;
            if(convertView == null)
                v = newDropDownView(mContext, mCursor, parent);
            else
                v = convertView;
            bindView(v, mContext, mCursor);
            return v;
        } else
        {
            return null;
        }
    }

    public abstract View newView(Context context, Cursor cursor, ViewGroup viewgroup);

    public View newDropDownView(Context context, Cursor cursor, ViewGroup parent)
    {
        return newView(context, cursor, parent);
    }

    public abstract void bindView(View view, Context context, Cursor cursor);

    public float getAlpha(boolean focused, int offset)
    {
        return 1.0F;
    }

    public float getScale(boolean focused, int offset)
    {
        return 1.0F;
    }

    public void changeCursor(Cursor cursor)
    {
        if(mCursor != null)
        {
            mCursor.unregisterContentObserver(mChangeObserver);
            mCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = cursor;
        if(cursor != null)
        {
            cursor.registerContentObserver(mChangeObserver);
            cursor.registerDataSetObserver(mDataSetObserver);
            mRowIDColumn = cursor.getColumnIndex("_id");
            mDataValid = true;
            notifyDataSetChanged();
        } else
        {
            mRowIDColumn = -1;
            mDataValid = false;
            notifyDataSetInvalidated();
        }
    }

    protected String convertToString(Cursor cursor)
    {
        return cursor != null ? cursor.toString() : "";
    }

    protected Cursor runQuery(CharSequence constraint)
    {
        return mCursor;
    }

    public Filter getFilter()
    {
        return new CursorFilter();
    }

    protected boolean mDataValid;
    protected boolean mAutoRequery;
    private boolean mAutoRequeryInProgress;
    protected Cursor mCursor;
    protected Context mContext;
    protected int mRowIDColumn;
    protected ChangeObserver mChangeObserver;
    protected DataSetObserver mDataSetObserver;


}

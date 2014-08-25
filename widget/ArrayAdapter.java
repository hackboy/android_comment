// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ArrayAdapter.java

package android.widget;

import android.content.Context;
import android.content.Resources;
import android.view.*;
import java.util.*;

// Referenced classes of package android.widget:
//            BaseAdapter, TextView, Filterable, Filter

public class ArrayAdapter extends BaseAdapter
    implements Filterable
{
    private class ArrayFilter extends Filter
    {

        protected Filter.FilterResults performFiltering(CharSequence prefix)
        {
            Filter.FilterResults results = new Filter.FilterResults();
            if(mOriginalValues == null)
                synchronized(lock)
                {
                    mOriginalValues = new ArrayList(mObjects);
                }
            if(prefix == null || prefix.length() == 0)
            {
                synchronized(lock)
                {
                    ArrayList list = new ArrayList(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else
            {
                String prefixString = prefix.toString().toLowerCase();
                ArrayList values = mOriginalValues;
                int count = values.size();
                ArrayList newValues = new ArrayList(count);
                for(int i = 0; i < count; i++)
                {
                    Object value = values.get(i);
                    if(value != null && value.toString().toLowerCase().startsWith(prefixString))
                        newValues.add(value);
                }

                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        protected void publishResults(CharSequence constraint, Filter.FilterResults results)
        {
            mObjects = (List)results.values;
            if(results.count > 0)
                notifyDataSetChanged();
            else
                notifyDataSetInvalidated();
        }

        final ArrayAdapter this$0;

        private ArrayFilter()
        {
            this$0 = ArrayAdapter.this;
            super();
        }

    }


    public ArrayAdapter(Context context, int resource)
    {
        lock = new Object();
        init(context, resource, new ArrayList());
    }

    public ArrayAdapter(Context context, int resource, Object objects[])
    {
        lock = new Object();
        init(context, resource, Arrays.asList(objects));
    }

    public ArrayAdapter(Context context, int resource, List objects)
    {
        lock = new Object();
        init(context, resource, objects);
    }

    public void addObject(Object object)
    {
        if(mOriginalValues != null)
        {
            synchronized(lock)
            {
                mOriginalValues.add(object);
                notifyChange(false);
            }
        } else
        {
            mObjects.add(object);
            notifyChange(false);
        }
    }

    public void insertObject(Object object, int index)
    {
        if(mOriginalValues != null)
        {
            synchronized(lock)
            {
                mOriginalValues.add(index, object);
                notifyChange(false);
            }
        } else
        {
            mObjects.add(index, object);
            notifyChange(false);
        }
    }

    public void removeObject(Object object)
    {
        if(mOriginalValues != null)
            synchronized(lock)
            {
                mOriginalValues.remove(object);
            }
        else
            mObjects.remove(object);
        notifyChange(false);
    }

    private void init(Context context, int resource, List objects)
    {
        mContext = context;
        mInflate = (ViewInflate)context.getSystemService("inflate");
        mResource = mDropDownResource = resource;
        mObjects = objects;
    }

    public Context getContext()
    {
        return mContext;
    }

    public int getCount()
    {
        return mObjects.size();
    }

    public Object getItem(int position)
    {
        return mObjects.get(position);
    }

    public int getPosition(Object item)
    {
        return mObjects.indexOf(item);
    }

    public long getItemId(int position)
    {
        return (long)position;
    }

    public View getMeasurementView(ViewGroup parent)
    {
        int count = mObjects.size();
        int position = 0;
        if(count < 20)
        {
            int maxLength = 0;
            for(int i = 0; i < count; i++)
            {
                Object item = mObjects.get(i);
                if(item == null)
                    continue;
                String content = item.toString();
                int currentLength = content.length();
                if(currentLength > maxLength)
                {
                    maxLength = currentLength;
                    position = i;
                }
            }

        }
        return getView(position, null, parent);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        return createViewFromResource(position, convertView, mResource);
    }

    private View createViewFromResource(int position, View convertView, int resource)
    {
        TextView view;
        if(convertView == null)
            view = (TextView)mInflate.inflate(resource, null, null);
        else
            view = (TextView)convertView;
        view.setText(getItem(position).toString());
        return view;
    }

    public void setDropDownViewResource(int resource)
    {
        mDropDownResource = resource;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return createViewFromResource(position, convertView, mDropDownResource);
    }

    public void notifyChangeObserver(int selected)
    {
        notifyChange(true);
    }

    public int getNewSelectionForKey(int currentSelection, int key, KeyEvent event)
    {
        return 0x80000000;
    }

    public static ArrayAdapter createFromResource(Context context, int textArrayResId, int textViewResId)
    {
        CharSequence strings[] = context.getResources().getTextArray(textArrayResId);
        return new ArrayAdapter(context, textViewResId, strings);
    }

    public Filter getFilter()
    {
        if(mFilter == null)
            mFilter = new ArrayFilter();
        return mFilter;
    }

    private List mObjects;
    private Context mContext;
    private ViewInflate mInflate;
    private int mResource;
    private ArrayFilter mFilter;
    private ArrayList mOriginalValues;
    private int mDropDownResource;
    private final Object lock;





}

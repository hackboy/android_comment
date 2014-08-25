// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SettingsGroupListModel.java

package android.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.*;
import java.util.ArrayList;

// Referenced classes of package android.widget:
//            BaseGroupListAdapter, WidgetInflate, SettingsCategory, TextView, 
//            LinearLayout, GroupListConnector, Setting

public class SettingsGroupListModel extends BaseGroupListAdapter
{
    protected class MyDataSetObserver extends DataSetObserver
    {

        public void onChanged()
        {
            onInvalidated();
        }

        public void onInvalidated()
        {
            notifyDataSetInvalidated();
        }

        final SettingsGroupListModel this$0;

        protected MyDataSetObserver()
        {
            this$0 = SettingsGroupListModel.this;
            super();
        }
    }

    protected class MyContentObserver extends ContentObserver
    {

        public void onChange(boolean selfChange)
        {
            mDataSetObserver.onInvalidated();
        }

        final SettingsGroupListModel this$0;

        public MyContentObserver(Handler handler)
        {
            this$0 = SettingsGroupListModel.this;
            super(handler);
        }
    }


    public SettingsGroupListModel(Context context, Handler handler)
    {
        mOnSettingsClickListener = new GroupListConnector.OnChildClickListener() {

            public void onChildClick(GroupListConnector parent, View v, int groupPosition, int childPosition, long id)
            {
                android.content.Intent settingIntent = ((SettingsCategory)mCatList.get(groupPosition)).getSetting(childPosition).getIntent();
                if(settingIntent != null)
                    mContext.startActivity(settingIntent);
            }

            final SettingsGroupListModel this$0;

            
            {
                this$0 = SettingsGroupListModel.this;
                super();
            }
        };
        mOnGroupExpandListener = new GroupListConnector.OnGroupExpandListener() {

            public void onGroupExpand(int groupPosition)
            {
                ((SettingsCategory)mCatList.get(groupPosition)).onExpand();
            }

            final SettingsGroupListModel this$0;

            
            {
                this$0 = SettingsGroupListModel.this;
                super();
            }
        };
        mOnGroupCollapseListener = new GroupListConnector.OnGroupCollapseListener() {

            public void onGroupCollapse(int groupPosition)
            {
                ((SettingsCategory)mCatList.get(groupPosition)).onCollapse();
            }

            final SettingsGroupListModel this$0;

            
            {
                this$0 = SettingsGroupListModel.this;
                super();
            }
        };
        mContext = context;
        mInflater = new WidgetInflate(context);
        mHandler = handler;
        mCatList = new ArrayList();
        mContentObserver = new MyContentObserver(new Handler());
        mDataSetObserver = new MyDataSetObserver();
    }

    public void setGroupListConnector(GroupListConnector connector)
    {
        connector.setOnChildClickListener(mOnSettingsClickListener);
        connector.setOnGroupCollapseListener(mOnGroupCollapseListener);
        connector.setOnGroupExpandListener(mOnGroupExpandListener);
    }

    public Object getChild(int groupPos, int childPos)
    {
        return ((SettingsCategory)mCatList.get(groupPos)).getSetting(childPos).getName();
    }

    public long getChildId(int groupPos, int childPos)
    {
        return (long)(groupPos * 1000 + childPos);
    }

    public View getChildView(int groupPos, int childPos, View convertView, ViewGroup parent)
    {
        return ((SettingsCategory)mCatList.get(groupPos)).getView(childPos, convertView, parent);
    }

    public int getChildrenCount(int groupPos)
    {
        return ((SettingsCategory)mCatList.get(groupPos)).getCount();
    }

    public Object getGroup(int groupPos)
    {
        return ((SettingsCategory)mCatList.get(groupPos)).getName();
    }

    public int getGroupCount()
    {
        return mCatList.size();
    }

    public long getGroupId(int groupPos)
    {
        return (long)groupPos;
    }

    public View getGroupView(int groupPos, boolean isExpanded, View convertView, ViewGroup parent)
    {
        if(isExpanded)
        {
            TextView catTextView = (TextView)mInflater.inflate(0x104001c, null, null);
            catTextView.setText(((SettingsCategory)mCatList.get(groupPos)).getName());
            return catTextView;
        } else
        {
            LinearLayout verticalLayout = (LinearLayout)mInflater.inflate(0x104001b, null, null);
            SettingsCategory cat = (SettingsCategory)mCatList.get(groupPos);
            TextView catTextView = (TextView)verticalLayout.findViewById(0x1050068);
            catTextView.setText(cat.getName());
            TextView categoryDescTextView = (TextView)verticalLayout.findViewById(0x1050069);
            categoryDescTextView.setText(cat.getDescription());
            return verticalLayout;
        }
    }

    public GroupListConnector.GenericPosition getNewSelectionForKey(GroupListConnector.GenericPosition pos, int keyCode, KeyEvent event)
    {
        return null;
    }

    public boolean stableIds()
    {
        return true;
    }

    public boolean isChildSelectable(int groupPos, int childPos)
    {
        return !((SettingsCategory)mCatList.get(groupPos)).getSetting(childPos).isSeparator();
    }

    public void addCategory(SettingsCategory category)
    {
        mCatList.add(category);
        category.registerContentObserver(mContentObserver);
        category.registerDataSetObserver(mDataSetObserver);
    }

    public Context getContext()
    {
        return mContext;
    }

    public Handler getHandler()
    {
        return mHandler;
    }

    private final Context mContext;
    private final WidgetInflate mInflater;
    private final Handler mHandler;
    private ArrayList mCatList;
    private ContentObserver mContentObserver;
    private DataSetObserver mDataSetObserver;
    private GroupListConnector.OnChildClickListener mOnSettingsClickListener;
    private GroupListConnector.OnGroupExpandListener mOnGroupExpandListener;
    private GroupListConnector.OnGroupCollapseListener mOnGroupCollapseListener;



}

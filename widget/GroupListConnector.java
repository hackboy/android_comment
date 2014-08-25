// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GroupListConnector.java

package android.widget;

import android.database.DataSetObserver;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

// Referenced classes of package android.widget:
//            BaseAdapter, ListView, GroupListAdapter, AdapterView

public class GroupListConnector extends BaseAdapter
    implements AdapterView.OnItemClickListener
{
    public static class PositionMetadata
    {

        public boolean isExpanded()
        {
            return mGroupMetadata != null;
        }

        public GenericPosition mPosition;
        public GroupMetadata mGroupMetadata;
        public int mGroupInsertIndex;

        public PositionMetadata(int flatListPos, int type, int groupPos, int childPos)
        {
            mPosition = new GenericPosition(flatListPos, type, groupPos, childPos);
        }

        protected PositionMetadata(int flatListPos, int type, int groupPos, int childPos, GroupMetadata groupMetadata, int groupInsertIndex)
        {
            mPosition = new GenericPosition(flatListPos, type, groupPos, childPos);
            mGroupMetadata = groupMetadata;
            mGroupInsertIndex = groupInsertIndex;
        }
    }

    public static class GenericPosition
    {

        public static final int NONE = 0;
        public static final int CHILD = 1;
        public static final int GROUP = 2;
        public int mGroupPos;
        public int mChildPos;
        public int mFlatListPos;
        public int mType;

        public GenericPosition(int flatListPos, int type, int groupPos, int childPos)
        {
            mType = type;
            mFlatListPos = flatListPos;
            mGroupPos = groupPos;
            mChildPos = childPos;
        }

        public GenericPosition(int type, int groupPos, int childPos)
        {
            mType = type;
            mGroupPos = groupPos;
            mChildPos = childPos;
        }
    }

    class GroupMetadata
    {

        static final int REFRESH = -1;
        int flPos;
        int lastChildFlPos;
        int gPos;
        final GroupListConnector this$0;

        GroupMetadata()
        {
            this$0 = GroupListConnector.this;
            super();
        }
    }

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

        final GroupListConnector this$0;

        protected MyDataSetObserver()
        {
            this$0 = GroupListConnector.this;
            super();
        }
    }

    protected class ContentObserver extends android.database.ContentObserver
    {

        public void onChange(boolean selfChange)
        {
            mDataSetObserver.onInvalidated();
        }

        final GroupListConnector this$0;

        public ContentObserver(Handler handler)
        {
            this$0 = GroupListConnector.this;
            super(handler);
        }
    }

    public static interface OnChildClickListener
    {

        public abstract void onChildClick(GroupListConnector grouplistconnector, View view, int i, int j, long l);
    }

    public static interface OnGroupClickListener
    {

        public abstract void onGroupClick(GroupListConnector grouplistconnector, View view, int i, long l);
    }

    public static interface OnGroupExpandListener
    {

        public abstract void onGroupExpand(int i);
    }

    public static interface OnGroupCollapseListener
    {

        public abstract void onGroupCollapse(int i);
    }


    public GroupListConnector(GroupListAdapter groupListAdapter, ListView listView)
    {
        mMaxExpGroupCount = 0x7fffffff;
        mDataSetObserver = new MyDataSetObserver();
        mExpGroupMetadataList = new ArrayList();
        mContentObserver = new ContentObserver(new Handler());
        setGroupListAdapter(groupListAdapter);
        setListView(listView);
    }

    public void setListView(ListView listView)
    {
        if(mListView != null)
        {
            mListView.setOnItemClickListener(null);
            mListView.setAdapter(null);
        }
        mListView = listView;
        listView.setOnItemClickListener(this);
        listView.setAdapter(this);
    }

    public void setGroupListAdapter(GroupListAdapter groupListAdapter)
    {
        mGroupListAdapter = groupListAdapter;
        groupListAdapter.registerContentObserver(mContentObserver);
        groupListAdapter.registerCursorObserver(mDataSetObserver);
    }

    private PositionMetadata getUnflattenedPos(int flPos)
    {
        ArrayList egml = mExpGroupMetadataList;
        int numExpGroups = egml.size();
        int leftExpGroupIndex = 0;
        int rightExpGroupIndex = numExpGroups - 1;
        int midExpGroupIndex = 0;
        if(numExpGroups == 0)
            return new PositionMetadata(flPos, 2, flPos, -1, null, 0);
        while(leftExpGroupIndex <= rightExpGroupIndex) 
        {
            midExpGroupIndex = (rightExpGroupIndex - leftExpGroupIndex) / 2 + leftExpGroupIndex;
            GroupMetadata midExpGm = (GroupMetadata)egml.get(midExpGroupIndex);
            if(flPos > midExpGm.lastChildFlPos)
                leftExpGroupIndex = midExpGroupIndex + 1;
            else
            if(flPos < midExpGm.flPos)
            {
                rightExpGroupIndex = midExpGroupIndex - 1;
            } else
            {
                if(flPos == midExpGm.flPos)
                    return new PositionMetadata(flPos, 2, midExpGm.gPos, -1, midExpGm, midExpGroupIndex);
                if(flPos <= midExpGm.lastChildFlPos)
                {
                    int childPos = flPos - (midExpGm.flPos + 1);
                    return new PositionMetadata(flPos, 1, midExpGm.gPos, childPos, midExpGm, midExpGroupIndex);
                }
            }
        }
        int insertPosition = 0;
        int groupPos = 0;
        if(leftExpGroupIndex > midExpGroupIndex)
        {
            GroupMetadata leftExpGm = (GroupMetadata)egml.get(leftExpGroupIndex - 1);
            insertPosition = leftExpGroupIndex;
            groupPos = (flPos - leftExpGm.lastChildFlPos) + leftExpGm.gPos;
        } else
        if(rightExpGroupIndex < midExpGroupIndex)
        {
            GroupMetadata rightExpGm = (GroupMetadata)egml.get(++rightExpGroupIndex);
            insertPosition = rightExpGroupIndex;
            groupPos = rightExpGm.gPos - (rightExpGm.flPos - flPos);
        } else
        {
            throw new RuntimeException("Unknown state");
        }
        return new PositionMetadata(flPos, 2, groupPos, -1, null, insertPosition);
    }

    public PositionMetadata getFlattenedPos(GenericPosition pos)
    {
        ArrayList egml;
        int leftExpGroupIndex;
        int rightExpGroupIndex;
        int midExpGroupIndex;
label0:
        {
            egml = mExpGroupMetadataList;
            int numExpGroups = egml.size();
            leftExpGroupIndex = 0;
            rightExpGroupIndex = numExpGroups - 1;
            midExpGroupIndex = 0;
            if(numExpGroups == 0)
                return new PositionMetadata(pos.mGroupPos, pos.mType, pos.mGroupPos, pos.mChildPos, null, 0);
            GroupMetadata midExpGm;
label1:
            do
            {
                while(leftExpGroupIndex <= rightExpGroupIndex) 
                {
                    midExpGroupIndex = (rightExpGroupIndex - leftExpGroupIndex) / 2 + leftExpGroupIndex;
                    midExpGm = (GroupMetadata)egml.get(midExpGroupIndex);
                    if(pos.mGroupPos > midExpGm.gPos)
                    {
                        leftExpGroupIndex = midExpGroupIndex + 1;
                    } else
                    {
                        if(pos.mGroupPos >= midExpGm.gPos)
                            continue label1;
                        rightExpGroupIndex = midExpGroupIndex - 1;
                    }
                }
                break label0;
            } while(pos.mGroupPos != midExpGm.gPos);
            if(pos.mType == 2)
                return new PositionMetadata(midExpGm.flPos, pos.mType, pos.mGroupPos, pos.mChildPos, midExpGm, midExpGroupIndex);
            if(pos.mType == 1)
                return new PositionMetadata(midExpGm.flPos + pos.mChildPos + 1, pos.mType, pos.mGroupPos, pos.mChildPos, midExpGm, midExpGroupIndex);
            else
                return null;
        }
        if(pos.mType != 2)
            return null;
        if(leftExpGroupIndex > midExpGroupIndex)
        {
            GroupMetadata leftExpGm = (GroupMetadata)egml.get(leftExpGroupIndex - 1);
            int flPos = leftExpGm.lastChildFlPos + (pos.mGroupPos - leftExpGm.gPos);
            return new PositionMetadata(flPos, pos.mType, pos.mGroupPos, pos.mChildPos, null, leftExpGroupIndex);
        }
        if(rightExpGroupIndex < midExpGroupIndex)
        {
            GroupMetadata rightExpGm = (GroupMetadata)egml.get(--rightExpGroupIndex);
            int flPos = rightExpGm.flPos - (rightExpGm.gPos - pos.mGroupPos);
            return new PositionMetadata(flPos, pos.mType, pos.mGroupPos, pos.mChildPos, null, rightExpGroupIndex);
        } else
        {
            return null;
        }
    }

    public boolean areAllItemsSelectable()
    {
        return false;
    }

    public boolean isSelectable(int flatListPos)
    {
        GenericPosition pos = getUnflattenedPos(flatListPos).mPosition;
        if(pos.mType == 1)
            return mGroupListAdapter.isChildSelectable(pos.mGroupPos, pos.mChildPos);
        else
            return true;
    }

    public int getCount()
    {
        return mGroupListAdapter.getGroupCount() + mTotalExpChildrenCount;
    }

    public Object getItem(int flatListPos)
    {
        PositionMetadata posMetadata = getUnflattenedPos(flatListPos);
        if(posMetadata.mPosition.mType == 2)
            return mGroupListAdapter.getGroup(posMetadata.mPosition.mGroupPos);
        if(posMetadata.mPosition.mType == 1)
            return mGroupListAdapter.getChild(posMetadata.mPosition.mGroupPos, posMetadata.mPosition.mChildPos);
        else
            throw new RuntimeException("Flat list position is of unknown type");
    }

    public long getItemId(int flatListPos)
    {
        PositionMetadata posMetadata = getUnflattenedPos(flatListPos);
        if(posMetadata.mPosition.mType == 2)
            return mGroupListAdapter.getGroupId(posMetadata.mPosition.mGroupPos);
        if(posMetadata.mPosition.mType == 1)
            return mGroupListAdapter.getChildId(posMetadata.mPosition.mGroupPos, posMetadata.mPosition.mChildPos);
        else
            throw new RuntimeException("Flat list position is of unknown type");
    }

    public int getNewSelectionForKey(int selectedFlatListPos, int keyCode, KeyEvent event)
    {
        GenericPosition pos = getUnflattenedPos(selectedFlatListPos).mPosition;
        GenericPosition newSelectedPos = mGroupListAdapter.getNewSelectionForKey(pos, keyCode, event);
        if(newSelectedPos == null || newSelectedPos.mType == 0)
            return 0x80000000;
        else
            return getFlattenedPos(pos).mPosition.mFlatListPos;
    }

    public View getView(int flatListPos, View convertView, ViewGroup parent)
    {
        PositionMetadata posMetadata = getUnflattenedPos(flatListPos);
        if(posMetadata.mPosition.mType == 2)
            return mGroupListAdapter.getGroupView(posMetadata.mPosition.mGroupPos, posMetadata.isExpanded(), convertView, parent);
        if(posMetadata.mPosition.mType == 1)
        {
            View view = mGroupListAdapter.getChildView(posMetadata.mPosition.mGroupPos, posMetadata.mPosition.mChildPos, convertView, parent);
            view.setPadding(view.getPaddingLeft() + 20, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
            return view;
        } else
        {
            throw new RuntimeException("Flat list position is of unknown type");
        }
    }

    public boolean stableIds()
    {
        return mGroupListAdapter.stableIds();
    }

    private void refreshExpGroupMetadataList()
    {
        ArrayList egml = mExpGroupMetadataList;
        int egmlSize = egml.size();
        int curFlPos = 0;
        mTotalExpChildrenCount = 0;
        int lastGPos = 0;
        for(int i = 0; i < egmlSize; i++)
        {
            GroupMetadata curGm = (GroupMetadata)egml.get(i);
            int gChildrenCount;
            if(curGm.lastChildFlPos == -1)
                gChildrenCount = mGroupListAdapter.getChildrenCount(curGm.gPos);
            else
                gChildrenCount = curGm.lastChildFlPos - curGm.flPos;
            mTotalExpChildrenCount += gChildrenCount;
            curFlPos += curGm.gPos - lastGPos;
            lastGPos = curGm.gPos;
            curGm.flPos = curFlPos;
            curFlPos += gChildrenCount;
            curGm.lastChildFlPos = curFlPos;
        }

    }

    public void onItemClick(AdapterView parent, View v, int position, long id)
    {
        PositionMetadata posMetadata = getUnflattenedPos(position);
        if(posMetadata.mPosition.mType == 2)
        {
            if(posMetadata.isExpanded())
            {
                collapseGroupInt(posMetadata);
            } else
            {
                if(mOnGroupClickListener != null)
                    mOnGroupClickListener.onGroupClick(this, v, posMetadata.mPosition.mGroupPos, id);
                expandGroupInt(posMetadata);
            }
            refreshExpGroupMetadataList();
            notifyDataSetInvalidated();
        } else
        if(mOnChildClickListener != null)
            mOnChildClickListener.onChildClick(this, v, posMetadata.mPosition.mGroupPos, posMetadata.mPosition.mChildPos, id);
    }

    public void setOnGroupCollapseListener(OnGroupCollapseListener onGroupCollapseListener)
    {
        mOnGroupCollapseListener = onGroupCollapseListener;
    }

    public void collapseGroup(int groupPos)
    {
        collapseGroupInt(getFlattenedPos(new GenericPosition(-1, 2, groupPos, -1)));
    }

    private void collapseGroupInt(PositionMetadata posMetadata)
    {
        if(posMetadata.mGroupMetadata != null)
        {
            if(mOnGroupCollapseListener != null)
                mOnGroupCollapseListener.onGroupCollapse(posMetadata.mPosition.mGroupPos);
            mExpGroupMetadataList.remove(posMetadata.mGroupMetadata);
        }
    }

    public void setOnGroupExpandListener(OnGroupExpandListener onGroupExpandListener)
    {
        mOnGroupExpandListener = onGroupExpandListener;
    }

    public void expandGroup(int groupPos)
    {
        expandGroupInt(getFlattenedPos(new GenericPosition(-1, 2, groupPos, -1)));
    }

    private void expandGroupInt(PositionMetadata posMetadata)
    {
        if(posMetadata.mPosition.mGroupPos < 0)
            throw new RuntimeException("Need group");
        if(mMaxExpGroupCount > 0)
        {
            if(mOnGroupExpandListener != null)
                mOnGroupExpandListener.onGroupExpand(posMetadata.mPosition.mGroupPos);
            if(mExpGroupMetadataList.size() >= mMaxExpGroupCount)
            {
                GroupMetadata collapsedGm = (GroupMetadata)mExpGroupMetadataList.get(0);
                int collapsedIndex = mExpGroupMetadataList.indexOf(collapsedGm);
                collapseGroup(collapsedGm.gPos);
                if(posMetadata.mGroupInsertIndex > collapsedIndex)
                    posMetadata.mGroupInsertIndex--;
            }
            GroupMetadata expandedGm = new GroupMetadata();
            expandedGm.gPos = posMetadata.mPosition.mGroupPos;
            expandedGm.flPos = -1;
            expandedGm.lastChildFlPos = -1;
            mExpGroupMetadataList.add(posMetadata.mGroupInsertIndex, expandedGm);
        }
    }

    public void setOnGroupClickListener(OnGroupClickListener onGroupClickListener)
    {
        mOnGroupClickListener = onGroupClickListener;
    }

    public void setOnChildClickListener(OnChildClickListener onChildClickListener)
    {
        mOnChildClickListener = onChildClickListener;
    }

    public void setMaxExpGroupCount(int maxExpGroupCount)
    {
        mMaxExpGroupCount = maxExpGroupCount;
    }

    private GroupListAdapter mGroupListAdapter;
    private ListView mListView;
    private ArrayList mExpGroupMetadataList;
    private int mTotalExpChildrenCount;
    private int mMaxExpGroupCount;
    private ContentObserver mContentObserver;
    private DataSetObserver mDataSetObserver;
    private OnGroupCollapseListener mOnGroupCollapseListener;
    private OnGroupExpandListener mOnGroupExpandListener;
    private OnGroupClickListener mOnGroupClickListener;
    private OnChildClickListener mOnChildClickListener;

}

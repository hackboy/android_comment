// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EventLog.java

package android.util;

import com.google.android.collect.Lists;
import java.io.PrintStream;

public class EventLog
{
    public static final class TagItem
    {

        public static final int TEST_TAG = 1;
        public static final int TEST_TAG2 = 1000;
        int mTagToken;
        String mTagString;

        public TagItem(int tagToken, String tagString)
        {
            mTagToken = tagToken;
            mTagString = tagString;
        }
    }

    public static final class List
    {

        public final Object getItem(int pos)
        {
            return mItems[pos];
        }

        public final byte getNumItems()
        {
            return mNumItems;
        }

        private byte mNumItems;
        private Object mItems[];

        public List(java.util.List items)
            throws IllegalArgumentException
        {
            int itemsSize = items.size();
            if(itemsSize > 127)
                throw new IllegalArgumentException("An EventLog List must have fewer then 127 items in it.");
            if(itemsSize < 1)
                throw new IllegalArgumentException("An EventLog List must have at least one item in it.");
            Object item = items.get(0);
            if(!(item instanceof Tuple) && !(item instanceof List) && !(item instanceof String) && !(item instanceof Integer) && !(item instanceof Long))
            {
                throw new IllegalArgumentException("Attempt to create an EventLog List with illegal item type.");
            } else
            {
                mNumItems = (byte)itemsSize;
                mItems = items.toArray();
                return;
            }
        }

        public transient List(Object items[])
            throws IllegalArgumentException
        {
            this(((java.util.List) (Lists.newArrayList(items))));
        }
    }

    public static final class Tuple
    {

        public final Object getItem(int pos)
        {
            return mItems[pos];
        }

        public final byte getNumItems()
        {
            return mNumItems;
        }

        private byte mNumItems;
        private Object mItems[];

        public transient Tuple(Object items[])
            throws IllegalArgumentException
        {
            mNumItems = (byte)items.length;
            if(mNumItems > 127)
                throw new IllegalArgumentException("A Tuple must have fewer then 127 items in it.");
            if(mNumItems < 1)
                throw new IllegalArgumentException("A Tuple must have at least one item in it.");
            for(byte i = 0; i < mNumItems; i++)
            {
                Object item = items[i];
                if(!(item instanceof Tuple) && !(item instanceof List) && !(item instanceof String) && !(item instanceof Integer) && !(item instanceof Long))
                    throw new IllegalArgumentException("Attempt to create a Tuple with illegal item type.");
            }

            mItems = items;
        }
    }


    public EventLog()
    {
    }

    public static void generateTagsTable(PrintStream stream)
    {
        TagItem arr$[] = sTagTable;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            TagItem tagItem = arr$[i$];
            stream.printf("%d,%s\n", new Object[] {
                Integer.valueOf(tagItem.mTagToken), tagItem.mTagString
            });
        }

    }

    static native int writeEvent(int i, byte abyte0[], int j);

    static native int writeEvent(int i, int j);

    static native int writeEvent(int i, long l);

    static native int writeEvent(int i, String s);

    static native int writeEvent(int i, Tuple tuple);

    static native int writeEvent(int i, List list);

    public static final byte INT = 0;
    public static final byte LONG = 1;
    public static final byte STRING = 2;
    public static final byte LIST = 4;
    public static TagItem sTagTable[] = {
        new TagItem(1, "Test Tag")
    };

}

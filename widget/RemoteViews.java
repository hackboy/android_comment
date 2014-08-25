// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RemoteViews.java

package android.widget;

import android.content.*;
import android.net.ContentURI;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import java.util.ArrayList;

// Referenced classes of package android.widget:
//            ImageView, TextView

public class RemoteViews
    implements Parcelable
{
    private class SetImageViewUri extends Action
    {

        public void writeToParcel(Parcel dest)
        {
            dest.writeInt(3);
            dest.writeInt(viewId);
            ContentURI.writeToParcel(dest, uri);
        }

        public void apply(View root)
        {
            ImageView target = (ImageView)root.findViewById(viewId);
            if(target != null)
                target.setImageURI(uri);
        }

        int viewId;
        ContentURI uri;
        public static final int TAG = 3;
        final RemoteViews this$0;

        public SetImageViewUri(int id, ContentURI u)
        {
            this$0 = RemoteViews.this;
            super();
            viewId = id;
            uri = u;
        }

        public SetImageViewUri(Parcel parcel)
        {
            this$0 = RemoteViews.this;
            super();
            viewId = parcel.readInt();
            uri = (ContentURI)ContentURI.CREATOR.createFromParcel(parcel);
        }
    }

    private class SetImageViewResource extends Action
    {

        public void writeToParcel(Parcel dest)
        {
            dest.writeInt(2);
            dest.writeInt(viewId);
            dest.writeInt(srcId);
        }

        public void apply(View root)
        {
            ImageView target = (ImageView)root.findViewById(viewId);
            android.graphics.drawable.Drawable d = mContext.getResources().getDrawable(srcId);
            if(target != null)
                target.setImageDrawable(d);
        }

        int viewId;
        int srcId;
        public static final int TAG = 2;
        final RemoteViews this$0;

        public SetImageViewResource(int id, int src)
        {
            this$0 = RemoteViews.this;
            super();
            viewId = id;
            srcId = src;
        }

        public SetImageViewResource(Parcel parcel)
        {
            this$0 = RemoteViews.this;
            super();
            viewId = parcel.readInt();
            srcId = parcel.readInt();
        }
    }

    private class SetTextViewText extends Action
    {

        public void writeToParcel(Parcel dest)
        {
            dest.writeInt(1);
            dest.writeInt(viewId);
            TextUtils.writeToParcel(text, dest);
        }

        public void apply(View root)
        {
            TextView target = (TextView)root.findViewById(viewId);
            if(target != null)
                target.setText(text);
        }

        int viewId;
        CharSequence text;
        public static final int TAG = 1;
        final RemoteViews this$0;

        public SetTextViewText(int id, CharSequence t)
        {
            this$0 = RemoteViews.this;
            super();
            viewId = id;
            text = t;
        }

        public SetTextViewText(Parcel parcel)
        {
            this$0 = RemoteViews.this;
            super();
            viewId = parcel.readInt();
            text = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        }
    }

    private class SetViewVisibility extends Action
    {

        public void writeToParcel(Parcel dest)
        {
            dest.writeInt(0);
            dest.writeInt(viewId);
            dest.writeInt(visibility);
        }

        public void apply(View root)
        {
            View target = root.findViewById(viewId);
            if(target != null)
                target.setVisibility(visibility);
        }

        private int viewId;
        private int visibility;
        public static final int TAG = 0;
        final RemoteViews this$0;

        public SetViewVisibility(int id, int vis)
        {
            this$0 = RemoteViews.this;
            super();
            viewId = id;
            visibility = vis;
        }

        public SetViewVisibility(Parcel parcel)
        {
            this$0 = RemoteViews.this;
            super();
            viewId = parcel.readInt();
            visibility = parcel.readInt();
        }
    }

    private static abstract class Action
        implements Parcelable
    {

        public abstract void apply(View view)
            throws ActionException;

        private Action()
        {
        }

    }

    public static class ActionException extends RuntimeException
    {

        public ActionException(String message)
        {
            super(message);
        }
    }


    public RemoteViews(String packageName, int layoutId)
    {
        mPackage = packageName;
        mLayoutId = layoutId;
    }

    public RemoteViews(Parcel parcel)
    {
        mPackage = parcel.readString();
        mLayoutId = parcel.readInt();
        int count = parcel.readInt();
        if(count > 0)
        {
            mActions = new ArrayList(count);
            for(int i = 0; i < count; i++)
            {
                int tag = parcel.readInt();
                switch(tag)
                {
                case 0: // '\0'
                    mActions.add(new SetViewVisibility(parcel));
                    break;

                case 1: // '\001'
                    mActions.add(new SetTextViewText(parcel));
                    break;

                case 2: // '\002'
                    mActions.add(new SetImageViewResource(parcel));
                    break;

                case 3: // '\003'
                    mActions.add(new SetImageViewUri(parcel));
                    break;

                default:
                    throw new ActionException((new StringBuilder()).append("Tag ").append(tag).append("not found").toString());
                }
            }

        }
    }

    private void addAction(Action a)
    {
        if(mActions == null)
            mActions = new ArrayList();
        mActions.add(a);
    }

    public void setViewVisibility(int viewId, int visibility)
    {
        addAction(new SetViewVisibility(viewId, visibility));
    }

    public void setTextViewText(int viewId, CharSequence text)
    {
        addAction(new SetTextViewText(viewId, text));
    }

    public void setImageViewResource(int viewId, int srcId)
    {
        addAction(new SetImageViewResource(viewId, srcId));
    }

    public void setImageViewUri(int viewId, ContentURI uri)
    {
        addAction(new SetImageViewUri(viewId, uri));
    }

    public View apply(Context context, ViewGroup parent)
    {
        View result = null;
        Context c = null;
        String packageName = mPackage;
        if(packageName != null)
            try
            {
                c = context.createPackageContext(packageName, 0);
            }
            catch(android.content.PackageManager.NameNotFoundException e)
            {
                Log.e("RemoteViews", (new StringBuilder()).append("Package name ").append(packageName).append(" not found").toString());
            }
        else
            c = context;
        Resources r = c.getResources();
        ViewInflate inflater = (ViewInflate)c.getSystemService("inflate");
        result = inflater.inflate(mLayoutId, parent, false, null);
        mContext = c;
        int count = mActions.size();
        for(int i = 0; i < count; i++)
        {
            Action a = (Action)mActions.get(i);
            a.apply(result);
        }

        return result;
    }

    public void writeToParcel(Parcel dest)
    {
        dest.writeString(mPackage);
        dest.writeInt(mLayoutId);
        int count;
        if(mActions != null)
            count = mActions.size();
        else
            count = 0;
        dest.writeInt(count);
        for(int i = 0; i < count; i++)
        {
            Action a = (Action)mActions.get(i);
            a.writeToParcel(dest);
        }

    }

    private static final String LOG_TAG = "RemoteViews";
    private String mPackage;
    private int mLayoutId;
    private Context mContext;
    private ArrayList mActions;
    public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

        public RemoteViews createFromParcel(Parcel parcel)
        {
            return new RemoteViews(parcel);
        }

        public RemoteViews[] newArray(int size)
        {
            return new RemoteViews[size];
        }

        public volatile Object[] newArray(int x0)
        {
            return newArray(x0);
        }

        public volatile Object createFromParcel(Parcel x0)
        {
            return createFromParcel(x0);
        }

    };


}

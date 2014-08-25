// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ActivityAdapter.java

package android.widget;

import android.content.*;
import android.text.method.KeyCharacterMap;
import android.view.*;
import java.util.Collections;
import java.util.List;

// Referenced classes of package android.widget:
//            BaseAdapter, TextView

public class ActivityAdapter extends BaseAdapter
{

    public ActivityAdapter(Context context, Intent intent)
    {
        mContext = context;
        mIntent = new Intent(intent);
        mIntent.setComponent(null);
        mInflate = (ViewInflate)context.getSystemService("inflate");
        PackageManager pm = context.getPackageManager();
        mList = pm.queryIntentActivities(intent, 0);
        if(mList != null)
            Collections.sort(mList, new android.content.PackageManager.ResolveInfo.DisplayNameComparator(pm));
    }

    public Intent intentForPosition(int position)
    {
        if(mList == null)
        {
            return null;
        } else
        {
            Intent intent = new Intent(mIntent);
            android.content.PackageManager.ActivityInfo ai = ((android.content.PackageManager.ResolveInfo)mList.get(position)).activityInfo;
            intent.setClassName(ai.applicationInfo.packageName, ai.className);
            return intent;
        }
    }

    public int getCount()
    {
        return mList == null ? 0 : mList.size();
    }

    public Object getItem(int position)
    {
        return Integer.valueOf(position);
    }

    public long getItemId(int position)
    {
        return (long)position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;
        if(convertView == null)
            view = mInflate.inflate(0x1040001, parent, false, null);
        else
            view = convertView;
        bindView(view, (android.content.PackageManager.ResolveInfo)mList.get(position));
        return view;
    }

    public int getNewSelectionForKey(int currentSelection, int key, KeyEvent event)
    {
        KeyCharacterMap kmap = KeyCharacterMap.load(event.getKeyboardDevice());
        char c = Character.toLowerCase(kmap.getDisplayLabel(key));
        if(c == 0)
            return 0x80000000;
        int len = mList.size();
        int i = currentSelection + 1;
        do
        {
            if(i >= len)
                break;
            android.content.PackageManager.ResolveInfo info = (android.content.PackageManager.ResolveInfo)mList.get(i);
            char potential = getCandidateLetter(info);
            if(c == potential)
                return i;
            if(potential > c)
                break;
            i++;
        } while(true);
        i = 0;
        do
        {
            if(i >= currentSelection)
                break;
            android.content.PackageManager.ResolveInfo info = (android.content.PackageManager.ResolveInfo)mList.get(i);
            char potential = getCandidateLetter(info);
            if(c == potential)
                return i;
            if(potential > c)
                break;
            i++;
        } while(true);
        return 0x80000000;
    }

    private final char getCandidateLetter(android.content.PackageManager.ResolveInfo info)
    {
        PackageManager pm = mContext.getPackageManager();
        CharSequence label = pm.getResolveLabel(info);
        if(label == null)
        {
            String name = info.activityInfo.className;
            label = name;
        }
        return Character.toLowerCase(label.charAt(0));
    }

    private final void bindView(View view, android.content.PackageManager.ResolveInfo info)
    {
        TextView text = (TextView)view.findViewById(0x1050010);
        PackageManager pm = mContext.getPackageManager();
        CharSequence label = pm.getResolveLabel(info);
        text.setText(((CharSequence) (label == null ? ((CharSequence) (info.activityInfo.className)) : label)));
    }

    protected final Context mContext;
    protected final Intent mIntent;
    protected final ViewInflate mInflate;
    protected List mList;
}

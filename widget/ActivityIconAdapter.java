// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ActivityIconAdapter.java

package android.widget;

import android.content.*;
import android.view.*;
import java.util.List;

// Referenced classes of package android.widget:
//            ActivityAdapter, TextView, ImageView

public class ActivityIconAdapter extends ActivityAdapter
{

    public ActivityIconAdapter(Context context, Intent intent)
    {
        super(context, intent);
        mPM = context.getPackageManager();
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;
        if(convertView == null)
            view = mInflate.inflate(0x1040006, parent, false, null);
        else
            view = convertView;
        bindView(view, (android.content.PackageManager.ResolveInfo)mList.get(position));
        return view;
    }

    private final void bindView(View view, android.content.PackageManager.ResolveInfo info)
    {
        TextView text = (TextView)view.findViewById(0x1050010);
        ImageView icon = (ImageView)view.findViewById(0x1050051);
        text.setText(mPM.getResolveLabel(info));
        icon.setImageDrawable(mPM.getResolveIcon(info));
    }

    private final PackageManager mPM;
}

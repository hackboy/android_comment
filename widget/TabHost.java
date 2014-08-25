// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TabHost.java

package android.widget;

import android.app.LocalActivityManager;
import android.content.*;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.*;
import android.view.animation.AnimationUtils;
import java.util.List;
import java.util.Map;

//已过时

public class TabHost extends FrameLayout
{
    public static interface TabContentFactory
    {

        public abstract View createTabContent(String s);
    }

    private static class TabRec
    {

        public View view;
        public String tag;
        public Intent intent;
        public TabContentFactory factory;
        public View contentView;
        public int viewID;

        private TabRec()
        {
        }

    }

    public static interface OnTabChangeListener
    {

        public abstract void onTabChanged(String s);
    }


    public TabHost(Context context)
    {
        super(context);
        mCurrentTab = -1;
        mCurrentView = null;
        mLocalActivityManager = null;
        initTabHost();
    }

    public TabHost(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
        mCurrentTab = -1;
        mCurrentView = null;
        mLocalActivityManager = null;
        initTabHost();
    }

    private final void initTabHost()
    {
        setFocusable(true);
        setFocusType(2);
        mTabs = new TabRec[0];
        mCurrentTab = -1;
        mCurrentView = null;
    }

    public void setup()
    {
        mTabWidget = (TabWidget)findViewById(0x105000f);
        if(mTabWidget == null)
            throw new RuntimeException("Your TabHost must have a TabWidget whose id attribute is 'android.R.id.tabs'");
        mTabContent = (FrameLayout)findViewById(0x105000d);
        if(mTabContent == null)
            throw new RuntimeException("Your TabHost must have a FrameLayout whose id attribute is 'android.R.id.tabcontent'");
        else
            return;
    }

    public void setup(LocalActivityManager activityGroup)
    {
        setup();
        mLocalActivityManager = activityGroup;
    }

    public void addTabView(String tag, Intent intent, View tabView)
    {
        addTabView(tag, intent, -1, null, tabView);
    }

    public void addTabView(String tag, int viewID, View tabView)
    {
        addTabView(tag, null, viewID, null, tabView);
    }

    public void addTabView(String tag, TabContentFactory factory, View tabView)
    {
        addTabView(tag, null, -1, factory, tabView);
    }

    public void addTab(String tag, Intent intent, CharSequence label)
    {
        addTab(tag, intent, -1, null, label);
    }

    public void addTabs(ComponentName caller, Intent specifics[], Intent intent, int flags)
    {
        PackageManager pm = getContext().getPackageManager();
        List tabs = pm.queryIntentActivityOptions(caller, specifics, intent, 0);
        if(tabs != null)
        {
            int N = tabs.size();
            for(int i = 0; i < N; i++)
            {
                android.content.PackageManager.ResolveInfo ri = (android.content.PackageManager.ResolveInfo)tabs.get(i);
                int sidx = ri.specificIndex;
                ComponentName comp = new ComponentName(ri.activityInfo.applicationInfo.packageName, ri.activityInfo.className);
                if(sidx >= 0)
                {
                    Intent tintent = new Intent(specifics[sidx]);
                    tintent.setComponent(comp);
                    String action = tintent.getAction();
                    addTab(action == null ? comp.toShortString() : action, tintent, pm.getResolveLabel(ri));
                } else
                {
                    Intent tintent = new Intent(intent);
                    tintent.setComponent(comp);
                    addTab(comp.toShortString(), tintent, pm.getResolveLabel(ri));
                }
            }

        }
    }

    public void addTab(String tag, int viewID, CharSequence label)
    {
        addTab(tag, null, viewID, null, label);
    }

    public void addTab(String tag, TabContentFactory factory, CharSequence label)
    {
        addTab(tag, null, -1, factory, label);
    }

    private void addTab(String tag, Intent intent, int viewID, TabContentFactory factory, CharSequence label)
    {
        label = TextUtils.stringOrSpannedString(label);
        TabTextView view = new TabTextView(getContext());
        view.setText(label);
        view.setTextSize(16F);
        view.setTypeface(Typeface.DEFAULT_BOLD);
        addTabView(tag, intent, viewID, factory, view);
    }

    private void addTabView(String tag, Intent intent, int viewID, TabContentFactory factory, View tabView)
    {
        TabRec t = new TabRec();
        t.intent = intent;
        t.viewID = viewID;
        t.factory = factory;
        t.tag = tag;
        int n = mTabs.length;
        TabRec tabs[] = new TabRec[n + 1];
        if(n > 0)
            System.arraycopy(mTabs, 0, tabs, 0, n);
        tabs[n] = t;
        mTabs = tabs;
        if(t.tag == null)
            t.tag = (new StringBuilder()).append("android.app.TabActivity.index:").append(n).toString();
        if(viewID != -1)
        {
            View content = mTabContent.findViewById(viewID);
            if(content != null)
                content.setVisibility(8);
            else
                throw new RuntimeException((new StringBuilder()).append("Missing view ").append(viewID).toString());
        }
        t.view = tabView;
        mTabWidget.addView(tabView, new android.view.ViewGroup.LayoutParams(-2, -2));
    }

    public void clearAllTabs(boolean clearContent)
    {
        mTabWidget.removeAllViews();
        initTabHost();
        if(clearContent)
            mTabContent.removeAllViews();
        requestLayout();
        invalidate();
    }

    public TabWidget getTabWidget()
    {
        return mTabWidget;
    }

    public int getCurrentTab()
    {
        return mCurrentTab;
    }

    public String getCurrentTabTag()
    {
        if(mTabs != null && mCurrentTab >= 0)
            return mTabs[mCurrentTab].tag;
        else
            return null;
    }

    public View getCurrentTabView()
    {
        if(mTabs != null && mCurrentTab >= 0)
            return mTabs[mCurrentTab].view;
        else
            return null;
    }

    public View getCurrentView()
    {
        return mCurrentView;
    }

    public void setCurrentTabByTag(String tag)
    {
        int i = 0;
        do
        {
            if(i >= mTabs.length)
                break;
            if(mTabs[i].tag.equals(tag))
            {
                setCurrentTab(i);
                break;
            }
            i++;
        } while(true);
    }

    public boolean dispatchUnhandledMove(View focused, int direction)
    {
        boolean handled = super.dispatchUnhandledMove(focused, direction);
        if(!handled && mTabWidget.isEnabled() && mTabWidget.getVisibility() == 0)
            switch(direction)
            {
            default:
                break;

            case 17: // '\021'
                if(mCurrentTab > 0)
                {
                    setCurrentTab(mCurrentTab - 1);
                    handled = true;
                }
                break;

            case 66: // 'B'
                if(mCurrentTab < mTabs.length - 1)
                {
                    setCurrentTab(mCurrentTab + 1);
                    handled = true;
                }
                break;
            }
        return handled;
    }

    public FrameLayout getTabContentView()
    {
        return mTabContent;
    }

    public void setCurrentTab(int index)
    {
        if(mTabs.length > 0)
        {
            if(index < 0)
                index = 0;
            if(index >= mTabs.length)
                index = mTabs.length - 1;
            boolean animate = false;
            int offset = 0;
            if(index != mCurrentTab)
            {
                TabRec oldTab = null;
                if(mCurrentTab >= 0)
                    oldTab = mTabs[mCurrentTab];
                animate = false;
                offset = index - mCurrentTab;
                mCurrentTab = index;
                TabRec tab = mTabs[index];
                mTabWidget.setCurrentTab(mCurrentTab);
                View v = null;
                if(tab.intent != null)
                {
                    Window w = mLocalActivityManager.startActivity(tab.tag, tab.intent);
                    v = w == null ? null : w.getDecorView();
                    if(v != null)
                    {
                        v.setFocusable(true);
                        v.setFocusType(2);
                    }
                } else
                if(tab.viewID != -1)
                    v = mTabContent.findViewById(tab.viewID);
                else
                if(tab.factory != null)
                {
                    if(tab.contentView == null)
                        tab.contentView = tab.factory.createTabContent(tab.tag);
                    v = tab.contentView;
                }
                if(v != mCurrentView)
                {
                    clearDisappearingChildren();
                    if(v != null && (v.getParent() == null || v.getVisibility() == 8))
                    {
                        if(v.getParent() == null)
                            mTabContent.addView(v, new android.view.ViewGroup.LayoutParams(-1, -1));
                        v.setVisibility(0);
                        if(animate)
                        {
                            android.view.animation.Animation a = AnimationUtils.makeInAnimation(mContext, offset < 0);
                            v.setCurrentAnimation(a);
                        }
                    }
                    if(mCurrentView != null)
                    {
                        if(animate)
                        {
                            android.view.animation.Animation a = AnimationUtils.makeOutAnimation(mContext, offset < 0);
                            mCurrentView.setCurrentAnimation(a);
                        }
                        if(oldTab.intent != null)
                            mTabContent.removeView(mCurrentView);
                        else
                            mCurrentView.setVisibility(8);
                    }
                    mCurrentView = v;
                    mTabContent.takeFocus(2);
                }
                invokeOnTabChangeListener();
            }
        }
    }

    public void setOnTabChangedListener(OnTabChangeListener l)
    {
        mOnTabChangeListener = l;
    }

    private void invokeOnTabChangeListener()
    {
        if(mOnTabChangeListener != null)
            mOnTabChangeListener.onTabChanged(getCurrentTabTag());
    }

    private TabWidget mTabWidget;
    private FrameLayout mTabContent;
    private TabRec mTabs[];
    protected int mCurrentTab;
    private View mCurrentView;
    protected LocalActivityManager mLocalActivityManager;
    private OnTabChangeListener mOnTabChangeListener;
}

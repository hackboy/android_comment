// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StatusBar.java

package android.server.status;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.text.Layout;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.*;
import java.util.*;

// Referenced classes of package android.server.status:
//            StatusBarIcons, StatusBarAdapter, StatusBarData, StatusBarManager
//statusbar是一个view，继承自framelayout
public class StatusBar extends FrameLayout
    implements android.widget.AdapterView.OnItemClickListener, StatusBarManager.ChangeObserver, android.widget.AdapterView.OnItemSelectedListener
{
    private class TickerRunnable
        implements Runnable
    {

        public void run()
        {
            if(mState == 0)
            {
                StatusBarData data = mNotificationList.getItem(mTickerTag);
                if(data == null)
                    return;
                CharSequence tickerText = data.getTickerText();
                if(tickerText != null && tickerText.length() > 0)
                {
                    mTextSegments.clear();
                    StaticLayout l = new StaticLayout(tickerText, mTextSwitcherPaint, mTextSwitcher.getWidth(), android.text.Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                    int lineCount = l.getLineCount();
                    for(int i = 0; i < lineCount; i++)
                    {
                        CharSequence s = tickerText.subSequence(l.getLineStart(i), l.getLineEnd(i));
                        mTextSegments.add(s);
                    }

                    ImageView i = mTickerIcon;
                    i.setImageDrawable(data.getIcon(StatusBar.this.this$0));
                    if(data.getType() == 3)
                        i.setImageLevel(data.getIconLevel());
                    android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(28, 21);
                    i.setLayoutParams(lp);
                    i.setScaleType(android.widget.ImageView.ScaleType.CENTER);
                    mMainTicker.setVisibility(0);
                    mMainTicker.startAnimation(AnimationUtils.loadAnimation(StatusBar.this.this$0, 0x1030019));
                    mIcons.setVisibility(8);
                    mIcons.startAnimation(AnimationUtils.loadAnimation(StatusBar.this.this$0, 0x103001a));
                    mActiveViews.setVisibility(8);
                    if(mTextSegments.size() > 0)
                    {
                        mCurrentSegment = 0;
                        mTextSwitcher.setAnimateFirstView(false);
                        mTextSwitcher.reset();
                        mTextSwitcher.setText((CharSequence)mTextSegments.get(0));
                    }
                    scheduleNextSegment();
                    mState = 1;
                }
            }
        }

        private String mTickerTag;
        final StatusBar this$0;

        public TickerRunnable(String tag)
        {
            this$0 = StatusBar.this;
            super();
            mTickerTag = tag;
        }
    }


    public StatusBar(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);
        mTextSegments = new ArrayList();
        mState = 0;
        mAdvanceTickerRunnable = new Runnable() {

            public void run()
            {
                if(mState == 1)
                {
                    if(mTextSegments == null)
                    {
                        resetTicker();
                        return;
                    }
                    if(mCurrentSegment < mTextSegments.size() - 1)
                    {
                        mCurrentSegment++;
                        mTextSwitcher.setText((CharSequence)mTextSegments.get(mCurrentSegment));
                        scheduleNextSegment();
                    } else
                    {
                        resetTicker();
                    }
                }
            }

            final StatusBar this$0;

            
            {
                this$0 = StatusBar.this;
                super();
            }
        };
        mHandler = new Handler();
    }

    public void onFinishInflate()
    {
        Context context = getContext();
        mIcons = (LinearLayout)findViewById(0x105006d);
        mStatusIcons = (StatusBarIcons)findViewById(0x105006f);
        mNotificationIcons = (StatusBarIcons)findViewById(0x105006e);
        mActiveViews = findViewById(0x1050073);
        mStatusBarManager = StatusBarManager.getInstance();
        mStatusList = mStatusBarManager.getStatusList();
        mNotificationList = mStatusBarManager.getNotificationList();
        mStatusIcons.bind(mStatusList, true);
        mNotificationIcons.bind(mNotificationList, false);
        mNotificationGallery = (Gallery)findViewById(0x1050074);
        mNotificationGallery.setOnItemClickListener(this);
        mNotificationGallery.setOnItemSelectedListener(this);
        mNotificationList.registerChangeObserver(this);
        mMainTicker = findViewById(0x1050070);
        mTextSwitcher = (TextSwitcher)findViewById(0x1050072);
        TextView text = (TextView)mTextSwitcher.getChildAt(0);
        mTextSwitcherPaint = text.getPaint();
        android.view.animation.Animation in = AnimationUtils.loadAnimation(context, 0x1030019);
        android.view.animation.Animation out = AnimationUtils.loadAnimation(context, 0x103001a);
        mTextSwitcher.setInAnimation(in);
        mTextSwitcher.setOutAnimation(out);
        mTickerIcon = (ImageView)findViewById(0x1050071);
        mBalloon = findViewById(0x1050075);
        mBalloonText = (TextView)findViewById(0x1050076);
        mBalloonRemoteViews = (FrameLayout)findViewById(0x1050077);
        mNotificationAdapter = new StatusBarAdapter(context, false);
        mNotificationGallery.setAdapter(mNotificationAdapter);
    }

    public void activate()
    {
        if(mNotificationAdapter.getCount() > 0)
        {
            mState = 2;
            mIcons.setVisibility(8);
            mMainTicker.setVisibility(8);
            mActiveViews.setVisibility(0);
            Gallery notificationGallery = mNotificationGallery;
            onItemSelected(notificationGallery, notificationGallery.getSelectedView(), notificationGallery.getSelectedItemIndex(), notificationGallery.getSelectedItemId());
            mBalloon.startAnimation(AnimationUtils.loadAnimation(getContext(), 0x1030006));
            mBalloon.setVisibility(0);
            android.view.WindowManager.LayoutParams lp = (android.view.WindowManager.LayoutParams)getLayoutParams();
            lp.flags &= -9;
            WindowManagerImpl.getDefault().updateViewLayout(this, mLayoutParams);
            takeFocus(2);
        }
    }

    public void deactivate()
    {
        mState = 0;
        mIcons.setVisibility(0);
        mMainTicker.setVisibility(8);
        mActiveViews.setVisibility(8);
        mBalloon.setVisibility(8);
        mBalloonRemoteViews.removeAllViews();
        android.view.WindowManager.LayoutParams lp = (android.view.WindowManager.LayoutParams)getLayoutParams();
        lp.flags |= 8;
        WindowManagerImpl.getDefault().updateViewLayout(this, mLayoutParams);
    }

    public boolean dispatchUnhandledMove(View focused, int direction)
    {
        boolean handled = super.dispatchUnhandledMove(focused, direction);
        if(!handled && direction == 130)
        {
            deactivate();
            handled = true;
        }
        return handled;
    }

    public void onItemClick(AdapterView parent, View v, int position, long id)
    {
        StatusBarData data = (StatusBarData)mNotificationAdapter.getItem(position);
        Intent i = data.getIntent();
        if(i != null)
        {
            i.setLaunchFlags(4);
            getContext().startActivity(i);
        }
        deactivate();
    }

    public void onUpdate(String s)
    {
    }

    public void onDelete(String s)
    {
    }

    public void onInsert(String tag)
    {
        mHandler.postDelayed(new TickerRunnable(tag), 1000L);
    }

    public void onItemSelected(AdapterView parent, View v, int position, long id)
    {
        StatusBarData data = (StatusBarData)mNotificationAdapter.getItem(position);
        if(data != null)
        {
            RemoteViews remoteViews = data.getBalloon();
            if(remoteViews != null)
            {
                try
                {
                    mBalloon.setVisibility(0);
                    View balloon = remoteViews.apply(getContext(), mBalloonRemoteViews);
                    mBalloonRemoteViews.removeAllViews();
                    mBalloonRemoteViews.addView(balloon, new android.view.ViewGroup.LayoutParams(-2, -2));
                    mBalloonRemoteViews.setVisibility(0);
                    mBalloonText.setVisibility(8);
                }
                catch(Throwable t)
                {
                    Log.e("statusbar", (new StringBuilder()).append("Error inflating remoteViews for icon ").append(data.getIconPackage()).append(" ").append(data.getIconId()).toString());
                }
            } else
            {
                CharSequence balloonText = data.getBalloonText();
                if(balloonText != null)
                {
                    mBalloon.setVisibility(0);
                    mBalloonRemoteViews.removeAllViews();
                    mBalloonRemoteViews.setVisibility(8);
                    mBalloonText.setVisibility(0);
                    mBalloonText.setText(balloonText);
                }
            }
        }
    }

    public void onNothingSelected(AdapterView parent)
    {
        mBalloonText.setText(0x10a005f);
        mBalloonText.setVisibility(0);
        mBalloonRemoteViews.removeAllViews();
        mBalloonRemoteViews.setVisibility(8);
    }

    private void resetTicker()
    {
        mState = 0;
        mIcons.startAnimation(AnimationUtils.loadAnimation(mContext, 0x1030017));
        mMainTicker.startAnimation(AnimationUtils.loadAnimation(mContext, 0x1030018));
        mIcons.setVisibility(0);
        mMainTicker.setVisibility(8);
    }

    private void scheduleNextSegment()
    {
        mHandler.postDelayed(mAdvanceTickerRunnable, 3000L);
    }

    private static final int TICKER_SEGMENT_DELAY = 3000;
    static final String LOG_TAG = "statusbar";
    private static final int STATE_NORMAL = 0;
    private static final int STATE_TICKER = 1;
    private static final int STATE_ACTIVE = 2;
    LinearLayout mIcons;
    StatusBarIcons mStatusIcons;
    StatusBarIcons mNotificationIcons;
    View mActiveViews;
    HashMap mRecycle;
    private StatusBarManager mStatusBarManager;
    private StatusBarManager.StatusBarList mStatusList;
    private StatusBarManager.StatusBarList mNotificationList;
    private Gallery mNotificationGallery;
    private StatusBarAdapter mNotificationAdapter;
    private View mMainTicker;
    private TextSwitcher mTextSwitcher;
    private ImageView mTickerIcon;
    private View mBalloon;
    private TextView mBalloonText;
    private FrameLayout mBalloonRemoteViews;
    private ArrayList mTextSegments;
    private Paint mTextSwitcherPaint;
    private int mCurrentSegment;
    private int mState;
    private Handler mHandler;
    private Runnable mAdvanceTickerRunnable;
















}

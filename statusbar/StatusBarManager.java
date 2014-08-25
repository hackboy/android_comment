// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StatusBarManager.java

package android.server.status;

import android.app.Notification;
import android.content.*;
import android.database.ContentObserver;
import android.net.ContentURI;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.*;
import android.util.DateUtils;
import java.util.*;

// Referenced classes of package android.server.status:
//            StatusBarData

public class StatusBarManager
{
    private class VolumeChangeObserver extends ContentObserver
    {

        public void onChange(boolean selfChange)
        {
            updateVolume();
        }

        final StatusBarManager this$0;

        public VolumeChangeObserver(Handler handler)
        {
            this$0 = StatusBarManager.this;
            super(handler);
        }
    }

    private class StatusBarHandler extends Handler
    {

        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
            case 100: // 'd'
                updateSignalStrength(mPhoneStateReceiver.getSignalStrength());
                break;

            case 200: 
                updateMWI(mPhoneStateReceiver.getMessageWaitingIndicator());
                break;

            case 300: 
                updateDataConnection(mDataStateReceiver.getConnectionState());
                break;

            case 400: 
                updateDataActivity(mDataStateReceiver.getActivityState());
                break;
            }
        }

        final StatusBarManager this$0;

        private StatusBarHandler()
        {
            this$0 = StatusBarManager.this;
            super();
        }

    }

    private final class DequeueNotification
        implements Runnable
    {

        public void run()
        {
            String tag = getTag(mPkg, mId);
            mNotifications.removeEntry(tag);
        }

        final String mPkg;
        final int mId;
        final StatusBarManager this$0;

        DequeueNotification(String pkg, int id)
        {
            this$0 = StatusBarManager.this;
            super();
            mPkg = pkg;
            mId = id;
        }
    }

    private final class EnqueueNotification
        implements Runnable
    {

        public void run()
        {
            String tag = getTag(mPkg, mId);
            StatusBarData data = mNotifications.getItem(tag);
            Notification n = mNotification;
            if(data == null)
            {
                data = new StatusBarData(tag, 2, null);
                data.setVisible(true);
                data.setIconId(n.statusBarIcon);
                data.setIconPackage(mPkg);
                data.setIntent(n.statusBarClickIntent);
                data.setTickerText(n.statusBarTickerText);
                data.setBalloonText(n.statusBarBalloonText);
                data.setBalloon(n.statusBarBalloon);
                mNotifications.addEntry(data);
            } else
            {
                data.setVisible(true);
                data.setIconId(n.statusBarIcon);
                data.setIconPackage(mPkg);
                data.setIntent(n.statusBarClickIntent);
                data.setTickerText(n.statusBarTickerText);
                data.setBalloonText(n.statusBarBalloonText);
                data.setBalloon(n.statusBarBalloon);
                mNotifications.updateEntry(data);
            }
        }

        final String mPkg;
        final int mId;
        final Notification mNotification;
        final StatusBarManager this$0;

        EnqueueNotification(String pkg, int id, Notification n)
        {
            this$0 = StatusBarManager.this;
            super();
            mPkg = pkg;
            mId = id;
            mNotification = n;
        }
    }

    public class StatusBarList
    {

        public boolean registerChangeObserver(ChangeObserver observer)
        {
            ArrayList arraylist = mObservers;
            JVM INSTR monitorenter ;
            if(mObservers.indexOf(observer) != -1)
                break MISSING_BLOCK_LABEL_32;
            mObservers.add(observer);
            return true;
            arraylist;
            JVM INSTR monitorexit ;
              goto _L1
            Exception exception;
            exception;
            throw exception;
_L1:
            return false;
        }

        public boolean unregisterChangeObserver(ChangeObserver observer)
        {
            ArrayList arraylist = mObservers;
            JVM INSTR monitorenter ;
            return mObservers.remove(observer);
            Exception exception;
            exception;
            throw exception;
        }

        public int getCount()
        {
            return mVisibleCount;
        }

        public StatusBarData getItem(int position)
        {
            int count = mEntries.size();
            int which = 0;
            for(int i = 0; i < count; i++)
            {
                StatusBarData data = (StatusBarData)mEntries.get(i);
                if(!data.isVisible())
                    continue;
                if(which == position)
                    return data;
                which++;
            }

            return null;
        }

        public StatusBarData getItem(String tag)
        {
            int count = mEntries.size();
            int which = 0;
            for(int i = 0; i < count; i++)
            {
                StatusBarData data = (StatusBarData)mEntries.get(i);
                if(!data.isVisible())
                    continue;
                if(tag.equals(data.getTag()))
                    return data;
                which++;
            }

            return null;
        }

        private void addEntry(StatusBarData data)
        {
            synchronized(mEntries)
            {
                mEntries.add(data);
            }
            if(data.isVisible())
                visibilityChanged(data, true);
        }

        private void updateEntry(StatusBarData data)
        {
            synchronized(mEntries)
            {
                mEntries.remove(data);
                mEntries.add(data);
            }
            String tag = data.getTag();
            notifyInsert(tag);
        }

        private void removeEntry(String tag)
        {
            int index = findEntryIndex(tag);
            if(index != -1)
            {
                StatusBarData data = null;
                synchronized(mEntries)
                {
                    data = (StatusBarData)mEntries.remove(index);
                }
                if(data.isVisible())
                    visibilityChanged(data, false);
            }
        }

        private int findEntryIndex(String tag)
        {
            int count = mEntries.size();
            for(int i = 0; i < count; i++)
            {
                StatusBarData data = (StatusBarData)mEntries.get(i);
                if(tag.equals(data.getTag()))
                    return i;
            }

            return -1;
        }

        private boolean setVisibilityInt(StatusBarData d, boolean visible)
        {
            boolean changed = false;
            synchronized(mEntries)
            {
                boolean oldVisibility = d.isVisible();
                if(oldVisibility != visible)
                {
                    d.setVisible(visible);
                    changed = true;
                }
            }
            return changed;
        }

        private void setVisible(String tag, boolean visible)
        {
            int index = findEntryIndex(tag);
            if(index >= 0)
            {
                StatusBarData d = (StatusBarData)mEntries.get(index);
                boolean changed = setVisibilityInt(d, visible);
                if(changed)
                    visibilityChanged(d, visible);
            }
        }

        private void setImage(String tag, int resId)
        {
            int index = findEntryIndex(tag);
            if(index >= 0)
            {
                StatusBarData d = (StatusBarData)mEntries.get(index);
                if(!d.isVisible() || d.getIconId() != resId)
                {
                    d.setIconId(resId);
                    entryChanged(d);
                }
            }
        }

        private final void setImageLevel(String tag, int resId, int level)
        {
            int index = findEntryIndex(tag);
            if(index >= 0)
            {
                StatusBarData d = (StatusBarData)mEntries.get(index);
                if(d.getIconId() != resId || d.getIconLevel() != level)
                {
                    d.setIconId(resId);
                    d.setIconLevel(level);
                    entryChanged(d);
                }
            }
        }

        private void setText(String tag, CharSequence text)
        {
            int index = findEntryIndex(tag);
            if(index >= 0)
            {
                StatusBarData d = (StatusBarData)mEntries.get(index);
                CharSequence oldText = d.getText();
                if(text != null && !text.equals(oldText) || (text == null) != (oldText == null))
                {
                    d.setText(text);
                    entryChanged(d);
                }
            }
        }

        private void setBalloonText(String tag, CharSequence balloonText)
        {
            int index = findEntryIndex(tag);
            if(index >= 0)
            {
                StatusBarData d = (StatusBarData)mEntries.get(index);
                CharSequence oldTitle = d.getBalloonText();
                if(balloonText != null && !balloonText.equals(oldTitle) || balloonText == null && oldTitle != null)
                {
                    d.setBalloonText(balloonText);
                    entryChanged(d);
                }
            }
        }

        private void entryChanged(StatusBarData d)
        {
            boolean changed = setVisibilityInt(d, true);
            if(changed)
                visibilityChanged(d, true);
            else
                notifyUpdate(d);
        }

        private void visibilityChanged(StatusBarData d, boolean visible)
        {
            String tag = d.getTag();
            if(visible)
            {
                mVisibleCount++;
                notifyInsert(tag);
            } else
            {
                mVisibleCount--;
                notifyDelete(tag);
            }
        }

        private void notifyDelete(String tag)
        {
            synchronized(mObservers)
            {
                int n = mObservers.size() - 1;
                for(int i = n; i >= 0; i--)
                {
                    ChangeObserver observer = (ChangeObserver)mObservers.get(i);
                    if(observer != null)
                        observer.onDelete(tag);
                }

            }
        }

        private void notifyInsert(String tag)
        {
            synchronized(mObservers)
            {
                int n = mObservers.size() - 1;
                for(int i = n; i >= 0; i--)
                {
                    ChangeObserver observer = (ChangeObserver)mObservers.get(i);
                    if(observer != null)
                        observer.onInsert(tag);
                }

            }
        }

        private void notifyUpdate(StatusBarData d)
        {
            synchronized(mObservers)
            {
                int n = mObservers.size() - 1;
                for(int i = n; i >= 0; i--)
                {
                    ChangeObserver observer = (ChangeObserver)mObservers.get(i);
                    if(observer != null)
                        observer.onUpdate(d.getTag());
                }

            }
        }

        private ArrayList mEntries;
        private ArrayList mObservers;
        private int mVisibleCount;
        final StatusBarManager this$0;









        public StatusBarList()
        {
            this$0 = StatusBarManager.this;
            super();
            mVisibleCount = 0;
            mEntries = new ArrayList();
            mObservers = new ArrayList();
        }
    }

    public static interface ChangeObserver
    {

        public abstract void onUpdate(String s);

        public abstract void onDelete(String s);

        public abstract void onInsert(String s);
    }


    public static StatusBarManager main(Context context)
    {
        sSingleton = new StatusBarManager(context);
        return sSingleton;
    }

    public static StatusBarManager getInstance()
    {
        return sSingleton;
    }

    public StatusBarManager(Context c)
    {
        mHandler = new StatusBarHandler();
        mLastRingerVolumeIndex = -1;
        mIntentReceiver = new IntentReceiver() {

            public void onReceiveIntent(Context context, Intent intent)
            {
                String action = intent.getAction();
                if(action.equals("android.intent.action.TIME_TICK"))
                    updateClock();
                else
                if(action.equals("android.intent.action.BATTERY_CHANGED"))
                    updateBattery(intent);
                else
                if(action.equals("android.intent.action.TIMEZONE_CHANGED"))
                {
                    mTimeZone = TimeZone.getTimeZone((String)intent.getExtra("time-zone"));
                    TimeZone.setDefault(mTimeZone);
                    updateClock();
                }
            }

            final StatusBarManager this$0;

            
            {
                this$0 = StatusBarManager.this;
                super();
            }
        };
        mContext = c;
        mStatus = new StatusBarList();
        mNotifications = new StatusBarList();
        mStatus.addEntry(new StatusBarData("clock", 1, null));
        mStatus.addEntry(new StatusBarData("battery", 3, null));
        mStatus.addEntry(new StatusBarData("signal", 2, null));
        mStatus.addEntry(new StatusBarData("data", 2, null));
        StatusBarData data = new StatusBarData("mwi", 2, null);
        data.setBalloonText("Voicemail");
        data.setIconId(0x10600b5);
        Intent intent = new Intent("android.intent.action.CALL", ContentURI.create("voicemail:x"));
        data.setIntent(intent);
        mNotifications.addEntry(data);
        ContentResolver cr = mContext.getContentResolver();
        data = new StatusBarData("volume", 2, null);
        mStatus.addEntry(data);
        ContentURI contentUri = android.provider.Settings.System.getUriFor(cr, "ringer_volume");
        cr.registerContentObserver(contentUri, true, new VolumeChangeObserver(new Handler()));
        updateVolume();
        mPhoneStateReceiver = new PhoneStateIntentReceiver(mContext, mHandler);
        mPhoneStateReceiver.notifySignalStrength(100);
        mPhoneStateReceiver.notifyServiceState(100);
        mPhoneStateReceiver.notifyMessageWaitingIndicator(200);
        mPhoneStateReceiver.registerIntent();
        mDataStateReceiver = new DataStateIntentReceiver(mContext, mHandler);
        mDataStateReceiver.notifyConnectionState(300);
        mDataStateReceiver.notifyActivity(400);
        mDataStateReceiver.registerIntent();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_TICK");
        filter.addAction("android.intent.action.TIME_SET");
        filter.addAction("android.intent.action.BATTERY_CHANGED");
        filter.addAction("android.intent.action.TIMEZONE_CHANGED");
        mContext.registerReceiver(mIntentReceiver, filter, null, mHandler);
        mTimeZone = TimeZone.getDefault();
        updateClock();
        updateSignalStrength(-1);
        updateDataConnection(android.telephony.Phone.DataState.DISCONNECTED);
        updateMWI(false);
    }

    public StatusBarList getStatusList()
    {
        return mStatus;
    }

    public StatusBarList getNotificationList()
    {
        return mNotifications;
    }

    private final void updateBattery(Intent intent)
    {
        int level = ((Integer)intent.getExtra("draw-level")).intValue();
        int image = ((Integer)intent.getExtra("icon-small")).intValue();
        mStatus.setImageLevel("battery", image, level);
    }

    final void updateDataConnection(android.telephony.Phone.DataState state)
    {
        static class _cls2
        {

            static final int $SwitchMap$android$telephony$Phone$DataState[];
            static final int $SwitchMap$android$telephony$Phone$DataActivityState[];
            static final int $SwitchMap$android$telephony$ServiceState$State[];

            static 
            {
                $SwitchMap$android$telephony$ServiceState$State = new int[android.telephony.ServiceState.State.values().length];
                try
                {
                    $SwitchMap$android$telephony$ServiceState$State[android.telephony.ServiceState.State.OUT_OF_SERVICE.ordinal()] = 1;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$android$telephony$ServiceState$State[android.telephony.ServiceState.State.POWER_OFF.ordinal()] = 2;
                }
                catch(NoSuchFieldError ex) { }
                $SwitchMap$android$telephony$Phone$DataActivityState = new int[android.telephony.Phone.DataActivityState.values().length];
                try
                {
                    $SwitchMap$android$telephony$Phone$DataActivityState[android.telephony.Phone.DataActivityState.DATAIN.ordinal()] = 1;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$android$telephony$Phone$DataActivityState[android.telephony.Phone.DataActivityState.DATAOUT.ordinal()] = 2;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$android$telephony$Phone$DataActivityState[android.telephony.Phone.DataActivityState.DATAINANDOUT.ordinal()] = 3;
                }
                catch(NoSuchFieldError ex) { }
                $SwitchMap$android$telephony$Phone$DataState = new int[android.telephony.Phone.DataState.values().length];
                try
                {
                    $SwitchMap$android$telephony$Phone$DataState[android.telephony.Phone.DataState.CONNECTED.ordinal()] = 1;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$android$telephony$Phone$DataState[android.telephony.Phone.DataState.DISCONNECTED.ordinal()] = 2;
                }
                catch(NoSuchFieldError ex) { }
            }
        }

        switch(_cls2..SwitchMap.android.telephony.Phone.DataState[state.ordinal()])
        {
        case 1: // '\001'
            mStatus.setImage("data", 0x10600d3);
            break;

        case 2: // '\002'
            mStatus.setVisible("data", false);
            break;

        default:
            mStatus.setImage("data", 0x10600d2);
            break;
        }
    }

    final void updateDataActivity(android.telephony.Phone.DataActivityState state)
    {
        switch(_cls2..SwitchMap.android.telephony.Phone.DataActivityState[state.ordinal()])
        {
        case 1: // '\001'
            mStatus.setImage("data", 0x10600d4);
            break;

        case 2: // '\002'
            mStatus.setImage("data", 0x10600d6);
            break;

        case 3: // '\003'
            mStatus.setImage("data", 0x10600d5);
            break;

        default:
            updateDataConnection(mDataStateReceiver.getConnectionState());
            break;
        }
    }

    private final void updateMWI(boolean flag)
    {
        mNotifications.setVisible("mwi", flag);
    }

    private final void updateSignalStrength(int asu)
    {
        boolean hasService = true;
        ServiceState ss = mPhoneStateReceiver.getServiceState();
        if(ss != null)
        {
            android.telephony.ServiceState.State state = ss.getState();
            switch(_cls2..SwitchMap.android.telephony.ServiceState.State[state.ordinal()])
            {
            case 1: // '\001'
            case 2: // '\002'
                hasService = false;
                break;
            }
        } else
        {
            hasService = false;
        }
        if(!hasService)
        {
            mStatus.setImage("signal", 0x10600e2);
            return;
        }
        if(-1 == asu)
            asu = 0;
        else
        if(asu >= 16)
            asu = 4;
        else
        if(asu >= 8)
            asu = 3;
        else
        if(asu >= 4)
            asu = 2;
        else
            asu = 1;
        mStatus.setImage("signal", sSignalImages[asu]);
    }

    private final void updateClock()
    {
        Calendar c = Calendar.getInstance(mTimeZone);
        mStatus.setText("clock", DateUtils.timeString(c));
    }

    private final void updateVolume()
    {
        int ringerVolumeIndex = android.provider.Settings.System.getInt(mContext.getContentResolver(), "ringer_volume", 2);
        if(ringerVolumeIndex == mLastRingerVolumeIndex)
            return;
        mLastRingerVolumeIndex = ringerVolumeIndex;
        switch(ringerVolumeIndex)
        {
        case 0: // '\0'
            mStatus.setImage("volume", 0x10600db);
            mStatus.setBalloonText("volume", "Silent");
            break;

        case 1: // '\001'
            mStatus.setImage("volume", 0x10600dc);
            mStatus.setBalloonText("volume", "Vibrate");
            break;

        default:
            mStatus.setVisible("volume", false);
            break;
        }
    }

    public void enqueueNotification(String pkg, int id, Notification n)
    {
        mHandler.post(new EnqueueNotification(pkg, id, n));
    }

    public void removeNotification(String pkg, int id)
    {
        mHandler.post(new DequeueNotification(pkg, id));
    }

    private final String getTag(String pkg, int id)
    {
        StringBuilder buf = new StringBuilder(pkg);
        buf.append(id);
        return buf.toString();
    }

    static final String LOG_TAG = "StatusBarMgr";
    private static final int EVENT_SIGNAL_STRENGTH_CHANGED = 100;
    private static final int EVENT_MWI_CHANGED = 200;
    private static final int EVENT_DATA_CONN_STATE_CHANGED = 300;
    private static final int EVENT_DATA_ACTIVITY = 400;
    private static final int sSignalImages[] = {
        0x10600dd, 0x10600de, 0x10600df, 0x10600e0, 0x10600e1
    };
    private static StatusBarManager sSingleton;
    private StatusBarHandler mHandler;
    private Context mContext;
    private TimeZone mTimeZone;
    private PhoneStateIntentReceiver mPhoneStateReceiver;
    private DataStateIntentReceiver mDataStateReceiver;
    private StatusBarList mStatus;
    private StatusBarList mNotifications;
    private int mLastRingerVolumeIndex;
    private IntentReceiver mIntentReceiver;












}

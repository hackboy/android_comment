/**
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.appwidget;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.android.internal.appwidget.IAppWidgetHost;
import com.android.internal.appwidget.IAppWidgetService;

/***
 * 该类提供apps和appwidget service交互的能力，像home screen一样，他想把appwidgets嵌入到他们的ui
 * AppWidgetHost provides the interaction with the AppWidget service for apps,
 * like the home screen, that want to embed AppWidgets in their UI.
 */
//host顾名思义，用来容纳appwidget，比如home就是实现了一个apphost，我们可以实现自己的appwidgethost
//该类的使用方法以及android实现
public class AppWidgetHost {
    //三种消息======》分别用1、2、3代表
    static final int HANDLE_UPDATE = 1;
    static final int HANDLE_PROVIDER_CHANGED = 2;
    static final int HANDLE_VIEW_DATA_CHANGED = 3;
    //这个到底如何实现锁的功能？？
    final static Object sServiceLock = new Object();
    //用到了系统服务
    static IAppWidgetService sService;
    private DisplayMetrics mDisplayMetrics;
    //context由谁提供
    Context mContext;
    //何处用到包名
    String mPackageName;
    //几个回调，在回调里调用handler发送消息，包括发送更新、改变和视图数据改变消息，然后由
    //updatehandler进行处理
    //该回调会被appwidgetservice调用，只是直接发送消息，具体处理还在handler里
    //可以被远程对象调用
    class Callbacks extends IAppWidgetHost.Stub {
        //这三个方法何时会被调用，由谁调用
        public void updateAppWidget(int appWidgetId, RemoteViews views) {
            Message msg = mHandler.obtainMessage(HANDLE_UPDATE);
            msg.arg1 = appWidgetId;
            msg.obj = views;
            msg.sendToTarget();
        }

        public void providerChanged(int appWidgetId, AppWidgetProviderInfo info) {
            Message msg = mHandler.obtainMessage(HANDLE_PROVIDER_CHANGED);
            msg.arg1 = appWidgetId;
            msg.obj = info;
            msg.sendToTarget();
        }

        public void viewDataChanged(int appWidgetId, int viewId) {
            Message msg = mHandler.obtainMessage(HANDLE_VIEW_DATA_CHANGED);
            msg.arg1 = appWidgetId;
            msg.arg2 = viewId;
            msg.sendToTarget();
        }
    }
    //更新处理，供callback使用，和callback类的方法紧密相连
    //处理Callbacks方法发送的消息
    class UpdateHandler extends Handler {
        public UpdateHandler(Looper looper) {
            super(looper);
        }
       //这里面调用的函数从何而来 
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLE_UPDATE: {
                    updateAppWidgetView(msg.arg1, (RemoteViews)msg.obj);
                    break;
                }
                case HANDLE_PROVIDER_CHANGED: {
                    onProviderChanged(msg.arg1, (AppWidgetProviderInfo)msg.obj);
                    break;
                }
                case HANDLE_VIEW_DATA_CHANGED: {
                    viewDataChanged(msg.arg1, msg.arg2);
                    break;
                }
            }
        }
    }
    
    Handler mHandler;
    //host id是什么
    int mHostId;
    //定义一个回调
    Callbacks mCallbacks = new Callbacks();
    //AppWidgetHostView列表
    final HashMap<Integer,AppWidgetHostView> mViews = new HashMap<Integer, AppWidgetHostView>();
    //context由该类实例化者提供
    public AppWidgetHost(Context context, int hostId) {
        mContext = context;
        mHostId = hostId;
        mHandler = new UpdateHandler(context.getMainLooper());
        mDisplayMetrics = context.getResources().getDisplayMetrics();
        synchronized (sServiceLock) {
            if (sService == null) {
                //获得一个系统服务，我现在还是用不好，现在可以了
                //实现一个本地调用接口
                IBinder b = ServiceManager.getService(Context.APPWIDGET_SERVICE);
                sService = IAppWidgetService.Stub.asInterface(b);
            }
        }
    }

    /***
     * Start receiving onAppWidgetChanged calls for your AppWidgets.  Call this when your activity
     * becomes visible, i.e. from onStart() in your Activity.
     */
    //开始进行监听
    public void startListening() {
        //需要监听的appwidgetid
        int[] updatedIds;
        ArrayList<RemoteViews> updatedViews = new ArrayList<RemoteViews>();
        
        try {
            if (mPackageName == null) {
                mPackageName = mContext.getPackageName();
            }
            updatedIds = sService.startListening(mCallbacks, mPackageName, mHostId, updatedViews);
        }
        catch (RemoteException e) {
            throw new RuntimeException("system server dead?", e);
        }

        final int N = updatedIds.length;
        for (int i=0; i<N; i++) {
            updateAppWidgetView(updatedIds[i], updatedViews.get(i));
        }
    }

    /***
     * Stop receiving onAppWidgetChanged calls for your AppWidgets.  Call this when your activity is
     * no longer visible, i.e. from onStop() in your Activity.
     */
    public void stopListening() {
        try {
            sService.stopListening(mHostId);
        }
        catch (RemoteException e) {
            throw new RuntimeException("system server dead?", e);
        }
    }

    /***
     * Get a appWidgetId for a host in the calling process.
     *
     * @return a appWidgetId
     */
    //分配appwidget id
    public int allocateAppWidgetId() {
        try {
            if (mPackageName == null) {
                mPackageName = mContext.getPackageName();
            }
            return sService.allocateAppWidgetId(mPackageName, mHostId);
        }
        catch (RemoteException e) {
            throw new RuntimeException("system server dead?", e);
        }
    }

    /***
     * Stop listening to changes for this AppWidget.  
     */
    public void deleteAppWidgetId(int appWidgetId) {
        synchronized (mViews) {
            mViews.remove(appWidgetId);
            try {
                sService.deleteAppWidgetId(appWidgetId);
            }
            catch (RemoteException e) {
                throw new RuntimeException("system server dead?", e);
            }
        }
    }

    /***
     * Remove all records about this host from the AppWidget manager.
     * <ul>
     *   <li>Call this when initializing your database, as it might be because of a data wipe.</li>
     *   <li>Call this to have the AppWidget manager release all resources associated with your
     *   host.  Any future calls about this host will cause the records to be re-allocated.</li>
     * </ul>
     */
    public void deleteHost() {
        try {
            sService.deleteHost(mHostId);
        }
        catch (RemoteException e) {
            throw new RuntimeException("system server dead?", e);
        }
    }

    /***
     * Remove all records about all hosts for your package.
     * <ul>
     *   <li>Call this when initializing your database, as it might be because of a data wipe.</li>
     *   <li>Call this to have the AppWidget manager release all resources associated with your
     *   host.  Any future calls about this host will cause the records to be re-allocated.</li>
     * </ul>
     */
    public static void deleteAllHosts() {
        try {
            sService.deleteAllHosts();
        }
        catch (RemoteException e) {
            throw new RuntimeException("system server dead?", e);
        }
    }

    /***
     * Create the AppWidgetHostView for the given widget.
     * The AppWidgetHost retains a pointer to the newly-created View.
     */
    //为特定的appwidget创建appwidgethostview
    public final AppWidgetHostView createView(Context context, int appWidgetId,
            AppWidgetProviderInfo appWidget) {
        AppWidgetHostView view = onCreateView(context, appWidgetId, appWidget);
        view.setAppWidget(appWidgetId, appWidget);
        synchronized (mViews) {
            mViews.put(appWidgetId, view);
        }
        RemoteViews views;
        try {
            views = sService.getAppWidgetViews(appWidgetId);
        } catch (RemoteException e) {
            throw new RuntimeException("system server dead?", e);
        }
        view.updateAppWidget(views);
        return view;
    }

    /***
     * Called to create the AppWidgetHostView.  Override to return a custom subclass if you
     * need it.  {@more}
     */
    //可以对其进行覆盖
    protected AppWidgetHostView onCreateView(Context context, int appWidgetId,
            AppWidgetProviderInfo appWidget) {
        return new AppWidgetHostView(context);
    }

    /***
     * Called when the AppWidget provider for a AppWidget has been upgraded to a new apk.
     */
    //当一个appwidget的provider更新到新的apk时调用
    protected void onProviderChanged(int appWidgetId, AppWidgetProviderInfo appWidget) {
        AppWidgetHostView v;

        // Convert complex to dp -- we are getting the AppWidgetProviderInfo from the
        // AppWidgetService, which doesn't have our context, hence we need to do the 
        // conversion here.
        appWidget.minWidth =
            TypedValue.complexToDimensionPixelSize(appWidget.minWidth, mDisplayMetrics);
        appWidget.minHeight =
            TypedValue.complexToDimensionPixelSize(appWidget.minHeight, mDisplayMetrics);
        appWidget.minResizeWidth =
            TypedValue.complexToDimensionPixelSize(appWidget.minResizeWidth, mDisplayMetrics);
        appWidget.minResizeHeight =
            TypedValue.complexToDimensionPixelSize(appWidget.minResizeHeight, mDisplayMetrics);

        synchronized (mViews) {
            v = mViews.get(appWidgetId);
        }
        if (v != null) {
            v.resetAppWidget(appWidget);
        }
    }
    //被hangler调用，处理各个消息
    void updateAppWidgetView(int appWidgetId, RemoteViews views) {
        //又要转到hostview去观察
        AppWidgetHostView v;
        synchronized (mViews) {
            v = mViews.get(appWidgetId);
        }
        if (v != null) {
            v.updateAppWidget(views);
        }
    }

    void viewDataChanged(int appWidgetId, int viewId) {
        AppWidgetHostView v;
        synchronized (mViews) {
            v = mViews.get(appWidgetId);
        }
        if (v != null) {
            v.viewDataChanged(viewId);
        }
    }

    /***
     * Clear the list of Views that have been created by this AppWidgetHost.
     */
    protected void clearViews() {
        mViews.clear();
    }
}




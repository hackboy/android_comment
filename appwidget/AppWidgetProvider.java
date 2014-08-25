/**
 * Copyright (C) 2006 The Android Open Source Project
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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/***
 * 实现一个appwidget provider的便捷类
 * A convenience class to aid in implementing an AppWidget provider.
 * Everything you can do with AppWidgetProvider, you can do with a regular {@link BroadcastReceiver}.
 * AppWidgetProvider merely parses the relevant fields out of the Intent that is received in
 * {@link #onReceive(Context,Intent) onReceive(Context,Intent)}, and calls hook methods
 * with the received extras.
 *
 * <p>Extend this class and override one or more of the {@link #onUpdate}, {@link #onDeleted},
 * {@link #onEnabled} or {@link #onDisabled} methods to implement your own AppWidget functionality.
 * </p>
 * <p>For an example of how to write a AppWidget provider, see the
 * <a href="{@docRoot}guide/topics/appwidgets/index.html#Providers">AppWidgets</a> documentation.</p>
 */
//原来吧，appwidgetprovider是一个广播接收器，源码才是我的爱,我们继承该类，实现自己的widgetprovider，我们需要接收响应广播
//注册该广播进行接收处理
//一般我们要做的是重写该类的onupdate等方法
//我就是一个广播接收器而已，没意思
public class AppWidgetProvider extends BroadcastReceiver {
    /***
     * Constructor to initialize AppWidgetProvider.
     */
    public AppWidgetProvider() {
    }

    /***
     * Implements {@link BroadcastReceiver#onReceive} to dispatch calls to the various
     * other methods on AppWidgetProvider.  
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    // BEGIN_INCLUDE(onReceive)
    //在接收到广播时，会根据不同的action执行不同的函数，我们只要覆盖响应的函数实现自己的功能即可
    //该广播由谁发送，发送时数据如何定义，我想知道
    public void onReceive(Context context, Intent intent) {
        // Protect against rogue update broadcasts (not really a security issue,
        // just filter bad broacasts out so subclasses are less likely to crash).
        String action = intent.getAction();
        //定义了几种appwidget的消息，包括更新、删除、禁用和启用
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
                if (appWidgetIds != null && appWidgetIds.length > 0) {
                    this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
                }
            }
        }
        else if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
                final int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
                this.onDeleted(context, new int[] { appWidgetId });
            }
        }
        else if (AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)) {
            this.onEnabled(context);
        }
        else if (AppWidgetManager.ACTION_APPWIDGET_DISABLED.equals(action)) {
            this.onDisabled(context);
        }
    }
    // END_INCLUDE(onReceive)
    
    /***
     * Called in response to the {@link AppWidgetManager#ACTION_APPWIDGET_UPDATE} broadcast when
     * this AppWidget provider is being asked to provide {@link android.widget.RemoteViews RemoteViews}
     * for a set of AppWidgets.  Override this method to implement your own AppWidget functionality.
     *
     * {@more}
     * 
     * @param context   The {@link android.content.Context Context} in which this receiver is
     *                  running.
     * @param appWidgetManager A {@link AppWidgetManager} object you can call {@link
     *                  AppWidgetManager#updateAppWidget} on.
     * @param appWidgetIds The appWidgetIds for which an update is needed.  Note that this
     *                  may be all of the AppWidget instances for this provider, or just
     *                  a subset of them.
     *
     * @see AppWidgetManager#ACTION_APPWIDGET_UPDATE
     */
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    }
    
    /***
     * Called in response to the {@link AppWidgetManager#ACTION_APPWIDGET_DELETED} broadcast when
     * one or more AppWidget instances have been deleted.  Override this method to implement
     * your own AppWidget functionality.
     *
     * {@more}
     * 
     * @param context   The {@link android.content.Context Context} in which this receiver is
     *                  running.
     * @param appWidgetIds The appWidgetIds that have been deleted from their host.
     *
     * @see AppWidgetManager#ACTION_APPWIDGET_DELETED
     */
    public void onDeleted(Context context, int[] appWidgetIds) {
    }

    /***
     * Called in response to the {@link AppWidgetManager#ACTION_APPWIDGET_ENABLED} broadcast when
     * the a AppWidget for this provider is instantiated.  Override this method to implement your
     * own AppWidget functionality.
     *
     * {@more}
     * When the last AppWidget for this provider is deleted,
     * {@link AppWidgetManager#ACTION_APPWIDGET_DISABLED} is sent by the AppWidget manager, and
     * {@link #onDisabled} is called.  If after that, an AppWidget for this provider is created
     * again, onEnabled() will be called again.
     *
     * @param context   The {@link android.content.Context Context} in which this receiver is
     *                  running.
     *
     * @see AppWidgetManager#ACTION_APPWIDGET_ENABLED
     */
    public void onEnabled(Context context) {
    }

    /***
     * Called in response to the {@link AppWidgetManager#ACTION_APPWIDGET_DISABLED} broadcast, which
     * is sent when the last AppWidget instance for this provider is deleted.  Override this method
     * to implement your own AppWidget functionality.
     *
     * {@more}
     * 
     * @param context   The {@link android.content.Context Context} in which this receiver is
     *                  running.
     *
     * @see AppWidgetManager#ACTION_APPWIDGET_DISABLED
     */
    public void onDisabled(Context context) {
    }
}


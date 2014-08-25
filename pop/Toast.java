/**
 * Copyright (C) 2007 The Android Open Source Project
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

package android.widget;

import android.app.INotificationManager;
import android.app.ITransientNotification;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManagerImpl;

/***
 *作为一种不予用户发生互动的消息通知形式
 */ 
public class Toast {
    static final String TAG = "Toast";
    static final boolean localLOGV = false;

    /***
     * 预定义的显示通知时长
     */
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;

    //初始化handler对象，用于实现线程间通信，这样看来，一个app会有无限的handler吧
    final Handler mHandler = new Handler();

    final Context mCon
    text;

    final TN mTN;  //内部类
    int mDuration;//定义显示时间
    int mGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;//显示位置，默认为底部中间
    int mX, mY;//显示位置，精确x y坐标
    float mHorizontalMargin;
    float mVerticalMargin;
    View mView;     //view代表什么
    View mNextView;//nextview是要显示的toast的内容

    /***
     * 创建一个空的toast对象，在显示该对象之前，必须进行setview
     * toast使用context来获取一定的资源
     */
    public Toast(Context context) {
        mContext = context;
        //实例化一个tn对象
        mTN = new TN();
        mY = context.getResources().getDimensionPixelSize(
                com.android.internal.R.dimen.toast_y_offset);
    }
    
    /***
     * 显示该消息
     */
    public void show() {
        //toast无内容抛出异常
        if (mNextView == null) {
            throw new RuntimeException("setView must have been called");
        }
        //获取系统notification服务，用于显示通知，呵呵
        INotificationManager service = getService();
        //获取包名
        String pkg = mContext.getPackageName();
        //用处何在，总是喜欢定义局部变量
        TN tn = mTN;//这又是个什么玩意

        try {
            //系统管理一个toast队列，进行依次显示，也就是说系统保证toast不会相互覆盖
            //查看notificationmanagerservice获取详细信息
            //通过后台服务进行处理
            //如此这般啊，其实该类没做多少工作
            service.enqueueToast(pkg, tn, mDuration);
        } catch (RemoteException e) {
            // Empty
        }
    }

    /***
     * 手动取消该toast
     */
    public void cancel() {
        mTN.hide();
    }
    

    //toast是可以自定义显示内容的，而不是简单的文字
    public void setView(View view) {
        mNextView = view;
    }

    /***
     * 得到该toast的显示内容
     */
    public View getView() {
        return mNextView;
    }

    /***
     * 设置显示时长
     */
    public void setDuration(int duration) {
        mDuration = duration;
    }

    /***
     * 得到显示时长
     */
    public int getDuration() {
        return mDuration;
    }
    
    /***
    * 设置该对象的margin
    */

    public void setMargin(float horizontalMargin, float verticalMargin) {
        mHorizontalMargin = horizontalMargin;
        mVerticalMargin = verticalMargin;
    }

    /***
     * 返回水平margin
     */
    public float getHorizontalMargin() {
        return mHorizontalMargin;
    }

    /***
     * 返回垂直margin
     */
    public float getVerticalMargin() {
        return mVerticalMargin;
    }

    /***
     * 设置显示位置
     */
    public void setGravity(int gravity, int xOffset, int yOffset) {
        mGravity = gravity;
        mX = xOffset;
        mY = yOffset;
    }

     /***
     * 得到显示位置
     */
    public int getGravity() {
        return mGravity;
    }

    /***
     * x坐标
     */
    public int getXOffset() {
        return mX;
    }
    
    /***
     * y坐标
     */
    public int getYOffset() {
        return mY;
    }
    
    /***
     * 静态方法，创建一个仅包含文本信息的toast，定制性低，方便快捷，构建一个toast对象用于显示而已
     */
    public static Toast makeText(Context context, CharSequence text, int duration) {
        Toast result = new Toast(context);

        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //使用的系统布局
        View v = inflate.inflate(com.android.internal.R.layout.transient_notification, null);
        //用于显示消息的文本框
        TextView tv = (TextView)v.findViewById(com.android.internal.R.id.message);
        tv.setText(text);
        
        result.mNextView = v;
        result.mDuration = duration;

        return result;
    }

    //直接传入字符串资源
    public static Toast makeText(Context context, int resId, int duration)
                                throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    /***
     * 改变文字
     */
    public void setText(int resId) {
        setText(mContext.getText(resId));
    }
    
    /***
     * Update the text in a Toast that was previously created using one of the makeText() methods.
     * @param s The new text for the Toast.
     */
    public void setText(CharSequence s) {
        if (mNextView == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        TextView tv = (TextView) mNextView.findViewById(com.android.internal.R.id.message);
        if (tv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        tv.setText(s);
    }

    // =======================================================================================
    // All the gunk below is the interaction with the Notification Service, which handles
    // the proper ordering of these system-wide.
    // =======================================================================================
    //单例模式
    private static INotificationManager sService;

    static private INotificationManager getService() {
        if (sService != null) {
            return sService;
        }
        //获取一本地接口和service交互,内部使用了notification服务，返回一个服务接口，然后我们就能为所欲为了哎
        //其实我们也可以获取到该对象的说
        sService = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
        return sService;
    }
    //这个实现了一个binder，又是用于进程通信的玩意,该接口定义了show和hide方法
    private class TN extends ITransientNotification.Stub {
        final Runnable mShow = new Runnable() {
            public void run() {
                handleShow();
            }
        };

        final Runnable mHide = new Runnable() {
            public void run() {
                handleHide();
            }
        };

        private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        
        WindowManagerImpl mWM;

        TN() {
            // XXX This should be changed to use a Dialog, with a Theme.Toast
            // defined that sets up the layout params appropriately.
            final WindowManager.LayoutParams params = mParams;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            //这地方决定了你的toast不能获得焦点，也不能点击，而且半透明
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            params.format = PixelFormat.TRANSLUCENT;
            //toast动画，如何更改这个动画，我们没必要去更改，如果要修改该如何实现
            params.windowAnimations = com.android.internal.R.style.Animation_Toast;
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
            params.setTitle("Toast");
        }

        /***
         * 在正确的线程里执行handleshow
         */
        //这个方法其实是必须实现的接口方法
        public void show() {
            if (localLOGV) Log.v(TAG, "SHOW: " + this);
            //在mhandler线程里执行mshow
            mHandler.post(mShow);
        }

        /***
         * 在正确的线程里执行handlehide吗？？
         */
        public void hide() {
            if (localLOGV) Log.v(TAG, "HIDE: " + this);
            mHandler.post(mHide);
        }

        //如何显示该toast，这个才是核心
        public void handleShow() {
            if (localLOGV) Log.v(TAG, "HANDLE SHOW: " + this + " mView=" + mView
                    + " mNextView=" + mNextView);
            if (mView != mNextView) {
                // remove the old view if necessary
                handleHide();
                mView = mNextView;
                mWM = WindowManagerImpl.getDefault();
                final int gravity = mGravity;
                mParams.gravity = gravity;
                if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
                    mParams.horizontalWeight = 1.0f;
                }
                if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
                    mParams.verticalWeight = 1.0f;
                }
                mParams.x = mX;
                mParams.y = mY;
                mParams.verticalMargin = mVerticalMargin;
                mParams.horizontalMargin = mHorizontalMargin;
                if (mView.getParent() != null) {
                    if (localLOGV) Log.v(
                            TAG, "REMOVE! " + mView + " in " + this);
                    mWM.removeView(mView);
                }
                if (localLOGV) Log.v(TAG, "ADD! " + mView + " in " + this);
                //如此这般
                mWM.addView(mView, mParams);
            }
        }

        public void handleHide() {
            if (localLOGV) Log.v(TAG, "HANDLE HIDE: " + this + " mView=" + mView);
            if (mView != null) {
                // note: checking parent() just to make sure the view has
                // been added...  i have seen cases where we get here when
                // the view isn't yet added, so let's try not to crash.
                if (mView.getParent() != null) {
                    if (localLOGV) Log.v(
                            TAG, "REMOVE! " + mView + " in " + this);
                    //如此这般
                    mWM.removeView(mView);
                }

                mView = null;
            }
        }
    }
}


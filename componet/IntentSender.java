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

package android.content;

import android.app.ActivityManagerNative;
import android.content.Context;
import android.content.Intent;
import android.content.IIntentSender;
import android.content.IIntentReceiver;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AndroidException;


/***
 * A description of an Intent and target action to perform with it.
 * The returned object can be
 * handed to other applications so that they can perform the action you
 * described on your behalf at a later time.
 *
 * <p>By giving a IntentSender to another application,
 * you are granting it the right to perform the operation you have specified
 * as if the other application was yourself (with the same permissions and
 * identity).  As such, you should be careful about how you build the IntentSender:
 * often, for example, the base Intent you supply will have the component
 * name explicitly set to one of your own components, to ensure it is ultimately
 * sent there and nowhere else.
 *
 * <p>A IntentSender itself is simply a reference to a token maintained by
 * the system describing the original data used to retrieve it.  This means
 * that, even if its owning application's process is killed, the
 * IntentSender itself will remain usable from other processes that
 * have been given it.  If the creating application later re-retrieves the
 * same kind of IntentSender (same operation, same Intent action, data,
 * categories, and components, and same flags), it will receive a IntentSender
 * representing the same token if that is still valid.
 *
 * <p>Instances of this class can not be made directly, but rather must be
 * created from an existing {@link android.app.PendingIntent} with
 * {@link android.app.PendingIntent#getIntentSender() PendingIntent.getIntentSender()}.
 */
//一个intent和其目标action的描述
public class IntentSender implements Parcelable {

    //其实就是自己
    private final IIntentSender mTarget;

    /***
     * Exception thrown when trying to send through a PendingIntent that
     * has been canceled or is otherwise no longer able to execute the request.
     */
    public static class SendIntentException extends AndroidException {
        public SendIntentException() {
        }

        public SendIntentException(String name) {
            super(name);
        }

        public SendIntentException(Exception cause) {
            super(cause);
        }
    }

    /***
     * Callback interface for discovering when a send operation has
     * completed.  Primarily for use with a IntentSender that is
     * performing a broadcast, this provides the same information as
     * calling {@link Context#sendOrderedBroadcast(Intent, String,
     * android.content.BroadcastReceiver, Handler, int, String, Bundle)
     * Context.sendBroadcast()} with a final BroadcastReceiver.
     */
    //send操作完成后调用
    public interface OnFinished {
        /***
         * Called when a send operation as completed.
         *
         * @param IntentSender The IntentSender this operation was sent through.
         * @param intent The original Intent that was sent.
         * @param resultCode The final result code determined by the send.
         * @param resultData The final data collected by a broadcast.
         * @param resultExtras The final extras collected by a broadcast.
         */
        void onSendFinished(IntentSender IntentSender, Intent intent,
                int resultCode, String resultData, Bundle resultExtras);
    }

    //intentreceiver
    private static class FinishedDispatcher extends IIntentReceiver.Stub
            implements Runnable {
        private final IntentSender mIntentSender;
        private final OnFinished mWho;
        private final Handler mHandler;
        private Intent mIntent;
        private int mResultCode;
        private String mResultData;
        private Bundle mResultExtras;
        FinishedDispatcher(IntentSender pi, OnFinished who, Handler handler) {
            mIntentSender = pi;
            mWho = who;
            mHandler = handler;
        }
        public void performReceive(Intent intent, int resultCode,
                String data, Bundle extras, boolean serialized, boolean sticky) {
            mIntent = intent;
            mResultCode = resultCode;
            mResultData = data;
            mResultExtras = extras;
            if (mHandler == null) {
                run();
            } else {
                mHandler.post(this);
            }
        }
        public void run() {
            mWho.onSendFinished(mIntentSender, mIntent, mResultCode,
                    mResultData, mResultExtras);
        }
    }

    /***
     * Perform the operation associated with this IntentSender, allowing the
     * caller to specify information about the Intent to use and be notified
     * when the send has completed.
     *
     * @param context The Context of the caller.  This may be null if
     * <var>intent</var> is also null.
     * @param code Result code to supply back to the IntentSender's target.
     * @param intent Additional Intent data.  See {@link Intent#fillIn
     * Intent.fillIn()} for information on how this is applied to the
     * original Intent.  Use null to not modify the original Intent.
     * @param onFinished The object to call back on when the send has
     * completed, or null for no callback.
     * @param handler Handler identifying the thread on which the callback
     * should happen.  If null, the callback will happen from the thread
     * pool of the process.
     *
     *
     * @throws SendIntentException Throws CanceledIntentException if the IntentSender
     * is no longer allowing more intents to be sent through it.
     */
    public void sendIntent(Context context, int code, Intent intent,
            OnFinished onFinished, Handler handler) throws SendIntentException {
        sendIntent(context, code, intent, onFinished, handler, null);
    }

    /***
     * Perform the operation associated with this IntentSender, allowing the
     * caller to specify information about the Intent to use and be notified
     * when the send has completed.
     *
     * @param context The Context of the caller.  This may be null if
     * <var>intent</var> is also null.
     * @param code Result code to supply back to the IntentSender's target.
     * @param intent Additional Intent data.  See {@link Intent#fillIn
     * Intent.fillIn()} for information on how this is applied to the
     * original Intent.  Use null to not modify the original Intent.
     * @param onFinished The object to call back on when the send has
     * completed, or null for no callback.
     * @param handler Handler identifying the thread on which the callback
     * should happen.  If null, the callback will happen from the thread
     * pool of the process.
     * @param requiredPermission Name of permission that a recipient of the PendingIntent
     * is required to hold.  This is only valid for broadcast intents, and
     * corresponds to the permission argument in
     * {@link Context#sendBroadcast(Intent, String) Context.sendOrderedBroadcast(Intent, String)}.
     * If null, no permission is required.
     *
     *
     * @throws SendIntentException Throws CanceledIntentException if the IntentSender
     * is no longer allowing more intents to be sent through it.
     */
    //发送该intent
    public void sendIntent(Context context, int code, Intent intent,
            OnFinished onFinished, Handler handler, String requiredPermission)
            throws SendIntentException {
        try {
            String resolvedType = intent != null ?
                    intent.resolveTypeIfNeeded(context.getContentResolver())
                    : null;
            int res = mTarget.send(code, intent, resolvedType,
                    onFinished != null
                            ? new FinishedDispatcher(this, onFinished, handler)
                            : null,
                    requiredPermission);
            if (res < 0) {
                throw new SendIntentException();
            }
        } catch (RemoteException e) {
            throw new SendIntentException();
        }
    }

    /***
     * Return the package name of the application that created this
     * IntentSender, that is the identity under which you will actually be
     * sending the Intent.  The returned string is supplied by the system, so
     * that an application can not spoof its package.
     *
     * @return The package name of the PendingIntent, or null if there is
     * none associated with it.
     */
    //获取创建该intentsender对象的程序
    public String getTargetPackage() {
        try {
            return ActivityManagerNative.getDefault()
                .getPackageForIntentSender(mTarget);
        } catch (RemoteException e) {
            // Should never happen.
            return null;
        }
    }

    /***
     * Comparison operator on two IntentSender objects, such that true
     * is returned then they both represent the same operation from the
     * same package.
     */
    @Override
    public boolean equals(Object otherObj) {
        if (otherObj instanceof IntentSender) {
            return mTarget.asBinder().equals(((IntentSender)otherObj)
                    .mTarget.asBinder());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return mTarget.asBinder().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("IntentSender{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(": ");
        sb.append(mTarget != null ? mTarget.asBinder() : null);
        sb.append('}');
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeStrongBinder(mTarget.asBinder());
    }

    public static final Parcelable.Creator<IntentSender> CREATOR
            = new Parcelable.Creator<IntentSender>() {
        public IntentSender createFromParcel(Parcel in) {
            IBinder target = in.readStrongBinder();
            return target != null ? new IntentSender(target) : null;
        }

        public IntentSender[] newArray(int size) {
            return new IntentSender[size];
        }
    };

    /***
     * Convenience function for writing either a IntentSender or null pointer to
     * a Parcel.  You must use this with {@link #readIntentSenderOrNullFromParcel}
     * for later reading it.
     *
     * @param sender The IntentSender to write, or null.
     * @param out Where to write the IntentSender.
     */
    public static void writeIntentSenderOrNullToParcel(IntentSender sender,
            Parcel out) {
        out.writeStrongBinder(sender != null ? sender.mTarget.asBinder()
                : null);
    }

    /***
     * Convenience function for reading either a Messenger or null pointer from
     * a Parcel.  You must have previously written the Messenger with
     * {@link #writeIntentSenderOrNullToParcel}.
     *
     * @param in The Parcel containing the written Messenger.
     *
     * @return Returns the Messenger read from the Parcel, or null if null had
     * been written.
     */
    public static IntentSender readIntentSenderOrNullFromParcel(Parcel in) {
        IBinder b = in.readStrongBinder();
        return b != null ? new IntentSender(b) : null;
    }

    /*** @hide */
    public IIntentSender getTarget() {
        return mTarget;
    }

    /*** @hide */
    public IntentSender(IIntentSender target) {
        mTarget = target;
    }

    /*** @hide */
    public IntentSender(IBinder target) {
        mTarget = IIntentSender.Stub.asInterface(target);
    }
}

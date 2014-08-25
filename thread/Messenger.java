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

package android.os;

//不同进程间基于消息的通信，底层还是通过binder，不过比较轻量级
public final class Messenger implements Parcelable {
    //持有一个IMessenger对象，不知道是什么
    private final IMessenger mTarget;

    //handler的getIMessenger方法。。。
    public Messenger(Handler target) {
        mTarget = target.getIMessenger();
    }
    
    //通过IMessenger向handler所在进程发送消息
    public void send(Message message) throws RemoteException {
        mTarget.send(message);
    }
    
    //获取该IMessenger所使用的binder对象
    public IBinder getBinder() {
        //返回和其绑定handler交互的binder对象
        return mTarget.asBinder();
    }
    
    /***
     * Comparison operator on two Messenger objects, such that true
     * is returned then they both point to the same Handler.
     */
    public boolean equals(Object otherObj) {
        if (otherObj == null) {
            return false;
        }
        try {
            return mTarget.asBinder().equals(((Messenger)otherObj)
                    .mTarget.asBinder());
        } catch (ClassCastException e) {
        }
        return false;
    }

    public int hashCode() {
        return mTarget.asBinder().hashCode();
    }
    
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeStrongBinder(mTarget.asBinder());
    }

    public static final Parcelable.Creator<Messenger> CREATOR
            = new Parcelable.Creator<Messenger>() {
        public Messenger createFromParcel(Parcel in) {
            IBinder target = in.readStrongBinder();
            return target != null ? new Messenger(target) : null;
        }

        public Messenger[] newArray(int size) {
            return new Messenger[size];
        }
    };

    /***
     * Convenience function for writing either a Messenger or null pointer to
     * a Parcel.  You must use this with {@link #readMessengerOrNullFromParcel}
     * for later reading it.
     * 
     * @param messenger The Messenger to write, or null.
     * @param out Where to write the Messenger.
     */
    public static void writeMessengerOrNullToParcel(Messenger messenger,
            Parcel out) {
        out.writeStrongBinder(messenger != null ? messenger.mTarget.asBinder()
                : null);
    }
    
    /***
     * Convenience function for reading either a Messenger or null pointer from
     * a Parcel.  You must have previously written the Messenger with
     * {@link #writeMessengerOrNullToParcel}.
     * 
     * @param in The Parcel containing the written Messenger.
     * 
     * @return Returns the Messenger read from the Parcel, or null if null had
     * been written.
     */
    public static Messenger readMessengerOrNullFromParcel(Parcel in) {
        IBinder b = in.readStrongBinder();
        return b != null ? new Messenger(b) : null;
    }
    
    /***
     * Create a Messenger from a raw IBinder, which had previously been
     * retrieved with {@link #getBinder}.
     * 
     * @param target The IBinder this Messenger should communicate with.
     */
    //呵呵
    public Messenger(IBinder target) {
        mTarget = IMessenger.Stub.asInterface(target);
    }
}


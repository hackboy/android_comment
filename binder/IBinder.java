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

import java.io.FileDescriptor;
import java.io.PrintWriter;

/***
 // 远程对象的基本接口，执行进程内和跨进程调用时的一种高性能轻量级的rpc机制的核心部分
 * Base interface for a remotable object, the core part of a lightweight
 * remote procedure call mechanism designed for high performance when
 * performing in-process and cross-process calls.  This
 * interface describes the abstract protocol for interacting with a
 * remotable object.  Do not implement this interface directly, instead
 * extend from {@link Binder}.
 * 
 * <p>The key IBinder API is {@link #transact transact()} matched by
 * {@link Binder#onTransact Binder.onTransact()}.  These
 * methods allow you to send a call to an IBinder object and receive a
 * call coming in to a Binder object, respectively.  This transaction API
 * is synchronous, such that a call to {@link #transact transact()} does not
 * return until the target has returned from
 * {@link Binder#onTransact Binder.onTransact()}; this is the
 * expected behavior when calling an object that exists in the local
 * process, and the underlying inter-process communication (IPC) mechanism
 * ensures that these same semantics apply when going across processes.
 * //通过transact传输的数据是parcel
 * <p>The data sent through transact() is a {@link Parcel}, a generic buffer
 * of data that also maintains some meta-data about its contents.  The meta
 * data is used to manage IBinder object references in the buffer, so that those
 * references can be maintained as the buffer moves across processes.  This
 * mechanism ensures that when an IBinder is written into a Parcel and sent to
 * another process, if that other process sends a reference to that same IBinder
 * back to the original process, then the original process will receive the
 * same IBinder object back.  These semantics allow IBinder/Binder objects to
 * be used as a unique identity (to serve as a token or for other purposes)
 * that can be managed across processes.
 * 
 * <p>The system maintains a pool of transaction threads in each process that
 * it runs in.  These threads are used to dispatch all
 * IPCs coming in from other processes.  For example, when an IPC is made from
 * process A to process B, the calling thread in A blocks in transact() as
 * it sends the transaction to process B.  The next available pool thread in
 * B receives the incoming transaction, calls Binder.onTransact() on the target
 * object, and replies with the result Parcel.  Upon receiving its result, the
 * thread in process A returns to allow its execution to continue.  In effect,
 * other processes appear to use as additional threads that you did not create
 * executing in your own process.
 * 
 * <p>The Binder system also supports recursion across processes.  For example
 * if process A performs a transaction to process B, and process B while
 * handling that transaction calls transact() on an IBinder that is implemented
 * in A, then the thread in A that is currently waiting for the original
 * transaction to finish will take care of calling Binder.onTransact() on the
 * object being called by B.  This ensures that the recursion semantics when
 * calling remote binder object are the same as when calling local objects.
 * 
 * <p>When working with remote objects, you often want to find out when they
 * are no longer valid.  There are three ways this can be determined:
 * <ul>
 * <li> The {@link #transact transact()} method will throw a
 * {@link RemoteException} exception if you try to call it on an IBinder
 * whose process no longer exists.
 * <li> The {@link #pingBinder()} method can be called, and will return false
 * if the remote process no longer exists.
 * <li> The {@link #linkToDeath linkToDeath()} method can be used to register
 * a {@link DeathRecipient} with the IBinder, which will be called when its
 * containing process goes away.
 * </ul>
 * 
 * @see Binder
 */
public interface IBinder {
    //code范围0x00000001-----0x00ffffff,且从0x00000001开始
    int FIRST_CALL_TRANSACTION  = 0x00000001;
    int LAST_CALL_TRANSACTION   = 0x00ffffff;
    
    /***
     * IBinder protocol transaction code: pingBinder().
     */
    int PING_TRANSACTION        = ('_'<<24)|('P'<<16)|('N'<<8)|'G';
    
    /***
     * IBinder protocol transaction code: dump internal state.
     */
    int DUMP_TRANSACTION        = ('_'<<24)|('D'<<16)|('M'<<8)|'P';
    
    /***
     * IBinder protocol transaction code: interrogate the recipient side
     * of the transaction for its canonical interface descriptor.
     */
    int INTERFACE_TRANSACTION   = ('_'<<24)|('N'<<16)|('T'<<8)|'F';

    /***
     * IBinder protocol transaction code: send a tweet to the target
     * object.  The data in the parcel is intended to be delivered to
     * a shared messaging service associated with the object; it can be
     * anything, as long as it is not more than 130 UTF-8 characters to
     * conservatively fit within common messaging services.  As part of
     * {@link Build.VERSION_CODES#HONEYCOMB_MR2}, all Binder objects are
     * expected to support this protocol for fully integrated tweeting
     * across the platform.  To support older code, the default implementation
     * logs the tweet to the main log as a simple emulation of broadcasting
     * it publicly over the Internet.
     * 
     * <p>Also, upon completing the dispatch, the object must make a cup
     * of tea, return it to the caller, and exclaim "jolly good message
     * old boy!".
     */
    int TWEET_TRANSACTION   = ('_'<<24)|('T'<<16)|('W'<<8)|'T';

    /***
     * Flag to {@link #transact}: this is a one-way call, meaning that the
     * caller returns immediately, without waiting for a result from the
     * callee. Applies only if the caller and callee are in different
     * processes.
     */
    int FLAG_ONEWAY             = 0x00000001;
    
    /***
     * Get the canonical name of the interface supported by this binder.
     */
    //获取该binder对象支持的接口的正式名称，一般会实现一个接口
    public String getInterfaceDescriptor() throws RemoteException;

    //该对象是否存在
    public boolean pingBinder();

    //该binder所在进程是否激活
    public boolean isBinderAlive();
    
    /***
     * Attempt to retrieve a local implementation of an interface
     * for this Binder object.  If null is returned, you will need
     * to instantiate a proxy class to marshall calls through
     * the transact() method.
     */
    //获取该bider对象接口的本地实现
    //如果返回值为空，那么你需要实例化一个代理类来引导transact方法？还是不理解
    public IInterface queryLocalInterface(String descriptor);
    
    /***
     * Print the object's state into the given stream.
     * 
     * @param fd The raw file descriptor that the dump is being sent to.
     * @param args additional arguments to the dump request.
     */
    public void dump(FileDescriptor fd, String[] args) throws RemoteException;
    
    /***
     * Like {@link #dump(FileDescriptor, String[])} but always executes
     * asynchronously.  If the object is local, a new thread is created
     * to perform the dump.
     *
     * @param fd The raw file descriptor that the dump is being sent to.
     * @param args additional arguments to the dump request.
     */
    public void dumpAsync(FileDescriptor fd, String[] args) throws RemoteException;

    /***
     * Perform a generic operation with the object.
     * 
     * @param code The action to perform.  This should
     * be a number between {@link #FIRST_CALL_TRANSACTION} and
     * {@link #LAST_CALL_TRANSACTION}.
     * @param data Marshalled data to send to the target.  Must not be null.
     * If you are not sending any data, you must create an empty Parcel
     * that is given here.
     * @param reply Marshalled data to be received from the target.  May be
     * null if you are not interested in the return value.
     * @param flags Additional operation flags.  Either 0 for a normal
     * RPC, or {@link #FLAG_ONEWAY} for a one-way RPC.
     */
    //什么什么，理解不了
    public boolean transact(int code, Parcel data, Parcel reply, int flags)
        throws RemoteException;

    /***
     * Interface for receiving a callback when the process hosting an IBinder
     * has gone away.
     * 
     * @see #linkToDeath
     */
    //该binder依附的进程消失的回调
    public interface DeathRecipient {
        public void binderDied();
    }

    /***
     * Register the recipient for a notification if this binder
     * goes away.  If this binder object unexpectedly goes away
     * (typically because its hosting process has been killed),
     * then the given {@link DeathRecipient}'s
     * {@link DeathRecipient#binderDied DeathRecipient.binderDied()} method
     * will be called.
     * 
     * <p>You will only receive death notifications for remote binders,
     * as local binders by definition can't die without you dying as well.
     * 
     * @throws Throws {@link RemoteException} if the target IBinder's
     * process has already died.
     * 
     * @see #unlinkToDeath
     */
    public void linkToDeath(DeathRecipient recipient, int flags)
            throws RemoteException;

    /***
     * Remove a previously registered death notification.
     * The recipient will no longer be called if this object
     * dies.
     * 
     * @return Returns true if the <var>recipient</var> is successfully
     * unlinked, assuring you that its
     * {@link DeathRecipient#binderDied DeathRecipient.binderDied()} method
     * will not be called.  Returns false if the target IBinder has already
     * died, meaning the method has been (or soon will be) called.
     * 
     * @throws Throws {@link java.util.NoSuchElementException} if the given
     * <var>recipient</var> has not been registered with the IBinder, and
     * the IBinder is still alive.  Note that if the <var>recipient</var>
     * was never registered, but the IBinder has already died, then this
     * exception will <em>not</em> be thrown, and you will receive a false
     * return value instead.
     */
    public boolean unlinkToDeath(DeathRecipient recipient, int flags);
}


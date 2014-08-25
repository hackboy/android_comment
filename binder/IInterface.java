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

/***
 * binder接口的基类，当定义一个新接口时，你必须从IInteface获取到他。
 * Base class for Binder interfaces.  When defining a new interface,
 * you must derive it from IInterface.
 */
//asBinder方法
public interface IInterface
{
    /***
     * 获取和该接口绑定的binder对象
     * You must use this instead of a plain cast, so that proxy objects
     * can return the correct result.
     */
    //返回一个binder对象，asbinder具体的实现
    //binder对象的系统级的处理是什么样的，我实在是理解不了
    //返回一个IBinder对象，该binder具体的作用是什么
    public IBinder asBinder();
}


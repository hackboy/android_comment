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

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/***
 * Common base class of common implementation for an {@link Adapter} that can be
 * used in both {@link ListView} (by implementing the specialized
 * {@link ListAdapter} interface} and {@link Spinner} (by implementing the
 * specialized {@link SpinnerAdapter} interface.
 */
public abstract class BaseAdapter implements ListAdapter, SpinnerAdapter {
    
    private final DataSetObservable mDataSetObservable = new DataSetObservable();
    
    public boolean hasStableIds() {
        return false;
    }
    
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }
    
    //使用该方法更新视图
    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    /***
     * Notifies the attached observers that the underlying data is no longer valid
     * or available. Once invoked this adapter is no longer valid and should
     * not report further data set changes.
     */
    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    public boolean areAllItemsEnabled() {
        return true;
    }

    public boolean isEnabled(int position) {
        return true;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
    //返回0，到底什么意思,，也理解了，我们把这个类型定义为0,随便定义，who care
    public int getItemViewType(int position) {
        return 0;
    }
    //数量为1，也不理解,理解了，就是只有一种view类型
    public int getViewTypeCount() {
        return 1;
    }
    //简单定义
    public boolean isEmpty() {
        return getCount() == 0;
    }
}


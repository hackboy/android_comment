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

package android.widget;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/***
 * 一个适配器是数据和适配器视图的桥梁，适配器提供访问数据项的能力，并且为每条数据指定视图
 */
//好好学习这种模式，同时好好学习接口的用法等,这是什么模式呢，包含了好几种吧？？但真心不像是适配器模式
public interface Adapter {
    
    void registerDataSetObserver(DataSetObserver observer);

    void unregisterDataSetObserver(DataSetObserver observer);

    int getCount();   
    

    Object getItem(int position);
    
    long getItemId(int position);
    
    //有何用处
    boolean hasStableIds();
    
    //可以进一步优化吧
    View getView(int position, View convertView, ViewGroup parent);

    //这个是一种view类型
    static final int IGNORE_ITEM_VIEW_TYPE = AdapterView.ITEM_VIEW_TYPE_IGNORE;
    
    int getItemViewType(int position);
    
    int getViewTypeCount();
    
    static final int NO_SELECTION = Integer.MIN_VALUE;
 
     boolean isEmpty();
}



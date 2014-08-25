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

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/***
 * 拥有两个一次只能显示一个的控件，视图可以自行添加或者有viewfactory生成
 */
//视图切换
public class ViewSwitcher extends ViewAnimator {
    /***
     * 用于创建两个视图的工厂
     */
    ViewFactory mFactory;

    /***
     * Creates a new empty ViewSwitcher.
     *
     * @param context the application's environment
     */
    public ViewSwitcher(Context context) {
        super(context);
    }

    /***
     * Creates a new empty ViewSwitcher for the given context and with the
     * specified set attributes.
     *
     * @param context the application environment
     * @param attrs a collection of attributes
     */
    public ViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /***
     * {@inheritDoc}
     *
     * @throws IllegalStateException if this switcher already contains two children
     */
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() >= 2) {
            throw new IllegalStateException("Can't add more than 2 views to a ViewSwitcher");
        }
        super.addView(child, index, params);
    }

    /***
     * 获取下一个要显示的视图对象
     */
    //所以用index==0?1:0进行切换即可
    public View getNextView() {
        int which = mWhichChild == 0 ? 1 : 0;
        return getChildAt(which);
    }
    //获取子视图
    private View obtainView() {
        //通过factory创建视图对象
        View child = mFactory.makeView();
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        addView(child, lp);
        return child;
    }

    /***
     * Sets the factory used to create the two views between which the
     * ViewSwitcher will flip. Instead of using a factory, you can call
     * {@link #addView(android.view.View, int, android.view.ViewGroup.LayoutParams)}
     * twice.
     *
     * @param factory the view factory used to generate the switcher's content
     */
    public void setFactory(ViewFactory factory) {
        mFactory = factory;
        //生成两个子视图
        obtainView();
        obtainView();
    }

    /***
     * Reset the ViewSwitcher to hide all of the existing views and to make it
     * think that the first time animation has not yet played.
     */
    public void reset() {
        mFirstTime = true;
        View v;
        v = getChildAt(0);
        if (v != null) {
            v.setVisibility(View.GONE);
        }
        v = getChildAt(1);
        if (v != null) {
            v.setVisibility(View.GONE);
        }
    }

    /***
     * Creates views in a ViewSwitcher.
     */
    public interface ViewFactory {
        /***
         * 该视图如何生成
         */
        View makeView();
    }
}



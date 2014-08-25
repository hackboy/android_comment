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

package android.view.animation;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

/***
 * An animation that controls the alpha level of an object.
 * Useful for fading things in and out. This animation ends up
 * changing the alpha property of a {@link Transformation}
 *
 */
public class AlphaAnimation extends Animation {
    private float mFromAlpha;
    private float mToAlpha;

    /***
     * 从一个xml文件创建动画
     */
    public AlphaAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        TypedArray a =
            context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.AlphaAnimation);
        
        mFromAlpha = a.getFloat(com.android.internal.R.styleable.AlphaAnimation_fromAlpha, 1.0f);
        mToAlpha = a.getFloat(com.android.internal.R.styleable.AlphaAnimation_toAlpha, 1.0f);
        
        a.recycle();
    }
    
    /***
     * 直接用代码创建动画
     */
    public AlphaAnimation(float fromAlpha, float toAlpha) {
        mFromAlpha = fromAlpha;
        mToAlpha = toAlpha;
    }
    
    /***
     * 透明度转换
     */
    //实现透明度动画的关键代码
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float alpha = mFromAlpha;
        //得到一个alpha值，啊哈，根据差值，设定alpha的值，用fromalpha-toalpha的差值去计算当前的alpha值
        t.setAlpha(alpha + ((mToAlpha - alpha) * interpolatedTime));
    }

    @Override
    public boolean willChangeTransformationMatrix() {
        return false;
    }

    @Override
    public boolean willChangeBounds() {
        return false;
    }
}


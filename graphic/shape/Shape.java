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

package android.graphics.drawable.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

//这其中使用的设计模式
public abstract class Shape implements Cloneable {
    //该形状的高度和宽度
    private float mWidth;
    private float mHeight;
    
    /***
     * Returns the width of the Shape.
     */
    public final float getWidth() {
        return mWidth;
    }
    
    /***
     * Returns the height of the Shape.
     */
    public final float getHeight() {
        return mHeight;
    }


    //提供一个画笔和画布，剩下的就是如何绘制
    public abstract void draw(Canvas canvas, Paint paint);
    

   //没啥
    public final void resize(float width, float height) {
        if (width < 0) {
            width = 0;
        }
        if (height < 0) {
            height =0;
        }
        if (mWidth != width || mHeight != height) {
            mWidth = width;
            mHeight = height;
            onResize(width, height);
        }
    }
    
    
    public boolean hasAlpha() {
        return true;
    }
    
    //什么用
    protected void onResize(float width, float height) {}

    @Override
    public Shape clone() throws CloneNotSupportedException {
        return (Shape) super.clone();
    }
}


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
import android.graphics.RectF;

/***
 * 定义一个矩形形状
 */
public class RectShape extends Shape {
    //定义一个矩形区域
    private RectF mRect = new RectF();
    
    
    public RectShape() {}
    //直接调用传递的画布的drawrect方法
    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(mRect, paint);
    }
    //根据传入数据，初始化一个区域
    @Override
    protected void onResize(float width, float height) {
        mRect.set(0, 0, width, height);
    }
    
    /***
     * Returns the RectF that defines this rectangle's bounds.
     */
    protected final RectF rect() {
        return mRect;
    }

    //原型模式
    @Override
    public RectShape clone() throws CloneNotSupportedException {
        final RectShape shape = (RectShape) super.clone();
        shape.mRect = new RectF(mRect);
        return shape;
    }
}


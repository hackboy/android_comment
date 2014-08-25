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

package android.graphics;

import java.io.InputStream;
import java.io.OutputStream;

/***
 * A picture records drawing calls (via the canvas returned by beginRecording)
 * and can then play them back (via picture.draw(canvas) or canvas.drawPicture).
 * The picture's contents can also be written to a stream, and then later
 * restored to a new picture (via writeToStream / createFromStream). For most
 * content (esp. text, lines, rectangles), drawing a sequence from a picture can
 * be faster than the equivalent API calls, since the picture performs its
 * playback without incurring any java-call overhead.
 */
public class Picture {
    private Canvas mRecordingCanvas;
    private final int mNativePicture;

    private static final int WORKING_STREAM_STORAGE = 16 * 1024;

    public Picture() {
        this(nativeConstructor(0));
    }

    /***
     * Create a picture by making a copy of what has already been recorded in
     * src. The contents of src are unchanged, and if src changes later, those
     * changes will not be reflected in this picture.
     */
    public Picture(Picture src) {
        this(nativeConstructor(src != null ? src.mNativePicture : 0));
    }
    
    /***
     * To record a picture, call beginRecording() and then draw into the Canvas
     * that is returned. Nothing we appear on screen, but all of the draw
     * commands (e.g. drawRect(...)) will be recorded. To stop recording, call
     * endRecording(). At this point the Canvas that was returned must no longer
     * be referenced, and nothing should be drawn into it.
     */
    public Canvas beginRecording(int width, int height) {
        int ni = nativeBeginRecording(mNativePicture, width, height);
        mRecordingCanvas = new RecordingCanvas(this, ni);
        return mRecordingCanvas;
    }
    
    /***
     * Call endRecording when the picture is built. After this call, the picture
     * may be drawn, but the canvas that was returned by beginRecording must not
     * be referenced anymore. This is automatically called if Picture.draw() or
     * Canvas.drawPicture() is called.
     */
    public void endRecording() {
        if (mRecordingCanvas != null) {
            mRecordingCanvas = null;
            nativeEndRecording(mNativePicture);
        }
    }

    /***
     * Get the width of the picture as passed to beginRecording. This
     * does not reflect (per se) the content of the picture.
     */
    public native int getWidth();

    /***
     * Get the height of the picture as passed to beginRecording. This
     * does not reflect (per se) the content of the picture.
     */
    public native int getHeight();
    
    /***
     * Draw this picture on the canvas. The picture may have the side effect
     * of changing the matrix and clip of the canvas.
     * 
     * @param canvas  The picture is drawn to this canvas 
     */
    public void draw(Canvas canvas) {
        if (mRecordingCanvas != null) {
            endRecording();
        }
        nativeDraw(canvas.mNativeCanvas, mNativePicture);
    }

    /***
     * Create a new picture (already recorded) from the data in the stream. This
     * data was generated by a previous call to writeToStream().
     */
    public static Picture createFromStream(InputStream stream) {
        return new Picture(
            nativeCreateFromStream(stream, new byte[WORKING_STREAM_STORAGE]));
    }

    /***
     * Write the picture contents to a stream. The data can be used to recreate
     * the picture in this or another process by calling createFromStream.
     */
    public void writeToStream(OutputStream stream) {
        // do explicit check before calling the native method
        if (stream == null) {
            throw new NullPointerException();
        }
        if (!nativeWriteToStream(mNativePicture, stream,
                             new byte[WORKING_STREAM_STORAGE])) {
            throw new RuntimeException();
        }
    }

    protected void finalize() throws Throwable {
        nativeDestructor(mNativePicture);
    }
    
    /**package*/ final int ni() {
        return mNativePicture;
    }
    
    private Picture(int nativePicture) {
        if (nativePicture == 0) {
            throw new RuntimeException();
        }
        mNativePicture = nativePicture;
    }

    // return empty picture if src is 0, or a copy of the native src
    private static native int nativeConstructor(int nativeSrcOr0);
    private static native int nativeCreateFromStream(InputStream stream,
                                                byte[] storage);
    private static native int nativeBeginRecording(int nativeCanvas,
                                                    int w, int h);
    private static native void nativeEndRecording(int nativeCanvas);
    private static native void nativeDraw(int nativeCanvas, int nativePicture);
    private static native boolean nativeWriteToStream(int nativePicture,
                                           OutputStream stream, byte[] storage);
    private static native void nativeDestructor(int nativePicture);
    
    private static class RecordingCanvas extends Canvas {
        private final Picture mPicture;

        public RecordingCanvas(Picture pict, int nativeCanvas) {
            super(nativeCanvas);
            mPicture = pict;
        }
        
        @Override
        public void setBitmap(Bitmap bitmap) {
            throw new RuntimeException(
                                "Cannot call setBitmap on a picture canvas");
        }

        @Override
        public void drawPicture(Picture picture) {
            if (mPicture == picture) {
                throw new RuntimeException(
                            "Cannot draw a picture into its recording canvas");
            }
            super.drawPicture(picture);
        }
    }
}



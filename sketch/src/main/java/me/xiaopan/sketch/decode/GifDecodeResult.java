/*
 * Copyright (C) 2017 Peng fei Pan <sky@xiaopan.me>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.xiaopan.sketch.decode;

import me.xiaopan.sketch.cache.BitmapPool;
import me.xiaopan.sketch.drawable.ImageAttrs;
import me.xiaopan.sketch.drawable.SketchGifDrawable;
import me.xiaopan.sketch.request.ImageFrom;

public class GifDecodeResult implements DecodeResult {
    private SketchGifDrawable gifDrawable;
    private ImageAttrs imageAttrs;
    private ImageFrom imageFrom;

    private boolean banProcess;
    private boolean processed;

    public GifDecodeResult(ImageAttrs imageAttrs, SketchGifDrawable gifDrawable) {
        this.imageAttrs = imageAttrs;
        this.gifDrawable = gifDrawable;
    }

    @Override
    public ImageAttrs getImageAttrs() {
        return imageAttrs;
    }

    @Override
    public ImageFrom getImageFrom() {
        return imageFrom;
    }

    @Override
    public void setImageFrom(ImageFrom imageFrom) {
        this.imageFrom = imageFrom;
    }

    @Override
    public boolean isBanProcess() {
        return banProcess;
    }

    @Override
    public GifDecodeResult setBanProcess(boolean banProcess) {
        this.banProcess = banProcess;
        return this;
    }

    @Override
    public boolean isProcessed() {
        return processed;
    }

    @Override
    public GifDecodeResult setProcessed(boolean processed) {
        this.processed = processed;
        return this;
    }

    @Override
    public void recycle(BitmapPool bitmapPool) {
        if (gifDrawable != null) {
            gifDrawable.recycle();
        }
    }

    public SketchGifDrawable getGifDrawable() {
        return gifDrawable;
    }
}

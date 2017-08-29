/*
 * Copyright (C) 2013 Peng fei Pan <sky@xiaopan.me>
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

package me.xiaopan.sketch.display;

import android.graphics.drawable.Drawable;

import me.xiaopan.sketch.Identifier;
import me.xiaopan.sketch.request.ImageViewInterface;

/**
 * 图片显示器，用来在加载完成后显示图片
 */
public interface ImageDisplayer extends Identifier {
    int DEFAULT_ANIMATION_DURATION = 400;

    /**
     * 显示
     */
    void display(ImageViewInterface imageViewInterface, Drawable newDrawable);

    /**
     * 获取持续时间，单位毫秒
     */
    int getDuration();

    /**
     * 只要涉及到显示图片就得使用ImageDisplayer（显示从内存里取出的缓存图片时也不例外）
     */
    boolean isAlwaysUse();
}
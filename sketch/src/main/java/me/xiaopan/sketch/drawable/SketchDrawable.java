/*
 * Copyright (C) 2016 Peng fei Pan <sky@xiaopan.me>
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

package me.xiaopan.sketch.drawable;

import android.graphics.Bitmap;

import me.xiaopan.sketch.request.ImageFrom;

public interface SketchDrawable {
    /**
     * 获取图片ID
     */
    String getKey();

    /**
     * 获取图片URI
     */
    String getUri();

    /**
     * 获取图片原始宽
     */
    int getOriginWidth();

    /**
     * 获取图片原始高
     */
    int getOriginHeight();

    /**
     * 获取图片类型
     */
    String getMimeType();

    /**
     * 获取图片方向
     */
    int getOrientation();

    /**
     * 获取占用内存，单位字节
     */
    int getByteCount();

    /**
     * 获取Bitmap配置
     */
    Bitmap.Config getBitmapConfig();

    /**
     * 获取图片来源
     */
    ImageFrom getImageFrom();

    /**
     * 设置图片来源
     */
    void setImageFrom(ImageFrom imageFrom);

    /**
     * 获取一些信息
     */
    String getInfo();
}

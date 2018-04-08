package com.util.utilslibrary;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.view.Surface;
import android.view.TextureView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Description:Camera1 的工具类
 * Created by 禽兽先生
 * Created on 2017/10/9
 */

public class Camera1Util {
    private static Camera1Util sCamera1Util;
    private Camera mCamera;
    //照片存储路径
    private String path = Environment.getExternalStorageDirectory() + "/DCIM/QSCamera";

    private Camera1Util() {
        //默认打开后摄像头
        openCamera(0);
    }

    public static Camera1Util getInstance() {
        if (sCamera1Util == null) {
            synchronized (Camera1Util.class) {
                if (sCamera1Util == null) {
                    sCamera1Util = new Camera1Util();
                }
            }
        }
        return sCamera1Util;
    }

    /**
     * Description:判断是否有相机
     * Date:2017/10/9
     */
    public boolean hasCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * Description:判断是否有闪光灯
     * Date:2017/10/9
     */
    public boolean hasCameraFlash(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    /**
     * Description:返回相机摄像头数量
     * Date:2017/10/11
     */
    public int getNumberOfCameras() {
        return Camera.getNumberOfCameras();
    }

    /**
     * Description:获取前摄像头的 Id
     * Date:2017/10/11
     */
    public int getFrontCameraId() {
        for (int i = 0; i < getNumberOfCameras(); i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Description:获取后摄像头的 Id
     * Date:2017/10/11
     */
    public int getBackCameraId() {
        for (int i = 0; i < getNumberOfCameras(); i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return i;
            }
        }
        return 0;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Description:设置拍照尺寸
     * Date:2017/10/9
     */
    public void setPictureSize(int width, int height) {
        if (mCamera != null) {
            Camera.Parameters mCameraParameters = mCamera.getParameters();
            //获取可支持的拍照尺寸
            List<Camera.Size> supportedPictureSizes = mCameraParameters.getSupportedPictureSizes();
            Camera.Size mCameraSize = supportedPictureSizes.get(supportedPictureSizes.size() - 1);
            for (Camera.Size cameraSize : supportedPictureSizes) {
                if (cameraSize.width * cameraSize.height > width * height) {
                    mCameraSize = cameraSize;
                    break;
                }
            }
            //设置拍照尺寸
            mCameraParameters.setPictureSize(mCameraSize.width, mCameraSize.height);
            mCamera.setParameters(mCameraParameters);
        }
    }

    /**
     * Description:设置拍照格式
     * Date:2017/10/9
     */
    public void setPictureFormat(int format) {
        if (mCamera != null) {
            Camera.Parameters mCameraParameters = mCamera.getParameters();
            if (mCameraParameters.getSupportedPictureFormats().contains(format)) {
                mCameraParameters.setPictureFormat(format);
            } else {
                mCameraParameters.setPictureFormat(ImageFormat.JPEG);
            }
            mCamera.setParameters(mCameraParameters);
        }
    }

    /**
     * Description:设置预览尺寸
     * Date:2017/10/9
     */
    public void setPreviewSize(int width, int height) {
        if (mCamera != null) {
            Camera.Parameters mCameraParameters = mCamera.getParameters();
            //获取可支持的预览尺寸
            List<Camera.Size> supportedPreviewSizes = mCameraParameters.getSupportedPreviewSizes();
            Camera.Size mCameraSize = supportedPreviewSizes.get(supportedPreviewSizes.size() - 1);
            for (Camera.Size cameraSize : supportedPreviewSizes) {
                if (cameraSize.width * cameraSize.height > width * height) {
                    mCameraSize = cameraSize;
                    break;
                }
            }
            //设置预览尺寸
            mCameraParameters.setPreviewSize(mCameraSize.width, mCameraSize.height);
            mCamera.setParameters(mCameraParameters);
        }
    }

    /**
     * Description:设置预览格式
     * Date:2017/10/9
     */
    public void setPreviewFormat(int format) {
        if (mCamera != null) {
            Camera.Parameters mCameraParameters = mCamera.getParameters();
            if (mCameraParameters.getSupportedPreviewFormats().contains(format)) {
                mCameraParameters.setPreviewFormat(format);
            } else {
                mCameraParameters.setPreviewFormat(ImageFormat.NV21);
            }
            mCamera.setParameters(mCameraParameters);
        }
    }

    /**
     * Description:设置对焦模式
     * Date:2017/10/9
     */
    public void setFocusMode(String focusMode) {
        if (mCamera != null) {
            Camera.Parameters mCameraParameters = mCamera.getParameters();
            //获取支持的对焦模式
            List<String> supportedFocusModes = mCameraParameters.getSupportedFocusModes();
            if (supportedFocusModes.contains(focusMode)) {
                mCameraParameters.setFocusMode(focusMode);
            } else {
                if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                }
            }
            mCamera.setParameters(mCameraParameters);
        }
    }

    /**
     * Description:设置场景模式
     * Date:2017/10/9
     */
    public void setSceneMode(String sceneMode) {
        if (mCamera != null) {
            Camera.Parameters mCameraParameters = mCamera.getParameters();
            //获取支持的场景模式
            List<String> supportedSceneModes = mCameraParameters.getSupportedSceneModes();
            if (supportedSceneModes.contains(sceneMode)) {
                mCameraParameters.setSceneMode(sceneMode);
            } else {
                if (supportedSceneModes.contains(Camera.Parameters.SCENE_MODE_AUTO)) {
                    mCameraParameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
                }
            }
            mCamera.setParameters(mCameraParameters);
        }
    }

    public void openCamera(int cameraId) {
        stopPreview();
        //获取相机设备
        mCamera = Camera.open(cameraId);

        setPictureSize(1080, 1920);
        setPictureFormat(ImageFormat.JPEG);

        setPreviewSize(1080, 1920);
        setPreviewFormat(ImageFormat.NV21);

        setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
    }

    /**
     * Description:开启预览
     * Date:2017/10/11
     */
    public void startPreview(final Activity activity, TextureView textureView) {
        if (!textureView.isAvailable()) {
            textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    //设置预览参数
                    setParameters(activity, surface, width, height);
                    //开启预览
                    mCamera.startPreview();
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    stopPreview();
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                }
            });
        } else {
            //设置预览参数
            setParameters(activity, textureView.getSurfaceTexture(), textureView.getMeasuredWidth(), textureView.getMeasuredHeight());
            //开启预览
            mCamera.startPreview();
        }
    }

    private void setParameters(Activity activity, SurfaceTexture surface, int width, int height) {
        //选择最合适的拍照尺寸
        Camera.Size mPictureSize = getBestSupportedSize(mCamera.getParameters().getSupportedPictureSizes(), width, height);
        //设置拍照尺寸
        setPictureSize(mPictureSize.width, mPictureSize.height);

        //选择最合适的预览尺寸
        Camera.Size mPreviewSize = getBestSupportedSize(mCamera.getParameters().getSupportedPreviewSizes(), width, height);
        //设置预览尺寸
        setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        try {
            //设置相机的预览视图
            mCamera.setPreviewTexture(surface);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //根据相机信息和 Activity 的横竖屏信息来设置预览方向
        Camera.CameraInfo mCameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(0, mCameraInfo);
        mCamera.setDisplayOrientation(getBestDisplayOrientation(activity, mCameraInfo));
    }

    /**
     * Description:在可支持的尺寸中获取最合适的尺寸
     * Date:2017/10/9
     */
    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height) {
        Camera.Size bestSize = sizes.get(sizes.size() - 1);
        int largestArea = width * height;
        for (Camera.Size size : sizes) {
            int area = size.width * size.height;
            if (area > largestArea) {
                bestSize = size;
                break;
            }
        }
        return bestSize;
    }

    /**
     * Description:根据屏幕方向和是前摄像头还是后摄像头来决定预览方向
     * Date:2017/10/11
     */
    private int getBestDisplayOrientation(Activity activity, Camera.CameraInfo cameraInfo) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (cameraInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (cameraInfo.orientation - degrees + 360) % 360;
        }
        return result;
    }

    /**
     * Description:拍照,通过回调返回拍摄结果
     * Date:2017/10/9
     */
    public void takePicture(final Context context, final OnTakePictureCallback onTakePictureCallback) {
        if (mCamera != null) {
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] bytes, Camera camera) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Matrix matrix = new Matrix();
                    int orientation = context.getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        matrix.setRotate(0);
                    } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        matrix.setRotate(90);
                    }
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    onTakePictureCallback.onPictureTaken(bitmap);
                }
            });
        }
    }

    /**
     * Description:保存照片
     * Date:2017/10/9
     */
    public void savePicture(Bitmap bitmap) {
        File directory = new File(Environment.getExternalStorageDirectory() + "/DCIM/QSCamera");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HH-mm-ss").format(new Date(System.currentTimeMillis()));
        String filePath = directory.getAbsolutePath() + "/" + timeStamp + ".jpg";
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(filePath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public interface OnTakePictureCallback {
        void onPictureTaken(Bitmap bitmap);
    }


//            mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
//            @Override
//            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//                //获取相机对象
//                mCamera = Camera.open(0);
//                //获取照相机参数
//                Camera.Parameters mCameraParameters = mCamera.getParameters();
//                //选择最合适的拍照尺寸
//                Camera.Size mPictureSize = getBestSupportedSize(mCamera.getParameters().getSupportedPictureSizes(), width, height);
//                //设置拍照尺寸
//                mCameraParameters.setPictureSize(mPictureSize.width, mPictureSize.height);
//                //设置拍照格式
//                if (mCameraParameters.getSupportedPictureFormats().contains(ImageFormat.JPEG)) {
//                    mCameraParameters.setPictureFormat(ImageFormat.JPEG);
//                }
//
//                //选择最合适的预览尺寸
//                Camera.Size mPreviewSize = getBestSupportedSize(mCamera.getParameters().getSupportedPreviewSizes(), width, height);
//                //设置预览尺寸
//                mCameraParameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
//                //设置预览格式
//                if (mCameraParameters.getSupportedPreviewFormats().contains(ImageFormat.NV21)) {
//                    mCameraParameters.setPreviewFormat(ImageFormat.NV21);
//                }
//
//                //获取支持的对焦模式
//                List<String> supportedFocusModes = mCameraParameters.getSupportedFocusModes();
//                if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
//                    mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//                }
//
//                //获取支持的场景模式
//                List<String> supportedSceneModes = mCameraParameters.getSupportedSceneModes();
//                if (supportedSceneModes.contains(Camera.Parameters.SCENE_MODE_AUTO)) {
//                    mCameraParameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
//                }
//
//                //设置照相机参数
//                mCamera.setParameters(mCameraParameters);
//                try {
//                    //设置预览视图
//                    mCamera.setPreviewTexture(surface);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //设置预览角度
//                Camera.CameraInfo mCameraInfo = new Camera.CameraInfo();
//                Camera.getCameraInfo(0, mCameraInfo);
//                mCamera.setDisplayOrientation(getBestDisplayOrientation(mCameraInfo));
//                //开启预览
//                mCamera.startPreview();
//            }
//
//            @Override
//            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//
//            }
//
//            @Override
//            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//                //释放相机
//                mCamera.release();
//                return false;
//            }
//
//            @Override
//            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//
//            }
//        });
//        findViewById(R.id.btn_take_photo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mCamera.takePicture(null, null, new Camera.PictureCallback() {
//                    @Override
//                    public void onPictureTaken(byte[] bytes, Camera camera) {
//                        //照片目录路径
//                        File directory = new File(Environment.getExternalStorageDirectory() + "/DCIM/QSCamera");
//                        if (!directory.exists()) {
//                            directory.mkdirs();
//                        }
//                        //时间戳
//                        String timeStamp = new SimpleDateFormat("yyyyMMdd-HH-mm-ss").format(new Date(System.currentTimeMillis()));
//                        //照片路径
//                        String filePath = directory.getAbsolutePath() + "/" + timeStamp + ".jpg";
//                        FileOutputStream fos = null;
//                        try {
//                            //写入文件
//                            fos = new FileOutputStream(filePath);
//                            fos.write(bytes, 0, bytes.length);
//                            fos.flush();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } finally {
//                            //关闭流
//                            if (fos != null) {
//                                try {
//                                    fos.close();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }
//                });
//
//            }
//        });


}

package com.util.utilslibrary;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Created by 禽兽先生
 * Created on 2017/9/27
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera2Util {
    private static final SparseIntArray ORIENTATION = new SparseIntArray();

    static {
        ORIENTATION.append(Surface.ROTATION_0, 180);
        ORIENTATION.append(Surface.ROTATION_90, 90);
        ORIENTATION.append(Surface.ROTATION_180, 0);
        ORIENTATION.append(Surface.ROTATION_270, 270);
    }

    private static Camera2Util sCamera2Util;

    private ImageReader mImageReader;
    private CameraCaptureSession mCameraCaptureSession;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private CameraDevice mCameraDevice;
    private CaptureRequest mCaptureRequest;
    private Handler mCameraHandler;
    private String path = Environment.getExternalStorageDirectory() + "/DCIM/QSCamera/";

    private Camera2Util() {
    }

    public static Camera2Util getInstance() {
        if (sCamera2Util == null) {
            synchronized (Camera2Util.class) {
                if (sCamera2Util == null) {
                    sCamera2Util = new Camera2Util();
                }
            }
        }
        return sCamera2Util;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Description:打开摄像头,最好是在 onStart() 内调用
     * Date:2017/9/27
     */
    public void openCamera(final Context context, final TextureView textureView, final String cameraId) {
        HandlerThread mCameraThread = new HandlerThread("CameraThread");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
        //获取摄像头的管理者 CameraManager
        final CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        //获取StreamConfigurationMap，它是管理摄像头支持的所有输出格式和尺寸
        StreamConfigurationMap mStreamConfigurationMap = getStreamConfigurationMap(context, cameraId);
        //获取相机支持的最大拍照尺寸
        Size mCaptureSize = getCaptureSize(mStreamConfigurationMap);
        //此ImageReader用于拍照所需,2 代表 ImageReader 中最多可以获取两帧图像流
        mImageReader = ImageReader.newInstance(mCaptureSize.getWidth(), mCaptureSize.getHeight(),
                ImageFormat.JPEG, 10);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                mCameraHandler.post(new ImageSaver(reader.acquireNextImage()));
            }
        }, mCameraHandler);
        if (!textureView.isAvailable()) {
            textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    //当SurfaceTexture可用的时候，设置相机参数并打开相机
                    try {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        manager.openCamera(cameraId, new MyStateCallback(context, cameraId, textureView, mCameraHandler), mCameraHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

                }
            });
        } else {
            startPreview(context, cameraId, textureView, mCameraHandler);
        }
    }

    private Size getCaptureSize(StreamConfigurationMap map) {
        return Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new Comparator<Size>() {
            @Override
            public int compare(Size lhs, Size rhs) {
                return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getHeight() * rhs.getWidth());
            }
        });
    }

    /**
     * Description:获取 StreamConfigurationMap ，它是管理摄像头支持的所有输出格式和尺寸
     * Date:2017/9/28
     */
    private StreamConfigurationMap getStreamConfigurationMap(Context context, String cameraId) {
        try {
            CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            return characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class MyStateCallback extends CameraDevice.StateCallback {
        private Context mContext;
        private String mCameraId;
        private TextureView mTextureView;
        private Handler mCameraHandler;

        public MyStateCallback(Context context, String cameraId, TextureView textureView, Handler cameraHandler) {
            this.mContext = context;
            this.mCameraId = cameraId;
            this.mTextureView = textureView;
            this.mCameraHandler = cameraHandler;
        }

        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            startPreview(mContext, mCameraId, mTextureView, mCameraHandler);
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            mCameraDevice = null;
        }
    }

    private void startPreview(Context context, String cameraId, TextureView textureView, final Handler handler) {
        StreamConfigurationMap mStreamConfigurationMap = getStreamConfigurationMap(context, cameraId);
        if (mStreamConfigurationMap == null) {
            return;
        }
        //根据 TextureView 尺寸设置预览尺寸
        Size mPreviewSize = getOptimalSize(mStreamConfigurationMap.getOutputSizes(SurfaceTexture.class), textureView.getWidth(), textureView.getHeight());
        SurfaceTexture mSurfaceTexture = textureView.getSurfaceTexture();
        mSurfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        Surface previewSurface = new Surface(mSurfaceTexture);
        try {
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRequestBuilder.addTarget(previewSurface);
            mCameraDevice.createCaptureSession(Arrays.asList(previewSurface, mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        mCaptureRequest = mCaptureRequestBuilder.build();
                        mCameraCaptureSession = session;
                        mCameraCaptureSession.setRepeatingRequest(mCaptureRequest, null, handler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, handler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * Description:选择sizeMap中大于并且最接近width和height的size
     * Date:2017/9/28
     */
    private Size getOptimalSize(Size[] sizeMap, int width, int height) {
        List<Size> sizeList = new ArrayList<>();
        for (Size option : sizeMap) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    sizeList.add(option);
                }
            } else {
                if (option.getWidth() > height && option.getHeight() > width) {
                    sizeList.add(option);
                }
            }
        }
        if (sizeList.size() > 0) {
            return Collections.min(sizeList, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });
        }
        return sizeMap[0];
    }

    private class ImageSaver implements Runnable {

        private Image mImage;

        ImageSaver(Image image) {
            mImage = image;
        }

        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            File mImageFile = new File(path);
            if (!mImageFile.exists()) {
                mImageFile.mkdir();
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd-HH-mm-ss").format(new Date(System.currentTimeMillis()));
            String fileName = path + timeStamp + ".jpg";
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(fileName);
                fos.write(data, 0, data.length);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void takePhoto(Context context, OnTakePhotoCallback onTakePhotoCallback) {
        try {
            this.mOnTakePhotoCallback = onTakePhotoCallback;
            mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
            mCameraCaptureSession.capture(mCaptureRequestBuilder.build()
                    , new MyCaptureCallback(context, mCaptureRequestBuilder, mCaptureRequest, mCameraHandler)
                    , mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private class MyCaptureCallback extends CameraCaptureSession.CaptureCallback {
        private Context context;
        private CaptureRequest.Builder captureRequestBuilder;
        private CaptureRequest captureRequest;
        private Handler cameraHandler;

        MyCaptureCallback(Context context, CaptureRequest.Builder captureRequestBuilder, CaptureRequest captureRequest, Handler cameraHandler) {
            this.context = context;
            this.captureRequestBuilder = captureRequestBuilder;
            this.captureRequest = captureRequest;
            this.cameraHandler = cameraHandler;
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            capture(context, captureRequestBuilder, captureRequest, cameraHandler);
        }
    }

    private void capture(final Context context, final CaptureRequest.Builder captureRequestBuilder, final CaptureRequest captureRequest, final Handler cameraHandler) {
        try {
            int orientation = context.getResources().getConfiguration().orientation;
            captureRequestBuilder.addTarget(mImageReader.getSurface());
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATION.get(orientation));
            CameraCaptureSession.CaptureCallback CaptureCallback = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (mOnTakePhotoCallback != null) {
                            mOnTakePhotoCallback.onError("拍照失败,没有读写内存卡权限");
                        }
                    } else {
                        if (mOnTakePhotoCallback != null) {
                            String timeStamp = new SimpleDateFormat("yyyyMMdd-HH-mm-ss").format(new Date(System.currentTimeMillis()));
                            String photoPath = path + timeStamp + ".jpg";
                            mOnTakePhotoCallback.onSuccess(photoPath);
                        }
                    }
                    unLockFocus(captureRequestBuilder, captureRequest, cameraHandler);
                }
            };
            mCameraCaptureSession.stopRepeating();
            mCameraCaptureSession.capture(captureRequestBuilder.build(), CaptureCallback, cameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void unLockFocus(CaptureRequest.Builder captureRequestBuilder, CaptureRequest captureRequest, Handler cameraHandler) {
        try {
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            mCameraCaptureSession.setRepeatingRequest(captureRequest, null, cameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Description:关闭摄像头,最好是在 onStop() 内调用
     * Date:2017/9/28
     */
    public void closeCamera() {
        if (mCameraCaptureSession != null) {
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }

        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }

        if (mImageReader != null) {
            mImageReader.close();
            mImageReader = null;
        }
    }

    /**
     * Description:获取后置摄像头的 ID
     * Date:2017/9/27
     */
    public String getBackCameraId(Context context) {
        CameraManager mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : mCameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                //此处默认打开后置摄像头
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK)
                    return cameraId;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * Description:获取前置摄像头的 ID
     * Date:2017/9/27
     */
    public String getFrontCameraId(Context context) {
        CameraManager mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : mCameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                //此处默认打开后置摄像头
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT)
                    return cameraId;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return "0";
    }

    private boolean flashAvailable(CameraManager cameraManager, String cameraId) {
        Boolean flashAvailable = false;
        try {
            CameraCharacteristics mCameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
            flashAvailable = mCameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return flashAvailable != null ? flashAvailable : false;
    }

    private OnTakePhotoCallback mOnTakePhotoCallback;

    public interface OnTakePhotoCallback {
        void onSuccess(String photoPath);

        void onError(String errorInfo);
    }

}

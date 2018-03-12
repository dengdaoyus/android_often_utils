package com.util.utilslibrary.photo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;


public class YCLTools {
    public static OnChoosePictureListener listener = null;
    private static YCLTools tools;

    public static final int MODE_UPLOAD_IMAGE_CAMERA = 920;

    public static final int MODE_UPLOAD_IMAGE_ABLUME = 921;

    private Activity activity;
    private static String YCL_FOLDER_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "ycl/";
    private String TEMP_PIC_NAME_JPG = "avatar.jpg";



    private Uri toTackPhotoUri;


    public static YCLTools getInstance() {
        if (tools == null) {
            tools = new YCLTools();
            return tools;
        } else {
            return tools;
        }
    }


    public void startChoose(Activity activity, int what,File file) {
        this.activity = activity;
        switch (what) {
            // 0 take photo
            case 0:
                toTackPhotoUri = Uri.fromFile(file);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, toTackPhotoUri);
                activity.startActivityForResult(intent, MODE_UPLOAD_IMAGE_CAMERA);
                break;
            // 1 choose from gallery
            case 1:
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activity.startActivityForResult(intent1, MODE_UPLOAD_IMAGE_ABLUME);
                break;
        }
    }

    public void upLoadImage(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Intent intent;
            switch (requestCode) {
                case YCLTools.MODE_UPLOAD_IMAGE_CAMERA:
                    intent = new Intent(activity, CropImageViewActivity.class );
                    intent.putExtra("photo_path", toTackPhotoUri.getPath());
                    activity.startActivity(intent);
                    break;
                case YCLTools.MODE_UPLOAD_IMAGE_ABLUME:
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    ContentResolver resolver = activity.getContentResolver();
                    Cursor cursor = resolver.query(selectedImage, filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                    }
                    final String path = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                    cursor.close();
                    intent = new Intent(activity, CropImageViewActivity.class);
                    intent.putExtra("photo_path", path);
                    activity.startActivity(intent);
                    break;
            }
        }
    }

    public void upLoadImage(int requestCode, int resultCode, Intent data,int type) {
        if (resultCode == Activity.RESULT_OK) {
            Intent intent;
            switch (requestCode) {
                case YCLTools.MODE_UPLOAD_IMAGE_CAMERA:
                    intent = new Intent(activity, CropImageViewActivity.class );
                    intent.putExtra("photo_path", toTackPhotoUri.getPath());
                    intent.putExtra("type", type );
                    activity.startActivity(intent);
                    break;
                case YCLTools.MODE_UPLOAD_IMAGE_ABLUME:
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    ContentResolver resolver = activity.getContentResolver();
                    Cursor cursor = resolver.query(selectedImage, filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                    }
                    final String path = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                    cursor.close();
                    intent = new Intent(activity, CropImageViewActivity.class);
                    intent.putExtra("photo_path", path);
                    intent.putExtra("type", type );
                    activity.startActivity(intent);
                    break;
            }
        }
    }

    public void setOnChoosePictureListener(OnChoosePictureListener l) {
        listener = l;
    }


}

package com.util.utilslibrary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 保存图片异步任务
 */
public class SaveResImageAsyncTask extends AsyncTask<String, Integer, String> {

    private Context context;
    private int imageId;

    public SaveResImageAsyncTask(Context context, int imageId) {
        this.context = context;
        this.imageId = imageId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        File dir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = new File(Environment.getExternalStorageDirectory(), "download");
        }
        if (dir == null) {
            dir = context.getFilesDir();
        }
        if (dir == null) {
            return "无法获取目录";
        } else if (!dir.exists()) {
            dir.mkdirs();
        }

        File outImageFile = new File(dir, imageId + ".png");
        try {
            outImageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return "创建文件失败，请重试";
        }

        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            outputStream = new FileOutputStream(outImageFile);
            inputStream = context.getResources().openRawResource(imageId);
            byte[] data = new byte[1024];
            int length;
            while ((length = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "保存图片失败，请重试";
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outImageFile)));
        return "已保存到 " + outImageFile.getParentFile().getPath() + "目录";
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }
}
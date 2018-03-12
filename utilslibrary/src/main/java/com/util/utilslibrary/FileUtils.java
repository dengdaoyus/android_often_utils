package com.util.utilslibrary;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;

/**
 * 文件处理工具
 */
public class FileUtils {

    /**
     * 缓存文件位置设置
     */
    private static final String cacheDir = "/Android/data/net.bodecn";

    /**
     * 文件缓存地址
     *
     * @param context   上下文
     * @param cacheName 缓存文件名
     * @return 缓存文件
     */
    public static File cacheFile(Context context, String cacheName) {
        File fileDir = null;
        // 判断是否有内置SD卡存在
        if (isExistSD())
            fileDir = new File(Environment.getExternalStorageDirectory(),
                    String.format("%s/%s", cacheDir, cacheName));
        // 判断文件夹是否存在
        if (fileDir == null || !fileDir.exists() && !fileDir.mkdirs())
            fileDir = context.getCacheDir();

        return fileDir;
    }

    public static String getCachePath(String cacheName) {
        return Environment.getExternalStorageDirectory() +
                String.format("%s/%s", cacheDir, cacheName);
    }

    /**
     * 判断SD卡是否存在
     *
     * @return true 存在 false 不存在
     */
    public static boolean isExistSD() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取Assets下的json文件
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return json字符串
     */
    public static String getAssetsJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf =
                    new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 删除文件夹
     *
     * @param dirFile 文件夹对象
     * @return 是否成功 成功 true 失败 false
     */
    public static boolean deleteDirectory(File dirFile) {
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return true;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                flag = deleteFile(file);
                if (!flag)
                    break;
            } // 删除子目录
            else {
                flag = deleteDirectory(file);
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前目录
        return "images".equals(dirFile.getName()) || dirFile.delete();
    }

    /**
     * 删除单个文件
     *
     * @return 单个文件删除成功返回true，否则返回false
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean deleteFile(File file) {
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }

    /**
     * 获取SD path
     */
    public static String getSDPath() {
        File sdDir;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        }
        return null;
    }

    /**
     * 将文件按时间升排列序
     */
    static class FileComparator2 implements Comparator<File> {

        @Override
        public int compare(File file1, File file2) {
            if (file1.lastModified() < file2.lastModified()) {
                return -1;// 最后修改的文件在前
            } else {
                return 1;
            }
        }
    }

}

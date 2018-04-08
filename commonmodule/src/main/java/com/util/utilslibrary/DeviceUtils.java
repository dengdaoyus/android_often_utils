package com.util.utilslibrary;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/**
 * 手机设备工具
 * Created by Administrator on 2017/6/7 0007.
 */

public class DeviceUtils {
    private DeviceUtils() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    /**
     * 获取设备MAC地址
     * <p>需添加权限<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     */
    public static String getMacAddress(Context context) {
        String macAddress;
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        macAddress = info.getMacAddress();
        if (null == macAddress) {
            return "";
        }
        macAddress = macAddress.replace(":", "");
        return macAddress;
    }

    /**
     * 获取设备厂商，如Xiaomi
     */
    public static String getManufacturer() {
        String MANUFACTURER = Build.MANUFACTURER;
        return MANUFACTURER;
    }

    /**
     * 获取设备型号，如MI2SC
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }
    /**
     * 获取CPU核心数
     *
     * @return CPU核心数
     */
    public static int getNumCores() {
        try {
            // 获取本地CPU信息文件
            File dir = new File("/sys/devices/system/cpu/");
            // 过滤内部文件集
            File[] files = dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    // 检查文件名匹配cpu0~cpu9
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }

            });
            // 返回cpu核心数
            return files.length;
        }
        catch (Exception e) {
            // 异常返回1个核心
            return 1;
        }
    }

    /**
     * 获取版本号和版本名
     *
     * @param context
     *            上下文
     * @return 版本号版本名字的数组
     */
    public static String[] getVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo =
                    pm.getPackageInfo(context.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            String[] strs = new String[3];
            strs[0] = packageInfo.packageName;
            strs[1] = String.valueOf(packageInfo.versionCode);
            strs[2] = packageInfo.versionName;
            return strs;
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
